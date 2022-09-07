class BackupThread extends Thread {
    @Test
    public void testReadXMLStreamReader() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(url.openStream());
        final ROMetaModel model = new ROMetaModelReader().read(reader);
        validateModel(model);
    }
}
