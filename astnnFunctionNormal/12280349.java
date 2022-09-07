class BackupThread extends Thread {
    void position(String msg) {
        tester.position(msg + " " + addIndex + '-' + remIndex + '-' + readIndex + '-' + writeIndex + '-' + member.length);
    }
}
