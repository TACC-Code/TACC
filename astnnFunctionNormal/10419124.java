class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            if (!jAtlasTree.testAtlasContentValid()) return;
            Object selObject = profilesCombo.getEditor().getItem();
            String profileName = null;
            Profile profile = null;
            boolean profileInList = false;
            if (selObject instanceof Profile) {
                profile = (Profile) selObject;
                profileName = profile.getName();
                profileInList = true;
            } else profileName = (String) selObject;
            if (profileName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Please enter a profile name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            profile = new Profile(profileName);
            if (profile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Profile \"" + profileName + "\" already exists. Overwrite?", "Please confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) return;
            }
            if (jAtlasTree.save(profile)) {
                if (!profileInList) profilesCombo.addItem(profile);
                JOptionPane.showMessageDialog(null, "Profile \"" + profileName + "\" has been successfully saved", "Profile save", JOptionPane.PLAIN_MESSAGE);
            }
        }
}
