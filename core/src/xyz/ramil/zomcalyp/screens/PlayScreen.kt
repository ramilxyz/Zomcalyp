package xyz.ramil.zomcalyp.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.ramil.zomcalyp.Constants
import xyz.ramil.zomcalyp.Constants.DEFAULT_ZOOM
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.entities.Blood
import xyz.ramil.zomcalyp.entities.StaticWheel
import xyz.ramil.zomcalyp.entities.actor.ActorObject
import xyz.ramil.zomcalyp.entities.car.CarObject
import xyz.ramil.zomcalyp.tools.MapLoader
import xyz.ramil.zomcalyp.ui.MenuTable
import xyz.ramil.zomcalyp.ui.UserControl
import java.util.*


class PlayScreen internal constructor(private val zgame: ZGame) : Screen {
    private val mBatch: SpriteBatch
    private val mB2dr: Box2DDebugRenderer
    val mCamera: OrthographicCamera
    private val mViewport: Viewport
    val stage: Stage

    val mWorld: World

    private val zombie: Actor

    private val label1: Label
    val mMapLoader: MapLoader
    private val texture: Texture

    private var zombieDeathCount = 0
    private val random = Random()

    var angelDirection = -1
    var floatCameraAngle = 0f
    var effect = -1f

    private var actorList: ArrayList<ActorObject> = arrayListOf()
    private var actorListToRemove: ArrayList<ActorObject> = arrayListOf()
    private var actorListToAdd: ArrayList<ActorObject> = arrayListOf()
    private var actorListCreat: ArrayList<xyz.ramil.zomcalyp.entities.actor.Actor> = arrayListOf()
    private var wheelVecArray: ArrayList<Vector2> = arrayListOf()
    private var tempWheelVecArray: ArrayList<Vector2> = arrayListOf()
    var z = 10
    val userControl: UserControl

    override fun show() {}

