package com.summersama.fisimili.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator

import java.util.Random

/**
 * Created by Jcs on 2017/7/10.
 */

class FishDrawable(private val mContext: Context) : Drawable() {
    //	private float mainAngle =90;//角度表示的角
    lateinit var finsAnimator: ObjectAnimator
    private var mPaint: Paint? = null
    //控制区域
    private var currentValue = 0//全局控制标志
    /**
     * 获取当前角度
     *
     * @return
     */
    /**
     * 设置身体主轴线方向角度
     *
     * @param mainAngle
     */
    var mainAngle = Random().nextFloat() * 360//角度表示的角
    private var waveFrequence = 1f
    //鱼头点
    /**
     * 设置头的位置
     *
     * @param headPoint
     */
    var headPoint: PointF? = null
    //转弯更自然的中心点
    var middlePoint: PointF? = null
    private var finsAngle = 0f
    private var bodyPaint: Paint? = null
    private var mPath: Path? = null

    init {
        init()
    }

    private fun init() {

        //路径
        mPath = Path()
        //画笔
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.isDither = true//防抖
        mPaint!!.color = Color.argb(OTHER_ALPHA, 244, 92, 71)
        //身体画笔
        bodyPaint = Paint()
        bodyPaint!!.isAntiAlias = true
        bodyPaint!!.style = Paint.Style.FILL
        bodyPaint!!.isDither = true//防抖
        bodyPaint!!.color = Color.argb(OTHER_ALPHA + 5, 244, 92, 71)
        //        middlePoint = new PointF(TOTAL_LENGTH + BODY_LENGHT / 2, TOTAL_LENGTH + BODY_LENGHT / 2);
        middlePoint = PointF(4.18f * HEAD_RADIUS, 4.18f * HEAD_RADIUS)

        //鱼鳍灵动动画
        finsAnimator = ObjectAnimator.ofFloat(this, "finsAngle", 0f, 1f, 0f)
        finsAnimator.repeatMode = ValueAnimator.REVERSE
        finsAnimator.repeatCount = Random().nextInt(3)

        //引擎部分
        val valueAnimator = ValueAnimator.ofInt(0, 540 * 100)
        valueAnimator.duration = (180 * 1000).toLong()
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int
            //				mainAngle = currentValue % 360;
            invalidateSelf()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                finsAnimator.start()
            }
        })
        valueAnimator.start()

    }

    //画身子

    override fun draw(canvas: Canvas) {

        //生成一个半透明图层，否则与背景白色形成干扰,尺寸必须与view的大小一致否则鱼显示不全
        canvas.saveLayerAlpha(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), 240, Canvas.ALL_SAVE_FLAG)
        makeBody(canvas, HEAD_RADIUS)
        canvas.restore()
        mPath!!.reset()
        mPaint!!.color = Color.argb(OTHER_ALPHA, 244, 92, 71)
    }

    /**
     * 主方向是头到尾的方向跟X轴正方向的夹角（顺时针为正）
     * 前进方向和主方向相差180度
     * R + 3.2R
     *
     * @param canvas
     * @param headRadius
     */
    private fun makeBody(canvas: Canvas, headRadius: Float) {
        //sin参数为弧度值
        //现有角度=原始角度+ sin（域值[-1，1]）*可摆动的角度   sin作用是控制周期摆动
        val angle =
            mainAngle + Math.sin(Math.toRadians(currentValue.toDouble() * 1.2 * waveFrequence.toDouble())).toFloat() * 2//中心轴线加偏移量和X轴顺时针方向夹角
        headPoint = calculatPoint(middlePoint!!, BODY_LENGHT / 2, mainAngle)
        //画头
        canvas.drawCircle(headPoint!!.x, headPoint!!.y, HEAD_RADIUS, mPaint!!)
        //右鳍 起点
        val pointFinsRight = calculatPoint(headPoint!!, headRadius * 0.9f, angle - 110)
        makeFins(canvas, pointFinsRight, FINS_RIGHT, angle)
        //左鳍 起点
        val pointFinsLeft = calculatPoint(headPoint!!, headRadius * 0.9f, angle + 110)
        makeFins(canvas, pointFinsLeft, FINS_LEFT, angle)

        val endPoint = calculatPoint(headPoint!!, BODY_LENGHT, angle - 180)
        //躯干2
        val mainPoint = PointF(endPoint.x, endPoint.y)
        makeSegments(canvas, mainPoint, headRadius * 0.7f, 0.6f, angle)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        val contralLeft: PointF
        val contralRight: PointF
        //point1和4的初始角度决定发髻线的高低值越大越低
        point1 = calculatPoint(headPoint!!, headRadius, angle - 80)
        point2 = calculatPoint(endPoint, headRadius * 0.7f, angle - 90)
        point3 = calculatPoint(endPoint, headRadius * 0.7f, angle + 90)
        point4 = calculatPoint(headPoint!!, headRadius, angle + 80)
        //决定胖瘦
        contralLeft = calculatPoint(headPoint!!, BODY_LENGHT * 0.56f, angle - 130)
        contralRight = calculatPoint(headPoint!!, BODY_LENGHT * 0.56f, angle + 130)
        mPath!!.reset()
        mPath!!.moveTo(point1.x, point1.y)
        mPath!!.quadTo(contralLeft.x, contralLeft.y, point2.x, point2.y)
        mPath!!.lineTo(point3.x, point3.y)
        mPath!!.quadTo(contralRight.x, contralRight.y, point4.x, point4.y)
        mPath!!.lineTo(point1.x, point1.y)

        mPaint!!.color = Color.argb(BODY_ALPHA, 244, 92, 71)
        //画最大的身子
        canvas.drawPath(mPath!!, mPaint!!)
    }

    /**
     * 第二节节肢
     * 0.7R * 0.6 =1.12R
     *
     * @param canvas
     * @param mainPoint
     * @param segmentRadius
     * @param MP            梯形上边下边长度比
     */
    private fun makeSegments(canvas: Canvas, mainPoint: PointF, segmentRadius: Float, MP: Float, fatherAngle: Float) {
        val angle =
            fatherAngle + Math.cos(Math.toRadians(currentValue.toDouble() * 1.5 * waveFrequence.toDouble())).toFloat() * 15//中心轴线和X轴顺时针方向夹角
        //身长
        val segementLenght = segmentRadius * (MP + 1)
        val endPoint = calculatPoint(mainPoint, segementLenght, angle - 180)

        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(mainPoint, segmentRadius, angle - 90)
        point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90)
        point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90)
        point4 = calculatPoint(mainPoint, segmentRadius, angle + 90)

        canvas.drawCircle(mainPoint.x, mainPoint.y, segmentRadius, mPaint!!)
        canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint!!)
        mPath!!.reset()
        mPath!!.moveTo(point1.x, point1.y)
        mPath!!.lineTo(point2.x, point2.y)
        mPath!!.lineTo(point3.x, point3.y)
        mPath!!.lineTo(point4.x, point4.y)
        canvas.drawPath(mPath!!, mPaint!!)

        //躯干2
        val mainPoint2 = PointF(endPoint.x, endPoint.y)
        makeSegmentsLong(canvas, mainPoint2, segmentRadius * 0.6f, 0.4f, angle)
    }

    /**
     * 第三节节肢
     * 0.7R * 0.6 * (0.4 + 2.7) + 0.7R * 0.6 * 0.4=1.302R + 0.168R
     *
     * @param canvas
     * @param mainPoint
     * @param segmentRadius
     * @param MP            梯形上边下边长度比
     */
    private fun makeSegmentsLong(
        canvas: Canvas,
        mainPoint: PointF,
        segmentRadius: Float,
        MP: Float,
        fatherAngle: Float
    ) {
        val angle =
            fatherAngle + Math.sin(Math.toRadians(currentValue.toDouble() * 1.5 * waveFrequence.toDouble())).toFloat() * 35//中心轴线和X轴顺时针方向夹角
        //身长
        val segementLenght = segmentRadius * (MP + 2.7f)
        val endPoint = calculatPoint(mainPoint, segementLenght, angle - 180)

        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(mainPoint, segmentRadius, angle - 90)
        point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90)
        point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90)
        point4 = calculatPoint(mainPoint, segmentRadius, angle + 90)

        makeTail(canvas, mainPoint, segementLenght, segmentRadius, angle)


        canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint!!)
        mPath!!.reset()
        mPath!!.moveTo(point1.x, point1.y)
        mPath!!.lineTo(point2.x, point2.y)
        mPath!!.lineTo(point3.x, point3.y)
        mPath!!.lineTo(point4.x, point4.y)
        canvas.drawPath(mPath!!, mPaint!!)
    }

    /**
     * 鱼鳍
     *
     * @param canvas
     * @param startPoint
     * @param type
     */
    private fun makeFins(canvas: Canvas, startPoint: PointF, type: Int, fatherAngle: Float) {
        val contralAngle = 115f//鱼鳍三角控制角度
        mPath!!.reset()
        mPath!!.moveTo(startPoint.x, startPoint.y)
        val endPoint = calculatPoint(
            startPoint,
            FINS_LENGTH,
            if (type == FINS_RIGHT) fatherAngle - finsAngle - 180f else fatherAngle + finsAngle + 180f
        )
        val contralPoint = calculatPoint(
            startPoint, FINS_LENGTH * 1.8f, if (type == FINS_RIGHT)
                fatherAngle - contralAngle - finsAngle
            else
                fatherAngle + contralAngle + finsAngle
        )
        mPath!!.quadTo(contralPoint.x, contralPoint.y, endPoint.x, endPoint.y)
        mPath!!.lineTo(startPoint.x, startPoint.y)
        mPaint!!.color = Color.argb(FINS_ALPHA, 244, 92, 71)
        canvas.drawPath(mPath!!, mPaint!!)
        mPaint!!.color = Color.argb(OTHER_ALPHA, 244, 92, 71)

    }

    /**
     * 鱼尾及鱼尾张合
     *
     * @param canvas
     * @param mainPoint
     * @param length
     * @param maxWidth
     */
    private fun makeTail(canvas: Canvas, mainPoint: PointF, length: Float, maxWidth: Float, angle: Float) {
        val newWidth =
            Math.abs(Math.sin(Math.toRadians(currentValue.toDouble() * 1.7 * waveFrequence.toDouble())) * maxWidth + HEAD_RADIUS / 5 * 3)
                .toFloat()
        //endPoint为三角形底边中点
        val endPoint = calculatPoint(mainPoint, length, angle - 180)
        val endPoint2 = calculatPoint(mainPoint, length - 10, angle - 180)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(endPoint, newWidth, angle - 90)
        point2 = calculatPoint(endPoint, newWidth, angle + 90)
        point3 = calculatPoint(endPoint2, newWidth - 20, angle - 90)
        point4 = calculatPoint(endPoint2, newWidth - 20, angle + 90)
        //内
        mPath!!.reset()
        mPath!!.moveTo(mainPoint.x, mainPoint.y)
        mPath!!.lineTo(point3.x, point3.y)
        mPath!!.lineTo(point4.x, point4.y)
        mPath!!.lineTo(mainPoint.x, mainPoint.y)
        canvas.drawPath(mPath!!, mPaint!!)
        //外
        mPath!!.reset()
        mPath!!.moveTo(mainPoint.x, mainPoint.y)
        mPath!!.lineTo(point1.x, point1.y)
        mPath!!.lineTo(point2.x, point2.y)
        mPath!!.lineTo(mainPoint.x, mainPoint.y)
        canvas.drawPath(mPath!!, mPaint!!)

    }

    private fun setFinsAngle(currentValue: Float) {
        finsAngle = 45 * currentValue
    }

    fun setWaveFrequence(waveFrequence: Float) {
        this.waveFrequence = waveFrequence
    }

    override fun setAlpha(alpha: Int) {
        mPaint!!.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint!!.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    /**
     * 高度要容得下两个鱼身长度
     * 8.36计算过程 身长6.79减去头顶到中部位置的长度2.6 再乘以2
     *
     * @return
     */
    override fun getIntrinsicHeight(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    override fun getIntrinsicWidth(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    companion object {
        val HEAD_RADIUS = 30f
        val totalLength = 6.79f * HEAD_RADIUS
        protected val BODY_LENGHT = HEAD_RADIUS * 3.2f //第一节身体长度
        private val TAG = "Jcs_Fishsss"
        private val BODY_ALPHA = 220
        private val OTHER_ALPHA = 160
        private val FINS_ALPHA = 100
        private val FINS_LEFT = 1//左鱼鳍
        private val FINS_RIGHT = -1
        private val FINS_LENGTH = HEAD_RADIUS * 1.3f

        /**
         * 输入起点、长度、旋转角度计算终点
         * @param startPoint 起点
         * @param length 长度
         * @param angle 旋转角度
         * @return 计算结果点
         */
        private fun calculatPoint(startPoint: PointF, length: Float, angle: Float): PointF {
            val deltaX = Math.cos(Math.toRadians(angle.toDouble())).toFloat() * length
            //符合Android坐标的y轴朝下的标准
            val deltaY = Math.sin(Math.toRadians((angle - 180).toDouble())).toFloat() * length
            return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
        }
    }
}