package com.example.ui.i18n

import com.example.data.model.AppLanguage

class StringsProvider(private val language: AppLanguage) {

    // General & Navigation
    val appTitle: String get() = when (language) {
        AppLanguage.TR -> "Aile Bütçesi & Fiş Takibi"
        AppLanguage.EN -> "Family Budget & Receipt Tracker"
        AppLanguage.DE -> "Familienbudget & Beleg-Tracker"
        AppLanguage.ES -> "Presupuesto Familiar y Recibos"
        AppLanguage.FR -> "Budget Familial & Reçus"
        AppLanguage.AR -> "ميزانية الأسرة وتتبع الإيصالات"
        AppLanguage.RU -> "Семейный бюджет и чеки"
        AppLanguage.IT -> "Bilancio Familiare & Scontrini"
    }

    val navOverview: String get() = when (language) {
        AppLanguage.TR -> "Bütçe Özeti"
        AppLanguage.EN -> "Overview"
        AppLanguage.DE -> "Übersicht"
        AppLanguage.ES -> "Resumen"
        AppLanguage.FR -> "Aperçu"
        AppLanguage.AR -> "نظرة عامة"
        AppLanguage.RU -> "Обзор"
        AppLanguage.IT -> "Riepilogo"
    }

    val navExpenses: String get() = when (language) {
        AppLanguage.TR -> "Harcamalar"
        AppLanguage.EN -> "Expenses"
        AppLanguage.DE -> "Ausgaben"
        AppLanguage.ES -> "Gastos"
        AppLanguage.FR -> "Dépenses"
        AppLanguage.AR -> "المصروفات"
        AppLanguage.RU -> "Расходы"
        AppLanguage.IT -> "Spese"
    }

    val navShopping: String get() = when (language) {
        AppLanguage.TR -> "Alışveriş"
        AppLanguage.EN -> "Shopping"
        AppLanguage.DE -> "Einkaufsliste"
        AppLanguage.ES -> "Compras"
        AppLanguage.FR -> "Courses"
        AppLanguage.AR -> "التسوق"
        AppLanguage.RU -> "Покупки"
        AppLanguage.IT -> "Spesa"
    }

    val navStatistics: String get() = when (language) {
        AppLanguage.TR -> "İstatistikler"
        AppLanguage.EN -> "Statistics"
        AppLanguage.DE -> "Statistiken"
        AppLanguage.ES -> "Estadísticas"
        AppLanguage.FR -> "Statistiques"
        AppLanguage.AR -> "الإحصائيات"
        AppLanguage.RU -> "Статистика"
        AppLanguage.IT -> "Statistiche"
    }

    val navSettings: String get() = when (language) {
        AppLanguage.TR -> "Aile & Ayarlar"
        AppLanguage.EN -> "Family & Settings"
        AppLanguage.DE -> "Familie & Einstellungen"
        AppLanguage.ES -> "Familia y Ajustes"
        AppLanguage.FR -> "Famille & Paramètres"
        AppLanguage.AR -> "العائلة والإعدادات"
        AppLanguage.RU -> "Семья и Настройки"
        AppLanguage.IT -> "Famiglia & Impostazioni"
    }

    // Dashboard Cards
    val monthlyTotalIncome: String get() = when (language) {
        AppLanguage.TR -> "Toplam Aylık Gelir (Maaş + Ek)"
        AppLanguage.EN -> "Total Monthly Income (Salary + Extra)"
        AppLanguage.DE -> "Monatliches Gesamteinkommen"
        AppLanguage.ES -> "Ingresos Mensuales Totales"
        AppLanguage.FR -> "Revenu Mensuel Total"
        AppLanguage.AR -> "إجمالي الدخل الشهري"
        AppLanguage.RU -> "Общий месячный доход"
        AppLanguage.IT -> "Reddito Mensile Totale"
    }

    val totalSpent: String get() = when (language) {
        AppLanguage.TR -> "Toplam Harcama"
        AppLanguage.EN -> "Total Spent"
        AppLanguage.DE -> "Gesamtausgaben"
        AppLanguage.ES -> "Gasto Total"
        AppLanguage.FR -> "Dépenses Totales"
        AppLanguage.AR -> "إجمالي المصروفات"
        AppLanguage.RU -> "Всего потрачено"
        AppLanguage.IT -> "Totale Speso"
    }

    val remainingBudget: String get() = when (language) {
        AppLanguage.TR -> "Kalan Bütçe"
        AppLanguage.EN -> "Remaining Budget"
        AppLanguage.DE -> "Verbleibendes Budget"
        AppLanguage.ES -> "Presupuesto Restante"
        AppLanguage.FR -> "Budget Restant"
        AppLanguage.AR -> "الميزانية المتبقية"
        AppLanguage.RU -> "Остаток бюджета"
        AppLanguage.IT -> "Budget Rimanente"
    }

    val spendingLimit: String get() = when (language) {
        AppLanguage.TR -> "Harcama Limiti"
        AppLanguage.EN -> "Spending Limit"
        AppLanguage.DE -> "Ausgabenlimit"
        AppLanguage.ES -> "Límite de Gasto"
        AppLanguage.FR -> "Plafond de Dépenses"
        AppLanguage.AR -> "حد الإنفاق"
        AppLanguage.RU -> "Лимит расходов"
        AppLanguage.IT -> "Limite di Spesa"
    }

    val budgetUsage: String get() = when (language) {
        AppLanguage.TR -> "Bütçe Kullanım Oranı"
        AppLanguage.EN -> "Budget Utilization Rate"
        AppLanguage.DE -> "Budget-Nutzungsrate"
        AppLanguage.ES -> "Tasa de Uso del Presupuesto"
        AppLanguage.FR -> "Taux d'Utilisation du Budget"
        AppLanguage.AR -> "نسبة استخدام الميزانية"
        AppLanguage.RU -> "Использование бюджета"
        AppLanguage.IT -> "Utilizzo Budget"
    }

    // Alerts
    val alertNormal: String get() = when (language) {
        AppLanguage.TR -> "Bütçeniz kontrol altında. Harcamalarınız belirlenen limitler dahilindedir."
        AppLanguage.EN -> "Budget is on track. Expenses are well within limits."
        AppLanguage.DE -> "Budget ist im Rahmen. Ausgaben liegen innerhalb des Limits."
        AppLanguage.ES -> "El presupuesto va bien. Los gastos están dentro de los límites."
        AppLanguage.FR -> "Le budget est sous contrôle. Les dépenses respectent les limites."
        AppLanguage.AR -> "الميزانية تحت السيطرة والمصروفات ضمن الحدود."
        AppLanguage.RU -> "Бюджет под контролем. Расходы в пределах лимита."
        AppLanguage.IT -> "Il budget è sotto controllo."
    }

    val alertWarning: String get() = when (language) {
        AppLanguage.TR -> "DİKKAT: Aylık bütçe limitinizin %s kadarını kullandınız! Harcamalarınızı gözden geçirin."
        AppLanguage.EN -> "WARNING: You have used %s of your monthly budget limit! Please review your expenses."
        AppLanguage.DE -> "WARNUNG: Sie haben %s Ihres monatlichen Limits verbraucht!"
        AppLanguage.ES -> "ATENCIÓN: ¡Ha utilizado el %s de su límite mensual!"
        AppLanguage.FR -> "ATTENTION : Vous avez utilisé %s de votre budget mensuel !"
        AppLanguage.AR -> "تحذير: لقد استخدمت %s من حد ميزانيتك الشهرية!"
        AppLanguage.RU -> "ВНИМАНИЕ: Вы израсходовали %s от месячного лимита бюджета!"
        AppLanguage.IT -> "ATTENZIONE: Hai utilizzato il %s del tuo limite mensile!"
    }

    val alertExceeded: String get() = when (language) {
        AppLanguage.TR -> "KRİTİK UYARI: Harcama limiti aşıldı! Bütçe aşım miktarı: %s"
        AppLanguage.EN -> "CRITICAL ALERT: Spending limit exceeded! Overspent amount: %s"
        AppLanguage.DE -> "KRITISCHE WARNUNG: Ausgabenlimit überschritten! Überziehung: %s"
        AppLanguage.ES -> "ALERTA CRÍTICA: ¡Límite de gasto superado! Exceso: %s"
        AppLanguage.FR -> "ALERTE CRITIQUE : Limite de dépenses dépassée ! Excédent : %s"
        AppLanguage.AR -> "تنبيه حرج: تم تجاوز حد الإنفاق! المبلغ الزائد: %s"
        AppLanguage.RU -> "КРИТИЧЕСКОЕ ПРЕДУПРЕЖДЕНИЕ: Лимит расходов превышен! Превышение: %s"
        AppLanguage.IT -> "ALLERTA CRITICA: Limite di spesa superato! Importo in eccesso: %s"
    }

    // Quick Actions
    val scanReceipt: String get() = when (language) {
        AppLanguage.TR -> "Fiş Tarama"
        AppLanguage.EN -> "Scan Receipt"
        AppLanguage.DE -> "Beleg scannen"
        AppLanguage.ES -> "Escanear Recibo"
        AppLanguage.FR -> "Scanner le Reçu"
        AppLanguage.AR -> "مسح الإيصال"
        AppLanguage.RU -> "Сканировать чек"
        AppLanguage.IT -> "Scansiona Scontrino"
    }

    val manualExpense: String get() = when (language) {
        AppLanguage.TR -> "Manuel Fiş Girişi"
        AppLanguage.EN -> "Manual Entry"
        AppLanguage.DE -> "Manuelle Eingabe"
        AppLanguage.ES -> "Entrada Manual"
        AppLanguage.FR -> "Saisie Manuelle"
        AppLanguage.AR -> "إدخال يدوي"
        AppLanguage.RU -> "Ручной ввод"
        AppLanguage.IT -> "Inserimento Manuale"
    }

    val demoReceiptPresets: String get() = when (language) {
        AppLanguage.TR -> "Hızlı Örnek Fişler"
        AppLanguage.EN -> "Quick Sample Receipts"
        AppLanguage.DE -> "Beispielbelege"
        AppLanguage.ES -> "Recibos de Prueba"
        AppLanguage.FR -> "Exemples de Reçus"
        AppLanguage.AR -> "إيصالات نموذجية سريعة"
        AppLanguage.RU -> "Примеры чеков"
        AppLanguage.IT -> "Esempi Rapidi"
    }

