# Evidence Artifact Manifest

- `maven-clean-verify.log`: complete Maven console output.
- `target/surefire-reports/`: JUnit execution results.
- `target/site/jacoco/index.html`: readable coverage report.
- `target/site/jacoco/jacoco.xml`: machine-readable coverage report.
- `target/site/jacoco/jacoco.csv`: coverage summary.
- `target/dependency-check-report.html` and `.xml`: dependency scan results.

Generate and archive these artifacts from the exact revision recorded in the attestation.
