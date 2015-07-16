package regio_vinco;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lenovo
 */
public class Record {
    private String name;
    private int highScore;
    private int wrong;
    private String duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Record(String name, int highScore, int wrong, String duration) {
        this.name = name;
        this.highScore = highScore;
        this.wrong = wrong;
        this.duration = duration;
    }

}
