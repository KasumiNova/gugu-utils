package com.warmthdawn.mod.gugu_utils.botania.lens;

import com.warmthdawn.mod.gugu_utils.GuGuUtils;
import com.warmthdawn.mod.gugu_utils.botania.BotaniaCompact;
import com.warmthdawn.mod.gugu_utils.botania.recipes.TransformRecipe;
import com.warmthdawn.mod.gugu_utils.crafttweaker.gugu.TransformContext;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.common.item.lens.Lens;

import java.util.List;

public class LensTransform extends Lens {
    private static final String TAG_TRIPPED = GuGuUtils.MODID + "transformerLensTripped";
    private static final String TAG_MANA = GuGuUtils.MODID + "mana";

    @Override
    public void apply(ItemStack stack, BurstProperties props) {
        props.maxMana *= 4;
    }

    @Override
    public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
        IManaBurst burst = spreader.runBurstSimulation();
        Entity e = (Entity) burst;
        return e.getEntityData().getBoolean(TAG_TRIPPED);
    }


    private boolean doCraft(IManaBurst burst, EntityThrowable entity, TransformRecipe recipe, EntityItem entityItem) {
        ItemStack input = entityItem.getItem();
        int inputNum = recipe.getInputNum();
        int crafts = input.getCount() / inputNum;
        int mana;
        int outputNum;
        ItemStack output;

        if (recipe.getFunction() != null) {
            TransformContext transformContext = TransformContext.create(entity, recipe);
            boolean result = recipe.getFunction().process(CraftTweakerMC.getIItemStack(input), transformContext);
            if (!result) {
                return false;
            }
            mana = transformContext.getMana();
            output = transformContext.getOutputStack();
            outputNum = output.getCount();
        } else {
            mana = recipe.getMana();
            outputNum = recipe.getOutput().getCount();
            output = recipe.getOutput();
        }


        int previousMana = entityItem.getEntityData().getInteger(TAG_MANA);
        int outputCount = 0;

        for (int i = 0; i < crafts; i++) {

            if (burst.getMana() + previousMana >= mana) {
                if (previousMana != 0) {
                    entityItem.getEntityData().removeTag(TAG_MANA);
                    previousMana = 0;
                }
                burst.setMana(burst.getMana() + previousMana - mana);
                input.shrink(inputNum);
                outputCount += outputNum;
            } else {
                int currentMana = burst.getMana();
                entityItem.getEntityData().setInteger(TAG_MANA, currentMana);
                burst.setMana(0);
                break;
            }
        }

        if (outputCount > 0) {
            spawnOutput(entity, output, outputCount, output.getMaxStackSize());
            return true;
        }
        return false;
    }

    private void spawnOutput(EntityThrowable entity, ItemStack output, int outputCount, int maxStack) {
        while (outputCount > 0) {
            ItemStack out = output.copy();
            out.setCount(Math.min(maxStack, outputCount));
            entity.world.spawnEntity(
                new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,
                    out));
            outputCount -= maxStack;
        }
    }

    @Override
    public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {
        if (entity.world.isRemote)
            return;
        double range = 0.5;
        AxisAlignedBB bounds = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).grow(range);
        List<EntityItem> entities = entity.world.getEntitiesWithinAABB(EntityItem.class, bounds);


        if (!entities.isEmpty()) {

            for (EntityItem entityItem : entities) {
                if (!entityItem.isDead) {
                    for (TransformRecipe recipe : BotaniaCompact.recipeBurstTransform) {
                        ItemStack input = entityItem.getItem();
                        if (recipe.matches(input)) {
                            if (burst.isFake()) {
                                Entity e = (Entity) burst;
                                e.getEntityData().setBoolean(TAG_TRIPPED, true);
                                return;
                            }
                            if (doCraft(burst, entity, recipe, entityItem)) {
                                break;
                            }
                        }
                    }
                }
            }

        }

    }
}
