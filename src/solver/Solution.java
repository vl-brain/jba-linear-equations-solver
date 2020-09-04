package solver;

public enum Solution {
    NONE {
        @Override
        public ComplexNumber[] getAnswers() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public Solution setAnswers(ComplexNumber[] answers) {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public String toString() {
            return "No solutions";
        }
    },
    MANY {
        @Override
        public ComplexNumber[] getAnswers() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public Solution setAnswers(ComplexNumber[] answers) {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public String toString() {
            return "Infinitely many solutions";
        }
    },
    ONE {
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("The solution is: (");
            boolean first = true;
            for (ComplexNumber answer : getAnswers()) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append(answer);
                first = false;
            }
            builder.append(")");
            return builder.toString();
        }
    };

    private static final RuntimeException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException();
    private ComplexNumber[] answers;

    public ComplexNumber[] getAnswers() {
        return answers;
    }

    public Solution setAnswers(ComplexNumber[] answers) {
        this.answers = answers;
        return this;
    }
}
