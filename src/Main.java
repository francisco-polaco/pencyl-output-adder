import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String RESULT_PATHNAME = "result.dat";
    private static final String LINE_START = "   ";
    private static final int COLUMN_TO_SUM = 3;

    public static void main(String[] args) throws Exception {
        // write your code here
        File f1 = new File("dose-charge-01.dat");
        if (!f1.setWritable(false)) {
            printErrorReadOnly(f1);
            return;
        }
        File f2 = new File("dose-charge-02.dat");
        if (!f2.setWritable(false)) {
            printErrorReadOnly(f2);
            return;
        }

        ArrayList<String> linesFile1 = getLines(f1);
        ArrayList<String> linesFile2 = getLines(f2);
        ArrayList<Double[]> numbersFile1 = getNumbers(linesFile1);
        ArrayList<Double[]> numbersFile2 = getNumbers(linesFile2);
        // 2 - 1
        ArrayList<Double[]> numbersFileResult = subtract(numbersFile2, numbersFile1);

        ArrayList<String> linesFileResult = prepareResult(numbersFileResult, linesFile1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULT_PATHNAME))) {
            for (String line : linesFileResult) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static ArrayList<String> prepareResult(ArrayList<Double[]> numbersFileResult, ArrayList<String> linesFile1) {
        ArrayList<String> toRet = new ArrayList<>();
        // Setup header
        for (String line : linesFile1) {
            if (line.equals("")) break;
            else toRet.add(line);
        }
        toRet.add("");
        for (int i = 0; i < numbersFileResult.size(); i++) {
            Double[] doubles = numbersFileResult.get(i);
            String line = LINE_START;
            for (int j = 0; j < doubles.length; j++) {

                if (j == 0)
                    line += String.format("%.6E", doubles[j]);
                else if (doubles[j] < 0.0)
                    line += " " + String.format("%.6E", doubles[j]);
                else {
                    line += "  " + String.format("%.6E", doubles[j]);
                }
            }
            toRet.add(line);
            if ((i + 1) % 100 == 0 && i > 0) toRet.add(LINE_START); // notation

        }
        return toRet;

    }

    private static void printErrorReadOnly(File f) throws IOException {
        System.err.println("Could not set read only to file " + f.getCanonicalPath());
    }

    private static ArrayList<String> getLines(File file) throws IOException {
        ArrayList<String> linesFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linesFile.add(line);
            }
        }
        return linesFile;
    }

    private static ArrayList<Double[]> getNumbers(ArrayList<String> file) throws IOException {
        ArrayList<String[]> linesNumbers = new ArrayList<>();
        for (String line : file) {
            if (!(line.startsWith(" #") || line.equals(LINE_START) || line.equals(""))) {
                String[] parsedLine = line.split(" ");
                String[] yey = arrayWithoutEmptyString(parsedLine);
                linesNumbers.add(yey);
            }
        }
        return stringToDouble(linesNumbers);
    }

    private static ArrayList<Double[]> stringToDouble(ArrayList<String[]> convert) {
        ArrayList<Double[]> toRet = new ArrayList<>();
        for (String[] array : convert) {
            Double[] line = new Double[array.length];
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                line[i] = Double.valueOf(s);
            }
            toRet.add(line);
        }
        return toRet;
    }

    private static String[] arrayWithoutEmptyString(String[] array) {
        List<String> list = Arrays.asList(array);
        ArrayList<String> toArray = new ArrayList<>();
        for (String el : list) {
            if (!el.equals("")) {
                toArray.add(el);
            }
        }
        String[] toRet = new String[toArray.size()];
        return toArray.toArray(toRet);
    }

    private static ArrayList<Double[]> subtract(ArrayList<Double[]> f1, ArrayList<Double[]> f2) {
        if (f1.size() != f2.size()) throw new RuntimeException("Different Size");
        ArrayList<Double[]> toRet = new ArrayList<>();
        for (int i = 0; i < f1.size(); i++) {
            Double[] doubles1 = f1.get(i);
            Double[] doubles2 = f2.get(i);
            Double[] doubles3 = new Double[doubles1.length];
            for (int j = 0; j < doubles1.length; j++) {
                if (j == COLUMN_TO_SUM - 1) {
                    doubles3[j] = doubles1[j] - doubles2[j];
                } else doubles3[j] = doubles1[j];
            }
            toRet.add(doubles3);
        }
        return toRet;
    }
}
