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
* Then: The system displays a list of subjects scheduled for that teacher during the current month

---

Student teacher-specific subject list (Range):

* Given: User is logged in as a Student
* Action: User opens My Schedule, selects a Teacher's Name, and sets a two-week range
* Then: The system displays a list of subjects scheduled for that teacher during those two weeks

---

Teacher daily workload flow:

* Given: User is logged in as a Teacher
* Action: User navigates to the My Schedule menu
* Then: The system displays all lessons scheduled for the current day

---

Student full monthly lesson view:

* Given: User is logged in as a Student
* Action: User navigates to My Schedule and selects the Get by Month option
* Then: The system displays all lessons for the group filtered by the selected month

---
