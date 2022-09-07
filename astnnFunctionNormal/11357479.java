class BackupThread extends Thread {
    protected void closeStartTag() throws IOException {
        if (finished) {
            throw new IllegalArgumentException("trying to write past already finished output" + getLocation());
        }
        if (seenBracket) {
            seenBracket = seenBracketBracket = false;
        }
        if (startTagIncomplete || setPrefixCalled) {
            if (setPrefixCalled) {
                throw new IllegalArgumentException("startTag() must be called immediately after setPrefix()" + getLocation());
            }
            if (!startTagIncomplete) {
                throw new IllegalArgumentException("trying to close start tag that is not opened" + getLocation());
            }
            writeNamespaceDeclarations();
            out.write('>');
            elNamespaceCount[depth] = namespaceEnd;
            startTagIncomplete = false;
        }
    }
}