    val recentReceipts: String get() = when (language) {
        AppLanguage.TR -> "Son Fişler & Harcamalar"
        AppLanguage.EN -> "Recent Receipts & Expenses"
        AppLanguage.DE -> "Letzte Belege"
        AppLanguage.ES -> "Recibos Recientes"
        AppLanguage.FR -> "Reçus Récents"
        AppLanguage.AR -> "أحدث الإيصالات"
        AppLanguage.RU -> "Последние чеки"
        AppLanguage.IT -> "Scontrini Recenti"
    }

    val seeAll: String get() = when (language) {
        AppLanguage.TR -> "Tümünü Gör"
        AppLanguage.EN -> "See All"
        AppLanguage.DE -> "Alle anzeigen"
        AppLanguage.ES -> "Ver todo"
        AppLanguage.FR -> "Voir tout"
        AppLanguage.AR -> "عرض الكل"
        AppLanguage.RU -> "Посмотреть все"
        AppLanguage.IT -> "Vedi tutti"
    }

    // Search & Filter
    val searchPlaceholder: String get() = when (language) {
        AppLanguage.TR -> "Fiş, mağaza veya ürün ara (ör: Süt, Benzin)..."
        AppLanguage.EN -> "Search receipts, stores or items (e.g. Milk, Fuel)..."
        AppLanguage.DE -> "Suche nach Belegen, Geschäften oder Artikeln..."
        AppLanguage.ES -> "Buscar recibos, tiendas o artículos..."
        AppLanguage.FR -> "Rechercher reçus, magasins ou articles..."
        AppLanguage.AR -> "البحث عن إيصال، متجر أو منتج..."
        AppLanguage.RU -> "Поиск чеков, магазинов или товаров..."
        AppLanguage.IT -> "Cerca scontrini, negozi o articoli..."
    }

    val filterByDateRange: String get() = when (language) {
        AppLanguage.TR -> "Tarih Aralığı Filtresi"
        AppLanguage.EN -> "Date Range Filter"
        AppLanguage.DE -> "Datumsbereich-Filter"
        AppLanguage.ES -> "Filtro por Rango de Fechas"
        AppLanguage.FR -> "Filtre par Période"
        AppLanguage.AR -> "تصفية حسب النطاق الزمني"
        AppLanguage.RU -> "Фильтр по диапазону дат"
        AppLanguage.IT -> "Filtro per Intervallo di Date"
    }

    val startDate: String get() = when (language) {
        AppLanguage.TR -> "Başlangıç Tarihi"
        AppLanguage.EN -> "Start Date"
        AppLanguage.DE -> "Startdatum"
        AppLanguage.ES -> "Fecha de Inicio"
        AppLanguage.FR -> "Date de Début"
        AppLanguage.AR -> "تاريخ البدء"
        AppLanguage.RU -> "Дата начала"
        AppLanguage.IT -> "Data Inizio"
    }

    val endDate: String get() = when (language) {
        AppLanguage.TR -> "Bitiş Tarihi"
        AppLanguage.EN -> "End Date"
        AppLanguage.DE -> "Enddatum"
        AppLanguage.ES -> "Fecha de Fin"
        AppLanguage.FR -> "Date de Fin"
        AppLanguage.AR -> "تاريخ الانتهاء"
        AppLanguage.RU -> "Дата окончания"
        AppLanguage.IT -> "Data Fine"
    }

    val filterAll: String get() = when (language) {
        AppLanguage.TR -> "Tümü"
        AppLanguage.EN -> "All"
        AppLanguage.DE -> "Alle"
        AppLanguage.ES -> "Todos"
        AppLanguage.FR -> "Tous"
        AppLanguage.AR -> "الكل"
        AppLanguage.RU -> "Все"
        AppLanguage.IT -> "Tutti"
    }

    val filterThisMonth: String get() = when (language) {
        AppLanguage.TR -> "Bu Ay"
        AppLanguage.EN -> "This Month"
        AppLanguage.DE -> "Dieser Monat"
        AppLanguage.ES -> "Este Mes"
        AppLanguage.FR -> "Ce Mois"
        AppLanguage.AR -> "هذا الشهر"
        AppLanguage.RU -> "Этот месяц"
        AppLanguage.IT -> "Questo Mese"
    }

    val filterLast7Days: String get() = when (language) {
        AppLanguage.TR -> "Son 7 Gün"
        AppLanguage.EN -> "Last 7 Days"
        AppLanguage.DE -> "Letzte 7 Tage"
        AppLanguage.ES -> "Últimos 7 Días"
        AppLanguage.FR -> "7 Derniers Jours"
        AppLanguage.AR -> "آخر 7 أيام"
        AppLanguage.RU -> "Последние 7 дней"
        AppLanguage.IT -> "Ultimi 7 Giorni"
    }

    val filterLast30Days: String get() = when (language) {
        AppLanguage.TR -> "Son 30 Gün"
        AppLanguage.EN -> "Last 30 Days"
        AppLanguage.DE -> "Letzte 30 Tage"
        AppLanguage.ES -> "Últimos 30 Días"
        AppLanguage.FR -> "30 Derniers Jours"
        AppLanguage.AR -> "آخر 30 يوماً"
        AppLanguage.RU -> "Последние 30 дней"
        AppLanguage.IT -> "Ultimi 30 Giorni"
    }

    val filterCustom: String get() = when (language) {
        AppLanguage.TR -> "Özel Tarih"
        AppLanguage.EN -> "Custom Date"
        AppLanguage.DE -> "Benutzerdefiniert"
        AppLanguage.ES -> "Fecha Personalizada"
        AppLanguage.FR -> "Période Personnalisée"
        AppLanguage.AR -> "تاريخ مخصص"
        AppLanguage.RU -> "Выбрать даты"
        AppLanguage.IT -> "Data Personalizzata"
    }

    val filterByAmount: String get() = when (language) {
        AppLanguage.TR -> "Harcama Tutarı Filtresi"
        AppLanguage.EN -> "Expense Amount Filter"
        AppLanguage.DE -> "Ausgabenbetrag-Filter"
        AppLanguage.ES -> "Filtro por Monto de Gasto"
        AppLanguage.FR -> "Filtre par Montant"
        AppLanguage.AR -> "تصفية حسب قيمة المصروف"
        AppLanguage.RU -> "Фильтр по сумме расходов"
        AppLanguage.IT -> "Filtro per Importo Spesa"
    }

    val minAmount: String get() = when (language) {
        AppLanguage.TR -> "Min Tutar (₺)"
        AppLanguage.EN -> "Min Amount (₺)"
        AppLanguage.DE -> "Mindestbetrag (₺)"
        AppLanguage.ES -> "Monto Mínimo (₺)"
        AppLanguage.FR -> "Montant Min (₺)"
        AppLanguage.AR -> "الحد الأدنى (₺)"
        AppLanguage.RU -> "Мин. сумма (₺)"
        AppLanguage.IT -> "Importo Minimo (₺)"
    }

    val maxAmount: String get() = when (language) {
        AppLanguage.TR -> "Max Tutar (₺)"
        AppLanguage.EN -> "Max Amount (₺)"
        AppLanguage.DE -> "Höchstbetrag (₺)"
        AppLanguage.ES -> "Monto Máximo (₺)"
        AppLanguage.FR -> "Montant Max (₺)"
        AppLanguage.AR -> "الحد الأقصى (₺)"
        AppLanguage.RU -> "Макс. сумма (₺)"
        AppLanguage.IT -> "Importo Massimo (₺)"
    }

    val sortBy: String get() = when (language) {
        AppLanguage.TR -> "Sıralama"
        AppLanguage.EN -> "Sort By"
        AppLanguage.DE -> "Sortieren nach"
        AppLanguage.ES -> "Ordenar Por"
        AppLanguage.FR -> "Trier Par"
        AppLanguage.AR -> "ترتيب حسب"
        AppLanguage.RU -> "Сортировка"
        AppLanguage.IT -> "Ordina Per"
    }

    val sortNewestFirst: String get() = when (language) {
        AppLanguage.TR -> "En Yeni Tarih"
        AppLanguage.EN -> "Newest First"
        AppLanguage.DE -> "Neueste zuerst"
        AppLanguage.ES -> "Más Recientes"
        AppLanguage.FR -> "Plus Récents"
        AppLanguage.AR -> "الأحدث أولاً"
        AppLanguage.RU -> "Сначала новые"
        AppLanguage.IT -> "Più Recenti"
    }

    val sortOldestFirst: String get() = when (language) {
        AppLanguage.TR -> "En Eski Tarih"
        AppLanguage.EN -> "Oldest First"
        AppLanguage.DE -> "Älteste zuerst"
        AppLanguage.ES -> "Más Antiguos"
        AppLanguage.FR -> "Plus Anciens"
        AppLanguage.AR -> "الأقدم أولاً"
        AppLanguage.RU -> "Сначала старые"
        AppLanguage.IT -> "Meno Recenti"
    }

    val sortHighestAmount: String get() = when (language) {
        AppLanguage.TR -> "En Yüksek Tutar"
        AppLanguage.EN -> "Highest Amount"
        AppLanguage.DE -> "Höchster Betrag"
        AppLanguage.ES -> "Mayor Monto"
        AppLanguage.FR -> "Montant le plus élevé"
        AppLanguage.AR -> "الأعلى سعراً"
        AppLanguage.RU -> "Сначала дорогие"
        AppLanguage.IT -> "Importo Maggiore"
    }

    val sortLowestAmount: String get() = when (language) {
        AppLanguage.TR -> "En Düşük Tutar"
        AppLanguage.EN -> "Lowest Amount"
        AppLanguage.DE -> "Niedrigster Betrag"
        AppLanguage.ES -> "Menor Monto"
        AppLanguage.FR -> "Montant le plus bas"
        AppLanguage.AR -> "الأقل سعراً"
        AppLanguage.RU -> "Сначала дешевые"
        AppLanguage.IT -> "Importo Minore"
    }

    val clearFilters: String get() = when (language) {
        AppLanguage.TR -> "Filtreleri Temizle"
        AppLanguage.EN -> "Clear Filters"
        AppLanguage.DE -> "Filter zurücksetzen"
        AppLanguage.ES -> "Limpiar Filtros"
        AppLanguage.FR -> "Effacer les filtres"
        AppLanguage.AR -> "إعادة تعيين الفلاتر"
        AppLanguage.RU -> "Сбросить фильтры"
        AppLanguage.IT -> "Cancella Filtri"
    }

