package net.granseal.GraalEditorClone.windows

import net.granseal.GraalEditorClone.NPC
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JTextArea

class ScriptEditor(val npc: NPC) : JFrame() {
    fun init() {
        size = Dimension(400,400)
        preferredSize = size
        title = "Script Editor"
        add(JTextArea(npc.script).apply {
            preferredSize = Dimension(400,400)
            size = preferredScrollableViewportSize
        })

        isVisible = true
    }
}