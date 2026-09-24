package alabaster.hearthandharvest.client.gui;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.KegBlockEntity;
import alabaster.hearthandharvest.common.block.entity.container.KegMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class KegGUI extends AbstractContainerScreen<KegMenu> implements RecipeUpdateListener {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "textures/gui/keg_gui.png");

    private static final int INPUT_TANK_X = 8;
    private static final int OUTPUT_TANK_X = 152;
    private static final int TANK_Y = 22;
    private static final int TANK_WIDTH = 16;
    private static final int TANK_HEIGHT = 63;
    private static final int FLUID_INSET = 0;
    private static final int FLUID_BUBBLE_COUNT = 9;
    private static final int FLUID_BUBBLE_COLOR = 0x1AFFFFFF;
    private static final int INPUT_TANK_OVERLAY_U = 192;
    private static final int OUTPUT_TANK_OVERLAY_U = 208;
    private static final int TANK_OVERLAY_V = 0;

    private static final int LEFT_BUBBLES_X = 54;
    private static final int RIGHT_BUBBLES_X = 114;
    private static final int BUBBLES_Y = 30;
    private static final int BUBBLES_WIDTH = 8;
    private static final int BUBBLES_HEIGHT = 47;
    private static final int BUBBLES_U = 176;
    private static final int BUBBLES_V = 3;

    private static final int MODE_BUTTON_X = 71;
    private static final int MODE_BUTTON_Y = 46;
    private static final int MODE_BUTTON_WIDTH = 34;
    private static final int MODE_BUTTON_HEIGHT = 18;

    private static final int RECIPE_BUTTON_X = 78;
    private static final int RECIPE_BUTTON_Y = 68;
    private static final WidgetSprites RECIPE_BUTTON = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("recipe_book/button"),
            ResourceLocation.withDefaultNamespace("recipe_book/button"));

    private final KegRecipeBookComponent recipeBookComponent = new KegRecipeBookComponent();
    private boolean widthTooNarrow;
    private Button modeButton;

    public KegGUI(KegMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + RECIPE_BUTTON_X, this.topPos + RECIPE_BUTTON_Y, 20, 18, RECIPE_BUTTON, button -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + RECIPE_BUTTON_X, this.topPos + RECIPE_BUTTON_Y);
            if (this.modeButton != null) {
                this.modeButton.setPosition(this.leftPos + MODE_BUTTON_X, this.topPos + MODE_BUTTON_Y);
            }
        }));
        this.modeButton = this.addRenderableWidget(Button.builder(modeLabel(), button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, KegMenu.MODE_BUTTON_ID);
                    }
                })
                .bounds(this.leftPos + MODE_BUTTON_X, this.topPos + MODE_BUTTON_Y, MODE_BUTTON_WIDTH, MODE_BUTTON_HEIGHT)
                .build());
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);

        if (this.minecraft != null) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BARREL_OPEN, 0.8F, 0.5F));
        }
    }

    private Component modeTooltip() {
        return Component.translatable(this.menu.blockEntity.isFillMode()
                ? "container.hearthandharvest.keg.fill.tooltip"
                : "container.hearthandharvest.keg.drain.tooltip");
    }

    private Component modeLabel() {
        return Component.translatable(this.menu.blockEntity.isFillMode()
                ? "container.hearthandharvest.keg.fill"
                : "container.hearthandharvest.keg.drain");
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        if (this.modeButton != null) {
            this.modeButton.setMessage(modeLabel());
            this.modeButton.setTooltip(Tooltip.create(modeTooltip()));
        }
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(gui, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTick);
        } else {
            this.renderBackground(gui, mouseX, mouseY, partialTick);
            super.render(gui, mouseX, mouseY, partialTick);
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTick);
            this.recipeBookComponent.renderGhostRecipe(gui, this.leftPos, this.topPos, false, partialTick);
        }
        this.renderTankTooltip(gui, mouseX, mouseY, INPUT_TANK_X, this.menu.blockEntity.getInputTank());
        this.renderTankTooltip(gui, mouseX, mouseY, OUTPUT_TANK_X, this.menu.blockEntity.getOutputTank());
        this.renderProgressTooltip(gui, mouseX, mouseY);
        this.renderTooltip(gui, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(gui, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderTooltip(gui, mouseX, mouseY);

        if (this.hoveredSlot != null && this.hoveredSlot.getItem().isEmpty()) {
            Component label = slotLabel(this.hoveredSlot.index);
            if (label != null) {
                gui.renderTooltip(this.font, label, mouseX, mouseY);
            }
        }
    }

    private static Component slotLabel(int slotIndex) {
        return switch (slotIndex) {
            case 0, 1 -> Component.translatable("gui.hearthandharvest.keg.ingredient_slot");
            case 2 -> Component.translatable("gui.hearthandharvest.keg.container_slot");
            case 3 -> Component.translatable("gui.hearthandharvest.keg.container_output_slot");
            case 4, 5 -> Component.translatable("gui.hearthandharvest.keg.output_slot");
            default -> null;
        };
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonId)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, buttonId);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonIdx) {
        boolean outside = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.imageWidth) || mouseY >= (double) (y + this.imageHeight);
        return outside && this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonIdx);
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        gui.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        renderTank(gui, INPUT_TANK_X, INPUT_TANK_OVERLAY_U, this.menu.blockEntity.getInputTank());
        renderFluidBubbles(gui);
        renderTank(gui, OUTPUT_TANK_X, OUTPUT_TANK_OVERLAY_U, this.menu.blockEntity.getOutputTank());
        renderProgressBubbles(gui);
    }

    private void renderFluidBubbles(GuiGraphics gui) {
        if (!this.menu.isFermenting()) return;

        FluidTank tank = this.menu.blockEntity.getInputTank();
        if (tank.isEmpty()) return;

        int filled = Mth.clamp(TANK_HEIGHT * tank.getFluidAmount() / tank.getCapacity(), 1, TANK_HEIGHT);
        int surface = TANK_Y + TANK_HEIGHT - filled;
        long time = System.currentTimeMillis() / 60L;

        for (int i = 0; i < FLUID_BUBBLE_COUNT; i++) {
            int rise = (int) ((time / 2L + i * 11L) % filled);
            int y = TANK_Y + TANK_HEIGHT - 1 - rise;
            if (y <= surface) continue;

            int drift = (int) Math.round(Math.sin(time / 9.0D + i * 2.3D) * 2.0D);
            int x = INPUT_TANK_X + 3 + (i * 5) % (TANK_WIDTH - 6) + drift;
            gui.fill(this.leftPos + x, this.topPos + y, this.leftPos + x + 1, this.topPos + y + 1, FLUID_BUBBLE_COLOR);
        }
    }

    private void renderProgressBubbles(GuiGraphics gui) {
        int filled = this.menu.getProgressScaled(BUBBLES_HEIGHT);
        if (filled <= 0) return;

        for (int x : new int[]{LEFT_BUBBLES_X, RIGHT_BUBBLES_X}) {
            gui.blit(BACKGROUND,
                    this.leftPos + x,
                    this.topPos + BUBBLES_Y + BUBBLES_HEIGHT - filled,
                    BUBBLES_U,
                    BUBBLES_V + BUBBLES_HEIGHT - filled,
                    BUBBLES_WIDTH,
                    filled);
        }
    }

    private void renderTank(GuiGraphics gui, int x, int overlayU, FluidTank tank) {
        FluidStack fluid = tank.getFluid();
        if (!fluid.isEmpty()) {
            renderFluid(gui, x, tank, fluid);
        }
        gui.blit(BACKGROUND, this.leftPos + x, this.topPos + TANK_Y, overlayU, TANK_OVERLAY_V, TANK_WIDTH, TANK_HEIGHT);
    }

    private void renderFluid(GuiGraphics gui, int x, FluidTank tank, FluidStack fluid) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTexture = extensions.getStillTexture(fluid);
        if (stillTexture == null) return;

        TextureAtlasSprite sprite = this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tint = extensions.getTintColor(fluid);
        int height = TANK_HEIGHT - FLUID_INSET * 2;
        int width = TANK_WIDTH - FLUID_INSET * 2;
        int filled = Mth.clamp(height * fluid.getAmount() / tank.getCapacity(), 1, height);

        RenderSystem.setShaderColor(
                (tint >> 16 & 0xFF) / 255.0F,
                (tint >> 8 & 0xFF) / 255.0F,
                (tint & 0xFF) / 255.0F,
                (tint >> 24 & 0xFF) / 255.0F);

        int drawn = 0;
        while (drawn < filled) {
            int slice = Math.min(16, filled - drawn);
            gui.blit(this.leftPos + x + FLUID_INSET, this.topPos + TANK_Y + FLUID_INSET + height - drawn - slice, 0, width, slice, sprite);
            drawn += slice;
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderTankTooltip(GuiGraphics gui, int mouseX, int mouseY, int x, FluidTank tank) {
        if (!this.isHovering(x, TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY)) return;

        FluidStack fluid = tank.getFluid();
        List<Component> lines = fluid.isEmpty()
                ? List.of(Component.translatable("container.hearthandharvest.keg.empty"))
                : List.of(fluid.getHoverName(),
                Component.translatable("container.hearthandharvest.keg.amount", fluid.getAmount(), tank.getCapacity()));
        gui.renderComponentTooltip(this.font, lines, mouseX, mouseY);
    }

    private void renderProgressTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        boolean hovering = this.isHovering(LEFT_BUBBLES_X, BUBBLES_Y, BUBBLES_WIDTH, BUBBLES_HEIGHT, mouseX, mouseY)
                || this.isHovering(RIGHT_BUBBLES_X, BUBBLES_Y, BUBBLES_WIDTH, BUBBLES_HEIGHT, mouseX, mouseY);
        if (!hovering) return;

        int remaining = this.menu.getRemainingSeconds();
        Component text = remaining <= 0
                ? Component.translatable("container.hearthandharvest.keg.idle")
                : Component.translatable("container.hearthandharvest.keg.remaining", remaining / 60, remaining % 60);
        gui.renderTooltip(this.font, text, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        gui.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }
}