    val advancedFilters: String get() = when (language) {
        AppLanguage.TR -> "Detaylı Filtreleme"
        AppLanguage.EN -> "Advanced Filters"
        AppLanguage.DE -> "Erweiterte Filter"
        AppLanguage.ES -> "Filtros Avanzados"
        AppLanguage.FR -> "Filtres Avancés"
        AppLanguage.AR -> "فلاتر متقدمة"
        AppLanguage.RU -> "Расширенные фильтры"
        AppLanguage.IT -> "Filtri Avanzati"
    }

    val last7DaysTitle: String get() = when (language) {
        AppLanguage.TR -> "Son 7 Günün Harcama Özeti"
        AppLanguage.EN -> "Last 7 Days Expense Summary"
        AppLanguage.DE -> "Ausgabenübersicht der letzten 7 Tage"
        AppLanguage.ES -> "Resumen de Gastos de los Últimos 7 Días"
        AppLanguage.FR -> "Résumé des Dépenses des 7 Derniers Jours"
        AppLanguage.AR -> "ملخص مصروفات آخر 7 أيام"
        AppLanguage.RU -> "Сводка расходов за последние 7 дней"
        AppLanguage.IT -> "Riepilogo Spese Ultimi 7 Giorni"
    }

    val last7DaysSpending: String get() = when (language) {
        AppLanguage.TR -> "Son 7 Günlük Toplam"
        AppLanguage.EN -> "Last 7 Days Total"
        AppLanguage.DE -> "Gesamt der letzten 7 Tage"
        AppLanguage.ES -> "Total Últimos 7 Días"
        AppLanguage.FR -> "Total 7 derniers jours"
        AppLanguage.AR -> "إجمالي آخر 7 أيام"
        AppLanguage.RU -> "Итого за 7 дней"
        AppLanguage.IT -> "Totale Ultimi 7 Giorni"
    }

    val previousWeekComparison: String get() = when (language) {
        AppLanguage.TR -> "Önceki 7 Güne Göre"
        AppLanguage.EN -> "vs Previous 7 Days"
        AppLanguage.DE -> "vs. vorherige 7 Tage"
        AppLanguage.ES -> "vs 7 Días Anteriores"
        AppLanguage.FR -> "vs 7 jours précédents"
        AppLanguage.AR -> "مقارنة بـ 7 أيام السابقة"
        AppLanguage.RU -> "к предыдущим 7 дням"
        AppLanguage.IT -> "rispetto ai 7 giorni prima"
    }

    val dailyAverage: String get() = when (language) {
        AppLanguage.TR -> "Günlük Ortalama"
        AppLanguage.EN -> "Daily Average"
        AppLanguage.DE -> "Tagesdurchschnitt"
        AppLanguage.ES -> "Promedio Diario"
        AppLanguage.FR -> "Moyenne journalière"
        AppLanguage.AR -> "المعدل اليومي"
        AppLanguage.RU -> "Среднесуточно"
        AppLanguage.IT -> "Media Giornaliera"
    }

    // Statistics & Reports
    val statsTitle: String get() = when (language) {
        AppLanguage.TR -> "Detaylı Raporlar ve Trendler"
        AppLanguage.EN -> "Detailed Reports & Trends"
        AppLanguage.DE -> "Detaillierte Berichte & Trends"
        AppLanguage.ES -> "Informes y Tendencias"
        AppLanguage.FR -> "Rapports Détaillés & Tendances"
        AppLanguage.AR -> "التقارير المفصلة والاتجاهات"
        AppLanguage.RU -> "Подробные отчеты и тренды"
        AppLanguage.IT -> "Report Dettagliati & Tendenze"
    }

    val monthlyPaidVat: String get() = when (language) {
        AppLanguage.TR -> "1 Aylık Ödenen KDV"
        AppLanguage.EN -> "1-Month Paid VAT"
        AppLanguage.DE -> "Im Monat gezahlte MwSt."
        AppLanguage.ES -> "IVA Pagado en 1 Mes"
        AppLanguage.FR -> "TVA payée sur 1 mois"
        AppLanguage.AR -> "ضريبة القيمة المضافة المدفوعة خلال شهر"
        AppLanguage.RU -> "Уплаченный НДС за 1 месяц"
        AppLanguage.IT -> "IVA Pagata in 1 Mese"
    }

    val vatAnalysis: String get() = when (language) {
        AppLanguage.TR -> "Aylık KDV ve Vergi Analizi"
        AppLanguage.EN -> "Monthly VAT & Tax Analysis"
        AppLanguage.DE -> "Monatliche MwSt.- und Steueranalyse"
        AppLanguage.ES -> "Análisis Mensual de IVA e Impuestos"
        AppLanguage.FR -> "Analyse mensuelle de TVA et taxes"
        AppLanguage.AR -> "تحليل ضريبة القيمة المضافة الشهري"
        AppLanguage.RU -> "Ежемесячный анализ НДС"
        AppLanguage.IT -> "Analisi Mensile IVA e Imposte"
    }

    val netAmountWithoutVat: String get() = when (language) {
        AppLanguage.TR -> "KDV Hariç Net Harcama"
        AppLanguage.EN -> "Net Expense (excl. VAT)"
        AppLanguage.DE -> "Nettoausgaben (ohne MwSt.)"
        AppLanguage.ES -> "Gasto Neto (sin IVA)"
        AppLanguage.FR -> "Dépense nette (hors TVA)"
        AppLanguage.AR -> "صافي المصروفات (بدون الضريبة)"
        AppLanguage.RU -> "Чистые расходы (без НДС)"
        AppLanguage.IT -> "Spesa Netta (escl. IVA)"
    }

    val grossAmountWithVat: String get() = when (language) {
        AppLanguage.TR -> "KDV Dahil Toplam Harcama"
        AppLanguage.EN -> "Gross Expense (incl. VAT)"
        AppLanguage.DE -> "Gesamtausgaben (inkl. MwSt.)"
        AppLanguage.ES -> "Gasto Total (con IVA)"
        AppLanguage.FR -> "Dépense totale (TTC)"
        AppLanguage.AR -> "إجمالي المصروفات (شامل الضريبة)"
        AppLanguage.RU -> "Всего расходов (с НДС)"
        AppLanguage.IT -> "Spesa Totale (incl. IVA)"
    }

    val effectiveVatRate: String get() = when (language) {
        AppLanguage.TR -> "Ortalama KDV Yükü"
        AppLanguage.EN -> "Effective VAT Burden"
        AppLanguage.DE -> "Effektive MwSt.-Belastung"
        AppLanguage.ES -> "Carga Media de IVA"
        AppLanguage.FR -> "Taux de TVA effectif moyen"
        AppLanguage.AR -> "متوسط عبء ضريبة القيمة المضافة"
        AppLanguage.RU -> "Средняя ставка НДС"
        AppLanguage.IT -> "Carico Medio IVA"
    }

    val tabCategoryMonthly: String get() = when (language) {
        AppLanguage.TR -> "Aylık Kategori Pasta Grafiği"
        AppLanguage.EN -> "Monthly Category Pie Chart"
        AppLanguage.DE -> "Kategorie-Kreisdiagramm"
        AppLanguage.ES -> "Gráfico Circular de Categorías"
        AppLanguage.FR -> "Graphique Circulaire des Catégories"
        AppLanguage.AR -> "مخطط دائري للمصروفات حسب الفئة"
        AppLanguage.RU -> "Круговая диаграмма категорий"
        AppLanguage.IT -> "Grafico a Torta delle Categorie"
    }

    val tabCategoryWeekly: String get() = when (language) {
        AppLanguage.TR -> "Haftalık Kategori Kıyaslama"
        AppLanguage.EN -> "Weekly Category Comparison"
        AppLanguage.DE -> "Wöchentlicher Vergleich"
        AppLanguage.ES -> "Comparación Semanal"
        AppLanguage.FR -> "Comparaison Hebdomadaire"
        AppLanguage.AR -> "المقارنة الأسبوعية"
        AppLanguage.RU -> "Еженедельное сравнение"
        AppLanguage.IT -> "Confronto Settimanale"
    }

    val tabProductSpend: String get() = when (language) {
        AppLanguage.TR -> "Ürün Bazında Harcama"
        AppLanguage.EN -> "Item-based Spending"
        AppLanguage.DE -> "Produktausgaben"
        AppLanguage.ES -> "Gasto por Producto"
        AppLanguage.FR -> "Dépenses par Article"
        AppLanguage.AR -> "الإنفاق حسب المنتج"
        AppLanguage.RU -> "Расходы по товарам"
        AppLanguage.IT -> "Spesa per Articolo"
    }

    val tabPriceTrend: String get() = when (language) {
        AppLanguage.TR -> "Ürün Fiyat Artış Trendi"
        AppLanguage.EN -> "Product Price Increase Trend"
        AppLanguage.DE -> "Preisentwicklungstrend"
        AppLanguage.ES -> "Tendencia de Precios"
        AppLanguage.FR -> "Tendance des Prix"
        AppLanguage.AR -> "اتجاه تغير أسعار المنتجات"
        AppLanguage.RU -> "Динамика цен на товары"
        AppLanguage.IT -> "Andamento dei Prezzi"
    }

    val selectProductForTrend: String get() = when (language) {
        AppLanguage.TR -> "Fiyat Trendini Görmek İçin Ürün Seçin:"
        AppLanguage.EN -> "Select Product for Price Trend:"
        AppLanguage.DE -> "Produkt für Trend auswählen:"
        AppLanguage.ES -> "Seleccione Producto para Tendencia:"
        AppLanguage.FR -> "Sélectionner un produit pour la tendance :"
        AppLanguage.AR -> "اختر منتجاً لعرض اتجاه سعره:"
        AppLanguage.RU -> "Выберите товар для просмотра цен:"
        AppLanguage.IT -> "Seleziona Prodotto per l'andamento:"
    }

    val priceChange6Months: String get() = when (language) {
        AppLanguage.TR -> "Son 6 Aylık Fiyat Değişimi"
        AppLanguage.EN -> "6-Month Price Change"
        AppLanguage.DE -> "6-Monats-Preisentwicklung"
        AppLanguage.ES -> "Cambio de Precio en 6 Meses"
        AppLanguage.FR -> "Évolution du Prix sur 6 Mois"
        AppLanguage.AR -> "تغير السعر خلال آخر 6 أشهر"
        AppLanguage.RU -> "Изменение цены за 6 месяцев"
        AppLanguage.IT -> "Variazione Prezzo ultimi 6 Mesi"
    }

