class BackupThread extends Thread {
    public void testBasicWorldSerialization() throws JAXBException {
        World world = createStructure();
        JAXBContext ctx = GameObjectManager.createJAXBContext();
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        m.marshal(world, writer);
        logDebug(writer.toString());
        StringReader reader = new StringReader(writer.toString());
        Unmarshaller um = ctx.createUnmarshaller();
        Object o = um.unmarshal(reader);
        verifySimilarity(o, world);
    }
}
