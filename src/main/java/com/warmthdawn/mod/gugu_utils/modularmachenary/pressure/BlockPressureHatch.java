package com.warmthdawn.mod.gugu_utils.modularmachenary.pressure;

import com.warmthdawn.mod.gugu_utils.GuGuUtils;
import com.warmthdawn.mod.gugu_utils.common.IGuiProvider;
import com.warmthdawn.mod.gugu_utils.common.VariantBlock;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.ManaHatchVariant;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.TileSparkManaHatch;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.TileSparkManaInputHatch;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.TileSparkManaOutputHatch;
import me.desht.pneumaticcraft.common.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.warmthdawn.mod.gugu_utils.common.Constants.*;
import static com.warmthdawn.mod.gugu_utils.tools.ResourceUtils.TOOLTIP_PREFIX;
import static com.warmthdawn.mod.gugu_utils.tools.ResourceUtils.j;


public class BlockPressureHatch extends VariantBlock<PressureHatchVariant> implements ITileEntityProvider {

    public static final PropertyEnum<PressureHatchVariant> VARIANT = PropertyEnum.create("variant", PressureHatchVariant.class);


    public BlockPressureHatch() {
        super(Material.ROCK, PressureHatchVariant.class);
        setHardness(2.0F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
        setRegistryName(RESOURCE_PRESSUREHATCH);
        setTranslationKey(j(GuGuUtils.MODID, NAME_PRESSUREHATCH));

    }


    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return 0;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos tilePos) {
        if (world instanceof World && !((World) world).isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBase) {
                ((TileEntityBase) te).onNeighborTileUpdate();
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBase) {
                ((TileEntityBase) te).onNeighborBlockUpdate();
            }
        }
    }


    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == PressureHatchVariant.OUTPUT.ordinal()) {
            return new TilePressureOutputHatch();
        } else {
            return new TilePressureInputHatch();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {

    }

    @Override
    public PropertyEnum<PressureHatchVariant> getVariant() {
        return VARIANT;
    }
}
