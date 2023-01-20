package com.wurple.domain.model.preview

import com.wurple.domain.model.user.User

data class UserTempPreview(
    val user: User,
    val createPreviewRequest: CreatePreviewRequest?
)