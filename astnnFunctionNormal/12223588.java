class BackupThread extends Thread {
    private String H(String param) {
        if (param != null) {
            Md5MessageDigest md5 = new Md5MessageDigest();
            byte[] d = md5.digest(param.getBytes());
            if (d != null) {
                return bufferToHex(d);
            }
        }
        return null;
    }
}
