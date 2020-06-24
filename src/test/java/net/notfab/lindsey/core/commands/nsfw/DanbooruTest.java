package net.notfab.lindsey.core.commands.nsfw;

import net.notfab.lindsey.framework.command.CommandDescriptor;
import net.notfab.lindsey.framework.command.Modules;
import net.notfab.lindsey.framework.command.help.HelpArticle;
import net.notfab.lindsey.framework.command.help.HelpPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DanbooruTest {

    private Danbooru command;

    @BeforeEach
    void setUp() {
        command = mock(Danbooru.class);
        when(command.getInfo())
                .thenCallRealMethod();
        when(command.help(null))
                .thenCallRealMethod();
    }

    @Test
    void getInfo() {
        CommandDescriptor info = command.getInfo();
        assertEquals("danbooru", info.getName(), "Name must be danbooru");
        assertEquals(Modules.NSFW, info.getModule(), "Module must be Nsfw");
        assertTrue(info.getPermissions().containsKey("commands." + info.getName()), "Must have permission with command name");
    }

    @Test
    void help() {
        HelpArticle article = command.help(null);
        HelpPage page = article.get("danbooru");
        assertNotNull(page, "Help page must not be null");
        assertEquals("commands." + command.getInfo().getName(), page.getPermission(), "Permission must be command name");
    }

}