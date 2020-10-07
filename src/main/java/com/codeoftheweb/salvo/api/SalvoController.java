package com.codeoftheweb.salvo.api;

import com.codeoftheweb.salvo.dao.*;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.ForComments;
import com.codeoftheweb.salvo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PostRepository PostRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private ForCommentsRepository forCommentsRepo;

    @Autowired
    private ShipRepository shipRepo;

    @Autowired
    private ScoreRepository scoreRepo;



    private Optional<Player> getAuthOptional(Authentication auth) {
        return auth == null ? Optional.empty() : playerRepo.findByUserName(auth.getName());
    }

    @RequestMapping("/posts")
    public Map<String, Object> getAll(Authentication authentication) {
        Optional<Player> playerOptional = getAuthOptional(authentication);

        return new LinkedHashMap<String, Object>(){{
            put("user", playerOptional.map(player -> player.makeDTO()).orElse(null));
            put("posts",  PostRepo.findAll().stream()
                    .map(post -> new LinkedHashMap<String, Object>(){{
                        put("id", post.getId());
                        put("date", post.getDateCreated());
                        put("score", post.getTotalScore());
                        put("title", post.getTitle());
                    }}).collect(toList()));
        }};
    }


     @RequestMapping("/post/{id}")
            public LinkedHashMap<String, Object> getPlayer(@PathVariable long id, Authentication authentication){
                Optional<Player> currentPlayer = getAuthOptional(authentication);

                Optional<Post> post = PostRepo.findById(id);

                if (post.isPresent() && currentPlayer.isPresent()){
                    return new LinkedHashMap<String, Object>(){{
                        put("id", post.get().getId());
                        put("created", post.get().getDateCreated());
                        put("title", post.get().getTitle());
                        put("Score", post.get().getTotalScore());
                        put("forComments", post.get().getForComments());
                    }};
                } else {
                    return null;
                }
            }


        private List<LinkedHashMap<String, Object>> getSalvoMap(Set<ForComments> salvos){
        return salvos.stream()
                .map(salvo -> new LinkedHashMap<String, Object>(){{
                put("id" , salvo.getId());
            }}).collect(toList());
        }

        private Map<String, Object> makeMap(String key, Object value) {
            Map<String, Object> map = new HashMap<>();
            map.put(key, value);
            map.put(key, value);
            return map;
        }


    @PostMapping(value = "/players")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam("username") String username, @RequestParam("password") String password){
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

