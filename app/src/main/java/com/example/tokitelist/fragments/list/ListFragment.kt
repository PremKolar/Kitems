package com.example.tokitelist.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokitelist.R
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.models.Season
import com.example.tokitelist.data.viewmodel.ToKiteViewModel
import com.example.tokitelist.fragments.edit.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private var season: MutableLiveData<Season> = MutableLiveData(Season.always)
    private val mToKiteViewModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        swipeToCheck(recyclerView)

        mToKiteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            triggerDataSetting(data)
        })

        this.season.observe(viewLifecycleOwner, Observer {
            triggerDataSetting(mToKiteViewModel.getAllData.value)
        })

        mSharedViewModel.dbIsEmpty.observe(viewLifecycleOwner, Observer {
            showEmptyDB(mSharedViewModel.dbIsEmpty.value)
        })

        view.floatingActionButton.setOnClickListener{
            val nc =findNavController()
            nc.navigate(R.id.action_listFragment_to_addFragment)
        }
        // Set Menu
        setHasOptionsMenu(true)

        return view
    }

    private fun triggerDataSetting(data: List<KiteItem>?) {
        val filteredData = filterData(data)
        adapter.setData(filteredData)
        mSharedViewModel.checkIfDataIsEmpty(filteredData)
    }

    private fun filterData(data: List<KiteItem>?): List<KiteItem>? {
        val fdata = data?.filter { !it.checked && (this.season.value == Season.always || (it.season==Season.always || this.season.value==it.season)) }
        return fdata
    }

    private fun swipeToCheck(recyclerView: RecyclerView){
        val swipeToCheckCallback = object :SwipeToCheck(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToCheck = adapter.dataList[viewHolder.adapterPosition]
                mToKiteViewModel.checkKitem(itemToCheck)
                Toast.makeText(requireContext(), "${itemToCheck.name} ✓", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToCheckCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showEmptyDB(dbIsEmpty: Boolean?) {
        if (dbIsEmpty==null || dbIsEmpty) {
            view?.imageView_ready?.visibility = View.VISIBLE
            view?.textView_ready?.visibility = View.VISIBLE
        }else{
            view?.imageView_ready?.visibility = View.INVISIBLE
            view?.textView_ready?.visibility = View.INVISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_summer -> filterOnSummer()
            R.id.menu_winter -> filterOnWinter()
            R.id.menu_restart -> restart()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun restart() {
        mToKiteViewModel.restart()
    }

    private fun filterOnWinter() {
        this.season.value = Season.winter
        (activity as AppCompatActivity).supportActionBar?.title = "winter sesh"
    }

    private fun filterOnSummer() {
        this.season.value = Season.summer
        (activity as AppCompatActivity).supportActionBar?.title = "summer sesh"
    }


}





