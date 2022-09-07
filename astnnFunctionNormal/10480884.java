class BackupThread extends Thread {
    public void put(Rectangle2D ext) {
        if ((ext != null) && ((num < 1) || (ext != extents[num - 1]))) {
            if (num < (NUMREC)) {
                extents[num] = ext;
                num = num + 1;
            } else {
                for (int i = 0; i < (NUMREC - 1); i++) {
                    extents[i] = extents[i + 1];
                }
                extents[num - 1] = ext;
            }
        }
    }
}
