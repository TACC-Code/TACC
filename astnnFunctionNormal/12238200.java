class BackupThread extends Thread {
    @Test
    public void should_not_append() throws IOException {
        file = new File("res/notexist.file");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(file);
        byte[] b = { 97, 98, 99 };
        os.write(b);
        assertEquals(3, os.getChannel().size());
        os.close();
        FileOutputStream os2 = new FileOutputStream(file);
        os2.write(b);
        assertEquals(3, os2.getChannel().size());
        os2.close();
    }
}
