package net.phoenix.playerhider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PlayerHider implements ClientModInitializer {

    public static final List<String> username = new ArrayList<>();

    public static boolean blockedPlayerChecked(String string) {
        for (String player : username) {
            if (string.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public static String getBlockedPlayer(String string) {
        for (String player : username) {
            if (string.contains(player)) {
                return player;
            }
        }
        return null;
    }

    public static void addBlockedPlayer(String player) {
        username.add(player);
        try {
            saveBlockedPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeBlockedPlayer(String player) {
        username.remove(player);
        try {
            saveBlockedPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InGameHud
    }

    private static void saveBlockedPlayers() throws IOException {
        File config = FabricLoader.getInstance().getConfigDir().resolve("playerhider.properties").toFile();
        config.createNewFile();
        FileInputStream propsInput = new FileInputStream(config);

        Properties prop = new Properties();
        prop.load(propsInput);

        Writer inputStream = new FileWriter(config);

        prop.setProperty("blocked_strings", username.toString());

        prop.store(inputStream, "");
    }

    private static void loadBlockedPlayers() throws IOException {
        File config = FabricLoader.getInstance().getConfigDir().resolve("playerhider.properties").toFile();
        config.createNewFile();
        FileInputStream propsInput = new FileInputStream(config);

        Properties prop = new Properties();
        prop.load(propsInput);

        String blockedStrings = prop.getProperty("blocked_strings");
        if (blockedStrings != null) {
            String[] blockedStringsArray = blockedStrings.replace("[", "").replace("]", "").split(",");
            username.addAll(Arrays.asList(blockedStringsArray));
        }
    }

    @Override
    public void onInitializeClient() {
        try {
            loadBlockedPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            PlayerHiderCommand command = new PlayerHiderCommand();
            command.build(dispatcher);
        });
    }


}
