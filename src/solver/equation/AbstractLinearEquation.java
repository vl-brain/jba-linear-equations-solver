package solver.equation;

import solver.Row;

public abstract class AbstractLinearEquation {
    protected final Row[] rows;
    protected final int sourceRowIndex;
    protected final int targetRowIndex;

    public AbstractLinearEquation(Row[] rows, int sourceRowIndex) {
        this(rows, sourceRowIndex, sourceRowIndex);
    }

    public AbstractLinearEquation(Row[] rows, int sourceRowIndex, int targetRowIndex) {
        this.rows = rows;
        this.sourceRowIndex = sourceRowIndex;
        this.targetRowIndex = targetRowIndex;
    }

    public final void eval() {
        if (evalInternal()) {
            System.out.println(this);
        }
    }

    protected abstract boolean evalInternal();

    protected String rowName(int index) {
        return "R" + (index + 1);
    }

    protected String getEquation() {
        return rowName(sourceRowIndex);
    }

    @Override
    public final String toString() {
        return getEquation() + " -> " + rowName(targetRowIndex);
    }

}
