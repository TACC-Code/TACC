class BackupThread extends Thread {
    protected void affichageLabel() {
        URL urlimg = null;
        File fimg = null;
        final Element el = (Element) noeud;
        final String nomf = el.getAttribute(srcAttr);
        if (doc.fsave == null) {
            try {
                if (doc.furl != null) {
                    urlimg = new URL(doc.furl, nomf);
                    if (nomf.startsWith("symboles")) {
                        try {
                            InputStream stream = urlimg.openStream();
                            stream.close();
                        } catch (IOException ex) {
                            urlimg = JEFichier.class.getClassLoader().getResource(nomf);
                        }
                    }
                } else {
                    fimg = new File(nomf);
                    urlimg = fimg.toURI().toURL();
                }
            } catch (final MalformedURLException ex) {
                LOG.error("affichageLabel()", ex);
            }
        } else {
            fimg = new File(nomf);
            if (!fimg.isAbsolute()) fimg = new File(doc.fsave.getParent() + File.separatorChar + nomf);
            if (!fimg.exists() && nomf.startsWith("symboles")) fimg = new File(nomf);
            try {
                urlimg = fimg.toURI().toURL();
            } catch (final MalformedURLException ex) {
                LOG.error("affichageLabel()", ex);
            }
        }
        if (label == null) label = new JLabel();
        if (urlimg == null || (fimg != null && (!fimg.exists() || !fimg.isFile()))) {
            label.setText(getString("erreur.FichierNonTrouve") + ": " + nomf);
            label.setIcon(null);
            label.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        } else {
            Image img = Toolkit.getDefaultToolkit().createImage(urlimg);
            boolean erreur = false;
            if (img == null || !chargerImage(img)) erreur = true;
            if (!erreur && reduction) img = reduireImage(img);
            if (!erreur) {
                final ImageIcon icon = new ImageIcon(img);
                if (icon == null || icon.getImageLoadStatus() == MediaTracker.ABORTED || icon.getImageLoadStatus() == MediaTracker.ERRORED) erreur = true; else {
                    label.setText(null);
                    label.setIcon(icon);
                    label.setBorder(null);
                }
            }
            if (erreur) {
                label.setText(getString("erreur.AffichageImage") + ": " + nomf);
                label.setIcon(null);
                label.setBorder(BorderFactory.createLineBorder(Color.darkGray));
            }
        }
    }
}
