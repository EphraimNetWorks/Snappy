package com.networks.snappy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.networks.snappy.data.CameraFilter
import com.networks.snappy.data.FaceProps
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.filter.Filters
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraActivity : AppCompatActivity(), FilterRecyclerAdapter.ViewHolderActions, FacePropRecyclerAdapter.ViewHolderActions {

    private val filterList = listOf(
        CameraFilter("None", Filters.NONE),
        CameraFilter("Auto Fix", Filters.AUTO_FIX),
        CameraFilter("Black and White", Filters.BLACK_AND_WHITE),
        CameraFilter("Documentary", Filters.DOCUMENTARY),
        CameraFilter("Fill Light", Filters.FILL_LIGHT),
        CameraFilter("Sharpness", Filters.SHARPNESS),
        CameraFilter("Temperature", Filters.TEMPERATURE),
        CameraFilter("Vignette", Filters.VIGNETTE),
        CameraFilter("Lomoish", Filters.LOMOISH),
    )

    private val filtersAdapter = FilterRecyclerAdapter(filterList, this)

    private val propsAdapter = FacePropRecyclerAdapter(FaceProps.values().toList(), this)

    private lateinit var processor: FacePropsProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        camera_view.setLifecycleOwner(this)

        face_props_recycler_view.adapter = propsAdapter

        filter_recyclerview.adapter = filtersAdapter

        processor = FacePropsProcessor(this,camera_view,overlay_view)

        processor.startProcessing()

        camera_view.addCameraListener(object : CameraListener(){

            override fun onPictureTaken(result: PictureResult) {

                val fileName = "IMG_${System.currentTimeMillis()}.png"
                val file = File(externalCacheDir.toString() + File.separator + fileName)

                result.toFile(file){
                    if(it != null){
                        ResultsActivity.newInstance(this@CameraActivity, it.absolutePath, false)
                    }else{
                        Toast.makeText(this@CameraActivity, "Unable to process captured image", Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onVideoTaken(result: VideoResult) {
                ResultsActivity.newInstance(this@CameraActivity, result.file.absolutePath, false)

            }
        })

    }

    override fun applyFilter(cameraFilter: CameraFilter) {
        camera_view.filter=  cameraFilter.filter.newInstance()
    }

    override fun applyFaceProp(props: FaceProps) {
        processor.applyFaceProp(props)
    }


}