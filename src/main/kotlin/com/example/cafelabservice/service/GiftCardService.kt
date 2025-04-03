package com.example.cafelabservice.service

import com.example.cafelabservice.entity.GiftCard
import com.example.cafelabservice.entity.User
import com.example.cafelabservice.repositories.GiftCardRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class GiftCardService(
    private val stripeService: StripeService,
    private val giftCardRepository: GiftCardRepository,
    private val userService: UserService
) {
    fun createGiftCard(amount: Long): GiftCard {
        val giftCard = GiftCard(
            code = generateShortAlphanumericCode(),
            amount = amount,
            balance = amount
        )
        return giftCardRepository.save(giftCard)
    }

    fun redeemGiftCard(code: String, user: User): String {
        val giftCard = giftCardRepository.findByCode(code)
            ?: throw Exception("Invalid gift card code.")

        if (giftCard.isRedeemed) throw Exception("Gift card already redeemed.")
        if (giftCard.expirationDate.isBefore(LocalDateTime.now())) throw Exception("Gift card expired.")

        val customerId = user.stripeId ?: stripeService.createStripeCustomer(user).id.also {
            val newUser = user.copy(stripeId = it)
            userService.updateUser(user.id, newUser)
        }

        val customer = stripeService.addBalanceToCustomer(customerId, giftCard.balance)

        giftCard.isRedeemed = true
        giftCard.redeemedBy = user
        giftCardRepository.save(giftCard)

        return "Gift card redeemed successfully for ${customer.email}."
    }

    fun getGiftCardBalance(code: String): Long {
        val giftCard = giftCardRepository.findByCode(code)
            ?: throw Exception("Invalid gift card code.")

        return giftCard.balance
    }

    fun generateShortAlphanumericCode(): String {
        var code: String
        do {
            val uuid = UUID.randomUUID().toString().replace("-", "")
            code = uuid.substring(0, 8).uppercase()
        } while (giftCardRepository.existsByCode(code))
        return code
    }
}