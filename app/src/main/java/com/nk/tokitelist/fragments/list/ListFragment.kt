package com.nk.tokitelist.fragments.list

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.fragments.edit.SharedViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {

    private val mToKiteViewModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }
    var sessionSeason:Season = Season.always


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.recyclerView
        recyclerView.itemAnimator = SlideInLeftAnimator().apply {
            addDuration = 300
            removeDuration = 300
            changeDuration = 300
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        swipeToCheck(recyclerView)

        mToKiteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            triggerDataSetting(data)
        })

        mSharedViewModel.dbIsEmpty.observe(viewLifecycleOwner, Observer {
            showEmptyDB(mSharedViewModel.dbIsEmpty.value)
        })

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val s = sharedPref?.getString("sessionSeason", "always")
        this.sessionSeason = mSharedViewModel.strToSeason(s)!!

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
        when (sessionSeason){
            Season.summer -> stylizeForSummerSession()
            Season.winter -> stylizeForWinterSession()
            else -> stylizeForGeneral()
        }
    }

    private fun stylizeForGeneral() {
        (activity as AppCompatActivity).supportActionBar?.title = "Kitems"
        colorActionBar(R.color.allYear)
    }
    private fun stylizeForWinterSession() {
        (activity as AppCompatActivity).supportActionBar?.title = "winter session"
        colorActionBar(R.color.cold)
    }
    private fun stylizeForSummerSession() {
        (activity as AppCompatActivity).supportActionBar?.title = "summer session"
        colorActionBar(R.color.hot)
    }

    private fun colorActionBar(rcolor:Int) {
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(), rcolor
                )
            )
        )
    }

    private fun filterData(data: List<KiteItem>?): List<KiteItem>? {
        val setSeason = sessionSeason
        val fdata = data?.filter { !it.checked && (setSeason == Season.always || (it.season==Season.always || setSeason==it.season)) }
        return fdata
    }

    private fun swipeToCheck(recyclerView: RecyclerView){
        val swipeToCheckCallback = object :SwipeToCheck(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToCheck = adapter.dataList[viewHolder.adapterPosition]
                mToKiteViewModel.checkKitem(itemToCheck)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

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
        setSeason(Season.winter)
        triggerDataSetting(mToKiteViewModel.getAllData.value)

    }

    private fun filterOnSummer() {
        setSeason(Season.summer)
        triggerDataSetting(mToKiteViewModel.getAllData.value)
    }


    fun setSeason(s: Season) {
        this.sessionSeason = s
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("sessionSeason", s.toString())
            apply()
        }

    }
}





