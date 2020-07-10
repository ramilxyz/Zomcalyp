package xyz.ramil.zomcalyp.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.entities.car.Car
import xyz.ramil.zomcalyp.screens.PlayScreen

class UserControl internal constructor(var zgame: ZGame, val playScreen: PlayScreen) {

    val gas: Actor
    var mPlayer: Car
    val brake: Actor
    val right: Actor
    val left: Actor
    val control: ArrayList<Actor>

    init {

        val gasTexture = zgame.assetManager.get("ui/gas.png", Texture::class.java)
        val brakeTexture = zgame.assetManager.get("ui/brake.png", Texture::class.java)
        val rightTexture = zgame.assetManager.get("ui/right.png", Texture::class.java)
        val leftTexture = zgame.assetManager.get("ui/left.png", Texture::class.java)

        gas = Image(gasTexture)
        brake = Image(brakeTexture)
        right = Image(rightTexture)
        left = Image(leftTexture)

        control = ArrayList()
        control.add(gas)
        control.add(brake)
        control.add(right)
        control.add(left)

        gas.setSize(130f, 180f)
        brake.setSize(120f, 120f)
        left.setSize(100f, 100f)
        right.setSize(100f, 100f)

        gas.name = "gas"
        brake.name = "brake"
        right.name = "right"
        left.name = "left"

        gas.setColor(gas.color.r, gas.color.g, gas.color.b, 0.7f)
        brake.setColor(brake.color.r, brake.color.g, brake.color.b, 0.7f)
        left.setColor(left.color.r, left.color.g, left.color.b, 0.7f)
        right.setColor(right.color.r, right.color.g, right.color.b, 0.7f)

        left.setPosition(50f, 50f)
        right.setPosition(200f, 50f)
        brake.setPosition(500f, 50f)
        gas.setPosition(650f, 50f)

        zgame.stage?.addActor(gas)
        zgame.stage?.addActor(brake)
        zgame.stage?.addActor(left)
        zgame.stage?.addActor(right)

        mPlayer = Car(35.0f, 0.8f, 80f, playScreen.mMapLoader, Car.DRIVE_4WD, playScreen.mWorld, control, zgame, playScreen.stage)
        mPlayer.body?.userData = "car"
        playScreen.stage.addActor(mPlayer)

        handleInputButton()
    }

    private fun handleInputButton() {
        gas.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                gas.setColor(gas.color.r, gas.color.g, gas.color.b, 0.7f)
                mPlayer.setDriveDirection(Car.Companion.DRIVE_DIRECTION_NONE)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                gas.setColor(gas.color.r, gas.color.g, gas.color.b, 0.5f)
                mPlayer.setDriveDirection(Car.Companion.DRIVE_DIRECTION_FORWARD)
                return true
            }
        })
        brake.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                brake.setColor(brake.color.r, brake.color.g, brake.color.b, 0.7f)
                mPlayer.setDriveDirection(Car.Companion.DRIVE_DIRECTION_NONE)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                brake.setColor(brake.color.r, brake.color.g, brake.color.b, 0.5f)
                mPlayer.setDriveDirection(Car.Companion.DRIVE_DIRECTION_BACKWARD)
                return true
            }
        })
        left.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                left.setColor(left.color.r, left.color.g, left.color.b, 0.7f)
                mPlayer.setTurnDirection(Car.Companion.TURN_DIRECTION_NONE)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                left.setColor(left.color.r, left.color.g, left.color.b, 0.5f)
                mPlayer.setTurnDirection(Car.Companion.TURN_DIRECTION_LEFT)
                return true
            }
        })
        right.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                right.setColor(left.color.r, right.color.g, right.color.b, 0.7f)
                mPlayer.setTurnDirection(Car.Companion.TURN_DIRECTION_NONE)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                right.setColor(right.color.r, right.color.g, right.color.b, 0.5f)
                mPlayer.setTurnDirection(Car.Companion.TURN_DIRECTION_RIGHT)
                return true
            }
        })
    }


}