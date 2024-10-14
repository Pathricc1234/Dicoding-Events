package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.database.local.entity.DetailEventEntity
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.dicoding.dicodingevent.ui.EventModelFactory

class DetailActivity : AppCompatActivity() {
    private var _binding : ActivityDetailBinding? = null
    private val binding get() = _binding

    private lateinit var eventsData: DetailEventEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val eventId = intent.getStringExtra(EVENT_ID)

        val factory : EventModelFactory = EventModelFactory.getInstance(this)
        val viewModel: DetailViewModel by viewModels {
            factory
        }

        if (eventId != null) {
            viewModel.getDetailEvent(eventId)
            viewModel.eventDetail.observe(this) { response ->
                when (response) {
                    is Result.Success -> {
                        showLoading(false)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            this,
                            "Error occurred: ${response.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }

            viewModel.setDetailEvent(eventId).observe(this) { detailEvent ->
                if (detailEvent != null) {
                    eventsData = detailEvent
                    setDetailData(detailEvent)
                }
            }
        }

        binding?.fabFavorite?.setOnClickListener{
            if(eventsData.favorite){
                eventsData.favorite = false
                binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_false)
                viewModel.updateDetail(eventsData)
                viewModel.setFavorite(eventsData, eventsData.favorite)
            }
            else{
                eventsData.favorite = true
                binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_true)
                viewModel.updateDetail(eventsData)
                viewModel.setFavorite(eventsData, eventsData.favorite)
            }
        }
    }

    private fun setDetailData(event: DetailEventEntity) {
        binding?.eventBanner?.let { Glide.with(this).load(event.banner).into(it) }
        binding?.tvEventName?.text = event.name
        binding?.tvOwner?.text = getString(R.string.host_name, event.ownerName)
        binding?.tvTime?.text = event.beginTime
        binding?.tvQuota?.text = getString(R.string.quota_left, (event.quota - event.registrant))
        binding?.tvDescription?.text = event.description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        binding?.btnRegister?.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)
            startActivity(intent)
        }
        if(event.favorite){
            binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_true)
        }
        else{
            binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pgDetail?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private const val EVENT_ID = "event_id"
    }
}