    //todo for pc
    private fun handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            userControl.mPlayer.setDriveDirection(CarObject.Companion.DRIVE_DIRECTION_FORWARD)
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            userControl.mPlayer.setDriveDirection(CarObject.Companion.DRIVE_DIRECTION_BACKWARD)
        } else {
            userControl.mPlayer.setDriveDirection(CarObject.Companion.DRIVE_DIRECTION_NONE)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            userControl.mPlayer.setTurnDirection(CarObject.Companion.TURN_DIRECTION_LEFT)
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            userControl.mPlayer.setTurnDirection(CarObject.Companion.TURN_DIRECTION_RIGHT)
        } else {
            userControl.mPlayer.setTurnDirection(CarObject.Companion.TURN_DIRECTION_NONE)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            userControl.playScreen.mCamera.zoom -= PlayScreen.CAMERA_ZOOM
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            userControl.playScreen.mCamera.zoom += PlayScreen.CAMERA_ZOOM
        }
    }

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // handleInput()
        update(delta)
        draw()

        if (actorListCreat.isNotEmpty())
            for (item in actorListCreat) {
                val a = ActorObject(item)
                if (item.name.contains("z"))
                    a.body.userData = "zombie"
                else
                    a.body.userData = "p"
                actorListToAdd.add(a)
                stage.addActor(a)

            }
        actorListCreat.clear()

        if (actorListToRemove.isNotEmpty())
            for (item in actorListToRemove) {

                val image: Actor = Blood(zgame, Vector2(item.position))

                stage.actors.insert(0, image)
// todo blood effect for PC
//                var a: BloodEffects
//                if (item.name.contains("m"))
//                    a = BloodEffects(Vector2(item.position), "effects/manblood/p", zgame = zgame)
//                else if (item.name.contains("r"))
//                    a = BloodEffects(Vector2(item.position), "effects/zred/p", zgame = zgame)
//                else if (item.name.contains("g"))
//                    a = BloodEffects(Vector2(item.position), "effects/zgreen/p", zgame = zgame)
//                else if (item.name.contains("y"))
//                    a = BloodEffects(Vector2(item.position), "effects/zyellow/p", zgame = zgame)
//                else a = BloodEffects(Vector2(null), "", zgame)
//                a.name = "e"
//                stage.addActor(a)
                item.dispose()
                item.remove()

            }
        actorList.removeAll(actorListToRemove)
        actorListToRemove.clear()

        actorList.addAll(actorListToAdd)
        actorListToAdd.clear()
    }

    private fun draw() {
        mBatch.projectionMatrix = mCamera.combined
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
        //  mB2dr.render(mWorld, mCamera.combined)
    }

    private fun update(delta: Float) {
        mCamera.position[userControl.mPlayer.body?.position] = 0f
        mCamera.update()

        mWorld.step(delta, Constants.VELOCITY_ITERATION, Constants.POSITION_ITERATION)

        mBatch.begin()
        mBatch.draw(texture, 0f, 0f, texture.width * 10 / Constants.PPM, texture.height * 10 / Constants.PPM)
        mBatch.end()

        label1.setText("x " + zombieDeathCount + " " + Gdx.graphics.framesPerSecond)

        /**
         *  dynamic camera
         */
//        mCamera.rotate((getCameraAngle(mCamera) - (mPlayer.body.getAngle() % 360) * MathUtils.radiansToDegrees) + 180);	// dynamic angle camera

        /**
         * floating camera
         */
//        var carAngel =    if((mPlayer.body.angle *180/Math.PI) % 360 < 0) {
//            (mPlayer.body.angle *180/Math.PI) % 360+360
//        } else (mPlayer.body.angle *180/Math.PI) % 360
//        b = carAngel-180
//        if(b < 0) {
//            b = b+360
//        }
//
//
//        if(angelDirection == -1) {
//            d = 0f
//        }
//
//
//        if(b - (getCameraAngle(mCamera)) > 90) {
//            angelDirection = 0
//
//        }
//
//       else if(b - (getCameraAngle(mCamera)) < -90) {
//            angelDirection = 1
//        }
//
//        if(b - (getCameraAngle(mCamera)) < -5||b - (getCameraAngle(mCamera)) > 5) {
//            cameraRotation(delta)
//        } else {
//            angelDirection = -1
//            d = 0f
//        }
    }

    fun cameraRotation(delta: Float) {
        floatCameraAngle += delta / 3
        if (0 - getCameraAngle(mCamera) < 180) {
            if (angelDirection == 1)
                mCamera.rotate(floatCameraAngle) else if (angelDirection == 0) mCamera.rotate(-floatCameraAngle)
        }
        if (0 - getCameraAngle(mCamera) > 180) {
            if (angelDirection == 1)
                mCamera.rotate(-floatCameraAngle) else if (angelDirection == 0) mCamera.rotate(floatCameraAngle)
        }
    }

    fun getCameraAngle(cam: OrthographicCamera): Float {
        return (-Math.atan2(cam.up.x.toDouble(), cam.up.y.toDouble())).toFloat() * MathUtils.radiansToDegrees + 180
    }

    override fun resize(width: Int, height: Int) {
        mViewport.update(width, height)
        stage.viewport.update(width, height, true)
    }

    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        mBatch.dispose()
        mWorld.dispose()
        mB2dr.dispose()
        mMapLoader.dispose()
        stage.dispose()
    }

    fun genWheel() {
        var index = random.nextInt(210)
        if (tempWheelVecArray.isEmpty())
            tempWheelVecArray.add(wheelVecArray[index]) else {
            if (tempWheelVecArray.contains(wheelVecArray[index])) {
                genWheel()
            } else {
                tempWheelVecArray.add(wheelVecArray[index])
                if (209 > index + 11 && 0 < index - 11)
                    if (!tempWheelVecArray.contains(wheelVecArray[index + 9]) && !tempWheelVecArray.contains(wheelVecArray[index - 9])
                            &&
                            !tempWheelVecArray.contains(wheelVecArray[index + 11]) && !tempWheelVecArray.contains(wheelVecArray[index - 11])) {
                        val a = StaticWheel(mWorld, wheelVecArray[index].x, wheelVecArray[index].y, zgame)
                        stage.addActor(a)
                    }
            }
        }
    }

    companion object {
        const val CAMERA_ZOOM = 0.3f
    }

    init {
        mBatch = SpriteBatch()
        mWorld = World(Constants.GRAVITY, true)
        mB2dr = Box2DDebugRenderer()
        mCamera = OrthographicCamera()
        mCamera.zoom = DEFAULT_ZOOM
        mViewport = StretchViewport(Constants.RESOLUTION.x / Constants.PPM, Constants.RESOLUTION.y / Constants.PPM, mCamera)
        stage = Stage(mViewport, mBatch)

        for (i in 0 until 21) {
            for (j in 0 until 10) {
                wheelVecArray.add(Vector2((25 + z * i).toFloat(), (25 + z * j).toFloat()))
            }
        }

        val list: MutableList<String> = mutableListOf()
        list.add("r")
        list.add("g")
        list.add("y")

        val zombieTexture = zgame.assetManager.get("ui/zombie.png", Texture::class.java)

        zombie = Image(zombieTexture)
        zombie.setSize(40f, 40f)
        zombie.name = "zombie_ui"
        zombie.setPosition(10f, 430f)
        zombie.setColor(zombie.color.r, zombie.color.g, zombie.color.b, 0.7f)
        zgame.stage?.addActor(zombie)

        label1 = Label("", zgame.skin)
        label1.setPosition(60f, 450f)
        zgame.stage?.addActor(label1)

        mMapLoader = MapLoader(mWorld, zgame)

        userControl = UserControl(zgame, this)

        texture = zgame.assetManager.get("maps/new_map_gray.png", Texture::class.java)
        Gdx.input.inputProcessor = zgame.stage!!

        for (x in 0 until 20) {
            genWheel()
        }

        mWorld.setContactListener(object : ContactListener {
            override fun endContact(contact: Contact?) {
            }

            override fun beginContact(contact: Contact?) {
                val userDataA = contact?.fixtureA?.body?.userData.toString()
                val userDataB = contact?.fixtureB?.body?.userData.toString()
                if ((userDataA.equals("p") && userDataB == "car") || (userDataA.equals("zombie") && userDataB == "car")) {
                    zombieDeathCount++
                    for (actor in actorList) {
                        if (actor.body.equals(contact?.fixtureA?.body)) {

                            actorListToRemove.add(actor)
                            actorListCreat.add(xyz.ramil.zomcalyp.entities.actor.Actor(
                                    world = mWorld,
                                    name = actor.name,
                                    pos = Vector2(actor.position.x + 10, actor.position.y + 10),
                                    type = actor.type,
                                    color = actor.color,
                                    zgame = zgame
                            ))
                        }
                    }

                } else if ((userDataB.equals("p") && userDataA == "car") || (userDataB.equals("zombie") && userDataA == "car")) {

                    zombieDeathCount++

                    for (actor in actorList) {
                        if (actor.body.equals(contact?.fixtureB?.body)) {
                            actorListToRemove.add(actor)

                            actorListCreat.add(xyz.ramil.zomcalyp.entities.actor.Actor(
                                    world = mWorld,
                                    name = actor.name,
                                    pos = Vector2(actor.position.x + 10, actor.position.y + 10),
                                    type = actor.type,
                                    color = actor.color,
                                    zgame = zgame))
                        }
                    }
                }

            }

            override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

            }

            override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

            }

        })

        MenuTable(zgame, this)

        for (x in 0 until 5) {
            val rc = list[random.nextInt(3)]
            val b = ActorObject(xyz.ramil.zomcalyp.entities.actor.Actor(
                    world = mWorld,
                    name = "z" + rc + x,
                    pos = Vector2(100f + 2 * x, 30f + 2 * x * 3),
                    type = 0,
                    color = rc,
                    zgame = zgame)
            )

            b.body.userData = "zombie"
            stage.addActor(b)
            actorList.add(b)
        }

        for (x in 0 until 5) {

            val a = ActorObject(xyz.ramil.zomcalyp.entities.actor.Actor(
                    world = mWorld,
                    name = "m" + x,
                    pos = Vector2(50f + 2 * x, 50f + 2 * x * 3),
                    type = 1,
                    zgame = zgame)
            )
            a.body.userData = "p"
            stage.addActor(a)
            actorList.add(a)
        }
    }
}