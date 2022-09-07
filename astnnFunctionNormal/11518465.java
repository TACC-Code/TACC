class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        LewisViewWindow lws = toolB.getFocusedWindow().getLewisView();
        Atom2D lastSelected = lws.getLastSelected();
        Atom2D beforeLastSelected = lws.getBeforeLastSelected();
        boolean success = false;
        if (remove) success = lws.removeLink(lastSelected, beforeLastSelected); else success = lws.addLink(lastSelected, beforeLastSelected, bondType);
        if (!remove && !success) {
            toolB.getConsoleWindow().write("Atom valency already satisfied. You must remove links from this atom to add another one", false);
            return;
        }
        toolB.getFocusedWindow().setModified(true);
    }
}
