package com.jarvan.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jarvan.app.User
import com.jarvan.app.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        fragmentDashboardBinding.user = User("https://img1.sycdn.imooc.com/5af93c7b000156ab02600260-160-160.jpg", true)
        return fragmentDashboardBinding.root
    }
}
