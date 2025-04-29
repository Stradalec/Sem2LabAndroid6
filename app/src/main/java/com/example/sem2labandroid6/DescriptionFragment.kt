package com.example.sem2labandroid6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.sem2labandroid6.databinding.FragmentEditDescriptionBinding

class EditDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentEditDescriptionBinding
    private var position: Int = -1
    private var initialDescription: String = ""

    companion object {
        const val REQUEST_KEY = "editDescriptionRequest"
        const val POSITION_KEY = "position"
        const val DESCRIPTION_KEY = "description"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        position = arguments?.getInt(POSITION_KEY, -1) ?: -1
        initialDescription = arguments?.getString(DESCRIPTION_KEY) ?: ""

        setupUI()
    }

    private fun setupUI() {
        binding.etDescription.setText(initialDescription)
        binding.etDescription.requestFocus()


        binding.etDescription.postDelayed({
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etDescription, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.btnSave.setOnClickListener {
            saveAndExit()
        }

        binding.btnCancel.setOnClickListener {
            exitWithoutSaving()
        }
    }

    private fun saveAndExit() {
        hideKeyboard()

        val result = bundleOf(
            POSITION_KEY to position,
            DESCRIPTION_KEY to binding.etDescription.text.toString()
        )

        parentFragmentManager.setFragmentResult(REQUEST_KEY, result)
        parentFragmentManager.popBackStack()
    }

    private fun exitWithoutSaving() {
        hideKeyboard()
        parentFragmentManager.popBackStack()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etDescription.windowToken, 0)
        binding.etDescription.clearFocus()
    }
}
