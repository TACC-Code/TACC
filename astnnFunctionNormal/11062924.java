class BackupThread extends Thread {
    public PasteCommand(ERDiagramEditor editor, NodeSet nodeElements, int x, int y) {
        this.viewer = editor.getGraphicalViewer();
        this.diagram = (ERDiagram) viewer.getContents().getModel();
        this.nodeElements = nodeElements;
        this.columnGroups = new GroupSet();
        for (NodeElement nodeElement : nodeElements) {
            nodeElement.setLocation(new Location(nodeElement.getX() + x, nodeElement.getY() + y, nodeElement.getWidth(), nodeElement.getHeight()));
            if (nodeElement instanceof ERTable) {
                ERTable table = (ERTable) nodeElement;
                for (Column column : table.getColumns()) {
                    if (column instanceof ColumnGroup) {
                        ColumnGroup group = (ColumnGroup) column;
                        if (!diagram.getDiagramContents().getGroups().contains(group)) {
                            columnGroups.add(group);
                        }
                    }
                }
            }
        }
    }
}
