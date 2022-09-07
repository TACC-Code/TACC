class BackupThread extends Thread {
    @Test
    public void testReadInputStream() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final ROMetaModel model = new ROMetaModelReader().read(url.openStream());
        validateModel(model);
    }
}
