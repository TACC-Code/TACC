class BackupThread extends Thread {
    @Test
    public void testReadInputSource() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final InputSource isource = new InputSource(url.openStream());
        isource.setSystemId(url.toExternalForm());
        final ROMetaModel model = new ROMetaModelReader().read(isource);
        validateModel(model);
    }
}
