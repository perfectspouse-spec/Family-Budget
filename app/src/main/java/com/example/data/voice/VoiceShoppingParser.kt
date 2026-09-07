package com.example.data.voice

import com.example.data.model.ShoppingCatalog
import java.util.Locale
import java.util.UUID

data class VoiceShoppingItemDraft(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var quantity: Double = 1.0,
    var unit: String = "Adet",
    var category: String = "Market & Gıda",
    var estimatedPrice: Double? = null,
    var icon: String = "🛒"
)

object VoiceShoppingParser {

    private val NUMBER_WORDS = mapOf(
        "sıfır" to 0.0,
        "çeyrek" to 0.25,
        "yarım" to 0.5,
        "buçuk" to 0.5,
        "bir" to 1.0,
        "tek" to 1.0,
        "iki" to 2.0,
        "üç" to 3.0,
        "dört" to 4.0,
        "beş" to 5.0,
        "altı" to 6.0,
        "yedi" to 7.0,
        "sekiz" to 8.0,
        "dokuz" to 9.0,
        "on" to 10.0,
        "on bir" to 11.0,
        "on iki" to 12.0,
        "on üç" to 13.0,
        "on dört" to 14.0,
        "on beş" to 15.0,
        "on altı" to 16.0,
        "on yedi" to 17.0,
        "on sekiz" to 18.0,
        "on dokuz" to 19.0,
        "yirmi" to 20.0,
        "otuz" to 30.0,
        "kırk" to 40.0,
        "elli" to 50.0,
        "altmış" to 60.0,
        "yetmiş" to 70.0,
        "seksen" to 80.0,
        "doksan" to 90.0,
        "yüz" to 100.0,
        "yüz elli" to 150.0,
        "iki yüz" to 200.0,
        "iki yüz elli" to 250.0,
        "üç yüz" to 300.0,
        "beş yüz" to 500.0,
        "bin" to 1000.0,
        // English words
        "one" to 1.0,
        "two" to 2.0,
        "three" to 3.0,
        "four" to 4.0,
        "five" to 5.0,
        "six" to 6.0,
        "seven" to 7.0,
        "eight" to 8.0,
        "nine" to 9.0,
        "ten" to 10.0,
        "half" to 0.5
    )

    private val UNIT_WORDS = mapOf(
        "kilo" to "Kg",
        "kilosu" to "Kg",
        "kg" to "Kg",
        "kilogram" to "Kg",
        "litre" to "Lt",
        "litresi" to "Lt",
        "lt" to "Lt",
        "litrelik" to "Lt",
        "paket" to "Paket",
        "paketi" to "Paket",
        "pkt" to "Paket",
        "paketlik" to "Paket",
        "adet" to "Adet",
        "tane" to "Adet",
        "tanesi" to "Adet",
        "gram" to "Gram",
        "gr" to "Gram",
        "gramlık" to "Gram",
        "koli" to "Koli",
        "kolisi" to "Koli",
        "kolilik" to "Koli",
        "demet" to "Demet",
        "demeti" to "Demet",
        "kutu" to "Kutu",
        "kutusu" to "Kutu",
        "rulo" to "Rulo",
        "rulosu" to "Rulo",
        "şişe" to "Adet",
        "şişesi" to "Adet",
        "kalıp" to "Adet",
        "file" to "Adet",
        "bardak" to "Adet",
        "porsiyon" to "Adet",
        // English
        "piece" to "Adet",
        "pieces" to "Adet",
        "bottle" to "Adet",
        "box" to "Kutu",
        "pack" to "Paket"
    )

    private val FILLER_WORDS = setOf(
        "al", "alalım", "lazım", "gerek", "gerekli", "ekle", "ekler misin", "yaz",
        "sepete", "sepete at", "listeye", "listeye ekle", "bize", "bana", "eve",
        "lütfen", "ve", "ile", "bir de", "ayrıca", "tane", "adet"
    )

