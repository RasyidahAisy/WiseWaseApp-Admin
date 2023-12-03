package com.wash.ajunalaundry_admin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wash.ajunalaundry_admin.dialog.EditJenisPesanan
import com.wash.ajunalaundry_admin.model.JenisPesananModels
import com.wash.arjunalaundry_admin.databinding.ListJenisPesananBinding

class JenisPesananAdapter(option:FirestoreRecyclerOptions<JenisPesananModels>,fragmentManager: FragmentManager):
    FirestoreRecyclerAdapter<JenisPesananModels, JenisPesananAdapter.ViewHolder>(option) {
    val fM = fragmentManager

        inner class ViewHolder(val binding:ListJenisPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListJenisPesananBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: JenisPesananModels) {
        with(holder){

            binding.textView27.text = model.jenis


            binding.cvJenisPesanan.setOnLongClickListener{
                Log.d("JenisPesananAdapter", "onBindViewHolder: Tekan pada ${model.docID}")
                EditJenisPesanan.newInstance(model.docID.toString(),
                    model.jenis.toString(),model.eta.toString(),model.hargaPerKilo.toString(),"Ubah").show(fM,"Show Jenis")
                true
            }
        }
    }
}