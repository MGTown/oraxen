package io.th0rgal.oraxen.font.packets;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import io.th0rgal.oraxen.utils.PacketHelpers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class InventoryPacketListener extends PacketListenerCommon implements PacketListener {

    @Override
    public PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
        return PacketListener.super.asAbstract(priority);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.OPEN_WINDOW) {
            return;
        }

        try {
            WrapperPlayServerOpenWindow wrapper = new WrapperPlayServerOpenWindow(event);
            Component originalTitle = wrapper.getTitle();

            if (originalTitle != null) {
                Component processedTitle = processInventoryTitle(originalTitle);
                if (processedTitle != null) {
                    wrapper.setTitle(processedTitle);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private Component processInventoryTitle(Component title) {
        try {
            String json = GsonComponentSerializer.gson().serialize(title);

            String processedText = PacketHelpers.readJson(json);
            String formattedJson = PacketHelpers.toJson(processedText);

            return GsonComponentSerializer.gson().deserialize(formattedJson);
        } catch (Exception e) {
            return null;
        }
    }
}