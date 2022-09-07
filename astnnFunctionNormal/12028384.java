class BackupThread extends Thread {
    public void testEncodeDecodePage() throws IOException {
        URL url;
        URLConnection connection;
        InputStream in;
        ByteArrayOutputStream out;
        byte[] bytes;
        byte[] result;
        int c;
        url = new URL("http://sourceforge.net/projects/htmlparser");
        connection = url.openConnection();
        in = connection.getInputStream();
        out = new ByteArrayOutputStream();
        while (-1 != (c = in.read())) out.write(c);
        in.close();
        out.close();
        bytes = out.toByteArray();
        result = encodedecode(bytes);
        check(bytes, result);
    }
}
