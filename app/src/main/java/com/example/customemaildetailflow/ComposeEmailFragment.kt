package com.example.customemaildetailflow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ComposeEmailFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private var emailType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_compose_email, container, false)

//        ViewModel Initialization
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        //        UI Initialization
        val checkBox =view.findViewById<CheckBox>(R.id.checkBox)
        val editTextTo = view.findViewById<TextInputEditText>(R.id.textTo)
        val subject = view.findViewById<TextInputEditText>(R.id.textSubject)
        val emailContent = view.findViewById<EditText>(R.id.emailContent)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

//        Updating Activity Toolbar
        val activityToolbar = requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).findViewById<MaterialToolbar>(R.id.toolbar)
        activityToolbar.apply {
            menu.setGroupVisible(R.id.group2,true)
        }
        activityToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)


        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        editTextTo.addTextChangedListener(object:
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.matches(emailRegex)!=true){
                    editTextTo.error = "Invalid Email"
                }
                else{
                    editTextTo.error = null
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })


        activityToolbar.menu.findItem(R.id.sendEmail).setOnMenuItemClickListener {
            println(editTextTo.text)
            println(checkBox.isChecked)
            println(editTextTo.text)
            println(subject.text)
            println(emailContent.text)
            checkBox.isChecked = false
            println(radioGroup.checkedRadioButtonId)
            when(radioGroup.checkedRadioButtonId){
                R.id.radioBtnImportant ->{
                    println("Important")
                    emailType = "Important"
                }
                R.id.radioBtnSocial -> {
                    println("Social")
                    emailType = "Social"
                }
                R.id.radioBtnBusiness -> {
                    println("Business")
                    emailType = "Business"
                }
                R.id.radioBtnPersonnel -> {
                    println("Personnel")
                    emailType = "Personnel"
                }
            }
            if((editTextTo.text != null) && (subject.text != null) && (emailContent.text != null)&&(radioGroup.checkedRadioButtonId!=-1)){
                viewModel.addItem(Email(title = editTextTo.text.toString(), subtitle = subject.text.toString(), date = "07 july",
                    content = emailContent.text.toString(), isStarred = false, isViewed = false, important = emailType, notifySender = checkBox.isChecked))
                editTextTo.text = null
                subject.text = null
                emailContent.text = null
                checkBox.isChecked = false
                editTextTo.error = null
                radioGroup.clearCheck()
                activityToolbar.apply {
                    menu.setGroupVisible(R.id.group2,false)

                    title = "Email"
                    navigationIcon = ContextCompat.getDrawable(context,R.drawable.baseline_menu_24)
                }
                requireActivity().supportFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(context,"Give Inputs for all the Fields",Toast.LENGTH_SHORT).show()
            }
            true
        }
        return view
    }
}