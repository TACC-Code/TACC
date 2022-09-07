class BackupThread extends Thread {
        protected synchronized void actBigSnap() {
            final float ratio = CtuluLibImage.getRatio(src_.w_, src_.h_, defaultBigSnapshotSize_);
            final BufferedImage im = src_.getImage(new Rectangle(0, 0, src_.w_, src_.h_), ratio, ratio, false);
            if (isFileModified() && snapshot_ != null) {
                snapshot_ = null;
            }
            updateFileTime();
            setBigSnapshot(new SoftReference<BufferedImage>(im), CtuluLibImage.getRatio(im.getWidth(), src_.w_));
            ImageWriter writer = null;
            writer = ImageIO.getImageWriter(src_.reader_);
            setFileFinish(false);
            File f = tmpBigSnapshot_;
            if (f == null) {
                try {
                    f = CtuluCacheFile.createTempFile("img512", src_.file_.getName());
                } catch (final IOException _evt) {
                    FuLog.error(_evt);
                }
            }
            if (f == null) {
                tmpBigSnapshot_ = null;
            } else {
                boolean ok = true;
                if (writer == null) {
                    if (tmpBigSnapshot_ != null) {
                        tmpBigSnapshot_.delete();
                    }
                    f = new File(f.getAbsolutePath() + ".png");
                    writer = CtuluLibImage.getImageWriter(f);
                } else {
                    try {
                        writer.setOutput(ImageIO.createImageOutputStream(f));
                    } catch (final IOException _evt) {
                        ok = false;
                        FuLog.error(_evt);
                    }
                }
                if (ok) {
                    try {
                        writer.write(im);
                    } catch (final IOException _evt) {
                        FuLog.error(_evt);
                        ok = false;
                    }
                }
                if (ok) {
                    tmpBigSnapshot_ = f;
                } else {
                    if (tmpBigSnapshot_ != null) {
                        tmpBigSnapshot_.delete();
                    }
                    tmpBigSnapshot_ = null;
                }
            }
            setFileFinish(true);
        }
}
