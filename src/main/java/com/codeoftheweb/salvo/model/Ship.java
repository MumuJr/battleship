package com.codeoftheweb.salvo.model;
import com.codeoftheweb.salvo.model.Post;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private Post post;


    public Ship (){ }




    public void setPost(Post post){
        this.post = post;
    }
}
