package com.alpha53.virtualteacher.models;

import jakarta.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "assignment_solution")
    private String assignment;

    @OneToOne
    @JoinColumn(name ="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

}
