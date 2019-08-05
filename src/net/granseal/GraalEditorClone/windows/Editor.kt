package net.granseal.GraalEditorClone.windows

import net.granseal.GraalEditorClone.*
import net.granseal.GraalEditorClone.windows.components.TilesetComponent
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.ScrollPane
import javax.swing.*
import javax.swing.border.CompoundBorder


object Editor : JFrame() {
    private lateinit var levelContainer: JTabbedPane
    private lateinit var statusRight: JLabel
    private lateinit var statusLeft: JLabel

    private val tabs = mutableListOf<Pair<Component, String>>()

    //Initialization functions.
    //This one should maintain a clear high level view of components in this window.
    fun init() {
        GraalEditorClone.logger.info("Initializing Editor Window")

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(1024, 768)
        minimumSize = Dimension(640, 480)
        title = TITLE

        jMenuBar = createMenu()
        add(createToolBar(), BorderLayout.NORTH)
        add(createStatusBar(), BorderLayout.SOUTH)
        add(createLevelTabContainer(), BorderLayout.CENTER)
        add(createTilesetContainer(), BorderLayout.EAST)

        isVisible = true
    }

    //Functions to initialize each top level component.
    private fun createTilesetContainer() = JTabbedPane().apply {
        GraalEditorClone.logger.info("Creating Tileset Container")
        preferredSize = Dimension(300, 0)
        val sp = ScrollPane()
        sp.add(TilesetComponent())
        add(sp, "Tileset")
    }

    private fun createLevelTabContainer() = JTabbedPane().apply {
        GraalEditorClone.logger.info("Creating Level Container")
        name = "LevelContainer"
        levelContainer = this
    }

    private fun createStatusBar() = JPanel().apply {
        GraalEditorClone.logger.info("Creating Status Bar")
        layout = BorderLayout()
        val leftBar = JLabel().apply {
            border = BorderFactory.createEmptyBorder(1, 10, 1, 1)
            preferredSize = Dimension(200, 24)
            statusLeft = this
        }
        val rightBar = JLabel().apply {
            preferredSize = Dimension(180, 24)
            border = CompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)
            )
            statusRight = this
        }
        add(leftBar, BorderLayout.CENTER)
        add(rightBar, BorderLayout.EAST)
    }

    private fun createToolBar() = JToolBar().apply {
        GraalEditorClone.logger.info("Creating Tool Bar")
        isFloatable = false
        border = BorderFactory.createEtchedBorder()
        add(JButton(UIManager.getIcon("FileView.fileIcon")).addCallback(GraalEditorClone::newMenu))
        add(JButton(UIManager.getIcon("FileView.directoryIcon")).addCallback(GraalEditorClone::openMenu))
        add(JButton(UIManager.getIcon("FileView.floppyDriveIcon")).addCallback(GraalEditorClone::saveMenu))
        add(JButton(UIManager.getIcon("")))
        add(JButton(UIManager.getIcon("")))
        add(JButton(UIManager.getIcon("")))
        add(JButton(UIManager.getIcon("")))
        add(JButton(UIManager.getIcon("")))
    }

    private fun createMenu() = JMenuBar().apply {
        GraalEditorClone.logger.info("Creating Menu Bar")
        add(JMenu("File").apply {
            add(JMenuItem("New NW").hotKey('N').addCallback(GraalEditorClone::newMenu))
            add(JMenuItem("New GMAP").apply { isEnabled = false })
            add(JMenuItem("New GANI").apply { isEnabled = false })
            addSeparator()
            add(JMenuItem("Open").hotKey('O').addCallback(GraalEditorClone::openMenu))
            add(JMenuItem("Save").hotKey('S').addCallback(GraalEditorClone::saveMenu))
            add(JMenuItem("Save As"))
            addSeparator()
            add(JMenuItem("Exit").addCallback(GraalEditorClone::exitMenu))
        })
        add(JMenu("Edit").apply {
            add(JMenuItem("Undo").hotKey('Z'))
            add(JMenuItem("Redo").hotKey('Y'))
            addSeparator()
            add(JMenuItem("Cut").hotKey('X'))
            add(JMenuItem("Copy").hotKey('C'))
            add(JMenuItem("Paste").hotKey('V'))
            add(JMenuItem("Delete").addCallback(::println))
            addSeparator()
            add(JMenuItem("Preferences").addCallback(::println))
        })
    }

    //Handy functions.
    fun addLevel(c: Component, title: String): Boolean {
        val tab = JScrollPane(c)
        val result = tabs.add(tab to title)
        return if (result) {
            GraalEditorClone.logger.info("Added new level, $title")
            tab.name = title
            levelContainer.add(tab, title)
            levelContainer.selectedIndex = levelContainer.tabCount - 1
            true
        } else {
            GraalEditorClone.logger.warning("Level could not be added.")
            false
        }
    }

    fun levels() = levelContainer.components.toList()

    fun removeLevel(title: String): Boolean {
        val tab = tabs.singleOrNull() { it.second == title }
        return if (tab != null) {
            levelContainer.remove(tab.first)
            tabs.remove(tab)
            GraalEditorClone.logger.info("Removed level, $title")
            true
        } else {
            GraalEditorClone.logger.warning("Level tab $title does not exist.")
            false
        }
    }

    //Handy Properties
    var leftStatus: String
        get() {return statusLeft.text}
        set(value){ statusLeft.text = value }
    var rightStatus: String
        get() {return statusRight.text}
        set(value){ statusRight.text = value }
}