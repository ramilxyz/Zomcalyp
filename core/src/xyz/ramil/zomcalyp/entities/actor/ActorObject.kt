package xyz.ramil.zomcalyp.entities.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.tools.Utils
import xyz.ramil.zomcalyp.tools.Utils.distance
import xyz.ramil.zomcalyp.tools.Utils.multiply
import java.util.*

class ActorObject(actor: Actor) : BaseActor(actor.world), BaseActor.IActor {

    val type: Int
    var nameId: String
    val color: String
    val zgame: ZGame

    var positionMan = Vector2(0f, 0f)
    var currentFrame: TextureRegion? = null

    override fun act(delta: Float) {
        if (!zgame.pause) {
            super.act(delta)
            currentFrame = walkAnimation.getKeyFrame(stateTime, true)
            render()

        } else {
            body.setLinearVelocity(0f, 0f)

            body.angularVelocity = 0f
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {

        super.draw(batch, parentAlpha)
        if (currentFrame != null)
            batch?.draw(currentFrame, body.position.x - 4 / 2f, body.position.y - 4 / 2f, 4 / 2.toFloat(), 4 / 2.toFloat(), 4f, 4f, 1f, 1f, MathUtils.radiansToDegrees * body.angle)
    }

    fun render() {
        if (type == 1) {
            stateTime += Gdx.graphics.deltaTime

            velocity = body.getWorldVector(Vector2(0f, 1f))
            velocity = multiply(max_speed, velocity)
            body.linearVelocity = velocity
            position = body.position
            diff = target!!.sub(position)
            desired_angle = Math.atan2(diff.y.toDouble(), diff.x.toDouble()).toFloat()
            current_angle = (body.angle + Math.PI / 2).toFloat()
            delta = desired_angle - current_angle
            while (delta < -Math.PI) delta += (Math.PI * 2.0f).toFloat()
            while (delta > Math.PI) delta -= (Math.PI * 2.0f).toFloat()
            var angle = 0f
            angle = if (Math.abs(delta) < max_angle / 60.0f) delta * 60.0f else if (delta > 0) max_angle else -max_angle
            body.angularVelocity = angle
            circleCenter = body.linearVelocity
            circleCenter.add(body.position)
            if (collisionBool) {
                t = if (i > 500) t + 5 else t - 5
                a = Utils.angleToVector(Vector2(9f, 0f), (t * Math.PI / 180).toFloat())
            } else {
                a = Utils.angleToVector(Vector2(9f, 0f), (randAngel() * Math.PI / 180).toFloat())
            }
            if (collisionBool) {
                timer += 1f
                if (timer > 30) {
                    collisionBool = false
                    timer = 0f
                }
            }
            a!!.add(a)
            val callback = RayCastCallback { fixture, point, normal, fraction ->
                collision.set(point)
                this@ActorObject.normal.set(normal).add(point)
                collisionBool = true
                0F
            }
            a!!.add(circleCenter)
            target = a
            world.rayCast(callback, circleCenter, a)

        } else {
            stateTime += Gdx.graphics.deltaTime
            val arrayList: ArrayList<Float> = arrayListOf()
            val arrayListPos: ArrayList<Vector2> = arrayListOf()
            val bodies = Array<Body>()
            world.getBodies(bodies)
            for (i in bodies) {

                if (i.userData != null)
                    if (i.userData.equals("p")) {
                        arrayList.add(distance(body.position, i.position))
                        arrayListPos.add(i.position)

                    }
            }

            val index = arrayList.indexOf(Collections.min(arrayList))
            positionMan = arrayListPos[index]
            target = positionMan
            velocity = body.getWorldVector(Vector2(0f, 1f))
            velocity = multiply(max_speed, velocity)
            body.linearVelocity = velocity
            position = body.position
            diff = target!!.sub(position)
            desired_angle = Math.atan2(diff.y.toDouble(), diff.x.toDouble()).toFloat()
            current_angle = (body.angle + Math.PI / 2).toFloat()
            delta = desired_angle - current_angle
            while (delta < -Math.PI) delta += (Math.PI * 2.0f).toFloat()
            while (delta > Math.PI) delta -= (Math.PI * 2.0f).toFloat()
            var angle = 0f
            angle = if (Math.abs(delta) < max_angle / 60.0f) delta * 60.0f else if (delta > 0) max_angle else -max_angle
            body.angularVelocity = angle
            circleCenter = body.linearVelocity
            circleCenter.add(body.position)
        }
    }

    init {
        this.zgame = actor.zgame

        nameId = actor.name
        color = actor.color

        if (actor.type == 1) {
            type = 1
            max_speed = 10.0f
            bdef.position[actor.pos.x] = actor.pos.y
            bdef.type = BodyDef.BodyType.DynamicBody
            body = world.createBody(bdef)
            velocity = body.getWorldVector(Vector2(0f, 1f))
            velocity = multiply(max_speed, velocity)
            body.linearVelocity = velocity
            position = body.position
            diff = target!!.sub(position)
            desired_angle = Math.atan2(diff.y.toDouble(), diff.x.toDouble()).toFloat()
            current_angle = (body.angle + Math.PI / 2).toFloat()
            delta = desired_angle - current_angle
            while (delta < -Math.PI) delta += (Math.PI * 2.0f).toFloat()
            while (delta > Math.PI) delta -= (Math.PI * 2.0f).toFloat()
            var angle = 0f
            angle = if (Math.abs(delta) < max_angle / 60.0f) delta * 60.0f else if (delta > 0) max_angle else -max_angle
            body.angularVelocity = angle
            // define fixture
            val shape = CircleShape()
            shape.radius = 1.5f
            val fdef = FixtureDef()
            fdef.shape = shape
            fdef.density = 0.05f
            fdef.friction = 0f
            body.createFixture(fdef)
            shape.dispose()
            walkSheet = actor.zgame.assetManager.get("actors/m.png", Texture::class.java)
            val tmp = TextureRegion.split(walkSheet,
                    walkSheet.width / FRAME_COLS,
                    walkSheet.height / FRAME_ROWS)
            val walkFrames = arrayOfNulls<TextureRegion>(FRAME_COLS * FRAME_ROWS)
            var index = 0
            for (i in 0 until FRAME_ROWS) {
                for (j in 0 until FRAME_COLS) {
                    walkFrames[index++] = tmp[i][j]
                }
            }
            walkAnimation = Animation<TextureRegion>(0.04f, *walkFrames)
            stateTime = 1f
            target = Vector2(30f, 30f)
            this.world = world
        } else {
            type = 0
            max_speed = 5.0f
            val bdef = BodyDef()
            bdef.position[actor.pos.x] = actor.pos.y
            bdef.type = BodyDef.BodyType.DynamicBody
            body = actor.world.createBody(bdef)
            velocity = body.getWorldVector(Vector2(0f, 1f))
            velocity = multiply(max_speed, velocity)
            body.linearVelocity = velocity
            position = body.position
            diff = target!!.sub(position)
            desired_angle = Math.atan2(diff.y.toDouble(), diff.x.toDouble()).toFloat()
            current_angle = (body.angle + Math.PI / 2).toFloat()
            delta = desired_angle - current_angle
            while (delta < -Math.PI) delta += (Math.PI * 2.0f).toFloat()
            while (delta > Math.PI) delta -= (Math.PI * 2.0f).toFloat()
            var angle = 0f
            angle = if (Math.abs(delta) < max_angle / 60.0f) delta * 60.0f else if (delta > 0) max_angle else -max_angle
            body.angularVelocity = angle

            val shape = CircleShape()
            shape.radius = 1.5f
            val fdef = FixtureDef()
            fdef.shape = shape
            fdef.density = 0.05f
            fdef.friction = 0f
            body.createFixture(fdef)
            shape.dispose()
            walkSheet = actor.zgame.assetManager.get("actors/g.png", Texture::class.java)
            if (color.equals("y"))
                walkSheet = actor.zgame.assetManager.get("actors/y.png", Texture::class.java)
            if (color.equals("r"))
                walkSheet = actor.zgame.assetManager.get("actors/r.png", Texture::class.java)

            val tmp = TextureRegion.split(walkSheet,
                    walkSheet.width / FRAME_COLS,
                    walkSheet.height / FRAME_ROWS)
            val walkFrames = arrayOfNulls<TextureRegion>(FRAME_COLS * FRAME_ROWS)
            var index = 0
            for (i in 0 until FRAME_ROWS) {
                for (j in 0 until FRAME_COLS) {
                    walkFrames[index++] = tmp[i][j]
                }
            }
            walkAnimation = Animation<TextureRegion>(0.08f, *walkFrames)
            stateTime = 1f
        }
    }

    override fun dispose() {
        world.destroyBody(body)
    }

    override fun getName(): String {
        return nameId
    }
}