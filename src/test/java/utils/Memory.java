package utils;

/** Simple in-memory stash for data we pass across steps. */
public class Memory {
    private static String lastUsername;
    private static String lastPassword;
    private static Integer lastCartTotal;

    public static void saveCreds(String u, String p) { lastUsername = u; lastPassword = p; }
    public static String getUser() { return lastUsername; }
    public static String getPass() { return lastPassword; }

    public static void setCartTotal(Integer t) { lastCartTotal = t; }
    public static Integer getCartTotal() { return lastCartTotal; }
}
