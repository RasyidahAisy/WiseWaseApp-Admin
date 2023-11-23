package com.wash.ajunalaundry_admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wash.ajunalaundry_admin.fragment.PesananFragment
import com.wash.ajunalaundry_admin.viewpager.MainPagerActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp
import com.wash.ajunalaundry_admin.fragment.RiwayatFragment
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MainPagerActivity

    private val mOnNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener{ item ->
        when (item.itemId) {
            R.id.pesanan -> {
                binding.vpMain.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.riwayat -> {
                binding.vpMain.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.penjualan -> {
                binding.vpMain.currentItem = 2
                return@OnItemSelectedListener true
            }
            R.id.pengguna -> {
                binding.vpMain.currentItem = 3
                return@OnItemSelectedListener true
            }
            else -> {binding.vpMain.currentItem = 0
                return@OnItemSelectedListener true}
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        myAdapter = MainPagerActivity(supportFragmentManager,lifecycle)

        // add Fragments in your ViewPagerFragmentAdapter class

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(PesananFragment())
        myAdapter.addFragment(RiwayatFragment())

        binding.vpMain.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.vpMain.adapter = myAdapter

        binding.btnNavigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener)



        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.btnNavigation.menu.findItem(R.id.pesanan).isChecked = true
                    1 -> binding.btnNavigation.menu.findItem(R.id.riwayat).isChecked = true
                    2 -> binding.btnNavigation.menu.findItem(R.id.penjualan).isChecked = true
                    3 -> binding.btnNavigation.menu.findItem(R.id.pengguna).isChecked = true
                }

            }
        })

    }
}