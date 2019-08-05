package net.granseal.GraalEditorClone

import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D.Float as RectF

data class NWLevel(var tiles: MutableList<Tile> = mutableListOf(),
                   var links: MutableList<Link> = mutableListOf(),
                   var chests: MutableList<Chest> = mutableListOf(),
                   var baddies: MutableList<Baddy> = mutableListOf(),
                   var npcs: MutableList<NPC> = mutableListOf(),
                   var signs: MutableList<Sign> = mutableListOf()){

    fun getClickedObject(point: Point2D.Float) = getClickedObject(point.x,point.y)
    fun getClickedObject(x: Float, y: Float) = getClickedObject(x.toDouble(),y.toDouble())
    fun getClickedObject(x: Double, y: Double): LevelObject {
        val npc =  npcs.firstOrNull{it.getBounds().contains(x,y)}
        if (npc != null) return npc
        val baddy = baddies.firstOrNull { it.getBounds().contains(x,y) }
        if (baddy != null) return baddy
        val chest = chests.firstOrNull { it.getBounds().contains(x,y) }
        if (chest != null) return chest
        val sign = signs.firstOrNull { it.getBounds().contains(x,y) }
        if (sign != null) return sign
        val link = links.firstOrNull { it.getBounds().contains(x,y) }
        if (link  != null) return link
        return tiles.first { it.getBounds().contains(x,y) }
    }

}


data class Tile(val x: Int = 0,val y: Int = 0 ,val layer: Int = 0,val value: Short): LevelObject{
    override fun getBounds() = rectangle(x,y,1,1)
}

data class Link(val dest: String, val x: Int, val y: Int,
                val width: Int, val height: Int,
                val destx: String, val desty: String): LevelObject{
    override fun getBounds() = rectangle(x,y,width,height)
}

data class Chest(val x: Int, val y: Int, val item: String, val signIndex: Int ): LevelObject {
    override fun getBounds() = rectangle(x,y,2,2)
}

data class Baddy(val x: Int, val y: Int, val type: Int, val dialog: List<String>): LevelObject {
    override fun getBounds() = rectangle(x.toFloat(),y.toFloat(),2.5f,2.5f)
}

data class NPC(val image: String, val x: Int, val y: Int, val script: String): LevelObject{
    fun hasImage(): Boolean = image  != "-"
    override fun getBounds(): RectF {
        return rectangle(
            x.toFloat(),
            y.toFloat(),
            if (hasImage()) Assets(image).getWidth(null).toLevel() else Assets("blanknpc.png").getWidth(null).toLevel(),
            if (hasImage()) Assets(image).getHeight(null).toLevel() else Assets("blanknpc.png").getHeight(null).toLevel()
        )
    }
}

data class Sign(val x: Int, val y: Int, val text: String): LevelObject {
    override fun getBounds() = rectangle(x,y,2,1)
}


interface LevelObject {
    fun getBounds(): RectF
}
