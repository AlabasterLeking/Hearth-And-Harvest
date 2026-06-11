package alabaster.hearthandharvest.integration;

import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

public class CreateCompat {

    // Effects (pungent, poison) are handled by on_entity_hit in the data file.
    // This handler only sets the fly-particle ticks, which can't be done via JSON.
    @SubscribeEvent
    public static void onPotatoCannonHit(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof PotatoProjectileEntity proj)) return;
        if (proj.level().isClientSide) return;
        if (!proj.getItem().is(HHModItems.MANURE.get())) return;
        if (!(event.getRayTraceResult() instanceof EntityHitResult entityHit)) return;
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return;
        if (target == proj.getOwner()) return;
        target.setData(HHModAttachments.MANURE_FLY_TICKS.get(), 200);
    }
}