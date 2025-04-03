package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.entity.GiftCard
import com.example.cafelabservice.service.GiftCardService
import com.example.cafelabservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/giftcard")
class GiftCardController(
    private val giftCardService: GiftCardService,
    private val userService: UserService
) {
    @PostMapping
    fun createGiftCard(
        @RequestParam amount: Long,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<GiftCard> {
        val token = authorization.removePrefix("Bearer ")
        // Use the token as needed
        val giftCard = giftCardService.createGiftCard(amount)
        return ResponseEntity.ok(giftCard)
    }

    @GetMapping("/redeem/{code}")
    fun redeemGiftCard(
        @PathVariable code: String,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<String> {
        val user = userService.getUserFromToken(authorization.removePrefix("Bearer "))
        val message = giftCardService.redeemGiftCard(code, user)
        return ResponseEntity.ok(message)
    }

    @GetMapping("/balance")
    fun getGiftCardBalance(
        @RequestParam code: String,
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<Long> {
        val balance = giftCardService.getGiftCardBalance(code)
        return ResponseEntity.ok(balance)
    }
}