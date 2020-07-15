package xyz.ramil.zomcalyp.entities.car

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.tools.MapLoader
import java.util.ArrayList

data class Car (
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