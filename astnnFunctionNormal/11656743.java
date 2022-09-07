class BackupThread extends Thread {
    @Test
    public void testFindCollision() throws NoSuchAlgorithmException {
        byte[] dataToFind = new byte[] { 1, 2 };
        byte[] hash = MessageDigest.getInstance("MD5").digest(dataToFind);
        HashBreaker breaker = new HashBreaker(hash, new byte[] { 0, 0, 0 }, new byte[] { 127, 127, 127 });
        byte[] collision = breaker.findCollisionForHash();
        assertArrayEquals(dataToFind, collision);
    }
}
