package util;

public class StringUtil {
    private StringUtil(){}

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
