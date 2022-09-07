class BackupThread extends Thread {
    public static void prune(String oldCatalogFile, String newCatalogFile) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Problem getting SHA-256 digest: " + e);
            return;
        }
        ObjectOutputStream newOutput = null;
        try {
            FileOutputStream fos = new FileOutputStream(newCatalogFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 4096 * 4);
            newOutput = new ObjectOutputStream(bos);
        } catch (IOException e) {
            System.out.println("Problem openning newCatalogFile: " + e);
        }
        ArrayList<File> allFiles = null;
        try {
            FileInputStream fis = new FileInputStream(oldCatalogFile);
            BufferedInputStream bis = new BufferedInputStream(fis, 4096 * 4);
            ObjectInputStream ois = new ObjectInputStream(bis);
            allFiles = (ArrayList<File>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            System.out.println("Problem openning oldCatalogFile: " + e);
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("Problem reading catalog from [" + oldCatalogFile + "]: " + e);
            return;
        }
        System.out.println("Found " + allFiles.size() + " PDF files to process");
        ArrayList<File> prunedFiles = new ArrayList<File>(Math.max(allFiles.size(), 1));
        HashMap<FileHash, String> hash2path = new HashMap<FileHash, String>(allFiles.size());
        HashMap<String, ArrayList<DuplicateEntry>> duplicatePaths = new HashMap<String, ArrayList<DuplicateEntry>>();
        byte[] buffer = new byte[8 * 1024];
        int numMissing = 0;
        int numDuplicates = 0;
        long then = 0;
        for (int i = 0; i < allFiles.size(); i++) {
            File file = allFiles.get(i);
            String path = file.getAbsolutePath();
            long now = System.currentTimeMillis();
            if ((now - then) >= 5000L) {
                then = now;
                System.out.println("Commencing processing file " + (i + 1) + " of " + allFiles.size());
            }
            if (!file.exists()) {
                System.out.println("Removed non-existant '" + path + "'");
                numMissing++;
                continue;
            }
            try {
                FileInputStream fis = new FileInputStream(file);
                digest.reset();
                while (true) {
                    int read = fis.read(buffer);
                    if (read < 0) break;
                    digest.update(buffer, 0, read);
                }
                fis.close();
            } catch (IOException e) {
                System.out.println("Problem hashing '" + path + "' : " + e);
            }
            FileHash fh = new FileHash(digest.digest(), file.length());
            String originalPath = hash2path.get(fh);
            if (originalPath == null) {
                hash2path.put(fh, path);
                prunedFiles.add(file);
            } else {
                ArrayList<DuplicateEntry> duplicates = duplicatePaths.get(originalPath);
                if (duplicates == null) {
                    duplicates = new ArrayList<DuplicateEntry>();
                    duplicatePaths.put(originalPath, duplicates);
                }
                DuplicateEntry de = new DuplicateEntry(originalPath, path, i);
                duplicates.add(de);
                numDuplicates++;
            }
        }
        prunedFiles.trimToSize();
        System.out.println("Finished processing " + allFiles.size() + " PDF files. Found " + numDuplicates + " duplicates, and " + numMissing + " missing.");
        for (File file : prunedFiles) {
            System.out.println(file.getAbsolutePath());
        }
        try {
            newOutput.writeObject(prunedFiles);
            newOutput.flush();
            newOutput.close();
        } catch (IOException e) {
            System.out.println("Problem saving newCatalogFile: " + e);
        }
    }
}
