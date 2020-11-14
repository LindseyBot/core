package net.notfab.lindsey.core.framework.profile;

import lombok.Data;
import net.notfab.lindsey.core.framework.i18n.Language;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Lazy
@RedisHash("Lindsey:Profile")
public class UserProfile {

    @Id
    private long owner;

    private String name;
    private long lastSeen;
    private Language language = Language.en_US;

    private long cookies = 0;

    private long cookieStreak = 0;
    private long lastDailyCookies = 0;

}
