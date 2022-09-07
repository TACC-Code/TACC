class BackupThread extends Thread {
    public void run() {
        log.debug("Starting request");
        try {
            socket.setSoTimeout(5000);
            BufferedInputStream clientIn = new BufferedInputStream(socket.getInputStream());
            OutputStream clientOut = socket.getOutputStream();
            HttpRequest request = new HttpRequest(clientIn);
            HttpResponse response = request.execute();
            clientOut.write(response.getResponseBytes());
            clientOut.flush();
            int responseCode = response.getResponseCode();
            contentLength = response.getContentLength();
            log.debug("response code = " + responseCode + " contentLength = " + contentLength);
            if (request.getRequestType().equalsIgnoreCase("GET") && (responseCode == 200 || responseCode == 206) && contentLength > 5000000) {
                response.close();
                doDownload(request, contentLength, clientOut);
            } else if (!(responseCode >= 300 && responseCode < 400) && !request.getRequestType().equalsIgnoreCase("HEAD")) {
                InputStream stream = response.getInputStream();
                BufferedOutputStream bufOut = new BufferedOutputStream(clientOut);
                for (int counter = 0, b = -1; (counter < contentLength || contentLength < 0) && (b = stream.read()) >= 0; counter++) {
                    bufOut.write(b);
                }
                bufOut.flush();
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                downloadFinished = true;
                socket.close();
            } catch (Exception e) {
            }
        }
        log.debug("Finished with request");
    }
}
