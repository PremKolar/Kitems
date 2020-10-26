package com.example.tokitelist.fragments.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tokitelist.MainActivity
import com.example.tokitelist.R
import com.example.tokitelist.data.viewmodel.ToKiteViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private val mToKiteViewModel: ToKiteViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val activity = getActivity() as MainActivity
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

//        recyclerView.setOnLongClickListener {
//            AlertDialog.Builder(activity)
//                .setTitle("REMOVE ITEM")
//                .setMessage(R.string.removeItem)
//                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
//                    adapter.deleteItem(activity,this)
//                })
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show()
//            true}



        mToKiteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->adapter.setData(data) })

        view.floatingActionButton.setOnClickListener{
            val nc =findNavController()
            nc.navigate(R.id.action_listFragment_to_addFragment)
        }
        // Set Menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

}





