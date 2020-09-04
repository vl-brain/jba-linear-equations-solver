package solver;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        File inFile = getOption("-in", args)
                .map(File::new)
                .orElseThrow();
        File outFile = getOption("-out", args)
                .map(File::new)
                .orElseThrow();
        final Matrix matrix = readMatrix(inFile);
        System.out.println("Start solving the equation.");
        final Solution solution = matrix.solve();
        writeSolution(outFile, solution);
        System.out.println("Saved to file " + outFile.getName());
    }

    private static Optional<String> getOption(String option, String[] args) {
        boolean foundOption = false;
        for (String arg : args) {
            if (foundOption) {
                return Optional.of(arg);
            }
            if (option.equals(arg)) {
                foundOption = true;
            }
        }
        return Optional.empty();
    }

    private static Matrix readMatrix(File inputFile) {
        try (Scanner scanner = new Scanner(inputFile)) {
            final int variableCount = scanner.nextInt();
            final int rowsCount = scanner.nextInt();
            return new Matrix(Stream.generate(() ->
                    new Row(Stream.generate(scanner::next)
                            .limit(variableCount + 1)
                            .map(ComplexNumber::parse)
                            .toArray(ComplexNumber[]::new)))
                    .limit(rowsCount)
                    .toArray(Row[]::new));
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read input file", e);
        }
    }

    private static void writeSolution(File outputFile, Solution solution) {
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("Can't create output file", e);
            }
        }
        try (PrintStream printStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
            switch (solution) {
                case NONE:
                case MANY:
                    printStream.println(solution);
                    System.out.println(solution);
                    break;
                case ONE:
                    for (ComplexNumber answer : solution.getAnswers()) {
                        if (answer.isRealNumber()) {
                            printStream.println(answer.getReal());
                        } else {
                            printStream.println(answer);
                        }
                    }
                    System.out.println(solution);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't write solution to output file", e);
        }
    }
}
