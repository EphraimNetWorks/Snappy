package com.networks.snappy

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.networks.snappy.data.CameraProp
import com.networks.snappy.data.FaceProps


class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // The detected face
    var faces: List<Face>? = null
        set(value) {
            field = value

            // Trigger redraw when a new detected face object is passed in
            postInvalidate()
        }

    // The preview width
    var previewWidth: Int? = null

    // The preview height
    var previewHeight: Int? = null

    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f

    private var cameraPropsBitmapList = listOf<Bitmap>()

    var faceProps: FaceProps = FaceProps.CHRISTMAS
        set(value) {
            cameraPropsBitmapList  = value.props.map {
                BitmapFactory.decodeResource(resources, it.propId)
            }
            field = value

            // Trigger redraw when a new face prop object is passed in
            postInvalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Create local variables here so they canot not be changed anywhere else
        val faces = faces
        faces?.forEach {
            val previewWidth = previewWidth
            val previewHeight = previewHeight

            if (canvas != null && previewWidth != null && previewHeight != null) {

                // Calculate the scale factor
                widthScaleFactor = width.toFloat() / previewWidth.toFloat()
                heightScaleFactor = height.toFloat() / previewHeight.toFloat()

                drawFaceProp(canvas, it)
            }
        }

    }

    private fun drawFaceProp(canvas: Canvas, face:Face){
        when(faceProps){
            FaceProps.CHRISTMAS->drawChristmasProp(canvas, face)
            FaceProps.PIRATE->drawPirateProp(canvas, face)
            FaceProps.PUPPY_FACE->drawPuppyProp(canvas, face)
            FaceProps.NERD->drawNerdProp(canvas, face)
        }
    }

    /***
     * Draw christmas hat on the head
     */
    private fun drawChristmasProp(canvas: Canvas, face: Face) {
        face.getContour(FaceContour.FACE)?.let {

            val hatBottom = it.points[0].y
            val hatStart = face.boundingBox.left
            val hatEnd = face.boundingBox.right

            val rectHeight = (cameraPropsBitmapList[0].height/cameraPropsBitmapList[0].width)*(hatEnd-hatStart)
            val hatTop = hatBottom - rectHeight

            val hatRect = Rect(
                translateX(hatStart.toFloat()).toInt(),
                translateY(hatTop).toInt(),
                translateX(hatEnd.toFloat()).toInt(),
                translateY(hatBottom).toInt()
            )
            canvas.save()
            canvas.rotate(face.headEulerAngleY)

            canvas.drawBitmap(cameraPropsBitmapList[0], null, hatRect, null)
            canvas.restore()
        }

    }


    /***
     * Draw puppy ears and puppy nose
     */
    private fun drawPuppyProp(canvas: Canvas, face: Face) {

        faceProps.props.forEachIndexed { index, prop->
            when(prop.type){
                CameraProp.Type.LEFT_HEAD->{
                    face.getContour(FaceContour.FACE)?.let {
                        val puppyLeftEarBottom = it.points[32].y
                        val puppyLeftEarStart = face.boundingBox.left
                        val puppyLeftEarEnd = it.points[32].x

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(puppyLeftEarEnd-puppyLeftEarStart)
                        val puppyLeftEarTop = puppyLeftEarBottom - rectHeight

                        val puppyLeftEarRect = Rect(
                            translateX(puppyLeftEarStart.toFloat()).toInt(),
                            translateY(puppyLeftEarTop).toInt(),
                            translateX(puppyLeftEarEnd).toInt(),
                            translateY(puppyLeftEarBottom).toInt()
                        )
                        canvas.save()
                        canvas.rotate(face.headEulerAngleY)

                        canvas.drawBitmap(cameraPropsBitmapList[0], null, puppyLeftEarRect, null)
                        canvas.restore()
                    }
                }
                CameraProp.Type.RIGHT_HEAD->{
                    face.getContour(FaceContour.FACE)?.let {
                        val puppyRightEarBottom = it.points[3].y
                        val puppyRightEarStart = it.points[3].x
                        val puppyRightEarEnd = face.boundingBox.right

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(puppyRightEarEnd-puppyRightEarStart)
                        val puppyRightEarTop = puppyRightEarBottom - rectHeight

                        val puppyRightEarRect = Rect(
                            translateX(puppyRightEarStart).toInt(),
                            translateY(puppyRightEarTop).toInt(),
                            translateX(puppyRightEarEnd.toFloat()).toInt(),
                            translateY(puppyRightEarBottom).toInt()
                        )
                        canvas.save()
                        canvas.rotate(face.headEulerAngleY)

                        canvas.drawBitmap(cameraPropsBitmapList[0], null, puppyRightEarRect, null)
                        canvas.restore()
                    }
                }
                CameraProp.Type.NOSE->{
                    face.getContour(FaceContour.NOSE_BOTTOM)?.let {
                        val puppyNoseBottom = it.points[1].y
                        val puppyNoseStart = it.points[0].x
                        val puppyNoseEnd = it.points[2].x

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(puppyNoseEnd-puppyNoseStart)
                        val puppyNoseTop = puppyNoseBottom - rectHeight

                        val puppyNoseRect = Rect(
                            translateX(puppyNoseStart).toInt(),
                            translateY(puppyNoseTop).toInt(),
                            translateX(puppyNoseEnd).toInt(),
                            translateY(puppyNoseBottom).toInt()
                        )

                        drawBitmapOnCanvas(canvas, cameraPropsBitmapList[index],puppyNoseRect, face.headEulerAngleY)
                    }
                }
                else->{}
            }
        }

    }



    /***
     * Draw glasses on top of eyes
     */
    private fun drawNerdProp(canvas: Canvas, face: Face) {

        val leftEyebrow = face.getContour(FaceContour.LEFT_EYEBROW_TOP)
        val rightEyebrow = face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)

        if(leftEyebrow!= null && rightEyebrow !=null){

            val glassesTop = leftEyebrow.points[0].y
            val glassesStart = face.boundingBox.left
            val glassesEnd = face.boundingBox.right
            val rectHeight = (cameraPropsBitmapList[0].height/cameraPropsBitmapList[0].width)*(glassesEnd-glassesStart)

            val glassesBottom = glassesTop + rectHeight

            val glassesRect = Rect(
                translateX(glassesStart.toFloat()).toInt(),
                translateY(glassesTop).toInt(),
                translateX(glassesEnd.toFloat()).toInt(),
                translateY(glassesBottom).toInt()
            )


            drawBitmapOnCanvas(canvas, cameraPropsBitmapList[0],glassesRect, face.headEulerAngleY)
        }
    }


    /**
     * Draw pirate hat on the head, an eye patch on left eye and a moustache above the mouth
     */
    private fun drawPirateProp(canvas: Canvas, face: Face) {
        faceProps.props.forEachIndexed { index, prop->
            when(prop.type){
                CameraProp.Type.HEAD->{
                    face.getContour(FaceContour.FACE)?.let {
                        val pirateHatBottom = it.points[0].y
                        val pirateHatStart = face.boundingBox.left
                        val pirateHatEnd = face.boundingBox.right

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(pirateHatEnd-pirateHatStart)
                        val pirateHatTop = pirateHatBottom - rectHeight

                        val pirateHatRect = Rect(
                            translateX(pirateHatStart.toFloat()).toInt(),
                            translateY(pirateHatTop).toInt(),
                            translateX(pirateHatEnd.toFloat()).toInt(),
                            translateY(pirateHatBottom).toInt()
                        )

                        drawBitmapOnCanvas(canvas, cameraPropsBitmapList[index],pirateHatRect, face.headEulerAngleY)
                    }
                }
                CameraProp.Type.LEFT_EYEBROW->{
                    face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)?.let {
                        val pirateEyePatchTop = it.points[0].y
                        val pirateEyePatchStart = it.points[0].x
                        val pirateEyePatchEnd = it.points[4].x

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(pirateEyePatchEnd-pirateEyePatchStart)
                        val pirateEyePatchBottom = pirateEyePatchTop + rectHeight

                        val pirateEyePatchRect = Rect(
                            translateX(pirateEyePatchStart).toInt(),
                            translateY(pirateEyePatchTop).toInt(),
                            translateX(pirateEyePatchEnd).toInt(),
                            translateY(pirateEyePatchBottom).toInt()
                        )

                        drawBitmapOnCanvas(canvas, cameraPropsBitmapList[index],pirateEyePatchRect, face.headEulerAngleY)
                    }
                }
                CameraProp.Type.NOSE->{
                    face.getContour(FaceContour.NOSE_BOTTOM)?.let {
                        val moustacheTop = it.points[1].y
                        val moustacheStart = it.points[0].x
                        val moustacheEnd = it.points[2].x

                        val rectHeight = (cameraPropsBitmapList[index].height/cameraPropsBitmapList[index].width)*(moustacheEnd-moustacheStart)
                        val moustacheBottom = moustacheTop + rectHeight

                        val moustacheRect = Rect(
                            translateX(moustacheStart).toInt(),
                            translateY(moustacheTop).toInt(),
                            translateX(moustacheEnd).toInt(),
                            translateY(moustacheBottom).toInt()
                        )

                        drawBitmapOnCanvas(canvas, cameraPropsBitmapList[index],moustacheRect, face.headEulerAngleY)
                    }
                }
                else->{}
            }
        }
    }

    private fun drawBitmapOnCanvas(canvas: Canvas, bitmap: Bitmap, rect:Rect, rotation:Float){
        canvas.save()
        canvas.rotate(rotation)

        canvas.drawBitmap(bitmap, null, rect, null)
        canvas.restore()
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateX(x: Float): Float {
        return width - scaleX(x)
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateY(y: Float): Float {
        return scaleY(y)
    }

    /** Adjusts a vertical value of the supplied value from the preview scale to the view scale. */
    private fun scaleX(x: Float): Float {
        return x * widthScaleFactor
    }


    /** Adjusts a vertical value of the supplied value from the preview scale to the view scale. */
    private fun scaleY(y: Float): Float {
        return y * heightScaleFactor
    }
}