class BackupThread extends Thread {
                    @Override
                    public int workOnFile(InputStream input) throws Exception {
                        Scanner sc = new Scanner(input);
                        while (sc.hasNextLine()) {
                            parser.getShell().printLine(sc.nextLine());
                        }
                        return 0;
                    }
}
