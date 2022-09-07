class BackupThread extends Thread {
    public void generateNextProfile() {
        mapPanel.showMessage("Generating profile for xsection " + xsindex + " for channel: " + channel.getId());
        mapPanel.getInfoPanel().clear();
        mapPanel.getInfoPanel().add(xsEditorPanel);
        xsEditorPanel.draw(channel, xsindex, mapPanel, new ElevationDataLoaded() {

            public void elevationDataLoaded() {
                xsEditorPanel.snapToElevationProfile(mapPanel);
                xsEditorPanel.trimProfile(mapPanel);
                xsEditorPanel.updateProfile(mapPanel);
                xsindex++;
                if (xsindex < channel.getXsections().size()) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                        public void execute() {
                            GenerateProfileForXSection.this.generateNextProfile();
                        }
                    });
                } else {
                    mapPanel.showMessage(xsindex + " cross sections generated for channel: " + channel.getId());
                    mapPanel.getInfoPanel().clear();
                    mapPanel.getChannelManager().getChannelClickHandler().drawXSectionLines(channel);
                }
            }
        });
    }
}
