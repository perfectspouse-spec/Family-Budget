package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ocr.ReceiptOcrService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Aile Bütçesi", appName)
  }

  @Test
  fun `receipt OCR presets are valid and non empty`() {
    val presets = ReceiptOcrService.getPresetReceipts()
    assertTrue("Preset receipts should not be empty", presets.isNotEmpty())
    presets.forEach { preset ->
      assertTrue("Merchant name should not be blank", preset.merchantName.isNotBlank())
      assertTrue("Total amount should be positive", preset.totalAmount > 0.0)
      assertTrue("Receipt date should follow YYYY-MM-DD", preset.receiptDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
      assertTrue("Items list should not be empty", preset.items.isNotEmpty())
    }
  }

  @Test
  fun `notification helper creates notification channel without crash`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    com.example.data.local.NotificationHelper.createNotificationChannel(context)
    com.example.data.local.NotificationHelper.showApproachingBudgetNotification(
      context = context,
      currentSpent = 42000.0,
      limit = 50000.0,
      percent = 84,
      remainingAmount = 8000.0,
      dailySpendable = 800.0,
      daysLeft = 10
    )
    com.example.data.local.NotificationHelper.showExceededBudgetNotification(
      context = context,
      currentSpent = 53000.0,
      limit = 50000.0,
      overspentAmount = 3000.0
    )
  }

  @Test
  fun `family member expense calculation and filtering logic`() {
    val member1 = com.example.data.model.FamilyMember(
      id = 1L,
      name = "Ahmet",
      countryCode = "+90",
      phoneNumber = "555 123 45 67",
      role = "Aile Reisi",
      monthlySalary = 35000.0,
      additionalIncome = 5000.0,
      isApproved = true,
      isCurrentUser = true
    )
    val member2 = com.example.data.model.FamilyMember(
      id = 2L,
      name = "Elif",
      countryCode = "+90",
      phoneNumber = "555 987 65 43",
      role = "Eş",
      monthlySalary = 28000.0,
      additionalIncome = 0.0,
      isApproved = true,
      isCurrentUser = false
    )

    val receipts = listOf(
      com.example.data.model.ExpenseReceipt(id = 1, merchantName = "Migros", receiptDate = "2026-08-10", category = "Market", totalAmount = 1250.0, familyMemberId = 1L, familyMemberName = "Ahmet"),
      com.example.data.model.ExpenseReceipt(id = 2, merchantName = "Zara", receiptDate = "2026-08-12", category = "Giyim", totalAmount = 2400.0, familyMemberId = 2L, familyMemberName = "Elif"),
      com.example.data.model.ExpenseReceipt(id = 3, merchantName = "Shell", receiptDate = "2026-08-15", category = "Ulaşım / Yakıt", totalAmount = 1800.0, familyMemberId = 1L, familyMemberName = "Ahmet")
    )

    val ahmetReceipts = receipts.filter { it.familyMemberId == member1.id }
    val elifReceipts = receipts.filter { it.familyMemberId == member2.id }

    assertEquals(2, ahmetReceipts.size)
    assertEquals(3050.0, ahmetReceipts.sumOf { it.totalAmount }, 0.01)
    assertEquals(1, elifReceipts.size)
    assertEquals(2400.0, elifReceipts.sumOf { it.totalAmount }, 0.01)
  }

  @Test
  fun `room database market product mapping and strict store isolation`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = androidx.room.Room.inMemoryDatabaseBuilder(context, com.example.data.local.AppDatabase::class.java).build()
    val dao = db.marketProductDao()

    // Seed default mappings
    val mappings = com.example.data.local.MarketCatalogSeeder.getDefaultMappings()
    dao.insertAll(mappings)

    // Verify count > 0
    val totalCount = dao.getCount()
    assertTrue("Default mappings should be seeded", totalCount > 50)

    // Test specific scenario: "Mis Laktozsuz Süt" MUST ONLY exist in ŞOK, NEVER in Migros or BİM
    val misLaktozsuzSok = dao.findProductPriceInMarket("sok", "mis laktozsuz sut", "Mis Laktozsuz Süt")
    val misLaktozsuzMigros = dao.findProductPriceInMarket("migros", "mis laktozsuz sut", "Mis Laktozsuz Süt")
    val misLaktozsuzBim = dao.findProductPriceInMarket("bim", "mis laktozsuz sut", "Mis Laktozsuz Süt")

    org.junit.Assert.assertNotNull("Mis Laktozsuz Süt must exist in ŞOK", misLaktozsuzSok)
    assertEquals("sok", misLaktozsuzSok?.marketId)
    assertEquals(37.00, misLaktozsuzSok?.price ?: 0.0, 0.01)
    org.junit.Assert.assertNull("Mis Laktozsuz Süt must NOT exist in Migros", misLaktozsuzMigros)
    org.junit.Assert.assertNull("Mis Laktozsuz Süt must NOT exist in BİM", misLaktozsuzBim)

    // Test valid market IDs for Mis brand
    val misStores = dao.getValidMarketIdsForProduct("mis laktozsuz sut", "Mis Laktozsuz Süt")
    assertEquals(listOf("sok"), misStores)

    // Test getMarketOffersForProduct helper logic
    val offersForMis = com.example.data.model.ShoppingCatalog.getMarketOffersForProduct(
      productName = "Mis Laktozsuz Süt",
      category = "Süt & Kahvaltılık",
      quantity = 1.0,
      unit = "Lt"
    )
    assertEquals(1, offersForMis.size)
    assertEquals("sok", offersForMis.first().marketId)
    assertEquals("ŞOK", offersForMis.first().marketName)

    db.close()
  }
}
