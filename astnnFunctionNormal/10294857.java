class BackupThread extends Thread {
    @Test
    public void testVSegmentsWriteUpdate() throws IOException {
        File tmpFile = new File("testVSegmentsWriteUpdate.tmp");
        tmpFile.deleteOnExit();
        if (tmpFile.exists()) {
            assertTrue("delete file failed: " + tmpFile.getAbsolutePath(), tmpFile.delete());
        }
        System.out.println("tmpFile: " + tmpFile.getAbsolutePath());
        VarSegments segment = new VarSegments(tmpFile);
        byte[] b0 = new byte[] { 2, 2, 23 };
        segment.write(0, b0);
        byte[] b1 = new byte[] { 2, 1, 24 };
        segment.write(1, b1);
        byte[] b3 = new byte[] { 2, 43, 1 };
        segment.write(3, b3);
        byte[] b2 = new byte[] { 1, 3, 3 };
        segment.write(2, b2);
        segment.write(2, b3);
        byte[] b4 = new byte[] { 6, 1 };
        segment.write(4, b4);
        byte[] b5 = new byte[] { 9, 3, 4 };
        segment.write(5, b5);
        segment.write(5, b4);
        segment.flush(true);
        VarSegments readSegment = new VarSegments(tmpFile);
        ArrayAssert.assertEquals(b0, readSegment.read(0));
        ArrayAssert.assertEquals(b0, readSegment.read(0));
        ArrayAssert.assertEquals(b1, readSegment.read(1));
        ArrayAssert.assertEquals(b3, readSegment.read(3));
        ArrayAssert.assertEquals(b3, readSegment.read(2));
        ArrayAssert.assertEquals(b4, readSegment.read(4));
        readSegment.write(4, b3);
        ArrayAssert.assertEquals(b3, readSegment.read(4));
        ArrayAssert.assertEquals(b4, readSegment.read(5));
        tmpFile.delete();
    }
}
