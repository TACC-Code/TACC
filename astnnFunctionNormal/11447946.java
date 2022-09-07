class BackupThread extends Thread {
    @Override
    protected InputStream sendRequest(byte[] pRequest) throws SliceServerException {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            OutputStream os = urlConnection.getOutputStream();
            os.write(pRequest);
            os.close();
            return urlConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SliceServerException("Error trying to send data to slice server " + e.getMessage(), e);
        }
    }
}
