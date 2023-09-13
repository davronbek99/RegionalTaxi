package dev.davron.regionaltaxi.ui.dialogs.fragmentBottomSheetDialogs.settingPermissionDialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.davron.regionaltaxi.databinding.BottomSheetSettingPermissionFragmentBinding

class OpenSettingsBottomSheetFragment(
    val icon: Int,
    val title: String,
    val changeListener: ChangeListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSettingPermissionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSettingPermissionFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up
        setUpUi()
        uiClickListener()
    }

    private fun uiClickListener() {
        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.openSettingsBtn.setOnClickListener {
            dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun setUpUi() {
        binding.icon.setImageResource(icon)
        binding.title.text = title
    }

    interface ChangeListener {
        fun dismissListenerPermissionBottomSheet()
    }
}