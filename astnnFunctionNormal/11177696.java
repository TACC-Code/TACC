class BackupThread extends Thread {
    protected void initDebugger() {
        if (reader == null || writer == null) {
            throw new NullPointerException("Reader or writer isn't initialized.");
        }
        if (config.isDebuggerEnabled()) {
            if (debugger == null) {
                String className = null;
                try {
                    className = System.getProperty("smack.debuggerClass");
                } catch (Throwable t) {
                }
                Class<?> debuggerClass = null;
                if (className != null) {
                    try {
                        debuggerClass = Class.forName(className);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (debuggerClass == null) {
                    try {
                        debuggerClass = Class.forName("org.jivesoftware.smackx.debugger.EnhancedDebugger");
                    } catch (Exception ex) {
                        try {
                            debuggerClass = Class.forName("org.jivesoftware.smack.debugger.LiteDebugger");
                        } catch (Exception ex2) {
                            ex2.printStackTrace();
                        }
                    }
                }
                try {
                    Constructor<?> constructor = debuggerClass.getConstructor(Connection.class, Writer.class, Reader.class);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can't initialize the configured debugger!", e);
                }
            } else {
            }
        }
    }
}
