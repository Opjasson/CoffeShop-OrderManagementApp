package com.example.cafecornerapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafecornerapp.Adapter.CardProductlistAdapter
import com.example.cafecornerapp.R
import com.example.cafecornerapp.ViewModel.ProductViewModel
import com.example.cafecornerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel = ProductViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       initSideBar()

        initShowProduct()
    }

    private fun initShowProduct () {
        var kategori : String = ""

        binding.makananBtn.setOnClickListener {
            kategori = "makanan"
            viewModel.getProductByKategori(kategori)
        }

        binding.minumanBtn.setOnClickListener {
            kategori = "minuman"
            viewModel.getProductByKategori(kategori)
        }

        val productAdapter = CardProductlistAdapter(mutableListOf())
        binding.rvMenu.adapter = productAdapter



        viewModel.productKategoriResult.observe(this) {
                list ->
            binding.rvMenu.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.loadMenu.visibility = View.GONE

            productAdapter.updateData(list.toMutableList())
        }
    }

    private fun initSideBar () {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout

        val navigationView = binding.navigationView

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.menu_manageProduct -> {
                    startActivity(Intent(this, ManageProductActivity::class.java))
                }
//                R.id.menu_logout -> {
////                    logout()
//                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}