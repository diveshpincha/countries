package com.example.myapplication.specific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.database.CountriesDataBase
import com.example.myapplication.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private lateinit var binding:FragmentSecondBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        requireActivity().findViewById<View>(R.id.layout_change).visibility=View.GONE
        requireActivity().findViewById<View>(R.id.imageButton).visibility=View.GONE

        val countryId =arguments?.let{ SecondFragmentArgs.fromBundle(it) }

        val reqId = countryId?.key

        val application= requireNotNull(this.activity).application

        val dataSource= CountriesDataBase.getInstance(application).countriesDao

        val viewModelFactory= reqId?.let { SecondViewModelFactory(dataSource, it) }

        val viewModel = viewModelFactory?.let {
            ViewModelProvider(this,
                    it
            ).get(SecondViewModel::class.java)
        }

        binding = FragmentSecondBinding.inflate(inflater)

        binding.lifecycleOwner=this

        binding.data= viewModel


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<View>(R.id.layout_change).visibility=View.GONE
        requireActivity().findViewById<View>(R.id.imageButton).visibility=View.GONE
    }

}