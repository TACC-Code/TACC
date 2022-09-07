class BackupThread extends Thread {
    public byte[] getFileDataFromProperty(String name, Node node) throws IOException, MalformedDirectoryItemException {
        Property dataProperty;
        try {
            dataProperty = node.getProperty(name);
            InputStream is = dataProperty.getValue().getBinary().getStream();
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        } catch (RepositoryException e) {
            throw new MalformedDirectoryItemException("Cannot obtain binary property [" + name + "] from node " + node);
        }
    }
}
