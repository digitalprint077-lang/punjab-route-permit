package com.pakexciseinfo.app.data

import androidx.annotation.DrawableRes
import com.pakexciseinfo.app.BuildConfig
import com.pakexciseinfo.app.R

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
    @DrawableRes val logoRes: Int,
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
)

data class ContentSnapshot(
    val guides: List<GuideItem>,
    val provinces: List<Province>,
) {
    fun provinceById(id: String): Province? = provinces.find { it.id == id }
}

object AppContent {
    const val SITE_BASE = BuildConfig.SITE_URL

    fun siteUrl(path: String): String {
        val base = SITE_BASE.trimEnd('/')
        val clean = path.trimStart('/')
        return "$base/$clean"
    }

    val popularGuides = listOf(
        GuideItem(
            id = "verify",
            title = "Vehicle verification",
            description = "Official MTMIS steps, SMS tip, and what result fields mean.",
            iconRes = R.drawable.ic_cat_verify,
            guidePath = "guides/vehicle-verification.html",
            officialUrl = "https://mtmis.excise.punjab.gov.pk/",
        ),
        GuideItem(
            id = "token",
            title = "Token tax",
            description = "Why dues matter before purchase and how status is usually checked.",
            iconRes = R.drawable.ic_cat_pay,
            guidePath = "guides/token-tax.html",
            officialUrl = "https://epay.punjab.gov.pk/",
        ),
        GuideItem(
            id = "smartcard",
            title = "E-Smart card",
            description = "What the card is for and how tracking usually works.",
            iconRes = R.drawable.ic_cat_smartcard,
            guidePath = "guides/smart-card.html",
            officialUrl = "https://vrcentpunjab.com/",
        ),
        GuideItem(
            id = "challan",
            title = "E-Challan",
            description = "Unpaid fines that can block transfers — check early.",
            iconRes = R.drawable.ic_cat_challan,
            guidePath = "guides/e-challan.html",
            officialUrl = "https://epay.punjab.gov.pk/",
        ),
    )

