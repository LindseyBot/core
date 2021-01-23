package net.notfab.lindsey.core.repositories.mongo;

import net.notfab.lindsey.shared.entities.leaderboard.Leaderboard;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaderboardRepository extends MongoRepository<Leaderboard, String> {

    Page<Leaderboard> findAllByType(LeaderboardType type, Pageable pageable);

}
