package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private LocalDateTime created;
    private String title;

    @OneToMany(mappedBy="post", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    //    @OneToMany(mappedBy="post", fetch=FetchType.EAGER)
    //    Set<Ship> ships = new HashSet<>();
    //

    @OneToMany(mappedBy="post", fetch=FetchType.EAGER)
    Set<ForComments> forComments = new HashSet<>();


    public Post(){ }

    public Post(LocalDateTime gameCreated, String title){
        this.created = gameCreated;
        this.title = title;
    }

    public LocalDateTime getDateCreated() {
        return created;
    }

    public long getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public void addScore(Score score){
        score.setPost(this);
        scores.add(score);
    }
    @JsonIgnore
    public Set<Score> getScores() {
        return scores;
    }

    public Double getTotalScore() {
        return scores.stream().map(Score::getScore).reduce(Double::sum).orElse(0.0);
    }

    public Set<ForComments> getForComments(){
        return forComments;
    }

    public void addForComment(ForComments forComment) {
        forComment.setPost(this);
        forComments.add(forComment);
    }
}
