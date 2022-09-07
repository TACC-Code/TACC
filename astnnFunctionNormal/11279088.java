class BackupThread extends Thread {
    public void spider(Site site, SpiderProgressListener progress) {
        synchronized (sitesLock) {
            if (!sites.contains(site)) sites.add(site);
        }
        for (ListIterator it = site.urls.listIterator(); it.hasNext(); ) {
            String url = (String) it.next();
            synchronized (progress) {
                progress.setProgressText("Spidering " + url + "...");
            }
            try {
                HttpURLConnection.setFollowRedirects(false);
                URLConnection connection = new URL(url).openConnection();
                connection.setAllowUserInteraction(false);
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode >= 300 && responseCode < 400) {
                        String newLocation = httpConnection.getHeaderField("Location");
                        if (null != newLocation && !"".equals(newLocation)) {
                            processIdentityLink(site, it, newLocation);
                        }
                        continue;
                    } else if (responseCode != 200) {
                        continue;
                    }
                }
                Parser parser = new Parser(connection);
                NodeList list = parser.extractAllNodesThatMatch(NODE_FILTER);
                for (int i = 0; i < list.size(); i++) {
                    Node tag = list.elementAt(i);
                    if (tag instanceof TitleTag) {
                        processTitleTag(site, (TitleTag) tag);
                    } else if (tag instanceof FrameTag) {
                        processIdentityLink(site, it, ((FrameTag) tag).getFrameLocation().trim());
                    } else {
                        processLink(site, it, (LinkTag) tag);
                    }
                }
            } catch (ParserException e) {
            } catch (IOException e) {
            } catch (RuntimeException e) {
            }
        }
    }
}
