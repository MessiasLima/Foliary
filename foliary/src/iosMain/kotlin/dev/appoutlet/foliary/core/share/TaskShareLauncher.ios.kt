package dev.appoutlet.foliary.core.share

import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Single
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
@Single
class IosTaskShareLauncher : TaskShareLauncher {
    override fun share(text: String) {
        val activityItems = listOf(text)
        val viewController = UIActivityViewController(activityItems, null)

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

        rootViewController?.presentViewController(viewController, animated = true, completion = null)
    }
}
