package org.example;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test class to demonstrate the Student Grade Management System functionality
 * This class creates sample students and grades for testing purposes
 */
public class TestStudentGradeSystem {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("STUDENT GRADE MANAGEMENT SYSTEM - DEMO");
        System.out.println("=".repeat(60));

        // Create sample students
        ArrayList<Student> testStudents = createSampleStudents();

        // Display individual student details
        System.out.println("\nINDIVIDUAL STUDENT DETAILS:");
        System.out.println("-".repeat(40));
        for (Student student : testStudents) {
            displayStudentInfo(student);
            System.out.println();
        }

        // Display class statistics
        displayClassStatistics(testStudents);

        // Display grade distribution
        displayGradeDistribution(testStudents);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Demo completed! Run 'java StudentGradeManager' for interactive mode.");
        System.out.println("=".repeat(60));
    }

    /**
     * Create sample students with different grade patterns
     */
    private static ArrayList<Student> createSampleStudents() {
        ArrayList<Student> students = new ArrayList<>();

        // Student 1: High performer
        Student alice = new Student("Alice Johnson", "STU001");
        double[] aliceGrades = {95.0, 92.0, 88.0, 96.0, 90.0};
        addGradesToStudent(alice, aliceGrades);
        students.add(alice);

        // Student 2: Average performer
        Student bob = new Student("Bob Smith", "STU002");
        double[] bobGrades = {78.0, 82.0, 75.0, 80.0, 79.0, 83.0};
        addGradesToStudent(bob, bobGrades);
        students.add(bob);

        // Student 3: Struggling student
        Student charlie = new Student("Charlie Brown", "STU003");
        double[] charlieGrades = {65.0, 58.0, 72.0, 60.0};
        addGradesToStudent(charlie, charlieGrades);
        students.add(charlie);

        // Student 4: Excellent performer
        Student diana = new Student("Diana Prince", "STU004");
        double[] dianaGrades = {98.0, 94.0, 97.0, 99.0, 95.0, 92.0, 96.0};
        addGradesToStudent(diana, dianaGrades);
        students.add(diana);

        // Student 5: Inconsistent performer
        Student edward = new Student("Edward Wilson", "STU005");
        double[] edwardGrades = {85.0, 65.0, 90.0, 70.0, 88.0};
        addGradesToStudent(edward, edwardGrades);
        students.add(edward);

        return students;
    }

    /**
     * Helper method to add multiple grades to a student
     */
    private static void addGradesToStudent(Student student, double[] grades) {
        for (double grade : grades) {
            student.addGrade(grade);
        }
    }

    /**
     * Display detailed information for a student
     */
    private static void displayStudentInfo(Student student) {
        System.out.printf("Student: %s (ID: %s)\n", student.getName(), student.getId());

        if (student.getGradeCount() > 0) {
            double[] grades = student.getAllGrades();
            System.out.print("  Grades: ");
            for (int i = 0; i < grades.length; i++) {
                System.out.printf("%.1f", grades[i]);
                if (i < grades.length - 1) System.out.print(", ");
            }
            System.out.println();
            System.out.printf("  Average: %.2f\n", student.calculateAverage());
            System.out.printf("  Highest: %.1f, Lowest: %.1f\n", 
                    student.getHighestGrade(), student.getLowestGrade());
            System.out.printf("  Letter Grade: %c\n", student.getLetterGrade());
        } else {
            System.out.println("  No grades recorded");
        }
    }

    /**
     * Display overall class statistics
     */
    private static void displayClassStatistics(ArrayList<Student> students) {
        System.out.println("CLASS STATISTICS:");
        System.out.println("-".repeat(25));

        // Calculate overall statistics
        double totalAverage = 0;
        double highestAverage = 0;
        double lowestAverage = 100;
        double overallHighest = 0;
        double overallLowest = 100;
        int studentsWithGrades = 0;

        for (Student student : students) {
            if (student.getGradeCount() > 0) {
                studentsWithGrades++;
                double avg = student.calculateAverage();
                totalAverage += avg;

                if (avg > highestAverage) highestAverage = avg;
                if (avg < lowestAverage) lowestAverage = avg;
                if (student.getHighestGrade() > overallHighest) overallHighest = student.getHighestGrade();
                if (student.getLowestGrade() < overallLowest) overallLowest = student.getLowestGrade();
            }
        }

        if (studentsWithGrades > 0) {
            totalAverage /= studentsWithGrades;

            System.out.printf("Total Students: %d\n", students.size());
            System.out.printf("Students with Grades: %d\n", studentsWithGrades);
            System.out.printf("Class Average: %.2f\n", totalAverage);
            System.out.printf("Highest Student Average: %.2f\n", highestAverage);
            System.out.printf("Lowest Student Average: %.2f\n", lowestAverage);
            System.out.printf("Overall Highest Grade: %.1f\n", overallHighest);
            System.out.printf("Overall Lowest Grade: %.1f\n", overallLowest);
        }
    }

    /**
     * Display grade distribution
     */
    private static void displayGradeDistribution(ArrayList<Student> students) {
        System.out.println("\nGRADE DISTRIBUTION:");
        System.out.println("-".repeat(25));

        int[] gradeCounts = new int[5]; // A, B, C, D, F
        int totalStudents = 0;

        for (Student student : students) {
            if (student.getGradeCount() > 0) {
                totalStudents++;
                char grade = student.getLetterGrade();
                switch (grade) {
                    case 'A': gradeCounts[0]++; break;
                    case 'B': gradeCounts[1]++; break;
                    case 'C': gradeCounts[2]++; break;
                    case 'D': gradeCounts[3]++; break;
                    case 'F': gradeCounts[4]++; break;
                }
            }
        }

        String[] gradeLabels = {"A", "B", "C", "D", "F"};
        for (int i = 0; i < 5; i++) {
            double percentage = totalStudents > 0 ? (gradeCounts[i] * 100.0 / totalStudents) : 0;
            System.out.printf("Grade %s: %d students (%.1f%%)\n", 
                    gradeLabels[i], gradeCounts[i], percentage);
        }
    }
}