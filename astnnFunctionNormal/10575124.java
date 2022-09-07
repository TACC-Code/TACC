class BackupThread extends Thread {
    public static String getInputType(URL url) {
        String inputType = null;
        try {
            URLConnection inConnection = url.openConnection();
            inputType = inConnection.getContentType();
            return inputType.trim();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        return inputType;
    }
}
