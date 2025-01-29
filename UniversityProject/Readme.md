# 🎓 PUP CORE - An Innovative Campus Productivity Suite
*Our Campus Connection, Organization, Resource, and Excellence.*

Welcome to **PUP CORE**, a comprehensive all-in-one software tailored for the **Polytechnic University of the Philippines (PUP)** to streamline campus life, boost productivity, and promote academic excellence. This project demonstrates the core **Object-Oriented Programming (OOP) principle of Polymorphism**, delivering dynamic, role-specific features for students and professors.

---

# 🚀 Features

## 1. **User Profile - ProfileHub**
#### *"Where your PUP story begins."*
- 🧑‍🎓 **Students**: View academic details (course, year level, subjects).
- 👨‍🏫 **Professors**: View department, office hours, and subjects taught.
- ✏️ Update profile details based on user role via overloading.

---

## 2. **Communication Portal - PUPConnect**
#### *"Stay connected, PUP-style."*
- 📩 Role-specific messaging: Students message classmates; professors broadcast announcements.
- 📨 Overloaded messaging: Private messages.
- 🔒 Secure, campus-only communication for distraction-free collaboration.

---

## 3. **Schedule Tracker - PUPriorities**
#### *"Make time for what matters—stay ahead, stay on track."*
- 📆 Consolidate academic timetables, extracurricular activities, and personal appointments.
- 🔔 Reminders to avoid conflicts.
- 🛠 Dynamic scheduling for students (deadlines, events) and professors (lectures, meetings).

---

## 4. **Event Hub - PUPEvents**
#### *"All your campus events in one place."*
- 🎉 Discover academic, cultural, and sports events.
- 📜 Tailored event information (guest speakers, schedules, team lineups).
- ➕ Add events via overloading (title, date, description).

---

## 5. **Campus Map - ISKOMPASS**
#### *"Your Iskolar compass to navigate campus life."*
- 🗺️ Role-specific navigation: Students find classrooms and libraries; professors locate offices and lounges.
- 🍴 Food stall details, including menus.
- 🧭 Overloaded route finder (basic directions or detailed routes).

---

## 6. **Achievement Dashboard - PUPExcel**
#### *"Celebrating your PUP-worthy achievements."*
- 🏆 Track academic, extracurricular, and personal milestones.
- 📜 Students: Document awards and certifications.
- 📚 Professors: Record research, speeches, and accolades.
- ✍️ Add achievements via overloading (title, description, date).

---

## 🎯 Application of Polymorphism

**Polymorphism** is central to the functionality of PUP CORE, enabling role-specific customizations through:
- **Method Overriding**: Customize core functionalities for students and professors.  
  Example: `viewProfile()` displays student-specific or professor-specific details.
- **Method Overloading**: Flexible functionalities for different parameters.  
  Example: `addEvent(String title, String date)` vs. `addEvent(String title, String date, String description)`.

---

## 🛠️ Tools & Technologies
- **JavaFX** for the GUI.
- **MySql** for database management.
- **Maven** for dependency management.
- **Scene Builder** for FXML design.

---

## 🏗️ Installation & Setup
#### 1. Clone the repository:
   ```bash
   git clone https://github.com/AshleyFullero/PUP-CORE.git
   ```
#### 2. Open the project in your favorite IDE.
#### 3. Install dependencies using Maven.
#### 4. Run the application:
   ```bash
   mvn javafx:run
   ```
### Alternative Installation Guide

#### 1. Download Required Files
Download the ZIP file and the SQL file for the project.

#### 2. Extract and Open Project
- Unzip the file.
- Right-click the project folder and select **"Open Folder as IntelliJ IDEA Project."**
#### 3. Update Database Credentials
- Navigate to **DBConnection** under the database package.
- Update the **USER** and **PASSWORD** fields with your MySQL credentials.
  ![DB Connection](screenshots/dbc.png)

#### 4. Create MySQL Connection
- Open MySQL and create a new connection with a custom name.
  ![MySQL Connection](screenshots/sql.png)

#### 5. Open the Connection
- Select the connection you created.
- Navigate to **Schemas** and click **Server.**
  ![MySQL Schemas](screenshots/sql1.png)

#### 6. Import Database
- Go to **Data Import.**
  ![Data Import](screenshots/sql2.png)
- Select **"Import from Self-Contained File."**
- Choose **pup_core.sql** as the file.
- From the dropdown menu, select **MySQL** and create a new schema named **pup_core.**
  ![Import Database](screenshots/sql3.png)

#### 7. Complete Database Import
- Click **Import Progress** and start the import process.
- Once the import is successful, return to IntelliJ.
  ![Import Progress](screenshots/sql4.png)
  ![Import Success](screenshots/sqlsuccess.png)

#### 8. Run the Program
- Locate the **Main** class in IntelliJ.
- Run the program and create an account to start using the application.
  ![Main Class](screenshots/main.png)

---


## 📸 Screenshots

#### 1. **User Login**
![User Login](screenshots/login.png)

#### 2. **Create Account for Students and Professors**
| General Account Creation  | Student Account Creation | Professor Account Creation|  
|--------------------------|----------------------------------------------------|--------------------------|  
| ![Create Account](screenshots/createacc.png) | ![Student Account Creation](screenshots/student_create.png) | ![Professor Account Creation](screenshots/professors_create.png) |  

