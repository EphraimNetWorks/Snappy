package com.networks.snappy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_results.*
import java.io.File

class ResultsActivity : AppCompatActivity() {

    private val filePath by lazy {
        intent.getStringExtra(EXTRA_PATH)?:""
    }

    private val isResultImage by lazy {
        intent.getBooleanExtra(EXTRA_TYPE,true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        if (isResultImage){
            Glide.with(results_image_view)
                .load(File(filePath))
                .fitCenter()
                .into(results_image_view)
        }else{
            initializePlayer()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer(){
        results_video_view.setVideoPath(filePath)
        results_video_view.start()
    }

    private fun releasePlayer(){
        results_video_view.stopPlayback()
    }

    private fun pausePlayer(){
        results_video_view.pause()
    }

    companion object{
        private const val EXTRA_PATH = "path"
        private const val EXTRA_TYPE = "type"
        fun newInstance(context: Context, resultsFilePath:String, isImage: Boolean): Intent {
            return Intent(context, ResultsActivity::class.java).apply {
                putExtra(EXTRA_PATH, resultsFilePath)
                putExtra(EXTRA_TYPE, isImage)
            }
        }
    }

}