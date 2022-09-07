class BackupThread extends Thread {
    public HttpMultipartRequest(String url, Hashtable params, String fileField, String fileName, String fileType, InputStream in) throws Exception {
        this.url = url;
        String boundary = getBoundaryString();
        String boundaryMessage = getBoundaryMessage(boundary, params, fileField, fileName, fileType);
        String endBoundary = "\r\n--" + boundary + "--\r\n";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(boundaryMessage.getBytes());
        int read = 0;
        byte[] buf = new byte[bufSize];
        while ((read = in.read(buf)) != -1) bos.write(buf, 0, read);
        bos.write(endBoundary.getBytes());
        this.postBytes = bos.toByteArray();
        bos.close();
    }
}
