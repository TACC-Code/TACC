class BackupThread extends Thread {
    public void downStart() {
        try {
            int port;
            URL url = CheckHost(getAddr());
            if (url == null) return;
            if (DNutil.RedirectCheck(url)) {
                String addr = DNutil.GetRedirectLocation(url);
                url = new URL(addr);
                String t = url.getPath().substring(url.getPath().lastIndexOf("/") + 1, url.getPath().length());
                Taddr.setText(addr);
                Tfilename.setText(t);
            }
            port = (url.getPort() != -1) ? url.getPort() : 80;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            rowNum = mymodel.getEmptyRow();
            File file = new File(getFolder() + "\\" + getFilename());
            File bkfile = new File(getFolder() + "\\" + getFilename() + ".dn");
            table.setValueAt(getFolder() + "\\" + getFilename(), rowNum, 0);
            table.setValueAt(con.getContentLength() + " Byte", rowNum, 1);
            table.setValueAt(0 + "", rowNum, 2);
            table.setValueAt(getSplit() + "", rowNum, 7);
            table.setValueAt(getAddr(), rowNum, 9);
            Information info = new Information(url.getHost(), url.getFile(), port, file, con.getContentLength(), getSplit(), bkfile);
            info.SetTable(table);
            info.SetRowNum(rowNum);
            SocketManager sm = new SocketManager(info);
            HttpHeader hheader = new HttpHeader();
            downloadmanager[rowNum] = new DownloadManager(info, hheader, sm);
            con.disconnect();
            downloadmanager[rowNum].start();
        } catch (MalformedURLException te) {
            te.printStackTrace();
        } catch (IOException te) {
            te.printStackTrace();
        }
    }
}
