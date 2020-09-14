package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

        @Autowired
        private GameRepository repo;

        @Autowired
        private PlayerRepository playerRepo;

        @Autowired
        private GamePlayerRepository gamePlayerRepo;

        @Autowired
        private SalvoRepository salvoRepo;

        @Autowired
        private ScoreRepository scoreRepo;

        @Autowired
        private ShipRepository shipRepo;

    @RequestMapping("/games")
        public Map<String, Object> getAll(Authentication authentication) {
            Optional<Player> playerOptional = getAuthOptional(authentication);

            return new LinkedHashMap<String, Object>(){{
                put("player", playerOptional.map(player -> player.makeDTO()).orElse(null));
                put("games",  repo.findAll().stream()
                        .map(game -> new LinkedHashMap<String, Object>(){{
                            put("id", game.getId());
                            put("date", game.getDateCreated());
                            put("gamePlayers", getGPMap(game.getGamePlayers()));
                            put("score", game.getScores());
                            put("playerID", game.getPlayerID());
                        }}).collect(toList()));
            }};
        }


        private Optional<Player> getAuthOptional(Authentication auth) {

            return auth == null ? Optional.empty() : playerRepo.findByUserName(auth.getName());
        }




        @RequestMapping("/game_view/{id}")
        public LinkedHashMap<String, Object> getPlayer(@PathVariable long id, Authentication authentication){

            Optional<Player> currentPlayer = getAuthOptional(authentication);

            Optional<GamePlayer> gamePlayer = gamePlayerRepo.findById(id);

            if (gamePlayer.isPresent() && currentPlayer.map(player -> player.equals(gamePlayer.get().getPlayer())).orElse(false)){
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
                         put("created", gp.getCreated());
                     }}).collect(toList());
        }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        map.put(key, value);
        return map;
    }


    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam("username") String username, @RequestParam("password") String password){

        System.out.println("entered");
        System.out.println(username);
        System.out.println(username);
        System.out.println(password);


        if (username.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>(makeMap("error", "Fields Empty"), HttpStatus.FORBIDDEN);

        }

        Optional<Player> newPlayer = playerRepo.findByUserName(username);

        if (newPlayer.isPresent()) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }

        Player createPlayer = new Player(username, PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));

        playerRepo.save(createPlayer);

        return new ResponseEntity<>(makeMap("id", createPlayer.getId()), HttpStatus.CREATED);

    }
}

