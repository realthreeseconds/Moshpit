package de.threeseconds.superjump.commands;

import de.threeseconds.superjump.SuperJump;
import de.threeseconds.superjump.manager.PlayerManager;
import de.threeseconds.superjump.util.SuperJumpConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandSuperjump extends Command {

    private PlayerManager playerManager;
    private MiniMessage miniMessage;
    private SuperJumpConfig superJumpConfig;

    public CommandSuperjump() {
        super("superjump");
        SuperJump.getInstance().getServer().getCommandMap().register(SuperJump.getInstance().getName(), this);

        this.playerManager = SuperJump.getInstance().getPlayerManager();
        this.miniMessage = SuperJump.getInstance().getMiniMessage();
        this.superJumpConfig = SuperJump.getInstance().getSuperJumpConfig();
    }

    /**
     *
     * Eine Tab-Completion für den Command, um die Funktionalität zu vereinfachen
     *
     * @param sender Source object which is executing this command
     * @param alias the alias being used
     * @param args All arguments passed to the command, split via ' '
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(players -> list.add(players.getName()));

            return list;
        }

        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(!player.hasPermission(this.superJumpConfig.getPermissionUse())) {
            player.sendMessage(this.miniMessage.deserialize("<red>Du hast keine Berechtigung!"));
            return false;
        }

        if(args.length > 1) {
            if(player.hasPermission(this.superJumpConfig.getPermissionTeam())) {
                player.sendMessage(this.miniMessage.deserialize("<green>/superjump [Player] <dark_gray>- <gray>Gebe einem anderen Spieler SuperJump!"));
            } else {
                player.sendMessage(this.miniMessage.deserialize("<green>/superjump <dark_gray>- <gray>Begebe dich in den SuperJump!"));
            }
            return false;
        }

        if(args.length == 1) {
            if(!player.hasPermission(this.superJumpConfig.getPermissionTeam())) {
                player.sendMessage(this.miniMessage.deserialize("<green>/superjump <dark_gray>- <gray>Begebe dich in den SuperJump!"));
                return false;
            }

            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if(targetPlayer == null) {
                player.sendMessage(this.miniMessage.deserialize("<yellow>" + args[0] + " <red>ist nicht online."));

                return false;
            }

            player.sendMessage(this.miniMessage.deserialize("<white>[<gold>INFO<white>] <gray>Du hast <yellow>" + targetPlayer.getName() + " <gold>SuperJump <gray>gebeben!"));
            Bukkit.getConsoleSender().sendMessage(this.miniMessage.deserialize("<white>[<gold>INFO<white>] <green>" + player.getName() + " <gray>hat <yellow>" + targetPlayer.getName() + " <gray>in den <gold>SuperJump <gray>befördert!"));

            //check if targetPlayer is the command executor (player/you)
            if(targetPlayer == player) {
                this.playerManager.handleSuperjump(targetPlayer);
                return false;
            }

            this.playerManager.handleSuperjump(targetPlayer);

            return false;
        }

        this.playerManager.handleSuperjump(player);

        return false;
    }
}
