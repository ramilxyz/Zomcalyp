package xyz.ramil.zomcalyp.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.MathUtils
import xyz.ramil.zomcalyp.Constants
import xyz.ramil.zomcalyp.ZGame
import xyz.ramil.zomcalyp.tools.ParticleEffectLoader


class LoadingScreen internal constructor(private val zgame: ZGame) : Screen {

    val shapeRenderer = ShapeRenderer()
    var process = 0f
    val logo = Texture("logo.png")

    init {
        queueAssets()
    }

    override fun hide() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        update(delta)
        process = MathUtils.lerp(process, zgame.assetManager.progress, 0.1f)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        shapeRenderer.color = Color.RED
        shapeRenderer.rect(0f, 0f, zgame.mViewport?.screenWidth!!.toFloat() * process, 10f)
        shapeRenderer.end()

        zgame.mBatch?.begin()
        zgame.mBatch?.draw(logo, 250f, 100f, 300f, 300f)
        zgame.mBatch?.end()

        zgame.mBatch?.begin()
        zgame.font?.draw(zgame.mBatch, (process * 100).toInt().toString() + "%", 365f, 80f)
        zgame.mBatch?.end()

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }

    fun update(delta: Float) {
        if (zgame.assetManager.update() && process <= zgame.assetManager.progress) {
            zgame.screen = PlayScreen(zgame)

        }
    }

    private fun queueAssets() {

        /**
         *  cars
         */
        zgame.assetManager.load("cars/y0.png", Texture::class.java)
        zgame.assetManager.load("cars/y1.png", Texture::class.java)
        zgame.assetManager.load("cars/b0.png", Texture::class.java)
        zgame.assetManager.load("cars/b1.png", Texture::class.java)
        zgame.assetManager.load("cars/r0.png", Texture::class.java)

        /**
         *  actor
         */
        zgame.assetManager.load("actors/g.png", Texture::class.java)
        zgame.assetManager.load("actors/m.png", Texture::class.java)
        zgame.assetManager.load("actors/r.png", Texture::class.java)
        zgame.assetManager.load("actors/y.png", Texture::class.java)

        /**
         *  ui
         */
        zgame.assetManager.load("ui/brake.png", Texture::class.java)
        zgame.assetManager.load("ui/gas.png", Texture::class.java)
        zgame.assetManager.load("ui/left.png", Texture::class.java)
        zgame.assetManager.load("ui/right.png", Texture::class.java)
        zgame.assetManager.load("ui/setting.png", Texture::class.java)
        zgame.assetManager.load("ui/menu.png", Texture::class.java)
        zgame.assetManager.load("ui/menu_frame.png", Texture::class.java)
        zgame.assetManager.load("ui/zombie.png", Texture::class.java)
        zgame.assetManager.load("ui/camera.png", Texture::class.java)
        zgame.assetManager.load("ui/sound_off.png", Texture::class.java)
        zgame.assetManager.load("ui/sound_on.png", Texture::class.java)
        zgame.assetManager.load("logo.png", Texture::class.java)

        /**
         *  maps
         */
        zgame.assetManager.load("maps/new_map_gray.png", Texture::class.java)
        zgame.assetManager.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        zgame.assetManager.load(Constants.MAP_NAME, TiledMap::class.java)

        /**
         *  entities
         */
        zgame.assetManager.load("entities/blood.png", Texture::class.java)
        zgame.assetManager.load("entities/wheel.png", Texture::class.java)

        /**
         *  effects
         */
        zgame.assetManager.setLoader(ParticleEffect::class.java, ParticleEffectLoader(InternalFileHandleResolver()))
        zgame.assetManager.load("effects/manblood/p", ParticleEffect::class.java)
        zgame.assetManager.setLoader(ParticleEffect::class.java, ParticleEffectLoader(InternalFileHandleResolver()))
        zgame.assetManager.load("effects/zgreen/p", ParticleEffect::class.java)
        zgame.assetManager.setLoader(ParticleEffect::class.java, ParticleEffectLoader(InternalFileHandleResolver()))
        zgame.assetManager.load("effects/zred/p", ParticleEffect::class.java)
        zgame.assetManager.setLoader(ParticleEffect::class.java, ParticleEffectLoader(InternalFileHandleResolver()))
        zgame.assetManager.load("effects/zyellow/p", ParticleEffect::class.java)
        zgame.assetManager.setLoader(ParticleEffect::class.java, ParticleEffectLoader(InternalFileHandleResolver()))
        zgame.assetManager.load("effects/wheel", ParticleEffect::class.java)

        /**
         *  effects
         */
        zgame.assetManager.load("effects/manblood/p", ParticleEffect::class.java)
        zgame.assetManager.load("effects/zgreen/p", ParticleEffect::class.java)
        zgame.assetManager.load("effects/zred/p", ParticleEffect::class.java)
        zgame.assetManager.load("effects/zyellow/p", ParticleEffect::class.java)
        zgame.assetManager.load("effects/wheel", ParticleEffect::class.java)

        /**
         *  font
         */
        zgame.assetManager.load("font/font.fnt", BitmapFont::class.java)

    }
}