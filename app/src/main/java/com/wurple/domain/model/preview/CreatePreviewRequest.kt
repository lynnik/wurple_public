package com.wurple.domain.model.preview

data class CreatePreviewRequest(
    var title: String,
    var previewLifetimeOption: PreviewLifetimeOption,

    // visibility values
    var userImageVisibility: PreviewVisibilityValue,
    var userNameVisibility: PreviewVisibilityValue,
    var userCurrentPositionVisibility: PreviewVisibilityValue,
    var userLocationVisibility: PreviewVisibilityValue,
    var userEmailVisibility: PreviewVisibilityValue,
    var userUsernameVisibility: PreviewVisibilityValue,
    var userBioVisibility: PreviewVisibilityValue,
    var userSkillsVisibility: PreviewVisibilityValue,
    var userExperienceVisibility: PreviewVisibilityValue,
    var userEducationVisibility: PreviewVisibilityValue,
    var userLanguagesVisibility: PreviewVisibilityValue
)