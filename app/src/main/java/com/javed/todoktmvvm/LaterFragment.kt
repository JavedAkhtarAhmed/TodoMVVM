package com.javed.todoktmvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.javed.todoktmvvm.adapters.CompletedAdapter
import com.javed.todoktmvvm.adapters.PendingAdapter
import com.javed.todoktmvvm.databinding.FragmentLaterBinding
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.viewModels.LaterFragmentViewModel
import com.javed.todoktmvvm.viewModels.LaterFragmentViewModelFactory
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModel
import com.javed.todoktmvvm.viewModels.TodayFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LaterFragment : Fragment() {

    lateinit var todayFragmentViewModel : TodayFragmentViewModel
    private lateinit var fragmentLaterBinding: FragmentLaterBinding
    lateinit var completedAdapter: CompletedAdapter
    lateinit var pendingAdapter: PendingAdapter
    lateinit var laterPendingList : MutableList<TodoResponseItem>
    lateinit var laterCompletedList : MutableList<TodoResponseItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragmentLaterBinding.lifecycleOwner = this

        pendingAdapter = PendingAdapter()
        completedAdapter = CompletedAdapter()
        todayFragmentViewModel.todoMutableLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            laterPendingList = arrayListOf()
            laterCompletedList = arrayListOf()
            fragmentLaterBinding.rvPending.adapter = pendingAdapter
            fragmentLaterBinding.rvCompleted.adapter = completedAdapter


            val todayDate = SimpleDateFormat("yyyyMMdd0000", Locale.getDefault()).format(Date())
            it.forEach { item->
                if (item.status=="PENDING" && item.scheduledDate.toLong()< todayDate.toLong()){
                    laterPendingList.add(item)
                }
                if (item.status=="COMPLETED" && item.scheduledDate.toLong()< todayDate.toLong()){
                    laterCompletedList.add(item)
                }

            }
            pendingAdapter.submitList(laterPendingList)
            completedAdapter.submitList(laterCompletedList)


        })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(fragmentLaterBinding.rvPending)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLaterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_later, container, false)


        val repository = (activity?.application as TodoApplication).todoRepository
        todayFragmentViewModel = ViewModelProvider(
            this,
            TodayFragmentViewModelFactory(repository)
        )[TodayFragmentViewModel::class.java]
        return fragmentLaterBinding.root

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

                    val position = laterPendingList[viewHolder.adapterPosition].id
                    laterPendingList.removeAt(viewHolder.adapterPosition)
                    pendingAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    todayFragmentViewModel.swipe(position)

                }
            }

        }
}