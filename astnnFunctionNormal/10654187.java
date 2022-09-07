class BackupThread extends Thread {
    @Test
    public void testReadXMLEventReader() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(url.openStream());
        final ROMetaModel model = new ROMetaModelReader().read(reader);
        validateModel(model);
    }
}
