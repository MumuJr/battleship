package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Double score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="post_id")
    private Post post;

    public Score (){ }

    public Score(Double score) {
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScore(){
        return score;
    }

    @JsonIgnore
    public long getPlayerId(){
        return player.getId();
    }

    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
