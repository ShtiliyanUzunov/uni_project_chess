package chess.services;

public class Logging {

    public static void log(String str) {
        if (GlobalContext.getConfiguration().isEnableLogging()) {
            System.out.println(str);
        }
    }

}
