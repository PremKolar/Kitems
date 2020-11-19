package com.nk.tokitelist.fragments.session

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nk.tokitelist.R
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_edit_session.view.*
import kotlinx.android.synthetic.main.fragment_session_overview.view.*

class SessionOverviewFragment : Fragment() {

    private val mToKiteModel: ToKiteViewModel by viewModels()
    private lateinit var thisView: View
    private val adapter: SessionListAdapter by lazy { SessionListAdapter() }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_session_overview, container, false)
        val recyclerView = thisView.sessionListRecyclerView
        recyclerView.itemAnimator = SlideInLeftAnimator().apply {
            addDuration = 300
            removeDuration = 300
            changeDuration = 300
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        mToKiteModel.getAllSession.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
        })

        val toolbar: Toolbar = thisView.toolbar_sessions_overview!!
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Session"


        return thisView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.session_overview_fragment_menu, menu)
    }
}