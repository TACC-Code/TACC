class BackupThread extends Thread {
    private void generateRandomFiguresOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {
        logger.info("Randomize figures order started.");
        if (meet.getFiguresOrderGenerated()) {
            int confirm = JOptionPane.showConfirmDialog(this, "You have already generated the random meet order.  Shall I do it again and overwrite the current ordering?", "Warning", JOptionPane.OK_CANCEL_OPTION);
            if (confirm != JOptionPane.OK_OPTION) {
                logger.info("Randomize figures order cancelled.");
                return;
            }
            logger.info("Randomize figures order override.");
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        controller.generateRandomFiguresOrder(meet);
        updateStatus();
        selectTab(Tab.FIGURES_ORDER);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        logger.info("Randomize figures order complete.");
    }
}
