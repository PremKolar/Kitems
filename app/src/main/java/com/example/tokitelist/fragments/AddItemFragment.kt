package com.example.tokitelist.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tokitelist.R
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.models.Season
import com.example.tokitelist.data.viewmodel.ToKiteViewModel
import com.example.tokitelist.fragments.edit.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*


class AddItemFragment : Fragment() {

    private val mToKiteModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        setHasOptionsMenu(true)

        view.spinner_season.onItemSelectedListener = mSharedViewModel.listener

        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add){
            addItemToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addItemToDB() {
        val mName = editText_nameOfItem.text.toString()
        val mSeason = Season.valueOf(spinner_season.selectedItem.toString())
        if (!mSharedViewModel.verifyDataFromUser(mName)){
            Toast.makeText(requireContext(),"Fill all fields!",Toast.LENGTH_SHORT).show()
            return
        }
        val newKitem = KiteItem(0,mName,mSeason)
        mToKiteModel.insertData(newKitem)
        Toast.makeText(requireContext(),"Kitem added!",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }




}