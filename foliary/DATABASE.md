# Database Configuration and Migration Strategy

## Overview
Foliary uses Room for local data persistence with multiplatform support (Android, iOS, JVM).

## Database Setup
The database is defined in `FoliaryDatabase.kt`. It uses `BundledSQLiteDriver` to ensure consistent SQLite behavior across all platforms.

### Platform Specifics
- **Android**: Database is stored in the application's internal database directory.
- **iOS**: Database is stored in the `NSHomeDirectory()`.
- **JVM**: Database is stored in `$USER_HOME/.foliary/foliary.db`.

## Migration Strategy
Currently, the database is configured with `fallbackToDestructiveMigration(true)`. This means that any schema changes without a defined migration will result in the database being cleared.

### Adding Migrations
When stable schema versions are reached, manual migrations should be implemented:

1.  Increment the `version` in `FoliaryDatabase`.
2.  Define a `Migration` object:
    ```kotlin
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL("ALTER TABLE ...")
        }
    }
    ```
3.  Add the migration to the database builder in `DatabaseModule.kt`:
    ```kotlin
    getDatabaseBuilder()
        .addMigrations(MIGRATION_1_2)
        .build()
    ```

## Schema Export
Room schemas are exported to the `foliary/schemas` directory. This allows for schema version tracking and is required for auto-migrations.
To generate/update the schema, run:
```bash
./gradlew :foliary:kspAndroidMain
```
