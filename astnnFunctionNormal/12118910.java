class BackupThread extends Thread {
        @Override
        protected Bitmap doInBackground(String... params) {
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            url = params[0];
            final HttpGet getRequest = new HttpGet(url);
            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w(TAG, "从" + url + "中下载图片时出错!,错误码:" + statusCode);
                    return null;
                }
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        inputStream = entity.getContent();
                        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                        outputStream = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                        copy(inputStream, outputStream);
                        outputStream.flush();
                        final byte[] data = dataStream.toByteArray();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (IOException e) {
                getRequest.abort();
                Log.w(TAG, "I/O error while retrieving bitmap from " + url, e);
            } catch (IllegalStateException e) {
                getRequest.abort();
                Log.w(TAG, "Incorrect URL: " + url);
            } catch (Exception e) {
                getRequest.abort();
                Log.w(TAG, "Error while retrieving bitmap from " + url, e);
            } finally {
                if (client != null) {
                    client.close();
                }
            }
            return null;
        }
}
