class BackupThread extends Thread {
    public static void createTemplatesMaps(Map<String, List<IMolecule>> entriesMol, Map<IMolecule, String> entriesMolName, Map<String, Icon> entriesIcon, boolean withsubdirs) throws Exception {
        DummyClass dummy = new DummyClass();
        try {
            URL url = new URL("jar:" + dummy.getClass().getProtectionDomain().getCodeSource().getLocation().toURI() + "!/");
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            JarFile jarfile = conn.getJarFile();
            for (Enumeration<JarEntry> e = jarfile.entries(); e.hasMoreElements(); ) {
                JarEntry entry = e.nextElement();
                if (entry.getName().indexOf(TEMPLATES_PACKAGE + "/") == 0) {
                    String restname = entry.getName().substring(new String(TEMPLATES_PACKAGE + "/").length());
                    if (restname.length() > 2) {
                        if (restname.indexOf("/") == restname.length() - 1) {
                            entriesMol.put(restname.substring(0, restname.length() - 1), new ArrayList<IMolecule>());
                        } else if (restname.indexOf("/") > -1 && withsubdirs) {
                            if (entry.getName().indexOf(".mol") > -1) {
                                InputStream ins = dummy.getClass().getClassLoader().getResourceAsStream(entry.getName());
                                MDLV2000Reader reader = new MDLV2000Reader(ins, Mode.STRICT);
                                IMolecule cdkmol = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());
                                entriesMol.get(restname.substring(0, restname.indexOf("/"))).add(cdkmol);
                                entriesMolName.put(cdkmol, entry.getName().substring(0, entry.getName().length() - 4));
                            } else {
                                Icon icon = new ImageIcon(new URL(url.toString() + entry.getName()));
                                entriesIcon.put(entry.getName().substring(0, entry.getName().length() - 4), icon);
                            }
                        }
                    }
                }
            }
        } catch (ZipException ex) {
            File file = new File(new File(dummy.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + File.separator + TEMPLATES_PACKAGE.replace('/', File.separatorChar));
            for (int i = 0; i < file.listFiles().length; i++) {
                if (file.listFiles()[i].isDirectory()) {
                    File dir = file.listFiles()[i];
                    if (!dir.getName().startsWith(".")) {
                        entriesMol.put(dir.getName(), new ArrayList<IMolecule>());
                        if (withsubdirs) {
                            for (int k = 0; k < dir.list().length; k++) {
                                if (dir.listFiles()[k].getName().indexOf(".mol") > -1) {
                                    MDLV2000Reader reader = new MDLV2000Reader(new FileInputStream(dir.listFiles()[k]), Mode.STRICT);
                                    IMolecule cdkmol = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());
                                    entriesMol.get(dir.getName()).add(cdkmol);
                                    entriesMolName.put(cdkmol, dir.listFiles()[k].getName().substring(0, dir.listFiles()[k].getName().length() - 4));
                                } else {
                                    Icon icon = new ImageIcon(dir.listFiles()[k].getAbsolutePath());
                                    if (dir.listFiles()[k].getName().toLowerCase().endsWith("png")) {
                                        entriesIcon.put(dir.listFiles()[k].getName().substring(0, dir.listFiles()[k].getName().length() - 4), icon);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
