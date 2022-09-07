class BackupThread extends Thread {
    @Override
    public boolean importData(final JComponent c, final Transferable t) {
        boolean dropOk = true;
        boolean patchDrop = t.isDataFlavorSupported(PatchDetailTransferable.FLAVOR);
        if (patchDrop) {
            model.unpatch();
        } else {
            boolean channelDrop = t.isDataFlavorSupported(PatchChannelTransferable.FLAVOR);
            if (channelDrop) {
                Table table = (Table) c;
                int[] rows = table.getSelectedRows();
                if (rows.length != 1) {
                    throw new AssertionError("Expected 1 selected row, found: " + rows.length);
                }
                int target = rows[0];
                try {
                    PatchChannelTransferable pct = (PatchChannelTransferable) t.getTransferData(PatchChannelTransferable.FLAVOR);
                    int[] sourceChannels = pct.getSelectedRows();
                    model.getChannelTableModel().shift(target, sourceChannels);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dropOk;
    }
}
