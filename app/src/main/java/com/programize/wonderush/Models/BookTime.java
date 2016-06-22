package com.programize.wonderush.Models;

public class BookTime {

    String id;
    int places;
    String time;
    String start_time;
    boolean bookable;

    public BookTime(String id, String time, int places, boolean bookable, String start_time) {

        this.id = id;
        this.places = places;
        this.time = time;
        this.start_time = start_time;
        this.bookable = bookable;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isBookable() {
        return bookable;
    }

    public void setBookable(boolean bookable) {
        this.bookable = bookable;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "BookTime{" +
                "id='" + id + '\'' +
                ", places=" + places +
                ", time='" + time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", bookable=" + bookable +
                '}';
    }
}
