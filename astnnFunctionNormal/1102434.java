class BackupThread extends Thread {
    public void init() throws IOException {
        if (file == null) {
            log.debug("No file, disabling unix channel");
            return;
        }
        if (wEnv != null && wEnv.getLocalId() != 0) {
            localId = wEnv.getLocalId();
        }
        if (localId != 0) {
            file = file + localId;
        }
        File socketFile = new File(file);
        if (!socketFile.isAbsolute()) {
            String home = wEnv.getJkHome();
            if (home == null) {
                log.debug("No jkhome");
            } else {
                File homef = new File(home);
                socketFile = new File(homef, file);
                log.debug("Making the file absolute " + socketFile);
            }
        }
        if (!socketFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(socketFile);
                fos.write(1);
                fos.close();
            } catch (Throwable t) {
                log.error("Attempting to create the file failed, disabling channel" + socketFile);
                return;
            }
        }
        if (!socketFile.delete()) {
            log.error("Can't remove socket file " + socketFile);
            return;
        }
        super.initNative("channel.un:" + file);
        if (apr == null || !apr.isLoaded()) {
            log.debug("Apr is not available, disabling unix channel ");
            apr = null;
            return;
        }
        setNativeAttribute("file", file);
        setNativeAttribute("listen", "10");
        if (next == null && wEnv != null) {
            if (nextName != null) setNext(wEnv.getHandler(nextName));
            if (next == null) next = wEnv.getHandler("dispatch");
            if (next == null) next = wEnv.getHandler("request");
        }
        super.initJkComponent();
        JMXRequestNote = wEnv.getNoteId(WorkerEnv.ENDPOINT_NOTE, "requestNote");
        if (this.domain != null) {
            try {
                tpOName = new ObjectName(domain + ":type=ThreadPool,name=" + getChannelName());
                Registry.getRegistry(null, null).registerComponent(tp, tpOName, null);
                rgOName = new ObjectName(domain + ":type=GlobalRequestProcessor,name=" + getChannelName());
                Registry.getRegistry(null, null).registerComponent(global, rgOName, null);
            } catch (Exception e) {
                log.error("Can't register threadpool");
            }
        }
        tp.start();
        AprAcceptor acceptAjp = new AprAcceptor(this);
        tp.runIt(acceptAjp);
        log.info("JK: listening on unix socket: " + file);
    }
}
