class BackupThread extends Thread {
    private void processFile(File file) throws IOException, SAXException, TikaException, TransformerConfigurationException {
        String uri = file.getPath().substring(this.parentFilepath.length());
        if (escapeBackslash) uri = uri.replace('\\', '/');
        if (file.isDirectory()) {
            System.out.println(name() + "skipping directory entry " + uri);
            return;
        }
        System.out.println(name() + uri);
        FileChannel input = new FileInputStream(file).getChannel();
        final long cid = cacheTxn.insertEntry(input);
        input.close();
        final long tkid = tikaTxn.getNextId();
        FileChannel tkOut = tikaTxn.getEntryInsertionChannel();
        OutputStream outputStream = Channels.newOutputStream(tkOut);
        Writer writer = new OutputStreamWriter(outputStream);
        TransformerHandler handler = saxHandlerFactory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "text");
        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
        handler.setResult(new StreamResult(writer));
        LinkSequenceHandler linkHandler = new ExternalLinkHandler();
        ContentHandler comboHandler = new TeeContentHandler(handler, linkHandler);
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        input = cacheTxn.getEntryChannel(cid);
        InputStream stream = Channels.newInputStream(input);
        try {
            this.tikaParser.parse(stream, comboHandler, metadata);
        } finally {
            writer.close();
            stream.close();
        }
        final long lid;
        StringBuilder metaString;
        final int metaStringInitCap = (2 + ((uri.length() + 48) / 64)) * 64;
        List<TypedLink> links = linkHandler.getLinks();
        if (links.isEmpty()) {
            lid = -1;
            metaString = new StringBuilder(metaStringInitCap);
        } else {
            metaString = new StringBuilder(Math.max(metaStringInitCap, links.size() * 32));
            for (TypedLink link : links) {
                metaString.append(link.getType()).append(' ').append(link.getUri()).append('\n');
            }
            ByteBuffer linkEntry = BufferUtil.INSTANCE.asciiBuffer(metaString);
            lid = tikaTxn.insertEntry(linkEntry);
            metaString.setLength(0);
        }
        metaString.append("uri=").append(uri).append("\ncache.id=").append(cid).append("\ncache.tid=").append(cacheTxnId).append("\ntika.txt.id=").append(tkid).append("\ntika.tid=").append(tikaTxnId);
        if (lid != -1) metaString.append("\ntika.link.id=").append(lid);
        metadata.remove(TikaMetadataKeys.RESOURCE_NAME_KEY);
        String[] tikaMetaKeys = metadata.names();
        for (String tikaKey : tikaMetaKeys) {
            String[] values = metadata.getValues(tikaKey);
            for (String value : values) {
                metaString.append("\ntika.m.").append(tikaKey).append('=').append(value.trim());
            }
        }
        ByteBuffer metaEntry = BufferUtil.INSTANCE.asciiBuffer(metaString);
        metaTxn.insertEntry(metaEntry);
        ++count;
    }
}
