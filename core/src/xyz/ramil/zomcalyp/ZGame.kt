package xyz.ramil.zomcalyp

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.pay.PurchaseManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.ramil.zomcalyp.screens.LoadingScreen


class ZGame : Game() {

    lateinit var purchaseManager: PurchaseManager

    var pause = false

    val purchaseManagerisInitialized: Boolean get() = ::purchaseManager.isInitialized

    var mBatch: SpriteBatch? = null
    var mCamera: OrthographicCamera? = null
    var mViewport: Viewport? = null
    var stage: Stage? = null
    var font: BitmapFont? = null

    var assetManager = AssetManager()
    lateinit var skin: Skin
    private var atlas: TextureAtlas? = null
    lateinit var dialog: Dialog

    override fun create() {

        skin = Skin()
        atlas = TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"))
        skin.addRegions(atlas)
        skin.load(Gdx.files.internal("skin/uiskin.json"))


        mBatch = SpriteBatch()
        mCamera = OrthographicCamera()
        mViewport = StretchViewport(800f, 480f)
        stage = Stage(mViewport, mBatch)
        font = BitmapFont(Gdx.files.internal("font/font.fnt"),
                Gdx.files.internal("font/font.png"), false)
        font!!.data.scale(0.8f)

        setScreen(LoadingScreen(this))

    }

    override fun render() {
        super.render()

        update()
        draw()

    }

    private fun draw() {
        mBatch?.projectionMatrix = mCamera?.combined
        stage?.act(Gdx.graphics.deltaTime)
        stage?.draw()
    }

    private fun update() {
        mCamera?.update()

    }

    override fun dispose() {
        super.dispose()
        mBatch?.dispose()
        stage?.dispose()
        assetManager.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        mViewport?.update(width, height)
        stage?.viewport?.update(width, height, true)
    }
}