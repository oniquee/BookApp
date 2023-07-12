package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookapp.databinding.ActivityEditDetailBookBinding
import com.example.bookapp.firebaserealtimedb.BookFirebaseRealtimeDBModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditDetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDetailBookBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDetailBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setData()
        settingFirebaseRealtimeDB()
        settingEditButton()
    }

    private fun setData() {
        if (intent.extras != null) {
            val bookTitle = intent.getStringExtra("bookTitle")!!
            val authorName = intent.getStringExtra("authorName")!!
            val publicationYear = intent.getStringExtra("publicationYear")!!
            val category = intent.getStringExtra("category")!!
            val bookCoverURL = intent.getStringExtra("bookCoverURL")!!

            binding.etEditJudulBuku.text = Editable.Factory.getInstance().newEditable(bookTitle)
            binding.etEditNamaPenulis.text = Editable.Factory.getInstance().newEditable(authorName)
            binding.etEditTahunTerbit.text =
                Editable.Factory.getInstance().newEditable(publicationYear)
            binding.etEditKategori.text = Editable.Factory.getInstance().newEditable(category)
            binding.etEditUrlCover.text = Editable.Factory.getInstance().newEditable(bookCoverURL)
        }
    }

    private fun settingFirebaseRealtimeDB() {
        database = FirebaseDatabase.getInstance().getReference("Books")
    }

    private fun settingEditButton() {
        binding.apply {
            btnSave.setOnClickListener {
                editData()
            }
        }
    }

    private fun editData() {
        val previousBookTitle = intent.getStringExtra("bookTitle")!!
        val bookTitle = binding.etEditJudulBuku.text.toString()
        val authorName = binding.etEditNamaPenulis.text.toString()
        val publicationYear = binding.etEditTahunTerbit.text.toString()
        val category = binding.etEditKategori.text.toString()
        val bookCoverURL = binding.etEditUrlCover.text.toString()

        if (bookTitle.isEmpty()) {
            binding.etEditJudulBuku.error = "Judul Buku tidak boleh kosong"
        }

        if (authorName.isEmpty()) {
            binding.etEditNamaPenulis.error = "Nama Penulis tidak boleh kosong"
        }

        if (publicationYear.isEmpty()) {
            binding.etEditTahunTerbit.error = "Tahun terbit tidak boleh kosong"
        }

        if (category.isEmpty()) {
            binding.etEditKategori.error = "Kategori tidak boleh kosong"
        }

        if (bookCoverURL.isEmpty()) {
            binding.etEditUrlCover.error = "URL Cover Buku tidak boleh kosong"
        }

        if (bookTitle.isNotEmpty() && authorName.isNotEmpty() && publicationYear.isNotEmpty() && category.isNotEmpty() && bookCoverURL.isNotEmpty()) {
            val book = BookFirebaseRealtimeDBModel(
                bookTitle,
                authorName,
                publicationYear,
                category,
                bookCoverURL
            )

            database.child(previousBookTitle).removeValue()

            database.child(bookTitle).setValue(book).addOnSuccessListener {
                Toast.makeText(
                    this@EditDetailBookActivity,
                    "Data berhasil diubah!",
                    Toast.LENGTH_SHORT
                )
                    .show()

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
                .addOnCanceledListener {
                    Toast.makeText(this@EditDetailBookActivity, "Batal diubah!", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@EditDetailBookActivity,
                        "Data Gagal diubah!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }

    }
}