    // Family & Members
    val familyMembersTitle: String get() = when (language) {
        AppLanguage.TR -> "Aile Bireyleri Yönetimi"
        AppLanguage.EN -> "Family Members Management"
        AppLanguage.DE -> "Familienmitglieder-Verwaltung"
        AppLanguage.ES -> "Gestión de Miembros Familiares"
        AppLanguage.FR -> "Gestion des Membres de la Famille"
        AppLanguage.AR -> "إدارة أفراد الأسرة"
        AppLanguage.RU -> "Управление членами семьи"
        AppLanguage.IT -> "Gestione Membri Familiari"
    }

    val addMember: String get() = when (language) {
        AppLanguage.TR -> "Aile Bireyi Ekle"
        AppLanguage.EN -> "Add Family Member"
        AppLanguage.DE -> "Mitglied hinzufügen"
        AppLanguage.ES -> "Añadir Miembro Familiar"
        AppLanguage.FR -> "Ajouter un Membre"
        AppLanguage.AR -> "إضافة فرد جديد"
        AppLanguage.RU -> "Добавить члена семьи"
        AppLanguage.IT -> "Aggiungi Membro"
    }

    val activeProfile: String get() = when (language) {
        AppLanguage.TR -> "Aktif Kullanıcı"
        AppLanguage.EN -> "Active User"
        AppLanguage.DE -> "Aktiver Benutzer"
        AppLanguage.ES -> "Usuario Activo"
        AppLanguage.FR -> "Utilisateur Actif"
        AppLanguage.AR -> "المستخدم النشط"
        AppLanguage.RU -> "Текущий пользователь"
        AppLanguage.IT -> "Utente Attivo"
    }

    val memberApproved: String get() = when (language) {
        AppLanguage.TR -> "Onaylandı"
        AppLanguage.EN -> "Approved"
        AppLanguage.DE -> "Bestätigt"
        AppLanguage.ES -> "Aprobado"
        AppLanguage.FR -> "Approuvé"
        AppLanguage.AR -> "تم التأكيد"
        AppLanguage.RU -> "Подтвержден"
        AppLanguage.IT -> "Approvato"
    }

    val memberPending: String get() = when (language) {
        AppLanguage.TR -> "Onay Bekliyor (SMS)"
        AppLanguage.EN -> "Pending Approval (SMS)"
        AppLanguage.DE -> "Ausstehend"
        AppLanguage.ES -> "Pendiente de Aprobación"
        AppLanguage.FR -> "En Attente (SMS)"
        AppLanguage.AR -> "بانتظار التأكيد (SMS)"
        AppLanguage.RU -> "Ожидает подтверждения"
        AppLanguage.IT -> "In Attesa (SMS)"
    }

    val approveButton: String get() = when (language) {
        AppLanguage.TR -> "Telefonu Onayla"
        AppLanguage.EN -> "Verify Phone"
        AppLanguage.DE -> "Telefon bestätigen"
        AppLanguage.ES -> "Verificar Teléfono"
        AppLanguage.FR -> "Vérifier le Téléphone"
        AppLanguage.AR -> "تأكيد رقم الهاتف"
        AppLanguage.RU -> "Подтвердить телефон"
        AppLanguage.IT -> "Verifica Telefono"
    }

    val salary: String get() = when (language) {
        AppLanguage.TR -> "Aylık Maaş"
        AppLanguage.EN -> "Monthly Salary"
        AppLanguage.DE -> "Monatsgehalt"
        AppLanguage.ES -> "Salario Mensual"
        AppLanguage.FR -> "Salaire Mensuel"
        AppLanguage.AR -> "الراتب الشهري"
        AppLanguage.RU -> "Ежемесячная зарплата"
        AppLanguage.IT -> "Stipendio Mensile"
    }

    val additionalIncome: String get() = when (language) {
        AppLanguage.TR -> "Ek Gelir (Kira, Prim vb.)"
        AppLanguage.EN -> "Extra Income"
        AppLanguage.DE -> "Zusatzeinkommen"
        AppLanguage.ES -> "Ingreso Adicional"
        AppLanguage.FR -> "Revenus Complémentaires"
        AppLanguage.AR -> "دخل إضافي"
        AppLanguage.RU -> "Дополнительный доход"
        AppLanguage.IT -> "Entrate Extra"
    }

    val phoneNumberWithCountry: String get() = when (language) {
        AppLanguage.TR -> "Ülke Kodu ve Telefon Numarası"
        AppLanguage.EN -> "Country Code & Phone Number"
        AppLanguage.DE -> "Ländercode & Telefonnummer"
        AppLanguage.ES -> "Código de País y Teléfono"
        AppLanguage.FR -> "Indicatif & Téléphone"
        AppLanguage.AR -> "رمز الدولة ورقم الهاتف"
        AppLanguage.RU -> "Код страны и номер телефона"
        AppLanguage.IT -> "Prefisso & Telefono"
    }

    // Receipt Form Fields
    val merchantName: String get() = when (language) {
        AppLanguage.TR -> "Firma / Mağaza Adı"
        AppLanguage.EN -> "Merchant / Store Name"
        AppLanguage.DE -> "Geschäftsname"
        AppLanguage.ES -> "Nombre del Comercio"
        AppLanguage.FR -> "Nom du Magasin"
        AppLanguage.AR -> "اسم المتجر / الشركة"
        AppLanguage.RU -> "Название магазина"
        AppLanguage.IT -> "Nome Esercente"
    }

    val receiptDate: String get() = when (language) {
        AppLanguage.TR -> "Fiş Tarihi"
        AppLanguage.EN -> "Receipt Date"
        AppLanguage.DE -> "Belegdatum"
        AppLanguage.ES -> "Fecha del Recibo"
        AppLanguage.FR -> "Date du Reçu"
        AppLanguage.AR -> "تاريخ الإيصال"
        AppLanguage.RU -> "Дата чека"
        AppLanguage.IT -> "Data Scontrino"
    }

    val category: String get() = when (language) {
        AppLanguage.TR -> "Kategori"
        AppLanguage.EN -> "Category"
        AppLanguage.DE -> "Kategorie"
        AppLanguage.ES -> "Categoría"
        AppLanguage.FR -> "Catégorie"
        AppLanguage.AR -> "الفئة"
        AppLanguage.RU -> "Категория"
        AppLanguage.IT -> "Categoria"
    }

    val vatRate: String get() = when (language) {
        AppLanguage.TR -> "KDV Oranı (%)"
        AppLanguage.EN -> "VAT Rate (%)"
        AppLanguage.DE -> "MwSt-Satz (%)"
        AppLanguage.ES -> "Tasa de IVA (%)"
        AppLanguage.FR -> "Taux de TVA (%)"
        AppLanguage.AR -> "نسبة ضريبة القيمة المضافة (%)"
        AppLanguage.RU -> "Ставка НДС (%)"
        AppLanguage.IT -> "Aliquota IVA (%)"
    }

    val vatAmount: String get() = when (language) {
        AppLanguage.TR -> "KDV Tutarı"
        AppLanguage.EN -> "VAT Amount"
        AppLanguage.DE -> "MwSt-Betrag"
        AppLanguage.ES -> "Importe del IVA"
        AppLanguage.FR -> "Montant de la TVA"
        AppLanguage.AR -> "مبلغ الضريبة"
        AppLanguage.RU -> "Сумма НДС"
        AppLanguage.IT -> "Importo IVA"
    }

    val totalAmount: String get() = when (language) {
        AppLanguage.TR -> "Toplam Tutar"
        AppLanguage.EN -> "Total Amount"
        AppLanguage.DE -> "Gesamtbetrag"
        AppLanguage.ES -> "Importe Total"
        AppLanguage.FR -> "Montant Total"
        AppLanguage.AR -> "المبلغ الإجمالي"
        AppLanguage.RU -> "Итоговая сумма"
        AppLanguage.IT -> "Importo Totale"
    }

    val productsList: String get() = when (language) {
        AppLanguage.TR -> "Ürünler & Fiyat Listesi"
        AppLanguage.EN -> "Products & Price List"
        AppLanguage.DE -> "Produktliste"
        AppLanguage.ES -> "Lista de Productos"
        AppLanguage.FR -> "Liste des Produits"
        AppLanguage.AR -> "قائمة المنتجات والأسعار"
        AppLanguage.RU -> "Список товаров и цен"
        AppLanguage.IT -> "Lista Prodotti e Prezzi"
    }

    val addProductItem: String get() = when (language) {
        AppLanguage.TR -> "+ Ürün Ekle"
        AppLanguage.EN -> "+ Add Item"
        AppLanguage.DE -> "+ Produkt hinzufügen"
        AppLanguage.ES -> "+ Añadir Producto"
        AppLanguage.FR -> "+ Ajouter un Produit"
        AppLanguage.AR -> "+ إضافة منتج"
        AppLanguage.RU -> "+ Добавить товар"
        AppLanguage.IT -> "+ Aggiungi Articolo"
    }

    val itemCancelled: String get() = when (language) {
        AppLanguage.TR -> "İptal / İade Edildi (-)"
        AppLanguage.EN -> "Cancelled / Refunded (-)"
        AppLanguage.DE -> "Storniert / Erstattet (-)"
        AppLanguage.ES -> "Cancelado / Reembolsado (-)"
        AppLanguage.FR -> "Annulé / Remboursé (-)"
        AppLanguage.AR -> "ملغى / مسترد (-)"
        AppLanguage.RU -> "Отменено / Возврат (-)"
        AppLanguage.IT -> "Annullato / Rimborsato (-)"
    }

    val saveExpense: String get() = when (language) {
        AppLanguage.TR -> "Fişi Kaydet"
        AppLanguage.EN -> "Save Receipt"
        AppLanguage.DE -> "Beleg speichern"
        AppLanguage.ES -> "Guardar Recibo"
        AppLanguage.FR -> "Enregistrer le reçu"
        AppLanguage.AR -> "حفظ الإيصال"
        AppLanguage.RU -> "Сохранить чек"
        AppLanguage.IT -> "Salva Ricevuta"
    }

