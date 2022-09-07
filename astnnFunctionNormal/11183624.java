class BackupThread extends Thread {
    private String readVersionFromSocket() {
        InputStream is = null;
        OutputStream os = null;
        String data = null;
        try {
            URL url = new URL(SystemGlobals.getValue(ConfigKeys.JFORUM_VERSION_URL));
            URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            os = new ByteArrayOutputStream();
            int available = is.available();
            while (available > 0) {
                byte[] b = new byte[available];
                is.read(b);
                os.write(b);
                available = is.available();
            }
            data = os.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
        return data;
    }
}
