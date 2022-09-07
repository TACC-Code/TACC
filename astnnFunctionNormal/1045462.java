class BackupThread extends Thread {
    boolean copyMain(InputStream in) throws IOException {
        try {
            File target = new File(Main.GetTempDir(), "main.jnlp");
            FileOutputStream out = new FileOutputStream(target);
            try {
                byte[] iob = new byte[0x200];
                int read;
                while (0 < (read = in.read(iob, 0, 0x200))) {
                    out.write(iob, 0, read);
                }
                return true;
            } catch (IOException exc) {
                exc.printStackTrace();
                target.delete();
                return false;
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