    val saveReceipt: String get() = when (language) {
        AppLanguage.TR -> "Fişi Kaydet"
        AppLanguage.EN -> "Save Receipt"
        AppLanguage.DE -> "Beleg speichern"
        AppLanguage.ES -> "Guardar Recibo"
        AppLanguage.FR -> "Enregistrer le reçu"
        AppLanguage.AR -> "حفظ الإيصال"
        AppLanguage.RU -> "Сохранить чек"
        AppLanguage.IT -> "Salva Ricevuta"
    }

    val saveRecord: String get() = when (language) {
        AppLanguage.TR -> "Kaydet"
        AppLanguage.EN -> "Save"
        AppLanguage.DE -> "Speichern"
        AppLanguage.ES -> "Guardar"
        AppLanguage.FR -> "Enregistrer"
        AppLanguage.AR -> "حفظ"
        AppLanguage.RU -> "Сохранить"
        AppLanguage.IT -> "Salva"
    }

    val cancel: String get() = when (language) {
        AppLanguage.TR -> "İptal"
        AppLanguage.EN -> "Cancel"
        AppLanguage.DE -> "Abbrechen"
        AppLanguage.ES -> "Cancelar"
        AppLanguage.FR -> "Annuler"
        AppLanguage.AR -> "إلغاء"
        AppLanguage.RU -> "Отмена"
        AppLanguage.IT -> "Annulla"
    }

    // Theme & Notifications
    val themeSettings: String get() = when (language) {
        AppLanguage.TR -> "Görünüm ve Tema Modu"
        AppLanguage.EN -> "Theme & Appearance"
        AppLanguage.DE -> "Erscheinungsbild & Thema"
        AppLanguage.ES -> "Tema y Apariencia"
        AppLanguage.FR -> "Thème et Apparence"
        AppLanguage.AR -> "المظهر والسمة"
        AppLanguage.RU -> "Тема и внешний вид"
        AppLanguage.IT -> "Tema & Aspetto"
    }

    val darkModeTitle: String get() = when (language) {
        AppLanguage.TR -> "Karanlık Tema (Dark Mode)"
        AppLanguage.EN -> "Dark Mode Theme"
        AppLanguage.DE -> "Dunkelmodus"
        AppLanguage.ES -> "Modo Oscuro"
        AppLanguage.FR -> "Mode Sombre"
        AppLanguage.AR -> "الوضع الداكن"
        AppLanguage.RU -> "Темная тема"
        AppLanguage.IT -> "Modalità Scura"
    }

    val darkModeDesc: String get() = when (language) {
        AppLanguage.TR -> "Düşük ışıklı ortamlarda gözü yormayan renk paleti ve yüksek kontrastlı grafikler"
        AppLanguage.EN -> "High-contrast visuals and comfortable low-light colors for graphs and dashboard"
        AppLanguage.DE -> "Angenehme Farben für schwaches Licht"
        AppLanguage.ES -> "Colores cómodos para entornos con poca luz"
        AppLanguage.FR -> "Couleurs douces pour les environnements sombres"
        AppLanguage.AR -> "ألوان مريحة للعين في الإضاءة المنخفضة"
        AppLanguage.RU -> "Комфортная палитра для темного времени суток"
        AppLanguage.IT -> "Colori confortevoli in ambienti poco illuminati"
    }

    val notificationSettings: String get() = when (language) {
        AppLanguage.TR -> "Bütçe ve Limit Bildirimleri"
        AppLanguage.EN -> "Budget & Limit Notifications"
        AppLanguage.DE -> "Budget-Benachrichtigungen"
        AppLanguage.ES -> "Notificaciones de Presupuesto"
        AppLanguage.FR -> "Notifications de Budget"
        AppLanguage.AR -> "إشعارات وتنبيهات الميزانية"
        AppLanguage.RU -> "Уведомления о лимитах бюджета"
        AppLanguage.IT -> "Notifiche di Budget"
    }

    val notificationDesc: String get() = when (language) {
        AppLanguage.TR -> "Aylık limit %80'e ulaştığında ve limit aşıldığında anlık yerel uyarı bildirimleri"
        AppLanguage.EN -> "Local alert notifications when 80% threshold is reached or limit is exceeded"
        AppLanguage.DE -> "Benachrichtigung bei 80% und Limitüberschreitung"
        AppLanguage.ES -> "Alertas al alcanzar el 80% o exceder el límite"
        AppLanguage.FR -> "Alertes à 80% et en cas de dépassement"
        AppLanguage.AR -> "تنبيهات فورية عند بلوغ 80% أو تجاوز الحد"
        AppLanguage.RU -> "Оповещения при достижении 80% и превышении лимита"
        AppLanguage.IT -> "Avvisi al raggiungimento dell'80% o superamento del limite"
    }

    val sendTestNotification: String get() = when (language) {
        AppLanguage.TR -> "Test Bildirimi Gönder"
        AppLanguage.EN -> "Send Test Notification"
        AppLanguage.DE -> "Test-Benachrichtigung senden"
        AppLanguage.ES -> "Enviar Notificación de Prueba"
        AppLanguage.FR -> "Envoyer une Notification Test"
        AppLanguage.AR -> "إرسال إشعار تجريبي"
        AppLanguage.RU -> "Отправить тестовое уведомление"
        AppLanguage.IT -> "Invia Notifica di Test"
    }

    val languageSelection: String get() = when (language) {
        AppLanguage.TR -> "Uygulama Dili / Language"
        AppLanguage.EN -> "App Language"
        AppLanguage.DE -> "App-Sprache"
        AppLanguage.ES -> "Idioma de la Aplicación"
        AppLanguage.FR -> "Langue de l'Application"
        AppLanguage.AR -> "لغة التطبيق"
        AppLanguage.RU -> "Язык приложения"
        AppLanguage.IT -> "Lingua dell'Applicazione"
    }

    val otherLanguagesComboBox: String get() = when (language) {
        AppLanguage.TR -> "Diğer Diller (Açılır Liste)"
        AppLanguage.EN -> "Other Languages (Dropdown)"
        AppLanguage.DE -> "Weitere Sprachen"
        AppLanguage.ES -> "Otros Idiomas"
        AppLanguage.FR -> "Autres Langues"
        AppLanguage.AR -> "لغات أخرى (القائمة المنسدلة)"
        AppLanguage.RU -> "Другие языки (список)"
        AppLanguage.IT -> "Altre Lingue"
    }

    val budgetLimitSettings: String get() = when (language) {
        AppLanguage.TR -> "Aylık Harcama Limiti & Uyarı Oranı"
        AppLanguage.EN -> "Monthly Spending Limit & Alert Threshold"
        AppLanguage.DE -> "Ausgabenlimit & Warnstufe"
        AppLanguage.ES -> "Límite de Gasto y Alerta"
        AppLanguage.FR -> "Plafond Mensuel & Seuil d'Alerte"
        AppLanguage.AR -> "إعدادات حد الإنفاق الشهري"
        AppLanguage.RU -> "Настройка лимита расходов и порога"
        AppLanguage.IT -> "Impostazioni Limite Mensile"
    }

    val cameraPermissionTitle: String get() = when (language) {
        AppLanguage.TR -> "Kamera İzni Gerekli"
        AppLanguage.EN -> "Camera Permission Required"
        AppLanguage.DE -> "Kameraberechtigung erforderlich"
        AppLanguage.ES -> "Permiso de Cámara Requerido"
        AppLanguage.FR -> "Permission Caméra Requise"
        AppLanguage.AR -> "إذن الكاميرا مطلوب"
        AppLanguage.RU -> "Требуется доступ к камере"
        AppLanguage.IT -> "Permesso Fotocamera Richiesto"
    }

    val cameraPermissionDesc: String get() = when (language) {
        AppLanguage.TR -> "Fişleri otomatik OCR ile tarayıp bütçenize aktarabilmek için kamera izni vermeniz gerekmektedir."
        AppLanguage.EN -> "Camera permission is required to scan physical receipts and extract financial data automatically."
        AppLanguage.DE -> "Kamerazugriff wird benötigt, um Belege per OCR zu scannen."
        AppLanguage.ES -> "Se requiere permiso de cámara para escanear recibos mediante OCR."
        AppLanguage.FR -> "La caméra est requise pour numériser les reçus avec l'OCR."
        AppLanguage.AR -> "يلزم إذن الكاميرا لمسح الإيصالات واستخراج البيانات مالياً."
        AppLanguage.RU -> "Разрешение на камеру необходимо для автоматического сканирования чеков."
        AppLanguage.IT -> "Il permesso fotocamera è necessario per scansionare gli scontrini."
    }

    val grantPermission: String get() = when (language) {
        AppLanguage.TR -> "İzin Ver"
        AppLanguage.EN -> "Grant Permission"
        AppLanguage.DE -> "Erlauben"
        AppLanguage.ES -> "Conceder Permiso"
        AppLanguage.FR -> "Autoriser"
        AppLanguage.AR -> "منح الإذن"
        AppLanguage.RU -> "Разрешить"
        AppLanguage.IT -> "Consenti"
    }

    val scanningReceipt: String get() = when (language) {
        AppLanguage.TR -> "Fiş yapay zeka ile taranıyor ve ayrıştırılıyor..."
        AppLanguage.EN -> "Scanning receipt and extracting data with AI..."
        AppLanguage.DE -> "Beleg wird mit KI gescannt..."
        AppLanguage.ES -> "Escaneando recibo con IA..."
        AppLanguage.FR -> "Numérisation et analyse du reçu par IA..."
        AppLanguage.AR -> "جاري مسح الإيصال واستخراج البيانات بالذكاء الاصطناعي..."
        AppLanguage.RU -> "Чек сканируется и анализируется с помощью ИИ..."
        AppLanguage.IT -> "Scansione ed elaborazione dello scontrino con AI..."
    }

    val reviewScannedReceipt: String get() = when (language) {
        AppLanguage.TR -> "Taranan Fişi Onaylayın"
        AppLanguage.EN -> "Review Scanned Receipt"
        AppLanguage.DE -> "Gescannter Beleg prüfen"
        AppLanguage.ES -> "Revisar Recibo Escaneado"
        AppLanguage.FR -> "Vérifier le Reçu Numérisé"
        AppLanguage.AR -> "مراجعة وتأكيد الإيصال"
        AppLanguage.RU -> "Проверьте отсканированный чек"
        AppLanguage.IT -> "Controlla Scontrino Scansionato"
    }

