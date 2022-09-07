class BackupThread extends Thread {
    public void tick() {
        Boolean detectInactivity = (Boolean) Settings.getDefault().getSetting("detectInactivity");
        if (detectInactivity.booleanValue()) {
            int inactivityTime = Integer.parseInt((String) Settings.getDefault().getSetting("inactivityTime"));
            if ((Tools.getInactivity() > inactivityTime * 60 * 1000) && (System.getProperty("inactivityReminderOpen") == null)) {
                Tools.recordActivity();
                SystemTray systemTray = SystemTray.getSystemTray();
                TrayIcon[] trayIcons = systemTray.getTrayIcons();
                for (int i = 0; i < trayIcons.length; i++) {
                    TrayIcon trayIcon = trayIcons[i];
                    if (trayIcon.getToolTip().startsWith(Tools.title)) trayIcon.displayMessage(Translator.getTranslation("WARNING.WARNING_TITLE"), Translator.getTranslation("INACTIVITYDIALOG.LBL_INACTIVITY_MESSAGE", new String[] { (String) Settings.getDefault().getSetting("inactivityTime") }), TrayIcon.MessageType.WARNING);
                }
                String inactivityAction = (String) Settings.getDefault().getSetting("inactivityAction");
                if (!inactivityAction.equals(Settings.ON_INACTIVITY_NOTIFY)) {
                    EventQueue.invokeLater(new Runnable() {

                        DayView dayView = (DayView) tpViews.getComponentAt(TAB_DAY_VIEW);

                        public void run() {
                            new InactivityReminderDialog(dayView).setVisible(true);
                        }
                    });
                }
            }
        }
        Boolean reportActivity = (Boolean) Settings.getDefault().getSetting("reportActivity");
        if (!reportActivity.booleanValue()) return;
        Calendar calendar = Calendar.getInstance();
        final int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        String reportedWeek = (String) Settings.getDefault().getSetting("rachota.reported.week");
        if (reportedWeek != null) {
            int week = Integer.parseInt(reportedWeek);
            if (week == currentWeek) return;
            if (week == -1) return;
        }
        String RID = Tools.getRID();
        Plan plan = Plan.getDefault();
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek - 1);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Day day = plan.getDay(calendar.getTime());
        long totalTime = 0;
        long idleTime = 0;
        long privateTime = 0;
        for (int i = 0; i < 7; i++) {
            Iterator tasks = day.getTasks().iterator();
            while (tasks.hasNext()) {
                Task task = (Task) tasks.next();
                if (task.isIdleTask()) idleTime = idleTime + task.getDuration(); else if (task.privateTask()) privateTime = privateTime + task.getDuration(); else totalTime = totalTime + task.getDuration();
            }
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            day = plan.getDay(calendar.getTime());
        }
        String userDir = (String) Settings.getDefault().getSetting("userDir");
        File[] diaries = new File(userDir).listFiles(new FileFilter() {

            public boolean accept(File file) {
                String name = file.getName();
                return (name.startsWith("diary_") && (name.endsWith(".xml")));
            }
        });
        final AnalyticsView analyticsView = (AnalyticsView) tpViews.getComponentAt(TAB_ANALYTICS_VIEW);
        String WUT = "" + totalTime + "|" + idleTime + "|" + privateTime + "|" + diaries.length + "|" + analyticsView.getWeeklyAnalysis();
        try {
            RID = URLEncoder.encode(RID, "UTF-8");
            WUT = URLEncoder.encode(WUT, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error: Can't build URL to Rachota Analytics server.");
            e.printStackTrace();
        }
        final String url_string = "http://rachota.sourceforge.net/reportActivity.php?rid=" + RID + "&wut=" + WUT;
        final Thread connectionThread = new Thread("Rachota Analytics Reporter") {

            public void run() {
                try {
                    if (reportingActivity) return;
                    reportingActivity = true;
                    URL url = new URL(url_string);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.getResponseMessage();
                    connection.disconnect();
                    Settings.getDefault().setSetting("rachota.reported.week", "" + currentWeek);
                    analyticsView.updateChart();
                    reportingActivity = false;
                } catch (Exception e) {
                    System.out.println("Error: Can't connect to Rachota Analytics server.");
                    Settings.getDefault().setSetting("rachota.reported.week", "-1");
                    reportingActivity = false;
                }
            }
        };
    }
}
