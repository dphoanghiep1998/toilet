package com.neko.hiepdph.toiletseries.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neko.hiepdph.monster_voice.common.RewardAdsEnum
import com.neko.hiepdph.monster_voice.common.clickWithDebounce
import com.neko.hiepdph.monster_voice.common.pushEvent
import com.neko.hiepdph.monster_voice.common.showRewardAds
import com.neko.hiepdph.toiletseries.R
import com.neko.hiepdph.toiletseries.common.AppSharePreference
import com.neko.hiepdph.toiletseries.common.DialogConfirm
import com.neko.hiepdph.toiletseries.databinding.FragmentHomeBinding
import com.neko.hiepdph.toiletseries.viewmodel.AppViewModel


class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<AppViewModel>()
    private var adapterHome: AdapterHome? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeBackPressCallBack()
        initView()
    }

    private fun initView() {
        initButton()
        initRecyclerView()
    }

    private fun initButton() {
        binding.btnBack.clickWithDebounce {
            findNavController().popBackStack()
        }
    }

    private fun initRecyclerView() {
        adapterHome = AdapterHome(onClickItem = {
            viewModel.setCurrentModel(it)
            findNavController().popBackStack()
        }, onClickLockItem = { monsterModel, pos ->
            val dialogConfirm = DialogConfirm(requireContext(), onPressPositive = {
                showRewardAds(actionSuccess = {
                    requireContext().pushEvent("click_home_unlock")
                    val dataPos = AppSharePreference.INSTANCE.getListUnlockPos(mutableListOf())
                        .toMutableList()
                    dataPos.remove(pos)
                    AppSharePreference.INSTANCE.saveListUnlockPos(dataPos)
                    adapterHome?.reloadData(pos)
                    viewModel.setCurrentModel(monsterModel)
                    findNavController().popBackStack()
                }, actionFailed = {
                    Toast.makeText(
                        requireContext(), getString(R.string.require_internet), Toast.LENGTH_SHORT
                    ).show()
                }, RewardAdsEnum.LOCK)
            })
            dialogConfirm.show()
        })
        adapterHome?.setData(viewModel.data)
        val gridlayoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
//        gridlayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return if (position != 3) {
//                    1
//                } else {
//                    gridlayoutManager.spanCount
//                }
//            }
//
//        }
        binding.rcvHome.layoutManager = gridlayoutManager
        binding.rcvHome.adapter = adapterHome
    }

    private fun changeBackPressCallBack() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

}