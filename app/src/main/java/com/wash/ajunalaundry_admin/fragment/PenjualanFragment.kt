package com.wash.ajunalaundry_admin.fragment


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.FragmentPenjualanBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PenjualanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PenjualanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentPenjualanBinding
    var pieArraylist: MutableList<PieEntry> = mutableListOf()
    var pieArraylist2: MutableList<PieEntry> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPenjualanBinding.inflate(inflater)
        val db = FirebaseFirestore.getInstance()
        val warna = ArrayList<Int>()

        warna.add(ContextCompat.getColor(requireContext(), R.color.biru))
        warna.add(ContextCompat.getColor(requireContext(), R.color.navy))
        warna.add(ContextCompat.getColor(requireContext(), R.color.white))


        db.collection("JenisPesanan").get()
            .addOnSuccessListener { documents ->

                for (document in documents) {

                    db.collection("ListPesanan")
                        .whereEqualTo("orderType", document.id)
                        .get()
                        .addOnSuccessListener { listDocument ->
                            val count = listDocument.size()
                            val name = document.get("jenis").toString()
                            pieArraylist.add(PieEntry(count.toFloat(), name))

                            val pieDataSet = PieDataSet(pieArraylist,name).apply {
                                colors = ColorTemplate.createColors(ColorTemplate.LIBERTY_COLORS)
                            }
                            val pieData = PieData(pieDataSet)

                            pieDataSet.valueTextSize = 16f
                            binding.chartView.data = pieData
                            binding.chartView.invalidate()

                        }

                }

                // Update pie chart here

            }
        return binding.root
    }

    fun getData(){
        pieArraylist.add(PieEntry(2f,"Cuci Kering"))
        pieArraylist.add(PieEntry(3f,"Cuci Basah"))
        pieArraylist.add(PieEntry(4f,"Cuci Kering"))
    }







    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PenjualanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PenjualanFragment().apply{
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}