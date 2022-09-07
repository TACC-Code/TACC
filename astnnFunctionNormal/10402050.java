class BackupThread extends Thread {
    protected void digestUserHome(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            UserHome userHome = getUserHome(child.getName());
            if (userHome != null) {
                userHome.digest(child);
                setUserHome(userHome);
                break;
            }
        }
    }
}
