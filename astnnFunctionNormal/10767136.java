class BackupThread extends Thread {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BufferedReader reader = request.getReader();
        StringWriter writer = new StringWriter();
        char[] buffer = new char[BUFFER_SIZE];
        int len = 0;
        while ((len = reader.read(buffer)) != -1) writer.write(buffer, 0, len);
        String result = writer.toString();
        try {
            String type = XMLUtil.getType(new ByteArrayInputStream(result.getBytes()));
            if ("Collection".equals(type)) {
                IESRCollection coll = new IESRCollection();
                coll.read(new ByteArrayInputStream(result.getBytes()));
                registry.putRecord(coll.getIdentifier(), result, "Collection");
            } else if ("Service".equals(type)) {
                IESRService service = new IESRService();
                service.read(new ByteArrayInputStream(result.getBytes()));
                registry.putRecord(service.getIdentifier(), result, "Service");
            } else throw new ServletException("not a recoganized format");
        } catch (SerializationException ex) {
            throw new ServletException(ex);
        } catch (ServiceException ex) {
            throw new ServletException(ex);
        } catch (IESRFormatException ex) {
            throw new ServletException(ex);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
