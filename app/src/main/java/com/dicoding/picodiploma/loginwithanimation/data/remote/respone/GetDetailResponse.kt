package com.dicoding.picodiploma.loginwithanimation.data.remote.respone

data class GetDetailResponse(
	val error: Boolean? = null,
	val message: String? = null,
	val story: Story? = null
)

data class Story(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val lon: Double? = null,
	val id: String? = null,
	val lat: Double? = null
)

