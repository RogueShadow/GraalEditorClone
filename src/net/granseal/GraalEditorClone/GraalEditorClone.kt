package net.granseal.GraalEditorClone

import java.awt.event.ActionEvent
import java.io.File
import java.io.FileFilter
import java.util.logging.Logger
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

fun main(){
    GraalEditorClone.start()
}


object GraalEditorClone {
    val logger = Logger.getGlobal()!!
    var selectedTile = Config.DEFAULT_TILE
        set(value) {
            field = value
            //Editor.scrollTileset.tileSetIcon.repaint()
        }

    fun start(){
        Editor.init()
        runSomeTests()
    }

    fun runSomeTests(){
        Editor.addLevel(JLabel("Added a new one!"),"New Tab!")
        Editor.removeLevel("New Tab!")
        val files = File("res/levels").listFiles(FileFilter{
            it.extension == "nw"
        })
        val levels = files.map {
            loadNWFile(it)
        }
        levels.forEach(::println)
    }

    fun openMenu(e: ActionEvent){
        val oldStatus = Editor.leftStatus
        Editor.leftStatus = "Please choose a file to open."
        val chooser = JFileChooser()
        chooser.currentDirectory = File(".")
        val filter = FileNameExtensionFilter("Graal Files(*.nw, *.gmap)","nw","gmap")
        chooser.fileFilter = filter
        if (chooser.showOpenDialog(Editor) == JFileChooser.APPROVE_OPTION){
            val newFile = chooser.selectedFile
            logger.info("Opened File: $newFile")
        }
        Editor.leftStatus = oldStatus
    }

    fun saveMenu(e: ActionEvent){
        val oldStatus = Editor.leftStatus
        Editor.leftStatus = "Please choose a location to save your file."
        val chooser = JFileChooser()
        val filter = FileNameExtensionFilter("Graal Files(*.nw, *.gmap)","nw","gmap")
        chooser.fileFilter = filter
        if (chooser.showSaveDialog(Editor) == JFileChooser.APPROVE_OPTION){
            val newFile = chooser.selectedFile
            logger.info("Saved File: $newFile")
        }
        Editor.leftStatus = oldStatus
    }

    fun exitMenu(e: ActionEvent){
        exitProcess(0)
    }

    fun newMenu(e: ActionEvent) {
        val newFiles = Editor.levels().filter{it.name.startsWith("<untitled>") && !it.name.endsWith(".nw")}
        Editor.addLevel(JScrollPane(),
            if (newFiles.count() == 0) "<untitled>"
            else "<untitled> ${newFiles.count()}"
        )
    }

}