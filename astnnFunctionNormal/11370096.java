class BackupThread extends Thread {
    public boolean createHtml(String url, String path) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(path)) return false;
        boolean rtn = false;
        HttpURLConnection huc = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.connect();
            InputStream stream = huc.getInputStream();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            rtn = true;
        } catch (IOException ioe) {
            logger.error("", ioe);
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                logger.error("", ioe);
            }
            try {
                bw.close();
            } catch (IOException ioe) {
                logger.error("", ioe);
            }
            huc.disconnect();
        }
        return rtn;
    }
}
