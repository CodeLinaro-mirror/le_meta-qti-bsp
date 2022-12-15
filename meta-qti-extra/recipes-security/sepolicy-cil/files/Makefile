MLS_SENS=1
MLS_CATS=1024
PRIVATE_TGT_WITH_NATIVE_COVERAGE=false
PRIVATE_SEPOLICY_SPLIT=true
PRIVATE_COMPATIBLE_PROPERTY=true
my_target_arch=arm64
with_asan=false
treble_sysprop_neverallow=true
with_native_coverage=false
PRODUCT_SEPOLICY_SPLIT=true
PRODUCT_COMPATIBLE_PROPERTY=true
WITH_DEXPREOPT=true
BOARD_SEPOLICY_VERS=30.0
PLATFORM_SEPOLICY_VERSION=30.0
POLICYVERS=30

PRIVATE_MLS_SENS := $(MLS_SENS)
PRIVATE_MLS_CATS := $(MLS_CATS)
PRIVATE_TARGET_BUILD_VARIANT := $(TARGET_BUILD_VARIANT)
PRIVATE_TGT_ARCH := $(my_target_arch)
PRIVATE_TGT_WITH_ASAN := $(with_asan)
PRIVATE_TGT_WITH_NATIVE_COVERAGE := $(with_native_coverage)
PRIVATE_ADDITIONAL_M4DEFS := $(LOCAL_ADDITIONAL_M4DEFS)
PRIVATE_SEPOLICY_SPLIT := $(PRODUCT_SEPOLICY_SPLIT)
PRIVATE_COMPATIBLE_PROPERTY := $(PRODUCT_COMPATIBLE_PROPERTY)
PRIVATE_TREBLE_SYSPROP_NEVERALLOW := $(treble_sysprop_neverallow)

VERSIONPOLICY = ./version_policy

PLAT_PUBLIC_POLICY := ${SYSTEM_SEPOLICY}/public
PLAT_PRIVATE_POLICY := ${SYSTEM_SEPOLICY}/private
SYSTEM_EXT_PUBLIC_POLICY := ${DEVICE_SEPOLICY}/generic/public ${DEVICE_SEPOLICY}/qva/public
SYSTEM_EXT_PRIVATE_POLICY := ${DEVICE_SEPOLICY}/generic/private ${DEVICE_SEPOLICY}/qva/private
PRODUCT_PUBLIC_POLICY := ${PACKAGES_POLICY}/car_product/sepolicy/public ${PACKAGES_POLICY}/watchdog/sepolicy/public ${DEVICE_SEPOLICY}/generic/product/public ${DEVICE_SEPOLICY}/qva/product/public
PRODUCT_PRIVATE_POLICY := ${PACKAGES_POLICY}/car_product/sepolicy/private ${PACKAGES_POLICY}/watchdog/sepolicy/private ${DEVICE_SEPOLICY}/generic/product/private ${DEVICE_SEPOLICY}/qva/product/private
REQD_MASK_POLICY := ${SYSTEM_SEPOLICY}/reqd_mask
PLAT_VENDOR_POLICY := ${SYSTEM_SEPOLICY}/vendor

ifeq (user, $(TARGET_BUILD_VARIANT))
BOARD_VENDOR_SEPOLICY_DIRS := ${DEVICE_SEPOLICYVNDR}/generic/vendor/common ${DEVICE_SEPOLICYVNDR}/qva/vendor/common/sysmonapp \
  ${DEVICE_SEPOLICYVNDR}/qva/vendor/ssg ${DEVICE_SEPOLICYVNDR}/qva/vendor/common ${DEVICE_SEPOLICYVNDR}/generic/vendor/gen3_metal ${DEVICE_SEPOLICYVNDR}/qva/vendor/gen3_metal
else
BOARD_VENDOR_SEPOLICY_DIRS := ${PACKAGES_POLICY}/car_product/sepolicy/test ${PACKAGES_POLICY}/watchdog/testclient/sepolicy ${DEVICE_SEPOLICYVNDR}/generic/vendor/common ${DEVICE_SEPOLICYVNDR}/qva/vendor/common/sysmonapp \
  ${DEVICE_SEPOLICYVNDR}/qva/vendor/ssg ${DEVICE_SEPOLICYVNDR}/qva/vendor/common ${DEVICE_SEPOLICYVNDR}/generic/vendor/gen3_metal ${DEVICE_SEPOLICYVNDR}/qva/vendor/gen3_metal ${DEVICE_SEPOLICYVNDR}/generic/vendor/test \
  ${DEVICE_SEPOLICYVNDR}/qva/vendor/test
