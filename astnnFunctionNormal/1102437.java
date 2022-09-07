class BackupThread extends Thread {
    public void registerRequest(Request req, MsgContext ep, int count) {
        if (this.domain != null) {
            try {
                RequestInfo rp = req.getRequestProcessor();
                rp.setGlobalProcessor(global);
                ObjectName roname = new ObjectName(getDomain() + ":type=RequestProcessor,worker=" + getChannelName() + ",name=JkRequest" + count);
                ep.setNote(JMXRequestNote, roname);
                Registry.getRegistry(null, null).registerComponent(rp, roname, null);
            } catch (Exception ex) {
                log.warn("Error registering request");
            }
        }
    }
}
