package de.w1zox.modules;

import de.w1zox.W1zoxAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Matches a user-maintained list of names with players the client can currently see. */
public class AdminListModule extends Module {
    public static AdminListModule INSTANCE;
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<String> adminNames = sgGeneral.add(new StringSetting.Builder()
        .name("admin-names")
        .description("Comma-separated player names to show as admins. Server roles cannot be read by a client.")
        .defaultValue("")
        .build()
    );
    private final List<String> onlineAdmins = new ArrayList<>();

    public AdminListModule() {
        super(W1zoxAddon.CATEGORY, "admin-list", "Shows configured admin names that are currently visible online.");
        INSTANCE = this;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        onlineAdmins.clear();
        if (mc.world == null || adminNames.get().isBlank()) return;
        String configured = "," + adminNames.get().toLowerCase(Locale.ROOT).replace(" ", "") + ",";
        mc.world.getPlayers().forEach(player -> {
            String name = player.getName().getString();
            if (configured.contains("," + name.toLowerCase(Locale.ROOT) + ",")) onlineAdmins.add(name);
        });
    }

    public List<String> getOnlineAdmins() { return List.copyOf(onlineAdmins); }
}
