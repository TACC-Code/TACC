class BackupThread extends Thread {
    protected void digestPolicy(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Policy policy = getPolicy(child.getName());
            if (policy != null) {
                policy.digest(child);
                setPolicy(policy);
                break;
            }
        }
    }
}
