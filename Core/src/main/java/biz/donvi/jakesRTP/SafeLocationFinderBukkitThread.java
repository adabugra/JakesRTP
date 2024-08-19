package biz.donvi.jakesRTP;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static biz.donvi.jakesRTP.SafeLocationUtils.requireMainThread;

public class SafeLocationFinderBukkitThread extends SafeLocationFinder {

    public SafeLocationFinderBukkitThread(final Location loc) { super(loc); }

    public SafeLocationFinderBukkitThread(
        final Location loc, int checkRadiusXZ, int checkRadiusVert,
        int lowBound, int highBound
    ) { super(loc, checkRadiusXZ, checkRadiusVert, lowBound, highBound); }

    @Override
    protected Material getLocMaterial(Location loc) {
        requireMainThread();
        CompletableFuture<Material> materialFuture = new CompletableFuture<>();

        JakesRtpPlugin.morePaperLib.scheduling().regionSpecificScheduler(loc).run(() -> {
            try {
                Material material = loc.getBlock().getType();
                materialFuture.complete(material);
            } catch (Exception e) {
                materialFuture.completeExceptionally(e);
            }

        });

        try {
            return materialFuture.get(); // Waits for the task to complete and returns the result
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get material from location", e);
        }
    }

    @Override
    protected void dropToGround() {
        requireMainThread();
        SafeLocationUtils.util.dropToGround(loc, lowBound, highBound);
    }

    @Override
    protected void dropToMiddle() {
        requireMainThread();
        SafeLocationUtils.util.dropToMiddle(loc, lowBound, highBound);
    }

}
