class BackupThread extends Thread {
    public static void getFtpFile(String url, String outfile) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream());
            out = new BufferedOutputStream(new FileOutputStream(outfile), 1024);
            byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                out.write(data, 0, x);
            }
        } catch (Exception e) {
            throw new CException(e);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (Exception e) {
            }
        }
    }
}
