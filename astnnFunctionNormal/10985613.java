class BackupThread extends Thread {
    @TestTargets({ @TestTargetNew(level = TestLevel.ADDITIONAL, method = "update", args = { byte.class }), @TestTargetNew(level = TestLevel.ADDITIONAL, method = "digest", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "method", args = {  }) })
    public void testMessageDigest2() {
        int val;
        try {
            while ((val = sourceData.read()) != -1) {
                digest.update((byte) val);
            }
        } catch (IOException e) {
            fail("failed to read digest data");
        }
        byte[] computedDigest = digest.digest();
        assertNotNull("computed digest is is null", computedDigest);
        assertEquals("digest length mismatch", checkDigest.length, computedDigest.length);
        for (int i = 0; i < checkDigest.length; i++) {
            assertEquals("byte " + i + " of computed and check digest differ", checkDigest[i], computedDigest[i]);
        }
    }
}
