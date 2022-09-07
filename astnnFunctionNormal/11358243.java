class BackupThread extends Thread {
    private Machine openStoryFile() {
        String source = getParameter("storyfile");
        java.io.InputStream inputstream = null;
        try {
            URL url = new URL(getDocumentBase(), source);
            inputstream = url.openStream();
            MachineConfig config = new DefaultMachineConfig(inputstream);
            StoryFileHeader fileheader = config.getStoryFileHeader();
            if (fileheader.getVersion() < 1 || fileheader.getVersion() == 6) {
                JOptionPane.showMessageDialog(null, "Story file version 6 is not supported.", "Story file read error", JOptionPane.ERROR_MESSAGE);
                stop();
            }
            Machine machine = new MachineImpl();
            InstructionDecoder decoder = new DefaultInstructionDecoder();
            machine.initialize(config, decoder);
            return machine;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inputstream != null) inputstream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
