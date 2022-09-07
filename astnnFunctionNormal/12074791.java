class BackupThread extends Thread {
    private void calcMD5() {
        md5.reset();
        md5.update(byTexto);
        byte[] dig = md5.digest();
        hash = dumpBytes(dig);
    }
}
