class BackupThread extends Thread {
    private void initInstrumentFields(Composite composite, TGTrackImpl track) {
        composite.setLayout(new GridLayout());
        Composite top = new Composite(composite, SWT.NONE);
        top.setLayout(new GridLayout(2, false));
        top.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
        Label instrumentLabel = new Label(top, SWT.NONE);
        instrumentLabel.setText(TuxGuitar.getProperty("instrument") + ":");
        instrumentLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
        this.instrumentCombo = new Combo(top, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.instrumentCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.instrumentCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkPercussionChannel();
            }
        });
        this.loadChannels(track.getChannelId());
        Button settings = new Button(top, SWT.PUSH);
        settings.setImage(TuxGuitar.instance().getIconManager().getSettings());
        settings.setToolTipText(TuxGuitar.getProperty("settings"));
        settings.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        settings.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (!TuxGuitar.instance().getChannelManager().isDisposed()) {
                    TuxGuitar.instance().getChannelManager().dispose();
                }
                TuxGuitar.instance().getChannelManager().show(TrackPropertiesAction.this.dialog);
            }
        });
    }
}
