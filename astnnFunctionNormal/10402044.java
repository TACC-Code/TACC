class BackupThread extends Thread {
    protected void digestListeners(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Listener listener = getListener(child.getName());
            if (listener != null) {
                listener.digest(child);
                addListener(listener);
            }
        }
    }
}
