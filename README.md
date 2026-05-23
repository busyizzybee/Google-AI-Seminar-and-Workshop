# Google-AI-Seminar-and-Workshop
# Campfire — Student Org Operating System (OS)

[![Role: Senior UX/UI & Product](https://img.shields.io/badge/Role-Senior_UX%2FUI_Template-orange?style=flat-square)]()
[![Target: Student Organizations](https://img.shields.io/badge/Target-Student_Org_Operations-blue?style=flat-square)]()

A comprehensive product blueprint and UI/UX prompt specification for **Campfire**, a unified SaaS platform designed to eliminate institutional memory loss, streamline operations, and bridge cross-committee silos in student organizations.

---

## 📌 Context & Objective

Student organizations face unique operational challenges—primarily a 100% leadership turnover rate every 12 months, fragmented communication, and siloed workflows. **Campfire** acts as a centralized operating system that links recruitment, documentation, finance, events, and task management into a single, cohesive user experience.

This repository serves as a structured documentation template to guide AI generation, developer scoping, or UI wireframing for the platform's core modules.

---

## 🏗️ Product Architecture & Core Modules

### 1. The Induction Pipeline `(Recruitment & Onboarding)`
*   **The Problem:** Chaotic onboarding utilizing a disjointed mix of external forms, manual emails, and cluttered group chats where new recruits get lost.
*   **UI/UX Requirements:** 
    *   An administrative Applicant Portal and panel dashboard.
    *   Dynamic UI state changes: toggling an applicant to "Accepted" must instantly trigger internal profile generation, automatic committee channel routing, and unlock a gamified onboarding checklist (e.g., *“Read Handbook”*, *“Attend Orientation”*).

### 2. Living Turnovers & The Vault `(Knowledge & Documentation)`
*   **The Problem:** Standard Operating Procedures (SOPs), legacy data, and organizational history vanish into personal Google Drives when officers graduate.
*   **UI/UX Requirements:** 
    *   A structural wiki/repository view built around **Roles** (e.g., *VP of Finance View*) rather than individual user accounts.
    *   Persistent access permissions that house historical budget ledgers, document templates, and end-of-year terminal reports passed down across generations.

### 3. The Ledger `(Compliance, Finance & Partnerships)`
*   **The Problem:** Fragmented budget tracking, painful manual liquidation processes, and disorganized corporate sponsorship tracking.
*   **UI/UX Requirements:** 
    *   Real-time budget tracking data tables.
    *   A drag-and-drop file uploader explicitly designed for liquidation receipts.
    *   A visual CRM pipeline tracking external relations from initial pitch deck delivery to a signed Memorandum of Agreement (MOA).

### 4. The Pulse `(Member Engagement & Events)`
*   **The Problem:** Disconnected event planning toolkits and an inability to track fading member participation before burnout occurs.
*   **UI/UX Requirements:** 
    *   An event creation wizard utilizing pre-made **"Event Blueprints"** (e.g., selecting a *Webinar Blueprint* automatically populates tasks, cross-committee dependencies, and marketing timelines).
    *   A fast-loading QR-code attendance tracker view.
    *   A gamified member participation scoreboard.

### 5. The Handoff `(Communications & Project Tracking)`
*   **The Problem:** Structural communication siloes where committees (Logistics, Marketing, Finance) are unaware of each other's progress.
*   **UI/UX Requirements:** 
    *   A multi-step, visual task workflow editor highlighting cross-committee dependencies.
    *   *System Event Example:* Marking a Logistics task as "Venue Booked" instantly shifts the UI state and triggers an automated notification to the Marketing team to publish materials.

---

## 📋 System Output Specifications

When processing or developing these modules, the generated blueprints must explicitly detail the following three pillars:

```mermaid
graph TD
    A[Module Specification] --> B[1. Screen Layout & Navigation]
    A --> C[2. Key Interactive Components]
    A --> D[3. Defined User Flows]
    B --> B1[Sidebar, Canvas, Positioning]
    C --> C1[Buttons, Tables, Toggles, Uploaders]
    D --> D1[Step-by-step Officer Interactions]

---

**## Logo Prompt
**A icon-style app icon design for an application: designed to solve institutional turnover, siloed workflows. The logo is a simple campfire with the text "Campfire" which is centered, set against a black background with simple details. High-resolution game art and graphics for a mobile app. Pictorial style 

