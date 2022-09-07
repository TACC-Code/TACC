class BackupThread extends Thread {
    public void setURL(String url) throws ParserException {
        if ((null != url) && !"".equals(url)) setConnection(openConnection(url, getFeedback()));
    }
}
