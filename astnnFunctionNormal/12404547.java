class BackupThread extends Thread {
    public void doObjectPre(MDObject obj) throws Exception {
        this.obj = obj;
        objName = obj.getName();
        locale = obj.getConfig().getLocale();
        readLink = obj.getReadLink();
        readLink += "&qid=" + qid;
        localizedDetailsString = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, locale, "details"), objName);
        cls = obj.getActualClass();
        if (!cls.isRelationship()) {
            String shorterName = Util.shortenString(objName);
            xb.openElement("td");
            String classString = "read_c" + cls.getId();
            xb.writeAnchor(readLink, shorterName, classString, shorterName);
            xb.closeElement("td");
        }
    }
}
