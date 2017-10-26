DEPENDS += " qemu-native"
PACKAGE_WRITE_DEPS += "qemu-native"
inherit qemu
pkg_postinst_${PN}-policy () {
   if [ x"$D" = "x" ] && ${bindir}/security-manager-policy-reload; then
       exit 0
   else

   POLICY_PATH=$D/usr/share/security-manager/policy

   # Create default buckets
   while read bucket default_policy
   do
       # Reuse the primary bucket for PRIVACY_MANAGER bucket
       [ "$bucket" = "PRIVACY_MANAGER" ] && bucket=""
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/  $D${sbindir}/cyad --set-bucket="$bucket" --type="$default_policy"
   done <<END
PRIVACY_MANAGER DENY
ADMIN NONE
MAIN DENY
MANIFESTS DENY
END

   # Link buckets together
   while read bucket_src bucket_dst
   do
       # Reuse the main bucket for PRIVACY_MANAGER bucket
       [ "$bucket_src" = "PRIVACY_MANAGER" ] && bucket_src=""
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} -E CYNARA_POLICY_PATH=$D/var/cynara/db/ \
             $D${sbindir}/cyad --set-policy --client="*" --user="*" --privilege="*" --type=BUCKET \
             --bucket="$bucket_src" --metadata="$bucket_dst"
   done <<END
MAIN MANIFESTS
PRIVACY_MANAGER MAIN
END

   # Import user-type policies
   find "$POLICY_PATH" -name "usertype-*.profile" |
   while read file
   do
       bucket="`echo $file | sed -r 's|.*/usertype-(.*).profile$|USER_TYPE_\1|' | tr '[:lower:]' '[:upper:]'`"

       # Re-create the bucket with empty contents
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/ $D${sbindir}/cyad --delete-bucket=$bucket || true
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/ $D${sbindir}/cyad --set-bucket=$bucket --type=DENY

       # Link the bucket to ADMIN bucket
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/ $D${sbindir}/cyad --set-policy --client="*" --user="*" --privilege="*" --type=BUCKET \
       --bucket="$bucket" --metadata="ADMIN"

       grep -v ^\' $file |
       while read app privilege
       do
       user="*"        # Match any user id
       policy="0xFFFF" # ALLOW (FIXME: cyad should parse policy names, not numeric values)
       printf '%s;%s;%s;%s;%s;\n' "$bucket" "$user" "$app" "$privilege" "$policy"
       done |
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/ $D${sbindir}/cyad --set-policy --bulk=-
   done


   # Non-application programs get access to all privileges
   for client in User System
   do
       PSEUDO_UNLOAD=1 qemuwrapper -L $D -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir} \
       -E CYNARA_POLICY_PATH=$D/var/cynara/db/ $D${sbindir}/cyad --set-policy --bucket=MANIFESTS --client="$client" --user="*" --privilege="*" --type=ALLOW
   done

   if [ ! -e "$D/var/db/security-manager" ]; then
        mkdir -p $D/var/db
        cp -ra $D/usr/dbspace/ $D/var/db/security-manager
   fi

   if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
       if [ -n "$D" ]; then
           OPTS="--root=$D"
       fi
       systemctl $OPTS disable init-security-manager-db.service
   fi

   exit 0
   fi
}