endif


sepolicy_build_cil_workaround_files := technical_debt.cil
sepolicy_build_files := security_classes \
                        initial_sids \
                        access_vectors \
                        global_macros \
                        neverallow_macros \
                        mls_macros \
                        mls_decl \
                        mls \
                        policy_capabilities \
                        te_macros \
                        attributes \
                        ioctl_defines \
                        ioctl_macros \
                        *.te \
                        roles_decl \
                        roles \
                        users \
                        initial_sid_contexts \
                        fs_use \
                        genfs_contexts \
                        port_contexts

###########################################################
# Command to turn collection of policy files into a policy.conf file to be
# processed by checkpolicy
###########################################################
define transform-policy-to-conf
@mkdir -p $(dir $@)
$(M4) --fatal-warnings $(PRIVATE_ADDITIONAL_M4DEFS) \
	-D mls_num_sens=$(PRIVATE_MLS_SENS) -D mls_num_cats=$(PRIVATE_MLS_CATS) \
	-D target_build_variant=$(PRIVATE_TARGET_BUILD_VARIANT) \
	-D target_with_dexpreopt=$(WITH_DEXPREOPT) \
	-D target_arch=$(PRIVATE_TGT_ARCH) \
	-D target_with_asan=$(PRIVATE_TGT_WITH_ASAN) \
	-D target_with_native_coverage=$(PRIVATE_TGT_WITH_NATIVE_COVERAGE) \
	-D target_full_treble=$(PRIVATE_SEPOLICY_SPLIT) \
	-D target_compatible_property=$(PRIVATE_COMPATIBLE_PROPERTY) \
	-D target_treble_sysprop_neverallow=$(PRIVATE_TREBLE_SYSPROP_NEVERALLOW) \
	-D target_exclude_build_test=$(PRIVATE_EXCLUDE_BUILD_TEST) \
	-D target_requires_insecure_execmem_for_swiftshader=$(PRODUCT_REQUIRES_INSECURE_EXECMEM_FOR_SWIFTSHADER) \
	$(PRIVATE_TGT_RECOVERY) \
	-s $(PRIVATE_POLICY_FILES) > $@
endef

###########################################################
# Compute policy files to be used in policy build.
# $(1): files to include
# $(2): directories in which to find files
###########################################################
define build_policy
$(foreach type, $(1), $(foreach file, $(addsuffix /$(type), $(2)), $(sort $(wildcard $(file)))))
endef

all: product_sepolicy.cil plat_mapping_file.cil system_ext_mapping_file.cil product_mapping_file.cil vendor_sepolicy.cil


###########################################################
# version_policy - Takes the given public platform policy, a private policy
# and a version number to produced a combined "versioned" policy file.
###########################################################
version_policy: version_policy.o
	${CC} $< $(VERSION_POLICY_LIB_PATH) $(VERSION_POLICY_LIB_NAME) -o $@

version_policy.o: version_policy.c
	${CC} $(VERSION_POLICY_INCLUDES) -c $<


###########################################################
# plat_policy.conf - A combination of the private and public platform policy
# which will ship with the device.  The platform will always reflect the most
# recent platform version and is not currently being attributized.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(PLAT_PRIVATE_POLICY))
plat_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
plat_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

PRIVATE_ADDITIONAL_CIL_FILES := \
  $(call build_policy, $(sepolicy_build_cil_workaround_files), $(PLAT_PRIVATE_POLICY))
plat_sepolicy.cil: plat_policy.conf $(CHECKPOLICY)
	$(CHECKPOLICY) -M -C -c $(POLICYVERS) -o $@.tmp $<
	cat $(PRIVATE_ADDITIONAL_CIL_FILES) >> $@.tmp
	mv $@.tmp $@

###########################################################
# system_ext_policy.conf - A combination of the private and public system_ext policy
# which will ship with the device. System_ext policy is not attributized.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(PLAT_PRIVATE_POLICY) \
  $(SYSTEM_EXT_PUBLIC_POLICY) $(SYSTEM_EXT_PRIVATE_POLICY))
system_ext_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
system_ext_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

system_ext_sepolicy.cil: system_ext_policy.conf plat_sepolicy.cil $(CHECKPOLICY)
	$(CHECKPOLICY) -M -C -c $(POLICYVERS) -o $@.tmp $<
	grep -Fxvf plat_sepolicy.cil $@.tmp > $@.tmp1
	grep -v ';;' $@.tmp1 > $@.tmp2
	mv $@.tmp2 $@

