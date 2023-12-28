package de.threeseconds.superjump.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class SuperJumpConfig {

    private String permissionUse, permissionTeam;
    private int duration;
    private Material material;

    public SuperJumpConfig(FileConfiguration fileConfiguration) {
        this.load(fileConfiguration);
    }

    /**
     * Laedt eine fileconfiguration
     * @param fileConfiguration
     */
    public void load(FileConfiguration fileConfiguration) {
        this.permissionUse = fileConfiguration.getString("permission_use");
        this.permissionTeam = fileConfiguration.getString("permission_team");
        this.duration = fileConfiguration.getInt("duration");

        /**
         * check if value or material is null
         */
        if(fileConfiguration.getString("material") == null || Material.getMaterial(fileConfiguration.getString("material")) == null) {

            /**
             * sets default item to diamond
             */
            this.material = Material.DIAMOND;
        } else {
            this.material = Material.getMaterial(fileConfiguration.getString("material"));
        }

    }

    public int getDuration() {
        return duration;
    }

    public String getPermissionUse() {
        return permissionUse;
    }

    public String getPermissionTeam() {
        return permissionTeam;
    }

    public Material getMaterial() {
        return material;
    }
}
