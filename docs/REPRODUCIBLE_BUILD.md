# Reproducible Build and Evidence

The authoritative build command is:

```text
mvn --batch-mode --no-transfer-progress clean verify
```

The GitHub Actions workflow in `.github/workflows/ci.yml` runs this command on Java 21 and uploads Surefire, JaCoCo, and dependency-check artifacts. The local `target/` directory is generated output and is not fresh execution evidence unless it is accompanied by the console log from the same revision.

The archive includes `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/maven-wrapper.properties`. The script-only wrapper downloads Maven 3.9.11 from Maven Central on first use and therefore requires network access. In an offline or network-restricted evaluator it cannot provide fresh execution; use the CI artifact or a preinstalled Maven distribution instead. The wrapper does not bundle Maven itself.
