package com.example.customemaildetailflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class EmailDetailFragment : Fragment() {

    private lateinit var title:TextView
    private lateinit var titleTool:MaterialToolbar
    private lateinit var emailG:Email
    private lateinit var appbar:AppBarLayout
    private lateinit var toolbar:MaterialToolbar
    private lateinit var subtitle:TextView
    private lateinit var content:TextView
    private lateinit var date:TextView
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var starImage:ImageView
    private lateinit var profileView:TextView
    private lateinit var view:View
    private lateinit var recyclerView:RecyclerView
    private lateinit var scrollView:ScrollView
    private var currentTitle=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("On Create")
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_email_detail, container, false)
        date = view.findViewById(R.id.date)
        subtitle = view.findViewById(R.id.heading)
        content = view.findViewById(R.id.content)
        starImage = view.findViewById(R.id.star)
        view.transitionName = arguments?.getString("transitionName")
        profileView = view.findViewById(R.id.profileView)
        scrollView = view.findViewById(R.id.scrollViewEmailDetail)
        recyclerView = view.findViewById(R.id.attachmentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
//        view.findViewById<ImageView>(R.id.downloadAttachment).apply {
//            setOnClickListener {
//                viewModel.enqueueDownloadWork(Data.Builder().putString("work","downloadData").build())
//                visibility = View.INVISIBLE
//            }
//        }
//        view.findViewById<ImageView>(R.id.downloadAttachment1).apply {
//            setOnClickListener {
//                viewModel.enqueueDownloadWork(
//                    Data.Builder().putString("work", "downloadData").build()
//                )
//                visibility = View.INVISIBLE
//            }
//        }
        println("resources123: ${resources.configuration.screenWidthDp}")
        println("resources123: ${resources.configuration.screenWidthDp<700}")
        if(resources.configuration.screenWidthDp<700){
            title = view.findViewById(R.id.title)
            toolbar = view.findViewById(R.id.toolbar)
            toolbar.menu.findItem(R.id.markAsRead).setOnMenuItemClickListener {
                updateView(emailG,true)
                Toast.makeText(context,"Marked as Read",Toast.LENGTH_SHORT).show()
                true
            }
            toolbar.menu.findItem(R.id.markAsUnread).setOnMenuItemClickListener {
                updateView(emailG,false)
                Toast.makeText(context,"Marked as Unread",Toast.LENGTH_SHORT).show()
                true
            }
            toolbar.setNavigationOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        else{

            println("Email Detail 12203 ${viewModel.appWorker==null}")
            println("Email Detail 12203 ${viewModel.appWorker}")
            if(viewModel.appWorker!=null && !viewModel.seen){
                println("Email Detail 12203${viewModel.appWorker}")
                viewModel.workManager.getWorkInfoByIdLiveData(viewModel.appWorker!!.id).observe(viewLifecycleOwner){
                    if((it != null) &&(it.state== WorkInfo.State.ENQUEUED)){
                        Toast.makeText(context,"Email will be sent automatically once the device is online",Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.seen = true
            }
            titleTool = view.findViewById(R.id.titleTool)
            titleTool.menu.findItem(R.id.markAsRead).setOnMenuItemClickListener {
                updateView(emailG,true)
                Toast.makeText(context,"Marked as Read",Toast.LENGTH_SHORT).show()
                true
            }
            titleTool.menu.findItem(R.id.markAsUnread).setOnMenuItemClickListener {
                updateView(emailG,false)
                Toast.makeText(context,"Marked as Unread",Toast.LENGTH_SHORT).show()
                true
            }
        }
        view.visibility = View.INVISIBLE

        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { email ->

             recyclerView.adapter = AttachmentView(email.attachments,requireActivity(),viewModel)
            emailG = email
            view.visibility = View.VISIBLE
            scrollView.scrollTo(0,0)
            date.text = email.date
            if(resources.configuration.screenWidthDp>700){
                titleTool.title = email.title
            }
            else{
                toolbar.title = email.title
                title.text = email.title
            }
            currentTitle = email.title
            subtitle.text = email.subtitle
            content.text = email.content

            if(email.isStarred){
                starImage.setImageResource(R.drawable.baseline_star_24)
            }
            else{
                starImage.setImageResource(R.drawable.baseline_star_outline_24)
            }
            profileView.text = email.title[0].uppercaseChar().toString()
            starImage.setOnClickListener {
                if(email.isStarred){
                    email.isStarred = false
                    starImage.setImageResource(R.drawable.baseline_star_outline_24)
                }
                else{
                    email.isStarred = true
                    starImage.setImageResource(R.drawable.baseline_star_24)
                }
                viewModel.selectedItem.value = email
            }
        })
        println("On CreateView")
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        println("On Destroy")
    }

    private fun updateView(email:Email, flag:Boolean){
        email.isViewed = flag
        viewModel.setSelectedItem(email)
    }

    override fun onResume() {
        super.onResume()
        println("On Resume")
    }

    override fun onStop() {
        super.onStop()
        println("On Stop")
    }
}