---
name: cheatday-ci-checks
description: Run the CheatDay CI verification workflow locally after any code changes. Use whenever you modify code in this repository — run detekt, build, unit tests, UI tests on the local emulator, and coverage verification; fix any failures and repeat until everything passes before committing and pushing.
license: MIT
---

# CheatDay CI Checks

After **any** code change in this repository, run the checks that CI runs (see `.github/workflows/build.yml`), adapted for local execution, and confirm they pass **before** committing and pushing. Fix any failure and re-run until green. Do not commit code that fails a step.

## Prerequisites

- JDK 21 — verify with `java -version`; `JAVA_HOME` must point to a valid JDK 21.
- `local.properties` and the debug keystore already exist locally (no setup scripts needed).
- An Android emulator must be running and available on the local machine for the UI tests step.

## CI Steps (in order, mirroring build.yml)

1. **Detekt** (static analysis, `maxIssues: 0` in `config/detekt/detekt.yml`)
   ```
   ./gradlew detekt
   ```
2. **Build** (assemble + lint + all default tasks)
   ```
   ./gradlew build
   ```
3. **Unit tests**
   ```
   ./gradlew testStageDebugUnitTest
   ```
4. **UI tests** — run on the locally available emulator:
   ```
   ./gradlew connectedStageDebugAndroidTest
   ```
5. **Coverage verification** (thresholds: 70% line coverage per class and per method, from `app/jacoco.gradle.kts`)
   ```
   ./gradlew jacocoTestCoverageVerification
   ```

## Workflow

1. Make your code change.
2. Run steps 1-5 above in order.
3. If a step fails:
   - Read the error and the generated report (paths below).
   - Fix the root cause in your code — never weaken a check, suppress a rule, or skip the step.
   - Re-run the failing step, then continue with the remaining steps.
4. Repeat the loop until every step passes.
5. Only then commit and push.

## Fixing guidance

- **Detekt**: fix the reported finding, or let formatting rules auto-correct (`autoCorrect: true`). See the `kotlin-code-formatting` skill for the enforced style.
- **Unit tests**: determine whether the test or the production code is wrong and fix accordingly.
- **UI tests**: verify an emulator is running (`adb devices`); if none is available, start one before running this step.
- **Coverage**: `jacocoTestCoverageVerification` enforces 70% line coverage per class/method; add tests to cover newly added code.
- **Build**: fix compilation/lint errors surfaced by Gradle output.

## Reports location

- Unit test report: `app/build/reports/tests/**`
- UI test report: `app/build/reports/androidTests/**`
- Jacoco report: `app/build/reports/jacoco/**`

## Commit policy

- Commit and push only after all steps pass.
- Use a clear, conventional commit message describing the change.
