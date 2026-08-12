# Scenario C - Compliance Reporting

## Ambiguous requirement

Regulators need to be able to audit access to client account data.

## Clarification questions

- Which client data and account resources are in scope?
- Which read and write actions must be recorded?
- Which actors and service identities must be included?
- What report filters, retention period, and export format are required?
- What privacy, authorization, and evidentiary requirements apply?

## Initial normalized requirement

The prototype will report authenticated reads or modifications of client-account resources by actor, action, resource, and time range, using audit records as the source of evidence.
