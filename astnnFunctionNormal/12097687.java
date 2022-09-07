class BackupThread extends Thread {
    public final void testWritebyteArrayintint04() throws NoSuchAlgorithmException, IOException {
        assertEquals(0, MY_MESSAGE_LEN % CHUNK_SIZE);
        for (int k = 0; k < algorithmName.length; k++) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(MY_MESSAGE_LEN);
                MessageDigest md = MessageDigest.getInstance(algorithmName[k]);
                DigestOutputStream dos = new DigestOutputStream(bos, md);
                dos.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN / CHUNK_SIZE; i++) {
                    dos.write(myMessage, i * CHUNK_SIZE, CHUNK_SIZE);
                }
                assertTrue("write", Arrays.equals(myMessage, bos.toByteArray()));
                assertTrue("update", Arrays.equals(dos.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[k] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
}
