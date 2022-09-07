class BackupThread extends Thread {
    public boolean authServer(String serverResponse) throws IOException {
        Hashtable map = tokenize(serverResponse);
        md5.update(ASCIIUtility.getBytes(":" + uri));
        md5.update(ASCIIUtility.getBytes(clientResponse + toHex(md5.digest())));
        String text = toHex(md5.digest());
        if (!text.equals((String) map.get("rspauth"))) {
            if (debugout != null) debugout.println("DEBUG DIGEST-MD5: " + "Expected => rspauth=" + text);
            return false;
        }
        return true;
    }
}
