package com.example.varchasdream11.Fragments;

public class MyMatch {

    public String teamName1, teamName2, teamImage1, teamImage2;

    public MyMatch(){}


    public MyMatch(String teamName1, String teamName2, String teamImage1, String teamImage2) {
        this.teamName1 = teamName1;
        this.teamName2 = teamName2;
        this.teamImage1 = teamImage1;
        this.teamImage2 = teamImage2;
    }

    public String getTeamName1() {
        return teamName1;
    }

    public void setTeamName1(String teamName1) {
        this.teamName1 = teamName1;
    }

    public String getTeamName2() {
        return teamName2;
    }

    public void setTeamName2(String teamName2) {
        this.teamName2 = teamName2;
    }

    public String getTeamImage1() {
        return teamImage1;
    }

    public void setTeamImage1(String teamImage1) {
        this.teamImage1 = teamImage1;
    }

    public String getTeamImage2() {
        return teamImage2;
    }

    public void setTeamImage2(String teamImage2) {
        this.teamImage2 = teamImage2;
    }
}
