package xyz.ramil.zomcalyp.tools

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.utils.Array

class ParticleEffectLoader(resolver: FileHandleResolver?) : SynchronousAssetLoader<ParticleEffect?, ParticleEffectLoader.ParticleEffectParameter?>(resolver) {

    class ParticleEffectParameter : AssetLoaderParameters<ParticleEffect?>()

    override fun load(assetManager: AssetManager?, fileName: String?, file: FileHandle?, parameter: ParticleEffectParameter?): ParticleEffect? {
        val effect = ParticleEffect()
        val effectFile = resolve(fileName)
        val imgDir = effectFile.parent()
        effect.load(effectFile, imgDir)
        return effect
    }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: ParticleEffectParameter?): Array<AssetDescriptor<Any>>? {
        return null
    }
}