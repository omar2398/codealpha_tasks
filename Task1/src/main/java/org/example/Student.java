package org.example;

import java.util.Objects;

public class Student {
    private String name;
    private String id;
    private double[] grades;
    private int gradeCount;
    private static final int MAX_GRADES = 10;

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        this.grades = new double[MAX_GRADES];
        this.gradeCount = 0;
    }

    public boolean addGrade(double grade) {
        if (gradeCount < MAX_GRADES && grade >= 0 && grade <= 100) {
            grades[gradeCount++] = grade;
            return true;
        }
        return false;
    }

    public double calculateAverage() {
        if (gradeCount == 0) return 0.0;
        double sum = 0;
        for (int i = 0; i < gradeCount; i++) {
            sum += grades[i];
        }
        return sum / gradeCount;
    }

    public double getHighestGrade() {
        if (gradeCount == 0) return 0.0;
        double highest = grades[0];
        for (int i = 1; i < gradeCount; i++) {
            if (grades[i] > highest) {
                highest = grades[i];
            }
        }
        return highest;
    }

    public double getLowestGrade() {
        if (gradeCount == 0) return 0.0;
        double lowest = grades[0];
        for (int i = 1; i < gradeCount; i++) {
            if (grades[i] < lowest) {
                lowest = grades[i];
            }
        }
        return lowest;
    }

    public char getLetterGrade() {
        double average = calculateAverage();
        if (average >= 90) return 'A';
        else if (average >= 80) return 'B';
        else if (average >= 70) return 'C';
        else if (average >= 60) return 'D';
        else return 'F';
    }

    public double[] getAllGrades() {
        double[] result = new double[gradeCount];
        System.arraycopy(grades, 0, result, 0, gradeCount);
        return result;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getGradeCount() { return gradeCount; }

    @Override
    public String toString() {
        return String.format("Student{name='%s', id='%s', grades=%d, average=%.2f, letter=%c}",
                name, id, gradeCount, calculateAverage(), getLetterGrade());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}