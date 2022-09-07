class BackupThread extends Thread {
    void open(ExecutionContext inContext, String inType, String inUrl, boolean inAsync) throws ScriptException {
        putInternal("type", new TString(inType));
        putInternal("url", new TString(inUrl));
        putInternal("readyState", new TNumber(READY_STATE_UNINITIALIZED));
        putInternal("async", TBoolean.valueOf(inAsync));
        URL url;
        try {
            url = new URL(inUrl);
        } catch (MalformedURLException e) {
            throw new ScriptException(ScriptException.Type.ERROR, e.toString(), inContext.getLineNumber());
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new ScriptException(ScriptException.Type.ERROR, e.toString(), inContext.getLineNumber());
        }
        if ("post".equalsIgnoreCase(inType)) {
            try {
                connection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new ScriptException(ScriptException.Type.ERROR, e.toString(), inContext.getLineNumber());
            }
        }
    }
}
