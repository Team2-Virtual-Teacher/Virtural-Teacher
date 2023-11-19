package com.alpha53.virtualteacher.models;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "grade")
    private double grade;

    @Column(name = "assignment_id")
    private int assignment_id;
}
