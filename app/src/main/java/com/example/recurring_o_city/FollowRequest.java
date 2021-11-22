package com.example.recurring_o_city;

public class FollowRequest {
    private String person;
    private Boolean accept;

    FollowRequest() {
    }

    FollowRequest(String person, Boolean accept) {
        this.person = person;
        this.accept = accept;
    }

    String getPerson() {
        return this.person;
    }

    void setPerson(String person) {
        this.person = person;
    }

    Boolean getAccept() {
        return this.accept;
    }

    void setAccept(Boolean accept) {
        this.accept = accept;
    }
}
