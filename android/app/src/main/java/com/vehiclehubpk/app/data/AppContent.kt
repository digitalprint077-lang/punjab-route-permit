package com.vehiclehubpk.app.data

import androidx.annotation.DrawableRes
import com.vehiclehubpk.app.BuildConfig
import com.vehiclehubpk.app.R

data class ServiceItem(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int,
    val officialUrl: String,
    val guidePath: String? = null,
)

data class Province(
    val id: String,
    val name: String,
    val badge: String,
    val description: String,
    val authorityName: String,
    val guidePath: String,
    val portalUrl: String,
    val services: List<ServiceItem>,
)

data class GuideItem(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int,
    val guidePath: String,
    val officialUrl: String,
    val authorityName: String = "",
)

data class LicencePortal(
    val id: String,
    val region: String,
    val title: String,
    val description: String,
    val authorityName: String,
    val portalUrl: String,
    val verifyUrl: String,
    val guideAnchor: String,
)

data class ContentSnapshot(
    val guides: List<GuideItem>,
    val provinces: List<Province>,
    val licences: List<LicencePortal>,
) {
    fun provinceById(id: String): Province? = provinces.find { it.id == id }
}

object AppContent {
    const val SITE_BASE = BuildConfig.SITE_URL
    const val LICENCE_GUIDE_PATH = "guides/driving-licence.html"

    fun siteUrl(path: String): String {
        val base = SITE_BASE.trimEnd('/')
        val clean = path.trimStart('/')
        return "$base/$clean"
    }

    val popularGuides = listOf(
        GuideItem(
            id = "licence",
            title = "Driving licence portals",
            description = "Find official driving-licence websites by province.",
            iconRes = R.drawable.ic_cat_license,
            guidePath = LICENCE_GUIDE_PATH,
            officialUrl = siteUrl(LICENCE_GUIDE_PATH),
            authorityName = "Provincial / ICT / GB traffic authorities",
        ),
        GuideItem(
            id = "verify",
            title = "Vehicle verification (Punjab)",
            description = "Open the Punjab Excise department website for official vehicle verification links.",
            iconRes = R.drawable.ic_cat_verify,
            guidePath = "guides/vehicle-verification.html",
            officialUrl = "https://excise.punjab.gov.pk/",
            authorityName = "Government of Punjab – Excise, Taxation & Narcotics Control Department",
        ),
        GuideItem(
            id = "token",
            title = "Token tax (Punjab)",
            description = "Open the Punjab e-Pay government payment website.",
            iconRes = R.drawable.ic_cat_pay,
            guidePath = "guides/token-tax.html",
            officialUrl = "https://epay.punjab.gov.pk/",
            authorityName = "Government of Punjab – e-Pay Punjab",
        ),
        GuideItem(
            id = "challan",
            title = "E-Challan (Punjab)",
            description = "Open government payment tools used for challan-related dues.",
            iconRes = R.drawable.ic_cat_challan,
            guidePath = "guides/e-challan.html",
            officialUrl = "https://epay.punjab.gov.pk/",
            authorityName = "Government of Punjab – e-Pay Punjab",
        ),
        GuideItem(
            id = "districts",
            title = "District Excise offices",
            description = "Independent guide to local Excise office contacts.",
            iconRes = R.drawable.ic_cat_portal,
            guidePath = "guides/districts.html",
            officialUrl = siteUrl("guides/districts.html"),
            authorityName = "Vehicle Hub PK independent guide",
        ),
        GuideItem(
            id = "smartcard",
            title = "E-Smart card (Punjab)",
            description = "Open the Punjab Excise department website for e-registration / smart card links.",
            iconRes = R.drawable.ic_cat_smartcard,
            guidePath = "guides/smart-card.html",
            officialUrl = "https://excise.punjab.gov.pk/",
            authorityName = "Government of Punjab – Excise, Taxation & Narcotics Control Department",
        ),
    )

