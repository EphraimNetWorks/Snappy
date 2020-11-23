package com.networks.snappy

import android.content.Context
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import com.networks.snappy.data.FaceProps
import com.otaliastudios.cameraview.CameraView

class FacePropsProcessor(
    private val context: Context,
    private val cameraView: CameraView,
    private val overlayView: OverlayView
) {

    private val options = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    fun startProcessing(){
        overlayView.previewHeight = cameraView.snapshotMaxHeight
        overlayView.previewWidth = cameraView.snapshotMaxWidth

        cameraView.addFrameProcessor { frame ->
            val rotation = frame.rotationToUser / 90
            if (rotation / 2 == 0) {
                overlayView.previewWidth = frame.size.width
                overlayView.previewHeight = frame.size.height
            } else {
                overlayView.previewWidth = frame.size.height
                overlayView.previewHeight = frame.size.width
            }

            detector.process(InputImage.fromMediaImage(frame.getData(), rotation))
                .addOnSuccessListener {
                    processFaceDetectionResult(it)
                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }

    fun applyFaceProp(prop: FaceProps){

        overlayView.faceProps = prop

    }


    private fun processFaceDetectionResult(result: List<Face>){

        overlayView.faces = result
    }
}