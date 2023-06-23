package com.neko.hiepdph.toiletseries.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.neko.hiepdph.monster_voice.common.clickWithDebounce
import com.neko.hiepdph.monster_voice.common.hide
import com.neko.hiepdph.monster_voice.common.show
import com.neko.hiepdph.toiletseries.common.AppSharePreference
import com.neko.hiepdph.toiletseries.data.MonsterModel
import com.neko.hiepdph.toiletseries.databinding.LayoutItemHomeBinding

class AdapterHome(
    private val onClickItem: (MonsterModel) -> Unit,
    private val onClickLockItem: (MonsterModel, pos: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data = mutableListOf<Any>()

    companion object {
        var firstTime = true
    }

    fun setData(rawData: MutableList<MonsterModel>) {
        data.clear()
        data.addAll(rawData)
        notifyDataSetChanged()
    }


    inner class HomeViewHolder(private val binding: LayoutItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = data[position] as MonsterModel
            binding.imvMonster.setImageResource(item.image)
//            binding.tag.text = itemView.context.getString(item.content)

            if (firstTime && position == 0) {
//                binding.icHand.show()
                val anim = ScaleAnimation(
                    1.0f,
                    0.6f,
                    1.0f,
                    0.6f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                anim.duration = 600
                anim.repeatCount = Animation.INFINITE
                anim.repeatMode = Animation.REVERSE
//                binding.icHand.startAnimation(anim)
            } else {
//                binding.icHand.clearAnimation()
//                binding.icHand.hide()

            }
            if (checkLock(position)) {
                binding.containerLock.show()
            } else {
                binding.containerLock.hide()
            }
            binding.imvMonster.clickWithDebounce {
                firstTime = false
                onClickItem(item)
            }
            binding.containerLock.clickWithDebounce {
                firstTime = false
                onClickLockItem(item, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == "ads") {
            0
        } else {
            1
        }
    }

//    inner class InfoNativeAdsViewHolder(val binding: ItemHomeNativeAdsBinding) :
//        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
//            0 -> {
//                val binding = ItemHomeNativeAdsBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false
//                )
//                InfoNativeAdsViewHolder(binding)
//            }

            1 -> {
                val binding: LayoutItemHomeBinding = LayoutItemHomeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HomeViewHolder(binding)
            }

            else -> {
                val binding: LayoutItemHomeBinding = LayoutItemHomeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HomeViewHolder(binding)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                (holder as HomeViewHolder).bind(position)

            }

            else -> {

            }
        }
    }

    private fun checkLock(position: Int): Boolean {
        val dataLock = AppSharePreference.INSTANCE.getListUnlockPos(mutableListOf()).toMutableList()
        return position in dataLock
    }

    private fun checkVideoPlayed(position: Int):Boolean{
        val dataPlayed = AppSharePreference.INSTANCE.getListVideoPlayed(mutableListOf()).toMutableList()
        return position in dataPlayed
    }

    fun reloadData(position: Int) {
        notifyItemChanged(position)
    }
}