package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

        @Autowired
        private GameRepository repo;

        @RequestMapping("/games")
        public List<LinkedHashMap<String, Object>> getAll() {
             return repo.findAll().stream()
                    .map(game -> new LinkedHashMap<String, Object>(){{
                        put("id", game.getId());
                        put("date", game.getDateCreated());
                        put("gamePlayers", game.getGamePlayers());
                    }}).collect(toList());
        }


        @Autowired
        private GamePlayerRepository gamePlayerRepo;

        @RequestMapping("/game_view/{id}")
        public LinkedHashMap<String, Object> getPlayer(@PathVariable long id){
            Optional<GamePlayer> gamePlayer = gamePlayerRepo.findById(id);

            if (gamePlayer.isPresent()){
                Game game = gamePlayer.get().getGame();
                return new LinkedHashMap<String, Object>(){{
                    put("id", game.getId());
                    put("created", game.getDateCreated());
                    put("gamePlayers", getGPMap(game.getGamePlayers()));
                    put("ships", gamePlayer.map(gp -> gp.getShips()).orElse(null));
                    put("salvo", game.getGamePlayers().stream().flatMap(gp -> getSalvoMap(gp.getSalvos()).stream()).collect(toList()));
                }};
            } else {
                return null;
            }
        }

        private List<LinkedHashMap<String, Object>> getSalvoMap(Set<Salvo> salvos){
        return salvos.stream()
                .map(salvo -> new LinkedHashMap<String, Object>(){{
                    put("id" , salvo.getId());
                    put("playerId", salvo.getGamePlayer().getPlayer().getId());
                    put("turn" , salvo.getTurn());
                    put("location", salvo.getLocation());
                }}).collect(toList());
    }

        private List<LinkedHashMap<String, Object>> getGPMap(Set<GamePlayer> gps){
            return gps.stream()
                     .map(gp -> new LinkedHashMap<String, Object>() {{
                         put("id", gp.getId());
                         put("player", gp.getPlayer());
                     }}).collect(toList());
        }
}

