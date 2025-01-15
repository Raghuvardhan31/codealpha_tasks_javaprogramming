
# Student Grade Management System

This is a simple Java-based program to manage and calculate student grades for multiple subjects. It allows users to input student names and their grades based on predefined grade boundaries. The program also calculates the average grade, highest grade, and lowest grade for each student.

## Features
- **Add Student Marks**: Input student names and grades for each subject.
- **Grade Calculation**: Converts letter grades (O, A, B, C, D, F) into numerical values.
- **Statistics**: Calculate the average, highest, and lowest grades for each student.
- **Display Student Details**: Display student names and their grades for each subject.

## Grade Boundaries
- **O**: Marks >= 90
- **A**: Marks 80 <= x < 90
- **B**: Marks 70 <= x < 80
- **C**: Marks 60 <= x < 70
- **D**: Marks 50 <= x < 60
- **F**: Marks < 50

## Prerequisites
- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.

## How to Run
1. **Compile the program**:
   ```bash
   javac Main.java
Run the program:
bash
Copy code
java Main
Usage
Input the number of students: The program will prompt you to enter the number of students.
Input the number of subjects: The program will prompt you to enter the number of subjects.
Add student marks: For each student, input the name and their grades for each subject.
View student details: After entering the data, the program will display the student names and their grades.
View calculated statistics: The program will calculate and display the average grade, highest grade, and lowest grade for each student.
Example Output
yaml
Copy code
Enter the no of students:
2
Enter the number of subjects:
3
Enter the name of the student:
John
Enter the grades of the student based on the marks:
1. Marks >= 90 : O
2. Marks 80 <= x < 90 : A
3. Marks 70 <= x < 80 : B
4. Marks 60 <= x < 70 : C
5. Marks 50 <= x < 60 : D
6. Marks < 50 : F
Enter the grade of John for subject1: A
Enter the grade of John for subject2: B
Enter the grade of John for subject3: O
Enter the name of the student:
Jane
Enter the grades of the student based on the marks:
1. Marks >= 90 : O
2. Marks 80 <= x < 90 : A
3. Marks 70 <= x < 80 : B
4. Marks 60 <= x < 70 : C
5. Marks 50 <= x < 60 : D
6. Marks < 50 : F
Enter the grade of Jane for subject1: C
Enter the grade of Jane for subject2: D
Enter the grade of Jane for subject3: A

Student Name: John
Student grades:
A
B
O

Student Name: Jane
Student grades:
C
D
A

name :  John:
Average Grade: B 
Highest Grade: O
Lowest Grade: B

name :  Jane:
Average Grade: C 
Highest Grade: A
Lowest Grade: D
Code Explanation
Student Class: Contains methods to input student marks, calculate statistics, and display student information.

addStudentMarks(): Adds student names and grades for each subject.
calculateStats(): Calculates the average grade, highest grade, and lowest grade for each student.
display(): Displays the student names and their grades.
gradeToNumber(): Converts letter grades to numerical values.
numberToGrade(): Converts numerical values back to letter grades.
Main Class: The entry point of the program where user inputs are taken and the student data is processed.

