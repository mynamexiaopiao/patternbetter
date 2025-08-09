package com.xiaopiao.patternbetter.mixin;


import appeng.api.parts.IPartItem;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.parts.AEBasePart;
import com.glodblock.github.extendedae.common.parts.PartExPatternProvider;
import com.xiaopiao.patternbetter.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = PartExPatternProvider.class  , priority = 500)
public abstract class PartExPatternProviderMixin extends AEBasePart implements PatternProviderLogicHost {
    public PartExPatternProviderMixin(IPartItem<?> partItem) {
        super(partItem);
    }


    @ModifyConstant(
            method = {"createLogic"},
            remap = false,
            constant = {@Constant(
                    intValue = 36
            )}
    )
    private int modifyContainer(int constant) {
        return ModConfig.slotValue;
    }
}
