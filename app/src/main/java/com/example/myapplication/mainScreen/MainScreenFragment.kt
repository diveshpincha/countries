package com.example.myapplication.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.database.CountriesDataBase
import com.example.myapplication.databinding.FragmentMainScreenBinding



class MainScreenFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val app= requireNotNull(this.activity).application
        val dataSource=CountriesDataBase.getInstance(app).countriesDao

        val mainViewModelFactory=MainViewModelFactory(dataSource)
        val viewModel =
            ViewModelProvider(this,mainViewModelFactory).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        val binding = FragmentMainScreenBinding.inflate(inflater)

        binding.lifecycleOwner=this

        binding.viewModel=viewModel

        binding.countriesList.adapter=CountriesListAdapter(CountriesListAdapter.OnClickListener{
            viewModel.ForNav(it.id)
        })

        viewModel.navId.observe(viewLifecycleOwner, Observer {
            if (it != null){
                this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSecondFragment(it))
                viewModel.navDone()
            }
        })

        val staggeredGridLayoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        //staggeredGridLayoutManager.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS)

        binding.countriesList.setLayoutManager(staggeredGridLayoutManager)

        return binding.root
    }

}