# Evaluation Evidence Bundle

Generate the local evidence with:

```powershell
mvn clean verify *>&1 | Tee-Object evaluation-evidence/maven-clean-verify.log
```

This produces the Maven log, Surefire results under `target/surefire-reports/`, and JaCoCo reports under `target/site/jacoco/`. The CI workflow uploads these artifacts for every push and pull request. Archive the reports or download the CI artifact with the submission; do not commit the entire `target/` directory.
