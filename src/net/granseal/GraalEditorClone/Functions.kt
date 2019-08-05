@file:JvmName("Functions")
package net.granseal.GraalEditorClone

import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D.Float as RectF
import java.io.File
import javax.swing.AbstractButton
import javax.swing.JMenuItem
import javax.swing.KeyStroke
import kotlin.collections.Iterator

fun tileToPoint(tile: Short): Point {
    return Point(
        (tile % 16) * 16 + (tile / 512) * 256,
        (tile / 16) * 16 % 512
    )
}

fun base64ToTile(s: String): Short {
    if (s.length > 2) return 0
    return (BASE64.indexOf(s.substring(0, 1)) * 64 + BASE64.indexOf(s.substring(1, 2))).toShort()
}

fun pointToTile(p: Point): Short {
    return (p.x / 16 % 16 + p.x / 256 * 512 + p.y / 16 * 16 % 512).toShort()
}

fun drawAllTiles(level: NWLevel, tileset: Image, g: Graphics){
    for (tile in level.tiles){
        val p = tileToPoint(tile.value)
        g.drawImage(
            tileset,
            tile.x*16,tile.y*16,
            tile.x*16+16,tile.y*16+16,
            p.x,p.y,
            p.x+16,p.y+16,
            null
        )
    }
}

fun loadNWFile(file: String) = loadNWFile(File(file))
fun loadNWFile(file: File): NWLevel {
    val level = NWLevel()

    fun validateFile(line: String): Boolean {
        return line.startsWith("GLEVNW01")
    }

    fun parseBOARD(line: String){
        val data = line.trim().split(' ')
        val startX = data[1].toInt()
        val tileY = data[2].toInt()
        val width = data[3].toInt()
        val layerIndex = data[4].toInt()
        val lineData = data[5]
        repeat(width){offX ->
            level.tiles.add(
                Tile(startX + offX,tileY,layerIndex,base64ToTile(lineData.substring(offX*2,offX*2 + 2)))
            )
        }
    }

    fun parseLINK(line: String){
        val data = line.trim().split(' ')
        if (data.size != 8){
            GraalEditorClone.logger.severe("Invalid Link Format in, ${file.name} in line,\n $line")
            return
        }
        level.links.add(
            Link(
                dest = data[1],
                x = data[2].toInt(),
                y = data[3].toInt(),
                width = data[4].toInt(),
                height = data[5].toInt(),
                destx = data[6],
                desty = data[7]
            )
        )
    }

    fun parseCHEST(line: String){
        val data = line.trim().split(' ')
        if (data.size != 5){
            GraalEditorClone.logger.severe("Invalid CHEST format in, ${file.name} in line,\n $line")
            return
        }
        level.chests.add(
            Chest(
                x = data[1].toInt(),
                y= data[2].toInt(),
                item = data[3],
                signIndex = data[4].toInt()
            )
        )
    }

    fun parseBADDY(line: String, iter: Iterator<String>){
        val data = line.trim().split(' ')
        if (data.size != 4){
            GraalEditorClone.logger.severe("Invalid BADDY format in, ${file.name} in line,\n $line")
            return
        }
        val baddyX = data[1].toInt()
        val baddyY = data[2].toInt()
        val type = data[3].toInt()
        val dialog = mutableListOf<String>()
        var nextLine = iter.next().trim()
        while (nextLine != "BADDYEND"){
            if (nextLine != ""){
                dialog.add(nextLine)
            }
            nextLine = iter.next()
        }
        level.baddies.add(
            Baddy(
                x = baddyX,
                y = baddyY,
                type = type,
                dialog = dialog
            )
        )
    }

    fun parseNPC(line: String, iter: Iterator<String>) {
        val data = line.trim().split(' ')
        if (data.size != 4){
            GraalEditorClone.logger.severe("Invalid NPC format in file, ${file.name}, in line,\n $line")
            return
        }
        val npcImage = data[1]
        val npcX = data[2].toInt()
        val npcY = data[3].toInt()
        val script = mutableListOf<String>()
        var nextLine = iter.next().trim()
        while (nextLine != "NPCEND"){
            script.add(nextLine)
            nextLine = iter.next()
        }
        level.npcs.add(
            NPC(
                image = npcImage,
                x = npcX,
                y = npcY,
                script = script.joinToString("\n")
            )
        )
    }

    fun parseSIGN(line: String, iter: Iterator<String>) {
        val data = line.trim().split(' ')
        if (data.size != 3){
            GraalEditorClone.logger.severe("Invalid SIGN format in file, ${file.name}, in line,\n $line")
            return
        }
        val signX = data[1].toInt()
        val signY = data[2].toInt()
        val text = mutableListOf<String>()
        var nextLine = iter.next().trim()
        while (nextLine != "SIGNEND"){
            text.add(nextLine)
            nextLine = iter.next()
        }
        level.signs.add(
            Sign(
                x = signX,
                y = signY,
                text = text.joinToString(" ")
            )
        )
    }

    val lvlFile = file.readLines()
    val it = lvlFile.iterator()
    if (!validateFile(it.next())){
        GraalEditorClone.logger.warning("Invalid NW File, ${file.name}")
        return NWLevel()
    }
    while (it.hasNext()) {
        val line = it.next()
        when {
            line.startsWith("BOARD ") -> parseBOARD(line)
            line.startsWith("LINK") -> parseLINK(line)
            line.startsWith("CHEST") -> parseCHEST(line)
            line.startsWith("BADDY") -> parseBADDY(line,it)
            line.startsWith("NPC") -> parseNPC(line,it)
            line.startsWith("SIGN") -> parseSIGN(line,it)
            line.trim() == "" -> ""
            else -> GraalEditorClone.logger.severe("Cannot parse line,\n $line \n Is the file invalid?")
        }
    }
    return level
}


fun clamp(value: Int, min: Int, max: Int): Int {
    return when {
        value < min -> min
        value >= max -> max
        else -> value
    }
}

fun invertColor(rgb: Int): Int {
    val a = 0xFF and (rgb shr 24)
    return if (a == 0) -0xff01 else 0xFFFFFF - rgb or -0x1000000
}


//Extentions
fun JMenuItem.hotKey(c: Char): JMenuItem {
    accelerator = KeyStroke.getKeyStroke(c, Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx)
    return this
}

fun <T: AbstractButton> T.addCallback(c: (ActionEvent) -> Unit): T {
    addActionListener{c(it)}
    return this
}

fun rectangle(x: Int,y: Int,w: Int, h: Int) = rectangle(x.toFloat(),y.toFloat(),w.toFloat(),h.toFloat())
fun rectangle(x: Float, y: Float, w: Float, h: Float) = RectF(x,y,w,h)


fun Int.toLevel() = this/16f
fun Float.toLevel() = this/16f
fun Int.toScreen() = this*16
fun Float.toScreen() = this*16
fun screenToLevel(p: Point) = screenToLevel(Point2D.Float(p.x.toFloat(),p.y.toFloat()))
fun screenToLevel(p: Point2D.Float) = Point2D.Float(p.x.toLevel(),p.y.toLevel())
fun levelToScreen(p: Point2D.Float) = Point2D.Float(p.x.toScreen(),p.y.toScreen())