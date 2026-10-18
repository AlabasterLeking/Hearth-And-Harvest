package alabaster.hearthandharvest.common.event;

import alabaster.hearthandharvest.HearthAndHarvest;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = HearthAndHarvest.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ChickenGlideClientEvents {
    private static final float MODEL_ORIGIN = 1.501F;
    private static final float VANILLA_HEIGHT_OFFSET = -0.2F;
    private static final float VANILLA_BOTTOM = MODEL_ORIGIN - 19.0F / 16.0F;
    private static final Map<Class<?>, List<Field>> PART_FIELDS = new ConcurrentHashMap<>();
    private static final List<ModelPart> HIDDEN = new ArrayList<>();
    private static float appliedOffset;

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

        EntityModel<?> model = event.getRenderer().getModel();
        List<ModelPart> roots = rootParts(model);
        HIDDEN.clear();
        for (ModelPart leg : legs(model, roots)) {
            if (leg.visible) {
                leg.visible = false;
                HIDDEN.add(leg);
            }
        }
        appliedOffset = heightOffset(roots, chicken);
        event.getPoseStack().translate(0.0F, appliedOffset, 0.0F);
    }

    @SubscribeEvent
    public static void onRenderPost(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof Chicken chicken) || !(chicken.getVehicle() instanceof Player)) return;

        for (ModelPart leg : HIDDEN) leg.visible = true;
        HIDDEN.clear();
        event.getPoseStack().translate(0.0F, -appliedOffset, 0.0F);
    }

    private static float heightOffset(List<ModelPart> roots, Chicken chicken) {
        float[] maxY = {Float.NEGATIVE_INFINITY};
        PoseStack poseStack = new PoseStack();
        for (ModelPart root : roots) measure(root, poseStack, maxY);
        if (maxY[0] == Float.NEGATIVE_INFINITY) return VANILLA_HEIGHT_OFFSET;
        float bottom = (MODEL_ORIGIN - maxY[0]) * chicken.getScale();
        return Mth.clamp(VANILLA_HEIGHT_OFFSET + VANILLA_BOTTOM - bottom, -1.0F, 1.0F);
    }

    private static void measure(ModelPart part, PoseStack poseStack, float[] maxY) {
        if (!part.visible) return;
        poseStack.pushPose();
        part.translateAndRotate(poseStack);
        if (!part.skipDraw) {
            Matrix4f pose = poseStack.last().pose();
            for (ModelPart.Cube cube : part.cubes) {
                for (int i = 0; i < 8; i++) {
                    Vector3f corner = new Vector3f((i & 1) == 0 ? cube.minX : cube.maxX, (i & 2) == 0 ? cube.minY : cube.maxY, (i & 4) == 0 ? cube.minZ : cube.maxZ).div(16.0F);
                    maxY[0] = Math.max(maxY[0], pose.transformPosition(corner).y);
                }
            }
        }
        for (ModelPart child : part.children.values()) measure(child, poseStack, maxY);
        poseStack.popPose();
    }

    private static List<ModelPart> legs(EntityModel<?> model, List<ModelPart> roots) {
        List<ModelPart> legs = new ArrayList<>();
        if (model instanceof ChickenModel<?> chickenModel) {
            legs.add(chickenModel.rightLeg);
            legs.add(chickenModel.leftLeg);
            return legs;
        }
        for (ModelPart root : roots) findLegs(root, legs);
        return legs;
    }

    private static void findLegs(ModelPart part, List<ModelPart> legs) {
        for (Map.Entry<String, ModelPart> entry : part.children.entrySet()) {
            if (entry.getKey().equals("right_leg") || entry.getKey().equals("left_leg")) {
                legs.add(entry.getValue());
            } else {
                findLegs(entry.getValue(), legs);
            }
        }
    }

    private static List<ModelPart> rootParts(EntityModel<?> model) {
        if (model instanceof HierarchicalModel<?> hierarchical) return List.of(hierarchical.root());
        List<ModelPart> parts = new ArrayList<>();
        for (Field field : PART_FIELDS.computeIfAbsent(model.getClass(), ChickenGlideClientEvents::findPartFields)) {
            try {
                if (field.get(model) instanceof ModelPart part && !parts.contains(part)) parts.add(part);
            } catch (IllegalAccessException ignored) {
            }
        }
        Set<ModelPart> nested = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ModelPart part : parts) part.getAllParts().skip(1).forEach(nested::add);
        parts.removeIf(nested::contains);
        return parts;
    }

    private static List<Field> findPartFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType() != ModelPart.class || Modifier.isStatic(field.getModifiers())) continue;
                try {
                    field.setAccessible(true);
                    fields.add(field);
                } catch (RuntimeException ignored) {
                }
            }
        }
        return fields;
    }
}