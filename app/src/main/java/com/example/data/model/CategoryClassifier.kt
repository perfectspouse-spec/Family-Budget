package com.example.data.model

object CategoryClassifier {

    val ALL_CATEGORIES = listOf(
        "Market & Gıda",
        "Akaryakıt & Ulaşım",
        "Giyim & Moda",
        "Fatura & Aidat",
        "Sağlık & Eczane",
        "Restoran & Kafe",
        "Ev & Yaşam",
        "Eğlence & Diğer"
    )

    fun classifyProduct(productName: String, merchantHint: String = ""): String {
        val lower = productName.lowercase()
        val mLower = merchantHint.lowercase()

        // Merchant hints first
        if (mLower.contains("shell") || mLower.contains("opet") || mLower.contains("bp") ||
            mLower.contains("petrol") || mLower.contains("total") || mLower.contains("akaryakıt")) {
            return "Akaryakıt & Ulaşım"
        }
        if (mLower.contains("eczane") || mLower.contains("pharmacy") || mLower.contains("sağlık") || mLower.contains("hastane")) {
            return "Sağlık & Eczane"
        }
        if (mLower.contains("enerjisa") || mLower.contains("iski") || mLower.contains("igdaş") ||
            mLower.contains("telekom") || mLower.contains("turkcell") || mLower.contains("vodafone") || mLower.contains("fatura")) {
            return "Fatura & Aidat"
        }
        if (mLower.contains("lc waikiki") || mLower.contains("koton") || mLower.contains("zara") ||
            mLower.contains("defacto") || mLower.contains("mavi") || mLower.contains("boyner")) {
            return "Giyim & Moda"
        }
        if (mLower.contains("starbucks") || mLower.contains("kahve") || mLower.contains("burger") ||
            mLower.contains("döner") || mLower.contains("kebap") || mLower.contains("restoran") || mLower.contains("cafe")) {
            return "Restoran & Kafe"
        }

        // Product text checks
        return when {
            // Food & Market
            lower.contains("süt") || lower.contains("peynir") || lower.contains("yumurta") ||
                    lower.contains("ekmek") || lower.contains("yağ") || lower.contains("kıyma") ||
                    lower.contains("et") || lower.contains("tavuk") || lower.contains("makarna") ||
                    lower.contains("çay") || lower.contains("şeker") || lower.contains("un") ||
                    lower.contains("domates") || lower.contains("elma") || lower.contains("muz") ||
                    lower.contains("yoğurt") || lower.contains("zeytin") || lower.contains("bisküvi") ||
                    lower.contains("çikolata") || lower.contains("su 5l") || lower.contains("pirinç") -> "Market & Gıda"

            // Transport & Fuel
            lower.contains("benzin") || lower.contains("motorin") || lower.contains("dizel") ||
                    lower.contains("lpg") || lower.contains("otopark") || lower.contains("bilet") ||
                    lower.contains("taksi") || lower.contains("akaryakıt") || lower.contains("otobüs") -> "Akaryakıt & Ulaşım"

            // Clothing
            lower.contains("tişört") || lower.contains("t-shirt") || lower.contains("pantolon") ||
                    lower.contains("gömlek") || lower.contains("çorap") || lower.contains("ayakkabı") ||
                    lower.contains("ceket") || lower.contains("elbise") || lower.contains("mont") ||
                    lower.contains("sweatshirt") || lower.contains("kazak") -> "Giyim & Moda"

            // Bills & Utilities
            lower.contains("elektrik") || lower.contains("su faturası") || lower.contains("doğalgaz") ||
                    lower.contains("internet") || lower.contains("aidat") || lower.contains("telefon faturası") ||
                    lower.contains("tüketim") -> "Fatura & Aidat"

            // Health & Pharmacy
            lower.contains("ilaç") || lower.contains("vitamin") || lower.contains("damla") ||
                    lower.contains("şurup") || lower.contains("krem") || lower.contains("ağrı kesici") ||
                    lower.contains("pastil") || lower.contains("bandaj") || lower.contains("merhem") -> "Sağlık & Eczane"

            // Restaurant & Dining
            lower.contains("kahve") || lower.contains("latte") || lower.contains("burger") ||
                    lower.contains("pizza") || lower.contains("döner") || lower.contains("çorba") ||
                    lower.contains("kebap") || lower.contains("tatlı") || lower.contains("menü") -> "Restoran & Kafe"

            // Home & Cleaning
            lower.contains("deterjan") || lower.contains("sabun") || lower.contains("şampuan") ||
                    lower.contains("yumuşatıcı") || lower.contains("havlu") || lower.contains("peçete") ||
                    lower.contains("paspas") || lower.contains("ampul") || lower.contains("tava") -> "Ev & Yaşam"

            else -> if (mLower.contains("migros") || mLower.contains("bim") || mLower.contains("a101") || mLower.contains("şok") || mLower.contains("carrefour")) {
                "Market & Gıda"
            } else {
                "Eğlence & Diğer"
            }
        }
    }
}
