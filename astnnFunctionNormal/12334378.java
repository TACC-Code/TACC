class BackupThread extends Thread {
    private void urlToFile(String urlAsString, String localPath, ProgressBar progress) {
        FileOutputStream f = null;
        URLConnection con = null;
        InputStream is = null;
        BufferedOutputStream out = null;
        if (progress != null) {
            progress.publish(localPath);
        }
        try {
            URL url = new URL(urlAsString);
            con = url.openConnection();
            ((HttpURLConnection) con).setRequestProperty("User-Agent", "autoupdater-" + appProps.app + "-" + appProps.version);
            is = con.getInputStream();
            BufferedInputStream in = new BufferedInputStream(is);
            f = new FileOutputStream(localPath);
            out = new BufferedOutputStream(f);
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
                if (progress != null) {
                    progress.totalDone++;
                    progress.progress();
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AutoUpdater file unavailable:" + localPath, e);
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                out.close();
            } catch (Throwable t) {
            }
            try {
                f.close();
            } catch (Throwable t) {
            }
        }
    }
}
