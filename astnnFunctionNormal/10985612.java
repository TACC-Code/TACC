class BackupThread extends Thread {
    @TestTargets({ @TestTargetNew(level = TestLevel.ADDITIONAL, method = "update", args = { byte[].class, int.class, int.class }), @TestTargetNew(level = TestLevel.ADDITIONAL, method = "digest", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "method", args = {  }) })
    public void testMessageDigest1() {
        byte[] buf = new byte[128];
        int read = 0;
        try {
            while ((read = sourceData.read(buf)) != -1) {
                digest.update(buf, 0, read);
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
