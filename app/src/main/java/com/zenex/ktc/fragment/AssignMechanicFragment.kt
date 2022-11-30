package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.zenex.ktc.R
import com.zenex.ktc.adapter.AssignMechanicAdapter
import com.zenex.ktc.data.MechanicDetails
import com.zenex.ktc.databinding.FragmentAssignMechanicBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AssignMechanicFragment : Fragment() {
    private var _binding: FragmentAssignMechanicBinding? = null
    private val binding get() = _binding!!
    var listAssignedMechanic = ArrayList<MechanicDetails>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignMechanicBinding.inflate(inflater, container, false)

        binding.btnReject.setOnClickListener{
            val direction = AssignMechanicFragmentDirections.actionAssignMechanicFragmentToFaultReportFragment()
            findNavController().navigate(direction)
        }

        val args = AssignMechanicFragmentArgs.fromBundle(requireArguments())
        binding.tvTitle.text = "Fault Report ${args.reportNo}"
        binding.tilAssetId.editText?.setText(args.assetId)

        binding.btnAssignMechanic.setOnClickListener{
            showDialogAssignMechanic()
        }

        binding.cardStatus.setBackgroundResource(R.drawable.shape_background_cardview_yellow)
        binding.btnReject.setOnClickListener{
            val dialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                .setMessage("Confirm to reject FR ${args.reportNo}?")
            dialog.setNegativeButton("Confirm"){_, _ ->
                binding.btnAssignMechanic.isEnabled = false
                binding.btnReject.visibility = GONE
                binding.cardStatus.setBackgroundResource(R.drawable.shape_background_cardview_status_red)
                binding.tvStatus.text = "REJECTED"
                listAssignedMechanic = ArrayList()
                updateRv()
            }

            dialog.setNeutralButton("Discard"){_, _ ->

            }

            dialog.show()
        }

        return binding.root

    }
    

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialogAssignMechanic(){
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.setContentView(R.layout.dialog_assign_mechanic)

        val tilMechanicName: TextInputLayout = dialog.findViewById(R.id.tilMechanicName)
        val tilSpecialisation: TextInputLayout = dialog.findViewById(R.id.tilSpecialisation)
        val tilWorkCenter: TextInputLayout = dialog.findViewById(R.id.tilWorkCenter)
        val tilDateAssigned: TextInputLayout = dialog.findViewById(R.id.tilDateAssigned)
        val btnAssignMechanic: Button = dialog.findViewById(R.id.btnAssignMechanic)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val formatted = current.format(formatter)
        tilDateAssigned.editText?.setText(formatted.toString())

        btnAssignMechanic.setOnClickListener{
            val assignedMechanic = MechanicDetails(
                name=tilMechanicName.editText?.text.toString(),
                specialisation=tilSpecialisation.editText?.text.toString(),
                workCenter=tilWorkCenter.editText?.text.toString(),
                dateAssigned=tilDateAssigned.editText?.text.toString(),
            )
            listAssignedMechanic.add(assignedMechanic)
            dialog.cancel()
            updateRv()
        }
        dialog.show()
    }

    fun updateRv(){
        if (listAssignedMechanic.size > 0){
            binding.llAssignMechanic.visibility = VISIBLE
            binding.rv.adapter = null
            val adapter = AssignMechanicAdapter(requireContext(), listAssignedMechanic, this)
            adapter.setHasStableIds(true)
            binding.rv.layoutManager = LinearLayoutManager(requireContext())
            binding.rv.adapter = adapter
        } else {
            binding.llAssignMechanic.visibility = GONE
        }


    }

}