package com.wash.ajunalaundry_admin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.UpdatePesananActivity
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
            binding.textView10.text = model.orderStatus
            val collectionReference = FirebaseFirestore.getInstance().collection("JenisPesanan")
            val documentReference = model.orderType?.let { collectionReference.document(it) }
            val task = documentReference?.get()
            task?.addOnSuccessListener { document ->
                // Get the document data
                binding.txtpesanan.text = document.getString("Jenis")
                // Do something with the document data
            }

            when (model.orderStatus){
                "Pesanan Dibuat" -> binding.cvListItemHome.setOnClickListener {
                    model.DocID?.let { it1 -> TerimaPesananOrderFragement.newInstance(it1,"").show(fM,"SeeOrder") }
                }

                else -> {
                    binding.cvListItemHome.setOnClickListener {
                        val intent = Intent(itemView.context,UpdatePesananActivity::class.java)
                        intent.putExtra("DocID",model.DocID)
                        ContextCompat.startActivity(itemView.context,intent,null)}
                    }
            }

        }
    }


}