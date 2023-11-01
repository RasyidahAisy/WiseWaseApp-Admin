package com.wash.ajunalaundry_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wash.ajunalaundry_admin.dialog.TerimaPesananOrderFragement
import com.wash.ajunalaundry_admin.model.PesananModels
import com.wash.arjunalaundry_admin.databinding.FragmentPesananBinding
import com.wash.arjunalaundry_admin.databinding.ListItemHomeBinding
import com.wash.arjunalaundry_admin.databinding.ListPesananItemBinding


class PesananAdapter(options: FirestoreRecyclerOptions<PesananModels>,fragmentManager: FragmentManager):
    FirestoreRecyclerAdapter<PesananModels, PesananAdapter.ViewHolder>(options) {
    class ViewHolder(val binding: ListItemHomeBinding): RecyclerView.ViewHolder(binding.root)

    val fM = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
 return ViewHolder(ListItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: PesananModels) {
        with(holder){
            binding.txtpesanan.text = model.DocID
            binding.textView10.text = model.orderStatus

            binding.cvListItemHome.setOnClickListener {
                model.DocID?.let { it1 -> TerimaPesananOrderFragement.newInstance(it1,"").show(fM,"SeeOrder") }
            }
        }
    }


}