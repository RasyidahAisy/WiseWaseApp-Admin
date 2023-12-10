package com.wash.ajunalaundry_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.model.ProgressModels
import com.wash.arjunalaundry_admin.databinding.ActivityUpdatePesananBinding
import java.text.SimpleDateFormat
import java.util.Date

class UpdatePesananActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatePesananBinding
    var harga:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar3.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        binding.textView20.text = intent.getStringExtra("HargaPerKilo")

        val db = FirebaseFirestore.getInstance()
        val getData =
            db.collection("ListPesanan").document(intent.getStringExtra("DocID").toString())
        getData.get().addOnSuccessListener {
            binding.textView14.text = it.id
            binding.Status.text = it.getString("orderStatus")
            if (it.get("orderTotal") == null){
                binding.textView26.text = "Total"
            }else{
                binding.editTextText.setText(it.get("qty").toString())
                binding.editTextText.keyListener = null
                binding.textView26.text = it.get("orderTotal").toString().toDouble().toString()
            }
            if (it.getBoolean("useVoucher") == true){
                binding.textView32.text = "Iya"
            }else{
                binding.textView32.text = "Tidak"
            }

            val timestamp: Timestamp = it.get("orderDate") as Timestamp
            val date = timestamp.toDate()
            if (it.get("qty") == 0) {
                binding.editTextText.setText(it.get("qty").toString())
                binding.editTextText.isEnabled = false
            } else {
                binding.editTextText.isEnabled = true
            }
            val getDataAlamat = db.collection("User").document(it.getString("uid").toString())
            getDataAlamat.get().addOnSuccessListener { user ->
                binding.textView18.text = user.getString("address")
            }

            val getDetailPesanan =
                db.collection("JenisPesanan").document(it.getString("orderType").toString())
            getDetailPesanan.get().addOnSuccessListener { orderType ->
                binding.textView19.text = orderType.getString("jenis")
                binding.textView20.text = orderType.get("hargaPerKilo").toString()
                harga = Integer.parseInt(orderType.get("hargaPerKilo").toString())

                binding.progressBar3.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            binding.textView17.text = formatter(date)

            binding.button4.setOnClickListener {
                when (binding.button4.text) {
                    "Proses" -> {
                        Log.d("UpdatePesanan", "onCreate: ubah ke pengeringan")
                        val progressModels = ProgressModels(Timestamp.now(), "Pesanan Diproses")
                        val terimapesanan = db.collection("ListPesanan")
                            .document(binding.textView14.text.toString()).collection("progress")
                        terimapesanan.add(progressModels).addOnSuccessListener {
                            Toast.makeText(this, "Anda Memproses Pesanan", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        val ubahStatus = db.collection("ListPesanan")
                            .document(binding.textView14.text.toString())
                        ubahStatus.update("orderStatus", "Pesanan Diproses")


                    }

                    "Selesai" -> {

                        if (binding.editTextText.text.isEmpty() || binding.editTextText.text.equals("0")) {
                            Toast.makeText(this, "Masukkan Berat Pakaian ", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }

                        if (binding.textView32.text.equals("Iya")){
                            if (binding.editTextText.text.toString().toDouble() > 3){
                                Toast.makeText(this, "Pesanan Voucher Maksimal 3 Kg", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }
                        }

                        val progressModels = ProgressModels(Timestamp.now(), "Pesanan Selesai")
                        val terimapesanan = db.collection("ListPesanan")
                            .document(binding.textView14.text.toString()).collection("progress")
                        terimapesanan.add(progressModels).addOnSuccessListener {
                            Toast.makeText(this, "Anda Menyelesaikan Pesanan", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        val ubahStatus = db.collection("ListPesanan")
                            .document(binding.textView14.text.toString())
                        ubahStatus.update("orderStatus", "Pesanan Siap Dikirim","qty", binding.editTextText.text.toString().toDouble(),"orderTotal",binding.textView26.text.toString().toDouble())
                        finish()
                    }

                    "Antar" -> {
                        val cekStatus = db.collection("ListPesanan")
                            .document(binding.textView14.text.toString())
                        cekStatus.get().addOnSuccessListener { cekStatus ->
                            if (cekStatus.getString("paymentStatus").equals("Success")) {
                                val progressModels =
                                    ProgressModels(Timestamp.now(), "Pesanan Dikirim")
                                val terimapesanan = db.collection("ListPesanan")
                                    .document(binding.textView14.text.toString())
                                    .collection("progress")
                                terimapesanan.add(progressModels).addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Anda Menyelesaikan Pesanan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                                val ubahStatus = db.collection("ListPesanan")
                                    .document(binding.textView14.text.toString())
                                ubahStatus.update("orderStatus", "Pesanan Telah Dikirim")
                                finish()

                            } else {
                                Toast.makeText(this, "Pesanan Belum Dibayar", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    "Hapus" -> {
                        db.collection("ListPesanan").document(binding.textView14.text.toString())
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Pesanan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                            finish()}
                    }
                }
            }

            when (binding.Status.text.toString()) {
                "Pesanan Diterima" -> binding.button4.text = "Proses"
                "Pesanan Diproses" -> binding.button4.text = "Selesai"
                "Pesanan Telah Selesai" -> {binding.button4.isEnabled = false
                binding.button4.text = "Selesai"}
                "Pesanan Telah Dikirim" -> {
                    binding.button4.isEnabled = false
                    binding.button4.text = "Antar"
                }
                "Pesanan Dibatalkan" -> binding.button4.text = "Hapus"

                else -> binding.button4.text = "Antar"
            }

        }

        binding.editTextText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.editTextText.length() != 0) {
                    binding.textView26.text = "Total"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.editTextText.text.toString() == ""){
                    binding.textView26.text = "Total"
                }else{
                    binding.textView26.text = ((binding.editTextText.text.toString().toDouble()) * harga).toString()
                }

            }

        })
    }

    fun formatter(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm")
        return formatter.format(date)
    }

    fun totalHarga(qty: Double, harga: Int): Double {
        return qty * harga
    }
}
