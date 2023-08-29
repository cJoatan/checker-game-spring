package br.com.codr.realtimechecker.repositories;

import br.com.codr.realtimechecker.models.entities.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardsRepository extends MongoRepository<Board, String> {


    Board findByCode(String creatorId);
}
