package dev.davron.regionaltaxi.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxi.R
import dev.davron.regionaltaxi.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {
    private lateinit var binding: FragmentPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setOnClickListener()
    }

    private fun init() {

    }

    private fun setOnClickListener() {
        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.to_sms_code)
        }
    }
}