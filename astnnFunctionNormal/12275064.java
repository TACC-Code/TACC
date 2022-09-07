class BackupThread extends Thread {
    public void testPathWithNamespace() throws Exception {
        Persister persister = new Persister();
        PathWithNamespaceExample example = new PathWithNamespaceExample();
        StringWriter writer = new StringWriter();
        example.setName("Tim");
        example.setValue("Value");
        persister.write(example, writer);
        PathWithNamespaceExample recovered = persister.read(PathWithNamespaceExample.class, writer.toString());
        assertEquals(recovered.name, example.name);
        assertEquals(recovered.value, example.value);
        validate(example, persister);
    }
}
