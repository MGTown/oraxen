package io.th0rgal.oraxen.mechanics.provided.gameplay.efficiency;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.th0rgal.oraxen.mechanics.Mechanic;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.utils.PluginUtils;
import org.bukkit.configuration.ConfigurationSection;

public class EfficiencyMechanicFactory extends MechanicFactory {

    private EfficiencyMechanicListener listener;

    public EfficiencyMechanicFactory(ConfigurationSection section) {
        super(section);

        if (PluginUtils.isEnabled("packetevents")) {
            if (listener != null) {
                PacketEvents.getAPI().getEventManager().unregisterListener(listener);
            }

            listener = new EfficiencyMechanicListener(this);
            PacketEvents.getAPI().getEventManager().registerListener(listener, PacketListenerPriority.NORMAL);
        }
    }

    @Override
    public Mechanic parse(ConfigurationSection itemMechanicConfiguration) {
        Mechanic mechanic = new EfficiencyMechanic(this, itemMechanicConfiguration);
        addToImplemented(mechanic);
        return mechanic;
    }

    public EfficiencyMechanicFactory getInstance() {
        return this;
    }

}
