package de.w1zox.modules;

import de.w1zox.W1zoxAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/** Draws chunk borders for players already supplied by the server to this client. */
public class PlayerChunksModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> includeSelf = sgGeneral.add(new BoolSetting.Builder().name("include-self").description("Also draw your current chunk.").defaultValue(false).build());
    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder().name("color").description("Chunk border and column color.").defaultValue(Color.RED).build());

    public PlayerChunksModule() { super(W1zoxAddon.CATEGORY, "player-chunks", "Draws chunk borders for visible players."); }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.world == null) return;
        mc.world.getPlayers().forEach(player -> {
            if (!includeSelf.get() && player == mc.player) return;
            BlockPos pos = player.getBlockPos();
            int minX = (pos.getX() >> 4) << 4;
            int minZ = (pos.getZ() >> 4) << 4;
            Box box = new Box(minX, mc.world.getBottomY(), minZ, minX + 16, mc.world.getTopY(), minZ + 16);
            event.renderer.box(box, color.get(), color.get(), ShapeMode.Both, 0);
        });
    }
}
