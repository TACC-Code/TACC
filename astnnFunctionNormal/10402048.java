class BackupThread extends Thread {
    protected void digestLogger(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Logger logger = getLogger(child.getName());
            if (logger != null) {
                logger.digest(child);
                setLogger(logger);
                break;
            }
        }
    }
}
