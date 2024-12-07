package model;

public record Movement(Param param, Dir dir) {
    public enum Param {
        CX, CY, POWER
    }

    public enum Dir {
        POSITIVE(+0.01), NEGATIVE(-0.01);
        private final double delta;

        Dir(double delta) {
            this.delta = delta;
        }

        public double add(double value) {
            return value + delta;
        }
    }
}
