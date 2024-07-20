package com.example.customemaildetailflow


import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Layout
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColor
import org.intellij.lang.annotations.JdkConstants.TreeSelectionMode
import org.w3c.dom.Text
import kotlin.math.max

class CustomEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    private var profileView:TextView
    private lateinit var star:ImageView
    private lateinit var title: TextView
    private lateinit var subTitle: TextView

    private var desiredTextSize = resources.displayMetrics.widthPixels
    private var dateLeftPos = 0
    private var dateRightPos = 0
    private lateinit var content:TextView
    private lateinit var date:TextView
    private var desiredHeight = 0
    private val shape = ShapeDrawable(OvalShape())

    init {

        profileView = TextView(context)
        star = ImageView(context)
        shape.paint.color = com.google.android.material.R.attr.colorAccent
        title = TextView(context)
        title.setTypeface(null,Typeface.BOLD)
        date = TextView(context)
        date.setTypeface(null,Typeface.BOLD)
        subTitle = TextView(context)
        subTitle.setTypeface(null,Typeface.BOLD)
        content = TextView(context)
        star.setImageResource(R.drawable.baseline_star_outline_24)


        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomEmail)
        val titleAttr= typedArray.getString(R.styleable.CustomEmail_emailTitle)
        val subtitleAttr= typedArray.getString(R.styleable.CustomEmail_emailSubtitle)
        val imgAttr = typedArray.getDrawable(R.styleable.CustomEmail_emailImage)
        val contentAttr = typedArray.getString(R.styleable.CustomEmail_emailContents)
        val dateAttr = typedArray.getString(R.styleable.CustomEmail_emailDate)
        val profileLetter = typedArray.getString(R.styleable.CustomEmail_profileLetter)
        val profileColor = typedArray.getColor(R.styleable.CustomEmail_profileColor,com.google.android.material.R.attr.colorAccent)

//        val profileColor = typedArray.getColor(R.styleable.CustomEmail_profileColor,Color.BLACK)
//        GRAVITY
        profileView.gravity = Gravity.CENTER
//        Profile views
        shape.paint.color = profileColor
        profileView.background = shape
        profileView.text = profileLetter
        profileView.setTextColor(Color.WHITE)
//        title
        title.text =titleAttr
        title.ellipsize = TextUtils.TruncateAt.END
        title.maxLines = 1
//        subtitle
        subTitle.text = subtitleAttr
        subTitle.maxLines =1
        subTitle.ellipsize = TextUtils.TruncateAt.END
//        content
        content.text = contentAttr
        content.ellipsize = TextUtils.TruncateAt.END
        content.maxLines = 1
//        date
        date.text = dateAttr
        date.textAlignment = TEXT_ALIGNMENT_TEXT_END
        date.ellipsize = TextUtils.TruncateAt.END
        date.maxLines = 1
