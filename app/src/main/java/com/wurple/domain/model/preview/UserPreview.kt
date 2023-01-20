package com.wurple.domain.model.preview

import com.wurple.domain.model.user.User

data class UserPreview(
    val user: User,
    val preview: Preview
)