import com.example.cafelabservice.models.dto.ProductViewDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.sql.Timestamp

class ProductViewDTOTest {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `test deserialization of ProductViewDTO`() {
        val json = """
            {
                "id": 1,
                "created_at": "2023-10-01T12:00:00Z",
                "nome_pt": "Produto",
                "descricao_pt": "Descrição",
                "origem": "Brasil",
                "grao": "Arábica",
                "preco": 10.0,
                "imagem": "imagem.jpg",
                "secao": "CAFES",
                "descricao_en": "Description",
                "nome_en": "Product",
                "price_id": "price_123",
                "size_pt": "Pequeno",
                "size_en": "Small",
                "is_active": true
            }
        """.trimIndent()

        val productViewDTO: ProductViewDTO = objectMapper.readValue(json)

        assertEquals(1L, productViewDTO.id)
        assertEquals("2023-10-01 13:00:00.0", productViewDTO.createdAt.toString())
        assertEquals("Produto", productViewDTO.nomePt)
        assertEquals("Descrição", productViewDTO.descricaoPt)
        assertEquals("Brasil", productViewDTO.origem)
        assertEquals("Arábica", productViewDTO.grao)
        assertEquals(10.0, productViewDTO.preco)
        assertEquals("imagem.jpg", productViewDTO.imagem)
        assertEquals("CAFES", productViewDTO.secao)
        assertEquals("Description", productViewDTO.descricaoEn)
        assertEquals("Product", productViewDTO.nomeEn)
        assertEquals("price_123", productViewDTO.priceId)
        assertEquals("Pequeno", productViewDTO.sizePt)
        assertEquals("Small", productViewDTO.sizeEn)
        assertEquals(true, productViewDTO.isActive)
    }

    @Test
        fun `test serialization of ProductViewDTO`() {
            val productViewDTO = ProductViewDTO(
                id = 1L,
                createdAt = Timestamp.valueOf("2023-10-01 13:00:00"),
                nomePt = "Produto",
                descricaoPt = "Descrição",
                origem = "Brasil",
                grao = "Arábica",
                preco = 10.0,
                imagem = "imagem.jpg",
                secao = "CAFES",
                descricaoEn = "Description",
                nomeEn = "Product",
                priceId = "price_123",
                sizePt = "Pequeno",
                sizeEn = "Small",
                isActive = true
            )

            val json = objectMapper.writeValueAsString(productViewDTO)
            val expectedJson = """
                {
                    "id":1,
                    "created_at":"1696161600000",
                    "nome_pt":"Produto",
                    "descricao_pt":"Descrição",
                    "origem":"Brasil",
                    "grao":"Arábica",
                    "preco":10.0,
                    "imagem":"imagem.jpg",
                    "secao":"CAFES",
                    "descricao_en":"Description",
                    "nome_en":"Product",
                    "price_id":"price_123",
                    "size_pt":"Pequeno",
                    "size_en":"Small",
                    "is_active":true
                }
            """.trimIndent()

            assertEquals(expectedJson, json)
        }
}