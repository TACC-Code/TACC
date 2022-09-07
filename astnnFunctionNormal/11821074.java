class BackupThread extends Thread {
    private void updateGraphicalViewer(ProcessFragment selected) {
        WorkflowEntityNode model = uiModelsCache.get(selected);
        if (model == null) {
            if (selected != null) {
                Set<WorkflowEntityNode> guiNodes = null;
                try {
                    guiNodes = new BPMOImporter().importProcessFragment(selected);
                } catch (Exception ex) {
                    guiNodes = new HashSet<WorkflowEntityNode>();
                    LogManager.logError(ex);
                }
                if (guiNodes.size() > 1) {
                    model = new ProcessNode("");
                    for (WorkflowEntityNode node : guiNodes) {
                        ((ProcessNode) model).addWorkflow(node);
                    }
                } else if (guiNodes.size() == 1) {
                    model = guiNodes.iterator().next();
                } else {
                    model = new BpmoModel();
                }
            } else {
                model = new BpmoModel();
            }
            uiModelsCache.put(selected, model);
            if (model instanceof WorkflowEntitiesContainer) {
                BPMOModelLayouter.doLayout((WorkflowEntitiesContainer) model);
            }
        }
        viewer.setContents(model);
        if (customPreviewScale != null) {
            ((ScalableFreeformRootEditPart) viewer.getRootEditPart()).getZoomManager().setZoomAsText(customPreviewScale);
        }
        previewArea.layout();
    }
}
