class BackupThread extends Thread {
    public void createNewMiddlewarePlugininstance(String pType, String pClassName) throws Exception {
        if (middlewares.get(pType) == null) middlewares.put(pType, new java.util.concurrent.CopyOnWriteArrayList<Middleware>());
        Middleware tmp = (Middleware) Class.forName(pClassName).newInstance();
        if (middlewares.get(pType).size() == 0) {
            try {
                tmp.setConfiguration();
            } catch (Exception e) {
                writeLogg(Base.getI().getLogg(pType), new LB(e));
            }
        }
        tmp.start();
        middlewares.get(pType).add(tmp);
        writeLogg(pType, new LB("created middleware plugin thread(" + tmp.getName() + ")"));
    }
}
