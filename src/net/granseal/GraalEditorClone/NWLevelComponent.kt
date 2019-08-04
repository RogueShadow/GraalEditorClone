package net.granseal.GraalEditorClone

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JPanel

class NWLevelComponent(val level: NWLevel): JPanel() {
    val levelBuffer = BufferedImage(64*16,64*16,BufferedImage.TYPE_INT_ARGB)
    val buf = levelBuffer.createGraphics()

    init {
        preferredSize = Dimension(64*16,64*16)
        size = preferredSize
        drawAllTiles(level, Assets("pics1.png"), buf)

        level.npcs.filter{it.hasImage()}.map{ it.image }.forEach{
            Assets.loadImage(IMAGE_PATH + it)
        }
    }

    override fun paint(g: Graphics) {
        g.drawImage(levelBuffer,0,0,null)
        level.npcs.forEach{
            println("${it.x} ${it.y}")
            if (Assets.exists(it.image)){
                g.drawImage(Assets(it.image),it.x*16,it.y*16,null)
            }else{
                g.drawImage(Assets("blanknpc.png"),it.x*16,it.y*16,null)
            }
        }
        level.links.forEach{
            g.color =  Color.white
            g.drawRect(it.x*16,it.y*16,it.width*16,it.height*16)
        }
        level.signs.forEach{
            g.color = Color.GRAY
            g.fillRect(it.x*16,it.y*16,32,16)
            g.color = Color.BLACK
            g.drawString(it.text,it.x*16,it.y*16)
        }
        level.chests.forEach{
            g.drawImage(Assets("chest.png"),it.x*16,it.y*16,null)
        }
    }
}