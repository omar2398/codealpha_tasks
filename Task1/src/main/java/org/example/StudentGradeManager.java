package org.example;

import java.util.*;

public class StudentGradeManager {
    private ArrayList<Student> students;
    private Scanner scanner;

    public StudentGradeManager() {
        this.students = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    STUDENT GRADE MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Add New Student");
        System.out.println("2. Add Grades to Student");
        System.out.println("3. View Student Details");
        System.out.println("4. View All Students");
        System.out.println("5. Display Summary Report");
        System.out.println("6. Search Student");
        System.out.println("7. Remove Student");
        System.out.println("8. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-8): ");
    }

    public void addStudent() {
        System.out.print("\nEnter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Error: Name cannot be empty!");
            return;
        }

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Error: ID cannot be empty!");
            return;
        }

        if (findStudentById(id) != null) {
            System.out.println("Error: Student with ID " + id + " already exists!");
            return;
        }

        Student student = new Student(name, id);
        students.add(student);
        System.out.println("Student added successfully: " + student.getName() + " (ID: " + student.getId() + ")");
    }

    public void addGradesToStudent() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found. Please add students first.");
            return;
        }

        System.out.print("\nEnter student ID: ");
        String id = scanner.nextLine().trim();

        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Adding grades for: " + student.getName());
        System.out.print("How many grades would you like to add? ");

        try {
            int count = Integer.parseInt(scanner.nextLine());
            if (count <= 0) {
                System.out.println("Invalid number of grades!");
                return;
            }

            int added = 0;
            for (int i = 0; i < count; i++) {
                System.out.print("Enter grade " + (i + 1) + " (0-100): ");
                try {
                    double grade = Double.parseDouble(scanner.nextLine());
                    if (student.addGrade(grade)) {
                        added++;
                    } else {
                        System.out.println("Invalid grade or maximum grades reached!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid grade format! Skipping...");
                }
            }

            System.out.println(added + " grades added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    public void viewStudentDetails() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }

        System.out.print("\nEnter student ID: ");
        String id = scanner.nextLine().trim();

        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        displayStudentDetails(student);
    }

    private void displayStudentDetails(Student student) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("STUDENT DETAILS");
        System.out.println("=".repeat(40));
        System.out.println("Name: " + student.getName());
        System.out.println("ID: " + student.getId());
        System.out.println("Number of Grades: " + student.getGradeCount());

        if (student.getGradeCount() > 0) {
            double[] grades = student.getAllGrades();
            System.out.print("Grades: ");
            for (int i = 0; i < grades.length; i++) {
                System.out.printf("%.1f", grades[i]);
                if (i < grades.length - 1) System.out.print(", ");
            }
            System.out.println();
            System.out.printf("Average: %.2f\n", student.calculateAverage());
            System.out.printf("Highest Grade: %.1f\n", student.getHighestGrade());
            System.out.printf("Lowest Grade: %.1f\n", student.getLowestGrade());
            System.out.println("Letter Grade: " + student.getLetterGrade());
        } else {
            System.out.println("No grades recorded yet.");
        }
        System.out.println("=".repeat(40));
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("ALL STUDENTS OVERVIEW");
        System.out.println("=".repeat(80));
        System.out.printf("%-15s %-10s %-8s %-8s %-8s %-8s %-8s\n",
                "Name", "ID", "Grades", "Average", "Highest", "Lowest", "Letter");
        System.out.println("-".repeat(80));

        for (Student student : students) {
            if (student.getGradeCount() > 0) {
                System.out.printf("%-15s %-10s %-8d %-8.2f %-8.1f %-8.1f %-8c\n",
                        student.getName().length() > 15 ? student.getName().substring(0, 12) + "..." : student.getName(),
                        student.getId(),
                        student.getGradeCount(),
                        student.calculateAverage(),
                        student.getHighestGrade(),
                        student.getLowestGrade(),
                        student.getLetterGrade());
            } else {
                System.out.printf("%-15s %-10s %-8s %-8s %-8s %-8s %-8s\n",
                        student.getName().length() > 15 ? student.getName().substring(0, 12) + "..." : student.getName(),
                        student.getId(),
                        "0", "N/A", "N/A", "N/A", "N/A");
            }
        }
        System.out.println("=".repeat(80));
    }

    public void displaySummaryReport() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }

        List<Student> studentsWithGrades = students.stream()
                .filter(s -> s.getGradeCount() > 0)
                .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);

        if (studentsWithGrades.isEmpty()) {
            System.out.println("\nNo students with grades found.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPREHENSIVE SUMMARY REPORT");
        System.out.println("=".repeat(60));

        double totalAverage = studentsWithGrades.stream()
                .mapToDouble(Student::calculateAverage)
                .average()
                .orElse(0.0);

        double highestClassAverage = studentsWithGrades.stream()
                .mapToDouble(Student::calculateAverage)
                .max()
                .orElse(0.0);

        double lowestClassAverage = studentsWithGrades.stream()
                .mapToDouble(Student::calculateAverage)
                .min()
                .orElse(0.0);

        double overallHighestGrade = studentsWithGrades.stream()
                .mapToDouble(Student::getHighestGrade)
                .max()
                .orElse(0.0);

        double overallLowestGrade = studentsWithGrades.stream()
                .mapToDouble(Student::getLowestGrade)
                .min()
                .orElse(0.0);

        System.out.println("OVERALL CLASS STATISTICS:");
        System.out.println("-".repeat(30));
        System.out.println("Total Students: " + students.size());
        System.out.println("Students with Grades: " + studentsWithGrades.size());
        System.out.printf("Class Average: %.2f\n", totalAverage);
        System.out.printf("Highest Student Average: %.2f\n", highestClassAverage);
        System.out.printf("Lowest Student Average: %.2f\n", lowestClassAverage);
        System.out.printf("Overall Highest Grade: %.1f\n", overallHighestGrade);
        System.out.printf("Overall Lowest Grade: %.1f\n", overallLowestGrade);

        Map<Character, Long> gradeDistribution = studentsWithGrades.stream()
                .collect(HashMap::new,
                        (map, student) -> map.merge(student.getLetterGrade(), 1L, Long::sum),
                        (map1, map2) -> map2.forEach((k, v) -> map1.merge(k, v, Long::sum)));

        System.out.println("\nGRADE DISTRIBUTION:");
        System.out.println("-".repeat(20));
        char[] grades = {'A', 'B', 'C', 'D', 'F'};
        for (char grade : grades) {
            long count = gradeDistribution.getOrDefault(grade, 0L);
            double percentage = (double) count / studentsWithGrades.size() * 100;
            System.out.printf("Grade %c: %d students (%.1f%%)\n", grade, count, percentage);
        }

        System.out.println("\nTOP 3 PERFORMING STUDENTS:");
        System.out.println("-".repeat(35));
        studentsWithGrades.stream()
                .sorted((s1, s2) -> Double.compare(s2.calculateAverage(), s1.calculateAverage()))
                .limit(3)
                .forEach(student -> 
                    System.out.printf("%s (ID: %s) - Average: %.2f, Grade: %c\n",
                            student.getName(), student.getId(), 
                            student.calculateAverage(), student.getLetterGrade()));

        System.out.println("=".repeat(60));
    }

    public void searchStudent() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }

        System.out.print("\nEnter student name or ID to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Student> results = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(query) || 
                student.getId().toLowerCase().contains(query)) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found matching: " + query);
        } else {
            System.out.println("\nSearch Results (" + results.size() + " found):");
            System.out.println("-".repeat(40));
            for (Student student : results) {
                System.out.printf("%s (ID: %s)\n", student.getName(), student.getId());
            }
        }
    }

    // Remove student
    public void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }

        System.out.print("\nEnter student ID to remove: ");
        String id = scanner.nextLine().trim();

        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.print("Are you sure you want to remove " + student.getName() + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            students.remove(student);
            System.out.println("Student removed successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private Student findStudentById(String id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void run() {
        System.out.println("Welcome to Student Grade Management System!");

        while (true) {
            showMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        addGradesToStudent();
                        break;
                    case 3:
                        viewStudentDetails();
                        break;
                    case 4:
                        viewAllStudents();
                        break;
                    case 5:
                        displaySummaryReport();
                        break;
                    case 6:
                        searchStudent();
                        break;
                    case 7:
                        removeStudent();
                        break;
                    case 8:
                        System.out.println("\nThank you for using Student Grade Management System!");
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("\nInvalid choice! Please enter a number between 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a number.");
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public static void main(String[] args) {
        StudentGradeManager manager = new StudentGradeManager();
        manager.run();
    }
}