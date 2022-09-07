class BackupThread extends Thread {
    public Modulo load(String aliaspro, String userlogin, Set<String> filenames) {
        try {
            ProyectoBean pb = ProyectoManager.getInstance().loadByPrimaryKey(aliaspro, userlogin);
            if (pb == null) {
                throw new RuntimeException("Proyecto " + userlogin + "/" + aliaspro + " no existe.");
            }
            List<String> loadedFiles = new ArrayList<String>();
            Modulo modulo = new Modulo(pb.getUsuario(), pb.getAlias());
            File files[] = getFiles(pb, false, filenames);
            modulo.startDigest();
            for (File file : files) {
                if (filenames == null && file.getName().startsWith("!")) {
                    continue;
                }
                loadedFiles.add(file.getName());
                Reader r = new InputStreamReader(new FileInputStream(file), Config.getMng().getEncoding());
                List<Line> lines = FileParser.toList(r);
                TextTools.prepare(lines);
                modulo.digest(file.getName(), lines);
                if (!modulo.getErrors().isEmpty()) {
                    pb.setExcepcion("{Fichero: " + file.getName() + "}\n" + TextTools.printStackTrace(modulo.getErrors().get(0)));
                    pb.setHash(null);
                    break;
                }
            }
            modulo.endDigest();
            modulo.addWarning(loadedFiles.size() + " ficheros procesados: " + loadedFiles);
            if (modulo.getErrors().isEmpty()) {
                pb.setExcepcion(null);
                pb.setHash(modulo.getHash());
            }
            cache.putInCache(pb.getUsuario() + "/" + pb.getAlias(), modulo);
            ProyectoManager.getInstance().save(pb);
            return modulo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
