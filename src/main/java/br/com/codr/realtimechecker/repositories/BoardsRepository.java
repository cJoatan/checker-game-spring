package br.com.codr.realtimechecker.repositories;

import br.com.codr.realtimechecker.models.entities.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BoardsRepository extends MongoRepository<Board, String> {


    Board findByCode(String creatorId);

    @Query("""
        {
            'or': [
                {
                    'playerWhite': {
                        'sessionId': ?0
                    }
                },
                {
                    'playerBlack': {
                        'sessionId': ?0
                    }
                }
            ]
        }
    """)
    Optional<Board> findByPlayersSessionId(String sessionId);
}
