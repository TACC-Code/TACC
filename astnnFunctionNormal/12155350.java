class BackupThread extends Thread {
    public AddeTextReader(String request) {
        try {
            URL url = new URL(request);
            debug = request.indexOf("debug=true") > 0;
            if (debug) System.out.println("Request: " + request);
            urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            dis = new DataInputStream(is);
        } catch (AddeURLException ae) {
            status = -1;
            statusString = "No data found";
            String aes = ae.toString();
            if (aes.indexOf(" Accounting ") != -1) {
                statusString = "No accounting data";
                status = -3;
            }
            if (debug) System.out.println("AddeTextReader Exception:" + aes);
        } catch (Exception e) {
            status = -2;
            if (debug) System.out.println("AddeTextReader Exception:" + e);
            statusString = "Error opening connection: " + e;
        }
        linesOfText = new Vector();
        if (status == 0) readText(((AddeURLConnection) urlc).getRequestType());
        if (linesOfText.size() < 1) statusString = "No data read";
        status = linesOfText.size();
    }
}
