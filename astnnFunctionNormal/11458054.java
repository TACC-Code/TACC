class BackupThread extends Thread {
    @Override
    protected void save(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath) throws Exception {
        ERDiagram diagram = this.getDiagram();
        Dictionary dictionary = diagram.getDiagramContents().getDictionary();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(saveFilePath)));
            CsvWriter writer = new CsvWriter(out);
            writer.print(ResourceString.getResourceString("label.physical.name"));
            writer.print(ResourceString.getResourceString("label.logical.name"));
            writer.print(ResourceString.getResourceString("label.column.type"));
            writer.print(ResourceString.getResourceString("label.column.description"));
            writer.crln();
            String database = diagram.getDatabase();
            List<Word> list = dictionary.getWordList();
            Collections.sort(list);
            for (Word word : list) {
                writer.print(word.getPhysicalName());
                writer.print(word.getLogicalName());
                if (word.getType() != null) {
                    writer.print(Format.formatType(word.getType(), word.getTypeData(), database));
                } else {
                    writer.print("");
                }
                writer.print(word.getDescription());
                writer.crln();
            }
            Activator.showMessageDialog("dialog.message.export.finish");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