    val licencePortals = listOf(
        LicencePortal(
            id = "dl-punjab",
            region = "Punjab",
            title = "Punjab DLIMS",
            description = "Learner, regular, and international licences via DLIMS / Dastak.",
            authorityName = "Government of Punjab – Driving Licence Issuance Management System (DLIMS)",
            portalUrl = "https://dlims.punjab.gov.pk/",
            verifyUrl = "https://dlims.punjab.gov.pk/verify",
            guideAnchor = "punjab",
        ),
        LicencePortal(
            id = "dl-sindh",
            region = "Sindh",
            title = "Sindh DLS",
            description = "Sindh Police DLS — verify licences and online services.",
            authorityName = "Government of Sindh – Sindh Police Driving License Sindh (DLS)",
            portalUrl = "https://dls.gos.pk/",
            verifyUrl = "https://dls.gos.pk/online-verification.html",
            guideAnchor = "sindh",
        ),
        LicencePortal(
            id = "dl-islamabad",
            region = "Islamabad",
            title = "Islamabad Traffic Police",
            description = "Islamabad Police official website for traffic and licence-related services.",
            authorityName = "Islamabad Traffic Police",
            portalUrl = "https://islamabadpolice.gov.pk/",
            verifyUrl = "https://islamabadpolice.gov.pk/",
            guideAnchor = "islamabad",
        ),
        LicencePortal(
            id = "dl-kpk",
            region = "KPK",
            title = "KPK Dastak (Citizen)",
            description = "Verify a licence by number + CNIC, or track an application.",
            authorityName = "Government of Khyber Pakhtunkhwa – Dastak Citizen Portal",
            portalUrl = "https://dastakappecitizenkp.pk/",
            verifyUrl = "https://dastakappecitizenkp.pk/",
            guideAnchor = "kpk",
        ),
        LicencePortal(
            id = "dl-balochistan",
            region = "Balochistan",
            title = "Quetta Traffic Police",
            description = "Learner, permanent, international, duplicate, and CNIC verification.",
            authorityName = "Government of Balochistan – Quetta Traffic Police",
            portalUrl = "https://qtp.gob.pk/",
            verifyUrl = "https://qtp.gob.pk/",
            guideAnchor = "balochistan",
        ),
        LicencePortal(
            id = "dl-gilgit",
            region = "Gilgit",
            title = "Gilgit-Baltistan Police",
            description = "Gilgit-Baltistan Police official website for licence-related services.",
            authorityName = "Gilgit-Baltistan Police",
            portalUrl = "https://gbp.gov.pk/",
            verifyUrl = "https://gbp.gov.pk/",
            guideAnchor = "gilgit",
        ),
    )

