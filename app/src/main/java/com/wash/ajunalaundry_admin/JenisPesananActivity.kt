package com.wash.ajunalaundry_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.adapter.JenisPesananAdapter
import com.wash.ajunalaundry_admin.adapter.PesananAdapter
import com.wash.ajunalaundry_admin.dialog.EditJenisPesanan
import com.wash.ajunalaundry_admin.model.JenisPesananModels
import com.wash.ajunalaundry_admin.model.PesananModels
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.ActivityJenisPesananBinding

class JenisPesananActivity : AppCompatActivity() {
    lateinit var binding: ActivityJenisPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJenisPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = FirebaseFirestore.getInstance().collection("JenisPesanan").whereNotEqualTo("jenis",null)
        val db = FirebaseFirestore.getInstance()
        val option = FirestoreRecyclerOptions.Builder<JenisPesananModels>()
            .setQuery(query, JenisPesananModels::class.java)
            .build()

        val adapter = JenisPesananAdapter(option,supportFragmentManager)
        binding.rvJenisPesanan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        binding.rvJenisPesanan.adapter = adapter
        adapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.jenis_pesanan_option_icon,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.tambahPesanan -> {
                EditJenisPesanan.newInstance("", "", "", "", "Tambah")
                    .show(supportFragmentManager, "TambahPesanan")
                return true
            }

            else -> {
                false
            }
        }
    }
}