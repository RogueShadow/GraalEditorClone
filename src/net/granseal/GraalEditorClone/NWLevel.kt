package net.granseal.GraalEditorClone

data class NWLevel(var tiles: MutableList<Tile> = mutableListOf(),
                   var links: MutableList<Link> = mutableListOf(),
                   var chests: MutableList<Chest> = mutableListOf(),
                   var baddies: MutableList<Baddy> = mutableListOf(),
                   var npcs: MutableList<NPC> = mutableListOf(),
                   var signs: MutableList<Sign> = mutableListOf())


data class Tile(val x: Int = 0,val y: Int = 0 ,val layer: Int = 0,val value: Short)

data class Link(val dest: String, val x: Int, val y: Int,
                val width: Int, val height: Int,
                val destx: String, val desty: String)

data class Chest(val x: Int, val y: Int, val item: String, val signIndex: Int )

data class Baddy(val x: Int, val y: Int, val type: Int, val dialog: List<String>)

data class NPC(val image: String, val x: Int, val y: Int, val script: String){
    fun hasImage(): Boolean = image  != "-"
}

data class Sign(val x: Int, val y: Int, val text: String)