class BackupThread extends Thread {
    public static InputStream loadURLAsStream(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", Environment.getPhexVendor());
            BufferedInputStream inStream = new BufferedInputStream(connection.getInputStream());
            return inStream;
        } catch (UnknownHostException exp) {
            return null;
        } catch (SocketException exp) {
            return null;
        } catch (IOException exp) {
            exp.printStackTrace();
            return null;
        }
    }
}
