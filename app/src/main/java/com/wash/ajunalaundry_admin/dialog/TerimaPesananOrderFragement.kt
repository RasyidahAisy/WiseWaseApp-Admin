package com.wash.ajunalaundry_admin.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.wash.ajunalaundry_admin.model.PesananModels
import com.wash.ajunalaundry_admin.model.ProgressModels
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.FragmentTerimaPesananOrderBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TerimaPesananOrderFragement.newInstance] factory method to
 * create an instance of this fragment.
 */
class TerimaPesananOrderFragement : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
    lateinit var binding: FragmentTerimaPesananOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTerimaPesananOrderBinding.inflate(inflater)
        initializeMaps()

        val mapController = binding.mapView.controller
        val startPoint = org.osmdroid.util.GeoPoint(-0.789275, 113.921327)
        mapController.setZoom(5.0)

        val db = FirebaseFirestore.getInstance()
        var geoPoint: GeoPoint
        val option = db.collection("ListPesanan").document(param1.toString())
        option.get().addOnSuccessListener { list->
            if (list.exists()){
                val getUser = list.getString("uid")?.let { db.collection("User").document(it) }
                getUser?.get()?.addOnSuccessListener {
                    if (it.exists()){
                        binding.textView6.text = it.get("address").toString()
                        binding.textView8.text = it.get("name").toString()
                        binding.textView11.text = it.get("phoneNumber").toString()


                        geoPoint = it.getGeoPoint("coordinatePoint")!!
                        val position = org.osmdroid.util.GeoPoint(geoPoint.latitude, geoPoint.longitude)

                        val startMarker = Marker(binding.mapView)
                        startMarker.position = position
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        binding.mapView.overlays.add(startMarker);
                        mapController.setZoom(15.0)
                        mapController.setCenter(position)

                        startMarker.setOnMarkerClickListener { marker, _ ->
                            when (marker) {
                                startMarker -> {Log.d("TAG", "onCreateView: ")
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("geo:<${geoPoint.latitude}>,<${geoPoint.longitude}>?q=<${geoPoint.latitude}>,<${geoPoint.longitude}>(Lokasi Pelanggan)")
                                    )
                                    startActivity(intent)
                                true}

                                else -> {false}
                            }
                        }


                    }
                }
                val getType = db.collection("JenisPesanan").document(list.getString("orderType").toString())
               getType.get().addOnSuccessListener {
                   binding.textView13.text = it.getString("jenis").toString()
               }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            dismiss();

        }

        binding.button2.setOnClickListener {
            val progressModels = ProgressModels(Timestamp.now(),"Pesanan Dibatalkan")
            val batalkanPesanan = db.collection("ListPesanan").document(param1.toString()).collection("progress")
            batalkanPesanan.add(progressModels).addOnSuccessListener {
                Toast.makeText(activity, "Anda Membatalkan Pesanan", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            val ubahStatus = db.collection("ListPesanan").document(param1.toString())
            ubahStatus.update("orderStatus","Dibatalkan")

        }

        binding.button3.setOnClickListener {
            val progressModels = ProgressModels(Timestamp.now(),"Pesanan Diterima")
            val terimapesanan = db.collection("ListPesanan").document(param1.toString()).collection("progress")
            terimapesanan.add(progressModels).addOnSuccessListener {
                Toast.makeText(activity, "Anda Menerima Pesanan", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            val ubahStatus = db.collection("ListPesanan").document(param1.toString())
            ubahStatus.update("orderStatus","Pesanan Diterima")

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
         * @return A new instance of fragment TerimaPesananOrder.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TerimaPesananOrderFragement().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initializeMaps() {
        Configuration.getInstance().userAgentValue = BuildConfig.BUILD_TYPE
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        binding.mapView.setScrollableAreaLimitDouble(BoundingBox(85.0, 180.0, -85.0, -180.0))
        binding.mapView.maxZoomLevel = 20.0
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.isHorizontalMapRepetitionEnabled = false
        binding.mapView.isVerticalMapRepetitionEnabled = false
        binding.mapView.setScrollableAreaLimitLatitude(
            MapView.getTileSystem().maxLatitude,
            MapView.getTileSystem().minLatitude, 0
        )
    }
}