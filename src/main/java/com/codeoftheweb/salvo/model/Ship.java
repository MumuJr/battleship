package com.codeoftheweb.salvo.model;
import com.codeoftheweb.salvo.model.GamePlayer;
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
    private String shipType;

    @ElementCollection
    @Column(name="location")
    private List<String> location = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship (){ }

    public Ship (String type, List<String> location) {
        this.shipType = type;
        this.location = location;
    }

    public String getType() {
        return shipType;
    }


    public List<String> getLocation() {
        return location;
    }

    public void setType(String type) {
        this.shipType = type;
    }

    public void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }
}
