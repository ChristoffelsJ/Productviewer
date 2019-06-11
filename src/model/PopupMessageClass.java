package model;

public class PopupMessageClass {
    /**the errormessage we give with the setErrormessage method
     *
     */
    private static String Errormessage;

    /**
     *
     * @return the errormessage we set with the setErrormessage method
     */
    public static String getErrormessage() {
        return Errormessage;
    }

    /**set the errormessage for for the error popup
     *
     * @param errormessage
     */
    public static void setErrormessage(String errormessage) {
       Errormessage = errormessage;
    }


}
