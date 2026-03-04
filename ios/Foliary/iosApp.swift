import SwiftUI
import FoliaryShared

@main
struct ComposeApp: App {
    init() {
        InitSentryKt.doInitSentry()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea(.all)
        }
    }
}