    // Income & Fixed Expenses
    val incomeSourcesTitle: String get() = when (language) {
        AppLanguage.TR -> "Aylık Aile Gelirleri (Maaş & Kira)"
        AppLanguage.EN -> "Monthly Family Incomes (Salary & Rent)"
        AppLanguage.DE -> "Monatliche Familieneinkünfte"
        AppLanguage.ES -> "Ingresos Familiares Mensuales"
        AppLanguage.FR -> "Revenus Familiaux Mensuels"
        AppLanguage.AR -> "الدخل الشهري للأسرة (الرواتب والإيجار)"
        AppLanguage.RU -> "Семейные доходы (Зарплата и Аренда)"
        AppLanguage.IT -> "Entrate Mensili Familiari"
    }

    val incomeSourcesDesc: String get() = when (language) {
        AppLanguage.TR -> "Maaş, kira geliri ve ek kazançların tutarları ile hesaba geçeceği gün bilgileri"
        AppLanguage.EN -> "Salaries, rental incomes, and deposit dates"
        AppLanguage.DE -> "Gehälter, Mieteinnahmen und Auszahlungstage"
        AppLanguage.ES -> "Salarios, rentas y fechas de abono"
        AppLanguage.FR -> "Salaires, revenus locatifs et dates de versement"
        AppLanguage.AR -> "الرواتب ودخل الإيجار ومواعيد إيداعها في الحساب"
        AppLanguage.RU -> "Зарплаты, доходы от аренды и даты зачисления"
        AppLanguage.IT -> "Stipendi, entrate da affitto e date di accredito"
    }

    val fixedExpensesTitle: String get() = when (language) {
        AppLanguage.TR -> "Sabit Giderler & Kredi Kartı Ödemeleri"
        AppLanguage.EN -> "Fixed Expenses & Credit Card Payments"
        AppLanguage.DE -> "Fixkosten & Kreditkartenzahlungen"
        AppLanguage.ES -> "Gastos Fijos y Tarjetas de Crédito"
        AppLanguage.FR -> "Dépenses Fixes & Cartes de Crédit"
        AppLanguage.AR -> "المصروفات الثابتة ومدفوعات البطاقات الائتمانية"
        AppLanguage.RU -> "Постоянные расходы и платежи по кредиткам"
        AppLanguage.IT -> "Spese Fisse & Carte di Credito"
    }

    val fixedExpensesDesc: String get() = when (language) {
        AppLanguage.TR -> "Kira gideri, 3 adet kredi kartı ödemeleri, aidat ve faturaların ödeme günleri"
        AppLanguage.EN -> "Rent, 3 credit card due dates, dues, and utility bills"
        AppLanguage.DE -> "Miete, 3 Kreditkarten-Fälligkeiten, Hausgeld und Rechnungen"
        AppLanguage.ES -> "Alquiler, 3 tarjetas de crédito, gastos de comunidad y facturas"
        AppLanguage.FR -> "Loyer, 3 cartes de crédit, charges et factures"
        AppLanguage.AR -> "إيجار المنزل، مدفوعات 3 بطاقات ائتمان، الفواتير والاشتراكات"
        AppLanguage.RU -> "Аренда жилья, 3 кредитные карты, ЖКУ и счета"
        AppLanguage.IT -> "Affitto, scadenze 3 carte di credito, spese e bollette"
    }

    val addIncomeSource: String get() = when (language) {
        AppLanguage.TR -> "Yeni Gelir Ekle"
        AppLanguage.EN -> "Add Income Source"
        AppLanguage.DE -> "Einkommen hinzufügen"
        AppLanguage.ES -> "Añadir Ingreso"
        AppLanguage.FR -> "Ajouter un Revenu"
        AppLanguage.AR -> "إضافة مصدر دخل"
        AppLanguage.RU -> "Добавить доход"
        AppLanguage.IT -> "Aggiungi Entrata"
    }

    val addFixedExpense: String get() = when (language) {
        AppLanguage.TR -> "Sabit Gider Ekle"
        AppLanguage.EN -> "Add Fixed Expense"
        AppLanguage.DE -> "Fixkosten hinzufügen"
        AppLanguage.ES -> "Añadir Gasto Fijo"
        AppLanguage.FR -> "Ajouter Dépense Fixe"
        AppLanguage.AR -> "إضافة مصروف ثابت"
        AppLanguage.RU -> "Добавить постоянный расход"
        AppLanguage.IT -> "Aggiungi Spesa Fissa"
    }

    val netDisposableIncome: String get() = when (language) {
        AppLanguage.TR -> "Net Harcanabilir Bütçe"
        AppLanguage.EN -> "Net Disposable Budget"
        AppLanguage.DE -> "Netto Verfügbares Budget"
        AppLanguage.ES -> "Presupuesto Neto Disponible"
        AppLanguage.FR -> "Budget Net Disponible"
        AppLanguage.AR -> "صافي الميزانية المتاحة للإنفاق"
        AppLanguage.RU -> "Чистый доступный бюджет"
        AppLanguage.IT -> "Budget Netto Disponibile"
    }

    val upcomingSchedule: String get() = when (language) {
        AppLanguage.TR -> "Ödeme ve Gelir Takvimi"
        AppLanguage.EN -> "Payment & Income Schedule"
        AppLanguage.DE -> "Zahlungs- & Einnahmenplan"
        AppLanguage.ES -> "Calendario de Pagos e Ingresos"
        AppLanguage.FR -> "Calendrier des Paiements et Revenus"
        AppLanguage.AR -> "جدول المدفوعات والإيرادات"
        AppLanguage.RU -> "График платежей и поступлений"
        AppLanguage.IT -> "Scadenziario Pagamenti & Incassi"
    }

    val budgetFormulaTitle: String get() = when (language) {
        AppLanguage.TR -> "Otomatik Bütçe Hesaplama (Gelir - Sabitler - Fişler)"
        AppLanguage.EN -> "Automated Budget Calculation (Income - Fixed - Receipts)"
        AppLanguage.DE -> "Automatische Budgetberechnung (Einnahmen - Fixkosten - Belege)"
        AppLanguage.ES -> "Cálculo Automático del Presupuesto (Ingresos - Fijos - Recibos)"
        AppLanguage.FR -> "Calcul Automatique du Budget (Revenus - Fixes - Reçus)"
        AppLanguage.AR -> "الحساب التلقائي للميزانية (الدخل - الثابت - الإيصالات)"
        AppLanguage.RU -> "Автоматический расчет бюджета (Доход - Постоянные - Чеки)"
        AppLanguage.IT -> "Calcolo Automatico del Budget (Entrate - Spese Fisse - Scontrini)"
    }

    val dailySpendableBudget: String get() = when (language) {
        AppLanguage.TR -> "Günlük Harcanabilir Limit"
        AppLanguage.EN -> "Daily Spendable Limit"
        AppLanguage.DE -> "Täglich verfügbares Limit"
        AppLanguage.ES -> "Límite Diario Gastable"
        AppLanguage.FR -> "Limite Quotidienne Dépensable"
        AppLanguage.AR -> "الحد اليومي المتاح للإنفاق"
        AppLanguage.RU -> "Дневной лимит расходов"
        AppLanguage.IT -> "Limite Giornaliero Spendibile"
    }

    val totalOutflowLabel: String get() = when (language) {
        AppLanguage.TR -> "Toplam Aylık Çıkış (Sabit + Fişler)"
        AppLanguage.EN -> "Total Monthly Outflow (Fixed + Receipts)"
        AppLanguage.DE -> "Gesamter monatlicher Abfluss (Fix + Belege)"
        AppLanguage.ES -> "Salida Total Mensual (Fijo + Recibos)"
        AppLanguage.FR -> "Sortie Totale Mensuelle (Fixes + Reçus)"
        AppLanguage.AR -> "إجمالي المصروفات الشهرية (الثابتة + الإيصالات)"
        AppLanguage.RU -> "Всего расходов за месяц (Постоянные + Чеки)"
        AppLanguage.IT -> "Uscite Totali Mensili (Fisse + Scontrini)"
    }

    // Shopping List Feature
    val shoppingListTitle: String get() = when (language) {
        AppLanguage.TR -> "Ortak Alışveriş Listesi"
        AppLanguage.EN -> "Family Shopping List"
        AppLanguage.DE -> "Gemeinsame Einkaufsliste"
        AppLanguage.ES -> "Lista de Compras Familiar"
        AppLanguage.FR -> "Liste de Courses Familiale"
        AppLanguage.AR -> "قائمة تسوق العائلة"
        AppLanguage.RU -> "Семейный список покупок"
        AppLanguage.IT -> "Lista Spesa Familiare"
    }

    val shoppingListSubtitle: String get() = when (language) {
        AppLanguage.TR -> "Market ürün gruplarından tek tıkla seçin veya yeni ürün ekleyin"
        AppLanguage.EN -> "Select from market categories with 1-click or add custom items"
        AppLanguage.DE -> "Wählen Sie aus Kategorien mit 1 Klick oder fügen Sie Artikel hinzu"
        AppLanguage.ES -> "Seleccione de las categorías con 1 clic o agregue artículos"
        AppLanguage.FR -> "Sélectionnez parmi les catégories en 1 clic ou ajoutez des articles"
        AppLanguage.AR -> "اختر من تصنيفات الماركت بضغطة زر أو أضف منتجات مخصصة"
        AppLanguage.RU -> "Выбирайте из категорий в 1 клик или добавляйте свои товары"
        AppLanguage.IT -> "Scegli dalle categorie con 1 clic o aggiungi nuovi articoli"
    }

    val marketGroupsTitle: String get() = when (language) {
        AppLanguage.TR -> "Market Ürün Grupları"
        AppLanguage.EN -> "Supermarket Categories"
        AppLanguage.DE -> "Supermarkt-Kategorien"
        AppLanguage.ES -> "Categorías del Supermercado"
        AppLanguage.FR -> "Rayons du Supermarché"
        AppLanguage.AR -> "أقسام السوبرماركت"
        AppLanguage.RU -> "Категории супермаркета"
        AppLanguage.IT -> "Reparti Supermercato"
    }

