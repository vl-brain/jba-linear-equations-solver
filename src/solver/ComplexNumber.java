package solver;

import java.util.Objects;

public class ComplexNumber {
    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);
    private final double real;
    private final double img;

    private ComplexNumber(double real, double img) {
        this.real = real;
        this.img = img;
    }

    public static ComplexNumber parse(String number) {
        if (!number.contains("i")) {
            return of(Double.parseDouble(number), 0);
        }
        number = number.substring(0, number.length() - 1);
        int imgPartSignIndex = Math.max(number.lastIndexOf('-'), number.lastIndexOf('+'));
        if (imgPartSignIndex <= 0) {
            return of(0, parseDouble(number));
        }
        return of(parseDouble(number.substring(0, imgPartSignIndex)),
                parseDouble(number.substring(imgPartSignIndex)));
    }

    private static double parseDouble(String number) {
        return number.isEmpty() || "+".equals(number) ? 1 :
                ("-".equals(number) ? -1 : Double.parseDouble(number));
    }

    public static ComplexNumber of(double real, double img) {
        return real == 0 && img == 0 ? ZERO :
                (real == 1 && img == 0 ? ONE : new ComplexNumber(real, img));
    }

    public ComplexNumber add(ComplexNumber other) {
        return other.isZero() ? this : of(this.real + other.real, this.img + other.img);
    }

    public ComplexNumber multiply(double k) {
        return k == 1 ? this :
                (k == -1 ? this.negate() : of(real * k, img * k));
    }

    public ComplexNumber multiply(ComplexNumber other) {
        if (isZero() || other.isZero()) {
            return ZERO;
        }
        if (isRealNumber()) return other.multiply(real);
        if (other.isRealNumber()) return multiply(other.real);
        return of(this.real * other.real - this.img * other.img,
                this.real * other.img + other.real * this.img);
    }

    public ComplexNumber divide(ComplexNumber other) {
        return multiply(other.conjugate()).multiply(1 / (other.real * other.real + other.img * other.img));
    }

    public ComplexNumber negate() {
        return of(-real, -img);
    }

    private ComplexNumber conjugate() {
        return of(real, -img);
    }

    public boolean isRealNumber() {
        return img == 0;
    }

    public boolean isZero() {
        return this == ZERO;
    }

    public boolean isOne() {
        return this == ONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(that.real, real) == 0 &&
                Double.compare(that.img, img) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, img);
    }

    @Override
    public String toString() {
        if (isRealNumber()) {
            return Double.toString(real);
        }
        final StringBuilder builder = new StringBuilder();
        if (real != 0) {
            builder.append(real);
        }
        if (Math.abs(img) == 1) {
            if (img > 0 && builder.length() == 0) {
                builder.append("i");
            } else {
                builder.append(img > 0 ? "+i" : "-i");
            }
        } else {
            if (img > 0 && builder.length() > 0) {
                builder.append('+');
            }
            builder.append(img);
            builder.append('i');
        }
        return builder.toString();
    }

    public double getReal() {
        return real;
    }
}
