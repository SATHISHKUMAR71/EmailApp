package com.example.customemaildetailflow

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class EmailDetailFragment : Fragment() {

    private lateinit var title:TextView
    private lateinit var subtitle:TextView
    private lateinit var content:TextView
    private lateinit var date:TextView
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var starImage:ImageView
    private lateinit var profileView:TextView
    private lateinit var view:View
    private lateinit var scrollView:ScrollView
    private var currentTitle=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        title = view.findViewById(R.id.title)
        content = view.findViewById(R.id.content)
        starImage = view.findViewById(R.id.star)
        view.transitionName = arguments?.getString("transitionName")
        profileView = view.findViewById(R.id.profileView)
        scrollView = view.findViewById(R.id.scrollViewEmailDetail)
        view.visibility = View.INVISIBLE


        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { email ->
            view.visibility = View.VISIBLE
            scrollView.scrollTo(0,0)
            date.text = email.date
            title.text = email.title
            currentTitle = email.title
            subtitle.text = email.subtitle
            content.text = email.content
            if(resources.configuration.screenWidthDp<700){
                activity?.findViewById<AppBarLayout>(R.id.appBarLayout)?.findViewById<MaterialToolbar>(R.id.toolbar)?.apply {
                    setNavigationIcon(R.drawable.baseline_arrow_back_24)
                    title = currentTitle
                }
            }
            if(email.isStarred){
                starImage.setImageResource(R.drawable.baseline_star_24)
            }
            else{
                starImage.setImageResource(R.drawable.baseline_star_outline_24)
            }
            profileView.text = email.title[0].toString()


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
        return view
    }

}