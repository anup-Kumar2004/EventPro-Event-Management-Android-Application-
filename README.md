📱 EventPro – Event Management Android App
EventPro is an Android application designed to simplify event management, especially in college campuses where multiple events often occur simultaneously. The app is built using Java and XML in Android Studio, with full backend support using Firebase Realtime Database and Firebase Authentication, along with Google Sign-In Integration via Google Cloud Console and OAuth 2.0.

🎯 Objective
The goal of EventPro is to provide a centralized platform for students and organizers:

Hosts can create and manage events.

Users can view upcoming events, join them, and receive invites.

Makes college event participation more organized and accessible.

🔧 Tech Stack & Tools Used
Frontend: Java, XML (Android Studio)

Authentication: Firebase Authentication, Google Sign-In (OAuth 2.0)

Database: Firebase Realtime Database

Cloud Platform: Google Cloud Console

UI Components: RecyclerView, CardView, Navigation Drawer

Additional Tools: GitHub (Versioning)

🧭 App Workflow
Step 1: User Authentication
Users can log in using either Firebase Email Authentication or Google Sign-In. Each user gets a unique UID to maintain their session and data.

<p align="center">
<img src="assets/img1.jpg" width="250" alt="Login Screen">
<img src="assets/img2.jpg" width="250" alt="Register Screen">
<img src="assets/img3.jpg" width="250" alt="Google Sign-In">
</p>

Step 2: Choose to Host or Join Event
After authentication, users are directed to a screen where they choose whether to Host an Event or Join an Event.

<p align="center">
<img src="assets/img4.jpg" width="250" alt="Enter Name">
<img src="assets/img5.jpg" width="250" alt="Choice Screen">
</p>

Step 3: Host an Event
Hosts can add an event with name, date, time, and details. They can also view a list of events they’ve hosted, and edit or delete existing events.

<p align="center">
<img src="assets/img7.jpg" width="250" alt="Create Event Details">
<img src="assets/img10.jpg" width="250" alt="Date Picker">
<img src="assets/img11.jpg" width="250" alt="Event Card">
<img src="assets/img12.jpg" width="250" alt="Edit Event">
</p>

Step 4: Invite Users
Hosts can invite other users to specific events by entering their email. Invited users receive a personal invite in their dashboard.

<p align="center">
<img src="assets/img14.jpg" width="250" alt="Invite Screen">
</p>

Step 5: View Participants
Hosts can see a list of all users who have joined their event.

<p align="center">
<img src="assets/img13.jpg" width="250" alt="Participants Nav">
<img src="assets/img19.jpg" width="250" alt="Participants List">
</p>

Step 6: Join Event
Users can view and join any event listed in the system. The joined events are shown in a separate tab.

<p align="center">
<img src="assets/img15.jpg" width="250" alt="Join Event List">
<img src="assets/img16.jpg" width="250" alt="Joined Event Details">
</p>

Step 7: Invited Events
Users can view and respond to event invites sent by hosts.

<p align="center">
<img src="assets/img18.jpg" width="250" alt="Invited Nav">
<img src="assets/img17.jpg" width="250" alt="Accept or Decline">
</p>
