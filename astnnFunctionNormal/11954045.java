class BackupThread extends Thread {
    public static void fill(InputStream in, OutputStream out) throws IOException {
        for (int b = in.read(); b != EOF; b = in.read()) out.write(b);
        out.flush();
    }
}
