class BackupThread extends Thread {
    public static void WebUpdate() {
        log = Log.get("AutoUpdate");
        Log.setAlert(Agenda.client);
        String basedir = FileTool.getBasePath();
        log.log("Pr�fe Verzeichnis " + basedir, Log.INFOS);
        File ldir = new File(basedir);
        String[] files = ldir.list();
        int n = -1;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].matches(filenameMask)) {
                    if ((n == -1) || (files[i].compareTo(files[n]) > 0)) {
                        n = i;
                    }
                }
            }
        }
        if (n == -1) {
            log.log("Keine Agenda-Jar gefunden! ", Log.ERRORS);
            Log.setAlert(null);
            return;
        }
        String local = files[n];
        log.log("Lokale Version: " + local, Log.DEBUGMSG);
        try {
            URL url = new URL(updateURL);
            URLConnection conn = url.openConnection();
            InputStream cont = (InputStream) conn.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(cont));
            String remote = br.readLine();
            log.log("Remote Version: " + remote, Log.DEBUGMSG);
            if (local.compareTo(remote) < 0) {
                PlainTextField plf = new PlainTextField("Es wurde eine neuere Version gefunden. Soll diese Version heruntergeladen werden? (Sie wird erst beim n�chsten Start aktiv)", true);
                if (SwingHelper.showConfirm("Neuere Version gefunden", plf, Agenda.client, SwingHelper.YESNO) == true) {
                    File fNew = new File(basedir + "upd_" + remote);
                    Downloader dl = new Downloader();
                    Agenda.setWaitCursor(true);
                    if (dl.download(downloadURL + remote, fNew, Agenda.client, true) != Downloader.OK) {
                        fNew.delete();
                        Agenda.setWaitCursor(false);
                        SwingHelper.alert(Agenda.client, "Abbruch", "Der Download wurde abgebrochen");
                        return;
                    }
                    Agenda.setWaitCursor(false);
                    File fDef = new File(basedir + remote);
                    JarFile jf = new JarFile(fNew);
                    Manifest mf = jf.getManifest();
                    Attributes att = mf.getMainAttributes();
                    String v = att.getValue("Implementation-Version");
                    jf.close();
                    String[] vs = v.split(" +");
                    VersionInfo vi = new VersionInfo(vs[0]);
                    VersionInfo vMine = new VersionInfo(Agenda.Version);
                    String question = "";
                    if (vi.maior().compareTo(vMine.maior()) > 0) {
                        question = "<html><p>Die heruntergeladene Version enth�lt grundlegende Neuerungen.<br>" + "Es wird empfohlen, die Installation manuell durchzuf�hren. Vorher sollte unbedingt die Datenbank gesichert werden.</p><br>+" + "Soll das update trotzdem jetzt automatisch installiert werden?</html>";
                    } else if (vi.minor().compareTo(vMine.minor()) > 0) {
                        question = "<html><p>Die heruntergeladene Version enth�lt gr�ssere �nderungen. Es wird empfohlen:</p>" + "<ul><li>Die Installation nur dann durchzuf�hren, wenn keine anderen Klienten aktiv sind</li>" + "<li>Vor der Installation eine Sicherung der Datenbank durchzuf�hren</li></ul><br>" + "Soll das update jetzt installiert werden?</html>";
                    } else {
                        question = "<html><p>Das Update wurde erfolgreich heruntergeladen. Es sollte sich problemlos " + "automatisch installieren lassen<br>Update jetzt installieren?</html>";
                    }
                    if (SwingHelper.showConfirm("Installation durchf�hren?", new JLabel(question), Agenda.client, SwingHelper.YESNO) == true) {
                        if (fNew.renameTo(fDef) == true) {
                            SwingHelper.alert(Agenda.client, "Update erfolgreich", "Das update wurde heruntergeladen. Es wird beim n�chsten Programmstart aktiv.");
                        } else {
                            SwingHelper.alert(Agenda.client, "Fehler beim Umbenennen", "Das Update konnte nicht installiert werden. Es liegt unter " + fNew.getAbsolutePath());
                        }
                    } else {
                        SwingHelper.alert(Agenda.client, "Update nicht durchgef�hrt", "Das Update wurde nich durchgef�hrt. Die heruntergeladene Datei liegt unter " + fNew.getAbsolutePath());
                    }
                }
            } else {
                SwingHelper.alert(Agenda.client, "Kein update", "Es wurde keine neuere Version gefunden");
            }
        } catch (UnknownHostException ex) {
            log.log("Update Site nicht erreichbar.", Log.ERRORS);
        } catch (Exception ex) {
            log.log("Konnte Verbindung nicht herstellen", Log.ERRORS);
            ExHandler.handle(ex);
        }
        Log.setAlert(null);
    }
}
