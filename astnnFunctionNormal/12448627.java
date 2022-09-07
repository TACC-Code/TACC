class BackupThread extends Thread {
    public void process(Reader reader, Writer writer) throws IOException {
        String requestString = IOUtils.toString(reader);
        if (logger.isDebugEnabled()) {
            logger.debug("Request data (SIMPLE FORM)=>" + requestString);
        }
        Map<String, String> formParameters;
        formParameters = RequestProcessorUtils.getDecodedRequestParameters(requestString);
        String result = process(formParameters, new HashMap<String, FileItem>());
        writer.write(result);
        if (logger.isDebugEnabled()) {
            logger.debug("ResponseData data (SIMPLE FORM)=>" + result);
        }
    }
}
