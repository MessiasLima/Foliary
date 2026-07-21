package dev.appoutlet.foliary.data.task.database.entity

enum class Priority {
    LOWEST, LOW, MEDIUM, HIGH, HIGHEST, BLOCKER;
    companion object {
        fun parse(priorityString: String): Priority {
            return entries.find { it.name.equals(priorityString, ignoreCase = true) }
                ?: error("Priority $priorityString not found")
        }
    }
}
