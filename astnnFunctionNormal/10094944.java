class BackupThread extends Thread {
        public void run() {
            synchronized (this) {
                running = true;
            }
            InputStream in = null;
            OutputStream out = null;
            File outFile = null;
            label.setText("downloading...");
            File dir = null;
            try {
                dir = new File(destDir);
                dir.mkdirs();
            } catch (Exception e) {
            }
            try {
                if (!dir.isDirectory() || !dir.canWrite()) {
                    endInstall("can't write to " + destDir);
                    return;
                }
            } catch (Exception e) {
                endInstall(e.getMessage());
                return;
            }
            try {
                URL url = new URL(mirror);
                URLConnection conn = url.openConnection();
                int length = conn.getContentLength();
                progress.setMaximum(length / 1024);
                progress.setMinimum(0);
                in = conn.getInputStream();
                outFile = File.createTempFile("Jake2Data", ".zip");
                outFile.deleteOnExit();
                out = new FileOutputStream(outFile);
                copyStream(in, out);
            } catch (Exception e) {
                endInstall(e.getMessage());
                return;
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            try {
                installData(outFile.getCanonicalPath());
            } catch (Exception e) {
                endInstall(e.getMessage());
                return;
            }
            try {
                if (outFile != null) outFile.delete();
            } catch (Exception e) {
            }
            endInstall("installation successful");
        }
}
