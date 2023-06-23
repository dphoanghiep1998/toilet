package com.neko.hiepdph.toiletseries.main.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_ALWAYS
import com.neko.hiepdph.monster_voice.common.RewardAdsEnum
import com.neko.hiepdph.monster_voice.common.clickWithDebounce
import com.neko.hiepdph.monster_voice.common.pushEvent
import com.neko.hiepdph.monster_voice.common.showRewardAds
import com.neko.hiepdph.toiletseries.R
import com.neko.hiepdph.toiletseries.common.AppSharePreference
import com.neko.hiepdph.toiletseries.common.DialogConfirm
import com.neko.hiepdph.toiletseries.databinding.FragmentVideoBinding
import com.neko.hiepdph.toiletseries.viewmodel.AppViewModel
import kotlin.system.exitProcess

class FragmentVideo : Fragment() {
    private lateinit var binding: FragmentVideoBinding
    private val viewModel by activityViewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayer()
        initButton()
        changeBackPressCallBack()
    }

    private fun initButton() {
        requireActivity().findViewById<ImageView>(R.id.play_again).clickWithDebounce {
            viewModel.reloadVideo()
        }
        requireActivity().findViewById<ImageView>(R.id.next).clickWithDebounce {
            val id = viewModel.getCurrentModel().id
            if (id < 39) {
                viewModel.setCurrentModel(viewModel.data[id + 1])
                if(checkLock(viewModel.getCurrentModel().id)){
                    val dialogConfirm = DialogConfirm(requireContext(), onPressPositive = {
                        showRewardAds(actionSuccess = {
                            requireContext().pushEvent("click_home_unlock")
                            val dataPos = AppSharePreference.INSTANCE.getListUnlockPos(mutableListOf())
                                .toMutableList()
                            dataPos.remove(viewModel.getCurrentModel().id)
                            AppSharePreference.INSTANCE.saveListUnlockPos(dataPos)
                            findNavController().popBackStack()
                        }, actionFailed = {
                            Toast.makeText(
                                requireContext(), getString(R.string.require_internet), Toast.LENGTH_SHORT
                            ).show()
                        }, RewardAdsEnum.LOCK)
                    })
                    dialogConfirm.show()
                }
                viewModel.playAudio(MediaItem.fromUri(viewModel.getCurrentModel().content), onEnd = {
                    Log.d("TAG", "setupPlayer: ")
                })
            }
        }
        requireActivity().findViewById<ImageView>(R.id.list_video).clickWithDebounce {
            viewModel.resetPlayer()
            findNavController().navigate(R.id.fragmentHome)
        }
    }
    private fun checkLock(position: Int): Boolean {
        val dataLock = AppSharePreference.INSTANCE.getListUnlockPos(mutableListOf()).toMutableList()
        return position in dataLock
    }

    private fun setupPlayer() {
        val mPlayer = ExoPlayer.Builder(requireContext()).setSeekForwardIncrementMs(15000).build()
        viewModel.setupPlayer(mPlayer)

        binding.playerView.apply {
            setShowBuffering(SHOW_BUFFERING_ALWAYS)
            player = viewModel.getPlayer()
            keepScreenOn = true

        }
        try {
            viewModel.playAudio(MediaItem.fromUri(viewModel.getCurrentModel().content), onEnd = {
                Log.d("TAG", "setupPlayer: ")
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetPlayer()
    }

    private fun changeBackPressCallBack() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialogConfirm = DialogConfirm(requireContext(), onPressPositive = {
                    requireActivity().finishActivity(0)
                    exitProcess(-1)
                }, true)
                dialogConfirm.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
}