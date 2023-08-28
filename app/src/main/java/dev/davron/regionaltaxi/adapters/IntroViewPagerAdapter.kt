package dev.davron.regionaltaxi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxi.databinding.LayoutIntroScreenBinding
import dev.davron.regionaltaxi.models.ScreenItem

class IntroViewPagerAdapter(var list: List<ScreenItem>) :
    RecyclerView.Adapter<IntroViewPagerAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: LayoutIntroScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: ScreenItem, position: Int) {
            binding.tvIntroTitle.text = model.title
            binding.ivIntro.setImageResource(model.screenImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutIntroScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }
}