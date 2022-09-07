class BackupThread extends Thread {
    private void loadPSF(SceModule module, boolean analyzeOnly) {
        if (module.psf != null) return;
        String filetoload = module.pspfilename;
        if (filetoload.startsWith("ms0:")) filetoload = filetoload.replace("ms0:", "ms0");
        File metapbp = null;
        File pbpfile = new File(filetoload);
        if (pbpfile.getParentFile() == null || pbpfile.getParentFile().getParentFile() == null) {
            return;
        }
        File metadir = new File(pbpfile.getParentFile().getParentFile().getPath() + File.separatorChar + "%" + pbpfile.getParentFile().getName());
        if (metadir.exists()) {
            File[] eboot = metadir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File arg0) {
                    return arg0.getName().equalsIgnoreCase("eboot.pbp");
                }
            });
            if (eboot.length > 0) metapbp = eboot[0];
        }
        metadir = new File(pbpfile.getParentFile().getParentFile().getPath() + File.separatorChar + pbpfile.getParentFile().getName() + "%");
        if (metadir.exists()) {
            File[] eboot = metadir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File arg0) {
                    return arg0.getName().equalsIgnoreCase("eboot.pbp");
                }
            });
            if (eboot.length > 0) metapbp = eboot[0];
        }
        if (metapbp != null) {
            FileChannel roChannel;
            try {
                roChannel = new RandomAccessFile(metapbp, "r").getChannel();
                ByteBuffer readbuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size());
                PBP meta = new PBP(readbuffer);
                module.psf = meta.readPSF(readbuffer);
                roChannel.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] psffile = pbpfile.getParentFile().listFiles(new FileFilter() {

                @Override
                public boolean accept(File arg0) {
                    return arg0.getName().equalsIgnoreCase("param.sfo");
                }
            });
            if (psffile != null && psffile.length > 0) {
                try {
                    FileChannel roChannel = new RandomAccessFile(psffile[0], "r").getChannel();
                    ByteBuffer readbuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size());
                    module.psf = new PSF();
                    module.psf.read(readbuffer);
                    roChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
