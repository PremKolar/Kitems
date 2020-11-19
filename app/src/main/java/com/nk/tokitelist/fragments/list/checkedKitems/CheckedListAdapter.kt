package com.nk.tokitelist.fragments.list.checkedKitems
import androidx.navigation.findNavController
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.fragments.list.ListAdapter

class CheckedListAdapter: ListAdapter() {
    override fun navigateToEditKitemFragment(
        kiteItem: KiteItem,
        holder: MyViewHolder
    ) {
        val action = CheckedKitemsListFragmentDirections.actionCheckedListFragmentToAddFragment(kiteItem)
        holder.itemView.findNavController().navigate(action)
    }
}