class BackupThread extends Thread {
    private void _processNamespaces() throws XMLStreamException {
        int namespaceCount = reader.getNamespaceCount();
        for (int i = 0; i < namespaceCount; i++) {
            writer.writeNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
            this.namespaceContext.put(reader.getNamespacePrefix(i), new Namespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i)));
        }
    }
}
