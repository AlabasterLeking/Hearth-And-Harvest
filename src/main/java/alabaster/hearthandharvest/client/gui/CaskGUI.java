package alabaster.hearthandharvest.client.gui;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.block.entity.container.CaskMenu;
import alabaster.hearthandharvest.common.utilities.HHTextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import java.awt.*;

public class CaskGUI extends AbstractContainerScreen<CaskMenu> implements RecipeUpdateListener {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(HearthAndHarvest.MODID, "textures/gui/cask_gui.png");

    private static final WidgetSprites RECIPE_BUTTON = new WidgetSprites(ResourceLocation.withDefaultNamespace("recipe_book/button"), ResourceLocation.withDefaultNamespace("recipe_book/button"));

    private static final Rectangle PROGRESS_ARROW = new Rectangle(81, 29, 0, 18);

    public static final Rectangle DIM_LIGHT = new Rectangle(85, 47, 13, 16);
    public static final Rectangle MEDIUM_LIGHT = new Rectangle(85, 47, 13, 16);
    public static final Rectangle BRIGHT_LIGHT = new Rectangle(85, 47, 13, 16);

    private static final Rectangle LEFT_BUBBLE = new Rectangle(108, 48, 9, 24);
    private static final Rectangle RIGHT_BUBBLE = new Rectangle(147, 48, 9, 24);

    private final CaskRecipeBookComponent recipeBookComponent = new CaskRecipeBookComponent();
    private boolean widthTooNarrow;

    private static final Rectangle TIMER_ICON = new Rectangle(89, 14, 7, 11);

    private static final int SEAL_BUTTON_X = 108;
    private static final int SEAL_BUTTON_Y = 59;
    private static final int SEAL_BUTTON_WIDTH = 48;
    private static final int SEAL_BUTTON_HEIGHT = 16;

    private Button sealButton;

    public CaskGUI(CaskMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.titleLabelX = 8;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, RECIPE_BUTTON, (button) ->
        {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + 5, this.height / 2 - 49);
        }));
        this.sealButton = this.addRenderableWidget(Button.builder(sealButtonLabel(), button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CaskMenu.SEAL_BUTTON_ID);
                    }
                })
                .bounds(this.leftPos + SEAL_BUTTON_X, this.topPos + SEAL_BUTTON_Y, SEAL_BUTTON_WIDTH, SEAL_BUTTON_HEIGHT)
                .build());
        updateSealButton();
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        // Render UI background
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft == null)
            return;

        gui.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Render progress arrow
        int l = this.menu.getCookProgressionScaled();
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 4, l + 1, PROGRESS_ARROW.height);

        // Render bubbles
        if (menu.getProgression() != 0) {
            int bubScale = (int) ((((this.menu.getProgression() / 80)) * LEFT_BUBBLE.height) % (LEFT_BUBBLE.height + 1));
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + LEFT_BUBBLE.x, this.topPos + LEFT_BUBBLE.y - bubScale, 176, 111 - bubScale, LEFT_BUBBLE.width, bubScale + 1);
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + RIGHT_BUBBLE.x, this.topPos + RIGHT_BUBBLE.y - bubScale, 186, 111 - bubScale, RIGHT_BUBBLE.width, bubScale + 1);
        }

        // Render light indicator
        int light = this.menu.blockEntity.getCurrentLightLevel();
        String lightCategory = light <= 5 ? "dim" : light <= 10 ? "medium" : "bright";
        switch (lightCategory) {
            case "dim" -> gui.blit(BACKGROUND_TEXTURE, this.leftPos + DIM_LIGHT.x, this.topPos + DIM_LIGHT.y, 176, 64, DIM_LIGHT.width, DIM_LIGHT.height);
            case "medium" -> gui.blit(BACKGROUND_TEXTURE, this.leftPos + MEDIUM_LIGHT.x, this.topPos + MEDIUM_LIGHT.y, 176, 48, MEDIUM_LIGHT.width, MEDIUM_LIGHT.height);
            case "bright" -> gui.blit(BACKGROUND_TEXTURE, this.leftPos + BRIGHT_LIGHT.x, this.topPos + BRIGHT_LIGHT.y, 176, 32, BRIGHT_LIGHT.width, BRIGHT_LIGHT.height);
        }
    }

    private void renderProgressTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (!this.isHovering(TIMER_ICON.x, TIMER_ICON.y, TIMER_ICON.width, TIMER_ICON.height, mouseX, mouseY)) return;

        int remaining = this.menu.getRemainingSeconds();
        Component text = remaining <= 0
                ? Component.translatable("container.hearthandharvest.cask.idle")
                : Component.translatable("container.hearthandharvest.cask.remaining", formatDuration(remaining));
        gui.renderTooltip(this.font, text, mouseX, mouseY);
    }

    private static Component formatDuration(int seconds) {
        if (seconds >= 3600) {
            return Component.translatable("container.hearthandharvest.cask.hours", seconds / 3600, (seconds % 3600) / 60);
        }
        if (seconds >= 60) {
            return Component.translatable("container.hearthandharvest.cask.minutes", seconds / 60);
        }
        return Component.translatable("container.hearthandharvest.cask.seconds", seconds);
    }

    private void renderLightTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.isHovering(DIM_LIGHT.x, DIM_LIGHT.y, DIM_LIGHT.width, DIM_LIGHT.height, mouseX, mouseY)) {
            int light = this.menu.blockEntity.getCurrentLightLevel();
            String lightCategory = light <= 5 ? "dim" : light <= 10 ? "medium" : "bright";
            String key = switch (lightCategory) {
                case "dim" -> "container.cask.dim";
                case "medium" -> "container.cask.medium";
                case "bright" -> "container.cask.bright";
                default -> "container.cask.default";
            };
            gui.renderTooltip(font, HHTextUtils.getTranslation(key), mouseX, mouseY);
        }
    }


    private Component sealButtonLabel() {
        return Component.translatable(this.menu.blockEntity.isSealed()
                ? "container.hearthandharvest.cask.unseal"
                : "container.hearthandharvest.cask.seal");
    }

    private void updateSealButton() {
        if (this.sealButton == null) return;
        this.sealButton.setPosition(this.leftPos + SEAL_BUTTON_X, this.topPos + SEAL_BUTTON_Y);
        this.sealButton.setMessage(sealButtonLabel());
        this.sealButton.setTooltip(Tooltip.create(Component.translatable(this.menu.blockEntity.isSealed()
                ? "container.hearthandharvest.cask.sealed.tooltip"
                : "container.hearthandharvest.cask.seal.tooltip")));
    }

    @Override
    public void render(GuiGraphics gui, final int mouseX, final int mouseY, float partialTicks) {
        updateSealButton();
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(gui, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTicks);
        } else {
            super.render(gui, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.render(gui, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(gui, this.leftPos, this.topPos, false, partialTicks);
        }
        this.renderTooltip(gui, mouseX, mouseY);
        this.renderLightTooltip(gui, mouseX, mouseY);
        this.renderProgressTooltip(gui, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(gui, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow && super.isHovering(x, y, width, height, mouseX, mouseY));
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderLabels(gui, mouseX, mouseY);
        gui.drawString(this.font, this.playerInventoryTitle, 8, (this.imageHeight - 96 + 2), 4210752, false);

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
        boolean flag = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.imageWidth) || mouseY >= (double) (y + this.imageHeight);
        return flag && this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonIdx);
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
    @Nonnull
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}