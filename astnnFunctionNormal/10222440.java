class BackupThread extends Thread {
    protected void sortFramesByHeight(JInternalFrame[] _frames) {
        sortFramesByTitle(_frames);
        JInternalFrame tmp;
        int i, l;
        l = _frames.length;
        for (i = 0; i + 1 < l; i++) if (_frames[i].getHeight() > _frames[i + 1].getHeight()) {
            tmp = _frames[i];
            _frames[i] = _frames[i + 1];
            _frames[i + 1] = tmp;
            i--;
            if (i >= 0) i--;
        }
    }
}
