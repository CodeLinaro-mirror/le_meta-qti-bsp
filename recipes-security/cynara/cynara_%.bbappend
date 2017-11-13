
FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append += "file://0001-cynara-Add-a-way-to-configure-offline-output-dir.patch"

# Enable cynara.service by default
inherit systemd
SYSTEMD_SERVICE_${PN} = "cynara.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Depends on qemu for offline configure
DEPENDS += " qemu-native"
PACKAGE_WRITE_DEPS += "qemu-native"
inherit qemu

pkg_postinst_${PN} () {
   # Fail on error.
   set -e

   # Run the code below while building an image,
   if [ x"$D" != "x" ]; then
      mkdir -p $D${localstatedir}/cynara
      chsmack -a System $D${localstatedir}/cynara

      VERSION=${@bb.data.getVar('PV',d,1).split('+git')[0]}
      STATE_PATH='/var/cynara'
      DB_DIR='db'
      INDEX_NAME='buckets'
      DEFAULT_BUCKET_NAME='_'
      CHECKSUM_NAME='checksum'
      DENY_POLICY=';0x0;'

      CYNARA_USER='cynara'
      CYNARA_GROUP='cynara'
      SMACK_LABEL='System'

      # Upgrade
      echo "NOTE: offline install updating cynara DB from 0.0.0 to version $VERSION"
      if [ ! -d "$D${STATE_PATH}/${DB_DIR}" ]; then
          # Create Cynara's database directory
          mkdir -p $D${STATE_PATH}/${DB_DIR}

          # Create contents of minimal database: first index file, then default bucket
          echo  ${DENY_POLICY} > $D${STATE_PATH}/${DB_DIR}/${INDEX_NAME}
          touch $D${STATE_PATH}/${DB_DIR}/${DEFAULT_BUCKET_NAME}

          # Set proper permissions for newly created database
          chown -R ${CYNARA_USER}:${CYNARA_GROUP} $D${STATE_PATH}/${DB_DIR}

          # Set proper SMACK labels for newly created database
          chsmack -a ${SMACK_LABEL} $D${STATE_PATH}/${DB_DIR}
          chsmack -a ${SMACK_LABEL} $D${STATE_PATH}/${DB_DIR}/*

          # Output file
          CHECKSUMS="$D${STATE_PATH}/${DB_DIR}/${CHECKSUM_NAME}"
          WILDCARD="*"

          # Mimic opening in truncate mode
          echo -n "" > "${CHECKSUMS}"

          # Actual checksums generation
          for POLICYFILE in $(find $D${STATE_PATH}/${DB_DIR}/${WILDCARD} -type f ! -name "${CHECKSUM_NAME}*" ); do
              CHECKSUM=""
              # MD5 0.10.0, VERSION 0.11.0
              PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
                  $D${sbindir}/cynara-db-chsgen ${POLICYFILE} -a md5 >> ${CHECKSUMS}
          done

          # Set proper permissions for newly created checksum file
          chown -R ${CYNARA_USER}:${CYNARA_GROUP} ${CHECKSUMS}

          # Set proper SMACK label for newly created checksum file
          chsmack -a ${SMACK_LABEL} ${CHECKSUMS}
          fi
   else

          # Fallback to online configure

          mkdir -p $D${sysconfdir}/cynara
          ${CHSMACK} -a System $D${sysconfdir}/cynara

          # Strip git patch level information, the version comparison code
          # in cynara-db-migration only expect major.minor.patch version numbers.
          VERSION=${@bb.data.getVar('PV',d,1).split('+git')[0]}
          if [ -d $D${localstatedir}/cynara ] ; then
          # upgrade
          echo "NOTE: updating cynara DB to version $VERSION"
          $D${sbindir}/cynara-db-migration upgrade -f 0.0.0 -t $VERSION
          else
          # install
          echo "NOTE: creating cynara DB for version $VERSION"
          mkdir -p $D${localstatedir}/cynara
          ${CHSMACK} -a System $D${localstatedir}/cynara
          $D${sbindir}/cynara-db-migration install -t $VERSION
          fi

          # Workaround for systemd.bbclass issue: it would call
          # "systemctl start" without "--no-block", but because
          # the service is not ready to run at the time when
          # this scripts gets executed by run-postinsts.service,
          # booting deadlocks.
          echo "NOTE: enabling and starting cynara service"
          systemctl enable cynara
          systemctl start --no-block cynara
   fi
   exit 0
}
