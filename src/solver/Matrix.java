package solver;

import solver.equation.LinearEquation;
import solver.equation.RowDivideToNumberEquation;
import solver.equation.RowMultiplyToNumberAndAddToTargetRowEquation;

import java.util.stream.IntStream;

public class Matrix {
    private final int[] variableColumnIndexes;
    private final Row[] rows;

    public Matrix(Row[] rows) {
        this.rows = rows;
        this.variableColumnIndexes = rows.length == 0 ? new int[0] :
                IntStream.range(0, rows[0].variableLength())
                        .toArray();
    }

    private Matrix(Row[] rows, int[] variableColumnIndexes) {
        this.rows = rows;
        this.variableColumnIndexes = variableColumnIndexes;
    }

    private Matrix copy() {
        final Row[] rows = new Row[this.rows.length];
        for (int i = 0; i < this.rows.length; i++) {
            rows[i] = this.rows[i].copy();
        }
        return new Matrix(rows, variableColumnIndexes.clone());
    }

    private static Matrix rowEchelonForm(Matrix source) {
        return source.copy().toRowEchelonForm();
    }

    private static Matrix reducedRowEchelonForm(Matrix source) {
        return source.copy().toReducedRowEchelonForm();
    }

    private Matrix toRowEchelonForm() {
        for (int i = 0; i < variableColumnIndexes.length && i < rows.length; i++) {
            ComplexNumber item = getOrSwapIfZero(i, i);
            if (item == ComplexNumber.ZERO) {
                break;
            }
            new RowDivideToNumberEquation(rows, i, item).eval();
            for (int j = i + 1; j < rows.length; j++) {
                final ComplexNumber subItem = getItem(j, i);
                if (subItem == ComplexNumber.ZERO) {
                    continue;
                }
                new RowMultiplyToNumberAndAddToTargetRowEquation(rows, i, j, subItem.negate()).eval();
            }
        }
        return this;
    }

    private Matrix toReducedRowEchelonForm() {
        for (int i = variableColumnIndexes.length - 2; i >= 0; i--) {
            final ComplexNumber item = rows[i + 1].getItem(i + 1);
            if (item == ComplexNumber.ZERO) {
                continue;
            }
            for (int j = i; j >= 0; j--) {
                final ComplexNumber aboveItem = rows[j].getItem(i + 1);
                if (aboveItem == ComplexNumber.ZERO) {
                    continue;
                }
                new RowMultiplyToNumberAndAddToTargetRowEquation(rows, i + 1, j, aboveItem.negate()).eval();
            }
        }
        return this;
    }

    private ComplexNumber getOrSwapIfZero(int rowIndex, int columnIndex) {
        ComplexNumber item = getItem(rowIndex, columnIndex);
        if (item == ComplexNumber.ZERO && (swapInRowToNonZeroItem(columnIndex, rowIndex, rowIndex + 1) ||
                swapInColumnToVarNonZeroItem(rowIndex, columnIndex, columnIndex + 1) ||
                swapInVarSubMatrixToNonZeroItem(rowIndex, columnIndex,
                        rowIndex + 1, variableColumnIndexes.length - 1))) {
            item = getItem(rowIndex, columnIndex);
        }
        return item;
    }

    private ComplexNumber getItem(int rowIndex, int columnIndex) {
        return rows[rowIndex].getItem(columnIndex);
    }

    private boolean swapInRowToNonZeroItem(int columnIndex, int rowIndex, int otherRowIndex) {
        if (otherRowIndex >= rows.length) {
            return false;
        }
        if (getItem(otherRowIndex, columnIndex) == ComplexNumber.ZERO) {
            return swapInRowToNonZeroItem(columnIndex, rowIndex, otherRowIndex + 1);
        }
        swapRows(rowIndex, otherRowIndex);
        return true;
    }

    private boolean swapInColumnToVarNonZeroItem(int rowIndex, int columnIndex, int otherColumnIndex) {
        if (otherColumnIndex >= variableColumnIndexes.length) {
            return false;
        }
        if (getItem(rowIndex, otherColumnIndex) == ComplexNumber.ZERO) {
            return swapInColumnToVarNonZeroItem(rowIndex, columnIndex, otherColumnIndex + 1);
        }
        swapColumns(columnIndex, otherColumnIndex);
        return true;
    }

    private boolean swapInVarSubMatrixToNonZeroItem(int rowIndex, int columnIndex, int otherRowIndex, int otherColumnIndex) {
        if (otherRowIndex >= rows.length) {
            return false;
        }
        if (otherColumnIndex <= columnIndex) {
            return swapInVarSubMatrixToNonZeroItem(rowIndex, columnIndex,
                    otherRowIndex + 1, variableColumnIndexes.length - 1);
        }
        if (getItem(otherRowIndex, otherColumnIndex) == ComplexNumber.ZERO) {
            return swapInVarSubMatrixToNonZeroItem(rowIndex, columnIndex,
                    otherRowIndex, otherColumnIndex - 1);
        }
        swapRows(rowIndex, otherRowIndex);
        swapColumns(columnIndex, otherColumnIndex);
        return true;
    }

    private void swapRows(int rowIndex, int otherRowIndex) {
        Row temp = rows[rowIndex];
        rows[rowIndex] = rows[otherRowIndex];
        rows[otherRowIndex] = temp;
        System.out.println("Rows manipulation:");
        System.out.printf("R%d <-> R%d\n", rowIndex + 1, otherRowIndex + 1);
    }

    private void swapColumns(int columnIndex, int otherColumnIndex) {
        for (Row row : rows) {
            row.swapItems(columnIndex, otherColumnIndex);
        }
        int temp = variableColumnIndexes[columnIndex];
        variableColumnIndexes[columnIndex] = variableColumnIndexes[otherColumnIndex];
        variableColumnIndexes[otherColumnIndex] = temp;
        System.out.println("Columns manipulation:");
        System.out.printf("C%d <-> C%d\n", columnIndex + 1, otherColumnIndex + 1);
    }

    public Solution solve() {
        final Matrix refMatrix = Matrix.rowEchelonForm(this);
        int significantEquations = 0;
        for (Row row : refMatrix.rows) {
            final int indexOfLeadingEntry = row.indexOfLeadingEntry();
            if (indexOfLeadingEntry < 0) {
                continue;
            }
            if (indexOfLeadingEntry == row.length() - 1) {
                return Solution.NONE;
            }
            significantEquations++;
        }
        if (significantEquations < refMatrix.variableColumnIndexes.length) {
            return Solution.MANY;
        }
        final Matrix rrefMatrix = Matrix.reducedRowEchelonForm(refMatrix);
        final ComplexNumber[] answers = new ComplexNumber[variableColumnIndexes.length];
        for (int i = 0; i < rrefMatrix.variableColumnIndexes.length; i++) {
            final Row row = rrefMatrix.rows[i];
            answers[rrefMatrix.variableColumnIndexes[i]] = row.getItem(row.length() - 1);
        }
        return Solution.ONE.setAnswers(answers);
    }

}
