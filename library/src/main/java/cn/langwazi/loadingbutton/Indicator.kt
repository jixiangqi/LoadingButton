package cn.langwazi.loadingbutton

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View


/**
 * Created by langwa on 2018/3/14.
 * 加载动画，新增的动画效果继承改改类，覆写相关方法即可.
 */
abstract class Indicator {

    private var mView: View? = null

    /**
     * 设置宿主view.
     */
    internal fun setView(view: View) {
        this.mView = view
    }

    /**
     * 设置Indicator的颜色.
     */
    abstract fun setIndicatorColor(color: Int)

    /**
     * 绘制图形.
     * @param canvas 画布
     * @param width 绘制区域的宽度
     * @param height 绘制区域的高度
     * @param paddingTop 绘制区域上方内边距
     * @param paddingBottom 绘制区域下发内边距
     * @param paddingLeft 绘制区域左方内边距
     * @param paddingRight 绘制局域右方内边距
     */
    abstract fun onDraw(canvas: Canvas, width: Int, height: Int,
                        paddingTop: Int, paddingBottom: Int,
                        paddingLeft: Int, paddingRight: Int)

    /**
     * 创建执行动画的ValueAnimator，在创建之前应该复位相关数据.
     */
    abstract fun onCreateAnimators(): ArrayList<ValueAnimator>

    /**
     * 通知view重绘.
     */
    protected fun invalidate() {
        mView?.invalidate()
    }

    /**
     * 重绘
     */
    protected fun postInvalidate() {
        mView?.postInvalidate()
    }

}