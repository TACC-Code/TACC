class BackupThread extends Thread {
    private void updatePreviewPanel(IStructuredSelection sel) {
        ProcessFragment fr = null;
        if (sel.isEmpty() == false && sel.getFirstElement() instanceof ProcessFragment) {
            fr = (ProcessFragment) sel.getFirstElement();
        }
        selectBpgButton.setEnabled(fr != null);
        bpgIRI.setEnabled(fr != null);
        clearBpgButton.setEnabled(fr != null && fr.getBusinessProcessGoal() != null);
        updateGraphicalViewer(fr);
        if (fr != null) {
            bpgIRI.setText((fr.getBusinessProcessGoal() == null) ? "" : fr.getBusinessProcessGoal().getInstanceIdentity().toString());
        } else {
            bpgIRI.setText("");
        }
    }
}
