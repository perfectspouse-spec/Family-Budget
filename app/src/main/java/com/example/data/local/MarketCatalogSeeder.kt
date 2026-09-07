package com.example.data.local

import com.example.data.model.MarketProductMapping

object MarketCatalogSeeder {

    fun getDefaultMappings(): List<MarketProductMapping> {
        val list = mutableListOf<MarketProductMapping>()

        fun add(
            key: String,
            name: String,
            brand: String,
            category: String,
            unit: String,
            qty: Double,
            marketId: String,
            marketName: String,
            marketProdName: String,
            price: Double,
            isPrivateLabel: Boolean = false
        ) {
            val unitDisplay = "%.2f ₺ / %s".format(java.util.Locale.forLanguageTag("tr"), price, unit)
            list.add(
                MarketProductMapping(
                    productKey = key,
                    productName = name,
                    brand = brand,
                    category = category,
                    unit = unit,
                    standardQuantity = qty,
                    marketId = marketId,
                    marketName = marketName,
                    marketProductName = marketProdName,
                    price = price,
                    unitPriceDisplay = unitDisplay,
                    isAvailable = true,
                    isPrivateLabel = isPrivateLabel
                )
            )
        }

        // ==========================================
        // 1. ŞOK EXCLUSIVE PRIVATE LABELS (MIS, EVIN, PIYALE, MINTAX, DEREN, ALTINKUP, LIO, CONFORT, MIS KASAP)
        // Strictly available ONLY at ŞOK!
        // ==========================================
        add("mis laktozsuz sut", "Mis Laktozsuz Süt (1L)", "Mis", "Süt & Kahvaltılık", "Lt", 1.0, "sok", "ŞOK", "Mis Laktozsuz Süt 1L", 37.00, true)
        add("mis sut tam yagli", "Mis Tam Yağlı Süt (1L)", "Mis", "Süt & Kahvaltılık", "Lt", 1.0, "sok", "ŞOK", "Mis Tam Yağlı Süt 1L", 31.50, true)
        add("mis sut yarim yagli", "Mis Yarım Yağlı Süt (1L)", "Mis", "Süt & Kahvaltılık", "Lt", 1.0, "sok", "ŞOK", "Mis Yarım Yağlı Süt 1L", 28.50, true)
        add("mis kasar peyniri", "Mis Taze Kaşar Peyniri (400g)", "Mis", "Süt & Kahvaltılık", "Gram", 400.0, "sok", "ŞOK", "Mis Taze Kaşar 400g", 115.00, true)
        add("mis suzme peynir", "Mis Süzme Peynir (500g)", "Mis", "Süt & Kahvaltılık", "Gram", 500.0, "sok", "ŞOK", "Mis Süzme Peynir 500g", 72.50, true)
        add("mis beyaz peynir", "Mis Tam Yağlı Beyaz Peynir (500g)", "Mis", "Süt & Kahvaltılık", "Gram", 500.0, "sok", "ŞOK", "Mis Beyaz Peynir 500g", 89.00, true)
        add("mis tereyagi", "Mis Geleneksel Tereyağı (250g)", "Mis", "Süt & Kahvaltılık", "Gram", 250.0, "sok", "ŞOK", "Mis Tereyağı 250g", 85.00, true)
        add("mis yogurt", "Mis Homojenize Yoğurt (1.5kg)", "Mis", "Süt & Kahvaltılık", "Kg", 1.5, "sok", "ŞOK", "Mis Yoğurt 1500g", 52.50, true)
        add("mis ayran", "Mis Ayran (1.5L)", "Mis", "İçecekler", "Lt", 1.5, "sok", "ŞOK", "Mis Ayran 1.5L", 34.50, true)
        add("mis labne", "Mis Labne Peyniri (400g)", "Mis", "Süt & Kahvaltılık", "Gram", 400.0, "sok", "ŞOK", "Mis Labne 400g", 54.00, true)
        add("mis yumurta", "Mis Yumurta M Boy (15'li)", "Mis", "Süt & Kahvaltılık", "Koli", 1.0, "sok", "ŞOK", "Mis Yumurta 15'li", 48.50, true)
        add("evin aycicek yagi", "Evin Ayçiçek Yağı (5L)", "Evin", "Temel Gıda & Yağ", "Lt", 5.0, "sok", "ŞOK", "Evin Ayçiçek Yağı Pet 5L", 229.00, true)
        add("piyale un", "Piyale Buğday Unu (5kg)", "Piyale", "Temel Gıda & Yağ", "Kg", 5.0, "sok", "ŞOK", "Piyale Un 5kg", 79.50, true)
        add("piyale makarna", "Piyale Makarna (500g)", "Piyale", "Temel Gıda & Yağ", "Paket", 1.0, "sok", "ŞOK", "Piyale Burgu/Spagetti 500g", 11.50, true)
        add("mintax bulasik tableti", "Mintax Hepsi 1 Arada Bulaşık Tableti (50'li)", "Mintax", "Temizlik & Deterjan", "Paket", 1.0, "sok", "ŞOK", "Mintax Bulaşık Tableti 50'li", 145.00, true)
        add("mintax bulasik deterjani", "Mintax Sıvı Bulaşık Deterjanı (750ml)", "Mintax", "Temizlik & Deterjan", "Adet", 1.0, "sok", "ŞOK", "Mintax Sıvı Bulaşık Deterjanı 750ml", 32.50, true)
        add("mintax camasir suyu", "Mintax Yoğun Çamaşır Suyu (750ml)", "Mintax", "Temizlik & Deterjan", "Adet", 1.0, "sok", "ŞOK", "Mintax Çamaşır Suyu 750ml", 24.50, true)
        add("anadolu mutfagi kirmizi mercimek", "Anadolu Mutfağı Kırmızı Mercimek (1kg)", "Anadolu Mutfağı", "Temel Gıda & Yağ", "Kg", 1.0, "sok", "ŞOK", "Anadolu Mutfağı Kırmızı Mercimek 1kg", 36.50, true)
        add("anadolu mutfagi pirinc", "Anadolu Mutfağı Pilavlık Pirinç (1kg)", "Anadolu Mutfağı", "Temel Gıda & Yağ", "Kg", 1.0, "sok", "ŞOK", "Anadolu Mutfağı Pilavlık Pirinç 1kg", 42.50, true)
        add("anadolu mutfagi nohut", "Anadolu Mutfağı Nohut (1kg)", "Anadolu Mutfağı", "Temel Gıda & Yağ", "Kg", 1.0, "sok", "ŞOK", "Anadolu Mutfağı Nohut 1kg", 46.00, true)
        add("anadolu mutfagi bulgur", "Anadolu Mutfağı Pilavlık Bulgur (1kg)", "Anadolu Mutfağı", "Temel Gıda & Yağ", "Kg", 1.0, "sok", "ŞOK", "Anadolu Mutfağı Pilavlık Bulgur 1kg", 24.50, true)
        add("deren cay", "Deren Rize Çayı (1kg)", "Deren", "İçecekler", "Kg", 1.0, "sok", "ŞOK", "Deren Karadeniz Çayı 1kg", 119.00, true)
        add("altinkup seker", "Altınküp Toz Şeker (1kg)", "Altınküp", "Temel Gıda & Yağ", "Kg", 1.0, "sok", "ŞOK", "Altınküp Toz Şeker 1kg", 34.00, true)
        add("lio zeytin", "Lio Siyah Zeytin (500g)", "Lio", "Süt & Kahvaltılık", "Gram", 500.0, "sok", "ŞOK", "Lio Doğal Salamura Siyah Zeytin 500g", 68.00, true)
        add("confort tuvalet kagidi", "Confort Çift Katlı Tuvalet Kağıdı (16'lı)", "Confort", "Kağıt & Hijyen", "Paket", 1.0, "sok", "ŞOK", "Confort Tuvalet Kağıdı 16'lı", 89.90, true)
        add("confort kagit havlu", "Confort Kağıt Havlu (6'lı)", "Confort", "Kağıt & Hijyen", "Paket", 1.0, "sok", "ŞOK", "Confort Havlu 6'lı", 54.50, true)
        add("mis kasap kiyma", "Mis Kasap Dana Kıyma %20 Yağlı (400g)", "Mis Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "sok", "ŞOK", "Mis Kasap Dana Kıyma 400g", 148.00, true)
        add("mis kasap kusbasi", "Mis Kasap Dana Kuşbaşı (400g)", "Mis Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "sok", "ŞOK", "Mis Kasap Dana Kuşbaşı 400g", 165.00, true)

        // ==========================================
        // 2. BİM EXCLUSIVE PRIVATE LABELS (DOST, AKNAZ, KEREM, SOLE, EFSANE, CARDELLA, SABAN, BERK, SAFAK, INCI, QUEEN, GULDAL, BILL, EMIN KASAP)
        // Strictly available ONLY at BİM!
        // ==========================================
        add("dost laktozsuz sut", "Dost Laktozsuz Süt (1L)", "Dost", "Süt & Kahvaltılık", "Lt", 1.0, "bim", "BİM", "Dost Laktozsuz Süt 1L", 36.50, true)
        add("dost sut tam yagli", "Dost Tam Yağlı Süt (1L)", "Dost", "Süt & Kahvaltılık", "Lt", 1.0, "bim", "BİM", "Dost Tam Yağlı Süt 1L", 31.50, true)
        add("dost sut yarim yagli", "Dost Yarım Yağlı Süt (1L)", "Dost", "Süt & Kahvaltılık", "Lt", 1.0, "bim", "BİM", "Dost Yarım Yağlı Süt 1L", 28.50, true)
        add("dost yogurt", "Dost Homojenize Yoğurt (1.5kg)", "Dost", "Süt & Kahvaltılık", "Kg", 1.5, "bim", "BİM", "Dost Yoğurt 1500g", 52.50, true)
        add("dost ayran", "Dost Ayran (1.5L)", "Dost", "İçecekler", "Lt", 1.5, "bim", "BİM", "Dost Ayran 1.5L", 34.50, true)
        add("aknaz beyaz peynir", "Aknaz Tam Yağlı Beyaz Peynir (500g)", "Aknaz", "Süt & Kahvaltılık", "Gram", 500.0, "bim", "BİM", "Aknaz Beyaz Peynir 500g", 89.00, true)
        add("aknaz kasar peyniri", "Aknaz Taze Kaşar Peyniri (400g)", "Aknaz", "Süt & Kahvaltılık", "Gram", 400.0, "bim", "BİM", "Aknaz Kaşar Peyniri 400g", 115.00, true)
        add("kerem suzme peynir", "Kerem Süzme Peynir (500g)", "Kerem", "Süt & Kahvaltılık", "Gram", 500.0, "bim", "BİM", "Kerem Süzme Peynir 500g", 72.50, true)
        add("kerem labne", "Kerem Labne (400g)", "Kerem", "Süt & Kahvaltılık", "Gram", 400.0, "bim", "BİM", "Kerem Labne 400g", 54.00, true)
        add("bili bili yumurta", "Bili Bili Yumurta M Boy (15'li)", "Bili Bili", "Süt & Kahvaltılık", "Koli", 1.0, "bim", "BİM", "Bili Bili Yumurta 15'li", 48.00, true)
        add("sole aycicek yagi", "Sole Ayçiçek Yağı (5L)", "Sole", "Temel Gıda & Yağ", "Lt", 5.0, "bim", "BİM", "Sole Ayçiçek Yağı 5L", 229.00, true)
        add("efsane un", "Efsane Buğday Unu (5kg)", "Efsane", "Temel Gıda & Yağ", "Kg", 5.0, "bim", "BİM", "Efsane Un 5kg", 79.50, true)
        add("cardella makarna", "Cardella Makarna (500g)", "Cardella", "Temel Gıda & Yağ", "Paket", 1.0, "bim", "BİM", "Cardella Makarna 500g", 11.50, true)
        add("saban kirmizi mercimek", "Saban Kırmızı Mercimek (1kg)", "Saban", "Temel Gıda & Yağ", "Kg", 1.0, "bim", "BİM", "Saban Kırmızı Mercimek 1kg", 36.50, true)
        add("saban pirinc", "Saban Pilavlık Pirinç (1kg)", "Saban", "Temel Gıda & Yağ", "Kg", 1.0, "bim", "BİM", "Saban Pilavlık Pirinç 1kg", 42.50, true)
        add("saban bulgur", "Saban Pilavlık Bulgur (1kg)", "Saban", "Temel Gıda & Yağ", "Kg", 1.0, "bim", "BİM", "Saban Bulgur 1kg", 24.50, true)
        add("saban nohut", "Saban Nohut (1kg)", "Saban", "Temel Gıda & Yağ", "Kg", 1.0, "bim", "BİM", "Saban Nohut 1kg", 46.00, true)
        add("berk cay", "Berk Karadeniz Çay (1kg)", "Berk", "İçecekler", "Kg", 1.0, "bim", "BİM", "Berk Rize Çayı 1kg", 119.00, true)
        add("safak seker", "Şafak Toz Şeker (1kg)", "Şafak", "Temel Gıda & Yağ", "Kg", 1.0, "bim", "BİM", "Şafak Toz Şeker 1kg", 34.00, true)
        add("inci zeytin", "İnci Siyah Zeytin (500g)", "İnci", "Süt & Kahvaltılık", "Gram", 500.0, "bim", "BİM", "İnci Salamura Siyah Zeytin 500g", 68.00, true)
        add("queen tuvalet kagidi", "Queen 3 Katlı Tuvalet Kağıdı (16'lı)", "Queen", "Kağıt & Hijyen", "Paket", 1.0, "bim", "BİM", "Queen Tuvalet Kağıdı 16'lı", 89.90, true)
        add("queen kagit havlu", "Queen 3 Katlı Kağıt Havlu (6'lı)", "Queen", "Kağıt & Hijyen", "Paket", 1.0, "bim", "BİM", "Queen Kağıt Havlu 6'lı", 54.50, true)
        add("guldal camasir suyu", "Güldal Ultra Çamaşır Suyu (750ml)", "Güldal", "Temizlik & Deterjan", "Adet", 1.0, "bim", "BİM", "Güldal Çamaşır Suyu 750ml", 24.50, true)
        add("bill bulasik deterjani", "Bill Sıvı Bulaşık Deterjanı (750ml)", "Bill", "Temizlik & Deterjan", "Adet", 1.0, "bim", "BİM", "Bill Bulaşık Deterjanı 750ml", 32.50, true)
        add("emin kasap kiyma", "Emin Kasap Dana Kıyma (400g)", "Emin Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "bim", "BİM", "Emin Dana Kıyma 400g", 148.00, true)
        add("emin kasap kusbasi", "Emin Kasap Dana Kuşbaşı (400g)", "Emin Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "bim", "BİM", "Emin Dana Kuşbaşı 400g", 165.00, true)

        // ==========================================
        // 3. A101 EXCLUSIVE PRIVATE LABELS (BIRSAH, PEYNES, AHIR, VERA, YEGENLER, BENDO, OVADAN, KARADEM, PETEK, ZEO, MISTRAL, CICEGIM, KOMBIINET)
        // Strictly available ONLY at A101!
        // ==========================================
        add("birsah laktozsuz sut", "Birşah Laktozsuz Süt (1L)", "Birşah", "Süt & Kahvaltılık", "Lt", 1.0, "a101", "A101", "Birşah Laktozsuz Süt 1L", 36.50, true)
        add("birsah sut tam yagli", "Birşah Tam Yağlı Süt (1L)", "Birşah", "Süt & Kahvaltılık", "Lt", 1.0, "a101", "A101", "Birşah Tam Yağlı Süt 1L", 31.50, true)
        add("birsah sut yarim yagli", "Birşah Yarım Yağlı Süt (1L)", "Birşah", "Süt & Kahvaltılık", "Lt", 1.0, "a101", "A101", "Birşah Yarım Yağlı Süt 1L", 28.50, true)
        add("birsah yogurt", "Birşah Homojenize Yoğurt (1.5kg)", "Birşah", "Süt & Kahvaltılık", "Kg", 1.5, "a101", "A101", "Birşah Yoğurt 1500g", 52.50, true)
        add("birsah ayran", "Birşah Ayran (1.5L)", "Birşah", "İçecekler", "Lt", 1.5, "a101", "A101", "Birşah Ayran 1.5L", 34.50, true)
        add("peynes beyaz peynir", "Peynes Tam Yağlı Beyaz Peynir (500g)", "Peynes", "Süt & Kahvaltılık", "Gram", 500.0, "a101", "A101", "Peynes Beyaz Peynir 500g", 89.00, true)
        add("peynes suzme peynir", "Peynes Süzme Peynir (500g)", "Peynes", "Süt & Kahvaltılık", "Gram", 500.0, "a101", "A101", "Peynes Süzme Peynir 500g", 72.50, true)
        add("ahir kasar peyniri", "Ahir Taze Kaşar Peyniri (400g)", "Ahir", "Süt & Kahvaltılık", "Gram", 400.0, "a101", "A101", "Ahir Taze Kaşar 400g", 115.00, true)
        add("vera aycicek yagi", "Vera Ayçiçek Yağı (5L)", "Vera", "Temel Gıda & Yağ", "Lt", 5.0, "a101", "A101", "Vera Ayçiçek Yağı 5L", 229.00, true)
        add("yegenler un", "Yeğenler Buğday Unu (5kg)", "Yeğenler", "Temel Gıda & Yağ", "Kg", 5.0, "a101", "A101", "Yeğenler Un 5kg", 79.50, true)
        add("bendo makarna", "Bendo Makarna (500g)", "Bendo", "Temel Gıda & Yağ", "Paket", 1.0, "a101", "A101", "Bendo Makarna 500g", 11.50, true)
        add("ovadan pirinc", "Ovadan Baldo Pirinç (1kg)", "Ovadan", "Temel Gıda & Yağ", "Kg", 1.0, "a101", "A101", "Ovadan Baldo Pirinç 1kg", 46.00, true)
        add("ovadan kirmizi mercimek", "Ovadan Kırmızı Mercimek (1kg)", "Ovadan", "Temel Gıda & Yağ", "Kg", 1.0, "a101", "A101", "Ovadan Kırmızı Mercimek 1kg", 36.50, true)
        add("ovadan nohut", "Ovadan Nohut (1kg)", "Ovadan", "Temel Gıda & Yağ", "Kg", 1.0, "a101", "A101", "Ovadan Nohut 1kg", 46.00, true)
        add("ovadan bulgur", "Ovadan Pilavlık Bulgur (1kg)", "Ovadan", "Temel Gıda & Yağ", "Kg", 1.0, "a101", "A101", "Ovadan Pilavlık Bulgur 1kg", 24.50, true)
        add("karadem cay", "Karadem Karadeniz Çayı (1kg)", "Karadem", "İçecekler", "Kg", 1.0, "a101", "A101", "Karadem Çay 1kg", 119.00, true)
        add("petek seker", "Petek Toz Şeker (1kg)", "Petek", "Temel Gıda & Yağ", "Kg", 1.0, "a101", "A101", "Petek Toz Şeker 1kg", 34.00, true)
        add("zeo zeytin", "Zeo Doğal Siyah Zeytin (500g)", "Zeo", "Süt & Kahvaltılık", "Gram", 500.0, "a101", "A101", "Zeo Siyah Zeytin 500g", 68.00, true)
        add("mistral tuvalet kagidi", "Mistral Çift Katlı Tuvalet Kağıdı (16'lı)", "Mistral", "Kağıt & Hijyen", "Paket", 1.0, "a101", "A101", "Mistral Tuvalet Kağıdı 16'lı", 89.90, true)
        add("mistral kagit havlu", "Mistral Kağıt Havlu (6'lı)", "Mistral", "Kağıt & Hijyen", "Paket", 1.0, "a101", "A101", "Mistral Kağıt Havlu 6'lı", 54.50, true)
        add("cicegim camasir suyu", "Çiçeğim Çamaşır Suyu (750ml)", "Çiçeğim", "Temizlik & Deterjan", "Adet", 1.0, "a101", "A101", "Çiçeğim Çamaşır Suyu 750ml", 24.50, true)
        add("cicegim bulasik deterjani", "Çiçeğim Sıvı Bulaşık Deterjanı (750ml)", "Çiçeğim", "Temizlik & Deterjan", "Adet", 1.0, "a101", "A101", "Çiçeğim Bulaşık Deterjanı 750ml", 32.50, true)
        add("kombinet kiyma", "Kombinet Dana Kıyma (400g)", "Kombinet", "Et, Tavuk & Balık", "Gram", 400.0, "a101", "A101", "Kombinet Dana Kıyma 400g", 148.00, true)
        add("kombinet kusbasi", "Kombinet Dana Kuşbaşı (400g)", "Kombinet", "Et, Tavuk & Balık", "Gram", 400.0, "a101", "A101", "Kombinet Dana Kuşbaşı 400g", 165.00, true)

        // ==========================================
        // 4. TARIM KREDİ EXCLUSIVE PRIVATE LABELS (TK KOOP, TK BIRLIK, TK KASAP, TK FILIZ)
        // Strictly available ONLY at Tarım Kredi!
        // ==========================================
        add("tk laktozsuz sut", "TK Laktozsuz Süt (1L)", "TK Koop", "Süt & Kahvaltılık", "Lt", 1.0, "tarimkredi", "Tarım Kredi", "Tarım Kredi Laktozsuz Süt 1L", 38.00, true)
        add("tk sut tam yagli", "TK Tam Yağlı Süt (1L)", "TK Koop", "Süt & Kahvaltılık", "Lt", 1.0, "tarimkredi", "Tarım Kredi", "Tarım Kredi Süt 1L", 32.00, true)
        add("tk sut yarim yagli", "TK Yarım Yağlı Süt (1L)", "TK Koop", "Süt & Kahvaltılık", "Lt", 1.0, "tarimkredi", "Tarım Kredi", "Tarım Kredi Yarım Yağlı Süt 1L", 29.00, true)
        add("tk yogurt", "TK Doğal Yoğurt (1.5kg)", "TK Koop", "Süt & Kahvaltılık", "Kg", 1.5, "tarimkredi", "Tarım Kredi", "Tarım Kredi Yoğurt 1500g", 54.00, true)
        add("tk beyaz peynir", "TK Tam Yağlı Beyaz Peynir (500g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 500.0, "tarimkredi", "Tarım Kredi", "TK Beyaz Peynir 500g", 92.00, true)
        add("tk kasar peyniri", "TK Taze Kaşar Peyniri (400g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 400.0, "tarimkredi", "Tarım Kredi", "TK Taze Kaşar 400g", 118.00, true)
        add("tk ezine peyniri", "TK Olgunlaştırılmış Ezine Peyniri (350g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 350.0, "tarimkredi", "Tarım Kredi", "TK Ezine Peyniri 350g", 105.00, true)
        add("tk tereyagi", "TK Geleneksel Tereyağı (250g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 250.0, "tarimkredi", "Tarım Kredi", "TK Tereyağı 250g", 89.00, true)
        add("tk birlik aycicek yagi", "TK Birlik Ayçiçek Yağı (5L)", "TK Birlik", "Temel Gıda & Yağ", "Lt", 5.0, "tarimkredi", "Tarım Kredi", "TK Birlik Ayçiçek Yağı 5L", 235.00, true)
        add("tk birlik zeytinyagi", "TK Birlik Natürel Sızma Zeytinyağı (1L)", "TK Birlik", "Temel Gıda & Yağ", "Lt", 1.0, "tarimkredi", "Tarım Kredi", "TK Birlik Sızma Zeytinyağı 1L", 285.00, true)
        add("tk un", "TK Buğday Unu (5kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 5.0, "tarimkredi", "Tarım Kredi", "TK Buğday Unu 5kg", 82.00, true)
        add("tk makarna", "TK Makarna Çeşitleri (500g)", "TK Koop", "Temel Gıda & Yağ", "Paket", 1.0, "tarimkredi", "Tarım Kredi", "TK Makarna 500g", 12.00, true)
        add("tk pirinc", "TK Baldo Pirinç (1kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK Baldo Pirinç 1kg", 44.00, true)
        add("tk kirmizi mercimek", "TK Kırmızı Mercimek (1kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK Kırmızı Mercimek 1kg", 38.00, true)
        add("tk nohut", "TK Koop Nohut (1kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK Koop Nohut 1kg", 48.00, true)
        add("tk kuru fasulye", "TK İspir Kuru Fasulye (1kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK İspir Kuru Fasulye 1kg", 58.00, true)
        add("tk filiz cay", "TK Filiz Siyah Çay (1kg)", "TK Filiz", "İçecekler", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK Filiz Çay 1kg", 125.00, true)
        add("tk seker", "TK Toz Şeker (1kg)", "TK Koop", "Temel Gıda & Yağ", "Kg", 1.0, "tarimkredi", "Tarım Kredi", "TK Toz Şeker 1kg", 35.00, true)
        add("tk zeytin", "TK Gemlik Siyah Zeytin (500g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 500.0, "tarimkredi", "Tarım Kredi", "TK Gemlik Zeytin 500g", 72.00, true)
        add("tk bal", "TK Süzme Çiçek Balı (850g)", "TK Koop", "Süt & Kahvaltılık", "Gram", 850.0, "tarimkredi", "Tarım Kredi", "TK Çiçek Balı 850g", 165.00, true)
        add("tk kasap kiyma", "TK Kasap Dana Kıyma (400g)", "TK Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "tarimkredi", "Tarım Kredi", "TK Kasap Dana Kıyma 400g", 152.00, true)
        add("tk kasap kusbasi", "TK Kasap Dana Kuşbaşı (400g)", "TK Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "tarimkredi", "Tarım Kredi", "TK Kasap Dana Kuşbaşı 400g", 170.00, true)
        add("tk kasap sucuk", "TK Kasap Fermente Sucuk (300g)", "TK Kasap", "Et, Tavuk & Balık", "Gram", 300.0, "tarimkredi", "Tarım Kredi", "TK Kasap Fermente Sucuk 300g", 145.00, true)

        // ==========================================
        // 5. MIGROS EXCLUSIVE PRIVATE LABELS (UZMAN KASAP, M LIFE, MIGROS PRIVATE LABEL)
        // Strictly available ONLY at Migros!
        // ==========================================
        add("uzman kasap kiyma", "Uzman Kasap Dana Kıyma %15 Yağlı (400g)", "Uzman Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "migros", "Migros", "Uzman Kasap Dana Kıyma 400g", 162.00, true)
        add("uzman kasap kusbasi", "Uzman Kasap Dana Kuşbaşı (400g)", "Uzman Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "migros", "Migros", "Uzman Kasap Dana Kuşbaşı 400g", 185.00, true)
        add("uzman kasap kofte", "Uzman Kasap Izgara Köfte (320g)", "Uzman Kasap", "Et, Tavuk & Balık", "Gram", 320.0, "migros", "Migros", "Uzman Kasap Izgara Köfte 320g", 142.00, true)
        add("uzman kasap sucuk", "Uzman Kasap Fermente Dana Sucuk (300g)", "Uzman Kasap", "Et, Tavuk & Balık", "Gram", 300.0, "migros", "Migros", "Uzman Kasap Dana Sucuk 300g", 165.00, true)
        add("m life organik yumurta", "M-Life Organik Yumurta M Boy (10'lu)", "M-Life", "Süt & Kahvaltılık", "Koli", 1.0, "migros", "Migros", "M-Life Organik Yumurta 10'lu", 72.00, true)
        add("migros aycicek yagi", "Migros Ayçiçek Yağı (5L)", "Migros", "Temel Gıda & Yağ", "Lt", 5.0, "migros", "Migros", "Migros Ayçiçek Yağı 5L", 249.90, true)
        add("migros un", "Migros Buğday Unu (5kg)", "Migros", "Temel Gıda & Yağ", "Kg", 5.0, "migros", "Migros", "Migros Un 5kg", 89.00, true)
        add("migros pirinc", "Migros Baldo Pirinç (1kg)", "Migros", "Temel Gıda & Yağ", "Kg", 1.0, "migros", "Migros", "Migros Baldo Pirinç 1kg", 58.00, true)
        add("migros kirmizi mercimek", "Migros Kırmızı Mercimek (1kg)", "Migros", "Temel Gıda & Yağ", "Kg", 1.0, "migros", "Migros", "Migros Kırmızı Mercimek 1kg", 46.00, true)
        add("migros kasar peyniri", "Migros Taze Kaşar Peyniri (400g)", "Migros", "Süt & Kahvaltılık", "Gram", 400.0, "migros", "Migros", "Migros Taze Kaşar 400g", 129.00, true)
        add("migros tereyagi", "Migros Tereyağı (250g)", "Migros", "Süt & Kahvaltılık", "Gram", 250.0, "migros", "Migros", "Migros Tereyağı 250g", 96.00, true)
        add("migros bal", "Migros Süzme Çiçek Balı (850g)", "Migros", "Süt & Kahvaltılık", "Gram", 850.0, "migros", "Migros", "Migros Çiçek Balı 850g", 189.00, true)

        // ==========================================
        // 6. CARREFOURSA EXCLUSIVE PRIVATE LABELS
        // Strictly available ONLY at CarrefourSA!
        // ==========================================
        add("carrefour kasap kiyma", "Carrefour Dana Kıyma %15 Yağlı (400g)", "Carrefour Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "carrefoursa", "CarrefourSA", "Carrefour Dana Kıyma 400g", 164.00, true)
        add("carrefour kasap kusbasi", "Carrefour Dana Kuşbaşı (400g)", "Carrefour Kasap", "Et, Tavuk & Balık", "Gram", 400.0, "carrefoursa", "CarrefourSA", "Carrefour Dana Kuşbaşı 400g", 188.00, true)
        add("carrefour aycicek yagi", "Carrefour Ayçiçek Yağı (5L)", "Carrefour", "Temel Gıda & Yağ", "Lt", 5.0, "carrefoursa", "CarrefourSA", "Carrefour Ayçiçek Yağı 5L", 249.90, true)
        add("carrefour un", "Carrefour Buğday Unu (5kg)", "Carrefour", "Temel Gıda & Yağ", "Kg", 5.0, "carrefoursa", "CarrefourSA", "Carrefour Un 5kg", 89.00, true)
        add("carrefour pirinc", "Carrefour Baldo Pirinç (1kg)", "Carrefour", "Temel Gıda & Yağ", "Kg", 1.0, "carrefoursa", "CarrefourSA", "Carrefour Baldo Pirinç 1kg", 58.00, true)
        add("carrefour kasar peyniri", "Carrefour Taze Kaşar Peyniri (400g)", "Carrefour", "Süt & Kahvaltılık", "Gram", 400.0, "carrefoursa", "CarrefourSA", "Carrefour Kaşar 400g", 129.00, true)

        // ==========================================
        // 7. NATIONAL / MULTI-STORE BRANDS WITH VERIFIED RETAIL DISTRIBUTION & REAL CURRENT PRICES
        // ==========================================

        // --- LAKTOZSUZ SÜT BRANDS ---
        // İçim Rahat Laktozsuz Süt (Sold at Migros, CarrefourSA, ŞOK, Tarım Kredi - NEVER at BİM/A101)
        add("icim rahat laktozsuz sut", "İçim Rahat Laktozsuz Süt (1L)", "İçim", "Süt & Kahvaltılık", "Lt", 1.0, "migros", "Migros", "İçim Rahat Laktozsuz Süt 1L", 44.50, false)
        add("icim rahat laktozsuz sut", "İçim Rahat Laktozsuz Süt (1L)", "İçim", "Süt & Kahvaltılık", "Lt", 1.0, "carrefoursa", "CarrefourSA", "İçim Rahat Laktozsuz Süt 1L", 45.00, false)
        add("icim rahat laktozsuz sut", "İçim Rahat Laktozsuz Süt (1L)", "İçim", "Süt & Kahvaltılık", "Lt", 1.0, "sok", "ŞOK", "İçim Rahat Laktozsuz Süt 1L", 44.00, false)
        add("icim rahat laktozsuz sut", "İçim Rahat Laktozsuz Süt (1L)", "İçim", "Süt & Kahvaltılık", "Lt", 1.0, "tarimkredi", "Tarım Kredi", "İçim Rahat Laktozsuz Süt 1L", 43.50, false)

        // Pınar Denge Laktozsuz Süt (Sold at Migros, CarrefourSA, ŞOK)
        add("pinar denge laktozsuz sut", "Pınar Denge Laktozsuz Süt (1L)", "Pınar", "Süt & Kahvaltılık", "Lt", 1.0, "migros", "Migros", "Pınar Denge Laktozsuz Süt 1L", 45.50, false)
        add("pinar denge laktozsuz sut", "Pınar Denge Laktozsuz Süt (1L)", "Pınar", "Süt & Kahvaltılık", "Lt", 1.0, "carrefoursa", "CarrefourSA", "Pınar Denge Laktozsuz Süt 1L", 45.50, false)
        add("pinar denge laktozsuz sut", "Pınar Denge Laktozsuz Süt (1L)", "Pınar", "Süt & Kahvaltılık", "Lt", 1.0, "sok", "ŞOK", "Pınar Denge Laktozsuz Süt 1L", 45.00, false)

        // Sütaş Tam Yağlı Süt (Sold everywhere)
        val sutasStores = listOf(
            Triple("migros", "Migros", 34.50),
            Triple("carrefoursa", "CarrefourSA", 34.90),
            Triple("sok", "ŞOK", 34.00),
            Triple("tarimkredi", "Tarım Kredi", 33.50),
            Triple("a101", "A101", 33.00),
            Triple("bim", "BİM", 33.00)
        )
        sutasStores.forEach { (mId, mName, p) ->
            add("sutas sut tam yagli", "Sütaş Tam Yağlı Süt (1L)", "Sütaş", "Süt & Kahvaltılık", "Lt", 1.0, mId, mName, "Sütaş Günlük/UHT Süt 1L", p, false)
        }

        // İçim Tam Yağlı Süt (Sold at Migros, CarrefourSA, ŞOK, Tarım Kredi)
        val icimStores = listOf(
            Triple("migros", "Migros", 34.00),
            Triple("carrefoursa", "CarrefourSA", 34.50),
            Triple("sok", "ŞOK", 33.50),
            Triple("tarimkredi", "Tarım Kredi", 33.00)
        )
        icimStores.forEach { (mId, mName, p) ->
            add("icim sut tam yagli", "İçim Tam Yağlı Süt (1L)", "İçim", "Süt & Kahvaltılık", "Lt", 1.0, mId, mName, "İçim Süt 1L", p, false)
        }

        // Pınar Tam Yağlı Süt (Sold at Migros, CarrefourSA, ŞOK)
        val pinarStores = listOf(
            Triple("migros", "Migros", 36.50),
            Triple("carrefoursa", "CarrefourSA", 36.90),
            Triple("sok", "ŞOK", 36.00)
        )
        pinarStores.forEach { (mId, mName, p) ->
            add("pinar sut tam yagli", "Pınar Tam Yağlı Süt (1L)", "Pınar", "Süt & Kahvaltılık", "Lt", 1.0, mId, mName, "Pınar Süt 1L", p, false)
        }

        // Torku Süt (Sold everywhere)
        val torkuStores = listOf(
            Triple("migros", "Migros", 33.50),
            Triple("carrefoursa", "CarrefourSA", 33.90),
            Triple("tarimkredi", "Tarım Kredi", 32.50),
            Triple("sok", "ŞOK", 33.00),
            Triple("a101", "A101", 32.50),
            Triple("bim", "BİM", 32.50)
        )
        torkuStores.forEach { (mId, mName, p) ->
            add("torku sut", "Torku Tam Yağlı Süt (1L)", "Torku", "Süt & Kahvaltılık", "Lt", 1.0, mId, mName, "Torku Doğal Süt 1L", p, false)
        }

        // Tahsildaroğlu Ezine Peyniri (500g)
        val tahsildarStores = listOf(
            Triple("migros", "Migros", 159.00),
            Triple("carrefoursa", "CarrefourSA", 162.00),
            Triple("tarimkredi", "Tarım Kredi", 155.00),
            Triple("sok", "ŞOK", 158.00)
        )
        tahsildarStores.forEach { (mId, mName, p) ->
            add("tahsildaroglu ezine peyniri", "Tahsildaroğlu Ezine Klasik Peynir (500g)", "Tahsildaroğlu", "Süt & Kahvaltılık", "Gram", 500.0, mId, mName, "Tahsildaroğlu Ezine 500g", p, false)
        }

        // Muratbey Taze Kaşar (400g)
        val muratbeyStores = listOf(
            Triple("migros", "Migros", 145.00),
            Triple("carrefoursa", "CarrefourSA", 148.00),
            Triple("sok", "ŞOK", 142.00),
            Triple("tarimkredi", "Tarım Kredi", 140.00)
        )
        muratbeyStores.forEach { (mId, mName, p) ->
            add("muratbey kasar peyniri", "Muratbey Taze Kaşar Peyniri (400g)", "Muratbey", "Süt & Kahvaltılık", "Gram", 400.0, mId, mName, "Muratbey Kaşar 400g", p, false)
        }

        // Ekici Beyaz Peynir (500g)
        val ekiciStores = listOf(
            Triple("migros", "Migros", 119.00),
            Triple("carrefoursa", "CarrefourSA", 122.00),
            Triple("sok", "ŞOK", 118.00),
            Triple("tarimkredi", "Tarım Kredi", 116.00)
        )
        ekiciStores.forEach { (mId, mName, p) ->
            add("ekici beyaz peynir", "Ekici Tam Yağlı Beyaz Peynir (500g)", "Ekici", "Süt & Kahvaltılık", "Gram", 500.0, mId, mName, "Ekici Beyaz Peynir 500g", p, false)
        }

        // Yudum Ayçiçek Yağı (5L)
        val yudumStores = listOf(
            Triple("migros", "Migros", 269.90),
            Triple("carrefoursa", "CarrefourSA", 269.90),
            Triple("sok", "ŞOK", 259.90),
            Triple("a101", "A101", 259.90),
            Triple("tarimkredi", "Tarım Kredi", 255.00)
        )
        yudumStores.forEach { (mId, mName, p) ->
            add("yudum aycicek yagi", "Yudum Ayçiçek Yağı (5L)", "Yudum", "Temel Gıda & Yağ", "Lt", 5.0, mId, mName, "Yudum Ayçiçek Yağı Pet 5L", p, false)
        }

        // Komili Ayçiçek Yağı (5L)
        val komiliStores = listOf(
            Triple("migros", "Migros", 274.90),
            Triple("carrefoursa", "CarrefourSA", 274.90),
            Triple("sok", "ŞOK", 265.00),
            Triple("a101", "A101", 265.00)
        )
        komiliStores.forEach { (mId, mName, p) ->
            add("komili aycicek yagi", "Komili Ayçiçek Yağı (5L)", "Komili", "Temel Gıda & Yağ", "Lt", 5.0, mId, mName, "Komili Ayçiçek Yağı Pet 5L", p, false)
        }

        // Reis Baldo Pirinç (1kg)
        val reisStores = listOf(
            Triple("migros", "Migros", 79.90),
            Triple("carrefoursa", "CarrefourSA", 82.00),
            Triple("tarimkredi", "Tarım Kredi", 76.00)
        )
        reisStores.forEach { (mId, mName, p) ->
            add("reis baldo pirinc", "Reis Gönen Baldo Pirinç (1kg)", "Reis", "Temel Gıda & Yağ", "Kg", 1.0, mId, mName, "Reis Baldo Pirinç 1kg", p, false)
        }

        // Duru Kırmızı Mercimek (1kg)
        val duruStores = listOf(
            Triple("migros", "Migros", 58.50),
            Triple("carrefoursa", "CarrefourSA", 59.90),
            Triple("tarimkredi", "Tarım Kredi", 56.00)
        )
        duruStores.forEach { (mId, mName, p) ->
            add("duru kirmizi mercimek", "Duru Kırmızı Futbol Mercimek (1kg)", "Duru", "Temel Gıda & Yağ", "Kg", 1.0, mId, mName, "Duru Kırmızı Mercimek 1kg", p, false)
        }

        // Barilla Makarna (500g) (Sold at Migros & CarrefourSA only)
        add("barilla makarna", "Barilla Penne Rigate / Spagetti (500g)", "Barilla", "Temel Gıda & Yağ", "Paket", 1.0, "migros", "Migros", "Barilla Makarna 500g", 27.50, false)
        add("barilla makarna", "Barilla Penne Rigate / Spagetti (500g)", "Barilla", "Temel Gıda & Yağ", "Paket", 1.0, "carrefoursa", "CarrefourSA", "Barilla Makarna 500g", 27.90, false)

        // Filiz Makarna (500g) (Sold at Migros, CarrefourSA, ŞOK, A101, BİM)
        val filizStores = listOf(
            Triple("migros", "Migros", 18.50),
            Triple("carrefoursa", "CarrefourSA", 18.50),
            Triple("sok", "ŞOK", 17.50),
            Triple("a101", "A101", 17.50),
            Triple("bim", "BİM", 17.50)
        )
        filizStores.forEach { (mId, mName, p) ->
            add("filiz makarna", "Filiz Makarna Çeşitleri (500g)", "Filiz", "Temel Gıda & Yağ", "Paket", 1.0, mId, mName, "Filiz Burgu/Fiyonk 500g", p, false)
        }

        // Nuh'un Ankara Makarna (500g)
        val nuhStores = listOf(
            Triple("migros", "Migros", 18.00),
            Triple("carrefoursa", "CarrefourSA", 18.00),
            Triple("sok", "ŞOK", 17.00),
            Triple("a101", "A101", 17.00),
            Triple("bim", "BİM", 17.00)
        )
        nuhStores.forEach { (mId, mName, p) ->
            add("nuhun ankara makarna", "Nuh'un Ankara Makarnası (500g)", "Nuh'un Ankara", "Temel Gıda & Yağ", "Paket", 1.0, mId, mName, "Nuh'un Ankara 500g", p, false)
        }

        // Söke Un (5kg)
        val sokeStores = listOf(
            Triple("migros", "Migros", 99.00),
            Triple("carrefoursa", "CarrefourSA", 102.00),
            Triple("sok", "ŞOK", 95.00),
            Triple("a101", "A101", 95.00),
            Triple("tarimkredi", "Tarım Kredi", 94.00)
        )
        sokeStores.forEach { (mId, mName, p) ->
            add("soke un", "Söke Geleneksel Buğday Unu (5kg)", "Söke", "Temel Gıda & Yağ", "Kg", 5.0, mId, mName, "Söke Un 5kg", p, false)
        }

        // Çaykur Rize Turist Çay (1kg) (Sold everywhere)
        val caykurStores = listOf(
            Triple("migros", "Migros", 159.00),
            Triple("carrefoursa", "CarrefourSA", 159.00),
            Triple("tarimkredi", "Tarım Kredi", 154.00),
            Triple("sok", "ŞOK", 155.00),
            Triple("a101", "A101", 155.00),
            Triple("bim", "BİM", 155.00)
        )
        caykurStores.forEach { (mId, mName, p) ->
            add("caykur rize cay", "Çaykur Rize Turist Çayı (1kg)", "Çaykur", "İçecekler", "Kg", 1.0, mId, mName, "Çaykur Rize Turist Çayı 1kg", p, false)
        }

        // Doğuş Çay Tiryaki (1kg)
        val dogusStores = listOf(
            Triple("migros", "Migros", 149.00),
            Triple("carrefoursa", "CarrefourSA", 149.00),
            Triple("sok", "ŞOK", 145.00),
            Triple("a101", "A101", 145.00),
            Triple("bim", "BİM", 145.00)
        )
        dogusStores.forEach { (mId, mName, p) ->
            add("dogus cay", "Doğuş Tiryaki Çay (1kg)", "Doğuş", "İçecekler", "Kg", 1.0, mId, mName, "Doğuş Çay Tiryaki 1kg", p, false)
        }

        // Kurukahveci Mehmet Efendi (100g)
        val kahveStores = listOf(
            Triple("migros", "Migros", 44.50),
            Triple("carrefoursa", "CarrefourSA", 45.00),
            Triple("sok", "ŞOK", 42.50),
            Triple("a101", "A101", 42.50),
            Triple("bim", "BİM", 42.50),
            Triple("tarimkredi", "Tarım Kredi", 42.50)
        )
        kahveStores.forEach { (mId, mName, p) ->
            add("kurukahveci mehmet efendi", "Kurukahveci Mehmet Efendi Türk Kahvesi (100g)", "Kurukahveci Mehmet Efendi", "İçecekler", "Gram", 100.0, mId, mName, "Kurukahveci Mehmet Efendi 100g", p, false)
        }

        // Tat Domates Salçası (830g)
        val tatStores = listOf(
            Triple("migros", "Migros", 48.50),
            Triple("carrefoursa", "CarrefourSA", 49.00),
            Triple("tarimkredi", "Tarım Kredi", 46.00),
            Triple("sok", "ŞOK", 47.00),
            Triple("a101", "A101", 47.00),
            Triple("bim", "BİM", 47.00)
        )
        tatStores.forEach { (mId, mName, p) ->
            add("tat domates salcasi", "Tat Domates Salçası (830g)", "Tat", "Temel Gıda & Yağ", "Gram", 830.0, mId, mName, "Tat Domates Salçası 830g", p, false)
        }

        // Öncü Domates Salçası (830g)
        val oncuStores = listOf(
            Triple("migros", "Migros", 56.50),
            Triple("carrefoursa", "CarrefourSA", 57.00),
            Triple("tarimkredi", "Tarım Kredi", 54.00),
            Triple("sok", "ŞOK", 55.00),
            Triple("a101", "A101", 55.00)
        )
        oncuStores.forEach { (mId, mName, p) ->
            add("oncu domates salcasi", "Öncü Domates Salçası (830g)", "Öncü", "Temel Gıda & Yağ", "Gram", 830.0, mId, mName, "Öncü Domates Salçası 830g", p, false)
        }

        // Marmarabirlik Siyah Zeytin (500g)
        val marmaraStores = listOf(
            Triple("migros", "Migros", 89.00),
            Triple("carrefoursa", "CarrefourSA", 92.00),
            Triple("tarimkredi", "Tarım Kredi", 84.00),
            Triple("sok", "ŞOK", 85.00),
            Triple("a101", "A101", 85.00),
            Triple("bim", "BİM", 85.00)
        )
        marmaraStores.forEach { (mId, mName, p) ->
            add("marmarabirlik zeytin", "Marmarabirlik Kuru Sele / Doğal Zeytin (500g)", "Marmarabirlik", "Süt & Kahvaltılık", "Gram", 500.0, mId, mName, "Marmarabirlik Zeytin 500g", p, false)
        }

        // Balparmak Çiçek Balı (850g)
        val balparmakStores = listOf(
            Triple("migros", "Migros", 245.00),
            Triple("carrefoursa", "CarrefourSA", 249.00),
            Triple("tarimkredi", "Tarım Kredi", 238.00),
            Triple("sok", "ŞOK", 240.00)
        )
        balparmakStores.forEach { (mId, mName, p) ->
            add("balparmak bal", "Balparmak Yayla Çiçek Balı (850g)", "Balparmak", "Süt & Kahvaltılık", "Gram", 850.0, mId, mName, "Balparmak Çiçek Balı 850g", p, false)
        }

        // Nutella Fındık Kreması (750g)
        val nutellaStores = listOf(
            Triple("migros", "Migros", 142.50),
            Triple("carrefoursa", "CarrefourSA", 145.00),
            Triple("sok", "ŞOK", 139.90),
            Triple("a101", "A101", 139.90),
            Triple("bim", "BİM", 139.90)
        )
        nutellaStores.forEach { (mId, mName, p) ->
            add("nutella", "Nutella Kakaolu Fındık Kreması (750g)", "Nutella", "Süt & Kahvaltılık", "Gram", 750.0, mId, mName, "Nutella Fındık Kreması 750g", p, false)
        }

        // Banvit Bütün Piliç (Kg)
        val banvitStores = listOf(
            Triple("migros", "Migros", 89.90),
            Triple("carrefoursa", "CarrefourSA", 91.00),
            Triple("bim", "BİM", 86.50),
            Triple("a101", "A101", 86.50),
            Triple("sok", "ŞOK", 87.00)
        )
        banvitStores.forEach { (mId, mName, p) ->
            add("banvit pilic", "Banvit Taze Bütün Piliç (Kg)", "Banvit", "Et, Tavuk & Balık", "Kg", 1.0, mId, mName, "Banvit Bütün Piliç Kg", p, false)
        }

        // Şenpiliç Baget (Kg)
        val senpilicStores = listOf(
            Triple("migros", "Migros", 115.00),
            Triple("carrefoursa", "CarrefourSA", 118.00),
            Triple("bim", "BİM", 109.00),
            Triple("a101", "A101", 109.00),
            Triple("sok", "ŞOK", 110.00)
        )
        senpilicStores.forEach { (mId, mName, p) ->
            add("senpilic baget", "Şenpiliç Taze Piliç Baget (Kg)", "Şenpiliç", "Et, Tavuk & Balık", "Kg", 1.0, mId, mName, "Şenpiliç Baget Kg", p, false)
        }

        // Fairy Platinum Bulaşık Tableti (60'lı)
        val fairyStores = listOf(
            Triple("migros", "Migros", 385.00),
            Triple("carrefoursa", "CarrefourSA", 389.00),
            Triple("sok", "ŞOK", 369.00),
            Triple("a101", "A101", 369.00),
            Triple("bim", "BİM", 369.00)
        )
        fairyStores.forEach { (mId, mName, p) ->
            add("fairy bulasik tableti", "Fairy Platinum Hepsi Bir Arada Bulaşık Tableti (60'lı)", "Fairy", "Temizlik & Deterjan", "Paket", 1.0, mId, mName, "Fairy Platinum Tablet 60'lı", p, false)
        }

        // Finish Quantum Tablet (60'lı)
        val finishStores = listOf(
            Triple("migros", "Migros", 365.00),
            Triple("carrefoursa", "CarrefourSA", 369.00),
            Triple("sok", "ŞOK", 355.00),
            Triple("a101", "A101", 355.00),
            Triple("bim", "BİM", 355.00)
        )
        finishStores.forEach { (mId, mName, p) ->
            add("finish bulasik tableti", "Finish Quantum Bulaşık Tableti (60'lı)", "Finish", "Temizlik & Deterjan", "Paket", 1.0, mId, mName, "Finish Quantum 60'lı", p, false)
        }

        // Ariel Toz Deterjan (6kg)
        val arielStores = listOf(
            Triple("migros", "Migros", 299.90),
            Triple("carrefoursa", "CarrefourSA", 305.00),
            Triple("sok", "ŞOK", 289.00),
            Triple("a101", "A101", 289.00),
            Triple("bim", "BİM", 289.00)
        )
        arielStores.forEach { (mId, mName, p) ->
            add("ariel toz deterjan", "Ariel Dağ Esintisi Toz Deterjan (6kg)", "Ariel", "Temizlik & Deterjan", "Kg", 6.0, mId, mName, "Ariel Toz Deterjan 6kg", p, false)
        }

        // Omo Sıvı Deterjan (1690ml)
        val omoStores = listOf(
            Triple("migros", "Migros", 215.00),
            Triple("carrefoursa", "CarrefourSA", 218.00),
            Triple("sok", "ŞOK", 209.00),
            Triple("a101", "A101", 209.00),
            Triple("bim", "BİM", 209.00)
        )
        omoStores.forEach { (mId, mName, p) ->
            add("omo sivi deterjan", "Omo Active Fresh Sıvı Deterjan (1690ml)", "Omo", "Temizlik & Deterjan", "Lt", 1.69, mId, mName, "Omo Sıvı Deterjan 1690ml", p, false)
        }

        // Papia Tuvalet Kağıdı (32'li)
        val papiaStores = listOf(
            Triple("migros", "Migros", 229.90),
            Triple("carrefoursa", "CarrefourSA", 235.00),
            Triple("sok", "ŞOK", 219.00),
            Triple("a101", "A101", 219.00),
            Triple("bim", "BİM", 219.00)
        )
        papiaStores.forEach { (mId, mName, p) ->
            add("papia tuvalet kagidi", "Papia 3 Katlı Tuvalet Kağıdı (32'li)", "Papia", "Kağıt & Hijyen", "Paket", 1.0, mId, mName, "Papia Tuvalet Kağıdı 32'li", p, false)
        }

        // Selpak Deluxe Tuvalet Kağıdı (32'li)
        val selpakStores = listOf(
            Triple("migros", "Migros", 249.90),
            Triple("carrefoursa", "CarrefourSA", 255.00),
            Triple("sok", "ŞOK", 239.00),
            Triple("a101", "A101", 239.00),
            Triple("bim", "BİM", 239.00)
        )
        selpakStores.forEach { (mId, mName, p) ->
            add("selpak tuvalet kagidi", "Selpak Deluxe 3 Katlı Tuvalet Kağıdı (32'li)", "Selpak", "Kağıt & Hijyen", "Paket", 1.0, mId, mName, "Selpak Deluxe 32'li", p, false)
        }

        // Domestos Çamaşır Suyu (750ml)
        val domestosStores = listOf(
            Triple("migros", "Migros", 42.50),
            Triple("carrefoursa", "CarrefourSA", 43.00),
            Triple("sok", "ŞOK", 39.50),
            Triple("a101", "A101", 39.50),
            Triple("bim", "BİM", 39.50)
        )
        domestosStores.forEach { (mId, mName, p) ->
            add("domestos camasir suyu", "Domestos Yoğun Çamaşır Suyu (750ml)", "Domestos", "Temizlik & Deterjan", "Adet", 1.0, mId, mName, "Domestos Çamaşır Suyu 750ml", p, false)
        }

        // Cif Krem (750ml)
        val cifStores = listOf(
            Triple("migros", "Migros", 44.50),
            Triple("carrefoursa", "CarrefourSA", 45.00),
            Triple("sok", "ŞOK", 42.00),
            Triple("a101", "A101", 42.00),
            Triple("bim", "BİM", 42.00)
        )
        cifStores.forEach { (mId, mName, p) ->
            add("cif krem", "Cif Tüm Yüzeyler Krem Temizleyici (750ml)", "Cif", "Temizlik & Deterjan", "Adet", 1.0, mId, mName, "Cif Krem 750ml", p, false)
        }

        // --- FRESH PRODUCE / MANAV (AVAILABLE EVERYWHERE WITH GENUINE MARKET PRICES) ---
        val produceItems = listOf(
            Pair("domates", "Domates (Kg)") to listOf(Triple("migros", "Migros", 39.90), Triple("carrefoursa", "CarrefourSA", 39.90), Triple("sok", "ŞOK", 34.90), Triple("bim", "BİM", 34.90), Triple("a101", "A101", 34.90), Triple("tarimkredi", "Tarım Kredi", 35.50)),
            Pair("salatalik", "Salatalık (Kg)") to listOf(Triple("migros", "Migros", 34.90), Triple("carrefoursa", "CarrefourSA", 34.90), Triple("sok", "ŞOK", 29.90), Triple("bim", "BİM", 29.90), Triple("a101", "A101", 29.90), Triple("tarimkredi", "Tarım Kredi", 30.00)),
            Pair("patates", "Patates (Kg)") to listOf(Triple("migros", "Migros", 19.90), Triple("carrefoursa", "CarrefourSA", 19.90), Triple("sok", "ŞOK", 16.90), Triple("bim", "BİM", 16.90), Triple("a101", "A101", 16.90), Triple("tarimkredi", "Tarım Kredi", 17.50)),
            Pair("kuru sogan", "Kuru Soğan (Kg)") to listOf(Triple("migros", "Migros", 16.90), Triple("carrefoursa", "CarrefourSA", 16.90), Triple("sok", "ŞOK", 13.90), Triple("bim", "BİM", 13.90), Triple("a101", "A101", 13.90), Triple("tarimkredi", "Tarım Kredi", 14.00)),
            Pair("muz", "Muz (Yerli Anamur) (Kg)") to listOf(Triple("migros", "Migros", 69.90), Triple("carrefoursa", "CarrefourSA", 72.00), Triple("sok", "ŞOK", 59.90), Triple("bim", "BİM", 59.90), Triple("a101", "A101", 59.90), Triple("tarimkredi", "Tarım Kredi", 62.00)),
            Pair("elma", "Elma (Starking/Gala) (Kg)") to listOf(Triple("migros", "Migros", 39.90), Triple("carrefoursa", "CarrefourSA", 39.90), Triple("sok", "ŞOK", 34.90), Triple("bim", "BİM", 34.90), Triple("a101", "A101", 34.90), Triple("tarimkredi", "Tarım Kredi", 35.00)),
            Pair("limon", "Limon (Kg)") to listOf(Triple("migros", "Migros", 44.90), Triple("carrefoursa", "CarrefourSA", 44.90), Triple("sok", "ŞOK", 39.90), Triple("bim", "BİM", 39.90), Triple("a101", "A101", 39.90), Triple("tarimkredi", "Tarım Kredi", 41.00)),
            Pair("havuc", "Havuç (Kg)") to listOf(Triple("migros", "Migros", 29.90), Triple("carrefoursa", "CarrefourSA", 29.90), Triple("sok", "ŞOK", 24.90), Triple("bim", "BİM", 24.90), Triple("a101", "A101", 24.90), Triple("tarimkredi", "Tarım Kredi", 25.00))
        )

        produceItems.forEach { (prod, stores) ->
            stores.forEach { (mId, mName, p) ->
                add(prod.first, prod.second, "Taze Manav", "Meyve & Sebze", "Kg", 1.0, mId, mName, "${prod.second} - ${mName} Manav", p, false)
            }
        }

        return list
    }
}
