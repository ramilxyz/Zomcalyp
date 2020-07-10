package xyz.ramil.zomcalyp.entities.actor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import java.util.*

open class BaseActor(world: World) : Actor() {
    lateinit var body: Body
    val bdef = BodyDef()
    var world: World
    lateinit var walkAnimation: Animation<TextureRegion>
    lateinit var walkSheet: Texture
    var stateTime: Float = 0.0f
    var max_speed = 0.0f
    var max_angle = (4.0f * Math.PI).toFloat()
    var target: Vector2? = Vector2(100f, 300f)
    lateinit var velocity: Vector2
    lateinit var position: Vector2
    lateinit var diff: Vector2
    var circleCenter = Vector2()
    var desired_angle: Float = 0.0f
    var current_angle: Float = 0.0f
    var delta: Float = 0.0f
    var random = Random()
    var a: Vector2? = Vector2(30f, 30f)
    var t = 0f
    var timer = 0f
    var collisionBool = false
    var collision = Vector2()
    var normal = Vector2()
    var i = 0

    init {
        this.world = world
    }

    fun randAngel(): Float {
        i = random.nextInt() * 1000
        t = if (i > 200) {
            t + 7
        } else {
            t - 7
        }
        return t
    }

    interface IActor {
        fun dispose()
        fun getName(): String
    }

    companion object {
        const val FRAME_COLS = 5
        const val FRAME_ROWS = 4
    }
}