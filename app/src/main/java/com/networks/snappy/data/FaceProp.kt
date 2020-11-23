package com.networks.snappy.data


import androidx.annotation.DrawableRes
import com.networks.snappy.R

enum class FaceProps constructor(
    @DrawableRes val icon: Int,
    val props:List<CameraProp>
){

    CHRISTMAS (
        R.drawable.christmas_hat,
        listOf(
            CameraProp(CameraProp.Type.HEAD, R.drawable.christmas_hat),
        )
    ),
    PUPPY_FACE (
        R.drawable.puppy_face,
        listOf(
            CameraProp(CameraProp.Type.LEFT_HEAD, R.drawable.puppy_left_ear),
            CameraProp(CameraProp.Type.RIGHT_HEAD, R.drawable.puppy_right_ear),
            CameraProp(CameraProp.Type.NOSE, R.drawable.puppy_nose),
        )
    ),
    NERD (
        R.drawable.glasses,
        listOf(
            CameraProp(CameraProp.Type.EYES, R.drawable.glasses), //use contour position 0 on left eye brow to position 0 on right eye brow
        )
    ),
    PIRATE(
        R.drawable.pirate_hat,
        listOf(
            CameraProp(CameraProp.Type.HEAD, R.drawable.christmas_hat),
            CameraProp(CameraProp.Type.LEFT_EYEBROW, R.drawable.pirate_eye_patch),
            CameraProp(CameraProp.Type.NOSE, R.drawable.moustache),
        )
    ),

}


data class CameraProp(
    val type: Type,
    val propId:Int
){

    enum class Type{
        HEAD,
        LEFT_HEAD,
        RIGHT_HEAD,
        EYES,
        LEFT_EYEBROW,
        NOSE,
    }
}
