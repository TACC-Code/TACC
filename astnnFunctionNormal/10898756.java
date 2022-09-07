class BackupThread extends Thread {
    private void doPost() {
        try {
            URL url = getURL();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            setHeaders(conn);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(StringUtils.attrsEncode(parameters));
            wr.flush();
            wr.close();
            response = new ClientResponse(conn);
        } catch (Exception e) {
            response = new ClientResponse(e);
        } finally {
            if (callback != null) {
                callback.run(response);
            }
        }
    }
}
