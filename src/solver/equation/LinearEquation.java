package solver.equation;

import solver.Row;

public class LinearEquation {
    private final int sourceRowIndex;
    private final int targetRowIndex;
    private double k;
    private int addRowIndex;
    private boolean evaluated = false;

    public LinearEquation(int sourceRowIndex, double k) {
        this(sourceRowIndex, sourceRowIndex, k);
    }

    public LinearEquation(int sourceRowIndex, int targetRowIndex, double k) {
        this(sourceRowIndex, targetRowIndex, k, -1);
    }

    public LinearEquation(int sourceRowIndex, int targetRowIndex, double k, int addRowIndex) {
        this.sourceRowIndex = sourceRowIndex;
        this.targetRowIndex = targetRowIndex;
        this.k = k;
        this.addRowIndex = addRowIndex;
    }

    public boolean eval(Row[] rows) {
        if (evaluated) {
            throw new IllegalStateException("Already evaluated!");
        }
        Row resultRow = rows[sourceRowIndex];
        if (sourceRowIndex != targetRowIndex) {
            resultRow = resultRow.copy();
        }
        evaluated = resultRow.multiply(k);
        if (addRowIndex >= 0) {
            evaluated = resultRow.add(rows[addRowIndex]);
        }
        if (evaluated) {
            if (sourceRowIndex != targetRowIndex) {
                rows[targetRowIndex] = resultRow;
            }
            System.out.print(this);
        }
        return evaluated;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (k != 1) {
            builder.append(String.format("%.1f * ", k));
        }
        builder.append(String.format("R%d", sourceRowIndex + 1));
        if (addRowIndex >= 0) {
            builder.append(String.format(" + R%d", addRowIndex + 1));
        }
        builder.append(String.format(" -> R%d\n", targetRowIndex + 1));
        return builder.toString();
    }

}