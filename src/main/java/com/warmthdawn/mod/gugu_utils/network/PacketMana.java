package com.warmthdawn.mod.gugu_utils.network;

import com.warmthdawn.mod.gugu_utils.GuGuUtils;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.TileSparkManaHatch;
import com.warmthdawn.mod.gugu_utils.modularmachenary.mana.TileSparkManaOutputHatch;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.activation.MailcapCommandMap;

public class PacketMana implements IMessage {
    private int mana;


    private int bx;
    private int by;
    private int bz;

    public PacketMana() {

    }

    public PacketMana(BlockPos pos, int data) {
        this.mana = data;
        this.bx = pos.getX();
        this.by = pos.getY();
        this.bz = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mana = buf.readInt();

        bx = buf.readInt();
        by = buf.readInt();
        bz = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(mana);

        buf.writeInt(bx);
        buf.writeInt(by);
        buf.writeInt(bz);
    }

    public int getMana() {
        return mana;
    }

    public BlockPos getPos() {
        return new BlockPos(bx, by, bz);
    }

    public static class Handler implements IMessageHandler<PacketMana, IMessage> {

        @Override
        public IMessage onMessage(PacketMana message, MessageContext ctx) {
            GuGuUtils.proxy.addScheduledTaskClient(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketMana message, MessageContext ctx) {
            EntityPlayer player = GuGuUtils.proxy.getClientPlayer();
            TileEntity te = player.world.getTileEntity(message.getPos());
            if (te instanceof TileSparkManaHatch) {
                ((TileSparkManaHatch) te).sync(message.getMana());
            }
        }
    }
}
