package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("success")
	val success: String? = null,

	@field:SerializedName("prediction")
	val prediction: Prediction? = null
)

data class Prediction(

	@field:SerializedName("confidence_score")
	val confidenceScore: String? = null,

	@field:SerializedName("class_name")
	val className: String? = null
)
