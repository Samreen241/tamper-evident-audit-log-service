# Reproducible Build and Evidence

The authoritative build command is:

```text
mvn --batch-mode --no-transfer-progress clean verify
```

The GitHub Actions workflow in `.github/workflows/ci.yml` runs this command on Java 21 and uploads Surefire, JaCoCo, and dependency-check artifacts. The local `target/` directory is generated output and is not fresh execution evidence unless it is accompanied by the console log from the same revision.

This archive currently does not include Maven Wrapper files. A reviewer without Maven installed should use the CI artifact or install Maven 3.9+ with Java 21. Adding a Maven Wrapper requires generating and committing its wrapper JAR and scripts from a Maven-enabled environment; it must be completed before claiming fully self-contained offline reproduction.
