class BackupThread extends Thread {
            public String doInBackground() {
                ok.setEnabled(false);
                BufferedReader in = null;
                try {
                    URL url = new URL(net.mjrz.fm.ui.utils.UIDefaults.LATEST_VERSION_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int status = conn.getResponseCode();
                    if (status == 200) {
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder str = new StringBuilder();
                        while (true) {
                            String line = in.readLine();
                            if (line == null) break;
                            str.append(line);
                        }
                        return str.toString();
                    } else {
                        logger.error("Unable to retrieve latest version: HTTP ERROR CODE: " + status);
                        return "";
                    }
                } catch (Exception e) {
                    logger.error("Unable to retrieve latest version: HTTP ERROR CODE: " + e.getMessage());
                    return null;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                }
            }
}
