package com.nk.tokitelist.fragments.list.kitems


import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.nk.tokitelist.data.models.SortMode
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.data.viewmodel.SharedViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_kitems_list.view.*


class KitemsListFragment : Fragment() {

    private lateinit var inflatedView: View
    private var sortMode: SortMode = SortMode.ALPHAASC
    private val mToKiteViewModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapterKitems: KitemsListAdapter by lazy { KitemsListAdapter() }
    var sessionSeason:Season = Season.always


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        setUpListeners(inflater, container)
        setHasOptionsMenu(true)
        restoreSeasonSelection()
        setUpToolbar()
        stylizeForGeneral()
        return inflatedView
    }

    private fun restoreSeasonSelection() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val s = sharedPref?.getString("sessionSeason", "always")
        this.sessionSeason = mSharedViewModel.strToSeason(s)!!
    }

    private fun setUpToolbar() {
        val toolbar: Toolbar = inflatedView.toolbar_kitems_list as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
    }

    private fun setUpListeners(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        inflatedView = inflater.inflate(R.layout.fragment_kitems_list, container, false)
        setUpRecyclerView()

        inflatedView.button_logSession.setOnClickListener {
            saveKiteSession()
        }

        inflatedView.floatingActionButton.setOnClickListener {
            val nc = findNavController()
            nc.navigate(R.id.action_listFragment_to_addFragment)
        }

        inflatedView.floatingActionButton_toCheckedList.setOnClickListener {
            val nc = findNavController()
            nc.navigate(R.id.action_listFragment_to_checkedListFragment)
        }

        mToKiteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            triggerDataSetting(data)
        })

        mSharedViewModel.dbIsEmpty.observe(viewLifecycleOwner, Observer {
            showEmptyDB(mSharedViewModel.dbIsEmpty.value)
        })
    }

    private fun setUpRecyclerView() {
        val recyclerView = inflatedView.kitemsListRecyclerView
        recyclerView.itemAnimator = SlideInLeftAnimator().apply {
            addDuration = 200
            removeDuration = 200
            changeDuration = 200
        }
        recyclerView.adapter = adapterKitems
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        swipeToCheck(recyclerView)

        inflatedView.swipeRefreshList.setOnRefreshListener {
            resortList(recyclerView)
            inflatedView.swipeRefreshList.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }


    private fun saveKiteSession() {
        findNavController().navigate(R.id.action_listFragment_to_editSessionFragment)
    }

    private fun resortList(recyclerView: RecyclerView?) {
        val values = enumValues<SortMode>()
        sortMode = values[(values.indexOf(sortMode) + 1) % values.size]
        Toast.makeText(
                requireContext(),
                "Sort-mode: ${mSharedViewModel.sortModeToText(sortMode)}.",
                Toast.LENGTH_LONG
        ).show()
        triggerDataSetting(mToKiteViewModel.getAllData.value)
    }

    private fun triggerDataSetting(data: List<KiteItem>?) {
        var filteredData = filterData(data)
        filteredData = sortData(filteredData)
        adapterKitems.setData(filteredData)
        mSharedViewModel.checkIfDataIsEmpty(filteredData)
        when (sessionSeason){
            Season.summer -> stylizeForSummerSession()
            Season.winter -> stylizeForWinterSession()
            else -> stylizeForGeneral()
        }
    }

    private fun sortData(data: List<KiteItem>?): List<KiteItem>? {
        return when (sortMode){
            SortMode.ALPHAASC -> sortOnAlpha(data, 1)
            SortMode.ALPHADESC -> sortOnAlpha(data, -1)
            SortMode.INDEXASC -> sortOnIndex(data, 1)
            SortMode.INDEXDESC -> sortOnIndex(data, -1)
            SortMode.SEASONASC -> sortOnSeason(data, 1)
            SortMode.SEASONDESC -> sortOnSeason(data, -1)
        }
    }

    private fun sortOnSeason(data: List<KiteItem>?, dir: Int): List<KiteItem>? {
        var ans = data?.sortedBy { it.season }
        if (dir==-1) {
            ans = ans?.reversed()
        }
        return ans
    }

    private fun sortOnIndex(data: List<KiteItem>?, dir: Int): List<KiteItem>? {
        var ans = data?.sortedBy { it.addedIdx }
        if (dir==-1) {
            ans = ans?.reversed()
        }
        return ans
    }

    private fun sortOnAlpha(data: List<KiteItem>?, dir: Int): List<KiteItem>? {
        var ans = data?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
        if (dir==-1) {
            ans = ans?.reversed()

        }
        return ans
    }


    private fun stylizeForGeneral() {
        (activity as AppCompatActivity).supportActionBar?.title = "Kitems"
        colorActionBar(R.color.primary)
    }
    private fun stylizeForWinterSession() {
        (activity as AppCompatActivity).supportActionBar?.title = "winter session"
        colorActionBar(R.color.cold)
    }
    private fun stylizeForSummerSession() {
        (activity as AppCompatActivity).supportActionBar?.title = "summer session"
        colorActionBar(R.color.hot)
    }

    private fun colorActionBar(rcolor: Int) {
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
        val swipeToCheckCallback = object : SwipeToCheck(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToCheck = adapterKitems.dataList[viewHolder.adapterPosition]
                mToKiteViewModel.checkKitem(itemToCheck)
                adapterKitems.notifyItemRemoved(viewHolder.adapterPosition)

                Toast.makeText(requireContext(), "${itemToCheck.name} ✓", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToCheckCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showEmptyDB(dbIsEmpty: Boolean?) {
        if (dbIsEmpty==null || dbIsEmpty) {
            view?.imageView_ready?.visibility = View.VISIBLE
            view?.button_logSession?.visibility = View.VISIBLE
        }else{
            view?.imageView_ready?.visibility = View.INVISIBLE
            view?.button_logSession?.visibility = View.INVISIBLE
        }
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





