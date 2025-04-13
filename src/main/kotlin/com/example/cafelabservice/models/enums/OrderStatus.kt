package com.example.cafelabservice.models.enums

enum class OrderStatus(val nome: String) {
    CREATED("CRIADO"),
    PENDING("PENDENTE"),
    IN_PROGRESS("EM ANDAMENTO"),
    PAYMENT_SUCCESSFUL("PAGAMENTO EFETUADO"),
    DELIVERED("ENTREGUE"),
    SUSPENDED("SUSPENSO"),
    CANCELLED("CANCELADO"),
    ACTIVE("ATIVO");

    companion object {
        val validStatuses = listOf(ACTIVE, PAYMENT_SUCCESSFUL, DELIVERED, SUSPENDED, CANCELLED, IN_PROGRESS)

        
    }
}