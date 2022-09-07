class BackupThread extends Thread {
    public final void testWritebyteArrayintint01() throws IOException {
        for (int k = 0; k < algorithmName.length; k++) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(MY_MESSAGE_LEN);
                MessageDigest md = MessageDigest.getInstance(algorithmName[k]);
                DigestOutputStream dos = new DigestOutputStream(bos, md);
                dos.write(myMessage, 0, MY_MESSAGE_LEN);
                assertTrue("write", Arrays.equals(myMessage, bos.toByteArray()));
                assertTrue("update", Arrays.equals(dos.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[k])));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
}
