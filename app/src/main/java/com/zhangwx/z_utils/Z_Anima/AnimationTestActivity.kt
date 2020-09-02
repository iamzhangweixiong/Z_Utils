package com.zhangwx.z_utils.Z_Anima

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_anima.*

/**
 * https://juejin.im/post/5c8e5c1e6fb9a070b24b067f
 */
class AnimationTestActivity : AppCompatActivity() {

    companion object {
        const val tag = "AnimationTestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anima)

        testAnimator()
        testAnimation()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun testAnimator() {

        // valueAnimator
        val valueAnimator = ValueAnimator
                .ofFloat(0f, 1f)
                .setDuration(1000)
        valueAnimator.repeatCount = 10
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener {
            valueAnimatorTextView.alpha = it.animatedValue as Float
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        valueAnimator.start()


        // objAnimator
        val objAnimator = ObjectAnimator
                .ofFloat(objAnimatorTextView, "rotation", 0.0f, 180f, 0.0f)
        objAnimator.duration = 1000
        objAnimator.interpolator = DecelerateInterpolator()
        objAnimator.start()

        // timeAnimator
        val timeAnimator = TimeAnimator()
        timeAnimator.setTimeListener { animation, totalTime, deltaTime ->
            Log.e(tag, "totalTime = $totalTime    deltaTime = $deltaTime    animation = $animation")
        }
        timeAnimator.start()

//        val animatorSet = AnimatorSet()
//        animatorSet.play(valueAnimator)
//                .with(timeAnimator)
//        animatorSet.start()
    }


    private fun testAnimation() {

        val scaleAnimation = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f)
        scaleAnimation.interpolator = LinearInterpolator()
        scaleAnimation.duration = 1000
        scaleAnimation.repeatMode = Animation.REVERSE
        scaleAnimation.repeatCount = 10
        scaleAnimationTextView.startAnimation(scaleAnimation)

//        val alphaAnimation = AlphaAnimation()
//
//        val translateAnimation = TranslateAnimation()
//
//        val rotateAnimation = RotateAnimation()
//
//        val animationSet = AnimationSet()
    }
}