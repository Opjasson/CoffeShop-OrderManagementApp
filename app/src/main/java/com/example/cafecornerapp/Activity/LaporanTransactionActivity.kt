package com.example.cafecornerapp.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cafecornerapp.Helper.ConvertDateTime
import com.example.cafecornerapp.R
import com.example.cafecornerapp.ViewModel.TransaksiViewModel
import com.example.cafecornerapp.databinding.ActivityLaporanTransactionBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.compareTo

class LaporanTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporanTransactionBinding
    private lateinit var drawerLayout: DrawerLayout

    private val localIDR = ConvertDateTime()
    private val viewModel = TransaksiViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaporanTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etDateRange = binding.etDateRange

        etDateRange.setOnClickListener {
            showDateRangePicker(etDateRange)
        }

        initSideBar()
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

    private fun showDateRangePicker(editText: TextInputEditText) {

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pilih Rentang Tanggal")
            .build()

        picker.show(supportFragmentManager, "DATE_RANGE_PICKER")

        picker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

            val start = formatter.format(Date(startDate))
            val end = formatter.format(Date(endDate))

            editText.setText("$start  -  $end")

            val table = binding.tableHarga

//            viewModelRekap.loadData(start, end)

            viewModel.loadTransaksiLaporan(start, end)
//            viewModel.transaksiLaporan.observe(this){
//                data ->
//                Log.d("DATALAPORAN", data.toString())
//            }
            viewModel.transaksiLaporan.observe(this){
                    list ->

                // 1️⃣ HAPUS SEMUA ROW DATA (kecuali header)
                val childCount = table.childCount
                if (childCount > 1) {
                    table.removeViews(1, childCount - 1)
                }
                Log.d("LISTBARANGMASUK", list.toString())

//                binding.cetakBtn.setOnClickListener {
//                    generatePdf(this, list)
//                }


                var totalKeseluruhan : Long = 0
                list.forEachIndexed { index2, prod ->

                    prod.cartItems.forEachIndexed {
                        index, item ->
                        val row = TableRow(this)
                        row.setBackgroundColor(Color.WHITE)

                        val subtotal = item.jumlah * item.harga
                        totalKeseluruhan += subtotal

                        row.addView(createCell((index + 1).toString(), Gravity.CENTER))
                        row.addView(createCell(item.createdAt.toString(), Gravity.CENTER))
                        row.addView(createCell(item.nama.toString(), Gravity.CENTER))
                        row.addView(createCell(item.jumlah.toString(), Gravity.START))
                        row.addView(createCell(item.harga.toString(), Gravity.START))
                        row.addView(createCell(
                            subtotal.toString(), Gravity.END)
                        )

                        table.addView(row)
                    }
                }
                binding.totalKeseluruhanTxt.text = "Total Akhir : " + localIDR.formatRupiah(totalKeseluruhan).toString()

            }


        }
    }

    private fun createCell(text: String, gravity: Int): TextView {
        return TextView(this).apply {
            this.text = text
            this.gravity = gravity
            setPadding(8, 8, 8, 8)
            setBackgroundResource(android.R.drawable.editbox_background)
        }
    }
}