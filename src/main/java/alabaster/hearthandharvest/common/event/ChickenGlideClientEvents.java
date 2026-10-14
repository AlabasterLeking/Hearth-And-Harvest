package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.common.mixin.accessor.ChickenModelAccessor;
import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RenderLivingEvent;

@Mod.EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChickenGlideClientEvents {
    private static final float HEIGHT_OFFSET = -0.2F;

    @SubscribeEvent
    public static void onRenderPre(RenderLivingEvent.Pre<?, ?> event) {
        if (!(event.getEntity() instanceof Chicken chicken) || !(chicken.getVehicle() instanceof Player player)) return;

        float bodyRot = Mth.rotLerp(event.getPartialTick(), player.yBodyRotO, player.yBodyRot);
        chicken.yBodyRot = bodyRot;
        chicken.yBodyRotO = bodyRot;
        chicken.yHeadRot = bodyRot;
        chicken.yHeadRotO = bodyRot;
        chicken.setXRot(0.0F);
        chicken.xRotO = 0.0F;

        if (event.getRenderer().getModel() instanceof ChickenModel<?> model) {
            ((ChickenModelAccessor) model).hearthandharvest$getRightLeg().visible = false;
            ((ChickenModelAccessor) model).hearthandharvest$getLeftLeg().visible = false;
        }
        event.getPoseStack().translate(0.0F, HEIGHT_OFFSET, 0.0F);
    }

    @SubscribeEvent
    public static void onRenderPost(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof Chicken chicken) || !(chicken.getVehicle() instanceof Player)) return;

        if (event.getRenderer().getModel() instanceof ChickenModel<?> model) {
            ((ChickenModelAccessor) model).hearthandharvest$getRightLeg().visible = true;
            ((ChickenModelAccessor) model).hearthandharvest$getLeftLeg().visible = true;
        }
        event.getPoseStack().translate(0.0F, -HEIGHT_OFFSET, 0.0F);
    }
}