package com.javed.todoktmvvm

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.javed.todoktmvvm.adapters.CompletedAdapter
import com.javed.todoktmvvm.adapters.PendingAdapter
import com.javed.todoktmvvm.databinding.FragmentTodayBinding
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModel
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {

    lateinit var todayFragmentViewModel: TodayFragmentViewModel
    private lateinit var fragmentTodayBinding: FragmentTodayBinding
    lateinit var completedAdapter: CompletedAdapter
    lateinit var pendingAdapter: PendingAdapter
    val todayPendingList: MutableList<TodoResponseItem> = arrayListOf()
    private val todayCompletedList: MutableList<TodoResponseItem> = arrayListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragmentTodayBinding.todayFragmentViewModel = todayFragmentViewModel
        fragmentTodayBinding.lifecycleOwner = this


        pendingAdapter = PendingAdapter()
        completedAdapter = CompletedAdapter()


        todayFragmentViewModel.todoMutableLiveData.observe(viewLifecycleOwner,Observer {
            fragmentTodayBinding.rvPending.adapter = pendingAdapter
            fragmentTodayBinding.rvCompleted.adapter = completedAdapter

            val todayDate = SimpleDateFormat("yyyyMMdd0000", Locale.getDefault()).format(Date())
            it.forEach { item ->
                if (item.status == "PENDING" && item.scheduledDate.toLong()> todayDate.toLong()) {
                    todayPendingList.add(item)
                }
                if (item.status == "COMPLETED" && item.scheduledDate.toLong()> todayDate.toLong()) {
                    todayCompletedList.add(item)
                }

            }
            if (todayPendingList.isEmpty()){
                fragmentTodayBinding.tvNoTodo.visibility = View.VISIBLE
                fragmentTodayBinding.rvPending.visibility = View.GONE
            }
            else{
                fragmentTodayBinding.tvNoTodo.visibility = View.GONE
                fragmentTodayBinding.rvPending.visibility = View.VISIBLE
                pendingAdapter.submitList(todayPendingList)
            }
            if(todayCompletedList.isEmpty()){
                fragmentTodayBinding.tvNoCompletedTodo.visibility = View.VISIBLE
                fragmentTodayBinding.rvCompleted.visibility = View.GONE
            }
            else {
                fragmentTodayBinding.tvNoCompletedTodo.visibility = View.GONE
                fragmentTodayBinding.rvCompleted.visibility = View.VISIBLE
                completedAdapter.submitList(todayCompletedList)
            }
        })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(fragmentTodayBinding.rvPending)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTodayBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)


        val repository = (activity?.application as TodoApplication).todoRepository
        todayFragmentViewModel = ViewModelProvider(
            this,
            TodayFragmentViewModelFactory(repository)
        )[TodayFragmentViewModel::class.java]


        return fragmentTodayBinding.root

    }

    private var simpleCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                if (direction == ItemTouchHelper.RIGHT) {

                    val position = todayPendingList[viewHolder.adapterPosition].id
                    todayPendingList.removeAt(viewHolder.adapterPosition)
                    pendingAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    todayFragmentViewModel.swipe(position)

                }
            }

        }
}


