class BackupThread extends Thread {
    @Test
    public void testDigest() {
        try {
            ContentObject coempty = new ContentObject(name, auth, new byte[0], pair.getPrivate());
            System.out.println("Created object with content of length " + coempty.contentLength() + " digest: " + DataUtils.printHexBytes(coempty.digest()));
            ContentObject coempty2 = new ContentObject(name, auth, null, pair.getPrivate());
            System.out.println("Created another object with content of length " + coempty2.contentLength() + " digest: " + DataUtils.printHexBytes(coempty2.digest()));
            Assert.assertNotNull(coempty.digest());
            Assert.assertArrayEquals(coempty.digest(), coempty2.digest());
        } catch (Exception e) {
            Assert.fail("Exception in testEncDec: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
