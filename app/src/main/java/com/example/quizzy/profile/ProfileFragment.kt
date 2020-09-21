package com.example.quizzy.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.databinding.FragmentProfileBinding
import com.example.quizzy.network.Status


class ProfileFragment: Fragment() {

    private val viewModel: ProfileViewModel by viewModels( ownerProducer = {this}, factoryProducer = {ViewModelFactory(requireActivity().application)} )

    private val getGalleryPicture = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.setImageUri(uri)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.user.observe(viewLifecycleOwner, {it?.let { viewModel.init() }})

        viewModel.editEnabled.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    binding.buttonEdit.setImageResource(R.drawable.ic_confirm)
                    binding.userName.isEnabled = true
                    binding.userName.setSelection(binding.userName.text.length)
                    binding.userEmail.isEnabled = true
                }
                false -> {
                    binding.buttonEdit.setImageResource(R.drawable.ic_edit)
                    binding.userName.isEnabled = false
                    binding.userEmail.isEnabled = false
                }
            }
        })

        viewModel.updateStatus.observe(viewLifecycleOwner, {
            it?.let {
                when(it) {
                    Status.SUCCESS -> Toast.makeText(requireActivity(), "Profile updated", Toast.LENGTH_SHORT).show()
                    Status.FAILURE -> Toast.makeText(requireActivity(), "Profile update failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.userPicture.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                viewModel.enableEdit()
                getGalleryPicture.launch("image/*")
            }
            else{
                requestPermission();
            }
            Log.d("choosing image =>","here to choose image");
        }

        binding.buttonEdit.setOnClickListener {
            if (viewModel.editEnabled.value == null || viewModel.editEnabled.value == false)
                viewModel.enableEdit()
            else {
                viewModel.postImage()
                viewModel.disableEdit()
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideTopTextView()
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(requireContext()).setTitle("Permission needed").setMessage("bla bla bla").setPositiveButton("okay", DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        IMAGE_PERMISSION)
            })
                    .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                    .create().show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IMAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == IMAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_LONG).show()
                getGalleryPicture.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "Not granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val IMAGE_PERMISSION = 1

}