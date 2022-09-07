class BackupThread extends Thread {
    private void _renderStartTableCell(ResponseWriter writer, String id, boolean alreadyRenderedId) throws IOException {
        writer.startElement("td", null);
        if (!alreadyRenderedId) {
            writer.writeAttribute("id", id, null);
        }
    }
}
