package xyz.ramil.zomcalyp.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import xyz.ramil.zomcalyp.ShopDialog
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.screens.PlayScreen

class MenuTable internal constructor(private val zgame: ZGame, private val playScreen: PlayScreen) : Table(zgame.skin) {
    private val buttonExit: Button
    private val openShopButton: Button
    private val garageButton: Button
    private val newGameButton: Button
    private val highScoreButton: Button
    private val soundOn: Actor
    private val soundOff: Actor
    private val camera: Actor
    val menu: Actor

    init {

        val menuTexture = zgame.assetManager.get("ui/menu.png", Texture::class.java)
        menu = Image(menuTexture)
        menu.setSize(50f, 50f)
        menu.setOrigin(Align.center)
        menu.name = "menu"
        menu.setColor(menu.color.r, menu.color.g, menu.color.b, 0.7f)
        menu.setPosition(740f, 420f)

        zgame.stage?.addActor(menu)

        val thisTexture = zgame.assetManager.get("ui/menu_frame.png", Texture::class.java)

        val soundOnTexture = zgame.assetManager.get("ui/sound_on.png", Texture::class.java)
        val soundOffTexture = zgame.assetManager.get("ui/sound_off.png", Texture::class.java)
        val cameraTexture = zgame.assetManager.get("ui/camera.png", Texture::class.java)

        buttonExit = TextButton("Exit", zgame.skin)
        soundOn = Image(soundOnTexture)
        soundOff = Image(soundOffTexture)
        if(zgame.dataBase.isSound) {
            soundOff.isVisible = false
        } else {
            soundOn.isVisible = false
        }
        soundOn.setSize(40f, 40f)
        soundOff.setSize(40f, 40f)


        this.setSize(143f, 480f)

        camera = Image(cameraTexture)
        camera.setSize(40f, 40f)

        this.name = "menu_frame"
        buttonExit.name = "button_exit"
        val menuDrawableBackground = TextureRegionDrawable(TextureRegion(thisTexture))
        this.background = menuDrawableBackground
        buttonExit.setPosition(20f, 20f)
        this.addActor(buttonExit)



        if (zgame.purchaseManagerisInitialized) {
            openShopButton = TextButton("Open shop", zgame.skin)
            openShopButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    ShopDialog(zgame).show(zgame.stage)
                }
            })
            openShopButton.setPosition(20f, 80f)
            this.addActor(openShopButton)
        } else {
            openShopButton = TextButton("Shop", zgame.skin)
            openShopButton.setPosition(20f, 80f)
            openShopButton.isDisabled = true
            this.addActor(openShopButton)
        }

        garageButton = TextButton("Garage", zgame.skin)
        garageButton.setPosition(20f, 140f)
        this.addActor(garageButton)

        newGameButton = TextButton("New game", zgame.skin)
        newGameButton.setPosition(20f, 200f)
        this.addActor(newGameButton)

        highScoreButton = TextButton("High score", zgame.skin)
        highScoreButton.setPosition(20f, 260f)
        this.addActor(highScoreButton)

        soundOn.setPosition(80f, 410f)
        this.addActor(soundOn)

        soundOff.setPosition(80f, 410f)
        this.addActor(soundOff)

        camera.setPosition(20f, 410f)
        this.addActor(camera)

        this.setPosition(800f, 0f)

        this.setColor(this.color.r, this.color.g, this.color.b, 0.7f)

        zgame.stage?.addActor(this)

        handleInputButton()
    }

    private fun handleInputButton() {

        buttonExit.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonExit.setColor(buttonExit.color.r, buttonExit.color.g, buttonExit.color.b, 1f)
                Gdx.app.exit()

            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonExit.setColor(buttonExit.color.r, buttonExit.color.g, buttonExit.color.b, 0.5f)


                return true
            }
        })


        openShopButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                openShopButton.setColor(openShopButton.color.r, openShopButton.color.g, openShopButton.color.b, 1f)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                openShopButton.setColor(openShopButton.color.r, openShopButton.color.g, openShopButton.color.b, 0.5f)
                return true
            }
        })

        garageButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                garageButton.setColor(garageButton.color.r, garageButton.color.g, garageButton.color.b, 1f)
              GarageDialog(zgame, playScreen).show(zgame.stage)

            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                garageButton.setColor(garageButton.color.r, garageButton.color.g, garageButton.color.b, 0.5f)
                return true
            }
        })

        newGameButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                newGameButton.setColor(newGameButton.color.r, newGameButton.color.g, newGameButton.color.b, 1f)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                newGameButton.setColor(newGameButton.color.r, newGameButton.color.g, newGameButton.color.b, 0.5f)
                return true
            }
        })

        highScoreButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                highScoreButton.setColor(highScoreButton.color.r, highScoreButton.color.g, highScoreButton.color.b, 1f)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                highScoreButton.setColor(highScoreButton.color.r, highScoreButton.color.g, highScoreButton.color.b, 0.5f)
                return true
            }
        })

        camera.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                camera.setColor(camera.color.r, camera.color.g, camera.color.b, 1f)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                camera.setColor(camera.color.r, camera.color.g, camera.color.b, 0.5f)
                return true
            }
        })

        soundOn.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                soundOn.setColor(soundOn.color.r, soundOn.color.g, soundOn.color.b, 1f)

                zgame.dataBase.setIsSound(false);

                if(zgame.dataBase.isSound) {
                    soundOff.isVisible = false
                    soundOn.isVisible = true
                } else {
                    soundOff.isVisible = true
                    soundOn.isVisible = false
                }



            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                soundOn.setColor(soundOn.color.r, soundOn.color.g, soundOn.color.b, 0.5f)
                return true
            }
        })

        soundOff.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                soundOff.setColor(soundOff.color.r, soundOff.color.g, soundOff.color.b, 1f)

                zgame.dataBase.setIsSound(true);

                if(zgame.dataBase.isSound) {
                    soundOff.isVisible = false
                    soundOn.isVisible = true
                } else {
                    soundOff.isVisible = true
                    soundOn.isVisible = false
                }

            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                soundOff.setColor(soundOff.color.r, soundOff.color.g, soundOff.color.b, 0.5f)
                return true
            }
        })

        menu.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                menu.setColor(menu.color.r, menu.color.g, menu.color.b, 0.7f)

            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                menu.setColor(menu.color.r, menu.color.g, menu.color.b, 0.5f)
                if (menu.x == 740.0f) {
                    zgame.pause = true
                    menu.addAction(Actions.rotateBy(360f, 0.5f, Interpolation.circle))
                    this@MenuTable.addAction(Actions.moveTo(652f, this@MenuTable.y, 0.5f, Interpolation.circle))
                    menu.addAction(Actions.moveTo(595f, menu.y, 0.5f, Interpolation.circle))
                } else if (menu.x == 595.0f) {
                    zgame.pause = false
                    val v = Vector2(0f, 0f)
                    menu.addAction(Actions.rotateBy(360f, 0.5f, Interpolation.circle))
                    this@MenuTable.addAction(Actions.moveTo(800f, this@MenuTable.y, 0.5f, Interpolation.circle))
                    menu.addAction(Actions.moveTo(740f, menu.y, 0.5f, Interpolation.circle))
                }

                return true
            }
        })

    }

}