# 📱 EventPro – Smart Event Management Android App

**EventPro** is a full-featured Android application designed to streamline the process of hosting and attending events, particularly within college campuses where multiple activities run concurrently. The app provides both hosts and participants with a seamless, centralized platform to manage and engage in event activities.

Developed using **Java and XML** in **Android Studio**, EventPro is powered by a robust **Firebase** backend, leveraging:
- 🔐 Firebase Authentication (Email & Google Sign-In via OAuth 2.0)
- ☁️ Firebase Realtime Database
- 🌐 Google Cloud Console for OAuth integration and project management

---

## 🎯 Objective

The primary aim of EventPro is to provide an intuitive and centralized platform where:

- 🧑‍💼 **Hosts** can easily create, manage, and invite users to events.
- 🧑‍🤝‍🧑 **Users** can browse, join, or respond to event invitations in a structured environment.
- 📆 College events become more organized, engaging, and accessible to everyone.

---

## 🔧 Tech Stack & Tools Used

| Category           | Technologies / Tools                                |
|--------------------|------------------------------------------------------|
| **Frontend**       | Java, XML (Android Studio)                          |
| **Authentication** | Firebase Authentication, Google Sign-In (OAuth 2.0) |
| **Database**       | Firebase Realtime Database                          |
| **Cloud Services** | Google Cloud Console                                |
| **UI Components**  | RecyclerView, CardView, Navigation Drawer           |
| **Version Control**| Git & GitHub                                         |

---

## 🧭 App Workflow

### 🔐 Step 1: User Authentication

Users can log in via:
- **Email/Password**
- **Google Sign-In**

Each user is assigned a **unique UID** for personalized data handling.

<p align="center">
<img src="assets/img1.jpg" width="250" alt="Login Screen">
<img src="assets/img2.jpg" width="250" alt="Register Screen">
<img src="assets/img3.jpg" width="250" alt="Google Sign-In">
</p>

---

### 🚦 Step 2: Choose to Host or Join Event

After login, users are prompted to select between:
- **Hosting an Event**
- **Joining an Event**

<p align="center">
<img src="assets/img4.jpg" width="250" alt="Enter Name">
<img src="assets/img5.jpg" width="250" alt="Choice Screen">
</p>

---

### 🏗️ Step 3: Host an Event

Hosts can:
- Create a new event with name, date, time, and details
- View their hosted events
- Edit or delete existing events

<p align="center">
<img src="assets/img7.jpg" width="250" alt="Create Event Details">
<img src="assets/img10.jpg" width="250" alt="Date Picker">
<img src="assets/img11.jpg" width="250" alt="Event Card">
<img src="assets/img12.jpg" width="250" alt="Edit Event">
</p>

---

### ✉️ Step 4: Invite Users

Hosts can invite other users to their events using email. Invites show up in the invitee’s dashboard.

<p align="center">
<img src="assets/img14.jpg" width="250" alt="Invite Screen">
</p>

---

### 🧑‍🤝‍🧑 Step 5: View Participants

Hosts can view the list of users who have joined their events.

<p align="center">
<img src="assets/img13.jpg" width="250" alt="Participants Nav">
<img src="assets/img19.jpg" width="250" alt="Participants List">
</p>

---

### 🎟️ Step 6: Join Event

Users can:
- Browse all available events
- Join events they are interested in
- View their joined events in a separate tab

<p align="center">
<img src="assets/img15.jpg" width="250" alt="Join Event List">
<img src="assets/img16.jpg" width="250" alt="Joined Event Details">
</p>

---

### 📨 Step 7: Invited Events

Users can view and respond to event invitations from hosts with **Accept** or **Decline** options.

<p align="center">
<img src="assets/img18.jpg" width="250" alt="Invited Nav">
<img src="assets/img17.jpg" width="250" alt="Accept or Decline">
</p>
