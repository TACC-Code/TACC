class BackupThread extends Thread {
    public Bitmap downloadBitmap(String url) {
        HttpURLConnection huc = null;
        InputStream is = null;
        Bitmap bm = null;
        try {
            huc = ((HttpURLConnection) (new URL(url).openConnection()));
            huc.setDoInput(true);
            huc.connect();
            is = huc.getInputStream();
            bm = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.d("TEST", e.getMessage(), e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                Log.d("TEST", e.getMessage(), e);
            }
            if (huc != null) huc.disconnect();
        }
        return bm;
    }
}
