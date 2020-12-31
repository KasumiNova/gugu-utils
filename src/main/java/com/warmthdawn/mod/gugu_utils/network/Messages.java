package com.warmthdawn.mod.gugu_utils.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Messages {
    public static SimpleNetworkWrapper INSTANCE;

    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);

        // Server side
        INSTANCE.registerMessage(PacketStarlight.Handler.class, PacketStarlight.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketSetContainerSlot.Handler.class, PacketSetContainerSlot.class, nextID(), Side.SERVER);


        // Client side
        INSTANCE.registerMessage(PacketStarlight.Handler.class, PacketStarlight.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketCollectorPostion.Handler.class, PacketCollectorPostion.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketMana.Handler.class, PacketMana.class, nextID(), Side.CLIENT);
    }
}
