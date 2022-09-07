class BackupThread extends Thread {
    public MyDocument(java.net.URL url) {
        setSource(url);
        makeTextDocument();
        if (url == null) return;
        try {
            final int charBufferSize = 1000;
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            char[] charBuffer = new char[charBufferSize];
            int numRead = 0;
            while ((numRead = reader.read(charBuffer, 0, charBufferSize)) != -1) {
                stringBuffer.append(charBuffer, 0, numRead);
            }
            textDocument.insertString(0, stringBuffer.toString(), null);
            setHasChanges(false);
        } catch (java.io.IOException exception) {
            throw new RuntimeException(exception.getMessage());
        } catch (BadLocationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
