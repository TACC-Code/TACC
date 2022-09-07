class BackupThread extends Thread {
    public boolean writeBg(Jbgs bgs, float fscale, int bgrotate, int pi) {
        if (!bgs.isPdf) return false;
        reader = ((JbgsPdf) bgs).getReader();
        if (reader == null) return false;
        try {
            pg = writer.getImportedPage(reader, pi + 1);
            float ww = pg.getWidth();
            float hh = pg.getHeight();
            JbgsPdf pbgs = (JbgsPdf) bgs;
            int pr = pbgs.getRotation(pi);
            if ((pr != 0) || (bgrotate != 0)) {
                int rot = bgrotate + (pr / 90);
                rot = rot % 4;
                if (rot < 0) rot = rot + 4;
                int drot = -90 * rot;
                double theta = Math.toRadians(drot);
                int shiftup = 0;
                int shiftright = 0;
                if ((rot == 1) || (rot == 3)) {
                    float temp = hh;
                    hh = ww;
                    ww = temp;
                }
                if ((rot == 1) || (rot == 2)) shiftup = 1;
                if ((rot == 2) || (rot == 3)) shiftright = 1;
                cb.addTemplate(pg, (float) (fscale * Math.cos(theta)), (float) (fscale * Math.sin(theta)), (float) (-fscale * Math.sin(theta)), (float) (fscale * Math.cos(theta)), margX + (shiftright * fscale * ww), -margY + pageH - (fscale * hh) + (shiftup * fscale * hh));
                return true;
            }
            cb.addTemplate(pg, fscale, 0f, 0f, fscale, margX, -margY + pageH - (fscale * hh));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
