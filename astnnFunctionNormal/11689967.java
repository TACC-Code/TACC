class BackupThread extends Thread {
    public VersionInfo getLastVersion() {
        VersionInfo versionInfo = null;
        try {
            URL url = new URL(RELEASES_RSS);
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(br);
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = reader.getLocalName();
                    if (versionInfo != null && name.equals("title")) {
                        String versionStr = getTextElement(reader);
                        Pattern pattern = Pattern.compile(VERSION_REGEXP);
                        Matcher matcher = pattern.matcher(versionStr);
                        if (matcher.find()) {
                            versionStr = matcher.group(1);
                        }
                        versionInfo.setVersion(versionStr);
                        continue;
                    }
                    if (versionInfo != null && name.equals("pubDate")) {
                        versionInfo.setReleaseDate(getTextElement(reader));
                        continue;
                    }
                    if (versionInfo != null && name.equals("link")) {
                        versionInfo.setNotesLink(getTextElement(reader));
                        continue;
                    }
                    if (name.equals("item")) {
                        if (versionInfo == null) {
                            versionInfo = new VersionInfo();
                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (versionInfo != null) {
                versionInfo.setFilesLink("http://sourceforge.net/project/showfiles.php?group_id=132880");
            }
            setStatus(versionInfo);
        } catch (Exception e) {
            timeSlotTracker.errorLog(e);
        }
        return versionInfo;
    }
}
