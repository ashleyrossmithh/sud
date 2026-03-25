package sud;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class CurrentContext {
    public static String HOST = "192.168.0.8";
    public static Integer PORT = 8080;
    public static String qrFilePathTmp = "";

    static  {
        try {
            String tomcatPath = System.getProperty("catalina.home");
            File iniFile = new File(tomcatPath, "qr_app.properties");
            Properties properties = new Properties();
            properties.load(new FileReader(iniFile.getAbsolutePath()));
            init(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init(Properties properties) {
        String hostValue = (String) properties.get("hostForClient");
        if (hostValue != null && !hostValue.trim().isEmpty()) {
            HOST = hostValue;
        }
        Object portObj = properties.get("portForClient");
        if (portObj != null && !portObj.toString().trim().isEmpty()) {
            PORT = Integer.parseInt(portObj.toString());
        }
    }

    public static String buildUrl(String contextPath, String uuid) {
        return "https://" + HOST + ":" + PORT + contextPath + "/login?uuid=" + uuid;
    }
}
