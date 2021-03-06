package com.nk.tokitelist.fragments.list.checkedKitems

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.data.viewmodel.SharedViewModel
import com.nk.tokitelist.fragments.list.kitems.SwipeToCheck
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_checked_list.view.*


class CheckedKitemsListFragment : Fragment() {

    private val mToKiteViewModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: CheckedListAdapter by lazy { CheckedListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checked_list, container, false)
        initRecyclerView(view)
        setUpListeners()
        setupToolbar(view)
        return view
    }

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.toolbar_checked_kitems_list as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpListeners() {
        mToKiteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            triggerDataSetting(data)
        })
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.checkedRecyclerView
        recyclerView.itemAnimator = SlideInLeftAnimator().apply {
            addDuration = 300
            removeDuration = 300
            changeDuration = 300
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        swipeToCheck(recyclerView)
    }

    private fun triggerDataSetting(data: List<KiteItem>?) {
        val filteredData = filterData(data)
        adapter.setData(filteredData)
        mSharedViewModel.checkIfDataIsEmpty(filteredData)
        (activity as AppCompatActivity).supportActionBar?.title = "Checked Kitems"

    }

    private fun filterData(data: List<KiteItem>?): List<KiteItem>? {
        return data?.filter { it.checked }
    }

    private fun swipeToCheck(recyclerView: RecyclerView){
        val swipeToCheckCallback = object : SwipeToCheck(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val kitemToUncheck = adapter.dataList[viewHolder.adapterPosition]
                mToKiteViewModel.uncheckKitem(kitemToUncheck)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                Toast.makeText(
                    requireContext(),
                    "Unchecked ${kitemToUncheck.name}!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToCheckCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checked_list_fragment_menu, menu)
    }

}





