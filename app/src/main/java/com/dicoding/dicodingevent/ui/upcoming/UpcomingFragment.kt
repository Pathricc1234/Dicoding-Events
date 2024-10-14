package com.dicoding.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.EventModelFactory
import com.dicoding.dicodingevent.ui.adapter.UpcomingAdapter

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: EventModelFactory = EventModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels { factory }

        val eventsAdapter = UpcomingAdapter()

        viewModel.activeEvent.observe(viewLifecycleOwner){ response ->
            when(response){
                is Result.Success -> {
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                    response.error.let { message ->
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }

        viewModel.getAllUpcoming().observe(viewLifecycleOwner) { finishedEvent ->
            eventsAdapter.submitList(finishedEvent)
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = eventsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pgUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}