    // Order matches Vehicle Hub PK home province grid
    val provinces = listOf(
        Province(
            id = "kpk",
            name = "KPK / Dastak",
            badge = "KPK",
            description = "Main Dastak portal for digital arms licensing — plus links to KPK driving licence verify / track.",
            authorityName = "Government of Khyber Pakhtunkhwa – Dastak Portal",
            guidePath = "guides/kpk-excise.html",
            portalUrl = "https://dastak.kp.gov.pk/",
            services = listOf(
                ServiceItem("kp-license", "Digital arms licensing", "Apply and manage licensing on Dastak.", R.drawable.ic_cat_license, "https://dastak.kp.gov.pk/"),
                ServiceItem("kp-dl", "Driving licence verify / track", "Citizen tools for licence verify and application tracking.", R.drawable.ic_cat_verify, "https://dastakappecitizenkp.pk/", LICENCE_GUIDE_PATH),
                ServiceItem("kp-renew", "Renewals & conversions", "Renew or convert existing licenses.", R.drawable.ic_cat_renew, "https://dastak.kp.gov.pk/"),
                ServiceItem("kp-pay", "E-payment", "Pay fees through the Dastak portal.", R.drawable.ic_cat_pay, "https://dastak.kp.gov.pk/"),
                ServiceItem("kp-track", "E-tracking & alerts", "Track applications and status updates.", R.drawable.ic_cat_track, "https://dastak.kp.gov.pk/"),
                ServiceItem("kp-bio", "Biometric step", "Complete biometric verification steps.", R.drawable.ic_cat_biometric, "https://dastak.kp.gov.pk/"),
                ServiceItem("kp-portal", "Official Dastak portal", "Open the KPK Dastak website.", R.drawable.ic_cat_portal, "https://dastak.kp.gov.pk/"),
            ),
        ),
        Province(
            id = "sindh",
            name = "Sindh Excise",
            badge = "Sindh",
            description = "Vehicle verification, CNIC search, number plate check, and tax tools.",
            authorityName = "Government of Sindh – Excise, Taxation & Narcotics Control Department",
            guidePath = "guides/sindh-excise.html",
            portalUrl = "https://excise.gos.pk/",
            services = listOf(
                ServiceItem("sd-verify", "Vehicle verification", "Search vehicle records on the Sindh Excise portal.", R.drawable.ic_cat_verify, "https://excise.gos.pk/vehicle/vehicle_search"),
                ServiceItem("sd-cnic", "Verification by CNIC", "Look up vehicles linked to a CNIC.", R.drawable.ic_cat_cnic, "https://excise.gos.pk/vehicle/vehicle_search_by_cnic"),
                ServiceItem("sd-dl", "Driving licence (DLS)", "Sindh Police DLS — verify and online licence services.", R.drawable.ic_cat_license, "https://dls.gos.pk/online-verification.html", LICENCE_GUIDE_PATH),
                ServiceItem("sd-plate", "Number plate check", "Check plate-related online services.", R.drawable.ic_cat_plate, "https://excise.gos.pk/Online-Services"),
                ServiceItem("sd-tax", "Tax payment", "Online tax calculator and payment tools.", R.drawable.ic_cat_tax, "https://excise.gos.pk/online_services/online_tax_calculator/"),
                ServiceItem("sd-token", "Token tax", "Quick pay for token tax.", R.drawable.ic_cat_pay, "https://taxportal.excise.gos.pk/home/quick_pay"),
                ServiceItem("sd-portal", "Official portal", "Open the Sindh Excise website.", R.drawable.ic_cat_portal, "https://excise.gos.pk/"),
            ),
        ),
        Province(
            id = "balochistan",
            name = "Balochistan Excise",
            badge = "Balochistan",
            description = "Official Balochistan Excise portal, Quetta Traffic Police licence tools, and published tax rates.",
            authorityName = "Government of Balochistan – Excise, Taxation & Anti-Narcotics Department",
            guidePath = "guides/balochistan-excise.html",
            portalUrl = "https://excise.balochistan.gov.pk/",
            services = listOf(
                ServiceItem("bl-verify", "Online vehicle verification", "Open the Balochistan Excise department website for verification tools.", R.drawable.ic_cat_verify, "https://excise.balochistan.gov.pk/"),
                ServiceItem("bl-dl", "Driving licence (QTP)", "Open Quetta Traffic Police website for licence verification and related services.", R.drawable.ic_cat_license, "https://qtp.gob.pk/", LICENCE_GUIDE_PATH),
                ServiceItem("bl-tax", "Tax rates (PDF)", "Published motor vehicle tax rates from Balochistan Excise.", R.drawable.ic_cat_tax, "https://excise.balochistan.gov.pk/wp-content/uploads/2026/01/mvtx.pdf"),
                ServiceItem("bl-portal", "Official portal", "Open the Balochistan Excise website.", R.drawable.ic_cat_portal, "https://excise.balochistan.gov.pk/"),
            ),
        ),
        Province(
            id = "punjab",
            name = "Punjab Excise",
            badge = "Punjab",
            description = "Punjab Excise department website, DLIMS licence tools, e-Pay payments, and tracking.",
            authorityName = "Government of Punjab – Excise, Taxation & Narcotics Control Department",
            guidePath = "guides/punjab-excise.html",
            portalUrl = "https://excise.punjab.gov.pk/",
            services = listOf(
                ServiceItem("pb-verify", "Vehicle verification", "Open Punjab Excise for official online vehicle verification links.", R.drawable.ic_cat_verify, "https://excise.punjab.gov.pk/", "guides/vehicle-verification.html"),
                ServiceItem("pb-dl", "Driving licence (DLIMS)", "Punjab DLIMS — apply, renew, or verify a licence.", R.drawable.ic_cat_license, "https://dlims.punjab.gov.pk/verify", LICENCE_GUIDE_PATH),
                ServiceItem("pb-smart", "E-Smart card / VRC", "Open Punjab Excise for e-registration / smart card links.", R.drawable.ic_cat_smartcard, "https://excise.punjab.gov.pk/", "guides/smart-card.html"),
                ServiceItem("pb-track", "Smart card tracking", "Track card or application status on Pakistan Post.", R.drawable.ic_cat_track, "https://ep.gov.pk/track.asp", "guides/smart-card.html"),
                ServiceItem("pb-pay", "Token tax / e-Pay", "Pay dues through provincial e-Pay.", R.drawable.ic_cat_pay, "https://epay.punjab.gov.pk/", "guides/token-tax.html"),
            ),
        ),
        Province(
            id = "islamabad",
            name = "Islamabad Excise",
            badge = "Islamabad",
            description = "Official Islamabad Excise website and Islamabad Police for traffic / licence services.",
            authorityName = "Islamabad Capital Territory – Excise & Taxation Department",
            guidePath = "guides/islamabad-excise.html",
            portalUrl = "https://islamabadexcise.gov.pk/",
            services = listOf(
                ServiceItem("is-verify", "Vehicle registration info", "Open Islamabad Excise vehicle registration information pages.", R.drawable.ic_cat_verify, "https://islamabadexcise.gov.pk/VEH_REG/REG.HTML"),
                ServiceItem("is-dl", "Driving licence (ITP)", "Islamabad Police official website for traffic and licence services.", R.drawable.ic_cat_license, "https://islamabadpolice.gov.pk/", LICENCE_GUIDE_PATH),
                ServiceItem("is-portal", "Official portal", "Open the Islamabad Excise website.", R.drawable.ic_cat_portal, "https://islamabadexcise.gov.pk/"),
            ),
        ),
        Province(
            id = "gilgit",
            name = "Gilgit Excise",
            badge = "Gilgit",
            description = "GB Excise portal for motor vehicle registration, vehicle search, and token tax.",
            authorityName = "Gilgit-Baltistan – Excise & Taxation Department",
            guidePath = "guides/gilgit-excise.html",
            portalUrl = "https://gbexcise.gov.pk/",
            services = listOf(
                ServiceItem("gb-register", "Motor vehicle registration", "Start GB vehicle registration on the official portal.", R.drawable.ic_cat_register, "https://gbexcise.gov.pk/"),
                ServiceItem("gb-verify", "Online vehicle verification", "Verify GB vehicle records online.", R.drawable.ic_cat_verify, "https://gbexcise.gov.pk/vehicle-search/"),
                ServiceItem("gb-dl", "Driving licence (GB Police)", "Gilgit-Baltistan Police official website for licence-related services.", R.drawable.ic_cat_license, "https://gbp.gov.pk/", LICENCE_GUIDE_PATH),
                ServiceItem("gb-search", "Vehicle search", "Search vehicle details on GB Excise.", R.drawable.ic_cat_plate, "https://gbexcise.gov.pk/vehicle-search/"),
                ServiceItem("gb-tax", "Token tax", "Token tax services via the official portal.", R.drawable.ic_cat_tax, "https://gbexcise.gov.pk/"),
                ServiceItem("gb-portal", "Official Gilgit portal", "Open gbexcise.gov.pk.", R.drawable.ic_cat_portal, "https://gbexcise.gov.pk/"),
            ),
        ),
    )


