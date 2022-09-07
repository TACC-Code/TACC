class BackupThread extends Thread {
    @Test
    public void testReadNode() throws Exception {
        final URL url = getClass().getResource("registryObjectModel.xml");
        Assert.assertNotNull(url);
        final DOMResult domResult = new DOMResult();
        final Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new StreamSource(url.openStream()), domResult);
        final ROMetaModel model = new ROMetaModelReader().read(domResult.getNode());
        validateModel(model);
    }
}
