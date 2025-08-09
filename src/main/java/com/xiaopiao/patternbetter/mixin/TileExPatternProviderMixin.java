package com.xiaopiao.patternbetter.mixin;


import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogic;
import com.glodblock.github.extendedae.common.tileentities.TileExPatternProvider;
import com.xiaopiao.patternbetter.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileExPatternProvider.class   , priority = 500)
public abstract class TileExPatternProviderMixin extends PatternProviderBlockEntity {
    public TileExPatternProviderMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
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
