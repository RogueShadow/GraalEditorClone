package net.granseal.GraalEditorClone

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JScrollPane

class NWLevelComponent(val level: NWLevel): Canvas() {
    val levelBuffer = BufferedImage(64*16,64*16,BufferedImage.TYPE_INT_ARGB)
    val buf = levelBuffer.createGraphics()

    init {
        size = Dimension(64*16,64*16)
        drawAllTiles(level, Editor.tilesetImage, buf)
    }

    override fun paint(g: Graphics) {
        g.drawImage(levelBuffer,0,0,null)
    }
}