package com.example.customemaildetailflow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data

class AttachmentView(private var attachmentList:List<String>, private var viewModel: MainActivityViewModel) :RecyclerView.Adapter<AttachmentView.AttachmentHolder>(){
    inner class AttachmentHolder(attachmentView:View):RecyclerView.ViewHolder(attachmentView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        return AttachmentHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachments_view,parent,false))
    }

    override fun getItemCount(): Int {
        return attachmentList.size
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.attachmentName).text = attachmentList[position]

        holder.itemView.findViewById<ImageView>(R.id.downloadAttachment).setOnClickListener{
            viewModel.enqueueDownloadWork(Data.Builder().putString("work","downloadData").build())
        }
    }
}