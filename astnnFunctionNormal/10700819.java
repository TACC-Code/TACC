class BackupThread extends Thread {
    public void writeURLContentToFile(String url, String fileName) {
        BufferedInputStream urlin = null;
        BufferedOutputStream fout = null;
        try {
            int bufSize = 8 * 1024;
            urlin = new BufferedInputStream(new URL(url).openConnection().getInputStream(), bufSize);
            fout = new BufferedOutputStream(new FileOutputStream(fileName), bufSize);
            int read = -1;
            byte[] buf = new byte[bufSize];
            while ((read = urlin.read(buf, 0, bufSize)) >= 0) {
                fout.write(buf, 0, read);
            }
            fout.flush();
        } catch (Exception ex) {
        } finally {
            if (urlin != null) {
                try {
                    urlin.close();
                } catch (IOException cioex) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException cioex) {
                }
            }
        }
    }
}
