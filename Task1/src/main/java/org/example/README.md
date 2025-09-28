# Student Grade Management System

## Overview
This is a comprehensive Java console application for managing student grades. The system allows you to input student information, manage grades, and generate detailed reports with statistical analysis.

## Features
- **Student Management**: Add, remove, and search students
- **Grade Management**: Add multiple grades per student (up to 10 grades)
- **Statistical Analysis**: Calculate averages, highest/lowest scores
- **Comprehensive Reports**: View individual student details and class summary reports
- **Grade Distribution**: Analyze grade distribution across the class
- **Top Performers**: Identify top 3 performing students
- **Data Validation**: Input validation and error handling

## Requirements
- Java JDK 8 or higher
- Command line or terminal

## Compilation Instructions
1. Navigate to the `codealpha_tasks` directory
2. Compile the Java files:
   ```bash
   javac *.java
   ```

## Running the Application
```bash
java StudentGradeManager
```

## Usage Guide

### Main Menu Options:
1. **Add New Student** - Create a new student record with name and ID
2. **Add Grades to Student** - Add grades (0-100) to existing students
3. **View Student Details** - Display detailed information for a specific student
4. **View All Students** - Overview of all students with their statistics
5. **Display Summary Report** - Comprehensive class statistics and analysis
6. **Search Student** - Find students by name or ID
7. **Remove Student** - Delete a student record
8. **Exit** - Close the application

### Grading Scale:
- A: 90-100
- B: 80-89
- C: 70-79
- D: 60-69
- F: 0-59

## Sample Usage Workflow:
1. Start the application
2. Add students using option 1
3. Add grades for students using option 2
4. View individual details using option 3
5. Generate comprehensive reports using option 5

## Code Structure:
- `Student.java` - Student class with grade management functionality
- `StudentGradeManager.java` - Main application with console interface
- `TestStudentGradeSystem.java` - Sample test class with demo data

## Data Storage:
- Uses ArrayList to store student objects in memory
- Maximum 10 grades per student
- Data is lost when the application is closed (no persistent storage)

## Error Handling:
- Input validation for grades (0-100 range)
- Duplicate student ID prevention
- Invalid input handling
- Empty field validation

## Author
Created for CodeAlpha Tasks - Java Development Internship
