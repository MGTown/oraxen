package io.th0rgal.oraxen.mechanics.provided.gameplay.efficiency;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class EfficiencyMechanicListener extends PacketListenerCommon implements PacketListener {

    private final EfficiencyMechanicFactory factory;

    public EfficiencyMechanicListener(final EfficiencyMechanicFactory factory) {
        this.factory = factory;
    }

    @Override
    public PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
        return PacketListener.super.asAbstract(priority);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_DIGGING) {
            return;
        }

        final Player player = (Player) event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        final String itemID = OraxenItems.getIdByItem(item);
        if (factory.isNotImplementedIn(itemID)) {
            return;
        }

        final EfficiencyMechanic mechanic = (EfficiencyMechanic) factory.getMechanic(itemID);

        final WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);
        final DiggingAction action = wrapper.getAction();

        if (action == DiggingAction.START_DIGGING) {
            Bukkit.getScheduler().runTask(OraxenPlugin.get(), () ->
                    player.addPotionEffect(new PotionEffect(mechanic.getType(),
                            20 * 60 * 5, // 5分钟
                            mechanic.getAmount() - 1,
                            false, false, false)));
        } else if (action == DiggingAction.CANCELLED_DIGGING || action == DiggingAction.FINISHED_DIGGING) {
            Bukkit.getScheduler().runTask(OraxenPlugin.get(), () ->
                    player.removePotionEffect(mechanic.getType()));
        }
    }
}
