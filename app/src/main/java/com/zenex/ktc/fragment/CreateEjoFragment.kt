package com.zenex.ktc.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.databinding.FragmentCreateEjoBinding
import java.util.stream.DoubleStream.builder

class CreateEjoFragment : Fragment() {
    private var _binding: FragmentCreateEjoBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEjoBinding.inflate(layoutInflater, container, false)

        binding.clMain.setOnClickListener {
            val act = activity as BaseActivity
            act.hideSoftKeyboard()
            act.currentFocus?.clearFocus()
        }

        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "EJO Saved!", Toast.LENGTH_SHORT).show()
            val direction = CreateEjoFragmentDirections.actionCreateEjoFragmentToHomeFragment()
            this.findNavController().navigate(direction)
        }

        binding.cardStatus.setBackgroundResource(R.drawable.shape_background_cardview_yellow)
        binding.btnCancel.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                .setMessage("Confirm to cancel ${binding.tvTitle.text}?")

//            dialog.setPositiveButton("Discard"){ _, _ ->
//
//            }
            dialog.setNegativeButton("Confirm"){_, _ ->
                binding.btnCancel.visibility = GONE
                binding.btnSave.isEnabled = false
                binding.cardStatus.setBackgroundResource(R.drawable.shape_background_cardview_status_red)
                binding.tvStatus.text = "CANCELLED"
            }

            dialog.show()
        }

        return binding.root
    }

}