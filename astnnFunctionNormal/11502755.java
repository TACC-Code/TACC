class BackupThread extends Thread {
    private final Vector<Class<?>> findSubclasses(URL location, String packageName, Class<?> superClass) {
        Logger.getLogger(ClassFinder.class.getName()).finest("looking in package:" + packageName);
        Logger.getLogger(ClassFinder.class.getName()).finest("looking for  class:" + superClass);
        synchronized (results) {
            Map<Class<?>, URL> thisResult = new TreeMap<Class<?>, URL>(CLASS_COMPARATOR);
            Vector<Class<?>> v = new Vector<Class<?>>();
            String fqcn = searchClass.getName();
            List<URL> knownLocations = new ArrayList<URL>();
            knownLocations.add(location);
            for (int loc = 0; loc < knownLocations.size(); loc++) {
                URL url = knownLocations.get(loc);
                File directory = new File(url.getFile());
                Logger.getLogger(ClassFinder.class.getName()).finest("\tlooking in " + directory);
                if (directory.exists()) {
                    String[] files = directory.list();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].endsWith(".class")) {
                            String classname = files[i].substring(0, files[i].length() - 6);
                            Logger.getLogger(ClassFinder.class.getName()).finest("\t\tchecking file " + classname);
                            try {
                                Class<?> c = Class.forName(packageName + "." + classname);
                                if (superClass.isAssignableFrom(c) && !fqcn.equals(packageName + "." + classname)) {
                                    thisResult.put(c, url);
                                }
                            } catch (ClassNotFoundException cnfex) {
                                errors.add(cnfex);
                            } catch (Exception ex) {
                                errors.add(ex);
                            }
                        }
                    }
                } else {
                    try {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        String starts = conn.getEntryName();
                        JarFile jarFile = conn.getJarFile();
                        Logger.getLogger(ClassFinder.class.getName()).finest("starts=" + starts);
                        Logger.getLogger(ClassFinder.class.getName()).finest("JarFile=" + jarFile);
                        Enumeration<JarEntry> e = jarFile.entries();
                        while (e.hasMoreElements()) {
                            JarEntry entry = e.nextElement();
                            String entryname = entry.getName();
                            Logger.getLogger(ClassFinder.class.getName()).finest("\tconsidering entry: " + entryname);
                            if (!entry.isDirectory() && entryname.endsWith(".class")) {
                                String classname = entryname.substring(0, entryname.length() - 6);
                                if (classname.startsWith("/")) {
                                    classname = classname.substring(1);
                                }
                                classname = classname.replace('/', '.');
                                Logger.getLogger(ClassFinder.class.getName()).finest("\t\ttesting classname: " + classname);
                                try {
                                    Class c = Class.forName(classname);
                                    if (superClass.isAssignableFrom(c) && !fqcn.equals(classname)) {
                                        thisResult.put(c, url);
                                    }
                                } catch (ClassNotFoundException cnfex) {
                                    errors.add(cnfex);
                                } catch (NoClassDefFoundError ncdfe) {
                                    errors.add(ncdfe);
                                } catch (UnsatisfiedLinkError ule) {
                                    errors.add(ule);
                                } catch (Exception exception) {
                                    errors.add(exception);
                                } catch (Error error) {
                                    errors.add(error);
                                }
                            }
                        }
                    } catch (IOException ioex) {
                        errors.add(ioex);
                    }
                }
            }
            Logger.getLogger(ClassFinder.class.getName()).finest("results = " + thisResult);
            results.putAll(thisResult);
            Iterator<Class<?>> it = thisResult.keySet().iterator();
            while (it.hasNext()) {
                v.add(it.next());
            }
            return v;
        }
    }
}
