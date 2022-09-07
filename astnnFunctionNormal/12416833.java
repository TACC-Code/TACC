class BackupThread extends Thread {
    private boolean saveSettings(boolean commit) {
        InitToolGameSettings settings = frame.getInitToolSettings();
        boolean change = false;
        String gameName = getString(GAME_NAME);
        if (gameName == null) {
            throw new IllegalStateException("A game name is required but none was set.");
        }
        if (!gameName.equals(settings.getGameName())) change = true;
        URL url = null;
        if (getPanel().getRadioButton(NEW_FILE_FLAG).isSelected()) {
            try {
                url = fileSelector.getUrl();
                change = true;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("The new url is invalid.");
            }
        }
        String initDisplay = getPanel().getText(INIT_DISPLAY_PROP);
        if (initDisplay == null || (initDisplay = initDisplay.trim()).length() == 0) initDisplay = null;
        checkPropertyName(initDisplay, "The initiative display property");
        if (initDisplay != settings.getInitDisplayProperty() && (initDisplay == null || !initDisplay.equals(settings.getInitDisplayProperty()))) change = true;
        String modDisplay = getPanel().getText(MODIFIER_DISPLAY_PROP);
        if (modDisplay == null || (modDisplay = modDisplay.trim()).length() == 0) modDisplay = null;
        checkPropertyName(modDisplay, "The initiative modifier display property");
        if (modDisplay != settings.getInitModifierDisplayProperty() && (modDisplay == null || !modDisplay.equals(settings.getInitModifierDisplayProperty()))) change = true;
        scripts.flushScript(null);
        change |= scripts.isChanged();
        change |= getPanel().getBoolean(INIT_EVERY_ROUND) != settings.isInitEachRound();
        if (!commit) return change;
        if (url != null) {
            try {
                File file = new File(System.getProperty("java.io.tmpdir") + File.separator + new GUID() + ".rpdat");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                InputStream is = url.openStream();
                FileUtil.copy(is, os);
                frame.getSettingsFile().importCombatantLookupFile(PropertySettings.getInstance().getDatabaseName(), file);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to copy the url file: " + url.toExternalForm(), e);
                throw new IllegalArgumentException("Unable to copy URL " + url.toExternalForm());
            }
        } else if (getPanel().getRadioButton(NO_FILE_FLAG).isSelected() && settings.isCombatantLookupAvailable()) {
            frame.getSettingsFile().removeCombatantLookupFile(PropertySettings.getInstance().getDatabaseName());
        }
        settings.getCustomPropertySet().setGame(gameName);
        settings.setInitEachRound(getPanel().getBoolean(INIT_EVERY_ROUND));
        settings.setInitDisplayProperty(initDisplay);
        settings.setInitModifierDisplayProperty(modDisplay);
        workingCombatantLookup = null;
        scripts.saveScripts(settings.getEventScripts());
        return change;
    }
}
