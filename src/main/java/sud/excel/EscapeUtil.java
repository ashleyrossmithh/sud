package sud.excel;

//import org.apache.commons.text.StringEscapeUtils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class EscapeUtil {
    private EscapeUtil() {

    }

    public static String escape(String str) {
        if (str == null) {
            return null;
        }
        str = StringEscapeUtils.escapeXml11(str);
        str = escape1_31(str);
        return str;
    }

    private static String escape1_31(String str) {
        StringWriter stringWriter = createStringWriter(str);

        try {
            escape1_31(stringWriter, str);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }

        return stringWriter.toString();
    }

    private static void escape1_31(Writer writer, String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            if (c < 32) {
                writer.write("?");
            } else {
                writer.write(c);
            }
        }
    }

    private static StringWriter createStringWriter(String str) {
        return new StringWriter((int) ((double) str.length() + (double) str.length() * 0.1D));
    }
}
