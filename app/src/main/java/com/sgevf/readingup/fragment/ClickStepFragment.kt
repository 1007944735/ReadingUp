package com.sgevf.readingup.fragment

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sgevf.readingup.R
import com.sgevf.readingup.databinding.FragmentStepClickBinding
import com.sgevf.readingup.screenshot.ScreenShotService
import com.sgevf.readingup.viewmodel.TaskStepCreateViewModel
import java.util.Objects

class ClickStepFragment: Fragment() {

    private lateinit var mViewModel: TaskStepCreateViewModel

    private lateinit var mDataBinding: FragmentStepClickBinding
    private var resultCode: Int = -1
    private var resultData: Intent ? = null

    private var launcher: ActivityResultLauncher<Intent>? = null

    private var screenShotService: ScreenShotService? = null

    private val con = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            p1?.let {
                screenShotService = (it as ScreenShotService.ScreenShotBinder).getService()
                startScreenShot()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            screenShotService = null
        }

    }

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
                startScreenShot()
            }
        }
        mDataBinding.btnChooseArea.setOnClickListener {
            if (resultCode == -1 && resultData == null) {
                requestRecordScreenPermission()
            } else {
                startScreenShot()
            }
        }
    }

    private fun requestRecordScreenPermission() {
        val mediaProjectionManager = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        launcher?.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    private fun startScreenShot() {
        if (screenShotService == null) {
            val intent = Intent(requireContext(), ScreenShotService::class.java)
            requireActivity().bindService(intent, con, Service.BIND_AUTO_CREATE)
        } else {
            screenShotService!!.screenCapture()?.let {
                mDataBinding.rivChooseArea.setImageBitmap(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}