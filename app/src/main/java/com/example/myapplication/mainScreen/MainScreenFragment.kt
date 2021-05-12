package com.example.myapplication.mainScreen

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.MainActivity.Companion.layout_num
import com.example.myapplication.R
import com.example.myapplication.database.CountriesData
import com.example.myapplication.database.CountriesDataBase
import com.example.myapplication.databinding.FragmentMainScreenBinding


class MainScreenFragment : Fragment() {

    private lateinit var staggeredGridLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val app= requireNotNull(this.activity).application
        val dataSource=CountriesDataBase.getInstance(app).countriesDao

        val mainViewModelFactory=MainViewModelFactory(dataSource)
        val viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        val binding = FragmentMainScreenBinding.inflate(inflater)

        val adapter = CountriesListAdapter(CountriesListAdapter.OnClickListener {
            viewModel.ForNav(it.id)
        })

        binding.lifecycleOwner=this

        binding.viewModel=viewModel

       /* binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // filter your list from your input
               // val temp: MutableList<CountriesData> = ArrayList()
                    /*for (d in viewModel.dbcountry.value!!) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.name.contains(s,true)) {
                            temp.add(d)
                        }
                        adapter.updalteList(temp)

                    }*/
                adapter.updateList(viewModel.filter(s))


                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })

        */
        binding.countriesList.adapter=adapter


        layout_num.observe(viewLifecycleOwner, {

            if(layout_num.value?.rem(2)  == 0) {
                staggeredGridLayoutManager = GridLayoutManager(this.context,2)
            }

            else{
                staggeredGridLayoutManager = LinearLayoutManager(context?.applicationContext , VERTICAL , false)
            }

            //staggeredGridLayoutManager.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS)
            binding.countriesList.layoutManager = staggeredGridLayoutManager
            //binding.countriesList.adapter=adapter
        })
        binding.invalidateAll()


        viewModel.navId.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSecondFragment(it))
                viewModel.navDone()
            }
        })


        return binding.root
    }

    override fun onStart() {
        super.onStart()

            requireActivity().findViewById<View>(R.id.layout_change).visibility=View.VISIBLE
            requireActivity().findViewById<View>(R.id.imageButton).visibility=View.VISIBLE
    }

}