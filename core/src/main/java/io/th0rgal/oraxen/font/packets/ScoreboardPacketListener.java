package io.th0rgal.oraxen.font.packets;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ScoreboardPacketListener implements PacketListener {

    private final Optional<ScoreFormat> numberFormat = Optional.of(
            ScoreFormat.fixedScore(Component.text("test"))
    );

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.SCOREBOARD_OBJECTIVE) {
            return;
        }

        try {
            WrapperPlayServerScoreboardObjective wrapper = new WrapperPlayServerScoreboardObjective(event);

            if (wrapper.getMode() == WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE) {
                return;
            }

            wrapper.setScoreFormat(numberFormat.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}