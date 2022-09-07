class BackupThread extends Thread {
    public ProgressWindow(QuickDiskTestView parent, File testDataFilesPath, boolean isVerifyMode, TestDataConfig writeModeConfig, TestDataGenerator testDataGenerator) {
        String errMsgPrefix = "";
        try {
            if (isVerifyMode) {
                worker = new WorkerThreadVerifier(testDataFilesPath, testDataGenerator);
            } else {
                worker = new WorkerThreadWriter(testDataFilesPath, writeModeConfig, testDataGenerator);
            }
            errMsgPrefix = "Error initialising the progress window: ";
            initComponents();
            this.parent = parent;
            this.testDataFilesPath = testDataFilesPath;
            this.isVerifyMode = isVerifyMode;
            setLocationRelativeTo(parent.getFrame());
            setIconImage(Common.appIcon);
            updateTitle();
            jLabelPaused.setVisible(isPaused);
            ActionListener timerTaskPerformer = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    timerTick(TIMER_PERIOD_MS);
                }
            };
            updateProgress();
            timer = new Timer(TIMER_PERIOD_MS, timerTaskPerformer);
            GuiUtils.disallowVerticalResizing((javax.swing.JFrame) this, getHeight());
            jToggleButtonPause.requestFocusInWindow();
            jCheckBoxSkipReconnection.setEnabled(!isVerifyMode);
            jLabelSkipReconnection.setEnabled(!isVerifyMode);
            jCheckBoxSkipReconnection.setSelected(false);
            GuiUtils.copyReadOnlyTextFieldBackgroundColourFromLabel(jTextFieldStartPath, jLabelCurrentPath);
            GuiUtils.copyReadOnlyTextFieldBackgroundColourFromLabel(jTextFieldTargetSize, jLabelCurrentPath);
            GuiUtils.copyReadOnlyTextFieldBackgroundColourFromLabel(jTextFieldElapsedTime, jLabelCurrentPath);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldStartPath);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldTargetSize);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldElapsedTime);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldTotalSize);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldEstimatedTime);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldSpeed);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jTextFieldCurrentPath);
            PropertyChangeListener propChangeListener = new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    assert "state".equals(evt.getPropertyName()) && evt.getNewValue().equals("done") : ErrUtils.assertionFailed();
                    processingDone();
                }
            };
            worker.setPropertyChangeListener(propChangeListener);
            setVisible(true);
        } catch (Throwable e) {
            dispose();
            throw ErrUtils.asRuntimeException(e, errMsgPrefix);
        }
    }
}
