package model;

import java.util.function.UnaryOperator;

public record Movement(Param param, Dir dir) {
    public enum Param {
        CX, CY
    }

    public enum Dir implements UnaryOperator<Double> {
        POSITIVE(d -> d), NEGATIVE(d -> -d);
        private final UnaryOperator<Double> setDir;

        Dir(UnaryOperator<Double> setDir) {
            this.setDir = setDir;
        }

        @Override
        public Double apply(Double val) {
            return setDir.apply(val);
        }
    }
}
