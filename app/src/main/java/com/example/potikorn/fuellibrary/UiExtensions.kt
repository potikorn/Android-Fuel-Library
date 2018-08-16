package com.example.potikorn.fuellibrary

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun Uri.getRealPathFromURI(context: Context): String = let {
    var cursor = context.contentResolver.query(this, null, null, null, null)
    cursor.moveToFirst()
    var documentId = cursor.getString(0)
    documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
    cursor.close()
    cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
        MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null
    )
    cursor.moveToFirst()
    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
    cursor.close()
    path
}