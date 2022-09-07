class BackupThread extends Thread {
    @Test
    public void testRepoFileOutputStream() throws Exception {
        int fileSize = random.nextInt(50000);
        ContentName fileName = ContentName.fromNative(testHelper.getTestNamespace("testRepoFileOutputStream"), "outputFile.bin");
        RepositoryFileOutputStream rfos = new RepositoryFileOutputStream(fileName, writeLib);
        byte[] digest = writeRandomFile(fileSize, rfos);
        Log.info("Wrote file to repository: " + rfos.getBaseName());
        CCNFileInputStream fis = new CCNFileInputStream(fileName, readLib);
        CountAndDigest readDigest = readRandomFile(fis);
        Log.info("Read file from repository: " + fis.getBaseName() + " has header? " + fis.hasHeader());
        if (!fis.hasHeader()) {
            Log.info("No header yet, waiting..");
            fis.waitForHeader();
        }
        Assert.assertTrue(fis.hasHeader());
        Log.info("Read file size: " + readDigest.count() + " written size: " + fileSize + " header file size " + fis.header().length());
        Assert.assertEquals(readDigest.count(), fileSize);
        Assert.assertEquals(fileSize, fis.header().length());
        Log.info("Read digest: " + DataUtils.printBytes(readDigest.digest()) + " wrote digest: " + digest);
        Assert.assertArrayEquals(digest, readDigest.digest());
        CCNFileInputStream fis2 = new CCNFileInputStream(rfos.getBaseName(), readLib);
        CountAndDigest readDigest2 = readRandomFile(fis2);
        Log.info("Read file from repository again: " + fis2.getBaseName() + " has header? " + fis2.hasHeader());
        if (!fis2.hasHeader()) {
            Log.info("No header yet, waiting..");
            fis2.waitForHeader();
        }
        Assert.assertTrue(fis2.hasHeader());
        Log.info("Read file size: " + readDigest2.count() + " written size: " + fileSize + " header file size " + fis.header().length());
        Assert.assertEquals(readDigest2.count(), fileSize);
        Assert.assertEquals(fileSize, fis2.header().length());
        Log.info("Read digest: " + DataUtils.printBytes(readDigest2.digest()) + " wrote digest: " + digest);
        Assert.assertArrayEquals(digest, readDigest2.digest());
    }
}
