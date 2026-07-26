package dev.appoutlet.foliary.core.share

import android.content.Context
import android.content.Intent
import org.koin.core.annotation.Single

@Single
class AndroidTaskShareLauncher(private val context: Context) : TaskShareLauncher {
    override fun share(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }
}
