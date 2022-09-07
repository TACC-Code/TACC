class BackupThread extends Thread {
    public static String plainStringToSHA1(String input) {
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        md.reset();
        byteHash = md.digest(input.getBytes());
        for (int i = 0; i < byteHash.length; i++) {
            resultString.append(Integer.toHexString(0xFF & byteHash[i]));
        }
        return (resultString.toString());
    }
}
