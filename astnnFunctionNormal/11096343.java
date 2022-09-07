class BackupThread extends Thread {
    public void digest(Element root) {
        super.digest(root);
        for (Iterator i = root.getChildren("param").iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            addParam(child.getChildTextTrim("name"), child.getChildTextTrim("value"));
        }
    }
}
