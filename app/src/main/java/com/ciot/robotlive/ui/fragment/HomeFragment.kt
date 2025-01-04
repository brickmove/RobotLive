package com.ciot.robotlive.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.ciot.robotlive.MainActivity
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.bean.RobotData
import com.ciot.robotlive.databinding.FragmentHomeBinding
import com.ciot.robotlive.ui.adapter.MenuListAdapter
import com.ciot.robotlive.ui.adapter.RobotListAdapter
import com.ciot.robotlive.ui.base.BaseFragment
import com.ciot.robotlive.ui.custom.MenuListDecoration
import com.ciot.robotlive.ui.custom.RobotDiffCallback
import com.ciot.robotlive.ui.custom.RobotListDecoration
import com.ciot.robotlive.utils.ContextUtil
import com.ciot.robotlive.utils.MyLog

class HomeFragment : BaseFragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }
    private lateinit var binding : FragmentHomeBinding
    private var menuListRecycleView: RecyclerView? = null
    private var robotListRecyclerView: RecyclerView? =null
    private var menuListAdapter: MenuListAdapter? = null
    private var robotListAdapter: RobotListAdapter? = null
    private val mDataList: MutableList<RobotData> = ArrayList()

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        menuListRecycleView = binding.rvHomeMenu
        robotListRecyclerView = binding.rvRobotList
    }

    private fun initListener() {

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMenuListAdapter()
        initRobotListAdapter()
    }

    private fun initRobotListAdapter() {
        mDataList.clear()
        robotListAdapter = RobotListAdapter(ContextUtil.getContext(), mDataList)
        robotListRecyclerView?.adapter = robotListAdapter
        val spaceItemDecoration = RobotListDecoration(30, 0)
        robotListRecyclerView?.addItemDecoration(spaceItemDecoration)
        robotListAdapter?.setCtlButtonClickListener(object : RobotListAdapter.OnCtlClickListener{
            override fun onCtlClick(position: Int) {
                if (position <= RecyclerView.NO_POSITION || mDataList.size == 0) {
                    //防止异常
                    return
                }
                val robotId = mDataList[position].id
                // 进入视频监控页面
                (activity as MainActivity).showLive()
            }
        })
    }

    private fun initMenuListAdapter() {
        mDataList.clear()
        menuListAdapter = MenuListAdapter(ContextUtil.getContext(), mDataList)
        menuListRecycleView?.adapter = menuListAdapter
        val spaceItemDecoration = MenuListDecoration(0, 11)
        menuListRecycleView?.addItemDecoration(spaceItemDecoration)
    }

    override fun refreshData(isRefreshImmediately: Boolean, data: DealResult) {
        val oldList = ArrayList(mDataList)
        if (isDetached) {
            return
        }
        if (mDataList.size > 0) {
            mDataList.clear()
        }
        data.robotInfoList?.let { mDataList.addAll(it) }
        MyLog.d(TAG, "refreshData: " + GsonUtils.toJson(mDataList))
        val diffCallback = RobotDiffCallback(oldList, mDataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        robotListAdapter?.let { diffResult.dispatchUpdatesTo(it) }
    }
}
