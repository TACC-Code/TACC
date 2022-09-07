class BackupThread extends Thread {
    private void handleResourceRequest(PhaseEvent event, String resource, String contentType) {
        URL url = AjaxPhaseListener.class.getResource(resource);
        URLConnection conn = null;
        InputStream stream = null;
        BufferedReader bufReader = null;
        HttpServletResponse response = (HttpServletResponse) event.getFacesContext().getExternalContext().getResponse();
        OutputStreamWriter outWriter = null;
        String curLine = null;
        try {
            outWriter = new OutputStreamWriter(response.getOutputStream(), response.getCharacterEncoding());
            conn = url.openConnection();
            conn.setUseCaches(false);
            stream = conn.getInputStream();
            bufReader = new BufferedReader(new InputStreamReader(stream));
            response.setContentType(contentType);
            response.setStatus(200);
            while (null != (curLine = bufReader.readLine())) {
                outWriter.write(curLine + "\n");
            }
            outWriter.flush();
            outWriter.close();
            event.getFacesContext().responseComplete();
        } catch (UnsupportedEncodingException e) {
            String message = "Can't load resource: " + url.toExternalForm();
            logger.error(message, e);
        } catch (IOException e) {
            String message = "Can't load resource: " + url.toExternalForm();
            logger.error(message, e);
        }
    }
}
