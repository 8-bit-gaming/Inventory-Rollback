package me.danjono.inventoryrollback;

import me.danjono.inventoryrollback.commands.Commands;
import me.danjono.inventoryrollback.config.ConfigFile;
import me.danjono.inventoryrollback.listeners.ClickGUI;
import me.danjono.inventoryrollback.listeners.EventLogs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryRollback extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft");
    private static InventoryRollback instance;

    private static String packageVersion;

    public static InventoryRollback getInstance() {
        return instance;
    }

    public static String getPluginVersion() {
        return instance.getDescription().getVersion();
    }

    public static String getPackageVersion() {
        return packageVersion;
    }

    @Override
    public void onEnable() {
        instance = this;
        packageVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        if (!isCompatible()) {
            logger.log(Level.WARNING, ChatColor.RED + " ** WARNING... Plugin may not be compatible with this version of Minecraft. **");
            logger.log(Level.WARNING, ChatColor.RED + " ** Tested versions: 1.8.8 to 1.13.1 **");
            logger.log(Level.WARNING, ChatColor.RED + " ** Please fully test the plugin before using on your server as features may be broken. **");
        }

        startupTasks();

        this.getCommand("inventoryrollback").setExecutor(new Commands());

        this.getServer().getPluginManager().registerEvents(new ClickGUI(), instance);
        this.getServer().getPluginManager().registerEvents(new EventLogs(), instance);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static void startupTasks() {
        ConfigFile config = new ConfigFile();

        config.setVariables();
        config.createStorageFolders();
    }

    private enum CompatibleVersions {
        v1_8_R1,
        v1_8_R2,
        v1_8_R3,
        v1_9_R1,
        v1_9_R2,
        v1_10_R1,
        v1_11_R1,
        v1_12_R1,
        v1_13_R1,
        v1_13_R2,
        v1_14_R1,
        v1_15_R1
    }

    public enum VersionName {
        v1_8,
        v1_9_v1_12,
        v1_13_PLUS
    }

    private static VersionName version = VersionName.v1_13_PLUS;

    public static VersionName getVersion() {
        return version;
    }

    private boolean isCompatible() {
        for (CompatibleVersions v : CompatibleVersions.values()) {

            if (v.name().equalsIgnoreCase(packageVersion)) {

                //Check if 1.8
                if (v.name().equalsIgnoreCase("v1_8_R1")
                        || v.name().equalsIgnoreCase("v1_8_R2")
                        || v.name().equalsIgnoreCase("v1_8_R3")) {
                    version = VersionName.v1_8;
                }
                //Check if 1.9 - 1.12.2
                else if (v.name().equalsIgnoreCase("v1_9_R1")
                        || v.name().equalsIgnoreCase("v1_9_R2")
                        || v.name().equalsIgnoreCase("v1_10_R1")
                        || v.name().equalsIgnoreCase("v1_11_R1")
                        || v.name().equalsIgnoreCase("v1_12_R1")) {
                    version = VersionName.v1_9_v1_12;
                }
                //Else it is 1.13+

                return true;
            }
        }

        return false;
    }
}
