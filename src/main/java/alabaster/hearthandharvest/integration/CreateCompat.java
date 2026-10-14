package alabaster.hearthandharvest.integration;

import alabaster.hearthandharvest.common.registry.HHModAttachments;
import alabaster.hearthandharvest.common.registry.HHModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class CreateCompat {
    private static final ResourceLocation POTATO_PROJECTILE = new ResourceLocation("create", "potato_projectile");

    // Effects (pungent, poison) are handled by on_entity_hit in the data file.
    // This handler only sets the fly-particle ticks, which can't be done via JSON.
    @SubscribeEvent
    public static void onPotatoCannonHit(ProjectileImpactEvent event) {
        Projectile proj = event.getProjectile();
        if (!POTATO_PROJECTILE.equals(ForgeRegistries.ENTITY_TYPES.getKey(proj.getType()))) return;
        if (proj.level().isClientSide) return;
        CompoundTag data = proj.saveWithoutId(new CompoundTag());
        if (!ItemStack.of(data.getCompound("Item")).is(HHModItems.MANURE.get())) return;
        if (!(event.getRayTraceResult() instanceof EntityHitResult entityHit)) return;
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return;
        if (target == proj.getOwner()) return;
        HHModAttachments.MANURE_FLY_TICKS.set(target, 200);
    }
}
