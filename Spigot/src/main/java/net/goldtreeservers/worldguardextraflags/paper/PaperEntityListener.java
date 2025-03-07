package net.goldtreeservers.worldguardextraflags.paper;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.SessionManager;
import io.papermc.paper.event.entity.EntityAttemptSmashAttackEvent;
import lombok.RequiredArgsConstructor;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PaperEntityListener implements Listener {

    private final WorldGuardPlugin worldGuardPlugin;
    private final SessionManager sessionManager;
    private final RegionContainer regionContainer;

    @EventHandler(ignoreCancelled = true)
    public void onSmashAttempt(EntityAttemptSmashAttackEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        LocalPlayer localPlayer = this.worldGuardPlugin.wrapPlayer((Player) entity);

        if (this.sessionManager.hasBypass(localPlayer, localPlayer.getWorld())) {
            return;
        }

        if (this.regionContainer.createQuery().queryState(localPlayer.getLocation(), localPlayer, Flags.SMASH_ATTACK) == StateFlag.State.DENY) {
            event.setResult(Event.Result.DENY);
        }
    }
}
