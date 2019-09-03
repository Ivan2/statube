package ru.sis.statube.additional

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.Comparator

fun Context.color(colorId: Int): Int = ContextCompat.getColor(this, colorId)

fun Context.drawable(drawableId: Int): Drawable? = ContextCompat.getDrawable(this, drawableId)

fun Context.string(stringId: Int): String = this.getString(stringId)

fun Context.string(stringId: Int, vararg args: Any): String = this.getString(stringId).format(Locale.ENGLISH, *args)

fun Context.pxFromDp(dp: Float): Float = dp * this.resources.displayMetrics.density

fun ViewGroup.inflate(resId: Int, attachToRoot: Boolean = true): View = LayoutInflater.from(this.context).inflate(resId, this, attachToRoot)

fun Context.inflate(resId: Int): View = LayoutInflater.from(this).inflate(resId, null)