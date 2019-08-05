package net.granseal.GraalEditorClone.windows.components

import net.granseal.GraalEditorClone.*
import net.granseal.GraalEditorClone.windows.Editor
import net.granseal.GraalEditorClone.windows.ScriptEditor
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import javax.swing.JPanel

class NWLevelComponent(val level: NWLevel): JPanel(), MouseListener {
    private val levelBuffer = BufferedImage(64*16,64*16,BufferedImage.TYPE_INT_ARGB)
    private val buf = levelBuffer.createGraphics()

    init {
        preferredSize = Dimension(64*16,64*16)
        size = preferredSize
        drawAllTiles(level, Assets("pics1.png"), buf)

        level.npcs.filter{it.hasImage()}.map{ it.image }.forEach{
            Assets.loadImage(IMAGE_PATH + it)
        }

        addMouseListener(this)
    }

    override fun paint(g: Graphics) {
        g.drawImage(levelBuffer,0,0,null)
        level.npcs.forEach{
            if (Assets.exists(it.image)){
                g.drawImage(Assets(it.image),it.x.toScreen(),it.y.toScreen(),null)
            }else{
                g.drawImage(Assets("blanknpc.png"),it.x.toScreen(),it.y.toScreen(),null)
            }
        }
        level.links.forEach{
            g.color =  Color.white
            g.drawRect(it.x.toScreen(),it.y.toScreen(),it.width.toScreen(),it.height.toScreen())
        }
        level.signs.forEach{
            g.color = Color.GRAY
            g.fillRect(it.x.toScreen(),it.y.toScreen(),32,16)
            g.color = Color.BLACK
            g.drawString(it.text,it.x.toScreen(),it.y.toScreen())
        }
        level.chests.forEach{
            g.drawImage(Assets("chest.png"),it.x.toScreen(),it.y.toScreen(),null)
        }
    }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {
        val click = screenToLevel(e.point)

        when (val obj = level.getClickedObject(click)){
            is NPC -> {
                ScriptEditor(obj).init()
            }
            is Sign  -> Editor.leftStatus = "Sign says, ${obj.text}"
            is Baddy -> Editor.leftStatus = "You clicked a baddy. $obj"
            is Link  -> Editor.leftStatus = "You clicked a link $obj"
            else -> Editor.leftStatus = obj.toString()
        }
    }
    override fun mouseExited(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {}
}