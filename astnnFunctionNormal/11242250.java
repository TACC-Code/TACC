class BackupThread extends Thread {
    private FullUpdateInfo getAvailableUpdates() throws IOException {
        FullUpdateInfo retValue = new FullUpdateInfo();
        boolean romException = false;
        HttpClient romHttpClient = new DefaultHttpClient();
        HttpClient themeHttpClient = new DefaultHttpClient();
        HttpEntity romResponseEntity = null;
        HttpEntity themeResponseEntity = null;
        systemRom = SysUtils.getModVersion();
        if (Customization.Screenshotsupport) themeInfos = mPreferences.getThemeInformations();
        showExperimentalRomUpdates = mPreferences.showExperimentalRomUpdates();
        showAllRomUpdates = mPreferences.showAllRomUpdates();
        boolean ThemeUpdateUrlSet = false;
        if (Customization.Screenshotsupport) {
            showExperimentalThemeUpdates = mPreferences.showExperimentalThemeUpdates();
            showAllThemeUpdates = mPreferences.showAllThemeUpdates();
            ThemeUpdateUrlSet = mPreferences.ThemeUpdateUrlSet();
            if (themeInfos == null || themeInfos.name.equalsIgnoreCase(Constants.UPDATE_INFO_WILDCARD)) {
                if (showDebugOutput) Log.d(TAG, "Wildcard is used for Theme Updates");
                themeInfos = new ThemeInfo();
                WildcardUsed = true;
            }
        }
        try {
            URI RomUpdateServerUri = URI.create(mPreferences.getRomUpdateFileURL());
            HttpUriRequest romReq = new HttpGet(RomUpdateServerUri);
            romReq.addHeader("Cache-Control", "no-cache");
            HttpResponse romResponse = romHttpClient.execute(romReq);
            int romServerResponse = romResponse.getStatusLine().getStatusCode();
            if (romServerResponse != HttpStatus.SC_OK) {
                if (showDebugOutput) Log.d(TAG, "Server returned status code for ROM " + romServerResponse);
                romException = true;
            }
            if (!romException) romResponseEntity = romResponse.getEntity();
        } catch (IllegalArgumentException e) {
            if (showDebugOutput) Log.d(TAG, "Rom Update URI wrong: " + mPreferences.getRomUpdateFileURL());
            romException = true;
        }
        if (Customization.Screenshotsupport && ThemeUpdateUrlSet) {
            try {
                LinkedList<ThemeList> tl = mPreferences.getThemeUpdateUrls();
                for (ThemeList t : tl) {
                    if (!t.enabled) {
                        if (showDebugOutput) Log.d(TAG, "Theme " + t.name + " disabled. Continuing");
                        continue;
                    }
                    PrimaryKeyTheme = -1;
                    if (showDebugOutput) Log.d(TAG, "Trying to download ThemeInfos for " + t.url.toString());
                    URI ThemeUpdateServerUri = t.url;
                    HttpUriRequest themeReq = new HttpGet(ThemeUpdateServerUri);
                    themeReq.addHeader("Cache-Control", "no-cache");
                    try {
                        HttpResponse themeResponse = themeHttpClient.execute(themeReq);
                        int themeServerResponse = themeResponse.getStatusLine().getStatusCode();
                        if (themeServerResponse != HttpStatus.SC_OK) {
                            if (showDebugOutput) Log.d(TAG, "Server returned status code for Themes " + themeServerResponse);
                            themeResponseEntity = themeResponse.getEntity();
                            continue;
                        }
                        themeResponseEntity = themeResponse.getEntity();
                    } catch (IOException ex) {
                        DisplayExceptionToast(getResources().getString(R.string.theme_download_exception) + t.name + ": " + ex.getMessage());
                        Log.e(TAG, "There was an error downloading Theme " + t.name + ": ", ex);
                        continue;
                    }
                    BufferedReader themeLineReader = new BufferedReader(new InputStreamReader(themeResponseEntity.getContent()), 2 * 1024);
                    StringBuffer themeBuf = new StringBuffer();
                    String themeLine;
                    while ((themeLine = themeLineReader.readLine()) != null) {
                        themeBuf.append(themeLine);
                    }
                    themeLineReader.close();
                    if (t.PrimaryKey > 0) PrimaryKeyTheme = t.PrimaryKey;
                    LinkedList<UpdateInfo> themeUpdateInfos = parseJSON(themeBuf, RomType.Update);
                    retValue.themes.addAll(getThemeUpdates(themeUpdateInfos));
                }
            } catch (IllegalArgumentException e) {
                if (showDebugOutput) Log.d(TAG, "Theme Update URI wrong");
            }
        }
        try {
            if (!romException) {
                PrimaryKeyTheme = -1;
                BufferedReader romLineReader = new BufferedReader(new InputStreamReader(romResponseEntity.getContent()), 2 * 1024);
                StringBuffer romBuf = new StringBuffer();
                String romLine;
                while ((romLine = romLineReader.readLine()) != null) {
                    romBuf.append(romLine);
                }
                romLineReader.close();
                LinkedList<UpdateInfo> romUpdateInfos = parseJSON(romBuf, RomType.Update);
                retValue.roms = getRomUpdates(romUpdateInfos);
                LinkedList<UpdateInfo> incrementalRomUpdateInfos = parseJSON(romBuf, RomType.IncrementalUpdate);
                retValue.incrementalRoms = getIncrementalRomUpdates(incrementalRomUpdateInfos);
            } else if (showDebugOutput) Log.d(TAG, "There was an Exception on Downloading the Rom JSON File");
        } finally {
            if (romResponseEntity != null) romResponseEntity.consumeContent();
            if (themeResponseEntity != null) themeResponseEntity.consumeContent();
        }
        FullUpdateInfo ful = FilterUpdates(retValue, State.loadState(this, showDebugOutput));
        if (!romException) State.saveState(this, retValue, showDebugOutput);
        return ful;
    }
}
