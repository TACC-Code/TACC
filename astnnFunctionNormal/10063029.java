class BackupThread extends Thread {
    public static void downloadFile(HttpServletResponse response, InputStream input, String fileName, boolean attachment) throws IOException {
        BufferedOutputStream output = null;
        try {
            int contentLength = input.available();
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            String disposition = attachment ? "attachment" : "inline";
            response.setContentLength(contentLength);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", disposition + "; filename=\"" + fileName + "\"");
            output = new BufferedOutputStream(response.getOutputStream());
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            output.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
