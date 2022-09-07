class BackupThread extends Thread {
    private void writeCommand(PrintWriter writer) throws IOException {
        writer.write("java");
        if (isMac()) {
            writer.write(" -XstartOnFirstThread");
        }
        if (maxmem != null) {
            writer.write(" -Xmx" + maxmem);
        }
        if (isWindows()) {
            writer.write(" -cp %CLASSPATH%");
        }
        if (debugIsEnable) {
            writer.write(" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=");
            writer.write(String.valueOf(debugport == -1 ? DEFAULT_DEBUG_PORT : debugport));
            writer.write(",suspend=y ");
        }
        writer.write(" com.google.gwt.dev.HostedMode");
        if (port != -1) {
            writer.write(" -port " + port);
        }
        if (noserver) {
            writer.write(" -noserver");
        }
        if (getLog().isDebugEnabled()) {
            writer.write(" -logLevel DEBUG");
        }
        if (style != null) {
            writer.write(" -style " + style);
        }
        if (url != null && !"".equals(url)) {
            writer.write(" -startupUrl " + url);
        }
        if (module != null && !"".equals(module)) {
            writer.write(" " + module);
        }
        writer.println();
    }
}
