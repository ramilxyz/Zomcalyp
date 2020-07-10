package xyz.ramil.zomcalyp.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import xyz.ramil.zomcalyp.ZGame
import java.util.*

class Blood(zgame: ZGame?, vector2: Vector2) : Image(zgame?.assetManager?.get("entities/blood.png", Texture::class.java)) {
    val random = Random()

    val image = Image(zgame?.assetManager?.get("entities/blood.png", Texture::class.java))

    init {
        this.clipBegin()
        this.rotation = random.nextInt(360).toFloat()
        this.setSize(10f, 10f)
        this.setOrigin(Align.center)
        this.setPosition(vector2.x - this.width / 2, vector2.y - this.height / 2)
        //todo fot PC
//        val run = RunnableAction()
//        run.setRunnable(Runnable { this.remove() })
//        this.addAction(sequence(alpha(0f, 5f), run));
    }
}
