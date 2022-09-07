class BackupThread extends Thread {
    public BrowserContext(String _id, Browser _browser, Control _widgetWaitingIndicator, boolean _forceVisibleAfterLoad) {
        super(_id, null);
        browser = _browser;
        forceVisibleAfterLoad = _forceVisibleAfterLoad;
        widgetWaitIndicator = _widgetWaitingIndicator;
        messageDispatcherSWT = new MessageDispatcherSWT(this);
        setMessageDispatcher(messageDispatcherSWT);
        final TimerEventPerformer showBrowersPerformer = new TimerEventPerformer() {

            public void perform(TimerEvent event) {
                if (browser != null && !browser.isDisposed()) {
                    Utils.execSWTThread(new AERunnable() {

                        public void runSupport() {
                            if (forceVisibleAfterLoad && browser != null && !browser.isDisposed() && !browser.isVisible()) {
                                browser.setVisible(true);
                            }
                        }
                    });
                }
            }
        };
        final TimerEventPerformer hideIndicatorPerformer = new TimerEventPerformer() {

            public void perform(TimerEvent event) {
                setPageLoading(false, browser.getUrl());
                if (widgetWaitIndicator != null && !widgetWaitIndicator.isDisposed()) {
                    Utils.execSWTThread(new AERunnable() {

                        public void runSupport() {
                            if (widgetWaitIndicator != null && !widgetWaitIndicator.isDisposed()) {
                                widgetWaitIndicator.setVisible(false);
                            }
                        }
                    });
                }
            }
        };
        final TimerEventPerformer checkURLEventPerformer = new TimerEventPerformer() {

            public void perform(TimerEvent event) {
                if (browser != null && !browser.isDisposed()) {
                    Utils.execSWTThreadLater(0, new AERunnable() {

                        public void runSupport() {
                            if (browser != null && !browser.isDisposed()) {
                                browser.execute("try { " + "tuxLocString = document.location.toString();" + "if (tuxLocString.indexOf('res://') == 0) {" + "  document.title = 'err: ' + tuxLocString;" + "} else {" + "  tuxTitleString = document.title.toString();" + "  if (tuxTitleString.indexOf('408 ') == 0 || tuxTitleString.indexOf('503 ') == 0 || tuxTitleString.indexOf('500 ') == 0) " + "  { document.title = 'err: ' + tuxTitleString; } " + "}" + "} catch (e) { }");
                            }
                        }
                    });
                }
            }
        };
        if (forceVisibleAfterLoad) {
            browser.setVisible(false);
        }
        setPageLoading(false, browser.getUrl());
        if (widgetWaitIndicator != null && !widgetWaitIndicator.isDisposed()) {
            widgetWaitIndicator.setVisible(false);
        }
        browser.addTitleListener(new TitleListener() {

            public void changed(TitleEvent event) {
                if (browser.isDisposed() || browser.getShell().isDisposed()) {
                    return;
                }
                if (!browser.isVisible()) {
                    SimpleTimer.addEvent("Show Browser", System.currentTimeMillis() + 700, showBrowersPerformer);
                }
                if (event.title.startsWith("err: ")) {
                    fillWithRetry(event.title, "err in title");
                }
            }
        });
        browser.addProgressListener(new ProgressListener() {

            public void changed(ProgressEvent event) {
            }

            public void completed(ProgressEvent event) {
                if (browser.isDisposed() || browser.getShell().isDisposed()) {
                    return;
                }
                checkURLEventPerformer.perform(null);
                if (forceVisibleAfterLoad && !browser.isVisible()) {
                    browser.setVisible(true);
                }
                browser.execute("try { if (azureusClientWelcome) { azureusClientWelcome('" + ConstantsVuze.AZID + "'," + "{ 'azv':'" + org.gudy.azureus2.core3.util.Constants.AZUREUS_VERSION + "', 'browser-id':'" + getID() + "' }" + ");} } catch (e) { }");
                if (org.gudy.azureus2.core3.util.Constants.isCVSVersion() || System.getProperty("debug.https", null) != null) {
                    if (browser.getUrl().indexOf("https") == 0) {
                        browser.execute("try { o = document.getElementsByTagName('body'); if (o) o[0].style.borderTop = '2px dotted #3b3b3b'; } catch (e) {}");
                    }
                }
                if (wiggleBrowser) {
                    Shell shell = browser.getShell();
                    Point size = shell.getSize();
                    size.x -= 1;
                    size.y -= 1;
                    shell.setSize(size);
                    size.x += 1;
                    size.y += 1;
                    shell.setSize(size);
                }
            }
        });
        checkURLEvent = SimpleTimer.addPeriodicEvent("checkURL", 10000, checkURLEventPerformer);
        browser.addOpenWindowListener(new OpenWindowListener() {

            public void open(WindowEvent event) {
                if (browser.isDisposed() || browser.getShell().isDisposed()) {
                    return;
                }
                event.required = true;
                if (browser.getUrl().contains("js.debug=1")) {
                    Shell shell = ShellFactory.createMainShell(SWT.SHELL_TRIM);
                    shell.setLayout(new FillLayout());
                    Browser subBrowser = new Browser(shell, Utils.getInitialBrowserStyle(SWT.NONE));
                    shell.open();
                    event.browser = subBrowser;
                } else {
                    final Browser subBrowser = new Browser(browser, Utils.getInitialBrowserStyle(SWT.NONE));
                    subBrowser.addLocationListener(new LocationListener() {

                        public void changed(LocationEvent arg0) {
                        }

                        public void changing(LocationEvent event) {
                            event.doit = false;
                            if (allowPopups() && !UrlFilter.getInstance().urlIsBlocked(event.location) && (event.location.startsWith("http://") || event.location.startsWith("https://"))) {
                                debug("open sub browser: " + event.location);
                                Program.launch(event.location);
                            } else {
                                debug("blocked open sub browser: " + event.location);
                            }
                            Utils.execSWTThreadLater(0, new AERunnable() {

                                public void runSupport() {
                                    subBrowser.dispose();
                                }
                            });
                        }
                    });
                    event.browser = subBrowser;
                }
            }
        });
        browser.addLocationListener(new LocationListener() {

            private TimerEvent timerevent;

            public void changed(LocationEvent event) {
                if (browser.isDisposed() || browser.getShell().isDisposed()) {
                    return;
                }
                debug("browser.changed " + event.location);
                if (timerevent != null) {
                    timerevent.cancel();
                }
                checkURLEventPerformer.perform(null);
                setPageLoading(false, event.top ? event.location : null);
                if (widgetWaitIndicator != null && !widgetWaitIndicator.isDisposed()) {
                    widgetWaitIndicator.setVisible(false);
                }
                if (!event.top) {
                    return;
                }
                String location = event.location.toLowerCase();
                boolean isWebURL = location.startsWith("http://") || location.startsWith("https://");
                if (!isWebURL) {
                    if (event.location.startsWith("res://")) {
                        fillWithRetry(event.location, "top changed");
                        return;
                    }
                }
                if (UrlFilter.getInstance().isWhitelisted(event.location)) {
                    lastValidURL = event.location;
                }
            }

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
        });
        browser.setData(CONTEXT_KEY, this);
        browser.addDisposeListener(this);
        final boolean enableMenu = System.getProperty(KEY_ENABLE_MENU, "0").equals("1");
        browser.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                event.doit = enableMenu;
            }
        });
        messageDispatcherSWT.registerBrowser(browser);
        this.display = browser.getDisplay();
    }
}
