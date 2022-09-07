class BackupThread extends Thread {
    public UniversePanel() {
        setToolTipText("Test");
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(final MouseEvent e) {
                ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
                dismissDelay = toolTipManager.getDismissDelay();
                initialDelay = toolTipManager.getInitialDelay();
                toolTipManager.setDismissDelay(Integer.MAX_VALUE);
                toolTipManager.setInitialDelay(0);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
                toolTipManager.setDismissDelay(dismissDelay);
                toolTipManager.setInitialDelay(initialDelay);
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (!layouter.isOutside(x, y)) {
                    System.out.println("Set start address " + layouter.getChannel(x, y));
                }
            }
        });
    }
}