#### 3. **Home**
| Professor User Home | Student User Home |  
|---------------------|-------------------|  
| ![Profile Update](screenshots/updateprof.png) | ![User Home](screenshots/ash.png) |  

#### 4. **Profile Hub - Edit Profile Picture & Details**
| Professor Profile  | Student Profile |  
|--------------------|-----------------|  
| ![Profile Details](screenshots/profdetails.png) | ![Student Profile](screenshots/student.png) |  

| Edit About Section | Keynotes & Research |  
|---------------------|---------------------|  
| ![Edit About](screenshots/editabout.png) | ![Keynotes](screenshots/keynotes.png) |  
| ![Research](screenshots/research.png) | ![Professor Profile](screenshots/excelprof.png) |  

| Professor Update | Student Update            |  
|------------------|---------------------------|  
| ![Professor Update](screenshots/profup.png) | ![User Profile View](screenshots/ash1.png) |  

#### 5. **Communication - User can create or view announcements & messages.**
| Create Announcement  and Messages             | View Message Inbox and Announcement            |  
|-----------------------------------------------|------------------------------------------------|  
| ![Announcements](screenshots/announcement.png) | ![Messages](screenshots/message.png)           |  
| ![Compose Message](screenshots/compose.png)   | ![Announcement View](screenshots/announce.png) |  

| Conversations & User Search | View User Profiles & Achievements |  
|-----------------------------|-----------------------------------|  
| ![Conversations](screenshots/convo.png) | ![View Profile](screenshots/view.png) |  
| ![Search User](screenshots/search.png) | ![Achievement Sharing](screenshots/achievement.png) |  

#### 6. **Organization - user can add task and create events.**
| Add Tasks & Events | Settings & Calendar |  
|-------------------|---------------------|  
| ![Add Task](screenshots/addtask.png) | ![Settings](screenshots/settings.png) |  
| ![Add Event](screenshots/add.png) | ![Calendar](screenshots/calendar.png) |  

| Task Management | Professor Task |  
|-----------------|-----------------|  
| ![Task Management](screenshots/task.png) | ![Professor Task](screenshots/proftask.png) |  

#### 7. **Resources - user can navigate the map smoothly**
| Navigate the Map | Facilities & Food Locations |  
|------------------|-----------------------------|  
| ![Map](screenshots/map.png) | ![Facilities](screenshots/facilities.png) |  
| ![Food Locations](screenshots/food.png) |  ![Additional Resources](screenshots/1.png) |  


#### 8. **Excellence - user can share Achievements, Thoughts, and Experiences**
| View Post        | Create post       |  
|------------------|-------------------|  
| ![Excellence](screenshots/excellence.png) | ![Post View](screenshots/postview.png) |  
| ![Post Creation](screenshots/post.png) | ![Image Post](screenshots/img_1.png) |  

---


## 📚Future Enhancements

### AI-Powered Friend Recommendations
Enhance user connections by suggesting friends based on shared activities, interests, and interactions.

### Real-Time Notification System
Implement instant notifications for events, messages, and important updates to keep users engaged and informed.

### Learning Vault - PUPStudyHub
A centralized platform for organizing learning materials across various courses and platforms.
- Students can seamlessly upload, categorize, and access their resources.
- Integrated note-taking features ensure that study materials remain consolidated for easy reference.

### Budget Tracker - ISKOBudget
A user-friendly financial management tool designed to help students make informed decisions about their finances.
- Manage expenses and monitor budgets.
- Set financial goals with personalized insights.
- Analyze spending patterns to develop smart budgeting habits.

Stay tuned for more updates and innovations to enhance the user experience!

---
## 📝 License
This project was developed solely for academic purposes under the guidance of the Polytechnic University of the Philippines (PUP).

---

## 🤝 Acknowledgments

### **TEAM OOPTIMUS PRIME**

#### **Team Leader and Programming Lead**
- **Ashley Nicole S. Fullero** — *Project Manager, Full-Stack Developer (Front-End & Back-End)*

#### **Team Members**
- **Nicko Adrian E. Baptista** — *Front-End, Documentation Specialist*
- **Jule Alexander B. Coralde** — *UI/UX Designer, Documentation Specialist*
- **Denelle Rose D. Nava** — *Front-End, Documentation Specialist*
- **Kyle Ethan C. Porciuncula** — *UI/UX Designer, Documentation Specialist*
- **Loreen May Jodi G. Gallos** — *Back-End, Documentation Specialist*
- **Clarisse Jem T. Salazar** — *Back-End, Researcher*

#### **Faculty Advisor**
- **Prof. Chris Piamonte** — *For invaluable guidance and mentorship*

---
## 🌐 Contact Information

If you have any questions, suggestions, or need assistance, feel free to reach out!

- **Email**: [ashleyfullero0906@gmail.com](mailto:ashleyfullero0906@gmail.com)
- **LinkedIn**: [Ashley Nicole Fullero](https://www.linkedin.com/in/ashleyfullero/)
- **GitHub**: [Ashley Fullero](https://github.com/AshleyFullero)

I’m happy to connect and discuss any queries or feedback you may have. Looking forward to hearing from you!
