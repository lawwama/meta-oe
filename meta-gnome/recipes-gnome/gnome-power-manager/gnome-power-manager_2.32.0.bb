DESCRIPTION = "Power management daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PR = "r1"

DEPENDS = "glib-2.0 gtk+ gconf gnome-keyring dbus dbus-glib libnotify libwnck cairo libunique xrandr virtual/libx11 libxrender libcanberra upower"

inherit gnome
SRC_URI[archive.md5sum] = "9a08e85dce3ffb90775f15e3bda4adda"
SRC_URI[archive.sha256sum] = "17fa301bf7e133285c0e054ae3be2b0f690c48f59b09f67e04d6ed339b330476"

EXTRA_OECONF = " --disable-scrollkeeper \
                 --disable-keyring \
                 --disable-applets \
                 --x-includes=${STAGING_INCDIR} \
                 --x-libraries=${STAGING_LIBDIR} \
                 --with-dpms-ext=${STAGING_INCDIR}/.. \
                 --enable-compile-warnings=no \
                 ac_cv_header_X11_extensions_dpms_h=yes \
               "

do_configure_prepend() {
        sed -i -e 's:	man	::g' ${S}/Makefile.am
}

do_configure_append() {
        rm config.log
        # Sigh... --enable-compile-warnings=no doesn't actually turn off -Werror
        for i in $(find ${S} -name "Makefile") ; do
            sed -i -e s:-Werror::g $i
        done
		sed -e "s/libtool --/${TARGET_SYS}-libtool --/" -i ${S}/src/Makefile
}

PACKAGES =+ "${PN}-applets"

FILES_${PN}-applets = "${bindir}/*applet* \
                       ${libdir}/bonobo/servers \
		       ${datadir}/gnome-2.0/ui"

FILES_${PN} += "${datadir}/icons \
                ${datadir}/dbus-1 \
		${datadir}/gnome/autostart \
		"

FILES_${PN}-doc += "${datadir}/omf \
                    ${datadir}/gnome/help "

