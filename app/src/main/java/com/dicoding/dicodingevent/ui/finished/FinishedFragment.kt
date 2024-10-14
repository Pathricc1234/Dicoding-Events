package com.dicoding.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.EventModelFactory
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.ui.adapter.FinishedAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFinishedBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: EventModelFactory = EventModelFactory.getInstance(requireActivity())
        val viewModel: FinishedViewModel by viewModels {
            factory
        }

        val eventsAdapter = FinishedAdapter()

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

        viewModel.getAllFinished().observe(viewLifecycleOwner) { finishedEvents ->
            eventsAdapter.submitList(finishedEvents)
        }

        binding?.rvFinished?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = eventsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pgFinished?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}