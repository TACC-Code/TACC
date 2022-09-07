class BackupThread extends Thread {
    public final void testWriteint02() throws IOException {
        for (int k = 0; k < algorithmName.length; k++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[k]);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(MY_MESSAGE_LEN);
                DigestOutputStream dos = new DigestOutputStream(bos, md);
                dos.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN; i++) {
                    dos.write(myMessage[i]);
                }
                assertTrue("write", Arrays.equals(MDGoldenData.getMessage(), bos.toByteArray()));
                assertTrue("update", Arrays.equals(dos.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[k] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
}
