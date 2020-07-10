package xyz.ramil.zomcalyp.tools

import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import xyz.ramil.zomcalyp.Constants
import xyz.ramil.zomcalyp.ZGame

class MapLoader(private val mWorld: World, private val zGame: ZGame) : Disposable {
    private val mMap: TiledMap

    val player: Body?
        get() {
            val rectangle = mMap.layers[MAP_PLAYER].objects.getByType(RectangleMapObject::class.java)[0].rectangle
            return ShapeFactory.createRectangle(
                    Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2),
                    BodyDef.BodyType.DynamicBody, mWorld, PLAYER_DENSITY, false)
        }

    override fun dispose() {
        mMap.dispose()
    }

    companion object {
        private const val MAP_WALL = "wall"
        private const val MAP_PLAYER = "player"
        private const val OBJECT_DENSITY = 1f
        private const val PLAYER_DENSITY = 0.4f
    }

    init {
        mMap = zGame.assetManager.get(Constants.MAP_NAME)
        for (`object` in mMap.layers[MAP_WALL].objects) {
            if (`object` is PolygonMapObject) {
                ShapeFactory.createPoly(`object`, BodyDef.BodyType.StaticBody, mWorld, OBJECT_DENSITY, false)
            }
        }
    }
}