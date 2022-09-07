class BackupThread extends Thread {
    public void paintComponent(final Graphics _g) {
        super.paintComponent(_g);
        if (boite_ == null) {
            return;
        }
        if (vvz_ == null) {
            return;
        }
        if (isRapide()) {
            final TraceGeometrie tg = new TraceGeometrie(getVersEcran());
            tg.setForeground(getForeground());
            tg.dessinePolygone((Graphics2D) _g, boite_, false, true);
            return;
        }
        final GrBoite clip = getClipEcran(_g);
        final GrPolygone r = boite_.applique(getVersEcran());
        final GrBoite b = r.boite();
        if (b.intersectionXY(clip) == null) {
            return;
        }
        BPaletteCouleur ligPalette = palette_;
        if ((surface_ && contour_) || (surface_ && isolignes_) || (isosurfaces_ && contour_) || (isosurfaces_ && isolignes_)) {
            ligPalette = new BPaletteCouleurSimple();
            ((BPaletteCouleurSimple) ligPalette).setCouleurMin(getForeground());
            ((BPaletteCouleurSimple) ligPalette).setCouleurMax(getForeground());
        }
        final TraceIsoLignes isol = new TraceIsoLignes(ecart_, ligPalette);
        final TraceIsoSurfaces isos = new TraceIsoSurfaces(ecart_, palette_);
        Gouraud tg = null;
        if (paramGouraud_ != null) {
            tg = new Gouraud(_g, paramGouraud_.getNiveau(), paramGouraud_.getTaille());
        }
        final GrPoint o = r.sommet(0);
        final GrVecteur pasX = r.sommet(1).soustraction(o).division(vvz_.length - 1);
        final GrVecteur pasY = r.sommet(3).soustraction(o).division(vvz_[0].length - 1);
        final int imin = 0;
        final int imax = vvz_.length - 1;
        final int jmin = 0;
        final int jmax = vvz_[0].length - 1;
        final int[] x = new int[4];
        final int[] y = new int[4];
        final Color[] c = new Color[4];
        final double[] v = new double[4];
        for (int i = imin; i < imax; i++) {
            final GrPoint pi = o.addition(pasX.multiplication(i));
            for (int j = jmin; j < jmax; j++) {
                final GrPoint p0 = pi.addition(pasY.multiplication(j));
                final GrPoint p1 = p0.addition(pasX);
                final GrPoint p2 = p1.addition(pasY);
                final GrPoint p3 = p0.addition(pasY);
                boolean dedans = false;
                if (clip.contientXY(p0)) {
                    dedans = true;
                }
                if (clip.contientXY(p1)) {
                    dedans = true;
                }
                if (clip.contientXY(p2)) {
                    dedans = true;
                }
                if (clip.contientXY(p3)) {
                    dedans = true;
                }
                if (dedans) {
                    x[0] = (int) p0.x_;
                    y[0] = (int) p0.y_;
                    c[0] = vvc_[i][j];
                    v[0] = vvv_[i][j];
                    x[1] = (int) p1.x_;
                    y[1] = (int) p1.y_;
                    c[1] = vvc_[i + 1][j];
                    v[1] = vvv_[i + 1][j];
                    x[2] = (int) p2.x_;
                    y[2] = (int) p2.y_;
                    c[2] = vvc_[i + 1][j + 1];
                    v[2] = vvv_[i + 1][j + 1];
                    x[3] = (int) p3.x_;
                    y[3] = (int) p3.y_;
                    c[3] = vvc_[i][j + 1];
                    v[3] = vvv_[i][j + 1];
                    if (surface_ && (!isosurfaces_)) {
                        if (tg == null) {
                            _g.setColor(c[0]);
                            _g.fillPolygon(x, y, x.length);
                        } else {
                            tg.fillRectangle(x, y, c);
                        }
                    }
                    if (isosurfaces_) {
                        isos.draw(_g, new Polygon(x, y, x.length), v);
                    }
                    if (contour_) {
                        _g.setColor(getBackground());
                        _g.drawPolygon(x, y, x.length);
                    }
                    if (isolignes_) {
                        isol.draw(_g, new Polygon(x, y, x.length), v);
                    }
                }
            }
        }
    }
}
