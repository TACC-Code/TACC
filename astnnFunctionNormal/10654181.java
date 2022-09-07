class BackupThread extends Thread {
    @Test
    public void testReadFile() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        File file = File.createTempFile("ROMetaModelReaderTest", ".xml");
        file.deleteOnExit();
        try {
            copy(url.openStream(), new FileOutputStream(file));
            final ROMetaModel model = new ROMetaModelReader().read(file);
            validateModel(model);
        } finally {
            file.delete();
        }
    }
}
