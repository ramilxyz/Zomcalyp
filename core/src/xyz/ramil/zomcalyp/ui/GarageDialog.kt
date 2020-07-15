package xyz.ramil.zomcalyp.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.utils.Align
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.entities.car.Car
import xyz.ramil.zomcalyp.entities.car.CarObject
import xyz.ramil.zomcalyp.screens.PlayScreen
import java.nio.file.attribute.UserDefinedFileAttributeView


class GarageDialog internal constructor(var zgame: ZGame, val playScreen: PlayScreen) : Dialog("", zgame.skin) {

    val buttonleft: Actor
    val buttonRight: Actor
    val buttonCancel: Actor
    val buttonTuning: Actor
    val buttonSelect:Actor

    val imageList: ArrayList<Image> = arrayListOf()
    val textureList: ArrayList<Texture> = arrayListOf()
    val carList: ArrayList<Car> = arrayListOf()


    val selectTable = Table()
    var position = 0
    val label2: Label
    val label:Label

    val actionScaleOn:Action
    val actionScaleOff:Action



    init {
        actionScaleOn = Actions.scaleBy(0f, 1f, 1f)
        actionScaleOff = Actions.scaleBy(1f, 1f, 1f)


        val contentTable = contentTable
    //    contentTable.debug = true
        val image0 = Image(zgame.assetManager.get("cars/y0.png", Texture::class.java))
        val image1 = Image(zgame.assetManager.get("cars/y1.png", Texture::class.java))
        val image2 = Image(zgame.assetManager.get("cars/b0.png", Texture::class.java))
        val image3 = Image(zgame.assetManager.get("cars/b1.png", Texture::class.java))
        val image4 = Image(zgame.assetManager.get("cars/r0.png", Texture::class.java))

        imageList.add(image0)
        imageList.add(image1)
        imageList.add(image2)
        imageList.add(image3)
        imageList.add(image4)

        for(actor in imageList) {
            actorRotation(actor)
        }

        val t0 = zgame.assetManager.get("cars/y0.png", Texture::class.java)
        val t1 = zgame.assetManager.get("cars/y1.png", Texture::class.java)
        val t2 = zgame.assetManager.get("cars/b0.png", Texture::class.java)
        val t3 = zgame.assetManager.get("cars/b1.png", Texture::class.java)
        val t4 = zgame.assetManager.get("cars/r0.png", Texture::class.java)

        textureList.add(t0)
        textureList.add(t1)
        textureList.add(t2)
        textureList.add(t3)
        textureList.add(t4)


        val titleTable = Table()
        label = Label("Super GT Turbo(Available from level 5)", zgame.skin)
        label.setAlignment(Align.center)
        titleTable.add(label).pad(10f)
        contentTable.add(titleTable)
        contentTable.row()

        val grandSelectTable = Table()
     //   grandSelectTable.debug = true

        val tableLeft = Table()
        buttonleft = TextButton("<<", zgame.skin)
        tableLeft.add(buttonleft)
        grandSelectTable.add(tableLeft).pad(20f)

        selectTable.add(imageList.get(position))
        grandSelectTable.add(selectTable).padLeft(30f).padRight(30f)

        val tableRight = Table()
        buttonRight = TextButton(">>", zgame.skin)
        tableRight.add(buttonRight)
        grandSelectTable.add(tableRight).pad(20f)

        contentTable.add(grandSelectTable)

        contentTable.row()

        val scrolInfo = Table()
        label2 = Label(""+(position+1)+"/"+(imageList.size), zgame.skin)
        label2.setAlignment(Align.center)
        scrolInfo.add(label2)
        contentTable.add(scrolInfo)
        contentTable.row()

        val controlTable = Table()
        buttonCancel = TextButton("Cancel", zgame.skin)
        controlTable.add(buttonCancel).padRight(20f)
        buttonSelect = TextButton("Select", zgame.skin)
        controlTable.add(buttonSelect).padRight(20f).padLeft(20f)
        buttonTuning = TextButton("Tuning", zgame.skin)
        controlTable.add(buttonTuning).padLeft(20f)
        contentTable.add(controlTable)

        buttonleft.setColor(buttonleft.color.r, buttonleft.color.g, buttonleft.color.b, 0.7f)
        buttonRight.setColor(buttonRight.color.r, buttonRight.color.g, buttonRight.color.b, 0.7f)
        buttonTuning.setColor(buttonTuning.color.r, buttonTuning.color.g, buttonTuning.color.b, 0.7f)
        buttonSelect.setColor(buttonSelect.color.r, buttonSelect.color.g, buttonSelect.color.b, 0.7f)
        buttonCancel.setColor(buttonCancel.color.r, buttonCancel.color.g, buttonCancel.color.b, 0.7f)



        handleInputButton()
    }


    private fun actorRotation(actor: Actor) {
        actor.addListener(object : DragListener() {
            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                actor.setOrigin(actor.width/2, actor.height/2)
                val touchDegree = Vector2(x, y).sub(Vector2(actor.getOriginX(), actor.getOriginY())).angle()
                actor.rotateBy (touchDegree - Vector2(x, y).angle())
                Gdx.app.log("X"+actor.originX, "Y"+actor.originY)
            }
        })
    }

    private fun handleInputButton() {
        buttonleft.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonleft.setColor(buttonleft.color.r, buttonleft.color.g, buttonleft.color.b, 0.7f)
                if(position > 0) {
                    position--
                    label2.setText(""+(position+1)+"/"+(imageList.size))
                    selectTable.children[0].remove()
                    selectTable.add(imageList[position])

                } else {
                    position = imageList.size-1;
                    label2.setText(""+(position+1)+"/"+(imageList.size))
                    selectTable.children[0].remove()
                    selectTable.add(imageList[position])

                }
            }
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonleft.setColor(buttonleft.color.r, buttonleft.color.g, buttonleft.color.b, 0.5f)
                return true
            }
        })

        buttonRight.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonRight.setColor(buttonRight.color.r, buttonRight.color.g, buttonRight.color.b, 0.7f)
                if(position < imageList.size-1) {
                    position++
                    label2.setText(""+(position+1)+"/"+(imageList.size))
                    selectTable.children[0].remove()
                    selectTable.add(imageList[position])
                } else {
                    position = 0;
                    label2.setText(""+(position+1)+"/"+(imageList.size))
                    selectTable.children[0].remove()
                    selectTable.add(imageList[position])
                }
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonRight.setColor(buttonRight.color.r, buttonRight.color.g, buttonRight.color.b, 0.5f)
                return true
            }
        })

        buttonTuning.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonTuning.setColor(buttonTuning.color.r, buttonTuning.color.g, buttonTuning.color.b, 0.7f)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonTuning.setColor(buttonTuning.color.r, buttonTuning.color.g, buttonTuning.color.b, 0.5f)
                return true
            }
        })

        buttonSelect.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonSelect.setColor(buttonSelect.color.r, buttonSelect.color.g, buttonSelect.color.b, 0.7f)
                playScreen.userControl.mPlayer.setSprite(Sprite( textureList[position]))
                super@GarageDialog.remove()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonSelect.setColor(buttonSelect.color.r, buttonSelect.color.g, buttonSelect.color.b, 0.5f)
                return true
            }
        })

        buttonCancel.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonCancel.setColor(buttonCancel.color.r, buttonCancel.color.g, buttonCancel.color.b, 0.7f)
                super@GarageDialog.remove()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonCancel.setColor(buttonCancel.color.r, buttonCancel.color.g, buttonCancel.color.b, 0.5f)
                return true
            }
        })
    }
}