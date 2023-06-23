package com.neko.hiepdph.toiletseries.main.onboard.child_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neko.hiepdph.toiletseries.databinding.FragmentOnboardChild3Binding

class FragmentOnboardChild3 : Fragment() {
    private lateinit var binding: FragmentOnboardChild3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardChild3Binding.inflate(inflater, container, false)
        return binding.root
    }

}