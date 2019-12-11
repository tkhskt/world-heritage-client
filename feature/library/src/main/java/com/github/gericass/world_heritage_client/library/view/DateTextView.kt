package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.github.gericass.world_heritage_client.common.dp
import com.github.gericass.world_heritage_client.library.R
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DateTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val dateText: TextView

    init {
        inflate(context, R.layout.library_view_date_text, this)
        dateText = findViewById(R.id.date_text)
        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
    }

    @ModelProp
    fun setDate(date: Date) {
        val zone = ZoneId.systemDefault()
        val historyDate = Instant.ofEpochMilli(date.time)
            .atZone(zone)
            .toLocalDate()
        val now = LocalDate.now(zone)
        val period = Period.between(historyDate, now)

        val thisYearFormat = DateTimeFormatter.ofPattern("MM月dd日")
        val oldYearFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        dateText.text = when {
            period.days == 0 -> "今日"
            period.days == 1 -> "昨日"
            period.days in 2..6 ->
                DateTimeFormatter.ofPattern("EEEE", Locale.JAPANESE).format(historyDate)
            period.years == 0 -> historyDate.format(thisYearFormat)
            else -> historyDate.format(oldYearFormat)
        }
    }
}