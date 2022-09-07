class BackupThread extends Thread {
    public void startDownload(String source, String path) {
        File f = new File(path);
        if (!f.exists()) {
            File f2 = new File(f.getParent());
            f2.mkdir();
        }
        OutputStream out = null;
        URLConnection urlc = null;
        InputStream in = null;
        try {
            URL url = new URL(source);
            out = new BufferedOutputStream(new FileOutputStream(path));
            urlc = url.openConnection();
            max = urlc.getContentLength();
            in = urlc.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                current = numWritten;
                if (max != 0) {
                    float percent = (current * 100 / max);
                    status.setText(current + "/" + max + " bytes");
                    if (current == max) {
                        status.setText("complete!");
                    }
                    progress.setValue((int) percent);
                    progress.repaint();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
