package com.yijun.this_is_movie.data;

import java.io.Serializable;

public class Movie implements Serializable {
    Integer id;
    String title;
    String genre;
    Integer attendance;
    String year;
    Integer cnt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Movie(String title, String genre, Integer attendance, String year) {
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
    }

    public Movie(Integer id, String title, String genre, Integer attendance, String year, Integer cnt) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.cnt = cnt;
    }

    public Movie() {

    }
}