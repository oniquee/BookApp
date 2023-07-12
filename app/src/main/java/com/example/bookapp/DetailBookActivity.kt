package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookapp.databinding.ActivityDetailBookBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var database: DatabaseReference
    private lateinit var bookTitle: String
    private lateinit var authorName: String
    private lateinit var publicationYear: String
    private lateinit var category: String
    private lateinit var bookCoverURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        settingFirebaseRealtimeDB()
        setData()
        setButton()
    }

    private fun setData() {
        if (intent.extras != null) {
            bookTitle = intent.getStringExtra("bookTitle")!!
            authorName = intent.getStringExtra("authorName")!!
            publicationYear = intent.getStringExtra("publicationYear")!!
            category = intent.getStringExtra("category")!!
            bookCoverURL = intent.getStringExtra("bookCoverURL")!!

            binding.tvBookTitle.text = bookTitle
            binding.tvAuthorName.text = authorName
            binding.tvPublicationYear.text = publicationYear
            binding.tvCategory.text = category

            Glide
                .with(this)
                .load(bookCoverURL)
                .into(binding.ivBookCover)
        }
    }

    private fun setButton() {
        binding.btnDeleteBook.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        binding.btnEditBook.setOnClickListener{
            intent = Intent(this, EditDetailBookActivity::class.java)
            intent.putExtra("bookTitle", bookTitle)
            intent.putExtra("authorName", authorName)
            intent.putExtra("publicationYear", publicationYear)
            intent.putExtra("category", category)
            intent.putExtra("bookCoverURL", bookCoverURL)
            startActivity(intent)
        }
    }

    private fun settingFirebaseRealtimeDB() {
        database = FirebaseDatabase.getInstance().getReference("Books")
    }


    private fun deleteDataFromFirebaseRealtimeDB() {
        val bookRef = database.child(bookTitle)

        bookRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@DetailBookActivity, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@DetailBookActivity, "Gagal menghapus data!", Toast.LENGTH_SHORT).show()
            }
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
            .setMessage("Anda yakin ingin menghapus data ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteDataFromFirebaseRealtimeDB()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}