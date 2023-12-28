package de.threeseconds.superjump.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.threeseconds.superjump.SuperJump;
import de.threeseconds.superjump.manager.PlayerManager;
import de.threeseconds.superjump.util.SuperJumpConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private PlayerManager playerManager;
    private MiniMessage miniMessage;
    private SuperJumpConfig superJumpConfig;

    public PlayerListener() {
        SuperJump.getInstance().getServer().getPluginManager().registerEvents(this, SuperJump.getInstance());

        this.playerManager = SuperJump.getInstance().getPlayerManager();
        this.miniMessage = SuperJump.getInstance().getMiniMessage();
        this.superJumpConfig = SuperJump.getInstance().getSuperJumpConfig();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        if(!playerInteractEvent.hasItem()) return;

        if(playerInteractEvent.getItem() == null || playerInteractEvent.getMaterial() == Material.AIR) return;

        Player player = playerInteractEvent.getPlayer();
        if(playerInteractEvent.getAction().isRightClick()) {

            //compare if material matches the desired material from config
            if(playerInteractEvent.getMaterial() == this.superJumpConfig.getMaterial()) {
                if(!player.hasPermission(this.superJumpConfig.getPermissionUse())) {
                    player.sendMessage(this.miniMessage.deserialize("<red>Du hast keine Berechtigung!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 1);
                this.playerManager.handleSuperjump(player);
                return;
            }
        }
    }
}
