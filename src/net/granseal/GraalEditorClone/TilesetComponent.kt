package net.granseal.GraalEditorClone

import java.awt.Canvas
import java.awt.Dimension
import java.awt.Graphics

class TilesetComponent: Canvas() {
    init {
        preferredSize =  Dimension(Editor.tilesetImage.getWidth(null),Editor.tilesetImage.getHeight(null))
        size = preferredSize
    }

    override fun paint(g: Graphics) {
        preferredSize = Dimension(Editor.tilesetImage.getWidth(null),Editor.tilesetImage.getHeight(null))
        size = preferredSize
        g.drawImage(Editor.tilesetImage,0,0,null)
    }
}