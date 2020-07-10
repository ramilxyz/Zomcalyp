package xyz.ramil.zomcalyp.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import xyz.ramil.zomcalyp.ZGame


class BloodEffects(position: Vector2, name: String, zgame: ZGame) : Actor() {
    val particleEffect: ParticleEffect

    init {
        particleEffect = zgame.assetManager.get(name)
        particleEffect.scaleEffect(0.06f)
        particleEffect.emitters.get(0).transparency.setHigh(1f)
        particleEffect.setPosition(position.x, position.y)
        particleEffect.start()
    }

    override fun act(delta: Float) {
        super.act(delta)
        particleEffect.update(delta / 3)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        particleEffect.draw(batch)
    }
}