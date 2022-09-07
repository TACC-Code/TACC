class BackupThread extends Thread {
    private static String digest(byte[] data, MessageDigest digest) {
        byte[] enc = digest.digest(data);
        StringBuffer sb = new StringBuffer(enc.length * 2);
        for (byte b : enc) {
            String t = Integer.toString(b & 0xff, 16);
            if (t.length() == 1) sb.append('0');
            sb.append(t);
        }
        return sb.toString();
    }
}
