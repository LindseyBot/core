package net.notfab.lindsey.core.framework.i18n;

import com.moandjiezana.toml.Toml;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.notfab.lindsey.core.framework.profile.ProfileManager;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.enums.Language;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class Translator {

    private static final Map<Language, Toml> tomlMap = new HashMap<>();
    private static final Map<Language, Properties> propertiesMap = new HashMap<>();

    private final ProfileManager profiles;

    public Translator(ProfileManager profiles) {
        this.profiles = profiles;
        log.info("Loaded " + this.reloadLanguages() + " language files.");
    }

    private static InputStream getLanguage(String language) {
        return Translator.class.getResourceAsStream("/lang/" + language + ".toml");
    }

    public String get(Member member, String message, Object... args) {
        return get(member.getUser(), message, args);
    }

    public String get(User user, String message, Object... args) {
        Language language = profiles.get(user).getLanguage();
        return get(language, message, args);
    }

    public String get(Guild guild, String message, Object... args) {
        ServerProfile profile = profiles.get(guild);
        Language language;
        if (profile.getLanguage() == null) {
            language = Language.en_US;
        } else {
            language = profile.getLanguage();
        }
        return get(language, message, args);
    }

    public String get(Language language, String key, Object... args) {
        String template = this.getTemplate(language, key);
        if (template.equals(key)) {
            String tomlTemplate = this.getTomlTemplate(language, key);
            if (tomlTemplate != null) {
                template = tomlTemplate;
            }
        }
        String msg = template;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        return msg;
    }

    @Deprecated
    private String getTomlTemplate(Language language, String key) {
        Toml toml;
        if (tomlMap.containsKey(language)) {
            toml = tomlMap.get(language);
        } else {
            toml = new Toml().parse(getLanguage(language.name()));
            tomlMap.put(language, toml);
        }
        return toml.getString(key);
    }

    private String getTemplate(Language language, String key) {
        Properties properties = propertiesMap.get(language);
        if (properties == null) {
            return this.getTemplate(Language.en_US, key);
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else if (language != Language.en_US) {
            return this.getTemplate(Language.en_US, key);
        } else {
            return key;
        }
    }

    public int reloadLanguages() {
        OkHttpClient client = new OkHttpClient.Builder()
            .build();
        for (Language language : Language.values()) {
            Request request = new Request.Builder()
                .url("https://cdn.lindseybot.net/i18n/messages/" + language.name() + ".properties")
                .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body == null) {
                        continue;
                    }
                    Properties properties = new Properties();
                    properties.load(body.byteStream());
                    propertiesMap.put(language, properties);
                    body.close();
                }
            } catch (IOException ex) {
                log.error("Failed to load language " + language.name(), ex);
            }
        }
        return propertiesMap.size();
    }

}
