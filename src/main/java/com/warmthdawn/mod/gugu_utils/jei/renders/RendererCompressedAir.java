package com.warmthdawn.mod.gugu_utils.jei.renders;

import com.google.common.collect.Lists;
import com.warmthdawn.mod.gugu_utils.handler.ClientEventHandler;
import com.warmthdawn.mod.gugu_utils.jei.ingedients.IngredientCompressedAir;
import com.warmthdawn.mod.gugu_utils.jei.ingedients.IngredientMana;
import me.desht.pneumaticcraft.client.gui.GuiUtils;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.List;

import static com.warmthdawn.mod.gugu_utils.tools.RenderUtils.renderItem;
import static com.warmthdawn.mod.gugu_utils.tools.RenderUtils.renderManaBarVertical;

public class RendererCompressedAir implements IIngredientRenderer<IngredientCompressedAir> {
    public static final RendererCompressedAir INSTANCE = new RendererCompressedAir();
    public static final String KEY_AIR_AMOUNT = "tooltips.gugu-utils.gas_amount";
    public static final String KEY_AIR_AMOUNT_TICK = "tooltips.gugu-utils.gas_amount.tick";
    public static final String KEY_AIR_AMOUNT_TOTAL = "tooltips.gugu-utils.gas_amount.total";
    public static final String KEY_PRESSURE = "tooltips.gugu-utils.pressure";

    private final int offsetX;
    private final int offsetY;

    private RendererCompressedAir() {
        offsetX = 0;
        offsetY = 0;
    }

    @Override
    @NotNull
    public List<String> getTooltip(@NotNull Minecraft minecraft, IngredientCompressedAir ingredient, ITooltipFlag tooltipFlag) {
        List<String> result = Lists.newArrayList();
        if (!(ingredient.getValue() instanceof Number)) {
            result.add("Air");
            return result;
        }

        int air = (int) ingredient.getValue();

        if (ingredient.getMinPressure() != 0) {
            result.add(I18n.format(KEY_PRESSURE, ingredient.getMinPressure()));
        }
        if (air > 0) {
            if (ingredient.getTicks() > 0) {
                result.add(I18n.format(KEY_AIR_AMOUNT_TICK, air));
                result.add(I18n.format(KEY_AIR_AMOUNT_TOTAL, air * ingredient.getTicks()));
            } else {
                result.add(I18n.format(KEY_AIR_AMOUNT, air));
            }
        }


        return result;
    }

    @Override
    public void render(@NotNull Minecraft minecraft, int xPosition, int yPosition, @Nullable IngredientCompressedAir ingredient) {
        xPosition += offsetX + 20;
        yPosition += offsetY + 20;
        if (ingredient == null) {
            return;
        }
        float minPressure = ingredient.getMinPressure();
        float particalTicks = minecraft.getRenderPartialTicks();
        float ticks = (ClientEventHandler.elapsedTicks + particalTicks) % 60;
        float dangerPressure = PneumaticValues.DANGER_PRESSURE_TIER_TWO;
        float maxPressure = PneumaticValues.MAX_PRESSURE_TIER_TWO;
        float p2;
        if (Math.abs(minPressure) < 0.001) {
            minPressure = -Float.MAX_VALUE;
            p2 = dangerPressure * (ticks / 60);
        } else {
            p2 = minPressure * (ticks / 60);
        }
        GuiUtils.drawPressureGauge(minecraft.fontRenderer, -1, maxPressure, dangerPressure, minPressure, p2, xPosition, yPosition, 90);


    }


}
