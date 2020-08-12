package com.yijun.this_is_movie.data;

import java.io.Serializable;

public class Movie implements Serializable {
    Integer id;
    String title;
    String genre;
    Integer attendance;
    String year;
    Integer cnt;
    Integer is_favorite;

    public Movie(Integer id, String title, String genre, Integer attendance, String year, Integer is_favorite) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.is_favorite = is_favorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Integer getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Integer is_favorite) {
        this.is_favorite = is_favorite;
    }

    public Movie(Integer id, Integer cnt) {
        this.id = id;
        this.cnt = cnt;
    }

    public Movie(String title, String genre, Integer attendance, String year, Integer is_favorite) {
        this.title = title;
        this.genre = genre;
        this.attendance = attendance;
        this.year = year;
        this.is_favorite = is_favorite;
    }

    public Movie() {

    }
}