    /**
     * Parses spoken text (e.g. "2 kilo elma, bir paket makarna ve 3 litre süt")
     * and returns a structured list of VoiceShoppingItemDraft items.
     */
    fun parseSpokenShoppingText(rawText: String): List<VoiceShoppingItemDraft> {
        if (rawText.isBlank()) return emptyList()

        // Normalize text
        var normalized = rawText.lowercase(Locale("tr"))
            .replace(" ve ", " , ")
            .replace(" ile ", " , ")
            .replace(" artı ", " , ")
            .replace(" plus ", " , ")
            .replace(" and ", " , ")
            .replace("\n", " , ")
            .replace(";", " , ")
            .replace(".", " , ")

        // Split by comma
        val parts = normalized.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val resultList = mutableListOf<VoiceShoppingItemDraft>()

        for (part in parts) {
            val item = parseSingleItem(part)
            if (item != null && item.name.isNotBlank()) {
                resultList.add(item)
            }
        }

        // Fallback: If comma split resulted in nothing, try entire string
        if (resultList.isEmpty() && rawText.isNotBlank()) {
            val single = parseSingleItem(rawText.trim())
            if (single != null && single.name.isNotBlank()) {
                resultList.add(single)
            }
        }

        return resultList
    }

    private fun parseSingleItem(inputPart: String): VoiceShoppingItemDraft? {
        var text = inputPart.trim()
        if (text.isBlank()) return null

        var quantity = 1.0
        var unit = "Adet"

        // Check for compound numbers like "bir buçuk", "iki buçuk", "üç buçuk"
        if (text.contains("bir buçuk") || text.contains("1 buçuk") || text.contains("1.5") || text.contains("1,5")) {
            quantity = 1.5
            text = text.replace("bir buçuk", "").replace("1 buçuk", "").replace("1.5", "").replace("1,5", "").trim()
        } else if (text.contains("iki buçuk") || text.contains("2 buçuk") || text.contains("2.5") || text.contains("2,5")) {
            quantity = 2.5
            text = text.replace("iki buçuk", "").replace("2 buçuk", "").replace("2.5", "").replace("2,5", "").trim()
        } else if (text.contains("üç buçuk") || text.contains("3 buçuk") || text.contains("3.5") || text.contains("3,5")) {
            quantity = 3.5
            text = text.replace("üç buçuk", "").replace("3 buçuk", "").replace("3.5", "").replace("3,5", "").trim()
        } else if (text.contains("dört buçuk") || text.contains("4 buçuk") || text.contains("4.5") || text.contains("4,5")) {
            quantity = 4.5
            text = text.replace("dört buçuk", "").replace("4 buçuk", "").replace("4.5", "").replace("4,5", "").trim()
        } else if (text.contains("yarım") || text.contains("0.5") || text.contains("0,5")) {
            quantity = 0.5
            text = text.replace("yarım", "").replace("0.5", "").replace("0,5", "").trim()
        } else if (text.contains("çeyrek") || text.contains("0.25") || text.contains("0,25")) {
            quantity = 0.25
            text = text.replace("çeyrek", "").replace("0.25", "").replace("0,25", "").trim()
        } else {
            // Check for digit numbers: e.g. "2 kilo", "5 paket", "10 adet"
            val digitRegex = Regex("""\b(\d+([.,]\d+)?)\b""")
            val match = digitRegex.find(text)
            if (match != null) {
                val numStr = match.value.replace(",", ".")
                val num = numStr.toDoubleOrNull()
                if (num != null && num > 0) {
                    quantity = num
                    text = text.replaceRange(match.range, "").trim()
                }
            } else {
                // Check for word numbers
                for ((word, numVal) in NUMBER_WORDS) {
                    val wordRegex = Regex("""\b$word\b""", RegexOption.IGNORE_CASE)
                    if (wordRegex.containsMatchIn(text)) {
                        quantity = numVal
                        text = text.replace(wordRegex, "").trim()
                        break
                    }
                }
            }
        }

        // Check for units
        for ((uWord, uStandard) in UNIT_WORDS) {
            val uRegex = Regex("""\b$uWord\b""", RegexOption.IGNORE_CASE)
            if (uRegex.containsMatchIn(text)) {
                unit = uStandard
                text = text.replace(uRegex, "").trim()
                break
            }
        }

        // Clean filler words
        val words = text.split(" ")
            .map { it.trim().replace(Regex("[^a-zA-ZçÇğĞıİöÖşŞüÜ]"), "") }
            .filter { it.isNotBlank() && it.lowercase(Locale("tr")) !in FILLER_WORDS }

        if (words.isEmpty()) return null

        val rawCleanName = words.joinToString(" ")
        val finalName = capitalizeTitle(rawCleanName)
        if (finalName.isBlank()) return null

        // Auto-match category, icon, and estimated price from catalog
        val (category, icon, estPrice) = matchCategoryAndTemplate(finalName, unit, quantity)

        return VoiceShoppingItemDraft(
            name = finalName,
            quantity = quantity,
            unit = unit,
            category = category,
            estimatedPrice = estPrice,
            icon = icon
        )
    }

