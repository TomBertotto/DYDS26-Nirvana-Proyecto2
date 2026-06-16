import edu.dyds.countries.domain.entity.Country
import java.time.LocalDate

data class Trip(
    val id: String,
    val country: Country,
    val startDate: LocalDate,
    val endDate: LocalDate
)