package cn.langwazi.loadingbutton

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.MotionEvent
import android.text.TextUtils
import android.util.Log
import cn.langwazi.loadingbutton.indicators.BallIndicator

/**
 * Created by langwa on 2018/3/13.
 * 带有loading动画的button.
 */

class LoadingButton : AppCompatButton {

    companion object {
        const val STATUS_LOADING = 1
        const val STATUS_NORMAL = 2
    }

    private var mStatus = STATUS_NORMAL

    private var lbPaddingLeft = 0
    private var lbPaddingRight = 0
    private var lbPaddingTop = 0
    private var lbPaddingBottom = 0
    private val mIndicator: Indicator

    private var mAnimators: ArrayList<ValueAnimator>? = null
    private var mColorStateList: ColorStateList? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.LoadingButton, defStyleAttr, 0)
        lbPaddingTop = a!!.getDimensionPixelSize(R.styleable.LoadingButton_lbPaddingTop, 0)
        lbPaddingBottom = a.getDimensionPixelSize(R.styleable.LoadingButton_lbPaddingBottom, 0)
        lbPaddingLeft = a.getDimensionPixelSize(R.styleable.LoadingButton_lbPaddingLeft, 0)
        lbPaddingRight = a.getDimensionPixelSize(R.styleable.LoadingButton_lbPaddingRight, 0)
        val lbIndicatorColor = a.getColor(R.styleable.LoadingButton_lbIndicatorColor, Color.WHITE)
        val lbIndicatorName = a.getString(R.styleable.LoadingButton_lbIndicatorName)
        a.recycle()

        mIndicator = initIndicator(lbIndicatorName)
        mIndicator.setView(this)
        mIndicator.setIndicatorColor(lbIndicatorColor)
    }

    private fun initIndicator(indicatorName: String?): Indicator {
        if (!TextUtils.isEmpty(indicatorName)) {
            try {
                val drawableClass = Class.forName(indicatorName)
                return drawableClass.newInstance() as Indicator
            } catch (e: ClassNotFoundException) {
                Log.e("LoadingButton", "Didn't find your class , check the name again !")
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        //构建失败，初始化效果
        return BallIndicator()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //正在加载时屏蔽触摸事件
        return mStatus != STATUS_LOADING && super.onTouchEvent(event)
    }

    /**
     * 开始动画.
     */
    private fun startAnimators() {
        if (mAnimators == null) {
            mAnimators = mIndicator.onCreateAnimators()
        }
        mAnimators?.let {
            for (i in it.indices) {
                val animator = it[i]
                animator.start()
            }
        }
    }

    /**
     * 结束动画.
     */
    private fun stopAnimators() {
        mAnimators?.let {
            for (animator in it) {
                if (animator.isStarted) {
                    animator.removeAllUpdateListeners()
                    animator.cancel()
                }
            }
        }
        mAnimators = null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null && mStatus == STATUS_LOADING) {
            mIndicator.onDraw(canvas, width, height, lbPaddingTop,
                    lbPaddingBottom, lbPaddingLeft, lbPaddingRight)
        }
    }

    /**
     * 设置button的状态.
     */
    fun setStatus(status: Int) {
        if (status == mStatus) {
            return
        }
        when (status) {
            STATUS_LOADING -> {
                mColorStateList = textColors
                setTextColor(Color.TRANSPARENT)
                mStatus = STATUS_LOADING
                startAnimators()
            }
            STATUS_NORMAL -> {
                stopAnimators()
                mStatus = STATUS_NORMAL
                setTextColor(mColorStateList)
            }
            else -> {
                //do nothing
            }
        }
    }

    override fun onDetachedFromWindow() {
        stopAnimators()
        super.onDetachedFromWindow()
    }


}