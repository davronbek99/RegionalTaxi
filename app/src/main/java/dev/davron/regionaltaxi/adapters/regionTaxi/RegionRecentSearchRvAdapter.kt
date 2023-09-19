package dev.davron.regionaltaxi.adapters.regionTaxi

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxi.databinding.ItemInterCityHistoryBinding
import dev.davron.regionaltaxi.models.regionTaxi.RecentSearchTaxiModel


class RegionRecentSearchRvAdapter(
    val list: ArrayList<RecentSearchTaxiModel>,
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RegionRecentSearchRvAdapter.Vh>() {

    inner class Vh(val itemBinding: ItemInterCityHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(model: RecentSearchTaxiModel, position: Int) {
            itemBinding.fromToTv.text = model.fromDistrict + " → " + model.toDistrict
            if (position == list.lastIndex) {
                itemBinding.bottomLine.visibility = View.INVISIBLE
            }
            itemBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(model, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemInterCityHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(model: RecentSearchTaxiModel, position: Int)
    }
}