package xyz.ramil.zomcalyp.entities.car

data class CarSetting (
        val id: Int,
        val type: Int,
        val isLocked: Int,
        val max_speed: Float,
        val weight: Float,
        val suspension: Int,
        val engine: Int,
        val tires: Int,
        val transmission: Int,
        val additionallyInt: Int,
        val additionallyString: String
)