    val governmentSources = listOf(
        GovernmentSource("Punjab Excise", "Government of Punjab – Excise, Taxation & Narcotics Control Department", "https://excise.punjab.gov.pk/"),
        GovernmentSource("Punjab DLIMS", "Government of Punjab – Driving Licence Issuance Management System (DLIMS)", "https://dlims.punjab.gov.pk/"),
        GovernmentSource("Punjab e-Pay", "Government of Punjab – e-Pay Punjab", "https://epay.punjab.gov.pk/"),
        GovernmentSource("Sindh Excise", "Government of Sindh – Excise, Taxation & Narcotics Control Department", "https://excise.gos.pk/"),
        GovernmentSource("Sindh DLS", "Government of Sindh – Sindh Police Driving License Sindh (DLS)", "https://dls.gos.pk/"),
        GovernmentSource("Islamabad Excise", "Islamabad Capital Territory – Excise & Taxation Department", "https://islamabadexcise.gov.pk/"),
        GovernmentSource("Islamabad Police", "Islamabad Capital Territory Police", "https://islamabadpolice.gov.pk/"),
        GovernmentSource("KPK Dastak", "Government of Khyber Pakhtunkhwa – Dastak Portal", "https://dastak.kp.gov.pk/"),
        GovernmentSource("KPK Dastak Citizen (Licence)", "Government of Khyber Pakhtunkhwa – Dastak Citizen Portal", "https://dastakappecitizenkp.pk/"),
        GovernmentSource("Balochistan Excise", "Government of Balochistan – Excise, Taxation & Anti-Narcotics Department", "https://excise.balochistan.gov.pk/"),
        GovernmentSource("Quetta Traffic Police", "Government of Balochistan – Quetta Traffic Police", "https://qtp.gob.pk/"),
        GovernmentSource("Gilgit Excise", "Gilgit-Baltistan – Excise & Taxation Department", "https://gbexcise.gov.pk/"),
        GovernmentSource("Gilgit-Baltistan Police", "Gilgit-Baltistan Police", "https://gbp.gov.pk/"),
    )

    fun provinceById(id: String): Province? = provinces.find { it.id == id }

    fun snapshot(config: AppConfig = AppConfig()): ContentSnapshot {
        val guides = popularGuides.map { guide ->
            guide.copy(officialUrl = config.urlFor(guide.id, guide.officialUrl))
        }
        val nextProvinces = provinces.map { province ->
            province.copy(
                portalUrl = config.urlFor("${province.id}.portal", province.portalUrl),
                services = province.services.map { service ->
                    service.copy(officialUrl = config.urlFor(service.id, service.officialUrl))
                },
            )
        }
        val licences = licencePortals.map { portal ->
            portal.copy(
                portalUrl = config.urlFor("${portal.id}.portal", portal.portalUrl),
                verifyUrl = config.urlFor("${portal.id}.verify", portal.verifyUrl),
            )
        }
        return ContentSnapshot(guides = guides, provinces = nextProvinces, licences = licences)
    }
}
