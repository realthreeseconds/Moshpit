package de.threeseconds.superjump;

import de.threeseconds.superjump.commands.CommandSuperjump;
import de.threeseconds.superjump.listener.PlayerListener;
import de.threeseconds.superjump.manager.PlayerManager;
import de.threeseconds.superjump.util.SuperJumpConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperJump extends JavaPlugin {

    private static SuperJump instance;

    private SuperJumpConfig superJumpConfig;
    private MiniMessage miniMessage;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.init();

        this.initListener();
        this.initCommands();

    }

    private void init() {

        this.saveDefaultConfig();
        this.superJumpConfig = new SuperJumpConfig(this.getConfig());

        this.miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.reset())
                        .resolver(StandardTags.translatable())
                        .resolver(StandardTags.keybind())
                        .resolver(StandardTags.newline())
                        .build()
                )
                .build();

        this.playerManager = new PlayerManager();
    }

    private void initListener() {
        new PlayerListener();
    }

    private void initCommands() {
        new CommandSuperjump();
    }

    public SuperJumpConfig getSuperJumpConfig() {
        return superJumpConfig;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static SuperJump getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
