@file:JvmName(name = "Constants")

package net.granseal.GraalEditorClone

import java.awt.Image
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO

const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
const val MAJOR = "0"
const val MINOR = "0"
const val BUILD = "1"
const val VERSION = "$MAJOR.$MINOR.$BUILD"
const val TITLE = "Graal Editor $VERSION"
const val IMAGE_PATH = "res/images/"


object Config {
    var TILESET_PATH = "res/images/pics1.png"
    var BLANK_NPC_PATH = "res/images/blanknpc.png"
    var CHEST_PATH = "res/images/chest.png"
    var DEFAULT_TILE: Short = 0x7ff
}

object Assets {
    private val images = mutableMapOf<String,Image>()

    operator fun invoke(id: String): Image {
        return if (exists(id)){
            images[id] ?: throw Exception("No asset, $id, loaded.")
        }else{
            loadImage(IMAGE_PATH + id)
            Thread.sleep(10)
            images[id] ?: throw Exception("No asset, $id, loaded.")
        }
    }

    fun loadImage(file: String, reload: Boolean = false) = loadImage(File(file),reload)
    fun loadImage(file: File, reload: Boolean = false) {
        if (exists(file.name) && !reload)return
        try {
            images[file.name] = ImageIO.read(file)
        }catch (e: IIOException){
            e.stackTrace
            return
        }
    }
    fun removeImage(id: String) {
        images.remove(id)
    }
    fun exists(id: String): Boolean {
        return images.containsKey(id)
    }
}