package com.dewival.testdewival.ui.view.sender


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.dewival.testdewival.R
import com.dewival.testdewival.databinding.SenderFragmentBinding
import com.dewival.testdewival.ui.view.BaseFragment
import com.dewival.testdewival.ui.viewmodel.*
import com.dewival.testdewival.utils.viewModels


class SenderFragment : BaseFragment() {

    private var _binding: SenderFragmentBinding? = null
    private val binding get() = _binding!!
    private val sendViewModel: SenderViewModel by viewModels { SenderViewModelImpl() }
    private val args by navArgs<SenderFragmentArgs>()
    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SenderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendViewModel.result.observe(viewLifecycleOwner, ::handleSenderResult)
        checkPermissions()
        takePhoto()
        sendPhoto()
    }

    private var selectedImageUri: Uri? = null

    private fun takePhoto() {
        binding.photo.setOnClickListener(View.OnClickListener {
            openImageChooser()
        })
    }

    private fun sendPhoto() {
        binding.send.setOnClickListener {
            if (selectedImageUri != null)
                path?.let { it1 -> sendViewModel.uploadImage(args.token, it1) }
        }
    }

    private fun handleSenderResult(result: SenderResult) {
        when (result) {
            is Success -> {
                val tittle = requireContext().getString(R.string.success)
                val message = requireContext().getString(R.string.success_send_msg)
                showDialog(tittle, message)
            }
            is ErrorSenderResult -> result.e.message?.let { showError(it) }
        }
    }


    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    path = getRealPathFromURIForGallery(selectedImageUri!!)
                    binding.photo.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun getRealPathFromURIForGallery(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(
            uri, projection, null,
            null, null
        )
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }
        return uri.path
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                val title = requireContext().getString(R.string.permission_needed)
                val explanation = requireContext().getString(R.string.Explanation)
                showDialog(title, explanation)
            }
        }


    private fun checkPermissions() {
        val title = requireContext().getString(R.string.permission_needed)
        val explanation = requireContext().getString(R.string.Explanation)

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                READ_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(READ_PERMISSION) -> {
                showDialog(title, explanation)
            }
            else -> {
                requestPermissionLauncher.launch(
                    READ_PERMISSION
                )
            }
        }
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                WRITE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            shouldShowRequestPermissionRationale(WRITE_PERMISSION) -> {
                showDialog(title, explanation)
            }
            else -> {
                requestPermissionLauncher.launch(
                    WRITE_PERMISSION
                )
            }
        }
    }

    private var dialog: AlertDialog? = null
    private fun showDialog(tittle: String, message: String) {
        if (dialog == null) {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(tittle)
                .setMessage(message)
                .setPositiveButton(
                    "Ok"
                ) { alertDialog, id ->
                    dialog = null
                    alertDialog.cancel()
                }.setOnDismissListener {
                    dialog = null
                }
            dialog = alertDialog.create()
            dialog!!.show()
        }
    }


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
        const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}