    val tabPending: String get() = when (language) {
        AppLanguage.TR -> "Alınacaklar"
        AppLanguage.EN -> "To Buy"
        AppLanguage.DE -> "Zu kaufen"
        AppLanguage.ES -> "Por comprar"
        AppLanguage.FR -> "À acheter"
        AppLanguage.AR -> "المطلوب شراؤه"
        AppLanguage.RU -> "Купить"
        AppLanguage.IT -> "Da acquistare"
    }

    val tabPurchased: String get() = when (language) {
        AppLanguage.TR -> "Alınanlar (Sepet)"
        AppLanguage.EN -> "Purchased (Cart)"
        AppLanguage.DE -> "Gekauft (Wagen)"
        AppLanguage.ES -> "Comprados (Carrito)"
        AppLanguage.FR -> "Achetés (Panier)"
        AppLanguage.AR -> "تم الشراء (السلة)"
        AppLanguage.RU -> "Куплено (Корзина)"
        AppLanguage.IT -> "Acquistati (Carrello)"
    }

    val tabAll: String get() = when (language) {
        AppLanguage.TR -> "Tüm Liste"
        AppLanguage.EN -> "All Items"
        AppLanguage.DE -> "Alle Artikel"
        AppLanguage.ES -> "Todos los Artículos"
        AppLanguage.FR -> "Tous les Articles"
        AppLanguage.AR -> "كل العناصر"
        AppLanguage.RU -> "Все товары"
        AppLanguage.IT -> "Tutti gli Articoli"
    }

    val addShoppingItem: String get() = when (language) {
        AppLanguage.TR -> "Listeye Ürün Ekle"
        AppLanguage.EN -> "Add Item to List"
        AppLanguage.DE -> "Artikel hinzufügen"
        AppLanguage.ES -> "Agregar a la Lista"
        AppLanguage.FR -> "Ajouter à la Liste"
        AppLanguage.AR -> "إضافة عنصر للقائمة"
        AppLanguage.RU -> "Добавить в список"
        AppLanguage.IT -> "Aggiungi alla Lista"
    }

    val searchOrAddHint: String get() = when (language) {
        AppLanguage.TR -> "Ürün adı yazın (örn: 2 kg Domates, Süt 1L)..."
        AppLanguage.EN -> "Type item name (e.g. 2 kg Tomatoes, Milk)..."
        AppLanguage.DE -> "Artikel eingeben (z.B. 2 kg Tomaten, Milch)..."
        AppLanguage.ES -> "Escriba el producto (ej: 2 kg Tomates, Leche)..."
        AppLanguage.FR -> "Nom du produit (ex: 2 kg Tomates, Lait)..."
        AppLanguage.AR -> "اكتب اسم المنتج (مثل: 2 كغ طماطم، حليب)..."
        AppLanguage.RU -> "Введите название (напр. 2 кг помидоров, молоко)..."
        AppLanguage.IT -> "Scrivi il nome del prodotto (es: 2 kg Pomodori, Latte)..."
    }

    val clearPurchasedItems: String get() = when (language) {
        AppLanguage.TR -> "Alınanları Temizle"
        AppLanguage.EN -> "Clear Purchased"
        AppLanguage.DE -> "Gekaufte löschen"
        AppLanguage.ES -> "Limpiar Comprados"
        AppLanguage.FR -> "Effacer les Achetés"
        AppLanguage.AR -> "مسح العناصر المشتراة"
        AppLanguage.RU -> "Очистить купленные"
        AppLanguage.IT -> "Svuota Acquistati"
    }

    val convertToReceiptExpense: String get() = when (language) {
        AppLanguage.TR -> "Harcama / Fişe Aktar"
        AppLanguage.EN -> "Convert to Expense"
        AppLanguage.DE -> "In Ausgabe umwandeln"
        AppLanguage.ES -> "Convertir en Gasto"
        AppLanguage.FR -> "Convertir en Dépense"
        AppLanguage.AR -> "تحويل إلى مصروف / فاتورة"
        AppLanguage.RU -> "Перенести в расходы"
        AppLanguage.IT -> "Converti in Spesa"
    }

    val whoAddedLabel: String get() = when (language) {
        AppLanguage.TR -> "Ekleyen Aile Bireyi"
        AppLanguage.EN -> "Requested By"
        AppLanguage.DE -> "Hinzugefügt von"
        AppLanguage.ES -> "Agregado por"
        AppLanguage.FR -> "Ajouté par"
        AppLanguage.AR -> "أضيف بواسطة"
        AppLanguage.RU -> "Добавил(а)"
        AppLanguage.IT -> "Aggiunto da"
    }

    val liveSyncNote: String get() = when (language) {
        AppLanguage.TR -> "Tüm aile bireyleri anlık olarak listeyi görebilir ve ortak ürün ekleyebilir."
        AppLanguage.EN -> "All family members can see the list in real-time and add items together."
        AppLanguage.DE -> "Alle Familienmitglieder können die Liste live sehen und gemeinsam Artikel hinzufügen."
        AppLanguage.ES -> "Todos los miembros pueden ver la lista al instante y agregar artículos."
        AppLanguage.FR -> "Tous les membres peuvent voir la liste en direct et ajouter des articles."
        AppLanguage.AR -> "يمكن لجميع أفراد الأسرة رؤية القائمة مباشرة وإضافة المنتجات معاً."
        AppLanguage.RU -> "Все члены семьи видят список в реальном времени и могут добавлять товары."
        AppLanguage.IT -> "Tutti i familiari possono visualizzare la lista in tempo reale e aggiungere articoli."
    }

    val estimatedTotal: String get() = when (language) {
        AppLanguage.TR -> "Tahmini Tutar"
        AppLanguage.EN -> "Estimated Total"
        AppLanguage.DE -> "Geschätzte Summe"
        AppLanguage.ES -> "Total Estimado"
        AppLanguage.FR -> "Total Estimé"
        AppLanguage.AR -> "المجموع التقديري"
        AppLanguage.RU -> "Примерная сумма"
        AppLanguage.IT -> "Totale Stimato"
    }

    val noShoppingItems: String get() = when (language) {
        AppLanguage.TR -> "Alışveriş listeniz henüz boş"
        AppLanguage.EN -> "Your shopping list is empty"
        AppLanguage.DE -> "Ihre Einkaufsliste ist leer"
        AppLanguage.ES -> "Tu lista de compras está vacía"
        AppLanguage.FR -> "Votre liste de courses est vide"
        AppLanguage.AR -> "قائمة التسوق فارغة حالياً"
        AppLanguage.RU -> "Список покупок пуст"
        AppLanguage.IT -> "La tua lista della spesa è vuota"
    }

    val tabMarketComparison: String get() = when (language) {
        AppLanguage.TR -> "Market Karşılaştır"
        AppLanguage.EN -> "Market Compare"
        AppLanguage.DE -> "Märkte vergleichen"
        AppLanguage.ES -> "Comparar Mercados"
        AppLanguage.FR -> "Comparer Marchés"
        AppLanguage.AR -> "مقارنة الأسواق"
        AppLanguage.RU -> "Сравнение цен"
        AppLanguage.IT -> "Confronta Supermercati"
    }

    val cheapestMarketBadge: String get() = when (language) {
        AppLanguage.TR -> "En Ucuz Market"
        AppLanguage.EN -> "Cheapest Store"
        AppLanguage.DE -> "Günstigster Markt"
        AppLanguage.ES -> "Supermercado Más Barato"
        AppLanguage.FR -> "Magasin le Moins Cher"
        AppLanguage.AR -> "المتجر الأرخص"
        AppLanguage.RU -> "Самый выгодный магазин"
        AppLanguage.IT -> "Supermercato Più Conveniente"
    }

    val unitPriceLabel: String get() = when (language) {
        AppLanguage.TR -> "Birim Fiyat (Kg/Adet)"
        AppLanguage.EN -> "Unit Price (Kg/Pcs)"
        AppLanguage.DE -> "Stückpreis (Kg/Stk)"
        AppLanguage.ES -> "Precio Unitario (Kg/Ud)"
        AppLanguage.FR -> "Prix Unitaire (Kg/Pce)"
        AppLanguage.AR -> "سعر الوحدة (كغ/قطعة)"
        AppLanguage.RU -> "Цена за ед. (Кг/Шт)"
        AppLanguage.IT -> "Prezzo Unitario (Kg/Pz)"
    }

    val smartSplitTitle: String get() = when (language) {
        AppLanguage.TR -> "Akıllı Karma Sepet (Maksimum Tasarruf)"
        AppLanguage.EN -> "Smart Split Basket (Max Savings)"
        AppLanguage.DE -> "Smarter Korb (Maximales Sparen)"
        AppLanguage.ES -> "Cesta Mixta Inteligente"
        AppLanguage.FR -> "Panier Mixte Intelligent"
        AppLanguage.AR -> "سلة التوفير الذكية المختلطة"
        AppLanguage.RU -> "Умная сборная корзина"
        AppLanguage.IT -> "Cestino Misto Intelligente"
    }

    val quantityAndUnitLabel: String get() = when (language) {
        AppLanguage.TR -> "Miktar ve Birim (Adet / Kg)"
        AppLanguage.EN -> "Quantity & Unit (Pcs / Kg)"
        AppLanguage.DE -> "Menge & Einheit (Stk / Kg)"
        AppLanguage.ES -> "Cantidad y Unidad (Ud / Kg)"
        AppLanguage.FR -> "Quantité et Unité (Pce / Kg)"
        AppLanguage.AR -> "الكمية والوحدة (قطعة / كغ)"
        AppLanguage.RU -> "Количество и единица (Шт / Кг)"
        AppLanguage.IT -> "Quantità e Unità (Pz / Kg)"
    }

    val productGroupComboBox: String get() = when (language) {
        AppLanguage.TR -> "Ürün Grubu"
        AppLanguage.EN -> "Product Group"
        AppLanguage.DE -> "Warengruppe"
        AppLanguage.ES -> "Grupo de Productos"
        AppLanguage.FR -> "Groupe de Produits"
        AppLanguage.AR -> "مجموعة المنتجات"
        AppLanguage.RU -> "Группа товаров"
        AppLanguage.IT -> "Gruppo Prodotti"
    }

    val productsComboBox: String get() = when (language) {
        AppLanguage.TR -> "Ürünler"
        AppLanguage.EN -> "Products"
        AppLanguage.DE -> "Produkte"
        AppLanguage.ES -> "Productos"
        AppLanguage.FR -> "Produits"
        AppLanguage.AR -> "المنتجات"
        AppLanguage.RU -> "Товары"
        AppLanguage.IT -> "Prodotti"
    }