###########################################################
# product_policy.conf - A combination of the private and public product policy
# which will ship with the device. Product policy is not attributized.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(PLAT_PRIVATE_POLICY) \
  $(SYSTEM_EXT_PUBLIC_POLICY) $(SYSTEM_EXT_PRIVATE_POLICY) \
  $(PRODUCT_PUBLIC_POLICY) $(PRODUCT_PRIVATE_POLICY))
product_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
product_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

product_sepolicy.cil: product_policy.conf plat_sepolicy.cil system_ext_sepolicy.cil $(CHECKPOLICY)
	$(CHECKPOLICY) -M -C -c $(POLICYVERS) -o $@.tmp $<
	grep -Fxvf plat_sepolicy.cil $@.tmp > $@.tmp1
	grep -Fxvf system_ext_sepolicy.cil $@.tmp1 > $@.tmp2
	grep -v ';;' $@.tmp2 > $@.tmp3
	mv $@.tmp3 $@


###########################################################
# reqd_policy_mask - a policy.conf file which contains only the bare minimum
# policy necessary to use checkpolicy.  This bare-minimum policy needs to be
# present in all policy.conf files, but should not necessarily be exported as
# part of the public policy.  The rules generated by reqd_policy_mask will allow
# the compilation of public policy and subsequent removal of CIL policy that
# should not be exported.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), $(REQD_MASK_POLICY))
reqd_policy_mask.conf: PRIVATE_POLICY_FILES := $(policy_files)
reqd_policy_mask.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

reqd_policy_mask.cil: reqd_policy_mask.conf $(CHECKPOLICY)
	$(CHECKPOLICY) -C -M -c $(POLICYVERS) -o $@ $<

###########################################################
# pub_policy - policy that will be exported to be a part of non-platform
# policy corresponding to this platform version.  This is a limited subset of
# policy that would not compile in checkpolicy on its own.  To get around this
# limitation, add only the required files from private policy, which will
# generate CIL policy that will then be filtered out by the reqd_policy_mask.
#
# There are three pub_policy.cil files below:
#   - pub_policy.cil: exported 'product', 'system_ext' and 'system' policy.
#   - system_ext_pub_policy.cil: exported 'system_ext' and 'system' policy.
#   - plat_pub_policy.cil: exported 'system' policy.
#
# Those above files will in turn be used to generate the following versioned cil files:
#   - product_mapping_file: the versioned, exported 'product' policy in product partition.
#   - system_ext_mapping_file: the versioned, exported 'system_ext' policy in system_ext partition.
#   - plat_mapping_file: the versioned, exported 'system' policy in system partition.
#   - plat_pub_versioned.cil: the versioned, exported 'product', 'system_ext' and 'system'
#                             policy in vendor partition.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(SYSTEM_EXT_PUBLIC_POLICY) $(PRODUCT_PUBLIC_POLICY) $(REQD_MASK_POLICY))
pub_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
pub_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

pub_policy.cil: PRIVATE_POL_CONF := pub_policy.conf
pub_policy.cil: PRIVATE_REQD_MASK := reqd_policy_mask.cil
pub_policy.cil: $(CHECKPOLICY) pub_policy.conf reqd_policy_mask.cil
	$< -C -M -c $(POLICYVERS) -o $@.tmp $(PRIVATE_POL_CONF)
	grep -Fxvf $(PRIVATE_REQD_MASK) $@.tmp > $@

###########################################################
# plat_pub_versioned.cil - the exported platform policy associated with the version
# that non-platform policy targets.
###########################################################
plat_pub_versioned.cil : PRIVATE_VERS := $(BOARD_SEPOLICY_VERS)
plat_pub_versioned.cil : PRIVATE_TGT_POL := pub_policy.cil
plat_pub_versioned.cil : pub_policy.cil version_policy
	${VERSIONPOLICY} -b $< -t $(PRIVATE_TGT_POL) -n $(PRIVATE_VERS) -o $@

###########################################################
# plat_pub_policy.cil: exported 'system' policy.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(REQD_MASK_POLICY))
plat_pub_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
plat_pub_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

plat_pub_policy.cil: PRIVATE_POL_CONF := plat_pub_policy.conf
plat_pub_policy.cil: PRIVATE_REQD_MASK := reqd_policy_mask.cil
plat_pub_policy.cil: $(CHECKPOLICY) plat_pub_policy.conf reqd_policy_mask.cil
	$< -C -M -c $(POLICYVERS) -o $@.tmp $(PRIVATE_POL_CONF)
	grep -Fxvf $(PRIVATE_REQD_MASK) $@.tmp > $@

