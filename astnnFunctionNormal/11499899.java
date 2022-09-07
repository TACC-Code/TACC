class BackupThread extends Thread {
    public void debug() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        System.out.println("");
        while (selectedObjects.hasNext()) {
            Object o = ((EditPart) selectedObjects.next()).getModel();
            if (o instanceof NodeModel) {
                NodeModel model = (NodeModel) o;
                if (model instanceof Actor) {
                    Actor actor = (Actor) model;
                    System.out.println(actor.getEntity().l3pe.getRDFId());
                } else if (o instanceof ChbComplex) {
                    ChbComplex cmp = (ChbComplex) o;
                    System.out.println(cmp.getComplex().getRDFId());
                }
            }
        }
    }
}
