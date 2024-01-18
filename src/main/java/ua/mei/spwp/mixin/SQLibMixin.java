package ua.mei.spwp.mixin;

import com.fasterxml.jackson.dataformat.toml.*;
import me.mrnavastar.sqlib.*;
import me.mrnavastar.sqlib.config.*;
import me.mrnavastar.sqlib.database.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.loader.api.*;
import org.apache.logging.log4j.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Mixin(SQLib.class)
public abstract class SQLibMixin {
    @Shadow @Final private static ArrayList<Database> databases;

    @Shadow private static SQLibConfig config;

    @Shadow
    public static void log(Level level, String message) {
    }

    @Shadow private static Database database;

    @Inject(method = "onPreLaunch", at = @At("HEAD"), remap = false, cancellable = true)
    public void onPreLaunch(CallbackInfo ci) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> databases.forEach(Database::close));

        new File(FabricLoader.getInstance().getGameDir() + "/sqlib").mkdirs();

        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir() + "/sqlib.toml");

            if (!configFile.exists()) {
                Files.copy(Objects.requireNonNull(SQLib.class.getResourceAsStream("/sqlib.toml")), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            TomlMapper mapper = new TomlMapper();
            config = mapper.readValue(configFile, SQLibConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (config.database.type.equalsIgnoreCase("SQLITE")) {
            if (!config.validateSQLite()) {
                log(Level.ERROR, "Invalid config - Stopping");
                System.exit(1);
            }
            if (!new File(config.sqlite.directory).exists()) {
                log(Level.ERROR, "Invalid config - Stopping");
                log(Level.ERROR, "[SQLite] Path: " + config.sqlite.directory + " was not found!");
                System.exit(1);
            }

            database = new SQLiteDatabase(config.database.name, config.sqlite.directory);
        } else if (config.database.type.equalsIgnoreCase("MYSQL")) {
            if (!config.validateMySQL()) {
                log(Level.ERROR, "Invalid config - Stopping");
                System.exit(1);
            }
            
            database = new MySQLDatabase(config.database.name, config.mysql.address, String.valueOf(config.mysql.port), config.mysql.username, config.mysql.password);
        }

        ci.cancel();
    }
}
