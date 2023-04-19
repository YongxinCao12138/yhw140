package util;

public class StringUtil {
    private StringUtil(){}

    /**
     * String == null or Empty string
     * @param cs string
     * @return true
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
