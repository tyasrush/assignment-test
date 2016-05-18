package com.rusminanto.assignment.assignmentapp.model;

/**
 * Created by tyasrus on 18/05/16.
 */
public class Promo {

    private short id;
    private String name;
    private String city;

    public Promo() {
    }

    public Promo(short id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Promo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
