class BackupThread extends Thread {
    public void testConstructor() throws Exception {
        Embargo embargo = new Embargo(new DateTime("2010-06-02T12:00:12"), "cuba");
        Serializer serializer = new Persister(new DateTimeMatcher());
        StringWriter writer = new StringWriter();
        serializer.write(embargo, writer);
        Embargo embargoout = serializer.read(Embargo.class, writer.toString());
        System.out.println(writer.toString());
    }
}
