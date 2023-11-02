package com.wash.ajunalaundry_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.model.ProgressModels
import com.wash.arjunalaundry_admin.databinding.ActivityUpdatePesananBinding
import java.text.SimpleDateFormat
import java.util.Date

class UpdatePesananActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatePesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar3.visibility = View.VISIBLE;
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)




        val db = FirebaseFirestore.getInstance()
        val getData = db.collection("ListPesanan").document(intent.getStringExtra("DocID").toString())
        getData.get().addOnSuccessListener {
            binding.textView14.text = it.id
            binding.Status.text = it.getString("orderStatus")
            val timestamp: Timestamp = it.get("orderDate") as Timestamp
            val date = timestamp.toDate()
            val getDataAlamat = db.collection("User").document(it.getString("uid").toString())
            getDataAlamat.get().addOnSuccessListener { user ->
                binding.textView18.text = user.getString("address")
            }

            val getDetailPesanan = db.collection("JenisPesanan").document(it.getString("orderType").toString())
            getDetailPesanan.get().addOnSuccessListener { orderType ->
                binding.textView19.text = orderType.getString("Jenis")
                binding.textView20.text = orderType.get("HargaPerKilo").toString()

                binding.progressBar3.visibility = View.GONE;
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            binding.textView17.text = formatter(date)

            binding.button4.setOnClickListener {
                when (binding.button4.text){
                    "Proses" -> {Log.d("UpdatePesanan", "onCreate: ubah ke pengeringan")
                        val progressModels = ProgressModels(Timestamp.now(),"Pesanan Diterima")
                        val terimapesanan = db.collection("ListPesanan").document(binding.textView14.text.toString()).collection("progress")
                        terimapesanan.add(progressModels).addOnSuccessListener {
                            Toast.makeText(this, "Anda Memproses Pesanan", Toast.LENGTH_SHORT).show()
                        }
                        val ubahStatus = db.collection("ListPesanan").document(binding.textView14.text.toString())
                        ubahStatus.update("orderStatus","Pesanan Diproses")
                    }
                }
            }

            when (binding.Status.text.toString()){
                "Pesanan Diterima" -> binding.button4.text = "Proses"
                "Pesanan Diproses" -> binding.button4.text = "Selesai"
            }

        }
    }

    fun formatter(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date)
    }
}