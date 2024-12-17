package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.upload.UploadActivity
import com.dicoding.picodiploma.loginwithanimation.view.upload.UploadViewModel
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val adapter = StoryAdapter()
        binding.daftarCerita.adapter = adapter

        binding.daftarCerita.layoutManager = LinearLayoutManager(this)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getPagingStory(user.token.toString())
            }
        }

        viewModel.storyResponse.observe(this) { pagingData ->
            binding.progressBar.visibility = View.VISIBLE
            adapter.submitData(lifecycle, pagingData)
            binding.progressBar.visibility = View.INVISIBLE
        }

//        viewModel.storyResponse.observe(this) { stories ->
//            binding.progressBar.visibility = View.VISIBLE
//            if (stories.isNullOrEmpty()) {
//                Log.e("Story Response", "Tidak ada story tersedia")
//                AlertDialog.Builder(this).apply {
//                    setTitle("Oops!")
//                    setMessage("Tidak ada story tersedia")
//                    setPositiveButton("OK") { _, _ -> finish() }
//                    create()
//                    show()
//                }
//                binding.progressBar.visibility = View.INVISIBLE
//                binding.tambahStory.visibility = View.INVISIBLE
//            } else {
//                adapter.updateData(stories)
//                binding.progressBar.visibility = View.INVISIBLE
//            }
//        }



        binding.tambahStory.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) { user ->
            viewModel.getPagingStory(user.token.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionMenu -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Logout")
                    setMessage("Apakah kamu yakin ingin log out?")
                    setPositiveButton("Ya") { _, _ ->
                        viewModel.logout()
                        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                        finish()
                    }
                    setNegativeButton("Tidak", null)
                    create()
                    show()
                }
                true
            }
            R.id.actionMap -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}