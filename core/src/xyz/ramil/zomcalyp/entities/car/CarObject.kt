package xyz.ramil.zomcalyp.entities.car

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import xyz.ramil.zomcalyp.Constants
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.tools.MapLoader
import java.util.*

class CarObject(val car: Car) : Actor() {
    private val mAllWheels = Array<Wheel>()
    private val mRevolvingWheels = Array<Wheel>()
    private var mDriveDirection = DRIVE_DIRECTION_NONE
    private var mTurnDirection = TURN_DIRECTION_NONE
    private var mCurrentWheelAngle = 0f
    private var mCurrentMaxSpeed = 0f
    private var sprite = Sprite(car.texture)
    val body: Body?
    private val mId: Int
    val mstage: Stage
    val zgame: ZGame

    init {
        this.body = car.mapLoader.player
        mId = -1
        this.mstage = car.mstage
        this.zgame = car.zgame

    }

    private fun createWheels(world: World, wheelDrive: Int) {
        for (i in 0 until WHEEL_NUMBER) {
            var xOffset = 0f
            var yOffset = 0f
            when (i) {
                Wheel.UPPER_LEFT -> {
                    xOffset = -WHEEL_OFFSET_X
                    yOffset = WHEEL_OFFSET_Y
                }
                Wheel.UPPER_RIGHT -> {
                    xOffset = WHEEL_OFFSET_X
                    yOffset = WHEEL_OFFSET_Y
                }
                Wheel.DOWN_LEFT -> {
                    xOffset = -WHEEL_OFFSET_X
                    yOffset = -WHEEL_OFFSET_Y
                }
                Wheel.DOWN_RIGHT -> {
                    xOffset = WHEEL_OFFSET_X
                    yOffset = -WHEEL_OFFSET_Y
                }
                else -> throw IllegalArgumentException("Wheel number not supported. Create logic for positioning wheel with number $i")
            }
            val powered = wheelDrive == DRIVE_4WD || wheelDrive == DRIVE_2WD && i < 2
            val wheel = Wheel(
                    Vector2(body?.position?.x!! * Constants.PPM + xOffset, body.position.y * Constants.PPM + yOffset),
                    WHEEL_SIZE,
                    world,
                    i,
                    this,
                    powered, car.control)
            if (i < 2) {
                val jointDef = RevoluteJointDef()
                jointDef.initialize(body, wheel.body, wheel.body?.worldCenter)
                jointDef.enableMotor = false
                world.createJoint(jointDef)
            } else {
                val jointDef = PrismaticJointDef()
                jointDef.initialize(body, wheel.body, wheel.body?.worldCenter, Vector2(1f, 0f))
                jointDef.enableLimit = true
                jointDef.upperTranslation = 0f
                jointDef.lowerTranslation = jointDef.upperTranslation
                world.createJoint(jointDef)
            }
            mAllWheels.add(wheel)
            if (i < 2) {
                mRevolvingWheels.add(wheel)
            }
            wheel.setDrift(car.mDrift)
            mstage.addActor(wheel)
        }
    }

    fun setSprite(sprite: Sprite) {
        this.sprite = sprite
    }

