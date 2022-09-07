class BackupThread extends Thread {
    public Document receiveDocument() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read = inputstream.read();
        while (read != 0) {
            if (read == -1) {
                throw new IOException();
            }
            buffer.write(read);
            try {
                read = inputstream.read();
            } catch (IOException e) {
                throw new IOException("Reading from input-stream failed.");
            }
        }
        byte[] raw = buffer.toByteArray();
        Document doc;
        try {
            doc = documentbuilderfactory.newDocumentBuilder().parse(new ByteArrayInputStream(raw));
        } catch (SAXException e) {
            throw new IOException("Error parsing");
        } catch (IOException e) {
            throw new IOException("Error parsing");
        } catch (ParserConfigurationException e) {
            throw new IOException("Error parsing");
        }
        return doc;
    }
}
