/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxgl.animation

import com.almasb.fxgl.core.math.FXGLMath
import javafx.animation.Interpolator
import javafx.animation.PathTransition
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Path
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import kotlin.math.abs
import kotlin.math.min

/**
 * A value that can be animated (progressed) from value1 to value 2.
 * An interpolator can be used to control the rate of animation (progression).
 * Built-in supported types: Point2D, Double, Int, Long, Float.
 * Any other types must implement animate().
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class AnimatedValue<T>(val from: T, val to: T) {

    fun getValue(progress: Double): T {
        return animate(from, to, progress, Interpolator.LINEAR)
    }

    fun getValue(progress: Double, interpolator: Interpolator): T {
        return animate(from, to, progress, interpolator)
    }

    @Suppress("UNCHECKED_CAST")
    open fun animate(val1: T, val2: T, progress: Double, interpolator: Interpolator): T {
        return interpolator.interpolate(val1, val2, progress) as T
    }
}

class AnimatedPoint2D(from: Point2D, to: Point2D)
    : AnimatedValue<Point2D>(from, to) {

    override fun animate(val1: Point2D, val2: Point2D, progress: Double, interpolator: Interpolator): Point2D {
        return interpolate(val1, val2, progress, interpolator)
    }

    private fun interpolate(fromValue: Point2D, toValue: Point2D, progress: Double, interpolator: Interpolator): Point2D {
        val x = interpolator.interpolate(fromValue.x, toValue.x, progress)
        val y = interpolator.interpolate(fromValue.y, toValue.y, progress)

        return Point2D(x, y)
    }
}

class AnimatedQuadBezierPoint2D
(val path: QuadCurve) : AnimatedValue<Point2D>(Point2D.ZERO, Point2D.ZERO) {

    override fun animate(val1: Point2D, val2: Point2D, progress: Double, interpolator: Interpolator): Point2D {
        return FXGLMath.bezier(
                Point2D(path.startX, path.startY),
                Point2D(path.controlX, path.controlY),
                Point2D(path.endX, path.endY),
                interpolator.interpolate(0.0, 1.0, progress)
        )
    }
}

class AnimatedCubicBezierPoint2D
(val path: CubicCurve) : AnimatedValue<Point2D>(Point2D.ZERO, Point2D.ZERO) {

    override fun animate(val1: Point2D, val2: Point2D, progress: Double, interpolator: Interpolator): Point2D {
        return FXGLMath.bezier(
                Point2D(path.startX, path.startY),
                Point2D(path.controlX1, path.controlY1),
                Point2D(path.controlX2, path.controlY2),
                Point2D(path.endX, path.endY),
                interpolator.interpolate(0.0, 1.0, progress)
        )
    }
}

class AnimatedColor(from: Color, to: Color)
    : AnimatedValue<Color>(from, to) {

    override fun animate(val1: Color, val2: Color, progress: Double, interpolator: Interpolator): Color {
        return Color.color(
                interpolator.interpolate(val1.red, val2.red, progress),
                interpolator.interpolate(val1.green, val2.green, progress),
                interpolator.interpolate(val1.blue, val2.blue, progress),
                interpolator.interpolate(val1.opacity, val2.opacity, progress)
        )
    }
}

class AnimatedPath
(val path: Path) : AnimatedValue<Point2D>(Point2D.ZERO, Point2D.ZERO) {

    /**
     * Maps reference time values [0..1] to points on path at that time.
     */
    private val points = hashMapOf<Int, Point2D>()

    init {
        val dummy = Rectangle()
        val pt = PathTransition(Duration.seconds(1.0), path, dummy)
        pt.play()

        var t = 0.0
        var percent = 0

        while (t < 1.0) {
            points[percent++] = Point2D(dummy.translateX, dummy.translateY)

            t += 0.01

            pt.jumpTo(Duration.seconds(t))
        }

        pt.jumpTo(Duration.seconds(1.0))

        points[100] = Point2D(dummy.translateX, dummy.translateY)
    }

    override fun animate(val1: Point2D, val2: Point2D, progress: Double, interpolator: Interpolator): Point2D {
        val t = interpolator.interpolate(0.0, 1.0, progress)

        val key = (t * 100).toInt()

        return points[key]!!
    }
}