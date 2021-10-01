package com.warmthdawn.mod.gugu_utils.modularmachenary.hotair;

import com.warmthdawn.mod.gugu_utils.GuGuUtils;
import com.warmthdawn.mod.gugu_utils.common.GenericBlock;
import com.warmthdawn.mod.gugu_utils.modularmachenary.vanilla.OutputPortState;
import com.warmthdawn.mod.gugu_utils.modularmachenary.vanilla.TileEnergyOutputPort;
import lykrast.prodigytech.common.capability.CapabilityHotAir;
import lykrast.prodigytech.common.capability.IHotAir;
import lykrast.prodigytech.common.util.TemperatureHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.warmthdawn.mod.gugu_utils.common.Constants.*;
import static com.warmthdawn.mod.gugu_utils.tools.ResourceUtils.j;

public class BlockHotAirInputHatch extends GenericBlock implements ITileEntityProvider {
    public static final PropertyEnum<HotAirHatchState> STATE = PropertyEnum.create("state", HotAirHatchState.class);

    public BlockHotAirInputHatch() {
        super(Material.IRON);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(10.0F);
        setRegistryName(RESOURCE_HOTAIRHATCH_INPUT);
        setTranslationKey(j(GuGuUtils.MODID, NAME_HOTAIRHATCH_INPUT));

        setDefaultState(blockState.getBaseState().withProperty(STATE, HotAirHatchState.ON));
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        return true;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            IHotAir capability = tileEntity.getCapability(CapabilityHotAir.HOT_AIR, EnumFacing.UP);
            if (capability != null) {
                TemperatureHelper.hotAirDamage(entityIn, capability);
            }
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (te instanceof TileHotAirInputHatch) {
            return state.withProperty(STATE, ((TileHotAirInputHatch) te).getState());
        }
        return super.getActualState(state, world, pos);
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileHotAirInputHatch();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
