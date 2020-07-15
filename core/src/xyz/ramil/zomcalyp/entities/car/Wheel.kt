package xyz.ramil.zomcalyp.entities.car

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import xyz.ramil.zomcalyp.tools.ShapeFactory
import xyz.ramil.zomcalyp.tools.Utils
import java.util.*

class Wheel(position: Vector2, size: Vector2, world: World, id: Int, private val mCarObject: CarObject, val isPowered: Boolean, private val control: ArrayList<Actor>) : Actor() {
    var effect: ParticleEffect

    val body: Body?
    private val mId: Int
    private var mForwardSpeed: Vector2? = null
    private var mLateralSpeed: Vector2? = null
    private var mDrift = 1f

    private val sprite = Sprite(Texture("cars/wheel.png"))

    override fun act(delta: Float) {
        super.act(delta)

        val x = body?.position?.x?.minus(sprite.width / 2)
        val y = body?.position?.y?.minus(sprite.height / 2)
        val rotation = body?.angle?.toDouble()?.let { Math.toDegrees(it).toFloat() }
        sprite.setSize(0.8f, 1f)
        sprite.setOrigin(sprite.width / 2, sprite.height / 2)
        sprite.setPosition(x!!, y!!)
        if (rotation != null) {
            sprite.rotation = rotation
        }
        if (control[0].isTouchFocusTarget && control[2].isTouchFocusTarget || control[0].isTouchFocusTarget && control[3].isTouchFocusTarget ||
                control[1].isTouchFocusTarget && control[2].isTouchFocusTarget || control[1].isTouchFocusTarget && control[3].isTouchFocusTarget) {
            effect.setPosition(x + sprite.width / 2, y + sprite.height / 2)
            rotation?.minus(90)?.let { effect.emitters.first().angle.setHigh(it) }
            effect.emitters.first().transparency.setHigh(1f)
        } else {
            effect.emitters.first().transparency.setHigh(0f)
        }

        if (mDrift < 1) {
            mForwardSpeed = forwardVelocity
            mLateralSpeed = lateralVelocity
            if (mLateralSpeed!!.len() < DRIFT_OFFSET && mId > 1) {
                killDrift()
            } else {
                handleDrift()
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        sprite.draw(batch)
        effect.draw(batch, 1 / 60f / 5)
    }

    fun setAngle(angle: Float) {
        mCarObject.body?.angle?.plus(angle * DEGTORAD)?.let { body?.setTransform(body.position, it) }
    }

    fun setDrift(drift: Float) {
        mDrift = drift
    }

    private fun handleDrift() {
        val forwardSpeed = forwardVelocity
        val lateralSpeed = lateralVelocity
        body!!.setLinearVelocity(forwardSpeed.x + lateralSpeed.x * mDrift, forwardSpeed.y + lateralSpeed.y * mDrift)
    }

    private val forwardVelocity: Vector2
        private get() {
            val currentNormal = body!!.getWorldVector(Vector2(0f, 1f))
            val dotProduct = currentNormal.dot(body.linearVelocity)
            return Utils.multiply(dotProduct, currentNormal)
        }

    fun killDrift() {
        body!!.linearVelocity = mForwardSpeed
    }

    private val lateralVelocity: Vector2
        private get() {
            val currentNormal = body!!.getWorldVector(Vector2(1f, 0f))
            val dotProduct = currentNormal.dot(body.linearVelocity)
            return Utils.multiply(dotProduct, currentNormal)
        }

    companion object {
        val DRIFT_OFFSET = 1.0f
        const val UPPER_LEFT = 0
        const val UPPER_RIGHT = 1
        const val DOWN_LEFT = 2
        const val DOWN_RIGHT = 3
        private const val WHEEL_DENSITY = 0.4f
        private const val DEGTORAD = 0.0174532925199432957f
    }

    init {
        mId = id
        body = ShapeFactory.createRectangle(position, size, BodyDef.BodyType.DynamicBody, world, WHEEL_DENSITY, true)

        effect = ParticleEffect()
        effect.load(Gdx.files.internal("effects/wheel"), Gdx.files.internal("effects"))
        effect.scaleEffect(0.05f, 0.1f)
        effect.start()
    }
}