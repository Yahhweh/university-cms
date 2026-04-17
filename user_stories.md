### User Stories Specification: university-cms

Student daily schedule flow:

* Given: User is logged in as a Student
* Action: User navigates to the My Schedule menu
* Then: The system displays the daily schedule filtered by the current date

---

Student monthly schedule flow:

* Given: User is logged in as a Student
* Action: User navigates to My Schedule and selects the Get by Month option
* Then: The system displays the monthly schedule filtered by the selected month

---

Student teacher-specific subject list (Monthly):

* Given: User is logged in as a Student
* Action: User opens My Schedule and selects a Teacher's Name
* Then: The system displays a list of subjects scheduled for that teacher during
  the current month

---

Student teacher-specific subject list (Range):

* Given: User is logged in as a Student
* Action: User opens My Schedule, selects a Teacher's Name, and sets a two-week
  range
* Then: The system displays a list of subjects scheduled for that teacher during
  those two weeks

---

Teacher daily workload flow:

* Given: User is logged in as a Teacher
* Action: User navigates to the My Schedule menu
* Then: The system displays all lessons scheduled for the current day

---

Student full monthly lesson view:

* Given: User is logged in as a Student
* Action: User navigates to My Schedule and selects the Get by Month option
* Then: The system displays all lessons for the group filtered by the selected
  month

---

Admin rights

Given User `A` logged in with Admin role

* User `A` should be able to create/read/update/delete lessons.
* User `A` should be able to assign/reassign anything for lectures  
    (duration, studySubject, classroom, date).
* User `A` should be able to list all Users.
* User `A` should be able to do anything that Staff can do.   
* User `A` should be able to Create/Read/Update/Delete group information 
* User `A` can assign/ reassign Students to Group
* User `A` should be able to list all students in a group (read access).
---

Student/Lecturer rights

Given User `B` logged in with Student or Lecturer role

* User `B` should be able to list courses that he is enrolled in / assigned to (
  read access).
* User `B` (Lecturer) should be able to list all groups and their students
  within courses assigned to him.
* User `B` (Student) should be able to view only his own group
* User `B` should be able to list all groups information (read access).
* User `B` (Student) should be able to list all students related to his group (read access).
* User `B` (Lecturer) should be able to list all students in a group (read access).

---

Staff rights

Given User `C` logged in with Staff role.

* User `C` should be able to create/read/update/delete all lessons. 
* User `C` should be able to assign/reassign teacher to a lessons. 
* User `C` should be able to list Students and Lecturers (not staff and admins).
* User `C` should be able to view the list of courses and see the details of each course.
* User `C` should be able to Create/Read/Update group information.
* User `C` can assign/ reassign Students to Group
* User `C` should be able to list all students in a group (read access).

---

Mentor rights

Given User `D` logged in with Mentor role.  

* User `D` has the same rights as Student
* If User `D` is connected to the group he should be able to update info to this exact group
* If User `D` is connected to the group he should be able to list students of this exact group
* User `D` (Student) should be able to list all students related to his group (read access).

Mentor Role can be assigned to anybody;

---
