class BackupThread extends Thread {
    private Object eval(Reader reader, ScriptContext context, Bindings bindings) throws ScriptException {
        try {
            _setContext(context);
            this.jep.setInteractive(false);
            File temp = writeTemp(reader);
            this.jep.runScript(temp.getAbsolutePath());
        } catch (IOException e) {
            throw new ScriptException("Error writing to file: " + e.getMessage());
        } catch (JepException e) {
            throw (ScriptException) new ScriptException(e.getMessage()).initCause(e);
        }
        return null;
    }
}
