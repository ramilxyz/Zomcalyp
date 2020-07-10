package xyz.ramil.zomcalyp.entities.actor

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import xyz.ramil.zomcalyp.ZGame

data class Actor(
        val world: World,
        val name: String,  // name and id
        val pos: Vector2,  // position creat
        val type: Int = 0,  // 0 - zombie 1 - Man
        val color: String = "",
        val zgame: ZGame
)