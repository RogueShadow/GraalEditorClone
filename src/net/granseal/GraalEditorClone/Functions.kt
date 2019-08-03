@file:JvmName("Functions")
package net.granseal.GraalEditorClone

import java.awt.Container
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import javax.swing.AbstractButton
import javax.swing.JMenuItem
import javax.swing.KeyStroke

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

fun JMenuItem.hotKey(c: Char): JMenuItem {
    accelerator = KeyStroke.getKeyStroke(c, Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx)
    return this
}
fun <T: AbstractButton> T.addCallback(c: (ActionEvent) -> Unit): T {
    addActionListener{c(it)}
    return this
}
