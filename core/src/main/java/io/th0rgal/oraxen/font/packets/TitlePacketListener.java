package io.th0rgal.oraxen.font.packets;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerActionBar;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import io.th0rgal.oraxen.config.Settings;
import io.th0rgal.oraxen.utils.AdventureUtils;
import io.th0rgal.oraxen.utils.PacketHelpers;
import io.th0rgal.oraxen.utils.logs.Logs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class TitlePacketListener extends PacketListenerCommon implements PacketListener {
    @Override
    public PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
        return PacketListener.super.asAbstract(priority);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SET_TITLE_TEXT && Settings.FORMAT_TITLES.toBool()) {
            handleTitlePacket(event);
        } else if (event.getPacketType() == PacketType.Play.Server.SET_TITLE_SUBTITLE && Settings.FORMAT_SUBTITLES.toBool()) {
            handleSubtitlePacket(event);
        } else if (event.getPacketType() == PacketType.Play.Server.ACTION_BAR && Settings.FORMAT_ACTION_BAR.toBool()) {
            handleActionBarPacket(event);
        }
    }

    private void handleTitlePacket(PacketSendEvent event) {
        try {
            WrapperPlayServerSetTitleText wrapper = new WrapperPlayServerSetTitleText(event);
            Component originalTitle = wrapper.getTitle();
            Component formattedTitle = formatTitle(originalTitle, "title");
            if (formattedTitle != null) {
                wrapper.setTitle(formattedTitle);
            }
        } catch (Exception e) {
            if (Settings.DEBUG.toBool()) {
                Logs.logWarning("Error whilst processing title packet");
                e.printStackTrace();
            }
        }
    }

    private void handleSubtitlePacket(PacketSendEvent event) {
        try {
            WrapperPlayServerSetTitleSubtitle wrapper = new WrapperPlayServerSetTitleSubtitle(event);
            Component originalSubtitle = wrapper.getSubtitle();
            Component formattedSubtitle = formatTitle(originalSubtitle, "subtitle");
            if (formattedSubtitle != null) {
                wrapper.setSubtitle(formattedSubtitle);
            }
        } catch (Exception e) {
            if (Settings.DEBUG.toBool()) {
                Logs.logWarning("Error whilst processing subtitle packet");
                e.printStackTrace();
            }
        }
    }

    private void handleActionBarPacket(PacketSendEvent event) {
        try {
            WrapperPlayServerActionBar wrapper = new WrapperPlayServerActionBar(event);
            Component originalActionBar = wrapper.getActionBarText();
            Component formattedActionBar = formatTitle(originalActionBar, "actionbar");
            if (formattedActionBar != null) {
                wrapper.setActionBarText(formattedActionBar);
            }
        } catch (Exception e) {
            if (Settings.DEBUG.toBool()) {
                Logs.logWarning("Error whilst processing actionbar packet");
                e.printStackTrace();
            }
        }
    }

    private Component formatTitle(Component component, String type) {
        try {
            if (component == null) return null;

            String json = GsonComponentSerializer.gson().serialize(component);

            String processedText = PacketHelpers.readJson(json);
            String formattedJson = PacketHelpers.toJson(processedText);

            return GsonComponentSerializer.gson().deserialize(formattedJson);
        } catch (Exception e) {
            if (Settings.DEBUG.toBool()) {
                Logs.logWarning("Error whilst formatting " + type + " component: " + e.getMessage());
                if (Settings.DEBUG.toBool()) e.printStackTrace();
            }
            return null;
        }
    }

    private Component formatTitleAlternative(Component component, String type) {
        try {
            if (component == null) return null;

            String miniMessage = AdventureUtils.MINI_MESSAGE.serialize(component);
            String formattedJson = PacketHelpers.toJson(miniMessage);

            return GsonComponentSerializer.gson().deserialize(formattedJson);
        } catch (Exception e) {
            if (Settings.DEBUG.toBool()) {
                Logs.logWarning("Error whilst formatting " + type + " component (alternative method): " + e.getMessage());
                if (Settings.DEBUG.toBool()) e.printStackTrace();
            }
            return null;
        }
    }
}