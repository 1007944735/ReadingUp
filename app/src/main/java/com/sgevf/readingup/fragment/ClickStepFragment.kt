package com.sgevf.readingup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sgevf.readingup.R
import com.sgevf.readingup.databinding.FragmentStepClickBinding
import com.sgevf.readingup.viewmodel.TaskStepCreateViewModel

class ClickStepFragment: Fragment() {

    private lateinit var mViewModel: TaskStepCreateViewModel

    private lateinit var mDataBinding: FragmentStepClickBinding
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
        mDataBinding.btnChooseArea.setOnClickListener {

        }
    }
}