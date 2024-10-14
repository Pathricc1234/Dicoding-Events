package com.dicoding.dicodingevent.ui.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.ui.SettingModelFactory
import com.dicoding.dicodingevent.ui.reminder.MyWorker
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean ->
    }

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workManager = WorkManager.getInstance(requireContext())
        val switchTheme = view.findViewById<SwitchMaterial>(R.id.sw_dark)
        val switchNotification = view.findViewById<SwitchMaterial>(R.id.sw_reminder)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val mainViewModel = ViewModelProvider(this, SettingModelFactory(pref))[SettingViewModel::class.java]

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) {
                switchTheme.isChecked = true
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                switchTheme.isChecked = false
                AppCompatDelegate.MODE_NIGHT_NO
            })
        }

        mainViewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationActive: Boolean ->
            switchNotification.isChecked = isNotificationActive
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                startPeriodicNotification()
            }
            else{
                cancelPeriodicTask()
            }
            mainViewModel.saveNotificationSetting(isChecked)
        }
    }

    private fun startPeriodicNotification() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
}