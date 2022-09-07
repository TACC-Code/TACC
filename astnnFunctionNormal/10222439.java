class BackupThread extends Thread {
    protected void sortFramesByTitle(JInternalFrame[] _frames) {
        JInternalFrame tmp;
        int i, l;
        l = _frames.length;
        for (i = 0; i + 1 < l; i++) {
            String t1 = _frames[i].getTitle();
            String t2 = _frames[i + 1].getTitle();
            if ((t1 != null) && (t2 != null) && (t1.compareTo(t2) > 0)) {
                tmp = _frames[i];
                _frames[i] = _frames[i + 1];
                _frames[i + 1] = tmp;
                i--;
                if (i >= 0) i--;
            }
        }
    }
}
