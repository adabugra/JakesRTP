package biz.donvi.jakesRTP.claimsIntegrations;

import net.william278.husktowns.api.HuskTownsAPI;
import net.william278.husktowns.claim.Chunk;
import net.william278.husktowns.claim.World;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class LrHuskTowns implements LocationRestrictor {

    protected Plugin cmPlugin;

    public LrHuskTowns(Plugin cmPlugin) {
        this.cmPlugin = cmPlugin;
    }

    @Override
    public Plugin supporterPlugin() {
        return cmPlugin;
    }

    @Override
    public boolean denyLandingAtLocation(Location location) {
        return (!HuskTownsAPI.getInstance().isClaimAt(Chunk.at((int) location.getX(), (int) location.getZ()), World.of(location.getWorld().getUID(), location.getWorld().getName(), String.valueOf(location.getWorld().getEnvironment()))));
    }
}