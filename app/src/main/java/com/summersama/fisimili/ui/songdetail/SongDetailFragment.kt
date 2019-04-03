package com.summersama.fisimili.ui.songdetail

import android.app.AlertDialog
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

import com.summersama.fisimili.R
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.InjectorUtil
import kotlinx.android.synthetic.main.song_detail_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.noties.markwon.Markwon
import ru.noties.markwon.image.ImagesPlugin
import kotlin.coroutines.CoroutineContext

import android.os.Handler
import android.os.Message
import android.widget.Button
import java.lang.Exception


class SongDetailFragment : Fragment(), CoroutineScope {
    private var notice = false
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        timeUpdateThread.interrupt()
        seekBarHandler.removeMessages(1)
        mediaPlayer.release()
        job.cancel()
    }

    var url: String = ""
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    companion object {
        fun newInstance() = SongDetailFragment()
    }

    private lateinit var viewModel: SongDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.song_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.activity!!, InjectorUtil.getSongDetailModelFactory())
            .get(SongDetailViewModel::class.java)
        randomWaterBallAnimation();
        observe()
        timeUpdateThread.start()
        initData()

    }

    private fun observe() {
        viewModel.path.observe(this, Observer {
            url = it
            Log.d(this.javaClass.canonicalName, it + " pathOnChange")
            asd_play_btn.visibility = Button.VISIBLE
        })

    }

    private var total = 0
    private var seekBarHandler = Handler() {


        //  super.handleMessage(it);

        if (it.what == 1) {
            Log.d(
                "checkThread",
                "mediaPlayer isPlaying:${mediaPlayer.isPlaying} and total=${total} and current = ${mediaPlayer.currentPosition}"
            )
            if (mediaPlayer.isPlaying) {
                total = mediaPlayer.duration
                sdf_progress_sk.max = total

                sdf_progress_sk.progress = (mediaPlayer.currentPosition)
                val pt = Math.round((mediaPlayer.currentPosition / 1000).toDouble())
                val str = String.format(
                    "%02d:%02d", pt / 60,
                    pt % 60
                )
                sdf_start_tx.text = str
            } else {

                if (total != 0 && Math.abs(mediaPlayer.currentPosition - total) < 100) {
                    asd_play_btn.setBackgroundResource(R.drawable.play)
                }
            }

        }
        true

    }

    private val timeUpdateThread = Thread() {
        kotlin.run {
            while (true) {
                try {
                    Thread.sleep(100)
                    val message = Message()
                    message.what = 1
                    seekBarHandler.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }
    }

    private fun initData() {
        //   val searchVM = activity?.run { ViewModelProviders.of(this).get(SearchViewModel::class.java) }
        val bundle = arguments
        val isUrl = bundle!!.getString("url")
        var iss = IssuesInfo()

        launch {
            iss = viewModel.getIssues(isUrl)// searchVM!!.issues.value!![position]
            Glide.with(FApplication.context).load(iss.user.avatar_url).into(asd_uppic_iv)

            asd_upload_tx.text = iss.user.login
            val markwon = Markwon.builder(FApplication.context)
                .usePlugin(ImagesPlugin.create(FApplication.context)).build()
            markwon.setMarkdown(asd_body_tx, iss.body)
            //
            getMusicDownLoadPath(iss)
        }



        asd_play_btn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                if (!notice) {
                    val alertDialogBuilder = AlertDialog.Builder(activity)
                    alertDialogBuilder.setTitle("注意")
                    alertDialogBuilder.setMessage("播放将消耗大量流量，请在有WiFi连接下使用!")
                    alertDialogBuilder.setPositiveButton(
                        "确定"
                    ) { dialog, _ ->
                        dialog.dismiss()
                        if (url != "") {

                            Log.d("mp play", url)
                            mediaPlayer.reset()
                            mediaPlayer.setDataSource(url)
                            //  mediaPlayer.prepare()
                            // 异步装载
                            mediaPlayer.prepareAsync()
                            mediaPlayer.setOnPreparedListener {
                                mediaPlayer.start()
                                val totalTime = Math.round((mediaPlayer.getDuration() / 1000).toDouble())
                                val str = String.format(
                                    "%02d:%02d", totalTime / 60,
                                    totalTime % 60
                                )
                                sdf_end_tx.text = str

                                // seekBarHandler.postDelayed(timeUpdateThread, 1000)
                            }
                            //mediaPlayer.start()
                            mediaPlayer.setOnErrorListener { a, b, c ->
                                if (a != null && a.isPlaying) {
                                    a.seekTo(0);
                                }
                                false
                            }
                            asd_play_btn.setBackgroundResource(R.drawable.pause)
                        } else {
                            Toast.makeText(FApplication.context, "未找到该歌曲", Toast.LENGTH_SHORT).show()
                        }
                    };
                    alertDialogBuilder.setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    };
                    alertDialogBuilder.create().show()
                    notice = true
                } else {
                    if (url != "") {
                        Log.d("mp play", url)
                        /* mediaPlayer.reset()
                         mediaPlayer.setDataSource(url)
                         mediaPlayer.prepareAsync()
                         mediaPlayer.setOnPreparedListener {
                             mediaPlayer.start()
                         }*/
                        mediaPlayer.start()
                        asd_play_btn.setBackgroundResource(R.drawable.pause)
                    } else {
                        Toast.makeText(FApplication.context, "未找到该歌曲", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                mediaPlayer.pause()
                asd_play_btn.setBackgroundResource(R.drawable.play)
            }

        }

    }

    private suspend fun getMusicDownLoadPath(iss: IssuesInfo) {

        var key = iss.title
        key = key.replace("_", " ").replace("-", " ")
        Log.d("getMusicDownLoadPath:", key);
        viewModel.getPath(key)
    }

    private fun randomWaterBallAnimation() {

        val mAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.3f
        )
        mAnimation.duration = 25000
        mAnimation.repeatCount = -1
        mAnimation.repeatMode = Animation.REVERSE
        mAnimation.interpolator = LinearInterpolator()
        water_ball.animation = mAnimation


        val mAnimation2 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.7f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation2.duration = 30000
        mAnimation2.repeatCount = -1
        mAnimation2.repeatMode = Animation.REVERSE
        mAnimation2.interpolator = LinearInterpolator()
        water_ball_2.animation = mAnimation2


        val mAnimation3 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f
        )
        mAnimation3.duration = 25000
        mAnimation3.repeatCount = -1
        mAnimation3.repeatMode = Animation.REVERSE
        mAnimation3.interpolator = LinearInterpolator()
        water_ball_3.animation = mAnimation3


        val mAnimation4 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation4.duration = 45000
        mAnimation4.repeatCount = -1
        mAnimation4.repeatMode = Animation.REVERSE
        mAnimation4.interpolator = LinearInterpolator()
        water_ball_4.animation = mAnimation4


        val mAnimation5 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.9f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f
        )
        mAnimation5.duration = 20000
        mAnimation5.repeatCount = -1
        mAnimation5.repeatMode = Animation.REVERSE
        mAnimation5.interpolator = LinearInterpolator()
        water_ball_5.animation = mAnimation5


        val mAnimation6 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation6.duration = 35000
        mAnimation6.repeatCount = -1
        mAnimation6.repeatMode = Animation.REVERSE
        mAnimation6.interpolator = LinearInterpolator()
        water_ball_6.animation = mAnimation6


        val mAnimation7 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.6f
        )
        mAnimation7.duration = 28000
        mAnimation7.repeatCount = -1
        mAnimation7.repeatMode = Animation.REVERSE
        mAnimation7.interpolator = LinearInterpolator()
        water_ball_7.animation = mAnimation7


        val mAnimation8 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f
        )
        mAnimation8.duration = 21000
        mAnimation8.repeatCount = -1
        mAnimation8.repeatMode = Animation.REVERSE
        mAnimation8.interpolator = LinearInterpolator()
        water_ball_8.animation = mAnimation8


        val mAnimation10 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation10.duration = 25000
        mAnimation10.repeatCount = -1
        mAnimation10.repeatMode = Animation.REVERSE
        mAnimation10.interpolator = LinearInterpolator()
        water_ball_10.animation = mAnimation10


        val mAnimation9 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation9.duration = 31000
        mAnimation9.repeatCount = -1
        mAnimation9.repeatMode = Animation.REVERSE
        mAnimation9.interpolator = LinearInterpolator()
        water_ball_9.animation = mAnimation9
    }
}
