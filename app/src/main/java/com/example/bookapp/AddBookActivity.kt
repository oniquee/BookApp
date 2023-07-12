package com.example.bookapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookapp.databinding.ActivityAddBookBinding
import com.example.bookapp.firebaserealtimedb.BookFirebaseRealtimeDBModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        settingFirebaseRealtimeDB()

        settingSaveButton()
    }

    private fun settingFirebaseRealtimeDB() {
        database = FirebaseDatabase.getInstance().getReference("Books")
    }

    private fun settingSaveButton() {
        binding.apply {
            btnAddBook.setOnClickListener {
                savedata()
            }
        }
    }

    private fun savedata() {
        binding.apply {
            val bookTitle = etJudulBuku.text.toString()
            val authorName = etNamaPenulis.text.toString()
            val publicationYear = etTahunTerbit.text.toString()
            val category = etKategori.text.toString()
            val bookCoverURL = etUrlCoverBuku.text.toString()


            if (authorName.isEmpty()) {
                binding.etNamaPenulis.error = "Nama Penulis tidak boleh kosong"
            }

            if (publicationYear.isEmpty()) {
                binding.etTahunTerbit.error = "Tahun terbit tidak boleh kosong"
            }

            if (category.isEmpty()) {
                binding.etKategori.error = "Kategori tidak boleh kosong"
            }

            if (bookCoverURL.isEmpty()) {
                binding.etUrlCoverBuku.error = "URL Cover Buku tidak boleh kosong"
            }

            if (bookTitle.isNotEmpty() && authorName.isNotEmpty() && publicationYear.isNotEmpty() && category.isNotEmpty() && bookCoverURL.isNotEmpty()) {
                val book = BookFirebaseRealtimeDBModel(
                    bookTitle,
                    authorName,
                    publicationYear,
                    category,
                    bookCoverURL
                )

                database.child(bookTitle).setValue(book).addOnSuccessListener {
                    etJudulBuku.text.clear()
                    etNamaPenulis.text.clear()
                    etTahunTerbit.text.clear()
                    etKategori.text.clear()
                    etUrlCoverBuku.text.clear()

                    Toast.makeText(
                        this@AddBookActivity,
                        "Data berhasil disimpan!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                    .addOnCanceledListener {
                        Toast.makeText(this@AddBookActivity, "Batal disimpan!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@AddBookActivity,
                            "Data Gagal disimpan!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }

        }
    }
}