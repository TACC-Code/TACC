class BackupThread extends Thread {
    public String substitute(String str, String type) throws IllegalArgumentException {
        if (str == null) return null;
        StringReader reader = new StringReader(str);
        StringWriter writer = new StringWriter();
        try {
            substitute(reader, writer, type);
        } catch (IOException e) {
            throw new Error("Unexpected I/O exception when reading/writing memory " + "buffer; nested exception is: " + e);
        }
        return writer.getBuffer().toString();
    }
}
