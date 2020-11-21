package com.nk.tokitelist.fragments.session
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nk.tokitelist.R
import com.nk.tokitelist.Tools
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Rating
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.Spot
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import kotlinx.android.synthetic.main.fragment_edit_session.view.*
import kotlinx.android.synthetic.main.session_row_layout.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates.observable


class EditSessionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var deleteSessionButton: MenuItem? = null
    private var currentSession: KiteSession? by observable(null){ _, old: KiteSession?, new: KiteSession? ->
        onCurrentSessionChanged?.invoke(old,new)
    }
    private var onCurrentSessionChanged: ((KiteSession?, KiteSession?) -> Unit)? = null
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
        setUpListeners()
        fillUIcomponentsWithCurrentSessionFromArgs()
        setupToolbar()
        return thisView
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = thisView.toolbar_addEdit_session!!
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Edit Session"
    }

    private fun fillUIcomponentsWithCurrentSessionFromArgs() {
        currentSession = if (args.currentSession == null) {
            null
        } else {
            args.currentSession
        }

        currentSession?.let { Tools.setSpinnerByValue(thisView.spinner_spots, it.spot.toString()) }
        currentSession?.date?.let { Tools.setDateSetterToDate(thisView.datePickerForSession, it) }
        currentSession?.rating?.let { setRatingBarTo(it.rat) }
    }

    private fun setUpListeners() {
        thisView.spinner_spots!!.onItemSelectedListener = this

        thisView?.add_session_floatingActionButton.setOnClickListener {
            saveCurrentSession()
            val nc = findNavController()
            nc.navigate(R.id.action_editSessionFragment_to_sessionsOverviewFragment)
        }

        mToKiteModel.getAllSpotNames.observe(viewLifecycleOwner, Observer { data ->
            updateSpotsSpinner(data)
        })

        this.onCurrentSessionChanged = { old, new -> toggleTrashBin() }
    }

    private fun setRatingBarTo(rat: Int) {
        thisView.ratingBarInSessionEditor.rating = rat.toFloat()
    }

    private fun toggleTrashBin() {
        deleteSessionButton?.let { it.setVisible(currentSession != null)}
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
            val season = getSeason(date)
            val rating = getRating()
            var kiteSession: KiteSession?;
            if (currentSession==null){
                kiteSession = KiteSession(0, date!!, spot!!,season,rating)
                mToKiteModel.insertKiteSession(kiteSession!!)
            }else{
                kiteSession = currentSession!!
                kiteSession.date = date!!
                kiteSession.spot = spot!!
                kiteSession.season = season!!
                kiteSession.rating = rating!!
                mToKiteModel.updateKiteSession(kiteSession!!)
            }
        }
        Toast.makeText(
                requireContext(),
                "writing ${getSpotName()}-session to the data-base.",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun getRating(): Rating {
        val r = thisView.ratingBarInSessionEditor.rating.toInt()
        return Rating.fromInt(r)
    }

    private fun getSeason(date: Date?): Season {
        if (date == null) return Season.always
        if (date.month > 9 || date.month < 5) return Season.winter
        return Season.summer
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
            R.id.menu_delete_session -> deleteCurrentSession()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteCurrentSpot() {
        val spot = thisView.spinner_spots.selectedItem.toString()
        mToKiteModel.deleteSpot(spot)
    }

    private fun deleteCurrentSession() {
        if (currentSession==null) return // shouldnt be possible to get here..
        mToKiteModel.deleteSession(currentSession!!)
        currentSession = null
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
        deleteSessionButton = menu.findItem(R.id.menu_delete_session)
        toggleTrashBin()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}