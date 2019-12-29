package com.github.gericass.world_heritage_client.common.spinner

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.gericass.world_heritage_client.common.R
import com.github.gericass.world_heritage_client.common.vo.SpinnerItem

class VideoSpinnerAdapter(
    context: Context,
    private val items: List<SpinnerItem>
) : ArrayAdapter<SpinnerItem>(context, R.layout.common_view_spinner_item, items) {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SpinnerItem? = items[position]

    override fun getItemId(position: Int) = position.toLong()


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            text = items[position].title
        }
    }

}