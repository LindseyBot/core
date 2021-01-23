package net.notfab.lindsey.core.repositories.mongo;

import net.notfab.lindsey.shared.entities.ReactionRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReactionRoleRepository extends MongoRepository<ReactionRole, String> {

    List<ReactionRole> findAllByGuildId(long guildId);

}
