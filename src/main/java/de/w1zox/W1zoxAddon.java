package de.w1zox;

import com.mojang.logging.LogUtils;
import de.w1zox.hud.AdminListHud;
import de.w1zox.modules.AdminListModule;
import de.w1zox.modules.PlayerChunksModule;
import de.w1zox.modules.SpawnerMarkersModule;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class W1zoxAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("w1zox");
    public static final HudGroup HUD_GROUP = new HudGroup("w1zox");

    @Override public void onInitialize() {
        LOG.info("Initializing w1zox addon");
        Modules.get().add(new AdminListModule());
        Modules.get().add(new PlayerChunksModule());
        Modules.get().add(new SpawnerMarkersModule());
        Hud.get().register(AdminListHud.INFO);
    }

    @Override public void onRegisterCategories() { Modules.registerCategory(CATEGORY); }
    @Override public String getPackage() { return "de.w1zox"; }
}
