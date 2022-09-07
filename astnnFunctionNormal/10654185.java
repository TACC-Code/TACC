class BackupThread extends Thread {
    @Test
    public void testReadSource() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        final ROMetaModel model = new ROMetaModelReader().read(new StreamSource(url.openStream()));
        validateModel(model);
    }
}
