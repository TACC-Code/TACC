class BackupThread extends Thread {
    public static ImageIcon getImageIconResource(Class _class, String resource) {
        byte[] buffer = null;
        try {
            InputStream ins = _class.getResourceAsStream(resource);
            if (ins == null) {
                System.err.println(_class.getName() + "/" + resource + " not found.");
                return null;
            }
            BufferedInputStream in = new BufferedInputStream(ins);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            buffer = new byte[1024];
            int n;
            while ((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
            in.close();
            out.flush();
            buffer = out.toByteArray();
            if (buffer.length == 0) {
                System.err.println(resource + " is zero-length");
                return null;
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            return null;
        }
        return new ImageIcon(buffer);
    }
}
