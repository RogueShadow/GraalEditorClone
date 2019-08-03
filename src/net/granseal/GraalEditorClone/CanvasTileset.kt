package net.granseal.GraalEditorClone

import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

class CanvasTileset: JPanel() {
    val image = ImageIO.read(File(Config.TILESET_PATH))
    val bufferImage = ImageIO.read(File(Config.TILESET_PATH))
    var selection = arrayOf(-1,-1,-1,-1)
    var o = Point()
    var mouseDown = false
    var mouseButtons = mutableMapOf<Int,Boolean>()
    var tileSelection = mutableListOf<Int>()

    init {
        background = Color.BLACK
        addMouseMotionListener(object: MouseMotionAdapter(){
            override fun mouseMoved(e: MouseEvent) {
                val mx = Math.min(Math.max(e.x + 8, 0), 2056) / 16
                val my = Math.min(Math.max(e.y + 8, 0), 512) / 16
                val sx = (mx % 16)
                val sy = my
                //Editor.statusBarRight.text = ("Select tile ($sx, $sy) ")
            }
            override fun mouseDragged(e: MouseEvent) {
                val mx = Math.min(Math.max(e.x + 8, 0), 2056) / 16
                val my = Math.min(Math.max(e.y + 8, 0), 512) / 16

                // Move selection if mouse position is less than the original mouse click
                if (mx < o.x) {
                    selection[0] = mx
                    selection[2] = Math.abs(o.x - mx)
                } else {
                    selection[0] = o.x
                    selection[2] = mx - selection[0]
                }
                if (my < o.y) {
                    selection[1] = my
                    selection[3] = Math.abs(o.y - my)
                } else {
                    selection[1] = o.y
                    selection[3] = my - selection[1]
                }
                repaint()

                val output =
                    String.format("(%d, %d) -> (%d, %d) = (%d, %d) ", o.x % 16, o.y, mx, my, selection[2], selection[3])
                //Editor.statusBarRight.text = output
            }
        })
        addMouseListener(object: MouseAdapter(){
            override fun mousePressed(e: MouseEvent) {
                mouseButtons[e.button] = true
                // Only do a click check if the mouse is not already pressed
                if (!mouseDown) {
                    // Round mouse position to snap it to tile grid
                    val mx = Math.min(Math.max(e.getX() + 8, 0), 2056) / 16
                    val my = Math.min(Math.max(e.getY() + 8, 0), 512) / 16
                    // Record original mouse position for future reference
                    o.x = mx
                    o.y = my

                    selection = arrayOf(mx, my, 0, 0)


                }
                mouseDown = true

                repaint()
            }

            override fun mouseClicked(e: MouseEvent) {
                releaseSelection()

                if (e.clickCount == 2) {
                    val tile = pointToTile(e.getPoint())
                    GraalEditorClone.selectedTile = tile
                }
            }
            override fun mouseReleased(e: MouseEvent) {
                mouseButtons[e.button] = false
                tileSelection.clear()
                releaseSelection()
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
        if (selection[0] >= 0 && selection[1] >= 0)drawHighlight(g,selection)
    }

    private fun drawHighlight(g: Graphics, selection: Array<Int>) {
        val buffer = bufferImage.createGraphics()
        buffer.drawImage(image, 0, 0, null)
        buffer.dispose()
        val x = selection[0];val y = selection[1]
        val w = selection[2];val h = selection[3]
        var dx = x * 16; var dy = y * 16
        var dw = w *  16; var dh = h * 16
        drawInvertedRect(dx-1,dy-1,dw+2,dh+2)
        drawInvertedRect(dx,dy,dw,dh)

        g.drawImage(bufferImage, 0, 0, null)

        if (w > 0 || h > 0) {
            dx -= 1
            dy -= 1
            dw += 2
            dh += 2
            g.color = Color(0, 0, 0, 125)
            val xPoints = intArrayOf(0, 2056, 2056, 0, 0, dx, dx + dw, dx + dw, dx, dx)
            val yPoints = intArrayOf(0, 0, 512, 512, 0, dy, dy, dy + dh, dy + dh, dy)
            g.fillPolygon(xPoints, yPoints, xPoints.size)
        }

    }

    private fun drawInvertedRect(ix: Int, iy: Int,iw: Int, ih: Int) {
        val x = clamp(ix,0,image.width)
        val y = clamp(iy,0,image.height)
        val w = iw-1
        val h = ih-1
        (x..x+w).forEach{
            bufferImage.setRGB(it,y,invertColor(image.getRGB(it,y)))
            bufferImage.setRGB(it,y+h, invertColor(image.getRGB(it,y+h)))
        }
        (y..y+h).forEach{
            bufferImage.setRGB(it,y,invertColor(image.getRGB(x,it)))
            bufferImage.setRGB(it,y+h, invertColor(image.getRGB(x+w,it)))
        }
    }

    private fun releaseSelection() {
        // Reset the selection array
        selection = arrayOf(-1, -1, -1, -1)
        tileSelection.clear()
        this.repaint()
    }
}