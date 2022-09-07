class BackupThread extends Thread {
    public void showDialog(Shell shell) {
        TGTrackImpl track = getEditor().getTablature().getCaret().getTrack();
        if (track != null) {
            this.stringCount = track.getStrings().size();
            this.trackColor = track.getColor().clone(getSongManager().getFactory());
            this.percussionChannel = getSongManager().isPercussionChannel(track.getChannelId());
            this.initTempStrings(track.getStrings());
            this.dialog = DialogUtils.newDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            this.dialog.setLayout(new GridLayout(2, false));
            this.dialog.setText(TuxGuitar.getProperty("track.properties"));
            this.addListeners();
            this.dialog.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    removeListeners();
                }
            });
            Composite left = new Composite(this.dialog, SWT.NONE);
            left.setLayout(new GridLayout());
            left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            Composite right = new Composite(this.dialog, SWT.NONE);
            right.setLayout(new GridLayout());
            right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            Composite bottom = new Composite(this.dialog, SWT.NONE);
            bottom.setLayout(new GridLayout(2, false));
            bottom.setLayoutData(new GridData(SWT.END, SWT.FILL, true, true, 2, 1));
            initTrackInfo(makeGroup(left, 1, TuxGuitar.getProperty("track.properties.general")), track);
            initInstrumentFields(makeGroup(left, 1, TuxGuitar.getProperty("instrument")), track);
            initTuningInfo(makeGroup(right, 2, TuxGuitar.getProperty("tuning")), track);
            initButtons(bottom);
            updateTuningGroup(!this.percussionChannel);
            DialogUtils.openDialog(this.dialog, DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
        }
    }
}
