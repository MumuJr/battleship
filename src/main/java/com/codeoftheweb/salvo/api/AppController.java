package com.codeoftheweb.salvo.api;

import com.codeoftheweb.salvo.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private PlayerRepository playerRepository;
    private PostRepository postRepository;
    private ShipRepository shipRepository;
    private ForCommentsRepository forCommentsRepository;

}