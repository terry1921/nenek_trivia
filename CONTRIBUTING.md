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
   git checkout -b feat/my-new-feature
   ```
4. **Make your changes**
5. **Commit your changes** (see Commit Guidelines below)
6. **Push to your branch**
   ```bash
   git push origin feat/my-new-feature
   ```
7. **Open a Pull Request**

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
- `chore`: Changes to the build process or auxiliary tools

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
