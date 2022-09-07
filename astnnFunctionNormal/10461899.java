class BackupThread extends Thread {
    public void read(HttpServletRequest request, HttpServletResponse response, URL resource, String mimeType) throws Exception {
        URLConnection urlConnection = resource.openConnection();
        long lastModified = urlConnection.getLastModified();
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince / 1000 < lastModified / 1000) {
            response.setDateHeader("Last-Modified", lastModified);
            InputStream input = null;
            OutputStream output = null;
            try {
                input = new BufferedInputStream(urlConnection.getInputStream());
                response.setHeader("Content-Type", mimeType);
                output = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = input.read(buffer)) > -1) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
                logger.error("Error processing " + resource, e);
            } finally {
                if (input != null) input.close();
                if (output != null) output.close();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }
}
