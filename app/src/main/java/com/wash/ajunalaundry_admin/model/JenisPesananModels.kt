package com.wash.ajunalaundry_admin.model

import com.google.firebase.firestore.DocumentId

data class JenisPesananModels(var eta:String? = null, var hargaPerKilo:Int? = null, var jenis:String? = null, @DocumentId var docID:String?= null,)