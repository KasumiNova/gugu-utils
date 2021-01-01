package com.warmthdawn.mod.gugu_utils.modularmachenary.components;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import org.jetbrains.annotations.Nullable;

public class ComponentAura extends ComponentType {
    @Nullable
    @Override
    public String requiresModid() {
        return "naturesaura";
    }
}
