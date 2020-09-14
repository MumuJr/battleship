package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private LocalDateTime created;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();



    public Game(){ }

    public Game(LocalDateTime gameCreated){
        this.created = gameCreated;
    }

    public LocalDateTime getDateCreated() {
        return created;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public long getId() {
        return id;
    }


    @JsonIgnore
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void addScore(Score score){
        score.setGame(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public List<Long> getPlayerID(){
        return gamePlayers.stream().map(gpId -> gpId.getPlayer().getId()).collect(toList());
    }
}
