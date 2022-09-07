class BackupThread extends Thread {
    private static byte[] computeNodeAddress() {
        byte[] address = new byte[6];
        int thread = Thread.currentThread().hashCode();
        long time = System.currentTimeMillis();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);
        try {
            if (internetAddress != null) {
                out.write(internetAddress);
            }
            out.write(thread);
            out.writeLong(time);
            out.close();
        } catch (IOException exc) {
        }
        byte[] rand = byteOut.toByteArray();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception exc) {
        }
        md5.update(rand);
        byte[] temp = md5.digest();
        for (int i = 0; i < 6; i++) {
            address[i] = temp[i + 5];
        }
        address[0] = (byte) (address[0] | (byte) 0x80);
        return address;
    }
}
