package com.wash.ajunalaundry_admin.dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.model.JenisPesananModels
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.FragmentEditJenisPesananBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DOCUMENTID = "param1"
private const val ARG_JENIS = "param2"
private const val ARG_ETA = "param3"
private const val ARG_HARGA = "param4"
private const val ARG_KONSIDI = "param5"
/**
 * A simple [Fragment] subclass.
 * Use the [EditJenisPesanan.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditJenisPesanan : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var documentID: String? = null
    private var Jenis: String? = null
    private var ETA: String? = null
    private var HargaPerKilo: String? = null
    private var jenisPenggunakan:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentID = it.getString(ARG_DOCUMENTID)
            Jenis = it.getString(ARG_JENIS)
            ETA = it.getString(ARG_ETA)
            HargaPerKilo = it.getString(ARG_HARGA)
            jenisPenggunakan = it.getString(ARG_KONSIDI)
        }

    }
    lateinit var binding: FragmentEditJenisPesananBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentEditJenisPesananBinding.inflate(inflater)
        val db = FirebaseFirestore.getInstance()
        binding.editTextText2.setText(Jenis.toString())
        binding.editTextText3.setText(HargaPerKilo.toString())
        binding.editTextText4.setText(ETA.toString())

        binding.button5.setOnClickListener {
            dismiss()
        }



        binding.button.setOnClickListener {
            if (binding.editTextText4.text.isEmpty()){
                return@setOnClickListener
            }

            if (binding.editTextText3.text.isEmpty()){
                return@setOnClickListener
            }

            if (binding.editTextText2.text.isEmpty()){
                return@setOnClickListener
            }

            when (jenisPenggunakan){
                "Ubah" ->{
                    val updates = hashMapOf<String,Any>(
                        "eta" to binding.editTextText4.text.toString(),
                        "jenis" to binding.editTextText2.text.toString(),
                        "hargaPerKilo" to Integer.parseInt(binding.editTextText3.text.toString())
                    )
                    val dbJenisPesanan=  db.collection("JenisPesanan").document(documentID!!)
                    dbJenisPesanan.update(updates).addOnSuccessListener {
                        Log.d("EditJenisPesanan", "onCreateView: Berhasil Menyimpan")
                        Toast.makeText(requireContext(), "Berhasil Mengubah Data Data", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }.addOnFailureListener {
                        Log.e("EditJenisPesanan", "onCreateView: Gagal Menyimpan", it)
                    }
                }

                "Tambah" -> {
                    val data = JenisPesananModels(binding.editTextText4.text.toString(), Integer.parseInt(binding.editTextText3.text.toString()),binding.editTextText2.text.toString())
                    db.collection("JenisPesanan").add(data).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                }
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditJenisPesanan.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(docid: String, jenis: String,eta:String,harga:String,kondisi:String) =
            EditJenisPesanan().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOCUMENTID, docid)
                    putString(ARG_JENIS, jenis)
                    putString(ARG_ETA,eta)
                    putString(ARG_HARGA,harga)
                    putString(ARG_KONSIDI,kondisi)
                }
            }
    }

}