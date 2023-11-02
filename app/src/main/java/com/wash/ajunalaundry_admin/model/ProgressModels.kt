package com.wash.ajunalaundry_admin.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ProgressModels(
    var progressDate:Timestamp,
    var activity:String? = null,
    @DocumentId var UID:String?=null
)
