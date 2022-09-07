class BackupThread extends Thread {
    public String getSHA(String message) throws NoSuchAlgorithmException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageDigest sha = MessageDigest.getInstance("SHA");
        DigestOutputStream dos = new DigestOutputStream(baos, sha);
        PrintWriter pw = new PrintWriter(dos);
        pw.print(message);
        pw.flush();
        byte md[] = dos.getMessageDigest().digest();
        StringBuffer sb = new StringBuffer();
        for (int offset = 0; offset < md.length; offset++) {
            byte b = md[offset];
            if ((b & 0xF0) == 0) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b & 0xFF));
        }
        return sb.toString();
    }
}
