class BackupThread extends Thread {
    protected String serializeToken(Token t) {
        StringBuffer buf = new StringBuffer(50);
        buf.append(t.getTokenIndex());
        buf.append('\t');
        buf.append(t.getType());
        buf.append('\t');
        buf.append(t.getChannel());
        buf.append('\t');
        buf.append(t.getLine());
        buf.append('\t');
        buf.append(t.getCharPositionInLine());
        serializeText(buf, t.getText());
        return buf.toString();
    }
}
