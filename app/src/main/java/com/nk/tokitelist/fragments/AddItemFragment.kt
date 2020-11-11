package com.nk.tokitelist.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.fragments.edit.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*
import java.lang.Exception


class AddItemFragment : Fragment() {

    private var itemExistsAlready: Boolean = false
    private val mToKiteModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<AddItemFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        setHasOptionsMenu(true)

        view.spinner_season.onItemSelectedListener = mSharedViewModel.listener


        try {
            view.editText_nameOfItem.setText(args.currentKitem.name)
            view.spinner_season.setSelection(mSharedViewModel.seasonToInteger(args.currentKitem.season))
            this.itemExistsAlready = true
        }catch (e:Exception){
            this.itemExistsAlready = false
        }

        return view

    }

    private fun hideTrashcan(menu: Menu) {
        menu.getItem(0).isVisible = false
    }

    private fun showTrashcan(menu: Menu) {
        menu.getItem(0).isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
        if (itemExistsAlready){
            showTrashcan(menu)
        }else{
            hideTrashcan(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_add_kitem -> addItemToDB()
            R.id.menu_delete_kitem -> deleteItemFromDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItemFromDB() {
        val mName = editText_nameOfItem.text.toString()
        mToKiteModel.deleteKitem(mName)
        val snack =  Snackbar.make(requireView(),"Deleted '${mName}!'",Snackbar.LENGTH_LONG)
        snack.setAction("Undo"){
            mToKiteModel.insertKitem(args.currentKitem)
        }
        snack.show()
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }

    private fun addItemToDB() {
        val mName = editText_nameOfItem.text.toString()
        val mSeason = Season.valueOf(spinner_season.selectedItem.toString())
        if (!mSharedViewModel.verifyDataFromUser(mName)){
            Toast.makeText(requireContext(),"Fill all fields!",Toast.LENGTH_SHORT).show()
            return
        }
        createNewKitem(mName, mSeason)
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }

    private fun createNewKitem(
        mName: String,
        mSeason: Season
    ) {
        val newKitem = KiteItem(mName, mSeason,0, checked = false)
        mToKiteModel.insertKitem(newKitem)
        Toast.makeText(requireContext(), "Kitem added!", Toast.LENGTH_SHORT).show()
    }

}