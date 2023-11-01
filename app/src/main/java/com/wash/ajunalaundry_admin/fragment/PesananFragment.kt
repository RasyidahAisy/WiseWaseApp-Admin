package com.wash.ajunalaundry_admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.ajunalaundry_admin.adapter.PesananAdapter
import com.wash.ajunalaundry_admin.model.PesananModels
import com.wash.arjunalaundry_admin.databinding.FragmentPesananBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PesananFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PesananFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentPesananBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater)

        val query = FirebaseFirestore.getInstance().collection("ListPesanan").whereNotEqualTo("orderStatus","Selesai")
        val db = FirebaseFirestore.getInstance()
        val option = FirestoreRecyclerOptions.Builder<PesananModels>()
            .setQuery(query, PesananModels::class.java)
            .build()

        val adapter = PesananAdapter(option,childFragmentManager)
        binding.rvPesanan.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL ,false)
        binding.rvPesanan.adapter = adapter
        adapter.startListening()
        return binding.root
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PesananFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): PesananFragment {
            val fragment = PesananFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}