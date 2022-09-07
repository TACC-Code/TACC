class BackupThread extends Thread {
            public void changing(LocationEvent event) {
                debug("browser.changing " + event.location);
                if (browser.isDisposed() || browser.getShell().isDisposed()) {
                    return;
                }
                String event_location = event.location;
                if (event_location.startsWith("javascript") && event_location.indexOf("back()") > 0) {
                    if (browser.isBackEnabled()) {
                        browser.back();
                    } else if (lastValidURL != null) {
                        fillWithRetry(event_location, "back");
                    }
                    return;
                }
                String lowerLocation = event_location.toLowerCase();
                boolean isOurURI = lowerLocation.startsWith("magnet:") || lowerLocation.startsWith("vuze:") || lowerLocation.startsWith("bc:") || lowerLocation.startsWith("bctp:") || lowerLocation.startsWith("dht:");
                if (isOurURI) {
                    event.doit = false;
                    TorrentOpener.openTorrent(event_location);
                    return;
                }
                boolean isWebURL = lowerLocation.startsWith("http://") || lowerLocation.startsWith("https://");
                if (!isWebURL) {
                    return;
                }
                boolean blocked = UrlFilter.getInstance().urlIsBlocked(event_location);
                if (blocked) {
                    event.doit = false;
                    new MessageBoxShell(SWT.OK, "URL blocked", "Tried to open " + event_location + " but it's blocked").open(null);
                    browser.back();
                } else {
                    if (UrlFilter.getInstance().isWhitelisted(event_location)) {
                        lastValidURL = event_location;
                    }
                    setPageLoading(true, event.location);
                    if (event.top) {
                        if (widgetWaitIndicator != null && !widgetWaitIndicator.isDisposed()) {
                            widgetWaitIndicator.setVisible(true);
                        }
                        timerevent = SimpleTimer.addEvent("Hide Indicator", System.currentTimeMillis() + 20000, hideIndicatorPerformer);
                    } else {
                        boolean isTorrent = false;
                        boolean isVuzeFile = false;
                        if (event_location.endsWith(".torrent")) {
                            isTorrent = true;
                        } else {
                            boolean can_rpc = UrlFilter.getInstance().urlCanRPC(event_location);
                            boolean test_for_torrent = !can_rpc && event_location.indexOf(".htm") == -1;
                            boolean test_for_vuze = can_rpc && (event_location.endsWith(".xml") || event_location.endsWith(".vuze"));
                            if (test_for_torrent || test_for_vuze) {
                                try {
                                    URL url = new URL(event_location);
                                    URLConnection conn = url.openConnection();
                                    ((HttpURLConnection) conn).setRequestMethod("HEAD");
                                    String referer_str = null;
                                    try {
                                        URL referer = new URL(((Browser) event.widget).getUrl());
                                        if (referer != null) {
                                            referer_str = referer.toExternalForm();
                                        }
                                    } catch (Throwable e) {
                                    }
                                    UrlUtils.setBrowserHeaders(conn, referer_str);
                                    UrlUtils.connectWithTimeouts(conn, 1500, 5000);
                                    String contentType = conn.getContentType();
                                    if (contentType != null) {
                                        if (test_for_torrent && contentType.indexOf("torrent") != -1) {
                                            isTorrent = true;
                                        }
                                        if (test_for_vuze && contentType.indexOf("vuze") != -1) {
                                            isVuzeFile = true;
                                        }
                                    }
                                    String contentDisposition = conn.getHeaderField("Content-Disposition");
                                    if (contentDisposition != null) {
                                        if (test_for_torrent && contentDisposition.indexOf(".torrent") != -1) {
                                            isTorrent = true;
                                        }
                                        if (test_for_vuze && contentDisposition.indexOf(".vuze") != -1) {
                                            isVuzeFile = true;
                                        }
                                    }
                                } catch (Throwable e) {
                                }
                            }
                        }
                        if (isTorrent) {
                            event.doit = false;
                            try {
                                String referer_str = null;
                                try {
                                    referer_str = new URL(((Browser) event.widget).getUrl()).toExternalForm();
                                } catch (Throwable e) {
                                }
                                Map headers = UrlUtils.getBrowserHeaders(referer_str);
                                String cookies = (String) ((Browser) event.widget).getData("current-cookies");
                                if (cookies != null) {
                                    headers.put("Cookie", cookies);
                                }
                                String url = event_location;
                                if (torrentURLHandler != null) {
                                    try {
                                        torrentURLHandler.handleTorrentURL(url);
                                    } catch (Throwable e) {
                                        Debug.printStackTrace(e);
                                    }
                                }
                                PluginInitializer.getDefaultInterface().getDownloadManager().addDownload(new URL(url), headers);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        } else if (isVuzeFile) {
                            event.doit = false;
                            try {
                                String referer_str = null;
                                try {
                                    referer_str = new URL(((Browser) event.widget).getUrl()).toExternalForm();
                                } catch (Throwable e) {
                                }
                                Map headers = UrlUtils.getBrowserHeaders(referer_str);
                                String cookies = (String) ((Browser) event.widget).getData("current-cookies");
                                if (cookies != null) {
                                    headers.put("Cookie", cookies);
                                }
                                ResourceDownloader rd = StaticUtilities.getResourceDownloaderFactory().create(new URL(event_location));
                                VuzeFileHandler vfh = VuzeFileHandler.getSingleton();
                                VuzeFile vf = vfh.loadVuzeFile(rd.download());
                                if (vf == null) {
                                    event.doit = true;
                                } else {
                                    vfh.handleFiles(new VuzeFile[] { vf }, 0);
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
}
