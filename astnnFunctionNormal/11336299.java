class BackupThread extends Thread {
    public VerilogCallback addReadWriteSynchCallback(VerilogCallbackHandler handler) {
        try {
            return pliProxy.registerCallback(PLICallbackReason.READ_WRITE_SYNCH, handler);
        } catch (VerilogRuntimeException e) {
            throw new VerilogRuntimeException("Unable to register callback for read/write synch", e);
        }
    }
}
