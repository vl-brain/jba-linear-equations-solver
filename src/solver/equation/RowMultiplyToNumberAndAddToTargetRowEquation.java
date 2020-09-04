package solver.equation;

import solver.ComplexNumber;
import solver.Row;

public class RowMultiplyToNumberAndAddToTargetRowEquation extends AbstractLinearEquation {
    private final ComplexNumber k;

    public RowMultiplyToNumberAndAddToTargetRowEquation(Row[] rows, int sourceRowIndex, int targetRowIndex,
                                                        ComplexNumber k) {
        super(rows, sourceRowIndex, targetRowIndex);
        this.k = k;
    }

    @Override
    protected boolean evalInternal() {
        final Row resultRow = rows[sourceRowIndex].copy();
        resultRow.multiply(k);
        rows[targetRowIndex].add(resultRow);
        return true;
    }

    @Override
    protected String getEquation() {
        return k + " * " + super.getEquation() + " + " + rowName(targetRowIndex);
    }
}
