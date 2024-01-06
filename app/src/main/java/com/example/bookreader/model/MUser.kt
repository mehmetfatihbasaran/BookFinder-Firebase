package com.example.bookreader.model

data class MUser(
    val id: String?,
    val userId: String,
    val displayName: String,
    val image: String,
    val quote: String,
    val profession: String
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
            "image" to this.image
        )
    }

}
