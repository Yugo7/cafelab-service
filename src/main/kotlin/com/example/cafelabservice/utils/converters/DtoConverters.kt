package com.example.cafelabservice.utils.converters

import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.models.dto.ProductViewDTO

fun Product.toProductViewDTO(): ProductViewDTO {
    return ProductViewDTO(
        id = this.id,
        createdAt = this.createdAt,
        nomePt = this.nomePt,
        descricaoPt = this.descricaoPt,
        origem = this.origem,
        grao = this.grao,
        preco = this.preco,
        imagem = this.imagem,
        secao = this.secao.name,
        descricaoEn = this.descricaoEn,
        nomeEn = this.nomeEn,
        sizePt = this.sizePt,
        sizeEn = this.sizeEn
    )
}