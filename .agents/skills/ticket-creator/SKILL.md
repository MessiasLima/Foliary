---
name: ticket-creator
description: Drafts, refines, de-duplicates, and creates GitHub issue tickets for project management work. Use this skill whenever the user wants to write, draft, refine, or create a ticket or GitHub issue for a bug, task, spike, feature, backlog item, or similar work item, even if they only describe the problem and do not name the ticket type. Also use this skill to review and improve existing tickets, to plan and break down features into multiple tickets, and whenever the user asks to "review a ticket", "break down a feature", "plan tickets", or shares a GH issue link for feedback.
compatibility: Requires GitHub CLI (`gh`), network access to the repository's GitHub issues, and the ability to open a browser or URL handler after issue creation.
---

# Ticket Creator

Use this skill to turn a user request into a high-quality GitHub issue through investigation, draft iteration, and explicit approval.

## Preflight

1. Check that `gh` is installed before doing any other ticket work.
   Run `gh --version`.
2. If `gh` is missing, stop immediately.
   Tell the user to install GitHub CLI, for example with `brew install gh`, `winget install GitHub.cli`, or from <https://cli.github.com/>.
3. If `gh` is installed but issue commands fail because authentication is missing, stop and ask the user to run `gh auth login`.

Do not draft or create the ticket until the GitHub CLI requirement is satisfied.

## Sentry Integration

If the user request contains a Sentry Issue (a short ID like `DISCORKIE-FT`, a regular ID like `111242862`, or a URL like `https://appoutlet.sentry.io/issues/111242862/?...`), before proceeding to ticket creation, perform the following Sentry fetch process:

1. **Preflight Sentry:** Check if the Sentry CLI is installed and authenticated by running `sentry auth status`. If the CLI is not installed or the user is not authenticated, inform the user and abort the ticket creation process.
2. **Extract the ID:** The ID can be a short ID (e.g., `DISCORKIE-FT`), a regular ID (e.g., `111242862`), or extracted from a URL (the number on the path after the `issues` segment).
3. **Fetch Issue Info:** Run `sentry issue view [issue_id]` to fetch the basic issue information.
4. **Investigate Events:** To further understand the problem, list the events for the issue by running `sentry event list [issue_id] --json`. Then, extract the event ID from the `id` field of the JSON output and fetch its details by running `sentry event view [event_id]`.
5. **Proceed:** After gathering and understanding this information, proceed to the regular bug ticket creation workflow, incorporating these technical details into the draft (e.g., in the Description, Steps to Reproduce, and Additional Information sections).

## Understand The Assignment

Before writing anything, confirm you can answer three questions clearly:

1. **What problem does this solve?** — Be specific. "The login page crashes" not "auth is broken."
2. **Who benefits?** — Which user persona, role, or system? A vague answer means the scope is unbounded.
3. **What does "done" look like?** — What observable change would a teammate or QA engineer see and verify?

If any of these three answers are unclear or missing, **stop and ask the user focused questions**. A few targeted questions produce a better ticket than a draft built on assumptions.

Also determine the ticket type and intention:

1. What is the likely ticket type?
2. What is the intention: fix broken behavior, deliver new capability, track operational work, or investigate uncertainty?

Infer the type when it is obvious:

- `bug`: broken behavior, incorrect result, regression, crash, inconsistency
- `task`: operational or maintenance work, cleanup, upgrade, migration, documentation, routine implementation
- `spike`: investigation, discovery, technical evaluation, unknown scope, research before implementation
- `feature`: new user-facing or system-facing capability

If the type is ambiguous after reading the request, ask one concise clarifying question before continuing.

## Select The Template

Open only the template file for the chosen ticket type:

- Bug: `assets/bug.md`
- Task: `assets/task.md`
- Spike: `assets/spike.md`
- Feature: `assets/feature.md`

Treat the selected file as the source of truth for the draft structure. Keep the heading structure intact. If the template marks a section as optional, remove that section when it adds no value.

## Workflow

There are three distinct entry points into this skill. Choose based on what the user is doing:

