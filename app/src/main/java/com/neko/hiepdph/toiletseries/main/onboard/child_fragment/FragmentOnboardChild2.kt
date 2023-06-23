package com.neko.hiepdph.toiletseries.main.onboard.child_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neko.hiepdph.toiletseries.databinding.FragmentOnboardChild2Binding

class FragmentOnboardChild2 :Fragment() {
    private lateinit var binding: FragmentOnboardChild2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardChild2Binding.inflate(inflater, container, false)
        return binding.root
    }
}