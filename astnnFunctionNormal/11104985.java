class BackupThread extends Thread {
            public void widgetSelected(SelectionEvent e) {
                if (!TuxGuitar.instance().getChannelManager().isDisposed()) {
                    TuxGuitar.instance().getChannelManager().dispose();
                }
                TuxGuitar.instance().getChannelManager().show(TrackPropertiesAction.this.dialog);
            }
}
