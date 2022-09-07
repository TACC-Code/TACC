class BackupThread extends Thread {
    public void run() {
        System.out.println("Calling run method on insert action");
        tool = new CreationTool(factory);
        ElementEditor edit = (ElementEditor) this.editor;
        CreateRequest request = new CreateRequest("create child");
        System.out.println("Opening dialog");
        FileDialog dialog = new FileDialog(new Shell());
        dialog.open();
        String filename = dialog.getFileName();
        System.out.println("Got filename");
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("filename", filename);
        AdvancedFactory factory = new AdvancedFactory((Class) IImage.class, map);
        request.setFactory(factory);
        System.out.println("Factory finished");
        Rectangle bounds = new Rectangle(-1, -1, 0, 0);
        request.setSize(bounds.getSize());
        request.setLocation(bounds.getLocation());
        System.out.println("Rectangle finished");
        Object shaper = edit.getAdapter(GraphicalViewer.class);
        System.out.println(shaper);
        System.out.println("Rectangle finished");
        System.out.println(shaper.getClass().toString());
        GraphicalViewer shape = (GraphicalViewer) edit.getAdapter(GraphicalViewer.class);
        System.out.println("Getting adapter");
        EditPart root = shape.getContents();
        System.out.println("Getting command");
        Command command = root.getCommand(request);
        System.out.println("Calling execute");
        shape.getEditDomain().getCommandStack().execute(command);
        System.out.println("End insert action");
    }
}