- **Creating a new ticket** → Follow the full sequence below.
- **Reviewing an existing ticket** → Skip to [Improving Existing Tickets](#improving-existing-tickets).
- **Planning and breaking down a feature** → Skip to [Feature Planning & Ticket Breakdown](#feature-planning--ticket-breakdown).

### New ticket creation sequence

Follow this sequence every time:

1. If a Sentry ID or link was provided, execute the Sentry Integration steps.
2. Understand the assignment and infer or confirm the ticket type.
3. Search for existing issues in the current repository to avoid duplicates.
4. Investigate the codebase for relevant files, implementation details, constraints, and technical context.
5. Draft the ticket using the exact structure from the chosen template.
6. Perform a [Self-Review](#self-review) of the draft before showing it to the user.
7. Present the draft to the user and ask whether it is good enough or what should change.
8. Revise and re-present the draft until the user explicitly approves creation.
9. Derive a slug from the issue title by lowercasing and replacing spaces with underscores (e.g., `fix_image_parsing`). Write the body to `.opencode/issues/<slug>.md`, creating the directory if it does not exist, then create the issue with `gh issue create --body-file .opencode/issues/<slug>.md`.
10. Open the created issue URL in the browser: `open <url>` on macOS, `xdg-open <url>` on Linux, or `start <url>` on Windows.

Never skip the duplicate check, codebase investigation, self-review, or explicit approval steps.

## Duplicate Check

Before drafting, search for similar issues in the current repository.

Use `gh issue list --state all --limit 50 --search "<keywords>"`.

Build the search keywords from the user request and the inferred title/theme. Look for exact duplicates and closely related issues.

If you find likely duplicates:

1. Show the relevant issue numbers and titles to the user.
2. Explain briefly why they look overlapping.
3. Ask whether to stop, update the existing issue instead, or continue creating a new ticket.

If the user chooses not to continue, stop there.

## Codebase Investigation

Investigate the local repository before writing the draft.

- Search for the relevant feature area, files, classes, screens, modules, endpoints, or configuration.
- Read the files that give the future assignee useful context.
- Extract implementation details, constraints, current behavior, suspected causes, or related files.
- Use these findings to make the ticket more actionable and less generic.

The draft should reflect what you learned, especially in the description, current knowledge, additional information, references, or acceptance criteria.

## Drafting Rules

When drafting the issue:

- Present both the proposed title and body.
- Put the draft body in a fenced Markdown code block so the user can review it clearly.
- Do not create the issue yet.
- CRITICAL: DO NOT include the ticket title anywhere in the body of the ticket. The GitHub issue title is handled separately and must never be duplicated as a heading or text inside the issue body description.
- Keep the template structure strict for the chosen type.
- Keep the title concise and descriptive.
- Include relevant technical context from the repository investigation.
- Mention duplicate issues if the user still wants to proceed despite overlap.

Type-specific expectations:

- `bug`: include technical findings in `## Description` and `## Additional Information`, plus concrete reproduction and expected versus actual behavior.
- `task`: keep the title outcome-oriented and ensure acceptance criteria describe outcomes, not vague activity.
- `spike`: title must start with `Spike: ` and `### Current Knowledge` should capture what the investigation already uncovered.
- `feature`: the description should cover the problem, user benefit, technical considerations, and design references when available; each acceptance criterion should use a `GIVEN` / `WHEN` / `THEN` checklist item.

## Self-Review

Before presenting any draft to the user, challenge your own work. Walk through these checks silently, and fix issues before showing the ticket:

1. **Read the title alone.** Does it tell you what this ticket accomplishes without opening it? A title like "Fix bug" or "Update module" fails this test. The title should describe the outcome, not the activity.

2. **Read the description as someone with no context.** A developer who has never heard of this feature should understand the "why" immediately and know where the scope ends. If the description assumes tribal knowledge, fill in the gaps.

3. **Read each acceptance criterion.** Two questions:
   - Could a QA engineer verify it without asking a developer what it means?
   - Could an AI coding agent use it as a definition of done?
   If the answer to either is no, rewrite the criterion to be concrete and observable.

4. **Hunt for weasel words.** "Properly", "appropriately", "as expected", "correct", "handle errors correctly" — these mean nothing without a concrete definition. Replace every weasel word with a specific, verifiable behavior. Instead of "handles errors properly", write "displays an inline error message below the field and disables the submit button until the field is valid."

5. **Check for hidden scope.** Ask: does this ticket quietly require work that should be its own ticket? Examples:
   - A "simple UI tweak" that requires a new API endpoint
   - A "quick fix" that needs a database migration
   - A "minor change" that affects three platforms (iOS, Android, web)
   Flag these hidden dependencies — either scope them explicitly or split them into separate tickets.

Proactively surface what's missing: error states, empty states, loading states, edge cases, permissions, analytics/tracking, and backwards compatibility. If the ticket is silent on these, add them or flag them for discussion.

If you find issues during self-review, fix them before presenting. The draft the user sees should already have passed these checks.

## Iteration Loop

After every draft:

1. Ask the user if the draft is good enough or what should be improved.
2. Wait for feedback.
3. Apply the requested changes.
4. Present the revised draft again.

Repeat until the user gives explicit approval to create the issue.

Approval must be explicit. Accept clear confirmations such as `approved`, `create it`, `yes, create the issue`, or equivalent.

## Issue Creation

Once the user explicitly approves:

1. Derive a slug from the issue title: lowercase the title and replace spaces (and any consecutive non-alphanumeric characters) with underscores, trimming leading/trailing underscores. For example, `Fix image parsing crash` → `fix_image_parsing_crash`.
2. Ensure the `.opencode/issues/` directory exists (create it if needed).
3. Write the approved body to `.opencode/issues/<slug>.md` using the Write file tool. Write the raw markdown content exactly as it appears inside the draft code block — **do not include the opening ` ```markdown ` or closing ` ``` ` fence delimiters in the file**. The file must contain only the plain markdown body that GitHub will render.
4. Create the issue: `gh issue create --title "..." --body-file .opencode/issues/<slug>.md`
   Capture the URL printed by `gh issue create` (it is the last line of its output).
5. Open the issue URL in the default browser:
   - macOS: `open <url>`
   - Linux: `xdg-open <url>`
   - Windows: `start <url>`

Never use `--body` with an inline string for multiline content — shell escaping will corrupt backticks and other characters. Always use `--body-file`.

Do not create the issue before approval. Do not silently create a draft issue behind the scenes.

## Output Expectations

During the draft phase, the response should contain:

- The inferred ticket type
- Any duplicate issue findings
- Relevant codebase findings
- The proposed title
- The proposed body in a fenced Markdown code block
- A direct prompt asking the user whether to approve or what to change

After creation, the response should contain:

- The final issue title
- The path where the issue body was saved (`.opencode/issues/<slug>.md`)
- The created issue URL
- Confirmation that the browser was opened

## Improving Existing Tickets

When the user shares a ticket for review (paste, link, or file), follow this workflow:

### 1. Read with zero context

Read the ticket as a developer who has never heard of this feature before. Note every place where you needed to guess, assume, or fill in gaps — those are exactly the places the ticket is weak.

### 2. Rewrite, don't just critique

Don't just say "this could be clearer." Rewrite the weak parts inline so the user can see the difference between the original and the improved version. Present the improved ticket as a complete artifact in a fenced Markdown code block.

### 3. Provide structured feedback

Group your observations in chat by severity:

- **Blockers** — The ticket can't be worked on as-is. Examples: missing acceptance criteria, contradictory requirements, undefined scope, no way to verify completion.
- **Improvements** — The ticket is workable but likely to cause confusion or rework. Examples: vague language, implicit assumptions, missing edge cases, weasel words.
- **Suggestions** — Nice-to-haves that would raise quality. Examples: better title, additional context, links to related tickets or code files.

### 4. Explain the why

For every improvement you suggest, explain *why* it matters. Example: "This criterion says 'handles errors correctly' — a dev might interpret that as a toast message, a retry, or a redirect. Specify which behavior you want."

### 5. Present your review

Keep the improved ticket and the rationale separate:
- The **improved ticket** goes in a fenced Markdown code block as a clean artifact.
- The **critique and rationale** go in the chat message outside the code block, organized by severity level.

After presenting the review, ask whether the user wants to create the improved version as a new issue or update the existing one.

## Feature Planning & Ticket Breakdown

When the user describes a feature or goal (not a single ticket), follow this workflow:

### 1. Clarify the goal

Make sure you understand the user's intent before breaking anything down. Confirm: who is this for, what problem does it solve, and what's the minimum viable scope? If the goal is still fuzzy, ask focused questions.

### 2. Think through all the work

Before proposing tickets, mentally walk through every layer:
- Backend API changes (new endpoints, schema migrations, auth, rate limiting)
- Frontend UI (Android, iOS, web — each platform is potentially its own scope)
- Design and UX (new screens, states, flows)
- Data (new tables, migrations, backfills, data contracts)
- Edge cases (empty states, loading states, error states, permissions)
- Dependencies (what must exist before this can start)

### 3. Propose a breakdown

Present a summary in chat with:
- **Ticket titles** grouped by area (backend, frontend, design, etc.)
- **Dependencies and sequencing** — which tickets block others, suggested order of execution
- **Scope boundaries** — what each ticket covers and what it explicitly excludes

### 4. Iterate

The first breakdown is a draft. Invite the user to challenge it, rearrange tickets, merge or split them. Refine until the user is satisfied, then offer to create each ticket individually using the standard creation workflow.

Keep tickets small and focused. If a ticket covers more than one concern (e.g., "add the endpoint AND build the UI"), suggest splitting it.

## What You Don't Do

- **Don't write code or include code snippets in tickets.** Tickets describe what to build, not how to build it.
- **Don't make architectural decisions.** Surface the questions (e.g., "Should this use a new table or extend the existing one?") and let the team decide.
- **Don't assign tickets to individuals** unless the user explicitly asks.
- **Don't estimate with false precision.** If you're unsure about story points, say so and suggest a range or a spike ticket to investigate.
- **Don't assume the user has thought of everything.** Proactively surface: error states, empty states, loading states, edge cases, permissions, analytics/tracking, and backwards compatibility.
- **Don't create mega-tickets.** If a ticket covers more than one concern, suggest splitting it. Small, focused tickets ship faster.
