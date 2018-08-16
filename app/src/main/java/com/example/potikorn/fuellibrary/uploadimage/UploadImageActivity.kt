package com.example.potikorn.fuellibrary.uploadimage

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.potikorn.fuellibrary.R
import com.example.potikorn.fuellibrary.getRealPathFromURI
import com.github.kittinunf.fuel.httpUpload
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_upload_image.*
import java.io.File

class UploadImageActivity : AppCompatActivity() {

    private var currentUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        frameUpload.setOnClickListener {
            checkPermission()
        }
        btnUpload.apply {
            isEnabled = false
            isSelected = false
            setOnClickListener {
                currentUri?.let { uri ->
                    btnUpload.apply {
                        isEnabled = false
                        isSelected = false
                    }
                    pbLoading.visibility = View.VISIBLE
                    uploadImageToApi(File(uri.getRealPathFromURI(this@UploadImageActivity)))
                } ?: kotlin.run {
                    Toast.makeText(
                        this@UploadImageActivity,
                        getString(R.string.please_select_your_image),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            data?.apply {
                currentUri = this.data
                ivIconUpload.visibility = View.INVISIBLE
                Glide.with(this@UploadImageActivity).load(currentUri).into(ivUploadImagePreview)
                btnUpload.apply {
                    isEnabled = true
                    isSelected = true
                }
            }
        }
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
            .withPermissions(CAMERA, WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    when (!hasDeniedPermission(report)) {
                        true -> openImageChooser()
                        else -> Toast.makeText(
                            this@UploadImageActivity,
                            R.string.permission_denied,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) =
                    token.continuePermissionRequest()

                private fun hasDeniedPermission(report: MultiplePermissionsReport): Boolean {
                    val denyPermission = report.deniedPermissionResponses
                    return denyPermission != null && denyPermission.size > 0
                }
            }).check()
    }

    private fun openImageChooser() {
        startActivityForResult(
            Intent.createChooser(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ).apply { type = "image/*" },
                getString(R.string.choose_from_gallery)
            ),
            REQUEST_PICK_IMAGE
        )
    }

    private fun uploadImageToApi(imgFile: File) {
        "https://api.cloudinary.com/v1_1/potikorncloudstorage/upload"
            .httpUpload(parameters = listOf("upload_preset" to "wqn8q546"))
            .source { request, url ->
                imgFile
            }.name {
                "file"
            }.progress { readBytes, totalBytes ->
                val loadingProgress = (readBytes.toFloat() / totalBytes.toFloat()) * 100
                when (loadingProgress.toInt()) {
                    in 0..99 -> {
                        pbLoading.progress = loadingProgress.toInt()
                    }
                    100 -> {
                        pbLoading.apply {
                            progress = 0
                            visibility = View.GONE
                        }
                    }
                }
            }.responseString { request, response, result ->
                Log.d("UPLOAD_SUCCESS", request.toString())
                btnUpload.apply {
                    isEnabled = true
                    isSelected = true
                }
                result.fold({
                    Log.d("UPLOAD_SUCCESS", it)
                }, {
                    Log.d("UPLOAD_FAILED", String(it.errorData))
                })
            }
    }

    companion object {
        const val REQUEST_PICK_IMAGE = 1001
    }
}