//        star
        star.foregroundGravity = Gravity.END

        typedArray.recycle()
        addView(profileView)
        addView(title)
        addView(subTitle)
        addView(content)
        addView(date)
        addView(star)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure dimensions for all child views
        measureChildWithMargins(profileView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(title, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(subTitle, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(content, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(date, widthMeasureSpec, 0, heightMeasureSpec, 0)
        desiredTextSize = MeasureSpec.getSize(widthMeasureSpec)
        profileView.textSize = desiredTextSize * 0.02f
        title.textSize = desiredTextSize * 0.015f
        subTitle.textSize = desiredTextSize * 0.013f
        content.textSize = desiredTextSize * 0.010f
        date.textSize = desiredTextSize * 0.009f
        date.layoutParams = date.layoutParams.apply {
            width = (desiredTextSize*0.2).toInt()
        }
        dateLeftPos = desiredTextSize - (desiredTextSize*0.2).toInt() - paddingRight
        // Calculate total width required (considering padding)

        val profileViewWidth = (desiredTextSize * 0.1).toInt()
        val profileViewHeight = (profileViewWidth)

        val lp = profileView.layoutParams as MarginLayoutParams
        lp.width = profileViewWidth
        lp.height = profileViewHeight
        profileView.layoutParams = lp

        star.layoutParams = star.layoutParams.apply {
            width = (desiredTextSize*0.2).toInt()
        }
        title.layoutParams = title.layoutParams.apply {
            width = dateLeftPos - profileViewWidth - paddingLeft - (desiredTextSize*0.08).toInt()
        }

        subTitle.layoutParams = subTitle.layoutParams.apply {
            width =dateLeftPos - profileViewWidth - paddingLeft - (desiredTextSize*0.08).toInt()
//            width = dateLeftPos - profileViewWidth - paddingLeft - 50
//            width = dateLeftPos - profileViewWidth - paddingLeft - 50
        }

        content.layoutParams = content.layoutParams.apply {
//            width = (desiredTextSize-150-paddingRight) - profileViewWidth - paddingLeft - 70
            width =dateLeftPos - profileViewWidth - paddingLeft - (desiredTextSize*0.08).toInt()
        }





//        desiredWidth = profileViewWidth+maxOf( title.measuredWidth,subTitle.measuredWidth,content.measuredWidth)+ maxOf(date.measuredWidth,star.measuredWidth)+paddingLeft+paddingRight
//        val desiredWidth = profileViewWidth+ title.measuredWidth+ date.measuredWidth+ paddingLeft + paddingRight + resources.getDimensionPixelSize(R.dimen.horizontalPadding)

        // Calculate total height required (considering padding)
        desiredHeight = maxOf((title.measuredHeight +
                subTitle.measuredHeight +
                content.measuredHeight +
                paddingTop + paddingBottom + star.measuredHeight ),(profileViewHeight+paddingBottom+paddingTop+star.measuredHeight))
        // Resolve the measured dimensions against the measure specifications
        val measuredWidth = resolveSize(desiredTextSize, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)
        // Set the measured dimensions for this custom view
        setMeasuredDimension(measuredWidth, measuredHeight)
    }



    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // Variables to keep track of the current position
        var currentTop = paddingTop
        var currentWidth = paddingLeft

//      date Layout
        date.layout(
            dateLeftPos,
            currentTop,
            dateLeftPos + date.measuredWidth,
            currentTop + date.measuredHeight
        )
//        star layout
        star.layout(

            desiredTextSize-(desiredTextSize*0.05).toInt()-paddingRight,
            desiredHeight-(desiredHeight*0.3).toInt()-paddingBottom,
            dateLeftPos + date.measuredWidth,
            desiredHeight-paddingBottom
        )
//        Profile Layout
        profileView.layout(
            currentWidth,
            currentTop,
            currentWidth+profileView.measuredWidth,
            currentTop + profileView.measuredHeight
        )
        currentWidth += profileView.measuredWidth + paddingLeft
//        title Layouts
        title.layout(
            currentWidth,
            paddingTop,
            currentWidth+title.measuredWidth,
            currentTop+title.measuredHeight
        )
        currentTop += title.measuredHeight

//        Subtitle Layout
        subTitle.layout(
            currentWidth,
            currentTop,
            currentWidth+subTitle.measuredWidth,
            currentTop+subTitle.measuredHeight
        )
        currentTop += subTitle.measuredHeight
//        Content Layout
        content.layout(
            currentWidth,
            currentTop,
            currentWidth+content.measuredWidth,
            currentTop+content.measuredHeight
        )
//        Star Layout



    }



    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(lp: LayoutParams?): LayoutParams {
        return MarginLayoutParams(lp)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    fun setTitle(text: Text){
        title.text = text.toString()
    }
    fun setSubtitle(text:Text){
        subTitle.text = text.toString()
    }
    fun setContent(text:Text){
        content.text = text.toString()
    }
    fun setDate(text:Text){
        date.text = text.toString()
    }

    fun setTitle(text: String){
        title.text = text
    }
    fun setSubtitle(text:String){
        subTitle.text = text
    }
    fun setContent(text:String){
        content.text = text
    }
    fun setDate(text:String){
        date.text = text
    }
    fun setProfileLetter(letter:String){
        profileView.text = letter
    }


    fun getTitle():TextView{
        return title
    }
    fun getSubtitle():TextView{
        return subTitle
    }
    fun getContent():TextView{
        return content
    }
    fun getDate():TextView{
        return date
    }

    fun getStar():ImageView{
        return star
    }

    fun getProfileView():TextView{
        return profileView
    }
    fun getProfileLetter():String{
        return profileView.text.toString()
    }
    fun getShape():ShapeDrawable{
        return shape
    }

}