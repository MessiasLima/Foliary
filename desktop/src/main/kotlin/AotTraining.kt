import kotlin.system.exitProcess

private const val AotTrainingDurationMs = 45_000L

fun aotTraining() {
    Thread({
        Thread.sleep(AotTrainingDurationMs)
        exitProcess(0)
    }, "aot-timer").apply {
        isDaemon = false
        start()
    }
}
