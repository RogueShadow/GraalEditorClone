package net.granseal.GraalEditorClone

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants

class TilesetScrollPane: JScrollPane() {
    val tileSetIcon = TilesetPreviewIcon()
    private lateinit var backgroundImage: BufferedImage

    init {
        verticalScrollBar.unitIncrement = 16
        setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, tileSetIcon)
        verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
    }
    class TilesetPreviewIcon: JLabel() {
        private val image = ImageIO.read(File(Config.TILESET_PATH))

        override fun paintComponent(g: Graphics?) {
            super.paintComponent(g)
            val g2d = g as Graphics2D
            val tilecoord = tileToPoint(GraalEditorClone.selectedTile)
            g2d.drawImage(image,0,0,16,16,
                tilecoord.x,tilecoord.y,tilecoord.x + 16,tilecoord.y+16
                ,null)

        }
    }
}



