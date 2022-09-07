class BackupThread extends Thread {
    @Override
    public String getToolTipText(final MouseEvent e) {
        String result = null;
        if (!layouter.isOutside(e.getX(), e.getY())) {
            StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("DMX channel ");
            b.append(layouter.getChannel(e.getX(), e.getY()));
            b.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            b.append("<br>");
            b.append("<br>");
            b.append("<b><u>Fixture</u></b>");
            b.append("<br>");
            b.append("  channel=");
            b.append("</html>");
            int xspaces = e.getX() % 10;
            int yspaces = e.getY() % 10;
            for (int i = 0; i < xspaces; i++) {
                b.append(" ");
            }
            b.append("\n");
            for (int i = 0; i < yspaces; i++) {
                b.append(" ");
            }
            result = b.toString();
        }
        return result;
    }
}
