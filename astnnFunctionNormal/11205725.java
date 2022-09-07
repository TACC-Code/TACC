class BackupThread extends Thread {
    public Integer openRequest(A2iaRequest request) throws COMException {
        return (Integer) invoke("ScrOpenRequest", request.getChannel().getId(), request.getDocument().getId());
    }
}
