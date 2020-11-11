package com.nk.tokitelist.fragments.session
// TODO: 10.11.20 delete spot 
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nk.tokitelist.R
import com.nk.tokitelist.Tools
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Spot
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import kotlinx.android.synthetic.main.fragment_edit_session.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class EditSessionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var freshlyAddedSpotName: String = ""
    private val mToKiteModel: ToKiteViewModel by viewModels()
    private lateinit var thisView: View
    private val args by navArgs<EditSessionFragmentArgs>()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_edit_session, container, false)

        setHasOptionsMenu(true)

        thisView.spinner_spots!!.onItemSelectedListener = this

        thisView?.add_session_floatingActionButton.setOnClickListener{
            saveCurrentSession()
            val nc =findNavController()
            nc.navigate(R.id.action_editSessionFragment_to_sessionsOverviewFragment)
        }

        mToKiteModel.getAllSpotNames.observe(viewLifecycleOwner, Observer { data ->
            updateSpotsSpinner(data)
        })

        // TODO: 11.11.20 what happens when coming from ADDSESSION button?
        Tools.setSpinnerByValue(thisView.spinner_spots,args.currentSession.spot.toString())
        Tools.setDateSetterToDate(thisView.datePickerForSession, args.currentSession.date)

        return thisView

    }


    private fun updateSpotsSpinner(data: Array<String>) {
        val aa = activity?.let {
            ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_spinner_item,
                    data!!
            )
        }
        aa!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        thisView.spinner_spots!!.adapter = aa
        if (data.isNotEmpty()){
            freeSaveSessionButton()
        }else{
            hideSaveSessionButton()
        }
        Tools.setSpinnerByValue(thisView.spinner_spots,freshlyAddedSpotName)
    }

    private fun freeSaveSessionButton() {
        thisView.add_session_floatingActionButton.isVisible = true;
    }

    private fun hideSaveSessionButton() {
        thisView.add_session_floatingActionButton.isVisible = false;
    }

    private fun saveCurrentSession() {
        GlobalScope.launch {
            val date = getDate()
            val spot = getSpot()
            val kiteSession = KiteSession(0, date!!, spot!!)
            mToKiteModel.insertKiteSession(kiteSession)
        }
        Toast.makeText(
                requireContext(),
                "writing ${getSpotName()}-session to the data-base.",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun getDate(): Date? {
        val date = Tools.getDateFromDatePicker(thisView.datePickerForSession);
        return date;
    }

    private suspend fun getSpot(): Spot? {
        val spotName = getSpotName();
        return mToKiteModel.getSpotByName(spotName);
    }

    private fun getSpotName():String{
        return thisView.spinner_spots.selectedItem.toString();
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.add_new_spot -> addNewSpot()
            R.id.delete_spot -> deleteCurrentSpot()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteCurrentSpot() {
        val spot = thisView.spinner_spots.selectedItem.toString()
        mToKiteModel.deleteSpot(spot)
    }

    private fun addNewSpot() {
        val builder = AlertDialog.Builder(
                requireContext()
        )
        builder.setTitle("new spot")
        val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_add_spot, view as ViewGroup?, false)
        val input = viewInflated.findViewById(R.id.pop_up_new_spot) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(
                android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            var nameForNewSpot = input.text.toString()
            mToKiteModel.addNewSpot(nameForNewSpot)
            freshlyAddedSpotName = nameForNewSpot
            freeSaveSessionButton()
        }
        builder.setNegativeButton(
                android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_session_fragment_menu, menu)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}