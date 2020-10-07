package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;



@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String firstName;
    private String lastName;
    private Long win;
    private String userName;
    private String email;
    private String password;


    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Player(){ }

    public Player(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public Player(String first, String last, String userName, String password){
        this.firstName = first;
        this.lastName = last;
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }
    @JsonIgnore
    public Long getWin() {
        return scores.stream().filter(score -> score.getScore() == 1.0).count();
    }
    @JsonIgnore
    public Long getLoss() {
        return scores.stream().filter(score -> score.getScore() == -1.0).count();
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonIgnore
    public Set<Score> getScores() {
        return scores;
    }


    public Double getScore() {
        return scores.stream().map(Score::getScore).reduce(Double::sum).orElse(0.0);
    }
    @JsonIgnore
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(){
        this.firstName = firstName;
    }

    @JsonIgnore
    public String getLastName(){
        return lastName;
    }

    public void setLastName(){
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return firstName + " " + lastName;
    }

    public void addScore(Score score){
        score.setPlayer(this);
        scores.add(score);
    }

    public Map<String, Object> makeDTO(){
        return new LinkedHashMap<String, Object>(){{
            put("id", id);
            put("name", userName);
            put("score", getScore());
        }};
    }
}