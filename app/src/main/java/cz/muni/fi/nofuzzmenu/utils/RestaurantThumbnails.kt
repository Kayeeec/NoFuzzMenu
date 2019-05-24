package cz.muni.fi.nofuzzmenu.utils

import cz.muni.fi.nofuzzmenu.R

object RestaurantThumbnails {
    private var counter = 0
    private val thumbDrawables = listOf(
        R.drawable.ic_restaurant_thumb_1,
        R.drawable.ic_restaurant_thumb_2,
        R.drawable.ic_restaurant_thumb_3,
        R.drawable.ic_restaurant_thumb_4
    )

    fun resetCounter() {
        counter = 0
    }

    fun getThumbnailDrawableAndIncrement(): Int {
        counter = (counter % 4) + 1
        return thumbDrawables[counter - 1]
    }

}