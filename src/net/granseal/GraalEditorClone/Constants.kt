@file:JvmName(name = "Constants")

package net.granseal.GraalEditorClone

const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
const val MAJOR = "0"
const val MINOR = "0"
const val BUILD = "1"
const val VERSION = "$MAJOR.$MINOR.$BUILD"
const val TITLE = "Graal Editor $VERSION"


object Config {
    var TILESET_PATH = "res/images/pics1.png"
    var DEFAULT_TILE: Short = 0x7ff
}