    private fun capitalizeTitle(text: String): String {
        return text.split(" ").filter { it.isNotBlank() }.joinToString(" ") { word ->
            word.lowercase(Locale("tr")).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("tr")) else it.toString() }
        }
    }

    private fun matchCategoryAndTemplate(
        productName: String,
        unit: String,
        quantity: Double
    ): Triple<String, String, Double?> {
        val lower = productName.lowercase(Locale("tr"))

        // First check exact catalog match
        for (group in ShoppingCatalog.GROUPS) {
            for (template in group.popularProducts) {
                val tLower = template.name.lowercase(Locale("tr"))
                if (lower in tLower || tLower in lower || lower.split(" ").any { it.length > 2 && it in tLower }) {
                    val estPrice = template.estimatedPrice?.let { basePrice ->
                        val pricePerUnit = if (template.defaultQuantity > 0) basePrice / template.defaultQuantity else basePrice
                        val calculated = pricePerUnit * quantity
                        kotlin.math.round(calculated * 10) / 10.0
                    }
                    return Triple(group.nameTr, template.icon, estPrice)
                }
            }
        }

        // Semantic Category Heuristics
        return when {
            lower.contains("domates") || lower.contains("salatalık") || lower.contains("elma") ||
            lower.contains("biber") || lower.contains("patates") || lower.contains("soğan") ||
            lower.contains("marul") || lower.contains("muz") || lower.contains("limon") ||
            lower.contains("portakal") || lower.contains("mandalina") || lower.contains("havuç") ||
            lower.contains("ıspanak") || lower.contains("pırasa") || lower.contains("kabak") ||
            lower.contains("patlıcan") || lower.contains("çilek") || lower.contains("üzüm") ||
            lower.contains("karpuz") || lower.contains("kavun") || lower.contains("maydanoz") ||
            lower.contains("dereotu") || lower.contains("nane") || lower.contains("roka") ||
            lower.contains("yeşillik") || lower.contains("sarımsak") || lower.contains("mantar") -> {
                val icon = when {
                    lower.contains("domates") -> "🍅"
                    lower.contains("elma") -> "🍎"
                    lower.contains("muz") -> "🍌"
                    lower.contains("limon") -> "🍋"
                    lower.contains("portakal") || lower.contains("mandalina") -> "🍊"
                    lower.contains("patates") -> "🥔"
                    lower.contains("soğan") -> "🧅"
                    lower.contains("biber") -> "🫑"
                    lower.contains("havuç") -> "🥕"
                    lower.contains("salatalık") -> "🥒"
                    else -> "🍏"
                }
                Triple("Meyve & Sebze", icon, 30.0 * quantity)
            }

            lower.contains("süt") || lower.contains("peynir") || lower.contains("kaşar") ||
            lower.contains("yoğurt") || lower.contains("zeytin") || lower.contains("yumurta") ||
            lower.contains("tereyağ") || lower.contains("kaymak") || lower.contains("labne") ||
            lower.contains("bal") || lower.contains("reçel") || lower.contains("sucuk") ||
            lower.contains("salam") || lower.contains("sosis") || lower.contains("pastırma") ||
            lower.contains("nutella") || lower.contains("fındık kreması") || lower.contains("lor") -> {
                val icon = when {
                    lower.contains("süt") -> "🥛"
                    lower.contains("peynir") || lower.contains("kaşar") || lower.contains("labne") -> "🧀"
                    lower.contains("yumurta") -> "🥚"
                    lower.contains("tereyağ") -> "🧈"
                    lower.contains("zeytin") -> "🫒"
                    lower.contains("bal") -> "🍯"
                    lower.contains("sucuk") || lower.contains("salam") -> "🥓"
                    else -> "🥛"
                }
                Triple("Süt & Kahvaltılık", icon, 45.0 * quantity)
            }

            lower.contains("ekmek") || lower.contains("simit") || lower.contains("poğaça") ||
            lower.contains("lavaş") || lower.contains("tost") || lower.contains("börek") ||
            lower.contains("bazlama") || lower.contains("baget") || lower.contains("açma") -> {
                Triple("Fırın & Unlu Mamul", "🍞", 12.5 * quantity)
            }

            lower.contains("makarna") || lower.contains("pirinç") || lower.contains("bulgur") ||
            lower.contains("yağ") || lower.contains("zeytinyağ") || lower.contains("sıvı yağ") ||
            lower.contains("un") || lower.contains("şeker") || lower.contains("tuz") ||
            lower.contains("salça") || lower.contains("çay") || lower.contains("kahve") ||
            lower.contains("mercimek") || lower.contains("nohut") || lower.contains("fasulye") ||
            lower.contains("baharat") || lower.contains("sirke") || lower.contains("konserve") -> {
                val icon = when {
                    lower.contains("çay") -> "🫖"
                    lower.contains("kahve") -> "☕"
                    lower.contains("yağ") -> "🫒"
                    lower.contains("makarna") -> "🍝"
                    else -> "🌾"
                }
                Triple("Temel Gıda", icon, 35.0 * quantity)
            }

            lower.contains("kıyma") || lower.contains("et") || lower.contains("kuşbaşı") ||
            lower.contains("tavuk") || lower.contains("baget") || lower.contains("köfte") ||
            lower.contains("balık") || lower.contains("bonfile") || lower.contains("biftek") ||
            lower.contains("antrikot") || lower.contains("hindi") -> {
                Triple("Et, Tavuk & Balık", "🥩", 180.0 * quantity)
            }

            lower.contains("deterjan") || lower.contains("sabun") || lower.contains("şampuan") ||
            lower.contains("tuvalet kağıdı") || lower.contains("havlu") || lower.contains("kağıt havlu") ||
            lower.contains("yumuşatıcı") || lower.contains("bulaşık") || lower.contains("çamaşır") ||
            lower.contains("çöp torbası") || lower.contains("sünger") || lower.contains("cif") ||
            lower.contains("çamaşır suyu") || lower.contains("porçöz") || lower.contains("temizleyici") -> {
                val icon = when {
                    lower.contains("tuvalet") || lower.contains("havlu") -> "🧻"
                    lower.contains("sabun") || lower.contains("şampuan") -> "🧴"
                    else -> "🧼"
                }
                Triple("Temizlik & Hijyen", icon, 65.0 * quantity)
            }

            lower.contains("su") || lower.contains("kola") || lower.contains("soda") ||
            lower.contains("maden suyu") || lower.contains("meyve suyu") || lower.contains("ayran") ||
            lower.contains("gazoz") || lower.contains("fanta") || lower.contains("sprite") ||
            lower.contains("ice tea") || lower.contains("soğuk çay") || lower.contains("şalgam") -> {
                val icon = when {
                    lower.contains("su") -> "💧"
                    lower.contains("kola") || lower.contains("fanta") || lower.contains("gazoz") -> "🥤"
                    else -> "🧃"
                }
                Triple("İçecekler", icon, 20.0 * quantity)
            }

            lower.contains("çikolata") || lower.contains("bisküvi") || lower.contains("cips") ||
            lower.contains("gofret") || lower.contains("kek") || lower.contains("kuruyemiş") ||
            lower.contains("fındık") || lower.contains("fıstık") || lower.contains("badem") ||
            lower.contains("ceviz") || lower.contains("çekirdek") || lower.contains("şekerleme") ||
            lower.contains("jelibon") || lower.contains("kraker") || lower.contains("dondurma") -> {
                Triple("Atıştırmalık & Tatlı", "🍫", 25.0 * quantity)
            }

            lower.contains("diş macunu") || lower.contains("diş fırçası") || lower.contains("bebek bezi") ||
            lower.contains("ıslak mendil") || lower.contains("deodorant") || lower.contains("parfüm") ||
            lower.contains("ped") || lower.contains("tıraş") || lower.contains("krem") ||
            lower.contains("pamuk") || lower.contains("duş jeli") -> {
                Triple("Kişisel Bakım & Bebek", "👶", 55.0 * quantity)
            }

            lower.contains("kedi") || lower.contains("köpek") || lower.contains("mama") ||
            lower.contains("kum") || lower.contains("folyo") || lower.contains("streç") ||
            lower.contains("pişirme kağıdı") || lower.contains("pil") || lower.contains("ampul") -> {
                val icon = if (lower.contains("kedi") || lower.contains("köpek") || lower.contains("mama")) "🐱" else "🏠"
                Triple("Ev, Mutfak & Pet", icon, 40.0 * quantity)
            }

            else -> Triple("Market & Gıda", "🛒", null)
        }
    }
}