    private fun processInput() {
        val baseVector = Vector2(0f, 0f)
        if (mTurnDirection == TURN_DIRECTION_LEFT) {
            if (mCurrentWheelAngle < 0) {
                mCurrentWheelAngle = 0f
            }
            mCurrentWheelAngle = Math.min(WHEEL_TURN_INCREMENT.let { mCurrentWheelAngle += it; mCurrentWheelAngle }, MAX_WHEEL_ANGLE)
        } else if (mTurnDirection == TURN_DIRECTION_RIGHT) {
            if (mCurrentWheelAngle > 0) {
                mCurrentWheelAngle = 0f
            }
            mCurrentWheelAngle = Math.max(WHEEL_TURN_INCREMENT.let { mCurrentWheelAngle -= it; mCurrentWheelAngle }, -MAX_WHEEL_ANGLE)
        } else {
            mCurrentWheelAngle = 0f
        }
        for (wheel in Array.ArrayIterator(mRevolvingWheels)) {
            wheel.setAngle(mCurrentWheelAngle)
        }
        if (mDriveDirection == DRIVE_DIRECTION_FORWARD) {
            baseVector[0f] = car.mAcceleration * FORWARD_POWER
        } else
            if (mDriveDirection == DRIVE_DIRECTION_BACKWARD) {
                if (direction() == DIRECTION_BACKWARD) {
                    baseVector[0f] = -car.mAcceleration * REVERSE_POWER
                } else if (direction() == DIRECTION_FORWARD) {
                    baseVector[0f] = -car.mAcceleration * BREAK_POWER
                } else {
                    baseVector[0f] = -car.mAcceleration
                }
            }
        mCurrentMaxSpeed = car.mRegularMaxSpeed

        if (body?.linearVelocity?.len()!! < mCurrentMaxSpeed) {
            for (wheel in Array.ArrayIterator(mAllWheels)) {
                if (wheel.isPowered) {
                    wheel.body?.applyForceToCenter(wheel.body.getWorldVector(baseVector), true)
                }
            }
        }
    }

    fun setDriveDirection(driveDirection: Int) {
        mDriveDirection = driveDirection
    }

    fun setTurnDirection(turnDirection: Int) {
        mTurnDirection = turnDirection
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val x = body?.position?.x?.minus(sprite.width / 2)
        val y = body?.position?.y?.minus(sprite.height / 2)

        val rotation = Math.toDegrees(body?.angle?.toDouble()!!).toFloat()
        sprite.setSize(4f, 6.3f)
        sprite.setOrigin(sprite.width / 2, sprite.height / 2)
        sprite.setPosition(x!!, y!!)
        sprite.rotation = rotation
        sprite.draw(batch)
    }

    override fun act(delta: Float) {
        if (!zgame.pause) {
            super.act(delta)
            processInput()

        } else {
            body?.setLinearVelocity(0f, 0f)
            body?.angularVelocity = 0f
        }

        //todo эффект пролитого масла
//        if (mDrift < 1) {
//            mForwardSpeed = forwardVelocity
//            mLateralSpeed = lateralVelocity
//            if (mLateralSpeed!!.len() < BodyHolder.DRIFT_OFFSET && mId > 1) {
//                killDrift()
//            } else {
//                handleDrift()
//            }
//        }
    }

    fun direction(): Int {
        val tolerance = 0.2f
        return if (localVelocity.y < -tolerance) {
            DIRECTION_BACKWARD
        } else if (localVelocity.y > tolerance) {
            DIRECTION_FORWARD
        } else {
            DIRECTION_NONE
        }
    }

    private val localVelocity: Vector2
        private get() = body!!.getLocalVector(body.getLinearVelocityFromLocalPoint(Vector2(0f, 0f)))

    companion object {
        val DIRECTION_NONE = 0
        val DIRECTION_FORWARD = 1
        val DIRECTION_BACKWARD = 2
        const val DRIVE_2WD = 0
        const val DRIVE_4WD = 1
        const val DRIVE_DIRECTION_NONE = 0
        const val DRIVE_DIRECTION_FORWARD = 1
        const val DRIVE_DIRECTION_BACKWARD = 2
        const val TURN_DIRECTION_NONE = 0
        const val TURN_DIRECTION_LEFT = 1
        const val TURN_DIRECTION_RIGHT = 2
        private val WHEEL_SIZE = Vector2(16f, 32f)
        private const val LINEAR_DAMPING = 0.5f
        private const val RESTITUTION = 0.2f
        private const val MAX_WHEEL_ANGLE = 20.0f
        private const val WHEEL_TURN_INCREMENT = 1.0f
        private const val WHEEL_OFFSET_X = 64f
        private const val WHEEL_OFFSET_Y = 80f
        private const val WHEEL_NUMBER = 4
        private const val BREAK_POWER = 1.3f
        private const val REVERSE_POWER = 0.5f
        private const val FORWARD_POWER = 1f
    }

    init {
        body?.linearDamping = LINEAR_DAMPING
        body?.fixtureList?.get(0)?.restitution = RESTITUTION
        createWheels(car.world, car.wheelDrive)
    }
}