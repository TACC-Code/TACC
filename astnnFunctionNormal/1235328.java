class BackupThread extends Thread {
    public void testReadWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = Integer.class.getResourceAsStream("Integer.class");
        int ch;
        while ((ch = in.read()) != -1) out.write(ch);
        byte[] origBytes = out.toByteArray();
        byte[] bytes = _bc.toByteArray();
        assertEquals(origBytes.length, bytes.length);
        for (int i = 0; i < origBytes.length; i++) assertEquals(origBytes[i], bytes[i]);
        BCClass bc2 = new Project().loadClass(_bc);
        bytes = bc2.toByteArray();
        assertEquals(origBytes.length, bytes.length);
        for (int i = 0; i < origBytes.length; i++) assertEquals(origBytes[i], bytes[i]);
    }
}
