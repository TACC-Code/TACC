class BackupThread extends Thread {
    public static void main(String[] arg) {
        try {
            MD5OutputStream out = new MD5OutputStream(new com.twmacinta.io.NullOutputStream());
            InputStream in = new BufferedInputStream(new FileInputStream(arg[0]));
            byte[] buf = new byte[65536];
            int num_read;
            long total_read = 0;
            while ((num_read = in.read(buf)) != -1) {
                total_read += num_read;
                out.write(buf, 0, num_read);
            }
            System.out.println(MD5.asHex(out.hash()) + "  " + arg[0]);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
