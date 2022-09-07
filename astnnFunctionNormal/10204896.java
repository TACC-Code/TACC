class BackupThread extends Thread {
    public File matchVM(String user, String VMTemplateClass, String VMname, String hostName, int cores, int memSize, String[] nets) throws DirectoryException {
        File template = new File(tempDir + "/templateVM-" + VMname);
        String imagesPath = repo + "/" + VMTemplateClass;
        File dir = new File(imagesPath);
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".img") && !name.startsWith("swap") && !name.equalsIgnoreCase("disk.img");
            }
        };
        String[] extraImages = dir.list(filter);
        try {
            FileWriter fstream = new FileWriter(template);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("" + "NAME   = " + VMname + " \n" + "CPU    = " + "0.1" + " \n" + "MEMORY = " + memSize + " \n" + "OS     = [ \n" + " kernel   = \"/boot/" + kernel + "\", \n" + " initrd   = \"/boot/" + initrd + "\", \n" + " root     = \"xvda2 ro\" ] \n" + "DISK   = [ \n" + " type     = \"swap\", \n" + " size     = " + memSize + ", \n" + " target   = \"xvda1\"] \n" + "DISK   = [" + " source   = \"" + repo + "/" + user + "/" + VMTemplateClass + "/disk.img\", \n" + " target   = \"xvda2\", \n " + " readonly = \"no\" ] \n");
            out.write("RAW = [ type=\"xen\", data=\"vcpus=" + cores + "\" ]");
            if (extraImages != null) {
                for (int i = 0; i < extraImages.length; i++) {
                    try {
                        int j = getCounter(extraImages[i]);
                        out.write("DISK   = [" + " source   = \"" + repo + "/" + user + "/" + VMTemplateClass + "/" + extraImages[i] + "\", \n" + " target   = \"xvda" + j + "\", \n " + " readonly = \"no\" ] \n");
                    } catch (Exception x) {
                        System.out.println("No disk index number extracted from " + extraImages[i]);
                    }
                }
            }
            if (hostName != null) {
                String host = hostName.substring(0, hostName.indexOf('.'));
                out.write("REQUIREMENTS = \"HOSTNAME = \\\"" + host + "*\\\"\" \n");
            }
            for (int i = 0; i < nets.length; i++) {
                out.write("NIC    = [ NETWORK = \"" + nets[i] + "\" ] \n");
            }
            out.close();
            fstream.close();
            return template;
        } catch (Exception x) {
            throw new DirectoryException("Connot write ONE template file.", x);
        }
    }
}
