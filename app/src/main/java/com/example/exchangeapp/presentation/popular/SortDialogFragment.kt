package com.example.exchangeapp.presentation.popular

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.exchangeapp.R

class SortDialogFragment(
    private val clickListener: (Int) -> Unit
): DialogFragment() {
    private var choice: Int = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setSingleChoiceItems(arrayOf("По умолчанию", "По алфавиту", "По значению"),0) {_, pos->
                choice = pos
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                clickListener(choice)
            }
            .create()

    companion object {
        const val TAG = "SortDialogFragment"
    }
}