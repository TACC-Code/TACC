class BackupThread extends Thread {
    public static long createHash(String methodDesc) throws Exception {
        long hash = 0;
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(512);
        MessageDigest messagedigest = MessageDigest.getInstance("SHA");
        DataOutputStream dataoutputstream = new DataOutputStream(new DigestOutputStream(bytearrayoutputstream, messagedigest));
        dataoutputstream.writeUTF(methodDesc);
        dataoutputstream.flush();
        byte abyte0[] = messagedigest.digest();
        for (int j = 0; j < Math.min(8, abyte0.length); j++) hash += (long) (abyte0[j] & 0xff) << j * 8;
        return hash;
    }
}
