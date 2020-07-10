package xyz.ramil.zomcalyp.tools

import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import xyz.ramil.zomcalyp.Constants

object ShapeFactory {
    fun createRectangle(position: Vector2, size: Vector2, type: BodyDef.BodyType?, world: World, density: Float, sensor: Boolean): Body {
        val bdef = BodyDef()
        bdef.position[position.x / Constants.PPM] = position.y / Constants.PPM
        bdef.type = type
        val body = world.createBody(bdef)
        val shape = PolygonShape()
        shape.setAsBox(size.x / Constants.PPM, size.y / Constants.PPM)
        val fdef = FixtureDef()
        fdef.shape = shape
        fdef.density = density
        fdef.isSensor = sensor
        body.createFixture(fdef)
        shape.dispose()
        body.userData = size
        return body
    }

    fun createPoly(polygonMapObject: PolygonMapObject, type: BodyDef.BodyType?, world: World, density: Float, sensor: Boolean): Body {
        val bdef = BodyDef()
        bdef.type = type
        val body = world.createBody(bdef)
        val shape = PolygonShape()
        val vertices = polygonMapObject.polygon.transformedVertices
        val worldVertices = FloatArray(vertices.size)
        for (i in vertices.indices) {
            println(vertices[i])
            worldVertices[i] = vertices[i] / Constants.PPM
        }
        shape.set(worldVertices)
        val fdef = FixtureDef()
        fdef.shape = shape
        fdef.density = density
        fdef.isSensor = sensor
        body.createFixture(fdef)
        body.userData = "edge"
        shape.dispose()
        return body
    }
}