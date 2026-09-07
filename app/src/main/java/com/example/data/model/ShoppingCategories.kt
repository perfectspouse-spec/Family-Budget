package com.example.data.model

import java.util.Locale

data class MarketProductOption(
    val id: String,
    val marketId: String, // "bim", "a101", "sok", "tarimkredi", "migros", "carrefoursa"
    val marketName: String, // "BİM", "A101", "ŞOK", "Tarım Kredi", "Migros", "CarrefourSA"
    val marketIcon: String, // "🏬", "🛒", "⚡", "🌾", "🛍️", "🏪"
    val marketBrand: String, // "Dost", "Birşah", "Mis", "TK Koop", "Sütaş / Migros", "Pınar / Carrefour"
    val fullProductName: String, // "BİM • Dost Süt 1L (₺31.50)"
    val displayName: String, // "Dost Süt 1L"
    val price: Double, // Effective price for current quantity & unit
    val unitPriceDisplay: String, // "₺31.50 / Lt"
    val unit: String,
    val quantity: Double
)

data class MarketProductTemplate(
    val name: String,
    val defaultUnit: String = "Adet",
    val defaultQuantity: Double = 1.0,
    val estimatedPrice: Double? = null,
    val icon: String = "🛒",
    val brandVariants: List<String> = emptyList()
)

data class MarketGroup(
    val id: String,
    val nameTr: String,
    val icon: String,
    val colorHex: Long,
    val popularProducts: List<MarketProductTemplate>
)

