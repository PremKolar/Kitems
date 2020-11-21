package com.nk.tokitelist.fragments.edit

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.viewmodel.SharedViewModel
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.fragments.AddItemFragmentArgs
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*
import java.lang.Exception


class EditKitemFragment : Fragment() {

    private val MENU_IDX_OF_TRASH_CAN = 0
    private var itemExistsAlready: Boolean = false
    private val mToKiteModel: ToKiteViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<AddItemFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)
        setHasOptionsMenu(true)
        setupListeners(view)
        fillUIwithCurrentKitemFromArgs(view)
        setupToolbar(view)
        return view
    }

    private fun setupListeners(view: View) {
        view.spinner_season.onItemSelectedListener = mSharedViewModel.listener
    }

    private fun fillUIwithCurrentKitemFromArgs(view: View) {
        try {
            view.editText_nameOfItem.setText(args.currentKitem.name)
            view.spinner_season.setSelection(mSharedViewModel.seasonToInteger(args.currentKitem.season))
            this.itemExistsAlready = true
        } catch (e: Exception) {
            this.itemExistsAlready = false
        }
    }

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.toolbar_edit_kitem as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        (activity as AppCompatActivity).supportActionBar?.title = "Add/Edit"
    }


    private fun hideTrashcan(menu: Menu) {
        menu.getItem(MENU_IDX_OF_TRASH_CAN).isVisible = false
    }

    private fun showTrashcan(menu: Menu) {
        menu.getItem(MENU_IDX_OF_TRASH_CAN).isVisible = true
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
        val newKitem = KiteItem(mName, mSeason, MENU_IDX_OF_TRASH_CAN, checked = false)
        mToKiteModel.insertKitem(newKitem)
        Toast.makeText(requireContext(), "Kitem added!", Toast.LENGTH_SHORT).show()
    }

}