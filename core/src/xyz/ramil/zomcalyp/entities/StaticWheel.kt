package xyz.ramil.zomcalyp.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import xyz.ramil.zomcalyp.ZGame

class StaticWheel(aWorld: World, pos_x: Float, pos_y: Float, zgame: ZGame) : Image(zgame.assetManager.get("entities/wheel.png", Texture::class.java)) {
    private val body: Body
    private val world: World

    override fun act(delta: Float) {
        super.act(delta)
        rotation = body.angle * MathUtils.radiansToDegrees
        this.setPosition(body.position.x - width / 2, body.position.y - height / 2)
    }

    init {
        this.setSize(10f, 10f)
        this.setPosition(pos_x, pos_y)
        world = aWorld
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position[pos_x] = pos_y
        body = world.createBody(bodyDef)
        val shape = CircleShape()
        shape.radius = width / 2f
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.restitution = 1f
        val fixture = body.createFixture(fixtureDef)
        shape.dispose()
        this.setOrigin(width / 2, height / 2)
        body.userData = "edge"
    }
}