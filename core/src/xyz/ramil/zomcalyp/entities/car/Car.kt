package xyz.ramil.zomcalyp.entities.car

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.tools.MapLoader
import java.text.FieldPosition
import java.util.ArrayList

data class Car (
        val name: String = "car",
        val texture: Texture,
        val color: Color = Color.CLEAR,
        val position: Vector2 = Vector2(0f, 0f),
        val mRegularMaxSpeed: Float,
        val mDrift: Float,
        val mAcceleration: Float,
        val mapLoader: MapLoader,
        val wheelDrive: Int,
        val world: World,
        val control: ArrayList<Actor>,
        val zgame: ZGame,
        val mstage: Stage
)