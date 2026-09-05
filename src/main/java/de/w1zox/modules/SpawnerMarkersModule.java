package de.w1zox.modules;

import de.w1zox.W1zoxAddon;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/** Incrementally scans only client-loaded normal spawners near the player; it never sends server queries. */
public class SpawnerMarkersModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder().name("radius").description("Horizontal scan radius in blocks.").defaultValue(64).range(16, 128).sliderRange(16, 128).build());
    private final Setting<Integer> blocksPerTick = sgGeneral.add(new IntSetting.Builder().name("blocks-per-tick").description("Incremental scan work per tick. Higher values update faster but cost more FPS.").defaultValue(2048).range(128, 8192).sliderRange(128, 8192).build());
    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder().name("color").description("Marker color.").defaultValue(Color.RED).build());
    private final Long2ObjectOpenHashMap<BlockPos> spawners = new Long2ObjectOpenHashMap<>();
    private BlockPos origin;
    private int scanX, scanY, scanZ;

    public SpawnerMarkersModule() { super(W1zoxAddon.CATEGORY, "spawner-markers", "Marks normal spawners in chunks already loaded by your client."); }

    @Override public void onActivate() { reset(); }
    @Override public void onDeactivate() { spawners.clear(); origin = null; }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null) return;
        BlockPos now = mc.player.getBlockPos();
        if (origin == null || origin.getSquaredDistance(now) > 256) { origin = now; resetCursor(); }
        for (int i = 0; i < blocksPerTick.get(); i++) scanOne();
    }

    private void reset() { spawners.clear(); origin = null; resetCursor(); }
    private void resetCursor() { scanX = -radius.get(); scanZ = -radius.get(); scanY = mc.world == null ? 0 : mc.world.getBottomY(); }

    private void scanOne() {
        if (origin == null || mc.world == null) return;
        BlockPos pos = new BlockPos(origin.getX() + scanX, scanY, origin.getZ() + scanZ);
        BlockState state = mc.world.getBlockState(pos);
        long key = pos.asLong();
        if (state.isOf(Blocks.SPAWNER)) spawners.put(key, pos.toImmutable());
        else spawners.remove(key);
        scanY++;
        if (scanY >= mc.world.getTopY()) { scanY = mc.world.getBottomY(); scanX++; }
        if (scanX > radius.get()) { scanX = -radius.get(); scanZ++; }
        if (scanZ > radius.get()) { scanZ = -radius.get(); }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        for (BlockPos pos : spawners.values()) {
            // A vertical beam remains visible through terrain, like the reference image.
            Box beam = new Box(pos.getX(), mc.world.getBottomY(), pos.getZ(), pos.getX() + 1, mc.world.getTopY(), pos.getZ() + 1);
            event.renderer.box(beam, color.get(), color.get(), ShapeMode.Both, 0);
        }
    }
}
