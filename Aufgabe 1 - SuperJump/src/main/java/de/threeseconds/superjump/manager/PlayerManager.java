package de.threeseconds.superjump.manager;

import de.threeseconds.superjump.SuperJump;
import de.threeseconds.superjump.util.SuperJumpConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private PlayerManager playerManager;
    private MiniMessage miniMessage;
    private SuperJumpConfig superJumpConfig;

    private HashMap<UUID, Boolean> superJump;

    public PlayerManager() {
        this.superJump = new HashMap<>();

        this.playerManager = SuperJump.getInstance().getPlayerManager();
        this.miniMessage = SuperJump.getInstance().getMiniMessage();
        this.superJumpConfig = SuperJump.getInstance().getSuperJumpConfig();
    }

    public void handleSuperjump(Player player) {
        //check if player is already in superjump mode
        if(this.superJump.get(player.getUniqueId()) != null) {
            player.sendMessage(this.miniMessage.deserialize("<red>Du bist bereits im <gold>SuperJump<gray>!"));
            return;
        }

        this.superJump.put(player.getUniqueId(), Boolean.TRUE);
        player.sendMessage(this.miniMessage.deserialize("<gray>Du bist <green>nun <gray>für <green>" + this.superJumpConfig.getDuration() + " Sekunden <gray>im <gold>SuperJump<gray>!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 1, false, false, false));

        //send an info message to all staff members, whether a player activates superjump
        Bukkit.getOnlinePlayers().forEach(staff -> {
            if(staff.isOp() || staff.hasPermission(this.superJumpConfig.getPermissionTeam())) {
                staff.sendMessage(this.miniMessage.deserialize("<white>[<gold>INFO<white>] <yellow>" + player.getName() + " <gray>hat den <gold>SuperJump <gray>aktiviert."));
            }
        });
        Bukkit.getConsoleSender().sendMessage(this.miniMessage.deserialize("<white>[<gold>INFO<white>] <yellow>" + player.getName() + " <gray>hat den <gold>SuperJump <gray>aktiviert."));

        //after x seconds (defined in config) remove the player from superjump
        new BukkitRunnable() {

            @Override
            public void run() {
                superJump.remove(player.getUniqueId());
                player.clearActivePotionEffects();

                player.sendMessage(miniMessage.deserialize("<gray>Deine <gold>SuperJump <gray>Zeit ist abgelaufen."));

            }
        }.runTaskLater(SuperJump.getInstance(), 20L * (this.superJumpConfig.getDuration()));

    }
}
