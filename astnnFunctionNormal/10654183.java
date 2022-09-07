class BackupThread extends Thread {
    @Test
    public void testReadReader() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final Transformer t = TransformerFactory.newInstance().newTransformer();
        final StringWriter sw = new StringWriter();
        t.transform(new StreamSource(url.openStream()), new StreamResult(sw));
        final ROMetaModel model = new ROMetaModelReader().read(new StringReader(sw.toString()));
        validateModel(model);
    }
}
