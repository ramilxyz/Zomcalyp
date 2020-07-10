package xyz.ramil.zomcalyp.tools

import com.badlogic.gdx.math.Vector2

object Utils {
    fun vectorToAngle(vector: Vector2): Float {
        return Math.atan2(-vector.x.toDouble(), vector.y.toDouble()).toFloat()
    }

    fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        outVector.x = (-Math.sin(angle.toDouble())).toFloat()
        outVector.y = Math.cos(angle.toDouble()).toFloat()
        return outVector
    }

    fun distance(object1: Vector2, object2: Vector2): Float {
        return Math.sqrt(Math.pow((object2.x - object1.x).toDouble(), 2.0) + Math.pow((object2.y - object1.y).toDouble(), 2.0)).toFloat()
    }

    fun multiply(a: Float, v: Vector2): Vector2 {
        return Vector2(a * v.x, a * v.y)
    }
}