    val brandsComboBox: String get() = when (language) {
        AppLanguage.TR -> "Markalar / Çeşitler"
        AppLanguage.EN -> "Brands / Variants"
        AppLanguage.DE -> "Marken / Varianten"
        AppLanguage.ES -> "Marcas / Variantes"
        AppLanguage.FR -> "Marques / Variantes"
        AppLanguage.AR -> "العلامات التجارية"
        AppLanguage.RU -> "Бренды / Варианты"
        AppLanguage.IT -> "Marche / Varianti"
    }

    val selectBrandHint: String get() = when (language) {
        AppLanguage.TR -> "Marka Seçin (Tümü / Tercih)"
        AppLanguage.EN -> "Select Brand (All / Preferred)"
        AppLanguage.DE -> "Marke wählen (Alle / Bevorzugt)"
        AppLanguage.ES -> "Seleccionar Marca (Todas / Preferida)"
        AppLanguage.FR -> "Sélectionner la marque (Toutes / Préférée)"
        AppLanguage.AR -> "اختر العلامة التجارية"
        AppLanguage.RU -> "Выберите бренд (Все / Предпочтение)"
        AppLanguage.IT -> "Seleziona Marca (Tutte / Preferita)"
    }

    val allBrandsOrBest: String get() = when (language) {
        AppLanguage.TR -> "Tümü / Farketmez (En Uygun Fiyat)"
        AppLanguage.EN -> "All / Any Brand (Best Price)"
        AppLanguage.DE -> "Alle / Beliebig (Bester Preis)"
        AppLanguage.ES -> "Todas / Cualquier marca (Mejor precio)"
        AppLanguage.FR -> "Toutes / Peu importe (Meilleur prix)"
        AppLanguage.AR -> "الكل / أي علامة تجارية"
        AppLanguage.RU -> "Любой бренд (Лучшая цена)"
        AppLanguage.IT -> "Tutte / Qualsiasi (Miglior prezzo)"
    }

    val selectProductHint: String get() = when (language) {
        AppLanguage.TR -> "Ürün Seçin veya Yazın"
        AppLanguage.EN -> "Select or Type Product"
        AppLanguage.DE -> "Produkt wählen oder eingeben"
        AppLanguage.ES -> "Seleccionar o escribir producto"
        AppLanguage.FR -> "Sélectionner ou saisir le produit"
        AppLanguage.AR -> "اختر أو اكتب المنتج"
        AppLanguage.RU -> "Выберите или введите товар"
        AppLanguage.IT -> "Seleziona o digita prodotto"
    }

    val marketProductsComboBox: String get() = when (language) {
        AppLanguage.TR -> "Marketlerdeki Ürünler"
        AppLanguage.EN -> "Market Offerings"
        AppLanguage.DE -> "Marktangebote"
        AppLanguage.ES -> "Ofertas de Supermercados"
        AppLanguage.FR -> "Offres des Marchés"
        AppLanguage.AR -> "عروض المتاجر"
        AppLanguage.RU -> "Предложения супермаркетов"
        AppLanguage.IT -> "Offerte Supermercati"
    }

    val selectFromMarketsHint: String get() = when (language) {
        AppLanguage.TR -> "Marketlerdeki Ürünleri Seçin (Çoktan Seçmeli)"
        AppLanguage.EN -> "Select Market Items (Multi-Select)"
        AppLanguage.DE -> "Marktartikel auswählen (Mehrfachauswahl)"
        AppLanguage.ES -> "Seleccionar artículos del mercado (Selección múltiple)"
        AppLanguage.FR -> "Sélectionner les articles du marché (Sélection multiple)"
        AppLanguage.AR -> "اختر منتجات المتاجر (متعدد الخيارات)"
        AppLanguage.RU -> "Выберите товары в магазинах (Множественный выбор)"
        AppLanguage.IT -> "Seleziona prodotti del supermercato (Selezione multipla)"
    }

    val selectAll: String get() = when (language) {
        AppLanguage.TR -> "Tümünü Seç"
        AppLanguage.EN -> "Select All"
        AppLanguage.DE -> "Alle auswählen"
        AppLanguage.ES -> "Seleccionar todo"
        AppLanguage.FR -> "Tout sélectionner"
        AppLanguage.AR -> "تحديد الكل"
        AppLanguage.RU -> "Выбрать все"
        AppLanguage.IT -> "Seleziona tutto"
    }

    val selectAllMarkets: String get() = selectAll

    val clearSelection: String get() = when (language) {
        AppLanguage.TR -> "Temizle"
        AppLanguage.EN -> "Clear"
        AppLanguage.DE -> "Löschen"
        AppLanguage.ES -> "Limpiar"
        AppLanguage.FR -> "Effacer"
        AppLanguage.AR -> "مسح"
        AppLanguage.RU -> "Очистить"
        AppLanguage.IT -> "Cancella"
    }

    // Voice Shopping Add Strings
    val voiceAddShoppingTitle: String get() = when (language) {
        AppLanguage.TR -> "Sesli Ürün Ekleme"
        AppLanguage.EN -> "Voice Item Entry"
        AppLanguage.DE -> "Sprachbasierte Artikelerfassung"
        AppLanguage.ES -> "Añadir Productos por Voz"
        AppLanguage.FR -> "Ajout Vocal d'Articles"
        AppLanguage.AR -> "إضافة المنتجات بالصوت"
        AppLanguage.RU -> "Голосовое добавление товаров"
        AppLanguage.IT -> "Aggiunta Vocale Articoli"
    }

    val voiceAddShoppingDesc: String get() = when (language) {
        AppLanguage.TR -> "Mikrofona dokunup almak istediğiniz ürünleri söyleyin (Örn: '2 kilo elma, 1 paket makarna ve 3 litre süt')"
        AppLanguage.EN -> "Tap the mic and speak the items you need (e.g., '2 kg apples, 1 pack pasta and 3 liters milk')"
        AppLanguage.DE -> "Tippen Sie auf das Mikrofon und sprechen Sie die gewünschten Artikel"
        AppLanguage.ES -> "Toque el micrófono y hable los artículos que necesita"
        AppLanguage.FR -> "Appuyez sur le micro et dictez vos articles"
        AppLanguage.AR -> "اضغط على الميكروفون وتحدث بالمنتجات المطلوبة"
        AppLanguage.RU -> "Нажмите на микрофон и произнесите список покупок"
        AppLanguage.IT -> "Tocca il microfono e pronuncia gli articoli da acquistare"
    }

    val voiceListening: String get() = when (language) {
        AppLanguage.TR -> "Sizi Dinliyor..."
        AppLanguage.EN -> "Listening..."
        AppLanguage.DE -> "Hört zu..."
        AppLanguage.ES -> "Escuchando..."
        AppLanguage.FR -> "À l'écoute..."
        AppLanguage.AR -> "جاري الاستماع..."
        AppLanguage.RU -> "Слушаю..."
        AppLanguage.IT -> "In ascolto..."
    }

    val voiceTapToSpeak: String get() = when (language) {
        AppLanguage.TR -> "Konuşmak İçin Dokunun"
        AppLanguage.EN -> "Tap to Speak"
        AppLanguage.DE -> "Tippen zum Sprechen"
        AppLanguage.ES -> "Tocar para hablar"
        AppLanguage.FR -> "Appuyez pour parler"
        AppLanguage.AR -> "اضغط للتحدث"
        AppLanguage.RU -> "Нажмите, чтобы говорить"
        AppLanguage.IT -> "Tocca per parlare"
    }

    val voiceDetectedItems: String get() = when (language) {
        AppLanguage.TR -> "Algılanan Ürünler"
        AppLanguage.EN -> "Detected Items"
        AppLanguage.DE -> "Erkannte Artikel"
        AppLanguage.ES -> "Artículos Detectados"
        AppLanguage.FR -> "Articles Détectés"
        AppLanguage.AR -> "المنتجات المكتشفة"
        AppLanguage.RU -> "Обнаруженные товары"
        AppLanguage.IT -> "Articoli Rilevati"
    }

    val voiceAddAllToCart: String get() = when (language) {
        AppLanguage.TR -> "Listeye / Sepete Ekle"
        AppLanguage.EN -> "Add to Shopping List"
        AppLanguage.DE -> "Zur Liste hinzufügen"
        AppLanguage.ES -> "Añadir a la Lista"
        AppLanguage.FR -> "Ajouter à la Liste"
        AppLanguage.AR -> "إضافة إلى القائمة"
        AppLanguage.RU -> "Добавить в список"
        AppLanguage.IT -> "Aggiungi alla Lista"
    }

    val voiceExampleTip: String get() = when (language) {
        AppLanguage.TR -> "İpucu: Tek seferde birden fazla ürün söyleyebilirsiniz. Örneğin: '3 ekmek, 2 kilo domates ve 1 deterjan'."
        AppLanguage.EN -> "Tip: You can say multiple items at once. For example: '3 breads, 2 kg tomatoes and 1 detergent'."
        AppLanguage.DE -> "Tipp: Sie können mehrere Artikel auf einmal nennen."
        AppLanguage.ES -> "Consejo: Puede decir varios artículos a la vez."
        AppLanguage.FR -> "Astuce : Vous pouvez énoncer plusieurs articles à la suite."
        AppLanguage.AR -> "تلميح: يمكنك قول عدة منتجات في نفس الوقت."
        AppLanguage.RU -> "Совет: Вы можете назвать несколько товаров подряд."
        AppLanguage.IT -> "Suggerimento: Puoi pronunciare più articoli contemporaneamente."
    }

    val voiceSpeakPrompt: String get() = when (language) {
        AppLanguage.TR -> "Alışveriş listenize eklenecek ürünleri söyleyin"
        AppLanguage.EN -> "Speak items to add to shopping list"
        AppLanguage.DE -> "Sprechen Sie die gewünschten Artikel"
        AppLanguage.ES -> "Diga los artículos para la lista"
        AppLanguage.FR -> "Dictez les articles pour votre liste"
        AppLanguage.AR -> "تحدث بالمنتجات لإضافتها إلى القائمة"
        AppLanguage.RU -> "Произнесите список товаров"
        AppLanguage.IT -> "Pronuncia gli articoli da aggiungere"
    }
}


