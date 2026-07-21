# Fixture Creation In Foliary

Read this file before creating or updating any fixture function.

## Workflow

1. Read the entity primary constructor in `commonMain`.
2. Create the fixture file in `commonTest` under the exact same package as the entity.
3. Name the file `[EntityName]Fixture.kt`.
4. Implement the fixture as a companion extension function: `fun Entity.Companion.fixture(...)`.
5. Copy the fixture function arguments from the entity primary constructor exactly.
6. Keep the same argument names, types, nullability, and order as the entity constructor.
7. Return the entity by passing those arguments through directly.
8. If you need a concrete shape reference, read the relevant example in `templates/` before writing the fixture.

## Strict Rules

1. The fixture file must live in the same package as the entity, but under the `commonTest` source set.
2. The fixture function arguments must match the entity primary constructor exactly.
3. The fixture default values must be sensible and represent a realistic object.
4. Default values must never be `null`, even when the parameter type is nullable.
5. When a parameter is another entity, use that entity's fixture as the default value.
6. When a parameter type is a collection, the default value must contain at least one element.
7. Do not invent fixture-only parameters. The fixture signature must mirror the entity constructor.
8. Do not reorder parameters in the fixture.

## Default Value Guidance

1. For IDs, use a generated but valid ID such as `Uuid.random()`.
2. For names, titles, descriptions, and URLs, use readable real-looking values.
3. For dates, choose values that make sense together as one coherent object.
4. For nullable timestamps, enums, nested objects, and strings, still provide a non-null default.
5. For nested entities, call that nested entity's `fixture()`.
6. For collections, create at least one realistic child element using fixtures where appropriate.
