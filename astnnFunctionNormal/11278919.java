class BackupThread extends Thread {
    public static boolean getWebImage2(String url, ImageView image) {
        boolean result = false;
        HttpGet get = new HttpGet(url);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity().isStreaming()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
                    image.setImageBitmap(bitmap);
                    result = true;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
