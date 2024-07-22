package com.example.customemaildetailflow


import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class EmailListFragment : Fragment() {


    private lateinit var emailList:MutableList<Email>

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        emailList = viewModel.emailListVM
        println("ListFragment In onCreate")
        println("Saved ${savedInstanceState}")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("ListFragment OnCreateView")

        val view =  inflater.inflate(R.layout.fragment_email_list, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val adapter = EmailAdapter(viewModel.emailListVM,requireActivity(),viewModel)

        rv.adapter = adapter
        viewModel.addedItem.observe(requireActivity(), Observer {
            adapter.notifyDataSetChanged()
            rv.scrollToPosition(0)
        })
        rv.layoutManager =LinearLayoutManager(context)
        if(resources.configuration.screenWidthDp<700){

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)?.apply {
//            title = "Email"
//            navigationIcon = ContextCompat.getDrawable(context,R.drawable.baseline_menu_24)
//            menu.setGroupVisible(R.id.group1,false)
//            menu.setGroupVisible(R.id.group2,false)
//        }
        println("On DestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("On Destroy")
    }

}