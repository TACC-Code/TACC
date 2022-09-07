class BackupThread extends Thread {
    public void setupPanels() {
        for (int i = 0; i < settingsService.getSettings().getChannels(); i++) {
            drawingPanelList.add(new DrawingPanel());
            drawingPanelList.get(i).setMinimumSize(new Dimension(8000, 10));
            drawingPanelList.get(i).setPreferredSize(new Dimension(8000, 100));
            drawingPanelList.get(i).setSettingsService(settingsService);
            jPanelTop.add(drawingPanelList.get(i));
        }
        jPanelBottom.add(emotionSelector);
        jSplitPane.setDividerLocation(400);
    }
}
