class BackupThread extends Thread {
    public A2iaResult processRequest(A2iaRequest request) throws COMException {
        Integer id;
        id = (Integer) invoke("ScrGetResult", request.getChannel().getId(), request.getDocument().getId(), request.getTimeout());
        A2iaResult result = new A2iaResult(this);
        result.setId(id);
        return result;
    }
}
