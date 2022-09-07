class BackupThread extends Thread {
    public String invokeAction(String url, String name, String fileName, InputStream dataStream, int contentLength, String contentType) {
        StringBuffer response = null;
        try {
            if (!url.startsWith("https")) {
                throw new MalformedURLException("ITunesUFilePOST.invokeAction(): URL \"" + url + "\" does not use HTTPS.");
            }
            String boundary = createBoundary();
            byte[] header = ("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"\r\n" + "Content-Type: " + contentType + "\r\n" + "\r\n").getBytes("UTF-8");
            contentLength += header.length;
            byte[] footer = ("\r\n--" + boundary + "--\r\n").getBytes("UTF-8");
            contentLength += footer.length;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=\"" + boundary + "\"");
            connection.setFixedLengthStreamingMode(contentLength);
            connection.connect();
            OutputStream output = connection.getOutputStream();
            output.write(header);
            byte[] dataBuffer = new byte[16 * 1024];
            for (int n = 0; n >= 0; ) {
                n = dataStream.read(dataBuffer, 0, dataBuffer.length);
                if (n > 0) output.write(dataBuffer, 0, n);
            }
            output.write(footer);
            output.flush();
            output.close();
            response = new StringBuffer();
            InputStream input = connection.getInputStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            reader = new BufferedReader(reader);
            char[] buffer = new char[16 * 1024];
            for (int n = 0; n >= 0; ) {
                n = reader.read(buffer, 0, buffer.length);
                if (n > 0) response.append(buffer, 0, n);
            }
            input.close();
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            throw new java.lang.AssertionError("ITunesUFilePOST.invokeAction(): UTF-8 encoding not supported!");
        } catch (IOException e) {
            throw new java.lang.AssertionError("ITunesUFilePOST.invokeAction(): I/O Exception " + e);
        }
        return response.toString();
    }
}
