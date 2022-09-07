class BackupThread extends Thread {
    void exportTexturesToImages() {
        if (commandLineOptions.allFileNames.size() == 0) {
            System.out.println("Export error: need at least one filename of texture graph to load.");
            System.exit(0);
        }
        ChannelUtils.useCache = false;
        for (String filename : commandLineOptions.allFileNames) {
            TextureGraphEditorPanel te = new TextureGraphEditorPanel();
            te.load(filename, true);
            for (TextureGraphNode n : te.graph.getAllNodes()) {
                if (n.getChannel().isMarkedForExport()) {
                    String exportname = n.getChannel().exportName.get();
                    String f = filename;
                    String tmp_filename = f.substring(f.lastIndexOf("\\") + 1, f.length() - 4);
                    tmp_filename = tmp_filename.substring(tmp_filename.lastIndexOf("/") + 1);
                    exportname = exportname.replaceAll("\\%f", tmp_filename);
                    exportname = exportname.replaceAll("\\%r", commandLineOptions.exportResX + "x" + commandLineOptions.exportResY);
                    exportname = commandLineOptions.exportPath + "/" + exportname + ".png";
                    System.out.println("Exporting " + exportname);
                    try {
                        ImageIO.write(ChannelUtils.createAndComputeImage(n.getChannel(), commandLineOptions.exportResX, commandLineOptions.exportResY, new StdOutProgressBar(), 0), "png", new File(exportname));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.exit(0);
    }
}
