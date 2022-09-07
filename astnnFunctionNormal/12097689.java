class BackupThread extends Thread {
    public final void testOn() throws IOException {
        for (int k = 0; k < algorithmName.length; k++) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(MY_MESSAGE_LEN);
                MessageDigest md = MessageDigest.getInstance(algorithmName[k]);
                DigestOutputStream dos = new DigestOutputStream(bos, md);
                dos.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN - 1; i++) {
                    dos.write(myMessage[i]);
                }
                dos.on(true);
                dos.write(myMessage[MY_MESSAGE_LEN - 1]);
                byte[] digest = dos.getMessageDigest().digest();
                assertFalse(Arrays.equals(digest, MDGoldenData.getDigest(algorithmName[k])) || Arrays.equals(digest, MDGoldenData.getDigest(algorithmName[k] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
}
