package com.warmthdawn.mod.gugu_utils.modularmachenary.components;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import org.jetbrains.annotations.Nullable;

public class ComponentAspect extends ComponentType {
    @Nullable
    @Override
    public String requiresModid() {
        return "thaumcraft";
    }
}
