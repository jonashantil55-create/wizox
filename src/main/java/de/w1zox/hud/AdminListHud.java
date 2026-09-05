package de.w1zox.hud;

import de.w1zox.W1zoxAddon;
import de.w1zox.modules.AdminListModule;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;

import java.util.List;

public class AdminListHud extends HudElement {
    public static final HudElementInfo<AdminListHud> INFO = new HudElementInfo<>(W1zoxAddon.HUD_GROUP, "admin-list", "Configured admins currently visible to your client.", AdminListHud::new);

    public AdminListHud() { super(INFO); }

    @Override public void render(HudRenderer renderer) {
        List<String> admins = AdminListModule.INSTANCE == null ? List.of() : AdminListModule.INSTANCE.getOnlineAdmins();
        String title = "Online Admin List";
        int lineHeight = renderer.textHeight(true);
        double width = renderer.textWidth(title, true);
        for (String admin : admins) width = Math.max(width, renderer.textWidth(admin, true) + 12);
        if (admins.isEmpty()) width = Math.max(width, renderer.textWidth("Keine Admins sichtbar", true));
        width += 12;
        double height = 10 + lineHeight * (admins.size() + 1) + 4;
        setSize(width, height);

        // Compact dark card styled for the upper-right HUD placement used in the reference.
        renderer.quad(x, y, width, height, new Color(7, 14, 28, 220));
        renderer.text(title, x + 6, y + 4, Color.WHITE, true);
        if (admins.isEmpty()) {
            renderer.text("Keine Admins sichtbar", x + 6, y + 4 + lineHeight, Color.LIGHT_GRAY, true);
        } else {
            for (int i = 0; i < admins.size(); i++) {
                double lineY = y + 4 + lineHeight * (i + 1);
                renderer.quad(x + 6, lineY + 2, 5, 5, Color.GREEN);
                renderer.text(admins.get(i), x + 14, lineY, Color.WHITE, true);
            }
        }
    }
}
