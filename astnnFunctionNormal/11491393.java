class BackupThread extends Thread {
        public void doAction() {
            final Media media = (Media) getMediaList().getSelectedValue();
            final String oldCaption = media.getCaption();
            final String oldCredit = media.getCredit();
            final String oldType = media.getType();
            final String oldUrl = media.getUrl();
            final boolean oldIsPrimary = media.isPrimary();
            final MediaViewPanel panel = getMediaViewPanel();
            media.setCaption(panel.getCaptionArea().getText());
            media.setCredit(panel.getCreditArea().getText());
            media.setType((String) panel.getTypeComboBox().getSelectedItem());
            media.setPrimary(panel.getPrimaryCheckBox().isSelected());
            URL url = null;
            try {
                url = new URL(panel.getUrlField().getText());
                final InputStream in = url.openStream();
                final int b = in.read();
                if (b == -1) {
                    AppFrameDispatcher.showWarningDialog("Unable to read from " + url.toExternalForm());
                } else {
                    media.setUrl(url.toExternalForm());
                }
            } catch (Exception e1) {
                final String s = "Failed to open URL, " + panel.getUrlField().getText() + ". The URL will not be updated.";
                AppFrameDispatcher.showWarningDialog(s);
            }
            IMedia oldPrimaryMedia = null;
            if (media.isPrimary()) {
                final IConcept concept = media.getConceptDelegate().getConcept();
                oldPrimaryMedia = concept.getPrimaryMedia(media.getType());
                if (oldPrimaryMedia != null && !oldPrimaryMedia.equals(media)) {
                    log.info("You are adding a primary media of '" + media.getUrl() + "' to " + concept.getPrimaryConceptNameAsString() + ". This concept contained a primary media of '" + oldPrimaryMedia.getUrl() + "' which is now set to a secondary media");
                    oldPrimaryMedia.setPrimary(false);
                }
            }
            try {
                ConceptDelegateDAO.getInstance().update((Media) media.getConceptDelegate());
                getMediaList().paintImmediately(getMediaList().getBounds());
            } catch (DAOException e) {
                log.warn("Failed to update " + media + " in the database", e);
                media.setCaption(oldCaption);
                media.setCredit(oldCredit);
                media.setType(oldType);
                media.setUrl(oldUrl);
                media.setPrimary(oldIsPrimary);
                oldPrimaryMedia.setPrimary(true);
                AppFrameDispatcher.showErrorDialog("Failed to write changes to the database. Rolling back to original values");
            }
        }
}
