package ru.otus.library.entity;


import jakarta.persistence.*;

@Entity
@SequenceGenerator(name="book_id_seq", initialValue = 100, allocationSize = 100)
public class Book {

    public Long id;

    public String name;

    public Boolean booked;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "book_id_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "booked")
    public Boolean isBooked() {
        return booked != null ? booked : false;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }
}
