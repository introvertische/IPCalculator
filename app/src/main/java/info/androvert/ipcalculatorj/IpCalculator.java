package info.androvert.ipcalculatorj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpCalculator {

    private final int LEN_ELEMENTS = 8;

    private List<String> binIpAddress;
    private List<String> binMask;
    private String binId;
    private List<String> binSubnetAddress;

    public String getBinIpAddress(boolean isBinary) {
        return toStr(binIpAddress, isBinary);
    }

    public String getBinMask(boolean isBinary) {
        return toStr(binMask, isBinary);
    }

    public String getBinId(boolean isBinary) {
        return isBinary ? binId : Converter.toDec(binId, true);
    }

    public String getBinSubnetAddress(boolean isBinary) {
        return toStr(binSubnetAddress, isBinary);
    }

    public IpCalculator(String ipAddress, String mask) {
        binIpAddress = ipValidation(ipAddress) ?
                checkForElementSize(Converter.toBin(ipAddress.split("\\."))) :
                checkForElementSize(Converter.toBin("0.0.0.0".split("\\.")));
        binMask = Converter.toBin(mask.split(" – ")[1].split("\\."));
        definingIdAndSubnetAddress();
    }

    private boolean ipValidation(String ip) {
        Pattern ipTemplate = Pattern.compile(
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        Matcher matcherIp = ipTemplate.matcher(ip);
        return matcherIp.matches();
    }

    private String repeatElements(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private String addElements(String element, String valueElement, int count, boolean addToEnd) {
        String repeatElement = repeatElements(count - element.length(), valueElement);
        return addToEnd ? element + repeatElement : repeatElement + element;
    }

    private List<String> checkForElementSize(List<String> value) {
        List<String> newValue = new ArrayList<>();
        int size = 8;
        for (String it : value) {
            String element = "";
            if (it.length() < size) {
                element = addElements(it, "0", LEN_ELEMENTS, false);
            } else {
                element = it;
            }
            newValue.add(element);
        }
        return newValue;
    }

    private void definingIdAndSubnetAddress() {
        StringBuilder binId = new StringBuilder();
        List<String> binSubnetAddress = new ArrayList<>();
        boolean flag = true;

        for (int i = 0; i < binIpAddress.size(); i++) {
            String ip = binIpAddress.get(i);
            String mask = binMask.get(i);
            if (Converter.toDec(mask) != 255 && flag) {
                flag = false;
                char[] chIp = ip.toCharArray();
                char[] chMask = mask.toCharArray();
                for (int j = 0; j < chMask.length; j++) {
                    if (chMask[0] == '0') {
                        binId.append("0");
                        break;
                    } else if (chMask[j] == '1') {
                        binId.append(chIp[j]);
                    } else {
                        break;
                    }
                }
                binSubnetAddress.add(addElements(binId.toString(), "0", LEN_ELEMENTS, true));
            } else if (!flag) {
                binSubnetAddress.add(repeatElements(8, "0"));
            } else {
                binSubnetAddress.add(ip);
            }
        }

        this.binId = binId.toString();
        this.binSubnetAddress = binSubnetAddress;
    }

    public String getIpClass() {
        int value = Converter.toDec(binIpAddress.get(0));
        if (value >= 0 && value < 127) {
            return "A";
        } else if (value >= 128 && value < 192) {
            return "B";
        } else if (value >= 192 && value < 224) {
            return "C";
        } else if (value >= 224 && value < 240) {
            return "D";
        } else if (value >= 240 && value < 248) {
            return "E";
        } else {
            return "Unable to determine the IP address class!";
        }
    }

    private int calculatingQuantity(char value) {
        int count = 0;
        for (int i = 0; i < binMask.size(); i++) {
            if (Converter.toDec(binMask.get(i)) != 255) {
                for (char c : binMask.get(i).toCharArray()) {
                    if (c == value)
                        count++;
                }
            }
        }
        return count;
    }

    public int determiningNumberOfNodes() {
        return (int) (Math.pow(2, calculatingQuantity('0')) - 2);
    }

    public int determiningNumberOfSubnet() {
        return (int) (Math.pow(2, calculatingQuantity('1')));
    }

    private List<String> calculateIpAddressValues(int key) {
        List<String> binIpValue = new ArrayList<>();
        boolean isFirstEntry = true;
        String value = "";
        String inverseValue = "";
        String id = binId;
        int elementSize = binMask.size() - 1;
        int numberOfValues = 8;

        switch (key) {
            case 0:
                value = "1";
                inverseValue = "0";
                break;
            case 1:
                value = "0";
                inverseValue = "1";
                break;
            case 2:
                value = "1";
                inverseValue = "1";
                break;
            default:
                value = "-1";
                inverseValue = "-1";
                break;
        }

        for (int i = 0; i < binMask.size(); i++) {
            String binOctet = "";
            if (Converter.toDec(binMask.get(i)) != 255) {
                if (isFirstEntry) {
                    if (i == elementSize)
                        binOctet = addElements(id, inverseValue ,numberOfValues - 1, true) + value;
                    else
                        binOctet = addElements(id, inverseValue, numberOfValues, true);
                    isFirstEntry = false;
                } else {
                    if (i == elementSize)
                        binOctet = repeatElements(numberOfValues - 1, inverseValue) + value;
                    else
                        binOctet = repeatElements(numberOfValues, inverseValue);
                }
            } else {
                binOctet = binIpAddress.get(i);
            }
            binIpValue.add(binOctet);
        }

        return binIpValue;
    }

    private String toStr(List<String> value, boolean isBinary) {
        return isBinary ? String.join(".", value) : String.join(".", Converter.toDec(value));
    }

    private String calculateIpAddress(boolean isBinary, int key) {
        List<String> binIpValue = calculateIpAddressValues(key);
        return toStr(binIpValue, isBinary);
    }

    public String calculateFirstIpAddress(boolean isBinary) {
        return calculateIpAddress(isBinary, 0);
    }

    public String calculateLastIpAddress(boolean isBinary) {
        return calculateIpAddress(isBinary, 1);
    }

    public String calculateBroadcastAddress(boolean isBinary) {
        return calculateIpAddress(isBinary, 2);
    }
}