object ShoppingCatalog {
    val GROUPS = listOf(
        MarketGroup(
            id = "dairy_breakfast",
            nameTr = "Süt & Kahvaltılık",
            icon = "🥛",
            colorHex = 0xFF1E88E5,
            popularProducts = listOf(
                MarketProductTemplate("Tam Yağlı Süt (1L)", "Lt", 1.0, 31.5, "🥛", listOf("Sütaş", "İçim", "Pınar", "Dost (BİM)", "Birşah (A101)", "Mis (ŞOK)", "TK Koop", "Torku")),
                MarketProductTemplate("Yarım Yağlı Süt (1L)", "Lt", 1.0, 28.5, "🥛", listOf("İçim", "Sütaş", "Dost (BİM)", "Birşah (A101)", "Mis (ŞOK)", "Pınar", "Torku")),
                MarketProductTemplate("Laktozsuz Süt (1L)", "Lt", 1.0, 36.5, "🥛", listOf("Pınar Denge", "İçim Rahat", "Sütaş", "Dost (BİM)", "Birşah (A101)", "Mis (ŞOK)")),
                MarketProductTemplate("Tam Yağlı Beyaz Peynir", "Kg", 0.5, 89.0, "🧀", listOf("Tahsildaroğlu", "Ekici", "Sütaş", "Aknaz (BİM)", "Peynes (A101)", "Mis (ŞOK)", "TK Koop", "Muratbey")),
                MarketProductTemplate("Taze Kaşar Peyniri (400g)", "Gram", 400.0, 115.0, "🧀", listOf("Muratbey", "Sütaş", "İçim", "Aknaz (BİM)", "Ahir (A101)", "Mis (ŞOK)", "TK Koop", "Torku")),
                MarketProductTemplate("Süzme Peynir (500g)", "Gram", 500.0, 72.5, "🧀", listOf("Sütaş Süzme", "İçim Süzme", "Kerem (BİM)", "Peynes (A101)", "Mis (ŞOK)", "Pınar Süzme")),
                MarketProductTemplate("Labne Peynir (400g)", "Paket", 1.0, 49.5, "🧀", listOf("İçim Labne", "Pınar Labne", "Kerem (BİM)", "Milkten (A101)", "Mis (ŞOK)", "Sütaş")),
                MarketProductTemplate("Krem Peynir (200g)", "Paket", 1.0, 39.0, "🧀", listOf("Pınar Krem", "Sütaş Krem", "Kerem (BİM)", "Milkten (A101)", "Mis (ŞOK)")),
                MarketProductTemplate("Ezine Klasik Beyaz Peynir", "Gram", 350.0, 139.0, "🧀", listOf("Gürsüt", "Tahsildaroğlu", "Aknaz Ezine (BİM)", "Ahir Tulum (A101)", "TK Ezine", "Ekici")),
                MarketProductTemplate("Taze Lor Peyniri (500g)", "Paket", 1.0, 36.0, "🧀", listOf("Cihan Lor", "Aknaz Lor (BİM)", "Peynes Lor (A101)", "Mis Lor (ŞOK)", "Muratbey")),
                MarketProductTemplate("Yumurta (30'lu L Boy)", "Koli", 1.0, 119.0, "🥚", listOf("Keskinoğlu", "Biyum (BİM)", "Mis Yumurta (ŞOK)", "TK Koop", "Güres", "Torku")),
                MarketProductTemplate("Yumurta (15'li M Boy)", "Koli", 1.0, 62.0, "🥚", listOf("Biyum (BİM)", "Keskinoğlu", "Mis (ŞOK)", "TK Koop", "Güres")),
                MarketProductTemplate("Gezen Tavuk Yumurtası (10'lu)", "Koli", 1.0, 65.0, "🥚", listOf("Kor Gezen Tavuk", "Keskinoğlu", "TK Gezen Tavuk", "Organik Köy")),
                MarketProductTemplate("Geleneksel Tereyağı (250g)", "Gram", 250.0, 85.0, "🧈", listOf("Kebir", "Sütaş", "İçim", "Milkten (BİM)", "Mis (ŞOK)", "TK Koop", "Pınar", "Torku")),
                MarketProductTemplate("Rulo Kaymak (200g)", "Paket", 1.0, 59.0, "🧈", listOf("Eker", "Sütaş", "Kerem (BİM)", "Milkten (A101)", "Mis (ŞOK)")),
                MarketProductTemplate("Kaymaklı Yoğurt (1.5Kg)", "Kg", 1.5, 62.5, "🥣", listOf("Sütaş Tava", "Eker", "Dost (BİM)", "Birşah (A101)", "Mis (ŞOK)", "TK Koop", "İçim")),
                MarketProductTemplate("Süzme Yoğurt (900g)", "Paket", 1.0, 72.0, "🥣", listOf("İçim Süzme", "Sütaş Süzme", "Eker", "Dost Süzme (BİM)", "Birşah (A101)", "Mis (ŞOK)")),
                MarketProductTemplate("Doğal Siyah Zeytin (500g)", "Kg", 0.5, 79.0, "🫒", listOf("Marmarabirlik", "İnci (BİM)", "Zeo (A101)", "Lio (ŞOK)", "TK Gemlik", "Fora")),
                MarketProductTemplate("Çizik Yeşil Zeytin (500g)", "Kg", 0.5, 82.0, "🫒", listOf("Marmarabirlik", "Lio (ŞOK)", "İnci (BİM)", "Zeo (A101)", "TK Yeşil", "Fora")),
                MarketProductTemplate("Süzme Çiçek Balı (850g)", "Adet", 1.0, 149.0, "🍯", listOf("Balparmak", "Balküpü", "Anavarza", "Petek Bal", "TK Çiçek Balı")),
                MarketProductTemplate("Süzme Çam Balı (850g)", "Adet", 1.0, 165.0, "🍯", listOf("Balparmak", "Balküpü", "Anavarza", "TK Çam Balı")),
                MarketProductTemplate("Reçel (Çilek / Vişne 380g)", "Adet", 1.0, 44.0, "🍓", listOf("Seyidoğlu", "Yurdum (BİM)", "Mis (ŞOK)", "TK Koop", "Tamek")),
                MarketProductTemplate("Kakaolu Fındık Kreması (400g)", "Adet", 1.0, 58.0, "🍫", listOf("Nutella", "Sarelle", "Torku Banada", "Peripella (BİM)", "Findux (A101)", "Karmen (ŞOK)")),
                MarketProductTemplate("Tahin & Pekmez İkili (700g)", "Paket", 1.0, 88.0, "🍯", listOf("Koska", "Seyidoğlu", "Torku", "TK Koop")),
                MarketProductTemplate("Dana Kangal Sucuk (250g)", "Paket", 1.0, 129.0, "🥓", listOf("Şahin", "Cumhuriyet", "Namet", "Erşan", "Mis (ŞOK)", "TK Koop", "Polonez")),
                MarketProductTemplate("Dana Macar Salam (60g)", "Paket", 1.0, 29.0, "🥓", listOf("Pınar", "Namet", "Şahin", "Polonez", "Danet")),
                MarketProductTemplate("Hindi Füme (150g)", "Paket", 1.0, 44.0, "🥓", listOf("Namet", "Pınar", "Polonez", "Banvit"))
            )
        ),
        MarketGroup(
            id = "produce",
            nameTr = "Meyve & Sebze",
            icon = "🍏",
            colorHex = 0xFF43A047,
            popularProducts = listOf(
                MarketProductTemplate("Salkım Domates", "Kg", 1.0, 29.0, "🍅", listOf("Yerli Tarla", "Salkım", "Sera", "Pembe Domates")),
                MarketProductTemplate("Çeri Domates (500g)", "Paket", 1.0, 24.0, "🍅", listOf("Paketli Çeri", "Kokteyl Domates", "Şeker Domates")),
                MarketProductTemplate("Salatalık", "Kg", 1.0, 24.0, "🥒", listOf("Çengelköy", "Sera Çıtır", "Köy Salatalığı")),
                MarketProductTemplate("Patates (2Kg)", "Kg", 2.0, 32.0, "🥔", listOf("Ödemiş", "File Kızartmalık", "Taze Patates", "Agria")),
                MarketProductTemplate("Kuru Soğan (2Kg)", "Kg", 2.0, 28.0, "🧅", listOf("Amasya", "File Kuru Soğan", "Beyaz Soğan")),
                MarketProductTemplate("Tatlı Mor Soğan", "Kg", 1.0, 24.0, "🧅", listOf("Salatalık Mor Soğan", "Kırmızı Soğan")),
                MarketProductTemplate("Taze Dal Soğan", "Demet", 1.0, 15.0, "🌿", listOf("Taze Demet")),
                MarketProductTemplate("Sivri Biber (500g)", "Gram", 500.0, 29.0, "🫑", listOf("Tatlı Sivri", "Kıl Biber", "Acı Sivri")),
                MarketProductTemplate("Çarliston Biber (500g)", "Gram", 500.0, 27.0, "🫑", listOf("Yemeklik Çarliston", "Köy Biberi")),
                MarketProductTemplate("Kırmızı Kapya Biber", "Kg", 1.0, 45.0, "🫑", listOf("Etli Kapya", "Közlemelik Kapya")),
                MarketProductTemplate("Dolmalık Biber (500g)", "Gram", 500.0, 29.5, "🫑", listOf("Taze Dolmalık", "Kandil Dolma")),
                MarketProductTemplate("Limon", "Kg", 0.5, 17.0, "🍋", listOf("Lamas File", "Mayer", "Yatak Limon")),
                MarketProductTemplate("Yerli Muz", "Kg", 1.0, 68.0, "🍌", listOf("Anamur Yerli", "İthal Chiquita", "Dole")),
                MarketProductTemplate("Kırmızı Elma", "Kg", 1.5, 45.0, "🍎", listOf("Amasya", "Starking", "Fuji", "Gala")),
                MarketProductTemplate("Yeşil Elma", "Kg", 1.0, 37.0, "🍏", listOf("Granny Smith", "Amasya Ekşi")),
                MarketProductTemplate("Sıkmalık Portakal (2Kg)", "Kg", 2.0, 38.0, "🍊", listOf("Finike Sıkmalık", "Washington Portakal")),
                MarketProductTemplate("Mandalina (1.5Kg)", "Kg", 1.5, 34.0, "🍊", listOf("Bodrum Satsuma", "Klemantin")),
                MarketProductTemplate("Taze Havuç", "Kg", 1.0, 23.0, "🥕", listOf("Konya Yıkanmış", "Taze Havuç")),
                MarketProductTemplate("Kıvırcık Marul", "Adet", 1.0, 20.0, "🥬", listOf("Taze Kıvırcık", "Göbek Marul", "Aysberg", "Yedikule")),
                MarketProductTemplate("Taze Yeşillik Demeti", "Demet", 1.0, 14.0, "🌿", listOf("Maydanoz", "Dereotu", "Taze Nane", "Roka", "Tere")),
                MarketProductTemplate("Yaprak Ispanak", "Kg", 1.0, 32.0, "🥬", listOf("Kök Ispanak", "Yaprak Ispanak", "Bebek Ispanak")),
                MarketProductTemplate("İnce Pırasa", "Kg", 1.0, 28.0, "🥬", listOf("Taze İnce Pırasa", "Kışlık Pırasa")),
                MarketProductTemplate("Brokoli / Karnabahar", "Kg", 1.0, 42.0, "🥦", listOf("Taze Brokoli", "Beyaz Karnabahar")),
                MarketProductTemplate("Sakız Kabak", "Kg", 1.0, 26.0, "🥒", listOf("Taze Sakız Kabak", "Girit Kabak")),
                MarketProductTemplate("Kemer Patlıcan", "Kg", 1.0, 32.0, "🍆", listOf("Kemer Patlıcan", "Bostan Patlıcan")),
                MarketProductTemplate("Kültür Mantarı (400g)", "Paket", 1.0, 36.0, "🍄", listOf("Paketli Beyaz", "Kestane Mantarı", "İstiridye")),
                MarketProductTemplate("Çilek (500g)", "Paket", 1.0, 42.0, "🍓", listOf("Sera Çilek", "Dağ Çileği")),
                MarketProductTemplate("Karpuz / Kavun", "Kg", 3.0, 42.0, "🍉", listOf("Diyarbakır Karpuz", "Kırkağaç Kavun"))
            )
        ),
        MarketGroup(
            id = "meat_fish",
            nameTr = "Et, Tavuk & Balık",
            icon = "🥩",
            colorHex = 0xFFE53935,
            popularProducts = listOf(
                MarketProductTemplate("Dana Kıyma (%15-%20 Yağlı)", "Kg", 0.5, 198.0, "🥩", listOf("Uzman Kasap", "Emin (BİM)", "Kombinet (A101)", "Mis (ŞOK)", "TK Kasap", "Namet")),
                MarketProductTemplate("Dana Kuşbaşı", "Kg", 0.5, 218.0, "🥩", listOf("Uzman Kasap", "Emin (BİM)", "Kombinet (A101)", "Mis (ŞOK)", "TK Kasap", "Namet")),
                MarketProductTemplate("Dana Antrikot / Bonfile (400g)", "Gram", 400.0, 255.0, "🥩", listOf("Namet", "Uzman Kasap", "TK Dana", "Emin (BİM)")),
                MarketProductTemplate("Dana Biftek / Kontrfile (500g)", "Gram", 500.0, 238.0, "🥩", listOf("Uzman Kasap", "Namet", "Emin (BİM)", "TK Dana")),
                MarketProductTemplate("Kasap / İnegöl Köfte (400g)", "Paket", 1.0, 155.0, "🧆", listOf("Emin Kasap (BİM)", "Uzman Kasap", "Kombinet (A101)", "Mis (ŞOK)", "TK Dana Köfte", "Namet")),
                MarketProductTemplate("Piliç Göğüs Fileto", "Kg", 1.0, 155.0, "🍗", listOf("Banvit", "Lezita", "Şenpiliç", "CP Piliç", "Gedik", "Keskinoğlu")),
                MarketProductTemplate("Piliç But / Baget", "Kg", 1.0, 99.0, "🍗", listOf("Lezita", "Banvit", "Şenpiliç", "CP Piliç", "Gedik")),
                MarketProductTemplate("Piliç Izgara Kanat", "Kg", 1.0, 139.0, "🍗", listOf("Şenpiliç", "Banvit", "Lezita", "CP Piliç")),
                MarketProductTemplate("Taze Bütün Piliç", "Kg", 1.8, 129.0, "🍗", listOf("Banvit", "Lezita", "Şenpiliç", "CP Piliç", "Gedik")),
                MarketProductTemplate("Dana Pastırma (120g)", "Gram", 120.0, 99.0, "🥓", listOf("Şahin", "Namet", "Cumhuriyet", "Polonez", "Erşan")),
                MarketProductTemplate("Taze Somon Fileto (400g)", "Gram", 400.0, 175.0, "🐟", listOf("Norveç Somon", "Karadeniz Somon")),
                MarketProductTemplate("Taze Levrek / Çipura", "Kg", 0.8, 205.0, "🐟", listOf("Deniz Levreği", "Kültür Çipura", "Temizlenmiş")),
                MarketProductTemplate("Ton Balığı Konservesi (3x75g)", "Paket", 1.0, 82.0, "🥫", listOf("Dardanel", "Derya (BİM)", "Mis (ŞOK)", "Superfresh", "TK Koop"))
            )
        ),
        MarketGroup(
            id = "bakery",
            nameTr = "Fırın & Unlu Mamül",
            icon = "🍞",
            colorHex = 0xFFFB8C00,
            popularProducts = listOf(
                MarketProductTemplate("Taş Fırın Somun Ekmek", "Adet", 2.0, 18.0, "🍞", listOf("Günlük Somun", "Halk Ekmek", "Odun Ekmeği", "Köy Ekmeği")),
                MarketProductTemplate("Tam Buğday Ekmeği (500g)", "Adet", 1.0, 29.0, "🍞", listOf("Uno", "Ekmecik (BİM)", "Nimet (A101)", "Halk Ekmek", "Doygun")),
                MarketProductTemplate("Çavdar / Çok Tahıllı Ekmek", "Adet", 1.0, 32.0, "🍞", listOf("Uno", "Ekmecik (BİM)", "Nimet (A101)", "Halk Ekmek")),
                MarketProductTemplate("Tost Ekmeği (Büyük Dilim)", "Paket", 1.0, 36.0, "🥪", listOf("Uno", "Ekmecik (BİM)", "Nimet (A101)", "Mis (ŞOK)", "Doygun")),
                MarketProductTemplate("Susamlı Sokak Simiti", "Adet", 2.0, 24.0, "🥯", listOf("Geleneksel Simit", "Kandil Simiti")),
                MarketProductTemplate("Günlük Taze Yufka (5'li)", "Paket", 1.0, 38.0, "🥟", listOf("Nimet (A101)", "Ekmecik (BİM)", "Mis (ŞOK)", "Fırın Yufkası")),
                MarketProductTemplate("Dürüm Lavaş / Tortilla (12'li)", "Paket", 1.0, 32.0, "🌯", listOf("Uno", "Ekmecik (BİM)", "Nimet (A101)", "Mis (ŞOK)"))
            )
        ),
        MarketGroup(
            id = "staples_pantry",
            nameTr = "Temel Gıda & Bakliyat",
            icon = "🌾",
            colorHex = 0xFF8D6E63,
            popularProducts = listOf(
                MarketProductTemplate("Ayçiçek Yağı (5L)", "Adet", 1.0, 249.0, "🌻", listOf("Yudum", "Komili", "Sole (BİM)", "Vera (A101)", "Evin (ŞOK)", "TK Birlik", "Biryağ", "Orkide")),
                MarketProductTemplate("Ayçiçek Yağı (1L / 2L)", "Adet", 1.0, 54.0, "🌻", listOf("Komili", "Yudum", "Sole (BİM)", "Vera (A101)", "Evin (ŞOK)", "TK Birlik")),
                MarketProductTemplate("Sızma Zeytinyağı (1L)", "Adet", 1.0, 295.0, "🫒", listOf("Komili", "Sırım (BİM)", "Zeo (A101)", "Lio (ŞOK)", "TK Sızma", "Tariş", "Kristal")),
                MarketProductTemplate("Riviera Zeytinyağı (1L / 2L)", "Adet", 1.0, 240.0, "🫒", listOf("Komili", "Sırım (BİM)", "Zeo (A101)", "Lio (ŞOK)", "Tariş")),
                MarketProductTemplate("Baldo Pirinç (1Kg / 2.5Kg)", "Paket", 1.0, 68.0, "🍚", listOf("Duru", "Reis", "Efsane (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Osmancık Pirinç (1Kg)", "Paket", 1.0, 48.0, "🍚", listOf("Duru", "Reis", "Efsane (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Pilavlık Bulgur (1Kg)", "Paket", 1.0, 31.0, "🌾", listOf("Duru", "Reis", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Köftelik İnce Bulgur (1Kg)", "Paket", 1.0, 29.0, "🌾", listOf("Duru", "Reis", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)")),
                MarketProductTemplate("Kırmızı Mercimek (1Kg)", "Paket", 1.0, 44.0, "🍲", listOf("Reis", "Duru", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Yeşil Mercimek (1Kg)", "Paket", 1.0, 49.0, "🍲", listOf("Reis", "Duru", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Koçbaşı Nohut (1Kg)", "Paket", 1.0, 54.0, "🍲", listOf("Duru", "Reis", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Kuru Fasulye (Dermason 1Kg)", "Paket", 1.0, 66.0, "🍲", listOf("Reis", "Duru", "Saban (BİM)", "Ovadan (A101)", "Anadolu (ŞOK)", "TK Koop")),
                MarketProductTemplate("Makarna Çeşitleri (500g)", "Paket", 2.0, 26.0, "🍝", listOf("Barilla", "Filiz", "Cardella (BİM)", "Bendo (A101)", "Piyale (ŞOK)", "Pastavilla", "Nuh'un Ankara")),
                MarketProductTemplate("Şehriye (Tel & Arpa 500g)", "Paket", 1.0, 14.0, "🍝", listOf("Filiz", "Barilla", "Cardella (BİM)", "Bendo (A101)", "Piyale (ŞOK)", "Nuh'un Ankara")),
                MarketProductTemplate("Buğday Unu (2Kg / 5Kg)", "Paket", 1.0, 41.0, "🌾", listOf("Söke", "Sinangil", "Efsane (BİM)", "Nimet (A101)", "Piyale (ŞOK)", "TK Koop")),
                MarketProductTemplate("Toz Şeker (1Kg / 5Kg)", "Paket", 1.0, 34.5, "🍬", listOf("Torku", "Bor Şeker", "Şafak (BİM)", "Petek (A101)", "Altınküp (ŞOK)", "TK Şeker")),
                MarketProductTemplate("Küp Şeker (1Kg 360'lı)", "Kutu", 1.0, 37.5, "🍬", listOf("Torku", "Doğuş", "Şafak (BİM)", "Petek (A101)", "Altınküp (ŞOK)", "TK Şeker")),
                MarketProductTemplate("Domates Salçası (830g)", "Adet", 1.0, 44.0, "🥫", listOf("Tat", "Öncü", "Yurdum (BİM)", "Çokça (A101)", "Vatan (ŞOK)", "TK Koop", "Tamek", "Tukaş")),
                MarketProductTemplate("Biber Salçası (700g Tatlı/Acı)", "Adet", 1.0, 58.0, "🥫", listOf("Öncü", "Tat", "Yurdum (BİM)", "Çokça (A101)", "Vatan (ŞOK)", "TK Koop", "Tukaş")),
                MarketProductTemplate("İyotlu Sofra Tuzu (750g)", "Paket", 1.0, 12.0, "🧂", listOf("Billur Tuz", "Salina", "Efsane (BİM)", "Mis (ŞOK)", "TK Tuz"))
            )
        ),
        MarketGroup(
            id = "beverages",
            nameTr = "İçecekler",
            icon = "🧃",
            colorHex = 0xFF00ACC1,
            popularProducts = listOf(
                MarketProductTemplate("Siyah Dökme Çay (1Kg)", "Paket", 1.0, 148.0, "🫖", listOf("Çaykur (Rize/Tiryaki)", "Doğuş (Karadeniz)", "Berk (BİM)", "Deren (ŞOK)", "TK Filiz", "Lipton")),
                MarketProductTemplate("Demlik Poşet Çay (100'lü)", "Kutu", 1.0, 105.0, "🫖", listOf("Çaykur Demlik", "Doğuş Demlik", "Lipton Yellow Label", "Berk Demlik (BİM)", "Deren Demlik (ŞOK)")),
                MarketProductTemplate("Türk Kahvesi (100g)", "Paket", 1.0, 35.0, "☕", listOf("Kurukahveci Mehmet Efendi", "Kocatepe", "Abdullah Efendi (BİM)", "Tadım", "Kahve Dünyası")),
                MarketProductTemplate("Filtre Kahve (250g)", "Paket", 1.0, 115.0, "☕", listOf("Jacobs Monarch", "Mehmet Efendi Filtre", "Tchibo Feine Milde", "Starbucks")),
                MarketProductTemplate("Granül Kahve (Gold 100g)", "Kavanoz", 1.0, 75.0, "☕", listOf("Nescafe Gold", "Jacobs Gold", "Vip Coffee (BİM)", "Nescafe Classic")),
                MarketProductTemplate("Sade Maden Suyu (6'lı)", "Paket", 1.0, 38.0, "🍾", listOf("Kızılay Sade", "Beypazarı Sade", "Sırma Sade", "Uludağ Premium", "Sarıkız", "Avşar")),
                MarketProductTemplate("Doğal Kaynak Suyu (5L)", "Adet", 1.0, 18.0, "💧", listOf("Erikli", "Hayat Su", "Assu (BİM)", "Sırma", "Damla Su", "Pınar Su")),
                MarketProductTemplate("%100 Meyve Suyu (1L)", "Adet", 1.0, 32.0, "🧃", listOf("Dimes %100", "Cappy", "Juss (BİM)", "Tamek", "Pınar", "Meysu")),
                MarketProductTemplate("Ayran (1.5L)", "Adet", 1.0, 22.5, "🥛", listOf("Sütaş Ayran", "Dost Ayran (BİM)", "Birşah Ayran (A101)", "Mis Ayran (ŞOK)", "Eker", "İçim"))
            )
        ),
        MarketGroup(
            id = "cleaning",
            nameTr = "Temizlik & Deterjan",
            icon = "🧹",
            colorHex = 0xFF00897B,
            popularProducts = listOf(
                MarketProductTemplate("Toz Çamaşır Deterjanı (6Kg)", "Paket", 1.0, 195.0, "🧺", listOf("Ariel", "Omo Active Fresh", "Peros", "Alo", "Tursil", "Bingo", "Rinso", "Artmatik (BİM)")),
                MarketProductTemplate("Sıvı Çamaşır Deterjanı (3L)", "Adet", 1.0, 155.0, "🧺", listOf("Perwoll", "Omo Sıvı", "Ariel Sıvı", "Persil Jel", "Bingo Sıvı", "Bill Sıvı (BİM)")),
                MarketProductTemplate("Konsantre Çamaşır Yumuşatıcı", "Adet", 1.0, 65.0, "🌸", listOf("Yumoş Extra", "Vernel Max", "Bingo Soft", "Bulut (BİM)", "Çiçeğim (A101)")),
                MarketProductTemplate("Bulaşık Makinesi Tableti (50'li)", "Paket", 1.0, 145.0, "🍽️", listOf("Fairy Platinum Plus", "Finish Quantum", "Pril Gold", "Bind Activit (BİM)", "Mintax (ŞOK)")),
                MarketProductTemplate("Sıvı Bulaşık Deterjanı (1.5L)", "Adet", 1.0, 42.0, "🧼", listOf("Fairy Limonlu", "Pril Hijyen", "Bingo", "Bill Sıvı (BİM)", "Mintax (ŞOK)", "Çiçeğim (A101)")),
                MarketProductTemplate("Yağ Çözücü Mutfak Spreyi (750ml)", "Adet", 1.0, 36.0, "✨", listOf("Cif Mutfak Sprey", "Mr Muscle", "Marc Mutfak", "Mintax (ŞOK)", "Güldal (BİM)")),
                MarketProductTemplate("Yüzey Temizleyici (1.5L)", "Adet", 1.0, 39.0, "✨", listOf("Marc Yüzey", "Cif Yüzey Uzmanı", "Bingo Fresh", "Mintax (ŞOK)")),
                MarketProductTemplate("Ultra Çamaşır Suyu (810g Jel)", "Adet", 1.0, 29.0, "🧴", listOf("Domestos Ultra", "Ace Ultra Jel", "Mintax (ŞOK)", "Güldal (BİM)", "Çiçeğim (A101)")),
                MarketProductTemplate("Tuvalet Kağıdı (3 Katlı 16'lı)", "Paket", 1.0, 129.0, "🧻", listOf("Papia", "Selpak Deluxe", "Solo", "Queen (BİM)", "Familia", "Maylo")),
                MarketProductTemplate("Kağıt Havlu (3 Katlı 6'lı)", "Paket", 1.0, 69.0, "🧻", listOf("Solo Kağıt Havlu", "Selpak Havlu", "Papia Havlu", "Queen Havlu (BİM)", "Familia"))
            )
        ),
        MarketGroup(
            id = "personal_care",
            nameTr = "Kişisel Bakım",
            icon = "🧴",
            colorHex = 0xFF8E24AA,
            popularProducts = listOf(
                MarketProductTemplate("Şampuan (400ml - 500ml)", "Adet", 1.0, 79.0, "🧴", listOf("Head & Shoulders", "Elidor", "Pantene Pro-V", "Clear Man", "Gliss", "Blendax", "Duru")),
                MarketProductTemplate("Sıvı El Sabunu (500ml)", "Adet", 1.0, 28.0, "🧼", listOf("Duru Sıvı Sabun", "Eyüp Sabri Tuncer", "Activex Antibakteriyel", "Hacı Şakir", "Palmolive")),
                MarketProductTemplate("Diş Macunu (75ml)", "Adet", 1.0, 54.0, "🪥", listOf("Colgate Optic White", "Sensodyne Hızlı Rahatlama", "Signal White Now", "İpana 3D White")),
                MarketProductTemplate("Bebek Bezi (Maxi / Junior)", "Paket", 1.0, 245.0, "👶", listOf("Sleepy Natural Maxi", "Prima Aktif Bebek", "Molfix Maxi", "Baby Turco", "Canbebe"))
            )
        )
    )

    fun getCategoryByName(name: String): MarketGroup? {
        return GROUPS.find { it.nameTr.equals(name, ignoreCase = true) }
    }

    val UNITS = listOf("Adet", "Kg", "Lt", "Paket", "Gram", "Koli", "Demet", "Kutu", "Rulo")

    /**
     * Helper to safely match whole words or exact terms in product/brand text.
     */
    private fun containsWord(text: String, word: String): Boolean {
        val pattern = "(^|[^\\p{L}\\p{Nd}])${Regex.escape(word)}($|[^\\p{L}\\p{Nd}])"
        return Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(text)
    }

    /**
     * Determines which markets officially sell the given brand or product name.
     */
    fun getValidMarketsForBrand(brand: String?, productName: String): List<String> {
        val cleanBrand = (brand ?: "").trim()
        val combinedText = "${cleanBrand.lowercase(Locale.forLanguageTag("tr"))} ${productName.lowercase(Locale.forLanguageTag("tr"))}".trim()

        // 1. Explicit market tags
        if (combinedText.contains("(bim)") || combinedText.contains("(bım)")) return listOf("bim")
        if (combinedText.contains("(a101)")) return listOf("a101")
        if (combinedText.contains("(şok)") || combinedText.contains("(sok)")) return listOf("sok")
        if (combinedText.contains("(tarım kredi)") || combinedText.contains("(tarim kredi)") || combinedText.contains("(tk)")) return listOf("tarimkredi")
        if (combinedText.contains("(migros)")) return listOf("migros")
        if (combinedText.contains("(carrefour)") || combinedText.contains("(carrefoursa)")) return listOf("carrefoursa")

        // 2. Strict Private Label Brand Recognition (Never sold in competitor supermarkets)
        // ŞOK exclusive private labels
        val sokExclusive = listOf(
            "mis", "evin", "lio", "anadolu mutfağı", "anadolu mutfagi", "piyale", "mintax",
            "deren", "confort", "altınküp", "altinkup", "vatan", "karmen", "mis kasap"
        )
        if (sokExclusive.any { containsWord(combinedText, it) }) {
            return listOf("sok")
        }

        // BİM exclusive private labels
        val bimExclusive = listOf(
            "dost", "aknaz", "kerem", "biyum", "inci", "sole", "sırım", "sirim", "efsane", "saban",
            "berk", "cardella", "yurdum", "bill", "bind", "güldal", "guldal", "queen", "emin kasap", "emin",
            "derya", "vip coffee", "abdullah efendi", "peripella", "ekmecik", "artmatik", "bulut", "assu", "juss"
        )
        if (bimExclusive.any { containsWord(combinedText, it) }) {
            return listOf("bim")
        }

        // A101 exclusive private labels
        val a101Exclusive = listOf(
            "birşah", "birsah", "peynes", "ahir", "vera", "zeo", "ovadan", "nimet", "bendo",
            "karadem", "çokça", "cokca", "findux", "kombinet", "çiçeğim", "cicegim"
        )
        if (a101Exclusive.any { containsWord(combinedText, it) }) {
            return listOf("a101")
        }

        // Tarım Kredi exclusive private labels
        val tkExclusive = listOf(
            "tk koop", "tk birlik", "tk kasap", "tk peynir", "tk çam balı", "tk çiçek balı",
            "tk sut", "tk süt", "tk filiz", "tk şeker", "tk seker", "tk tuz", "tk ezine", "tk gemlik",
            "tk dogal", "tk doğal", "tk dana", "tk"
        )
        if (tkExclusive.any { containsWord(combinedText, it) } || combinedText.startsWith("tk ") || combinedText.startsWith("tk")) {
            return listOf("tarimkredi")
        }

        // Migros exclusive private labels
        if (combinedText.contains("uzman kasap") || combinedText.contains("m life") || combinedText.contains("touch me")) {
            return listOf("migros")
        }

        // CarrefourSA exclusive private labels
        if (combinedText.contains("carrefour kasap") || combinedText.contains("carrefoursa")) {
            return listOf("carrefoursa")
        }

        // Produce / Manav produce variants (sold everywhere)
        if (combinedText.contains("tarla") || combinedText.contains("sera") || combinedText.contains("yerli") ||
            combinedText.contains("file") || combinedText.contains("taze") || combinedText.contains("amasya") ||
            combinedText.contains("anamur") || combinedText.contains("finike") || combinedText.contains("bodrum") ||
            combinedText.contains("konya") || combinedText.contains("diyarbakır") || combinedText.contains("demet")
        ) {
            return listOf("bim", "a101", "sok", "tarimkredi", "migros", "carrefoursa")
        }

        // Real market distribution for prominent National Brands
        val lower = combinedText
        return when {
            containsWord(lower, "sütaş") || containsWord(lower, "sutas") ->
                listOf("migros", "carrefoursa", "sok", "tarimkredi", "a101", "bim")
            containsWord(lower, "pınar") || containsWord(lower, "pinar") ->
                listOf("migros", "carrefoursa", "sok")
            containsWord(lower, "içim") || containsWord(lower, "icim") ->
                listOf("migros", "carrefoursa", "sok", "tarimkredi")
            containsWord(lower, "torku") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "eker") ->
                listOf("migros", "carrefoursa", "sok", "bim")
            containsWord(lower, "tahsildaroğlu") || containsWord(lower, "muratbey") || containsWord(lower, "ekici") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok")
            containsWord(lower, "gürsüt") || containsWord(lower, "cihan") || containsWord(lower, "doygun") || containsWord(lower, "barilla") ->
                listOf("migros", "carrefoursa")
            containsWord(lower, "kebir") ->
                listOf("migros", "carrefoursa", "tarimkredi")
            containsWord(lower, "keskinoğlu") || containsWord(lower, "güres") ->
                listOf("migros", "carrefoursa", "a101", "sok", "bim")
            containsWord(lower, "banvit") || containsWord(lower, "lezita") || containsWord(lower, "şenpiliç") || containsWord(lower, "senpilic") || containsWord(lower, "gedik") || containsWord(lower, "cp") ->
                listOf("migros", "carrefoursa", "bim", "a101", "sok")
            containsWord(lower, "şahin") || containsWord(lower, "sahin") || containsWord(lower, "cumhuriyet") || containsWord(lower, "namet") || containsWord(lower, "polonez") || containsWord(lower, "erşan") || containsWord(lower, "danet") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok")
            containsWord(lower, "marmarabirlik") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "balparmak") || containsWord(lower, "balküpü") || containsWord(lower, "anavarza") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok")
            containsWord(lower, "nutella") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "sarelle") ->
                listOf("migros", "carrefoursa", "sok")
            containsWord(lower, "uno") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "yudum") || containsWord(lower, "komili") ->
                listOf("migros", "carrefoursa", "sok", "a101", "tarimkredi")
            containsWord(lower, "tariş") || containsWord(lower, "kristal") ->
                listOf("migros", "carrefoursa", "tarimkredi")
            containsWord(lower, "reis") || containsWord(lower, "duru") ->
                listOf("migros", "carrefoursa", "tarimkredi")
            containsWord(lower, "filiz") || containsWord(lower, "nuh'un ankara") || containsWord(lower, "pastavilla") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "söke") || containsWord(lower, "soke") || containsWord(lower, "sinangil") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101")
            containsWord(lower, "tat") || containsWord(lower, "öncü") || containsWord(lower, "oncu") || containsWord(lower, "tamek") || containsWord(lower, "tukaş") || containsWord(lower, "tukas") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "billur") || containsWord(lower, "salina") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "çaykur") || containsWord(lower, "caykur") || containsWord(lower, "doğuş") || containsWord(lower, "dogus") || containsWord(lower, "lipton") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "kurukahveci") || containsWord(lower, "mehmet efendi") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "jacobs") || containsWord(lower, "nescafe") || containsWord(lower, "tchibo") || containsWord(lower, "starbucks") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "kızılay") || containsWord(lower, "kizilay") || containsWord(lower, "beypazarı") || containsWord(lower, "beypazari") || containsWord(lower, "sırma") || containsWord(lower, "sirma") || containsWord(lower, "uludağ") || containsWord(lower, "uludag") || containsWord(lower, "sarıkız") || containsWord(lower, "avşar") ->
                listOf("migros", "carrefoursa", "tarimkredi", "sok", "a101", "bim")
            containsWord(lower, "erikli") || containsWord(lower, "hayat") || containsWord(lower, "damla") ->
                listOf("migros", "carrefoursa", "sok")
            containsWord(lower, "dimes") || containsWord(lower, "cappy") || containsWord(lower, "meysu") ->
                listOf("migros", "carrefoursa", "sok", "a101")
            containsWord(lower, "ariel") || containsWord(lower, "omo") || containsWord(lower, "peros") || containsWord(lower, "alo") || containsWord(lower, "tursil") || containsWord(lower, "bingo") || containsWord(lower, "rinso") || containsWord(lower, "perwoll") || containsWord(lower, "persil") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "yumoş") || containsWord(lower, "yumos") || containsWord(lower, "vernel") ->
                listOf("migros", "carrefoursa", "sok", "a101")
            containsWord(lower, "fairy") || containsWord(lower, "finish") || containsWord(lower, "pril") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "cif") || containsWord(lower, "mr muscle") || containsWord(lower, "marc") || containsWord(lower, "domestos") || containsWord(lower, "ace") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "papia") || containsWord(lower, "selpak") || containsWord(lower, "solo") || containsWord(lower, "familia") || containsWord(lower, "maylo") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "head & shoulders") || containsWord(lower, "elidor") || containsWord(lower, "pantene") || containsWord(lower, "clear") || containsWord(lower, "gliss") || containsWord(lower, "blendax") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "eyüp sabri") || containsWord(lower, "activex") || containsWord(lower, "hacı şakir") || containsWord(lower, "palmolive") ->
                listOf("migros", "carrefoursa", "sok", "a101")
            containsWord(lower, "colgate") || containsWord(lower, "sensodyne") || containsWord(lower, "signal") || containsWord(lower, "ipana") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            containsWord(lower, "sleepy") || containsWord(lower, "prima") || containsWord(lower, "molfix") || containsWord(lower, "baby turco") || containsWord(lower, "canbebe") ->
                listOf("migros", "carrefoursa", "sok", "a101", "bim")
            else ->
                // Generic unspecified product is available across all retail chains
                listOf("bim", "a101", "sok", "tarimkredi", "migros", "carrefoursa")
        }
    }

    /**
     * Returns the genuine, real-world private brand or official product name for each specific market.
     */
    fun getRealMarketBrandForProduct(marketId: String, productName: String, category: String): String {
        val lowerName = productName.lowercase(Locale.forLanguageTag("tr"))
        return when (marketId) {
            "bim" -> when {
                lowerName.contains("laktozsuz") -> "Dost Laktozsuz"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("yoğurt") || lowerName.contains("yogurt") || lowerName.contains("ayran") -> "Dost"
                lowerName.contains("beyaz peynir") || lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("ezine") || lowerName.contains("lor") || lowerName.contains("peynir") -> "Aknaz"
                lowerName.contains("labne") || lowerName.contains("kaymak") || lowerName.contains("krem peynir") -> "Kerem"
                lowerName.contains("tereyağ") || lowerName.contains("tereyag") -> "Milkten"
                lowerName.contains("yumurta") -> "Biyum"
                lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") -> "Sırım"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("yağ") || lowerName.contains("yag") -> "Sole"
                lowerName.contains("pirinç") || lowerName.contains("pirinc") || lowerName.contains("un") || lowerName.contains("tuz") -> "Efsane"
                lowerName.contains("bulgur") || lowerName.contains("nohut") || lowerName.contains("mercimek") || lowerName.contains("fasulye") || lowerName.contains("bakliyat") -> "Saban"
                lowerName.contains("çay") || lowerName.contains("cay") -> "Berk"
                lowerName.contains("makarna") || lowerName.contains("şehriye") || lowerName.contains("sehriye") -> "Cardella"
                lowerName.contains("salça") || lowerName.contains("salca") || lowerName.contains("reçel") || lowerName.contains("recel") -> "Yurdum"
                lowerName.contains("şeker") || lowerName.contains("seker") -> "Şafak"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") || lowerName.contains("antrikot") || lowerName.contains("biftek") -> "Emin Kasap"
                lowerName.contains("piliç") || lowerName.contains("pilic") || lowerName.contains("tavuk") -> "Banvit"
                lowerName.contains("ton balığı") || lowerName.contains("ton baligi") -> "Derya"
                lowerName.contains("ekmek") || lowerName.contains("yufka") || lowerName.contains("lavaş") || lowerName.contains("lavas") || lowerName.contains("tost") -> "Ekmecik"
                lowerName.contains("türk kahvesi") || lowerName.contains("turk kahvesi") -> "Abdullah Efendi"
                lowerName.contains("granül") || lowerName.contains("granul") || lowerName.contains("kahve") -> "Vip Coffee"
                lowerName.contains("fındık kreması") || lowerName.contains("findik kremasi") -> "Peripella"
                lowerName.contains("çamaşır deterjanı") || lowerName.contains("camasir deterjani") || lowerName.contains("sıvı deterjan") || lowerName.contains("bulaşık deterjanı") -> "Bill"
                lowerName.contains("tablet") || lowerName.contains("bulaşık makinesi") -> "Bind Activit"
                lowerName.contains("çamaşır suyu") || lowerName.contains("camasir suyu") || lowerName.contains("yağ çözücü") || lowerName.contains("yag cozucu") -> "Güldal"
                lowerName.contains("yumuşatıcı") || lowerName.contains("yumusatici") -> "Bulut"
                lowerName.contains("tuvalet") || lowerName.contains("havlu") || lowerName.contains("peçete") -> "Queen"
                lowerName.contains("su") && !lowerName.contains("suyu") -> "Assu"
                lowerName.contains("meyve suyu") -> "Juss"
                lowerName.contains("zeytin") -> "İnci"
                category == "Meyve & Sebze" -> "BİM Manav"
                else -> "BİM"
            }
            "a101" -> when {
                lowerName.contains("laktozsuz") -> "Birşah Laktozsuz"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("yoğurt") || lowerName.contains("yogurt") || lowerName.contains("ayran") -> "Birşah"
                lowerName.contains("lor") || lowerName.contains("beyaz peynir") || lowerName.contains("süzme peynir") -> "Peynes"
                lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("tulum") || lowerName.contains("peynir") -> "Ahir"
                lowerName.contains("labne") || lowerName.contains("krem peynir") || lowerName.contains("tereyağ") || lowerName.contains("kaymak") -> "Milkten"
                lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") || lowerName.contains("zeytin") -> "Zeo"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("yağ") || lowerName.contains("yag") -> "Vera"
                lowerName.contains("pirinç") || lowerName.contains("pirinc") || lowerName.contains("bulgur") || lowerName.contains("nohut") || lowerName.contains("mercimek") || lowerName.contains("fasulye") || lowerName.contains("bakliyat") -> "Ovadan"
                lowerName.contains("un") || lowerName.contains("ekmek") || lowerName.contains("yufka") || lowerName.contains("lavaş") || lowerName.contains("lavas") || lowerName.contains("tost") -> "Nimet"
                lowerName.contains("makarna") || lowerName.contains("şehriye") || lowerName.contains("sehriye") -> "Bendo"
                lowerName.contains("çay") || lowerName.contains("cay") -> "Karadem"
                lowerName.contains("şeker") || lowerName.contains("seker") -> "Petek"
                lowerName.contains("salça") || lowerName.contains("salca") || lowerName.contains("reçel") || lowerName.contains("recel") || lowerName.contains("konserve") -> "Çokça"
                lowerName.contains("fındık kreması") || lowerName.contains("findik kremasi") -> "Findux"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") || lowerName.contains("sucuk") || lowerName.contains("salam") -> "Kombinet"
                lowerName.contains("piliç") || lowerName.contains("pilic") || lowerName.contains("tavuk") -> "Gedik"
                lowerName.contains("yumurta") -> "Keskinoğlu"
                lowerName.contains("deterjan") || lowerName.contains("çamaşır") || lowerName.contains("camasir") || lowerName.contains("bulaşık") || lowerName.contains("bulasik") || lowerName.contains("temizlik") || lowerName.contains("yumuşatıcı") -> "Çiçeğim"
                lowerName.contains("tuvalet") || lowerName.contains("havlu") || lowerName.contains("peçete") -> "Familia"
                category == "Meyve & Sebze" -> "A101 Manav"
                else -> "A101"
            }
            "sok" -> when {
                lowerName.contains("laktozsuz") -> "Mis Laktozsuz"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("peynir") || lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("yoğurt") || lowerName.contains("yogurt") || lowerName.contains("ayran") || lowerName.contains("tereyağ") || lowerName.contains("tereyag") || lowerName.contains("kaymak") || lowerName.contains("yumurta") -> "Mis"
                lowerName.contains("makarna") || lowerName.contains("un") || lowerName.contains("şehriye") || lowerName.contains("sehriye") -> "Piyale"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("yağ") || lowerName.contains("yag") -> "Evin"
                lowerName.contains("zeytin") || lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") -> "Lio"
                lowerName.contains("pirinç") || lowerName.contains("pirinc") || lowerName.contains("bulgur") || lowerName.contains("mercimek") || lowerName.contains("nohut") || lowerName.contains("fasulye") || lowerName.contains("bakliyat") -> "Anadolu Mutfağı"
                lowerName.contains("deterjan") || lowerName.contains("bulaşık") || lowerName.contains("bulasik") || lowerName.contains("temizlik") || lowerName.contains("çamaşır suyu") || lowerName.contains("camasir suyu") || lowerName.contains("yüzey") || lowerName.contains("yuzey") || lowerName.contains("yağ çözücü") -> "Mintax"
                lowerName.contains("çay") || lowerName.contains("cay") -> "Deren"
                lowerName.contains("tuvalet") || lowerName.contains("havlu") || lowerName.contains("peçete") -> "Confort"
                lowerName.contains("şeker") || lowerName.contains("seker") -> "Altınküp"
                lowerName.contains("salça") || lowerName.contains("salca") || lowerName.contains("reçel") || lowerName.contains("recel") || lowerName.contains("turşu") -> "Vatan"
                lowerName.contains("fındık kreması") || lowerName.contains("findik kremasi") -> "Karmen"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") -> "Mis Kasap"
                lowerName.contains("piliç") || lowerName.contains("pilic") || lowerName.contains("tavuk") -> "Lezita"
                category == "Meyve & Sebze" -> "ŞOK Manav"
                else -> "ŞOK"
            }
            "tarimkredi" -> when {
                lowerName.contains("laktozsuz") -> "TK Laktozsuz Süt"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("yoğurt") || lowerName.contains("ayran") -> "TK Süt"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") || lowerName.contains("yağ") || lowerName.contains("yag") -> "TK Birlik"
                lowerName.contains("çay") || lowerName.contains("cay") -> "TK Filiz"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") || lowerName.contains("sucuk") || lowerName.contains("antrikot") -> "TK Kasap"
                lowerName.contains("peynir") || lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("ezine") || lowerName.contains("tulum") -> "TK Peynir"
                lowerName.contains("bal") -> "TK Bal"
                lowerName.contains("şeker") || lowerName.contains("seker") -> "TK Şeker"
                lowerName.contains("tuz") -> "TK Tuz"
                lowerName.contains("zeytin") -> "TK Gemlik"
                category == "Meyve & Sebze" -> "TK Doğal Manav"
                else -> "TK Koop"
            }
            "migros" -> when {
                lowerName.contains("laktozsuz") -> "İçim Rahat Laktozsuz"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("yoğurt") || lowerName.contains("yogurt") || lowerName.contains("ayran") -> "İçim"
                lowerName.contains("peynir") || lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("ezine") -> "Sütaş"
                lowerName.contains("tereyağ") || lowerName.contains("tereyag") || lowerName.contains("kaymak") -> "Sütaş"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") || lowerName.contains("yağ") || lowerName.contains("yag") -> "Yudum"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") || lowerName.contains("antrikot") || lowerName.contains("biftek") || lowerName.contains("sucuk") -> "Uzman Kasap"
                lowerName.contains("piliç") || lowerName.contains("pilic") || lowerName.contains("tavuk") -> "Banvit"
                lowerName.contains("çay") || lowerName.contains("cay") -> "Çaykur Rize"
                lowerName.contains("deterjan") || lowerName.contains("çamaşır") -> "Ariel"
                lowerName.contains("bulaşık") || lowerName.contains("tablet") -> "Finish"
                lowerName.contains("tuvalet") || lowerName.contains("havlu") -> "Papia"
                lowerName.contains("makarna") -> "Barilla"
                lowerName.contains("pirinç") || lowerName.contains("bulgur") || lowerName.contains("bakliyat") -> "Reis"
                lowerName.contains("salça") || lowerName.contains("salca") -> "Tat"
                lowerName.contains("zeytin") -> "Marmarabirlik"
                lowerName.contains("bal") -> "Balparmak"
                lowerName.contains("şampuan") || lowerName.contains("sampuan") -> "Head & Shoulders"
                category == "Meyve & Sebze" -> "Migros İyi Tarım"
                else -> "Migros"
            }
            "carrefoursa" -> when {
                lowerName.contains("laktozsuz") -> "Pınar Denge Laktozsuz"
                lowerName.contains("süt") || lowerName.contains("sut") || lowerName.contains("yoğurt") || lowerName.contains("yogurt") || lowerName.contains("ayran") -> "Pınar"
                lowerName.contains("peynir") || lowerName.contains("kaşar") || lowerName.contains("kasar") || lowerName.contains("ezine") -> "Pınar"
                lowerName.contains("tereyağ") || lowerName.contains("tereyag") || lowerName.contains("kaymak") -> "Pınar"
                lowerName.contains("ayçiçek") || lowerName.contains("aycicek") || lowerName.contains("zeytinyağ") || lowerName.contains("zeytinyag") || lowerName.contains("yağ") || lowerName.contains("yag") -> "Carrefour"
                lowerName.contains("kıyma") || lowerName.contains("kiyma") || lowerName.contains("kuşbaşı") || lowerName.contains("kusbasi") || lowerName.contains("köfte") || lowerName.contains("kofte") || lowerName.contains("antrikot") || lowerName.contains("biftek") -> "Carrefour Kasap"
                lowerName.contains("piliç") || lowerName.contains("pilic") || lowerName.contains("tavuk") -> "Lezita"
                lowerName.contains("çay") || lowerName.contains("cay") -> "Çaykur Tiryaki"
                lowerName.contains("deterjan") || lowerName.contains("çamaşır") -> "Omo"
                lowerName.contains("bulaşık") || lowerName.contains("tablet") -> "Fairy"
                lowerName.contains("tuvalet") || lowerName.contains("havlu") -> "Selpak Deluxe"
                lowerName.contains("makarna") -> "Filiz"
                lowerName.contains("pirinç") || lowerName.contains("bulgur") || lowerName.contains("bakliyat") -> "Duru"
                lowerName.contains("salça") || lowerName.contains("salca") -> "Öncü"
                lowerName.contains("zeytin") -> "Marmarabirlik"
                lowerName.contains("bal") -> "Balparmak"
                lowerName.contains("şampuan") || lowerName.contains("sampuan") -> "Elidor"
                category == "Meyve & Sebze" -> "Carrefour Taze Bahçe"
                else -> "Carrefour"
            }
            else -> marketId.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }
    }

    /**
     * Generates genuine market-specific offerings for the given product name, category, quantity, unit and optional selected brand.
     * Strictly verifies market availability so private label brands (e.g. Mis -> ŞOK only, Dost -> BİM only)
     * never appear in competitor stores like Migros.
     */
    fun getMarketOffersForProduct(
        productName: String,
        category: String = "Genel",
        quantity: Double = 1.0,
        unit: String = "Adet",
        selectedBrand: String? = null
    ): List<MarketProductOption> {
        val qty = if (quantity > 0) quantity else 1.0
        val cleanName = productName.trim()
        val hasBrandPreference = !selectedBrand.isNullOrBlank() &&
            !selectedBrand.contains("Tümü", ignoreCase = true) &&
            !selectedBrand.contains("Farketmez", ignoreCase = true)

        val allMarketConfigs = listOf(
            Triple("bim", "BİM", "🏬"),
            Triple("a101", "A101", "🛒"),
            Triple("sok", "ŞOK", "⚡"),
            Triple("tarimkredi", "Tarım Kredi", "🌾"),
            Triple("migros", "Migros", "🛍️"),
            Triple("carrefoursa", "CarrefourSA", "🏪")
        )

        // Filter markets strictly based on either selectedBrand or brand inside productName
        val brandQuery = if (hasBrandPreference) selectedBrand else cleanName
        val allowedMarketIds = getValidMarketsForBrand(brandQuery, cleanName)

        val activeMarketConfigs = allMarketConfigs.filter { it.first in allowedMarketIds }

        // If no market matches the specific filter (fallback safeguard), default to active markets
        val targetConfigs = if (activeMarketConfigs.isNotEmpty()) {
            activeMarketConfigs
        } else {
            allMarketConfigs
        }

        return targetConfigs.map { (mId, mName, mIcon) ->
            val marketInfo = MarketPriceEngine.MARKETS.find { it.id == mId } ?: MarketInfo(
                id = mId, name = mName, shortName = mName, icon = mIcon,
                brandColorHex = 0xFF0072CE, description = mName, tagLine = ""
            )

            // Determine authentic brand name for this specific market
            val brandName = if (hasBrandPreference && activeMarketConfigs.size <= 1) {
                // If user selected an exclusive brand (e.g. Mis), use it cleanly
                selectedBrand!!.replace(Regex("\\(.*\\)"), "").trim()
            } else {
                getRealMarketBrandForProduct(mId, cleanName, category)
            }

            // Calculate precise benchmark price
            val tempItem = ShoppingItem(
                id = 0L,
                name = if (cleanName.isNotBlank()) cleanName else "Ürün",
                category = category,
                quantity = qty,
                unit = unit,
                isPurchased = false
            )
            val (baseUnitPrice, unitDisplay, calculatedTotalPrice) = MarketPriceEngine.calculateItemPriceForMarket(
                tempItem, marketInfo, selectedBrand
            )

            val displayName = if (cleanName.isNotBlank()) {
                if (cleanName.contains(brandName, ignoreCase = true)) {
                    cleanName
                } else {
                    "$brandName $cleanName"
                }
            } else {
                "$brandName ($mName)"
            }

            MarketProductOption(
                id = "${mId}_${cleanName.hashCode()}_${brandName.hashCode()}",
                marketId = mId,
                marketName = mName,
                marketIcon = mIcon,
                marketBrand = brandName,
                fullProductName = "$mName • $displayName",
                displayName = displayName,
                price = calculatedTotalPrice,
                unitPriceDisplay = unitDisplay,
                unit = unit,
                quantity = qty
            )
        }
    }
}
