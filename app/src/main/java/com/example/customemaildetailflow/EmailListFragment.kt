package com.example.customemaildetailflow


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class EmailListFragment : Fragment() {


    private lateinit var emailList:MutableList<Email>

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        emailList = viewModel.emailListVM
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("ListFragment OnCreateView")

        val view =  inflater.inflate(R.layout.fragment_email_list, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val adapter = EmailAdapter(viewModel.emailListVM,requireActivity(),viewModel,viewLifecycleOwner)
        rv.adapter = adapter
        viewModel.addedItem.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemInserted(0)
            adapter.notifyDataSetChanged()
            rv.scrollToPosition(0)
        })
        rv.layoutManager =LinearLayoutManager(context)
        if(resources.configuration.screenWidthDp<700){
            if(viewModel.appWorker!=null){
                viewModel.workManager.getWorkInfoByIdLiveData(viewModel.appWorker!!.id).observe(viewLifecycleOwner){
                    if((it != null) &&(it.state== WorkInfo.State.ENQUEUED)){
                        Toast.makeText(context,"Email will be sent automatically once the device is online",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            view.findViewById<ExtendedFloatingActionButton>(R.id.composeEmail).setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentEmailList,ComposeEmailFragment())
                    .addToBackStack("Compose Email")
                    .commit()
            }
        }
        else {
            view.findViewById<ExtendedFloatingActionButton>(R.id.composeEmail).setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentEmailDetail, ComposeEmailFragment())
                    .addToBackStack("Compose Email")
                    .commit()
            }
        }
        rv.addItemDecoration(DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL))
        return view
    }

}