class BackupThread extends Thread {
    public void addNoize(String filename, int snr) {
        String command = MessageFormat.format("octave --path /home/as/src/ocatave/ --silent --eval [y,Fs,bits]=wavread(''{0}'');y=awgn(y'''',{1},''measured'');wavwrite(\"{0}\",y'''',Fs);", filename, snr);
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s = null;
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println(command);
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
