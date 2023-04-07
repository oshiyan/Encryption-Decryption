import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String mode = "enc";
        String data = "";
        int key = 0;
        String pathToInputFile = "";
        String message = "";
        String pathToOutputFile = "";
        String alg = "unicode";

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].contains("-mode")) {
                mode = args[i + 1];
            } else if (args[i].contains("-key")) {
                try {
                    key = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    key = 0;
                }
            } else if (args[i].contains("-data")) {
                data = args[i + 1].contains("-") ? " " : args[i + 1];
            } else if (args[i].contains("-in")) {
                pathToInputFile = args[i + 1];
            } else if (args[i].contains("-out")) {
                pathToOutputFile = args[i + 1];
            } else if (args[i].contains("-alg")) {
                alg = args[i + 1];
            }
        }

        if (data.length() > 0) {
            message = data;
        } else if (pathToInputFile.length() > 0) {
            File file = new File(pathToInputFile);

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    message = message.concat(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("No file found Error: " + pathToInputFile);
                return;
            }
        }

        char[] dataArr = message.toCharArray();

        if (pathToOutputFile.length() > 0) {
            File file = new File(pathToOutputFile);
            FileWriter writer = new FileWriter(file); // appends text to the file

            writer.write(encryption(mode, key, dataArr, alg));
            writer.close();
        } else {
            System.out.println(encryption(mode, key, dataArr, alg));
        }
    }
    public static String encryption(String mode, int key, char[] data, String alg) {
        if (alg.equals("unicode")) {
            return unicodeEncryption(mode, key, data);
        } else {
            return shiftEncryption(mode, key, data);
        }
    }

    public static String unicodeEncryption(String mode, int key, char[] data) {

        StringBuilder encrypt = new StringBuilder(new String());
        for (int i = 0; i < data.length; i++) {
            int index = mode.equals("dec") ? data[i] - key : data[i] + key;
            encrypt.append((char) index);
        }
        return encrypt.toString();
    }


    public static String shiftEncryption(String mode, int key, char[] values) {
        if (mode.equals("enc")) {
            for (int i = 0; i < values.length; ++i) {
                if (values[i] >= 'a' && values[i] <= 'z') {
                    values[i] += key;
                    if (values[i] > 'z') {
                        values[i] -= 26;
                    }
                }
                if (Character.isUpperCase(values[i])) {
                    if (values[i] >= 'A' && values[i] <= 'Z') {
                        values[i] += key;
                    }
                    if (values[i] >= 'Z') {
                        values[i] -= 26;
                    }
                }
            }
        } else {
            for (int i = 0; i < values.length; ++i) {
                if (values[i] >= 'a' && values[i] <= 'z') {
                    values[i] -= key;
                    if (values[i] < 'a') {
                        values[i] += 26;
                    }
                }

                if (Character.isUpperCase(values[i])) {
                    if (values[i] >= 'A' && values[i] <= 'Z') {
                        values[i] -= key;
                        if (values[i] <= 'Z') {
                            values[i] += 26;
                        }
                    }
                }
            }
        }
        return String.valueOf(values);
    }
}
