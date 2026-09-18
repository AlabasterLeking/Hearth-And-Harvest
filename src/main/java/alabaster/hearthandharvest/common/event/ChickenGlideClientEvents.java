package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ChickenGlideClientEvents {
    private static final float SINK_INTO_HEAD = 0.3F;

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
            model.rightLeg.visible = false;
            model.leftLeg.visible = false;
        }
        event.getPoseStack().translate(0.0F, -SINK_INTO_HEAD, 0.0F);
    }

    @SubscribeEvent
    public static void onRenderPost(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof Chicken chicken) || !(chicken.getVehicle() instanceof Player)) return;

        if (event.getRenderer().getModel() instanceof ChickenModel<?> model) {
            model.rightLeg.visible = true;
            model.leftLeg.visible = true;
        }
        event.getPoseStack().translate(0.0F, SINK_INTO_HEAD, 0.0F);
    }
}