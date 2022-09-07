class BackupThread extends Thread {
    public String getMessage() throws IOException {
        if (httpRequest.getContentType().equals(Constants.CONTENT_TYPE_URLENCODED)) {
            return httpRequest.getParameter("message");
        } else {
            if (cachedMessage == null) {
                String charset = httpRequest.getCharacterEncoding();
                if (charset == null) charset = "UTF-8";
                BufferedReader in = new BufferedReader(new InputStreamReader(httpRequest.getInputStream(), charset));
                CharArrayWriter data = new CharArrayWriter();
                char buf[] = new char[4096];
                int ret;
                while ((ret = in.read(buf, 0, 4096)) != -1) data.write(buf, 0, ret);
                cachedMessage = data.toString();
            }
            return cachedMessage;
        }
    }
}
