class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        URL urlUPGRADECOMPLETE = new URL("http://www.tegsoft.com/Tobe/forms/TobeOS/upgrade/upgrade_current.jsp?tegsoftCLIENTVERSION=" + "20110810" + "&tegsoftCLIENTUNITID=" + "4a55c1e3-edd5-46ef-b66f-d74634e8469a" + "&tegsoftCLIENTMAC=" + "98:4b:e1:4d:b1:b1" + "&tegsoftCOMMAND=UPGRADECOMPLETE");
        URLConnection connectionUPGRADECOMPLETE = urlUPGRADECOMPLETE.openConnection();
        InputStream is = connectionUPGRADECOMPLETE.getInputStream();
        is.close();
    }
}
