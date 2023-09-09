package dev.davron.regionaltaxi.ui.dialogs.fragmentBottomSheetDialogs.settingPermissionDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OpenSettingsBottomSheetFragment(
    val icon: Int,
    val title: String,
    val changeListener: ChangeListener
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    interface ChangeListener {
        fun dismissListenerPermissionBottomSheet()
    }
}