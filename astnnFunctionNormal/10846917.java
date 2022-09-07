class BackupThread extends Thread {
    private static void makeExtraBackupCopies(File backupFile, String who, String[] locations) {
        if (backupFile == null || who == null || who.length() == 0 || locations == null || locations.length == 0) return;
        String filename = "backup-" + FileUtils.makeSafe(who) + ".zip";
        Set<File> copies = new HashSet<File>();
        for (int i = 0; i < locations.length; i++) {
            ThreadThrottler.tick();
            File copy = null;
            File oneLocation = new File(locations[i]);
            if (oneLocation.isDirectory()) copy = new File(oneLocation, filename); else if (oneLocation.getParentFile().isDirectory()) {
                String oneName = oneLocation.getName();
                if (!oneName.toLowerCase().endsWith(".zip")) oneName = oneName + ".zip";
                if (oneName.indexOf("%date") != -1) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    String now = fmt.format(new Date());
                    oneName = StringUtils.findAndReplace(oneName, "%date", now);
                }
                copy = new File(oneLocation.getParentFile(), oneName);
            }
            if (copy == null || copies.contains(copy)) continue;
            try {
                FileUtils.copyFile(backupFile, copy);
                copies.add(copy);
            } catch (Exception e) {
                System.err.println("Warning: unable to make extra backup to '" + copy + "'");
            }
        }
    }
}
