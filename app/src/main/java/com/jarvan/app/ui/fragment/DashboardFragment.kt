package com.jarvan.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.jarvan.app.data.User
import com.jarvan.app.databinding.FragmentDashboardBinding
import com.jarvan.app.viewmodels.DashboardViewModel

class DashboardFragment : Fragment() {

//    private val dashboardViewModel: DashboardViewModel by viewModels {
//        CustomViewModelProvider.providerDashboardViewModel()
//    }

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]

        val fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        fragmentDashboardBinding.user = User(
            "https://img1.sycdn.imooc.com/5af93c7b000156ab02600260-160-160.jpg",
            true
        )
        fragmentDashboardBinding.mAvatar.setOnClickListener {
            ARouter.getInstance().build("/webview/test_activity").navigation()
        }
        fragmentDashboardBinding.mTextDashboard.text = dashboardViewModel.repoResult.value.toString()
        dashboardViewModel.repoResult.observe(viewLifecycleOwner, Observer {
            fragmentDashboardBinding.mTextDashboard.text = it.toString()
        })
        return fragmentDashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         dashboardViewModel.fetchRepos()
    }

}
