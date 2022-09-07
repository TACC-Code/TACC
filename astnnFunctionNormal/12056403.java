class BackupThread extends Thread {
    public void convert(InputStream in, OutputStream out, ConversionParameters params) throws ConverterException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("convert(InputStream, OutputStream, params.sourceCharset=" + params.getSourceCharset() + ", params.targetCharset=" + params.getTargetCharset() + ") - start");
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        reader = new BufferedReader(new InputStreamReader(in, new EightBitCharset()));
        writer = new BufferedWriter(new OutputStreamWriter(out, CharsetUtil.forName(params.getTargetCharset())));
        convert(reader, writer, params);
        try {
            writer.flush();
        } catch (IOException e) {
        }
    }
}
