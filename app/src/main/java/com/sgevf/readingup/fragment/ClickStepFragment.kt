package com.sgevf.readingup.fragment

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sgevf.readingup.FloatingWindowService
import com.sgevf.readingup.PermissionUtil
import com.sgevf.readingup.R
import com.sgevf.readingup.Utils
import com.sgevf.readingup.databinding.FragmentStepClickBinding
import com.sgevf.readingup.screenshot.ScreenShotService
import com.sgevf.readingup.viewmodel.TaskStepCreateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClickStepFragment: Fragment() {

    private lateinit var mViewModel: TaskStepCreateViewModel

    private lateinit var mDataBinding: FragmentStepClickBinding
    private var resultCode: Int = -1
    private var resultData: Intent ? = null

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(requireActivity())[TaskStepCreateViewModel::class.java]
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_click, container, false)
        mDataBinding.lifecycleOwner = this
        mDataBinding.vm = mViewModel
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                resultCode = it.resultCode
                resultData = it.data
                startFloatingWindowService()

            }
        }
        mDataBinding.btnChooseArea.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (PermissionUtil.permit(requireContext(), Manifest.permission.POST_NOTIFICATIONS)) {
                        screenShot()
                    }
                }
            } else {
                screenShot()
            }
        }
    }

    private fun screenShot() {
        if (resultCode == -1 && resultData == null) {
            requestRecordScreenPermission()
        } else {
            startFloatingWindowService()
        }
    }

    private fun requestRecordScreenPermission() {
        val mediaProjectionManager = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        launcher?.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    private fun startFloatingWindowService() {
        if (Utils.commonROMPermissionCheck(requireContext())) {
            val intent = Intent(requireContext(), FloatingWindowService::class.java)
            intent.putExtra("resultCode", resultCode)
            intent.putExtra("resultData", resultData)
            requireContext().startService(intent)
        }
        Utils.backToPhoneHome(requireContext())
    }
}