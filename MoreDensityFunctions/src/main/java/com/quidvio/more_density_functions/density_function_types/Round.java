package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Round(DensityFunction df) implements DensityFunctionTypes.class_6932 {

    private static final MapCodec<Round> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(Round::df)).apply(instance, (Round::new)));
    public static final CodecHolder<Round> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);

    @Override
    public DensityFunction input() {
        return this.df;
    }

    @Override
    public double apply(double density) {
        return Math.round(density);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return new Round(this.df.apply(visitor));
    }

    @Override
    public double minValue() {
        return apply(this.df.minValue());
    }

    @Override
    public double maxValue() {
        return apply(this.df.maxValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
