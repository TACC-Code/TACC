class BackupThread extends Thread {
    private String makeMD5(String text) {
        java.security.MessageDigest md = null;
        byte[] encryptMsg = null;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
            encryptMsg = md.digest(text.getBytes());
        } catch (java.security.NoSuchAlgorithmException e) {
            org.emftext.language.pl0.resource.pl0.ui.Pl0UIPlugin.logError("NoSuchAlgorithmException while creating MD5 checksum.", e);
        }
        String swap = "";
        String byteStr = "";
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i <= encryptMsg.length - 1; i++) {
            byteStr = Integer.toHexString(encryptMsg[i]);
            switch(byteStr.length()) {
                case 1:
                    swap = "0" + Integer.toHexString(encryptMsg[i]);
                    break;
                case 2:
                    swap = Integer.toHexString(encryptMsg[i]);
                    break;
                case 8:
                    swap = (Integer.toHexString(encryptMsg[i])).substring(6, 8);
                    break;
            }
            strBuf.append(swap);
        }
        return strBuf.toString();
    }
}
