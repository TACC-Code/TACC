class BackupThread extends Thread {
    public void addNotitie(String s) throws ServerErrorException {
        Hashtable request = new Hashtable();
        request.put("REQUEST", "add_notitie");
        request.put("notitie", s);
        if (!writeObj(request) || failure(readObj())) throw new ServerErrorException("Could not add notitie");
    }
}
