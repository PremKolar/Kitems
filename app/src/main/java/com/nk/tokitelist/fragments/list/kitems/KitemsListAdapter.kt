package com.nk.tokitelist.fragments.list.kitems

import androidx.navigation.findNavController
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.fragments.list.ListAdapter

class KitemsListAdapter: ListAdapter() {
    override fun navigateToEditKitemFragment(
        kiteItem: KiteItem,
        holder: MyViewHolder
    ) {
        val action = KitemsListFragmentDirections.actionListFragmentToAddFragment(kiteItem)
        holder.itemView.findNavController().navigate(action)
    }
}