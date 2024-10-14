package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.EventModelFactory
import com.dicoding.dicodingevent.ui.adapter.FinishedAdapter
import com.dicoding.dicodingevent.ui.adapter.HorizontalAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)

        val factory: EventModelFactory = EventModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels{
            factory
        }

        val homeUpcomingAdapter = HorizontalAdapter()
        val homeFinishedAdapter = FinishedAdapter()

        viewModel.activeEvent.observe(viewLifecycleOwner){ response ->
            when(response){
                is Result.Success -> {
                }
                is Result.Error -> {
                    showLoading(false)
                    response.error.let { message ->
                        Log.e("Home Fragment", message)
                    }
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { response ->
            when(response){
                is Result.Success -> {
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                    response.error.let { message ->
                        response.error.let { message ->
                            Toast.makeText(
                                context,
                                "Terjadi kesalahan" + message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }

        viewModel.getHomeUpcomingEvent().observe(viewLifecycleOwner) { upcomingEvents ->
            homeUpcomingAdapter.submitList(upcomingEvents)
        }

        viewModel.getHomeFinishedEvent().observe(viewLifecycleOwner) { finishedEvents ->
            homeFinishedAdapter.submitList(finishedEvents)
        }

        binding?.rvActive?.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = homeUpcomingAdapter
        }

        binding?.rvFinished?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeFinishedAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pgHome?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}