###########################################################
# plat_mapping_file.cil: the mapping file for current platform policy, since it needs to
# track platform policy development
###########################################################
plat_mapping_file.cil : PRIVATE_VERS := $(PLATFORM_SEPOLICY_VERSION)
plat_mapping_file.cil : plat_pub_policy.cil $(VERSIONPOLICY)
	$(VERSIONPOLICY) -b $< -m -n $(PRIVATE_VERS) -o $@

###########################################################
# system_ext_pub_policy.cil: exported 'system_ext' and 'system' policy.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(SYSTEM_EXT_PUBLIC_POLICY) $(REQD_MASK_POLICY))
system_ext_pub_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
system_ext_pub_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

system_ext_pub_policy.cil: PRIVATE_POL_CONF := system_ext_pub_policy.conf
system_ext_pub_policy.cil: PRIVATE_REQD_MASK := reqd_policy_mask.cil
system_ext_pub_policy.cil: $(CHECKPOLICY) system_ext_pub_policy.conf reqd_policy_mask.cil
	$< -C -M -c $(POLICYVERS) -o $@.tmp $(PRIVATE_POL_CONF)
	grep -Fxvf $(PRIVATE_REQD_MASK) $@.tmp > $@

###########################################################
# system_ext_mapping_file.cil: the versioned, exported 'system_ext' policy in system_ext partition.
###########################################################
system_ext_mapping_file.cil : PRIVATE_VERS := $(PLATFORM_SEPOLICY_VERSION)
system_ext_mapping_file.cil : PRIVATE_PLAT_MAPPING_CIL := plat_mapping_file.cil
system_ext_mapping_file.cil : system_ext_pub_policy.cil $(VERSIONPOLICY) plat_mapping_file.cil
	$(VERSIONPOLICY) -b $< -m -n $(PRIVATE_VERS) -o $@.tmp
	grep -Fxvf $(PRIVATE_PLAT_MAPPING_CIL) $@.tmp > $@

###########################################################
# product_mapping_file.cil: the versioned, exported 'product' policy in product partition
###########################################################
product_mapping_file.cil : PRIVATE_VERS := $(PLATFORM_SEPOLICY_VERSION)
product_mapping_file.cil : pub_policy.cil $(VERSIONPOLICY) system_ext_mapping_file.cil plat_mapping_file.cil
	$(VERSIONPOLICY) -b $< -m -n $(PRIVATE_VERS) -o $@.tmp
	grep -Fxvf plat_mapping_file.cil $@.tmp > $@.tmp1
	grep -Fxvf system_ext_mapping_file.cil $@.tmp1 > $@

###########################################################
# vendor_policy.cil - the vendor sepolicy. This needs attributization and to be combined
# with the platform-provided policy.  It makes use of the reqd_policy_mask files from private
# policy and the platform public policy files in order to use checkpolicy.
###########################################################
policy_files := $(call build_policy, $(sepolicy_build_files), \
  $(PLAT_PUBLIC_POLICY) $(SYSTEM_EXT_PUBLIC_POLICY) $(PRODUCT_PUBLIC_POLICY) \
  $(REQD_MASK_POLICY) $(PLAT_VENDOR_POLICY) $(BOARD_VENDOR_SEPOLICY_DIRS))

vendor_policy.conf: PRIVATE_POLICY_FILES := $(policy_files)
vendor_policy.conf: $(policy_files) $(M4)
	$(transform-policy-to-conf)

vendor_sepolicy.cil: PRIVATE_REQD_MASK := reqd_policy_mask.cil
vendor_sepolicy.cil: PRIVATE_VERS := $(BOARD_SEPOLICY_VERS)
vendor_sepolicy.cil: $(CHECKPOLICY) $(VERSIONPOLICY) vendor_policy.conf pub_policy.cil plat_pub_versioned.cil reqd_policy_mask.cil
	$< -C -M -c $(POLICYVERS) -o $@.tmp1 vendor_policy.conf
	grep -Fxvf $(PRIVATE_REQD_MASK) $@.tmp1 > $@.raw
	$(VERSIONPOLICY) -b pub_policy.cil -n $(PRIVATE_VERS) -t $@.raw -o $@.tmp2
	grep -Fxvf plat_pub_versioned.cil $@.tmp2 > $@
