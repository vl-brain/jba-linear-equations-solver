package solver;

import java.util.Arrays;

public class Row {
    private final ComplexNumber[] items;

    public Row(ComplexNumber[] items) {
        this.items = items;
    }

    public Row copy() {
        return new Row(Arrays.copyOf(items, items.length));
    }

    public int variableLength() {
        return items.length - 1;
    }

    public int length() {
        return items.length;
    }

    public int indexOfLeadingEntry() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != ComplexNumber.ZERO) {
                return i;
            }
        }
        return -1;
    }

    public ComplexNumber getItem(int index) {
        return items[index];
    }

    public void swapItems(int columnIndex, int otherColumnIndex) {
        ComplexNumber temp = items[columnIndex];
        items[columnIndex] = getItem(otherColumnIndex);
        items[otherColumnIndex] = temp;
    }

    public boolean add(Row other) {
        for (int i = 0; i < items.length && i < other.items.length; i++) {
            items[i] = items[i].add(other.items[i]);
        }
        return true;
    }

    public boolean multiply(double value) {
        boolean modified = false;
        for (int i = 0; i < items.length; i++) {
            final ComplexNumber newNumber = items[i].multiply(value);
            if (!items[i].equals(newNumber)) {
                items[i] = newNumber;
                modified = true;
            }
        }
        return modified;
    }

    public boolean multiply(ComplexNumber number) {
        if (number == ComplexNumber.ONE) {
            return false;
        } else if (ComplexNumber.ONE.negate().equals(number)) {
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].negate();
            }
        } else {
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].multiply(number);
            }
        }
        return true;
    }

    public boolean divide(ComplexNumber number) {
        if (number == ComplexNumber.ONE) {
            return false;
        } else if (ComplexNumber.ONE.negate().equals(number)) {
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].negate();
            }
        } else {
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].divide(number);
            }
        }
        return true;
    }
}
