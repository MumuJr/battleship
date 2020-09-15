package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

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
    private String password;


    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

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

    public void addGamePlayer(GamePlayer gameplayer) {
        gameplayer.setPlayer(this);
        gamePlayers.add(gameplayer);
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    public long getId() {
        return id;
    }

    public Long getWin() {
        return scores.stream().filter(score -> score.getScore() == 1).count();
    }
    public Long getLoss() {
        return scores.stream().filter(score -> score.getScore() == 0).count();
    }
    public Long getTie() {
        return scores.stream().filter(score -> score.getScore() == .5).count();
    }

    public Double getScore() {
        return scores.stream().map(s -> s.getScore()).reduce((a , b) -> a + b).orElse(0.0);
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(){
        this.firstName = firstName;
    }

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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return firstName + " " + lastName;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void addScore(Score score){
        score.setPlayer(this);
        scores.add(score);
    }

    public Map<String, Object> makeDTO(){
        return new LinkedHashMap<String, Object>(){{
            put("id", id);
            put("name", userName);
        }};
    }
}