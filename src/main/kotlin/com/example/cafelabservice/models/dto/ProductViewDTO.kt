package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.Product
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class ProductViewDTO(
    val id: Long,
    @JsonProperty("created_at")
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    @JsonProperty("nome_pt")
    val nomePt: String?,
    @JsonProperty("descricao_pt")
    val descricaoPt: String,
    val origem: String,
    val grao: String,
    val preco: Double,
    val imagem: String?,
    val secao: String,
    @JsonProperty("descricao_en")
    val descricaoEn: String?,
    @JsonProperty("nome_en")
    val nomeEn: String?,
    @JsonProperty("size_pt")
    val sizePt: String?,
    @JsonProperty("size_en")
    val sizeEn: String?
){
    companion object{
        fun fromProduct(product: Product): ProductViewDTO {
            return ProductViewDTO(
                id = product.id,
                createdAt = product.createdAt,
                nomePt = product.nomePt,
                descricaoPt = product.descricaoPt,
                origem = product.origem,
                grao = product.grao,
                preco = product.preco,
                imagem = product.imagem,
                secao = product.secao.name,
                descricaoEn = product.descricaoEn,
                nomeEn = product.nomeEn,
                sizePt = product.sizePt,
                sizeEn = product.sizeEn
            )
        }
    }
}
