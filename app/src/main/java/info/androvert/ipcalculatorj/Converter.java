package info.androvert.ipcalculatorj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {
    public static String toBin(int inputValue) {
        return Integer.toBinaryString(inputValue);
    }

    public static List<String> toBin(String[] inputValue) {
        List<String> outputValue = new ArrayList<>();
        for (String it : inputValue) {
            outputValue.add(toBin(Integer.parseInt(it)));
        }
        return outputValue;
    }

    public static int toDec(String inputValue) {
        return Integer.parseInt(inputValue, 2);
    }

    public static String toDec(String inputValue, boolean isString) {
        return isString ? String.valueOf(toDec(inputValue)) : null;
    }

    public static List<String> toDec(List<String> inputValue) {
        List<String> outputValue = new ArrayList<>();
        for (String it : inputValue) {
            outputValue.add(toDec(it, true));
        }
        return outputValue;
    }
}
