# Contributing Guidelines

Thank you for considering contributing to **Nenek Trivia**!

We welcome all contributions, whether it's fixing bugs, adding features, improving documentation, or suggesting new ideas.

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps:

1. **Fork the repository**
2. **Clone your fork**
   ```bash
   git clone https://github.com/terry1921/nenek_trivia.git
   ```
3. **Create your branch**
   ```bash
   git checkout development
   git checkout -b feat/my-new-feature
   ```
4. **Make your changes**
5. **Commit your changes** (see Commit Guidelines below)
6. **Push to your branch**
   ```bash
   git push origin feat/my-new-feature
   ```
7. **Open a Pull Request targeting `development`**

---

## ✏️ Commit Guidelines

This project enforces **Angular-style commit messages** using **commitlint**.

**Structure:**
```
<type>: <short summary>

[optional body]

[optional footer]
```

**Allowed types:**
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (white-space, formatting)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests
- `build`: Changes to the build process or dependencies
- `chore`: Changes to auxiliary tools

**Examples:**
```bash
feat: add user authentication
fix: correct login crash on splash screen
docs: update README with setup instructions
```

---

## 🛡️ Pre-commit Hooks

All commits will automatically trigger:

- **Ktlint** for Kotlin code formatting.
- **Gradle build validation** (`./gradlew build --dry-run`).
- **Unit tests execution** (`./gradlew testDebugUnitTest`).
- **Commit message validation** with **commitlint**.

Please fix any issues before pushing your commits.

You can manually run the hooks on all files with:
```bash
pre-commit run --all-files
```

---

## 📝 Code Style

- Follow Kotlin coding conventions.
- Ensure code is modular and loosely coupled.
- Write meaningful commit messages.
- Add tests where necessary.

---

## 🤝 How to Contribute

- Open an issue to discuss your idea.
- Fork the project and create a branch.
- Make your changes with clean, readable commits.
- Submit a Pull Request for review.

All contributions, small or large, are appreciated! 🚀

---

Thank you for helping make **Nenek Trivia** better! 🙌

## Branches and releases

Create work branches from `development` and open their pull requests against
`development`. Use lowercase names such as `feat/account-deletion`
(`feature/` also works), `fix/login-crash`, `build/firebase-distribution`
or `docs/play-console`. Use Conventional Commits, including `build:` for
build or dependency changes. Promote tested changes through a pull request
from this repository's `development` branch to `main`.

### GitHub branch protection

Protect both `development` and `main` with a branch ruleset. Require a
pull request before merging, block force pushes and branch deletion, and
require **PR branch policy** and **Android build and tests** from GitHub
Actions. Keep the bypass list empty so the policy applies to administrators.
The checks must run on a pull request before GitHub offers them in the
ruleset picker. For initial setup, run the checks on the configuration PR
to `development` and on the promotion PR to `main` before merging them.

Android CI uses the repository secret `GOOGLE_SERVICES_JSON` (base64 of
`google-services.json` containing both Android package registrations).
GitHub does not pass this secret to PRs from forks. A maintainer can review
the changes and run CI from a conventional branch in this repository.

### Firebase App Distribution

After a merge updates protected `development`, GitHub Actions builds the
`fenixartsFirebaseDebug` APK and distributes it to the configured tester
groups. Set up the `firebase-testing` Actions environment for
`development` with the following values.

| Kind | Name | Value |
| --- | --- | --- |
| Variable | `FIREBASE_APP_ID` | Firebase Android App ID for `com.fenixarts.nenektrivia` in the test Firebase project |
| Variable | `FIREBASE_TESTER_GROUPS` | Comma-separated group aliases created in Firebase App Distribution |
| Secret | `GOOGLE_SERVICES_JSON` | Base64 of the test project's `google-services.json`. Set this in the environment to override the repository secret for distribution |
| Secret | `FIREBASE_SERVICE_ACCOUNT` | JSON credentials for a dedicated service account with the Firebase App Distribution Admin role on the test project |
| Secret | `FIREBASE_DEBUG_KEYSTORE` | Base64 of a dedicated, persistent Android debug keystore with the standard Android debug alias and passwords |
| Variables | `GOOGLE_WEB_CLIENT_ID`, `FACEBOOK_APP_ID` | Test project sign-in configuration |
| Secrets | `FACEBOOK_CLIENT_TOKEN`, `CONFIG_API` | Test project integration values |

Register the debug signing certificate in the test Firebase and Meta
configurations as applicable. Reuse the same keystore for each run so
testers can install updates over previous builds. The distribution workflow
uses its own Google Services configuration when the environment secret
is set. Verify the first successful run in Firebase App Distribution and
install the APK from a tester invitation.