    // Order matches Vehicle Hub PK home province grid
    val provinces = listOf(
        Province(
            id = "kpk",
            name = "KPK / Dastak",
            badge = "KPK",
            description = "Digital licensing portal for apply, renew, pay, and track workflows.",
            logoRes = R.drawable.ic_prov_kpk,
            guidePath = "guides/kpk-excise.html",
            portalUrl = "https://dastak.kp.gov.pk/",
            services = listOf(
                ServiceItem("kp-license", "Digital arms licensing", "Apply and manage licensing on Dastak.", R.drawable.ic_cat_license, "https://dastak.kp.gov.pk/"),
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
            logoRes = R.drawable.ic_prov_sindh,
            guidePath = "guides/sindh-excise.html",
            portalUrl = "https://excise.gos.pk/",
            services = listOf(
                ServiceItem("sd-verify", "Vehicle verification", "Search vehicle records on the Sindh Excise portal.", R.drawable.ic_cat_verify, "https://excise.gos.pk/vehicle/vehicle_search"),
                ServiceItem("sd-cnic", "Verification by CNIC", "Look up vehicles linked to a CNIC.", R.drawable.ic_cat_cnic, "https://excise.gos.pk/vehicle/vehicle_search_by_cnic"),
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
            description = "Online vehicle verification, tax payment, number plate check, and tax rates.",
            logoRes = R.drawable.ic_prov_balochistan,
            guidePath = "guides/balochistan-excise.html",
            portalUrl = "https://excise.balochistan.gov.pk/",
            services = listOf(
                ServiceItem("bl-verify", "Online vehicle verification", "Verify vehicle records online.", R.drawable.ic_cat_verify, "https://etanb.com/vehicle-verification/"),
                ServiceItem("bl-pay", "Online tax payment", "Pay motor vehicle taxes online.", R.drawable.ic_cat_pay, "https://taxes.excise.gob.pk/"),
                ServiceItem("bl-plate", "Check number plate", "Number plate information service.", R.drawable.ic_cat_plate, "https://excise.balochistan.gov.pk/wp-content/uploads/2026/01/np1.html"),
                ServiceItem("bl-tax", "Tax rates", "View published motor vehicle tax rates.", R.drawable.ic_cat_tax, "https://excise.balochistan.gov.pk/wp-content/uploads/2026/01/mvtx.pdf"),
                ServiceItem("bl-calc", "Tax calculator", "Estimate dues with the official calculator.", R.drawable.ic_cat_calc, "https://taxes.excise.gob.pk/IdentityInfo"),
                ServiceItem("bl-portal", "Official portal", "Open the Balochistan Excise website.", R.drawable.ic_cat_portal, "https://excise.balochistan.gov.pk/"),
            ),
        ),
        Province(
            id = "punjab",
            name = "Punjab Excise",
            badge = "Punjab",
            description = "MTMIS verification, e-smart card, tracking, e-Pay payments, and route permit.",
            logoRes = R.drawable.ic_prov_punjab,
            guidePath = "guides/punjab-excise.html",
            portalUrl = "https://mtmis.excise.punjab.gov.pk/",
            services = listOf(
                ServiceItem("pb-verify", "Vehicle verification", "Owner, engine/chassis, and tax clues by registration number.", R.drawable.ic_cat_verify, "https://mtmis.excise.punjab.gov.pk/", "guides/vehicle-verification.html"),
                ServiceItem("pb-smart", "E-Smart card / VRC", "Registration card digital services.", R.drawable.ic_cat_smartcard, "https://vrcentpunjab.com/", "guides/smart-card.html"),
                ServiceItem("pb-track", "Smart card tracking", "Track card or application status.", R.drawable.ic_cat_track, "https://ep.gov.pk/track.asp", "guides/smart-card.html"),
                ServiceItem("pb-pay", "Token tax / e-Pay", "Pay dues through provincial e-Pay.", R.drawable.ic_cat_pay, "https://epay.punjab.gov.pk/", "guides/token-tax.html"),
                ServiceItem("pb-route", "Route permit", "Punjab transport route permit portal.", R.drawable.ic_cat_portal, "https://pdtg-transport.punjab.gov.pk/route-permit"),
            ),
        ),
        Province(
            id = "islamabad",
            name = "Islamabad Excise",
            badge = "Islamabad",
            description = "Vehicle detail check, registration/transfer, smart card status, and challan tools.",
            logoRes = R.drawable.ic_prov_islamabad,
            guidePath = "guides/islamabad-excise.html",
            portalUrl = "https://islamabadexcise.gov.pk/",
            services = listOf(
                ServiceItem("is-verify", "Check vehicle detail", "Look up Islamabad vehicle registration data.", R.drawable.ic_cat_verify, "http://58.65.189.226:8080/ovd/API_FOR_VEH_REG_DATA/VEHDATA.PHP"),
                ServiceItem("is-reg", "New vehicle registration", "Start a new registration workflow.", R.drawable.ic_cat_register, "http://58.65.189.226:8080/"),
                ServiceItem("is-transfer", "Transfer of ownership", "Transfer vehicle ownership online.", R.drawable.ic_cat_transfer, "http://58.65.189.226:8081/vehicle-transfer"),
                ServiceItem("is-smart", "Smart card status", "Check smart card / ETD request status.", R.drawable.ic_cat_smartcard, "http://58.65.189.226:8081/etd-online-be/reqcard.php"),
                ServiceItem("is-challan", "Challan verification", "Verify challan details online.", R.drawable.ic_cat_challan, "http://58.65.189.226:8080/DSonline/Public/VEH/CHDETAIL/REQCHALLAN.php"),
                ServiceItem("is-portal", "Official portal", "Open the Islamabad Excise website.", R.drawable.ic_cat_portal, "https://islamabadexcise.gov.pk/"),
            ),
        ),
        Province(
            id = "gilgit",
            name = "Gilgit Excise",
            badge = "Gilgit",
            description = "GB Excise portal for motor vehicle registration, vehicle search, and token tax.",
            logoRes = R.drawable.ic_prov_gilgit,
            guidePath = "guides/gilgit-excise.html",
            portalUrl = "https://gbexcise.gov.pk/",
            services = listOf(
                ServiceItem("gb-register", "Motor vehicle registration", "Start GB vehicle registration on the official portal.", R.drawable.ic_cat_register, "https://gbexcise.gov.pk/"),
                ServiceItem("gb-verify", "Online vehicle verification", "Verify GB vehicle records online.", R.drawable.ic_cat_verify, "https://gbexcise.gov.pk/vehicle-search/"),
                ServiceItem("gb-search", "Vehicle search", "Search vehicle details on GB Excise.", R.drawable.ic_cat_plate, "https://gbexcise.gov.pk/vehicle-search/"),
                ServiceItem("gb-tax", "Token tax", "Token tax services via the official portal.", R.drawable.ic_cat_tax, "https://gbexcise.gov.pk/"),
                ServiceItem("gb-portal", "Official Gilgit portal", "Open gbexcise.gov.pk.", R.drawable.ic_cat_portal, "https://gbexcise.gov.pk/"),
            ),
        ),
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
        return ContentSnapshot(guides = guides, provinces = nextProvinces)
    }
}
