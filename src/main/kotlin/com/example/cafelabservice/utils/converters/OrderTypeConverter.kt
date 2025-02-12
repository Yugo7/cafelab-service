import com.example.cafelabservice.models.enums.OrderType
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class OrderTypeConverter : AttributeConverter<OrderType, String> {
    override fun convertToDatabaseColumn(attribute: OrderType?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): OrderType? {
        return dbData?.let { OrderType.valueOf(it) }
    }
}