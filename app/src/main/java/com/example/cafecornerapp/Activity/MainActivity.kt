package com.example.cafecornerapp.Activity

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafecornerapp.Adapter.CardProductlistAdapter
import com.example.cafecornerapp.DataStore.TransaksiPreference
import com.example.cafecornerapp.DataStore.UserPreference
import com.example.cafecornerapp.R
import com.example.cafecornerapp.ViewModel.AuthViewModel
import com.example.cafecornerapp.ViewModel.CartViewModel
import com.example.cafecornerapp.ViewModel.ProductViewModel
import com.example.cafecornerapp.ViewModel.TransaksiViewModel
import com.example.cafecornerapp.ViewModel.UserViewModel
import com.example.cafecornerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    private val userViewModel = UserViewModel()


    private val viewModelTransaksi = TransaksiViewModel()

    private val viewModelCart = CartViewModel()
    private val viewModel = ProductViewModel()

    private val prefRepo = TransaksiPreference(this)

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initSideBar()
        initShowProduct()
        initShowOffer()
    }

    private fun initShowProduct () {
//      get user login

        userViewModel.getUserByUid()

        userViewModel.userLogin.observe(this) { user ->
            user?.let {
                binding.usernameTxt.text = "Good morning, " + user?.username

                val headerView = binding.navigationView.getHeaderView(0)
                headerView.findViewById<TextView>(R.id.tvNameHeader).text = user?.username
                headerView.findViewById<TextView>(R.id.tvEmailHeader).text = user?.email
            }



            //      create transaksi
            binding.transaksiBtn.setOnClickListener {

                viewModelTransaksi.createTransaksi(
                    user?.documentId.toString(),
                    0,
                    "",
                    ""
                )

                viewModelTransaksi.createStatus.observe(this) {
                    documentId ->
                    lifecycleScope.launch {
                        prefRepo.saveTransactionId(documentId)
                    }
                    Toast.makeText(this, "Silahkan melanjutkan pesanan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        var kategori : String = "makanan"
        viewModel.getProductByKategori(kategori)

//        Button kategori makanan handle
        binding.makananBtn.setOnClickListener {
            kategori = "makanan"
            binding.makananBtn.backgroundTintList = ColorStateList
                .valueOf(ContextCompat
                    .getColor(this, R.color.btnon))

            binding.makananBtn.setTextColor(
                ContextCompat
                    .getColor(this, R.color.white)
            )

            binding.minumanBtn.backgroundTintList = ColorStateList
                .valueOf(ContextCompat
                    .getColor(this, R.color.btnoff))

            binding.minumanBtn.setTextColor(
                ContextCompat
                    .getColor(this, R.color.black)
            )
            viewModel.getProductByKategori(kategori)
        }

//        Button kategori minuman handle
        binding.minumanBtn.setOnClickListener {
            kategori = "minuman"
            binding.makananBtn.backgroundTintList = ColorStateList
                .valueOf(ContextCompat
                    .getColor(this, R.color.btnoff))

            binding.makananBtn.setTextColor(
                ContextCompat
                    .getColor(this, R.color.black)
            )

            binding.minumanBtn.backgroundTintList = ColorStateList
                .valueOf(ContextCompat
                    .getColor(this, R.color.btnon))

            binding.minumanBtn.setTextColor(
                ContextCompat
                    .getColor(this, R.color.white)
            )

            viewModel.getProductByKategori(kategori)
        }


        val productAdapter = CardProductlistAdapter(
//            menerima data dari adapter
            onAddToCart = {
                    productId ->

                userViewModel.userLogin.observe(this) { user ->
                    lifecycleScope.launch {
                        val transaksiId = prefRepo.getTransactionId().first()

                        if (transaksiId.isNullOrBlank()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Buat Transaksi Dulu!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        viewModelCart.addOrUpdateCart(
                            userId = user!!.documentId!!,
                            transaksiId = transaksiId,
                            productId = productId,
                            qty = 1
                        )

                        delay(500)
                        startActivity(Intent(this@MainActivity, CartActivity::class.java))
                    }

                }

            },
            mutableListOf()
        )

        binding.rvMenu.adapter = productAdapter

        viewModel.productKategoriResult.observe(this@MainActivity) {
                list ->
            binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL,false
            )
            binding.loadMenu.visibility = View.GONE

            productAdapter.updateData(list.toMutableList())
        }


    }

    private fun initShowOffer () {
        val productAdapter = CardProductlistAdapter(
            onAddToCart = {
                    productId ->

                userViewModel.userLogin.observe(this) { user ->
                    lifecycleScope.launch {
                        val transaksiId = prefRepo.getTransactionId().first()

                        if (transaksiId.isNullOrBlank()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Buat Transaksi Dulu!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        viewModelCart.addOrUpdateCart(
                            userId = user!!.documentId!!,
                            transaksiId = transaksiId,
                            productId = productId,
                            qty = 1
                        )

                        delay(500)
                        startActivity(Intent(this@MainActivity, CartActivity::class.java))
                    }

                }

            },
            mutableListOf()
        )
        binding.rvSpecial.adapter = productAdapter


        viewModel.getProductOffer()

        viewModel.productOfferResult.observe(this) {
                list ->
            Log.d("OFFER", list.toString())
            binding.rvSpecial.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.loadOffer.visibility = View.GONE

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
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                }
                R.id.menu_history -> {
                    startActivity(Intent(this, HistoryTransaksiActivity::class.java))
                }
                R.id.menu_logout -> {
                    showLogoutDialog()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun showLogoutDialog() {
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Apakah kamu yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                authViewModel.logout()
                lifecycleScope.launch {
                    userPreference.deleteUserId()
                }
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}