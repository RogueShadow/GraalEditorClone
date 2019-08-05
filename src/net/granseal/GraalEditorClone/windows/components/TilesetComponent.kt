package net.granseal.GraalEditorClone.windows.components

import net.granseal.GraalEditorClone.Assets
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class TilesetComponent: JPanel() {

    override fun paint(g: Graphics) {
        preferredSize = Dimension(
            Assets("pics1.png").getWidth(null),
            Assets("pics1.png").getHeight(null))
        size = preferredSize
        g.drawImage(Assets("pics1.png"),0,0,null)
    }
}