package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.quidvio.more_density_functions.MoreDensityFunctionsMain;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

import java.util.Optional;

public record Modulo(DensityFunction dividend, DensityFunction divisor,
                     Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<Modulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(Modulo::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(Modulo::divisor), DensityFunction.FUNCTION_CODEC.optionalFieldOf("error_output").forGetter(Modulo::errorDf)).apply(instance, (Modulo::new)));
    public static final CodecHolder<Modulo> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);

    @Override
    public double sample(NoisePos pos) {

        int dividendValue = (int) this.dividend.sample(pos);
        int divisorValue = (int) this.divisor.sample(pos);

        if (divisorValue == 0) {
            if (errorDf.isPresent()) {
                return this.errorDf.get().sample(pos);
            }
            return MoreDensityFunctionsMain.DEFAULT_ERROR;
        }

        return dividendValue % divisorValue;
    }

    @Override
    public void method_40470(double[] densities, class_6911 applier) {
        applier.method_40478(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        if (this.errorDf.isPresent()) {
            return visitor.apply(new Modulo(this.dividend.apply(visitor), this.divisor.apply(visitor), Optional.of(this.errorDf.get().apply(visitor))));
        }
        return visitor.apply(new Modulo(this.dividend.apply(visitor), this.divisor.apply(visitor), Optional.empty()));
    }

    @Override
    public DensityFunction dividend() {
        return this.dividend;
    }

    @Override
    public DensityFunction divisor() {
        return this.divisor;
    }

    @Override
    public double minValue() {
        if (errorDf.isPresent()) {
            return Math.min(this.errorDf.get().minValue(), Math.min(-Math.abs(this.divisor.minValue()), -Math.abs(this.divisor.maxValue())));
        }
        return Math.min(MoreDensityFunctionsMain.DEFAULT_ERROR, Math.min(-Math.abs(this.divisor.minValue()), -Math.abs(this.divisor.maxValue())));
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(), Math.max(Math.abs(this.divisor.minValue()), Math.abs(this.divisor.maxValue())));
        }
        return Math.max(MoreDensityFunctionsMain.DEFAULT_ERROR, Math.max(Math.abs(this.divisor.minValue()), Math.abs(this.divisor.maxValue())));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}