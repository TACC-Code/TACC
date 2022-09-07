class BackupThread extends Thread {
    protected void digestGroupHome(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            GroupHome groupHome = getGroupHome(child.getName());
            if (groupHome != null) {
                groupHome.digest(child);
                setGroupHome(groupHome);
                break;
            }
        }
    }
}
