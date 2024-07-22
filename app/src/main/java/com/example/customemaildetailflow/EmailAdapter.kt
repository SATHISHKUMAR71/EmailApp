package com.example.customemaildetailflow

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class EmailAdapter(var emailListVM:MutableList<Email>,val activity: FragmentActivity,var viewModel: MainActivityViewModel) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {


    inner class EmailViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bindData(email:CustomEmail,position:Int){
            email.setDate(emailListVM[position].date)
            email.setTitle(emailListVM[position].title)
            email.setContent(emailListVM[position].content)
            email.setProfileLetter(emailListVM[position].title[0].uppercaseChar().toString())
            email.setSubtitle(emailListVM[position].subtitle)
            isViewed(email,position)
            isStarred(email, position)
        }
        fun isViewed(email: CustomEmail,position: Int){
            if(emailListVM[position].isViewed){
                email.getTitle().typeface = Typeface.DEFAULT
                email.getDate().typeface = Typeface.DEFAULT
                email.getSubtitle().typeface = Typeface.DEFAULT
            }
            else{
                email.getTitle().typeface = Typeface.DEFAULT_BOLD
                email.getDate().typeface = Typeface.DEFAULT_BOLD
                email.getSubtitle().typeface = Typeface.DEFAULT_BOLD
            }
        }

        fun isStarred(email: CustomEmail,position: Int){
            if(emailListVM[position].isStarred){
                email.getStar().setImageResource(R.drawable.baseline_star_24)
            }
            else{
                email.getStar().setImageResource(R.drawable.baseline_star_outline_24)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.email_view,parent,false)
        return EmailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emailListVM.size
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {

        holder.itemView.apply {
            val email = this.findViewById<CustomEmail>(R.id.emailView)
            val star = email.getStar()
            holder.bindData(email,position)
            this.setOnClickListener{
                emailListVM[position].isViewed = true
                viewModel.selectedItem.value = emailListVM[position]
                holder.isViewed(email,position)
                if(resources.configuration.screenWidthDp<700){
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentEmailList,EmailDetailFragment())
                        .addToBackStack("Fragment Detail")
                        .commit()
                }
            }
            viewModel.selectedItem.observe(context as FragmentActivity, Observer {
                holder.isStarred(email,position)
            })
            star.setOnClickListener {
                emailListVM[position].isStarred = !emailListVM[position].isStarred
                holder.isStarred(email,position)
                viewModel.selectedItem.value = emailListVM[position]
                notifyItemChanged(position)
            }
        }
    }
}