class BackupThread extends Thread {
    public void submitException(Error err) {
        if (!err.isReportable()) {
            return;
        }
        try {
            String data = URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("auto", "UTF-8");
            data += "&" + URLEncoder.encode("stack", "UTF-8") + "=" + URLEncoder.encode(err.toString(), "UTF-8");
            data += "&" + URLEncoder.encode("jvm", "UTF-8") + "=" + URLEncoder.encode(System.getProperty("java.version"), "UTF-8");
            data += "&" + URLEncoder.encode("ocdsver", "UTF-8") + "=" + URLEncoder.encode(Constants.OPENCDS_VERSION, "UTF-8");
            data += "&" + URLEncoder.encode("os", "UTF-8") + "=" + URLEncoder.encode(Constants.OS_NAME + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"), "UTF-8");
            URL url = new URL(Constants.BUGREPORT_SCRIPT);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception ex) {
            org.opencdspowered.opencds.core.logging.Logger.getInstance().logException(ex, false);
        }
    }
}
