package com.wurple.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.wurple.R
import com.wurple.databinding.*
import com.wurple.presentation.extension.gone
import com.wurple.presentation.extension.visible
import com.wurple.presentation.model.preview.*

class PreviewView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    // PrimaryInfo click listener
    var onUserImagePreviewVisibilityClickListener: (() -> Unit)? = null
    var onUserNamePreviewVisibilityClickListener: (() -> Unit)? = null
    var onUserPositionPreviewVisibilityClickListener: (() -> Unit)? = null

    // SecondaryInfo click listener
    var onUserLocationPreviewVisibilityClickListener: (() -> Unit)? = null
    var onUserEmailPreviewVisibilityClickListener: (() -> Unit)? = null
    var onUserEmailCopiedClickListener: (() -> Unit)? = null
    var onUserUsernamePreviewVisibilityClickListener: (() -> Unit)? = null
    var onUserBioPreviewVisibilityClickListener: (() -> Unit)? = null

    // Skills click listener
    var onUserSkillsPreviewVisibilityClickListener: (() -> Unit)? = null

    // Experience click listener
    var onUserExperiencePreviewVisibilityClickListener: (() -> Unit)? = null

    // Experience click listener
    var onUserEducationPreviewVisibilityClickListener: (() -> Unit)? = null

    // Languages click listener
    var onUserLanguagesPreviewVisibilityClickListener: (() -> Unit)? = null

    private val binding = ViewPreviewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setData(data: PreviewUi) {
        setupPrimaryInfo(data.primaryInfo)
        setupSecondaryInfo(data.secondaryInfo)
        setupSkills(data.skills)
        setupExperience(data.experience)
        setupEducation(data.education)
        setupLanguages(data.languages)
    }

    private fun setupPrimaryInfo(data: PreviewPrimaryInfoUi) {
        // UserImage
        binding.userImageImageView.load(data.userImageUrl) {
            error(R.drawable.image_user)
        }
        data.userImageButtonColor?.let {
            binding.userImageMaterialCardView.setCardBackgroundColor(it)
        }
        binding.userImageMaterialCardView.isVisible = data.isUserImageButtonVisible
        binding.userImageMaterialCardView.setOnClickListener {
            onUserImagePreviewVisibilityClickListener?.invoke()
        }

        // UserName
        binding.userNameTextView.text = data.userName
        data.userNameButtonColor?.let {
            binding.userNameMaterialCardView.setCardBackgroundColor(it)
        }
        binding.userNameMaterialCardView.isVisible = data.isUserNameButtonVisible
        binding.userNameMaterialCardView.setOnClickListener {
            onUserNamePreviewVisibilityClickListener?.invoke()
        }

        // UserExperience
        binding.userPositionTextView.text = data.userExperiencePosition
        binding.userPositionTextView.isVisible = data.userExperiencePosition.isNotEmpty()
        binding.companyNameTextView.text = data.userExperienceCompanyName
        binding.companyNameTextView.isVisible = data.userExperienceCompanyName.isNotEmpty()
        binding.companyNameSpace.isVisible = data.userExperiencePosition.isNotEmpty()
        binding.userPositionMaterialCardView
        data.userExperienceButtonColor?.let {
            binding.userPositionMaterialCardView.setCardBackgroundColor(it)
        }
        binding.userPositionMaterialCardView.isVisible =
            data.userExperiencePosition.isNotEmpty() && data.isUserExperienceButtonVisible
        binding.userPositionMaterialCardView.setOnClickListener {
            onUserPositionPreviewVisibilityClickListener?.invoke()
        }
    }

    private fun setupSecondaryInfo(data: PreviewSecondaryInfoUi) {
        binding.secondaryInfoMaterialCardView.isVisible = data.isUserLocationVisible ||
                data.isUserEmailVisible ||
                data.isUserUsernameVisible ||
                data.isUserBioVisible
        binding.secondaryInfoLinearLayout.removeAllViews()

        // UserLocation
        if (data.isUserLocationVisible) {
            with(ViewPreviewSecondaryInfoItemBinding.inflate(LayoutInflater.from(context))) {
                captionTextView.text = context.getString(R.string.edit_profile_location)
                textView.text = data.userLocation
                data.userLocationButtonColor?.let { materialCardView.setCardBackgroundColor(it) }
                materialCardView.isVisible = data.isUserLocationButtonVisible
                materialCardView.setOnClickListener {
                    onUserLocationPreviewVisibilityClickListener?.invoke()
                }
                binding.secondaryInfoLinearLayout.addView(root)
            }
        }

        // UserEmail
        if (data.isUserEmailVisible) {
            with(ViewPreviewSecondaryInfoItemBinding.inflate(LayoutInflater.from(context))) {
                if (data.isUserEmailCopyEnabled) {
                    (button as? MaterialButton)?.setIconResource(R.drawable.ic_content_copy)
                    button.visible()
                    button.setOnClickListener { onUserEmailCopiedClickListener?.invoke() }
                } else {
                    (button as? MaterialButton)?.icon = null
                    button.gone()
                    button.setOnClickListener(null)
                }
                captionTextView.text = context.getString(R.string.edit_profile_email)
                textView.text = data.userEmail
                data.userEmailButtonColor?.let { materialCardView.setCardBackgroundColor(it) }
                materialCardView.isVisible = data.isUserEmailButtonVisible
                materialCardView.setOnClickListener {
                    onUserEmailPreviewVisibilityClickListener?.invoke()
                }
                binding.secondaryInfoLinearLayout.addView(root)
            }
        }

        // UserUsername
        if (data.isUserUsernameVisible) {
            with(ViewPreviewSecondaryInfoItemBinding.inflate(LayoutInflater.from(context))) {
                captionTextView.text = context.getString(R.string.edit_profile_username)
                textView.text = data.userUsername
                data.userUsernameButtonColor?.let { materialCardView.setCardBackgroundColor(it) }
                materialCardView.isVisible = data.isUserUsernameButtonVisible
                materialCardView.setOnClickListener {
                    onUserUsernamePreviewVisibilityClickListener?.invoke()
                }
                binding.secondaryInfoLinearLayout.addView(root)
            }
        }

        // UserBio
        if (data.isUserBioVisible) {
            with(ViewPreviewSecondaryInfoItemBinding.inflate(LayoutInflater.from(context))) {
                captionTextView.text = context.getString(R.string.edit_profile_bio)
                textView.text = data.userBio
                data.userBioButtonColor?.let { materialCardView.setCardBackgroundColor(it) }
                materialCardView.isVisible = data.isUserBioButtonVisible
                materialCardView.setOnClickListener {
                    onUserBioPreviewVisibilityClickListener?.invoke()
                }
                binding.secondaryInfoLinearLayout.addView(root)
            }
        }
    }

    private fun setupSkills(data: PreviewSkillsUi) {
        // UserPersonalSkills
        binding.personalSkillsTitleTextView.isVisible = data.personalSkills.isNotEmpty()
        binding.personalSkillsChipGroup.isVisible = data.personalSkills.isNotEmpty()
        binding.personalSkillsChipGroup.removeAllViews()
        if (data.personalSkills.isNotEmpty()) {
            data.personalSkills.forEach { skill ->
                binding.personalSkillsChipGroup.addView(
                    Chip(context).apply {
                        text = skill
                        setEnsureMinTouchTargetSize(false)
                    }
                )
            }
        }

        // UserProfessionalSkills
        binding.professionalSkillsTitleTextView.isVisible = data.professionalSkills.isNotEmpty()
        binding.professionalSkillsChipGroup.isVisible = data.professionalSkills.isNotEmpty()
        binding.professionalSkillsChipGroup.removeAllViews()
        if (data.professionalSkills.isNotEmpty()) {
            data.professionalSkills.forEach { skill ->
                binding.professionalSkillsChipGroup.addView(
                    Chip(context).apply {
                        text = skill
                        setEnsureMinTouchTargetSize(false)
                    }
                )
            }
        }

        // UserSkills
        binding.skillsContainerMaterialCardView.isVisible = data.isUserSkillsVisible
        data.userSkillsButtonColor?.let { binding.skillsMaterialCardView.setCardBackgroundColor(it) }
        binding.skillsMaterialCardView.isVisible = data.isUserSkillsButtonVisible
        binding.skillsMaterialCardView.setOnClickListener {
            onUserSkillsPreviewVisibilityClickListener?.invoke()
        }
    }

    private fun setupExperience(data: PreviewExperienceUi) {
        binding.experienceContainerLinearLayout.removeAllViews()
        data.items.forEach { experienceItem ->
            with(ViewPreviewExperienceItemBinding.inflate(LayoutInflater.from(context))) {
                textView.text = experienceItem.position
                subtextView.text = experienceItem.companyName
                subtextView.isVisible = experienceItem.companyName.isNotEmpty()
                dateTextView.text = experienceItem.dateRange
                binding.experienceContainerLinearLayout.addView(root)
            }
        }
        binding.experienceContainerMaterialCardView.isVisible = data.isUserExperienceVisible
        data.userExperienceButtonColor?.let {
            binding.experienceMaterialCardView.setCardBackgroundColor(it)
        }
        binding.experienceMaterialCardView.isVisible = data.isUserExperienceButtonVisible
        binding.experienceMaterialCardView.setOnClickListener {
            onUserExperiencePreviewVisibilityClickListener?.invoke()
        }
    }

    private fun setupEducation(data: PreviewEducationUi) {
        binding.educationContainerLinearLayout.removeAllViews()
        data.items.forEach { educationItem ->
            with(ViewPreviewEducationItemBinding.inflate(LayoutInflater.from(context))) {
                textView.text = educationItem.degree
                subtextView.text = educationItem.institution
                subtextView.isVisible = educationItem.institution.isNotEmpty()
                dateTextView.text = educationItem.graduationDate
                binding.educationContainerLinearLayout.addView(root)
            }
        }
        binding.educationContainerMaterialCardView.isVisible = data.isUserEducationVisible
        data.userEducationButtonColor?.let {
            binding.educationMaterialCardView.setCardBackgroundColor(it)
        }
        binding.educationMaterialCardView.isVisible = data.isUserEducationButtonVisible
        binding.educationMaterialCardView.setOnClickListener {
            onUserEducationPreviewVisibilityClickListener?.invoke()
        }
    }

    private fun setupLanguages(data: PreviewLanguagesUi) {
        binding.languagesContainerLinearLayout.removeAllViews()
        data.items.forEach { languageItem ->
            with(ViewPreviewLanguageItemBinding.inflate(LayoutInflater.from(context))) {
                textView.text = languageItem.language
                subtextView.text = languageItem.languageLevel
                binding.languagesContainerLinearLayout.addView(root)
            }
        }
        binding.languagesContainerMaterialCardView.isVisible = data.isUserLanguagesVisible
        data.userLanguagesButtonColor?.let {
            binding.languagesMaterialCardView.setCardBackgroundColor(it)
        }
        binding.languagesMaterialCardView.isVisible = data.isUserLanguagesButtonVisible
        binding.languagesMaterialCardView.setOnClickListener {
            onUserLanguagesPreviewVisibilityClickListener?.invoke()
        }
    }
}