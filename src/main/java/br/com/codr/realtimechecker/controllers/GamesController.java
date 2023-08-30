package br.com.codr.realtimechecker.controllers;

import br.com.codr.realtimechecker.models.entities.Board;
import br.com.codr.realtimechecker.repositories.BoardsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GamesController {

    private BoardsRepository boardsRepository;

//    @PostMapping("invite-a-friend")
//    public String inviteAFriend() {
//        String random = RandomStringUtils.random(6, true, false);
//        var board = new Board();
//        board.setCode(random);
//        board.setPlayerBlack("");
//        boardsRepository.save(board);
//        return random;
//
//    }

}
