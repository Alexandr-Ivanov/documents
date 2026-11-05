# AGENTS.md

## Project

This is a Java 26 command-line document manager. It stores document names, types, binary content, and typed attributes in SQLite. The interactive command loop starts in `ru.ivanov.sitesoft_testcase.ConsoleMain`; commands are dispatched by `CommandProcessor` to the `DocumentsDomain` interface and its SQLite implementation, `DocumentsDomainImpl`.

## Project layout

- `src/main/java/ru/ivanov/sitesoft_testcase/` — console entry point and command handling.
- `src/main/java/ru/ivanov/sitesoft_testcase/domain/` — document records, storage interface, and SQLite implementation.
- `src/main/java/ru/ivanov/sitesoft_testcase/util/` — command-line parsing utilities, including quote-aware `StringSplitter`.
- `src/test/java/` — JUnit 4 tests for command processing, storage, and string splitting.
- `pom.xml` — Maven build, dependencies, and executable-jar configuration.
- `Dockerfile` — runs the packaged jar and mounts `/var/db` as a volume.
- `target/` — generated build output; do not edit or commit generated artifacts unless explicitly requested.

## Build and test

Use Java 26 and Maven.

```bash
mvn clean test
mvn package
```

`mvn package` builds `target/sitesoft-testcase-0.0.1-jar-with-dependencies.jar`, which is the jar used by the Dockerfile. To build the Docker image, first run `mvn package`, then build the image from the repository root.

## Important database safety note

`DocumentsDomainImpl` connects to `/var/db/<databaseName>`. The application uses `/var/db/Documents`; `DocumentsDomainTest` uses `/var/db/test` and calls `createDatabase()`, which drops and recreates its tables. Do not run integration tests against a directory containing important data. Run tests only in an isolated environment with a disposable `/var/db`, and never change tests to point at a real user database.

When documenting or changing database behavior, verify the actual path and initialization logic in `DocumentsDomainImpl` and `ConsoleMain`.

## Implementation guidance

- Keep command syntax and behavior in `CommandProcessor`; keep persistence operations behind `DocumentsDomain`.
- Use the existing `Document` and `DocumentAttribute` records for domain values.
- Preserve the current package structure and Java module declaration in `src/main/java/module-info.java` when adding classes or dependencies.
- Use prepared statements for SQL values. Keep document and attribute operations consistent with the existing schema and methods in `DocumentsDomainImpl`.
- Document content is binary data stored in SQLite and transferred through streams. Prefer stream-based APIs for content rather than converting arbitrary file content to text.
- `StringSplitter` supports quoted arguments. Add or update tests if changing how spaces, quotes, or empty arguments are parsed.
- Keep the command-line interface compatible unless a requested change explicitly alters its commands.

## Tests

- Add or update focused JUnit 4 tests for behavior changes.
- `StringSplitterTest` covers whitespace and quoted arguments.
- `CommandProcessorTest` covers command dispatch and argument handling using a test-domain implementation.
- `DocumentsDomainTest` exercises SQLite persistence, including document content and attributes; it is destructive to the tables in its configured test database, so use an isolated environment.
- Do not claim tests passed unless you ran them and verified the result.

## Changes and documentation

- Keep changes focused and avoid unrelated refactoring.
- Update `readme.txt` when changing documented commands or setup behavior.
- Do not edit the packaged jar under `target/` by hand; regenerate it with Maven.
- Do not add credentials, secrets, or real document data to the repository.