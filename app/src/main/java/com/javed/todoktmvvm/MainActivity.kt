package com.javed.todoktmvvm

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.javed.todoktmvvm.adapters.FragmentAdapter
import com.javed.todoktmvvm.databinding.ActivityMainBinding
import com.javed.todoktmvvm.databinding.AddTodoDialogBinding
import com.javed.todoktmvvm.interfaces.ActivityToTodayFragmentInterface
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.viewModels.MainViewModel
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModel
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModelFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var todayFragmentViewModel: TodayFragmentViewModel
    lateinit var activityToTodayFragmentInterface: ActivityToTodayFragmentInterface


    lateinit var binding: ActivityMainBinding
    private lateinit var dateTextMilliSeconds: String
    private lateinit var timeTextMilliSeconds: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        val repository = (application as TodoApplication).todoRepository
//        mainViewModel =
//            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        todayFragmentViewModel = ViewModelProvider(
            this,
            TodayFragmentViewModelFactory(repository)
        )[TodayFragmentViewModel::class.java]

        binding.tlMain.addTab(binding.tlMain.newTab().setText("Today"))
        binding.tlMain.addTab(binding.tlMain.newTab().setText("Later"))
        binding.tlMain.setBackgroundColor(resources.getColor(R.color.Dark_blue))
        binding.tlMain.setTabTextColors(
            resources.getColor(R.color.grey),
            resources.getColor(R.color.teal_700)
        )


        binding.tlMain.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpMain.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.vpMain.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tlMain.selectTab(binding.tlMain.getTabAt(position))
            }
        })
        val fragmentManager = supportFragmentManager
        val fragmentAdapter = FragmentAdapter(fragmentManager, lifecycle)
        binding.vpMain.adapter = fragmentAdapter

    }


    fun openDialog(view: android.view.View) {

        val dialog = Dialog(this)
        val binding = AddTodoDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        binding.btnSetDate.setOnClickListener {
            handleDateButton(binding.tvSetDate)
        }
        binding.btnSetTime.setOnClickListener {
            handleTimeButton(binding.tvSetTime)
        }
        binding.btnYes.setOnClickListener {

            var dialogTodoDescription = binding.etDialogueDescription.text.toString()
            var id: Int? = null
            todayFragmentViewModel.todoMutableLiveData.observe(this) {
                id = it.count()
                Log.d("TAG2", "openDialog: "+id)
            }
            val milliSeconds: String = dateTextMilliSeconds + timeTextMilliSeconds
            val status = "PENDING"
            val todoResponseItem =
                id?.let { id ->
                    TodoResponseItem(
                        dialogTodoDescription,
                        id, milliSeconds, status
                    )
                }
            if (binding.etDialogueDescription.text.toString() != "" && binding.tvSetDate.text
                    .toString() != "" && binding.tvSetTime.text.toString() != "" && todoResponseItem != null
            ) {
                todayFragmentViewModel.todoMutableLiveData.value?.add(todoResponseItem)
                todayFragmentViewModel.todoMutableLiveData.value =
                    todayFragmentViewModel.todoMutableLiveData.value


//                todayFragmentViewModel.todoMutableLiveData.observe(this, Observer {
//                    it.forEach {
//                        Log.d("TAG2", "openDialog:89 " + it.description)
//                    }
//                })
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Fill all the fields  ", Toast.LENGTH_LONG).show()
            }
        }


    }


    private fun handleTimeButton(view: TextView?) {
        val calendar = Calendar.getInstance()
        val HOUR = calendar[Calendar.HOUR]
        val MINUTE = calendar[Calendar.MINUTE]
        val is24HourFormat = DateFormat.is24HourFormat(this)

        val timePickerDialog = TimePickerDialog(
            this,
            { timePicker, hour, minute ->
                var timeText = ""
                //                Log.i("TAG2", "onTimeSet: " + hour + minute);
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.HOUR] = hour
                calendar1[Calendar.MINUTE] = minute
                timeText = DateFormat.format("h:mm a", calendar1).toString()
                Log.d("TAG2", "onTimeSet: $timeText")
                timeTextMilliSeconds = DateFormat.format("hhmm", calendar1).toString()
                if (timeText != "") {
                    view?.visibility = View.VISIBLE
                    view?.text = timeText
                }
            }, HOUR, MINUTE, is24HourFormat
        )

        timePickerDialog.show()
    }

    private fun handleDateButton(view: TextView?) {
        val calendar = Calendar.getInstance()
        val YEAR = calendar[Calendar.YEAR]
        val MONTH = calendar[Calendar.MONTH]
        val DATE = calendar[Calendar.DATE]

        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, year, month, date ->
                var dateText = ""
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.YEAR] = year
                calendar1[Calendar.MONTH] = month
                calendar1[Calendar.DATE] = date
                dateText = DateFormat.format(" MMM dd, yyyy", calendar1).toString()
                val c = DateFormat.format("yyyyMMMdd", calendar1).toString()
                var c1 = "" + c[4] + c[5] + c[6]
                val monthNames = arrayOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )
                var monthNumber: Int = 0
                while (monthNumber < monthNames.size) {
                    if (c1 == monthNames[monthNumber]) {
                        break
                    }
                    monthNumber++
                }
                c1 = if (monthNumber < 10) {
                    "0" + (monthNumber + 1)
                } else {
                    (monthNumber + 1).toString()
                }
                dateTextMilliSeconds = "" + c[0] + c[1] + c[2] + c[3] + c1 + c[7] + c[8]
                Log.d("TAG2", "onDateSet: $dateText")
                view?.text = dateText
                if (dateText != "") {
                    view?.visibility = View.VISIBLE
                    view?.text = dateText
                }
            }, YEAR, MONTH, DATE
        )

        datePickerDialog.show()
    }

}