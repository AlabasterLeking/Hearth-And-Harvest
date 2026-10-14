package alabaster.hearthandharvest.client.gui;

import alabaster.hearthandharvest.HearthAndHarvest;
import alabaster.hearthandharvest.common.crafting.KegRecipe;
import alabaster.hearthandharvest.common.utilities.HHTextUtils;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class KegRecipeBookComponent extends RecipeBookComponent {
    protected static final ResourceLocation RECIPE_BOOK_BUTTONS = new ResourceLocation(HearthAndHarvest.MODID, "textures/gui/keg_recipe_book_buttons.png");

    @Override
    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(0, 0, 26, 16, RECIPE_BOOK_BUTTONS);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.filterButton == null) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        boolean stateBefore = this.filterButton.isStateTriggered();
        boolean clickedFilter = this.filterButton.visible && this.filterButton.isMouseOver(mouseX, mouseY);
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        if (clickedFilter && this.filterButton != null && this.filterButton.isStateTriggered() == stateBefore) {
            this.filterButton.setStateTriggered(!stateBefore);
        }
        return handled;
    }

    public void hide() {
        this.setVisible(false);
    }

    @Override
    @Nonnull
    protected Component getRecipeFilterName() {
        return HHTextUtils.getTranslation("container.recipe_book.fermentable");
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        ItemStack resultStack = recipe.getResultItem(this.minecraft.level.registryAccess());
        this.ghostRecipe.setRecipe(recipe);

        Slot resultSlot = slots.get(this.menu.getResultSlotIndex());
        if (resultSlot.getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(resultStack), resultSlot.x, resultSlot.y);
        }

        Iterator<Ingredient> ingredients = recipe instanceof KegRecipe kegRecipe
                ? kegRecipe.getSlotIngredients().stream().map(KegRecipe.SlotIngredient::ingredient).iterator()
                : recipe.getIngredients().iterator();
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, ingredients, 0);
    }
}