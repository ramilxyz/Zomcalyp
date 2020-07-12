package xyz.ramil.zomcalyp.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import xyz.ramil.zomcalyp.ShopDialog
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.entities.car.Car

class GarageDialog internal constructor(var zgame: ZGame) : Dialog("", zgame.skin) {

    val buttonleft: Actor
    val buttonRight: Actor
    val buttonCancel: Actor
    val buttonTuning: Actor
    val buttonSelect:Actor
    val carList: ArrayList<Image> = arrayListOf()
    val selectTable = Table()
    var position = 0
    val label2: Label
    val label:Label



    init {



        val contentTable = contentTable
//        contentTable.debug = true
        val image0 = Image(zgame.assetManager.get("cars/y0.png", Texture::class.java))
        val image1 = Image(zgame.assetManager.get("cars/y1.png", Texture::class.java))
        val image2 = Image(zgame.assetManager.get("cars/b0.png", Texture::class.java))
        val image3 = Image(zgame.assetManager.get("cars/b1.png", Texture::class.java))
        val image4 = Image(zgame.assetManager.get("cars/r0.png", Texture::class.java))

        carList.add(image0)
        carList.add(image1)
        carList.add(image2)
        carList.add(image3)
        carList.add(image4)




        val titleTable = Table()
        label = Label("Super GT Turbo", zgame.skin)
        label.setAlignment(Align.center)
        titleTable.add(label).pad(10f)
        contentTable.add(titleTable)
        contentTable.row()

        val grandSelectTable = Table()
//        grandSelectTable.debug = true

        val tableLeft = Table()
        buttonleft = TextButton("<<", zgame.skin)
        tableLeft.add(buttonleft)
        grandSelectTable.add(tableLeft).pad(20f)

        selectTable.add(carList.get(position))
        grandSelectTable.add(selectTable)

        val tableRight = Table()
        buttonRight = TextButton(">>", zgame.skin)
        tableRight.add(buttonRight)
        grandSelectTable.add(tableRight).pad(20f)

        contentTable.add(grandSelectTable)

        contentTable.row()

        val scrolInfo = Table()
        label2 = Label(""+(position+1)+"/"+(carList.size), zgame.skin)
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
    private fun handleInputButton() {
        buttonleft.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                buttonleft.setColor(buttonleft.color.r, buttonleft.color.g, buttonleft.color.b, 0.7f)
                if(position > 0) {
                    position--
                    label2.setText(""+(position+1)+"/"+(carList.size))
                    selectTable.children[0] = carList[position]
                } else {
                    position = carList.size-1;
                    label2.setText(""+(position+1)+"/"+(carList.size))
                    selectTable.children[0] = carList[position]
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
                if(position < carList.size-1) {
                    position++
                    label2.setText(""+(position+1)+"/"+(carList.size))
                    selectTable.children[0] = carList[position]
                } else {
                    position = 0;
                    label2.setText(""+(position+1)+"/"+(carList.size))
                    selectTable.children[0] = carList[position]
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