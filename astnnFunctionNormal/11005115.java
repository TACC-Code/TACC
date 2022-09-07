class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            String errorText = validateInput(true);
            if (errorText.length() > 0) {
                JOptionPane.showMessageDialog(null, errorText, "Errors", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String profileName = (String) profilesCombo.getEditor().getItem();
            if (profileName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Please enter a profile name", "Error", JOptionPane.ERROR_MESSAGE);
            }
            Profile previousProfile = getProfile(profileName);
            if (previousProfile != null) {
                int response = JOptionPane.showConfirmDialog(null, "Profile \"" + profileName + "\" already exists. Overwrite?", "Please confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) return;
            }
            Profile profile;
            if (previousProfile != null) profile = previousProfile; else profile = new Profile();
            try {
                profile.setProfileName(profileName);
                profile.setAtlasName(atlasNameTextField.getText());
                profile.setMapSource(((TileSource) mapSourceCombo.getSelectedItem()).getName());
                profile.setLatitudeMax(latMaxTextField.getCoordinate());
                profile.setLatitudeMin(latMinTextField.getCoordinate());
                profile.setLongitudeMax(lonMaxTextField.getCoordinate());
                profile.setLongitudeMin(lonMinTextField.getCoordinate());
                boolean[] zoomLevels = new boolean[cbZoom.length];
                for (int i = 0; i < cbZoom.length; i++) {
                    zoomLevels[i] = cbZoom[i].isSelected();
                }
                profile.setZoomLevels(zoomLevels);
                profile.setTileSizeWidth(tileSizeWidth.getTileSize());
                profile.setTileSizeHeight(tileSizeHeight.getTileSize());
                if (previousProfile == null) {
                    profilesVector.addElement(profile);
                    profilesCombo.addItem(profileName);
                }
                PersistentProfiles.store(profilesVector);
                deleteProfileButton.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Saved profile " + profileName, "", JOptionPane.PLAIN_MESSAGE);
            } catch (ParseException exception) {
                log.error("", exception);
            }
        }
}
