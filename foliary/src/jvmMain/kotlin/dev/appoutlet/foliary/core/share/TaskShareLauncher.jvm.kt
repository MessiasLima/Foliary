package dev.appoutlet.foliary.core.share

import org.koin.core.annotation.Single
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Single
class JvmTaskShareLauncher : TaskShareLauncher {
    override fun share(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(text)
        clipboard.setContents(selection, selection)
    }
}
