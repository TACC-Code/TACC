class BackupThread extends Thread {
    @Override
    public File saveImgToCache(MovieInfo info, ContentProvider provider) {
        File cached = null;
        final String url = provider.getImageUrl(info);
        if (url != null) {
            InputStream is = null;
            try {
                URL imgUrl = new URL(url);
                URLConnection urlC = imgUrl.openConnection();
                is = new BufferedInputStream(imgUrl.openStream());
                Date date = new Date(urlC.getLastModified());
                LOGGER.trace("Saving resource type: {}, modified on: {}", urlC.getContentType(), date);
                cached = getCacheFile(url);
                FileTools.writeToFile(is, cached);
                is.close();
                cached.setLastModified(date.getTime());
                if (settings.getSaveAlbumArt()) {
                    LOGGER.info("COVER URL: " + url);
                    File cover = null;
                    cover = getCacheFile(url);
                    Set<MovieLocation> locations = info.getMovie().getLocations();
                    for (MovieLocation l : locations) {
                        File save = new File(new File(l.getPath()), info.getMovie().getTitle() + "-cover-art.jpg");
                        try {
                            FileTools.copy(cover, save);
                        } catch (FileNotFoundException ex) {
                            LOGGER.warn(String.format("Could not save cover from %s to %s (%s)", cover.getAbsolutePath(), save.getAbsolutePath(), ex.getMessage()));
                        }
                    }
                }
            } catch (MalformedURLException ex) {
                LOGGER.error("Could not save image '" + url + "'", ex);
            } catch (IOException ex) {
                LOGGER.error("Could not save image '" + url + "'", ex);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        LOGGER.error("Could not close input stream", ex);
                    }
                }
            }
        }
        return cached;
    }
}
