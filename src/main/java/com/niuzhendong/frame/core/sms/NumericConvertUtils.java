package com.niuzhendong.frame.core.sms;

public class NumericConvertUtils {
    /**
     * 在进制表示中的字符集合
     */
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    /**
     * 将十进制的数字转换为指定进制的字符串
     * 
     * @param n 十进制的数字
     * @param base 指定的进制
     * @return
     */
    public static String decimalism2Other(long n, int base) {
        long num = 0;
        if (n < 0) {
            num = ((long) 2 * 0x7fffffff) + n + 2;
        } else {
            num = n;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((num / base) > 0) {
            buf[--charPos] = digits[(int) (num % base)];
            num /= base;
        }
        buf[--charPos] = digits[(int) (num % base)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     * 
     * @param str 其它进制的数字（字符串形式）
     * @param base 指定的进制
     * @return
     */
    public static long other2Decimalism(String str, int base) {
        char[] buf = new char[str.length()];
        str.getChars(0, str.length(), buf, 0);
        long num = 0;
        for (int i = 0; i < buf.length; i++) {
            for (int j = 0; j < digits.length; j++) {
                if (digits[j] == buf[i]) {
                    num += j * Math.pow(base, buf.length - i - 1);
                    break;
                }
            }
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(decimalism2Other(16857120L, 36));
        System.out.println(other2Decimalism("A1B1AJASWDE", 36));
    }

    /**
     * hex字符串转byte数组
     * 
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hex2ByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     * 
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 
     * @Title: hex2Binary
     * @Description: 十六进制转二进制
     */
    public static String hex2Binary(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String toBinary(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * 
     * @param str
     * @return
     */
    public static String string2Hexadecimal(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        // Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    public static int byteArray2Int(int[] b) {
        return b[0] & 0xFF | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
    }

    public static int[] int2ByteArray(int a) {
//        return new byte[] { (byte) (a & 0xFF),(byte) ((a >> 8) & 0xFF),(byte) ((a >> 16) & 0xFF),(byte) ((a >> 24) & 0xFF)};
//        return new byte[] { (byte) ((a >> 24) & 0xFF),(byte) ((a >> 16) & 0xFF),(byte) ((a >> 8) & 0xFF),(byte) (a & 0xFF)};
        int[] byteArray = new int[4];
        byteArray[0] = (a & 0xFF);
        byteArray[1] = (a >> 8  & 0xFF);
        byteArray[2] = (a >> 16 & 0xFF);
        byteArray[3] = (a >> 24 & 0xFF);
        return byteArray;
    }

}