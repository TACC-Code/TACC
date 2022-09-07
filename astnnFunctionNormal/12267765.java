class BackupThread extends Thread {
    private void execute(Reader reader) throws XQueryException {
        final Writer writer;
        if (_out != null) {
            try {
                writer = new FastBufferedWriter(new OutputStreamWriter(new FileOutputStream(_out), _encoding), 8192);
            } catch (IOException e) {
                throw new DynamicError("Illegal Output file: " + e.getMessage());
            }
        } else {
            writer = new FastBufferedWriter(new OutputStreamWriter(System.out, Charset.forName(_encoding)), 4096);
        }
        if (_remoteEndpoint != null) {
            try {
                executeAt(reader, writer, _remoteEndpoint);
            } catch (IOException e) {
                throw new XQueryException("Caused an IO error", e);
            }
        } else {
            XQueryModule xqmod = new XQueryModule();
            XQueryProcessor proc = new XQueryProcessor(xqmod);
            XQueryModule module = proc.parse(reader, getBaseUri());
            if (_pull) {
                executeWithPullMode(proc, module, writer);
            } else {
                executeWithPushMode(proc, module, writer);
            }
        }
        try {
            writer.flush();
        } catch (IOException ignorable) {
            String msg = ignorable.getMessage();
            if (msg != null) {
                System.err.println(msg);
            }
        }
    }
}
