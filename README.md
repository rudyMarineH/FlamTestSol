# QA Automation Test Suite

A multi-module Java test automation framework covering REST API, GraphQL, and browser UI testing.

## CI / GitHub Actions

[View pipeline runs](https://github.com/rudyMarineH/FlamTestSol/actions)

## Prerequisites

- Java 24+
- Maven 3.6+
- Spring Boot 4.1.0
- AspectJ Weaver 1.9.x (enables `@Step` annotation interception for Allure reporting)
- Awaitility (transitive via `spring-boot-starter-test` — used for retry polling in async assertions)
- Chromium / Firefox (installed automatically by Playwright — see below)

## How to Run

**Run all tests**
```bash
mvn clean test
```

**Set API credentials (required for API tests)**
```bash
export API_USERNAME=admin
export API_PASSWORD=password123
```
or in yml

    username: ${API_USERNAME:admin}
    password: ${API_PASSWORD:password123}

**Run API tests and open Allure report**
```bash
mvn clean test -pl api && mvn allure:serve -pl api
```

**Run UI tests and open Allure report**
```bash
mvn clean test -pl web && mvn allure:serve -pl web
```

**Install Playwright browsers (first-time setup only — not part of `mvn test`)**
```bash
mvn -pl web exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps" -Dexec.classpathScope=test
```

**Run UI tests with a specific browser**
```bash
mvn clean test -pl web -Dbrowser=firefox
```

**Run API smoke tests**
```bash
mvn clean test -pl api -Dgroups="smoke" && mvn allure:serve -pl api
```

**Run API positive tests**
```bash
mvn clean test -pl api -Dgroups="positive" && mvn allure:serve -pl api
```

**Run API negative tests**
```bash
mvn clean test -pl api -Dgroups="negative" && mvn allure:serve -pl api
```

**Run UI smoke tests**
```bash
mvn clean test -pl web -Dgroups="smoke" && mvn allure:serve -pl web
```

**Run UI positive tests**
```bash
mvn clean test -pl web -Dgroups="positive" && mvn allure:serve -pl web
```

**Run UI negative tests**
```bash
mvn clean test -pl web -Dgroups="negative" && mvn allure:serve -pl web
```

## Test Strategy

I prioritized positive and negative test coverage across both API and UI layers, focusing on clear separation of concerns over breadth.

For API, I covered the full REST CRUD lifecycle and GraphQL query scenarios, validating both expected success responses and error handling for invalid inputs, missing fields, and unauthorized access.

For UI, I targeted the key form interactions — valid submissions, field validation, and boundary inputs — ensuring the most critical user flows are verified without over-specifying implementation details.

The layered architecture (client → steps → tests) was a deliberate choice to keep tests readable and maintainable: each layer has a single responsibility, so failures are easy to locate and tests are easy to extend.

## Challenges & Solutions

**1. GitHub Actions — Allure report links and gh-pages concurrency**
Publishing reports from two independent workflows to the same `gh-pages` branch caused race conditions. Solved by giving each job its own isolated deploy step (individual `peaceiris/actions-gh-pages` action per suite) instead of a shared common deploy, so `api/` and `web/` reports deploy independently without conflicting.

**2. Retry logic configured from YAML**
Implemented a `@UtilityClass` (`Retry`) whose `enabled` flag is set by a `RetryProperties` class annotated with `@PostConstruct`. The `@PostConstruct` method runs after Spring injects the YAML-bound properties and writes the value into the static field, bridging the Spring-managed config lifecycle with a static utility caller.

## What I Would Add With More Time

- **Increase test coverage** — expand edge case and boundary scenarios across both API and UI modules, add more negative tests for error codes and validation messages, and cover additional GraphQL query variations.
- **Tag-based selective execution** — a mechanism that detects which product areas changed in a build and runs only the tests tagged for those areas, reducing CI feedback time.
- **`@ConfigurationProperties` POJOs for all YAML files** — replace raw `@Value` injections with typed configuration classes (`@ConfigurationProperties(prefix = "...")`) for all application YAMLs, improving IDE support, validation, and refactoring safety.

## AI

Claude Code with custom skills was used to automate future test generation, reducing the manual effort of writing boilerplate test cases. Also added the **Graphify** plugin was used to build a knowledge graph of the codebase for faster navigation and architecture understanding.
