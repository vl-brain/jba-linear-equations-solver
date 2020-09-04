package solver.equation;

import solver.ComplexNumber;
import solver.Row;

public class RowDivideToNumberEquation extends AbstractLinearEquation {
    private final ComplexNumber k;

    public RowDivideToNumberEquation(Row[] rows, int rowIndex, ComplexNumber k) {
        super(rows, rowIndex);
        this.k = k;
    }

    @Override
    public boolean evalInternal() {
        return rows[sourceRowIndex].divide(k);
    }

    @Override
    protected String getEquation() {
        return super.getEquation() + " / " + k;
    }
}
