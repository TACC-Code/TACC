class BackupThread extends Thread {
    public void exec(URL url, boolean ownThread) throws Exception {
        String id = url.toString();
        Reader reader = new InputStreamReader(url.openStream());
        synchronized (scripts) {
            Scriptable sc = enterContext(null);
            Context context = Context.getCurrentContext();
            Script compiledScript = compileScript(context, sc, reader, id);
            scripts.put(id, compiledScript);
        }
        exec(id, ownThread);
    }
}
