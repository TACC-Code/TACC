class BackupThread extends Thread {
    public void menu() {
        keepRunning = true;
        String option = "1";
        int optionInt;
        while (keepRunning) {
            System.out.println();
            System.out.println("-  1 bind");
            System.out.println("-  2 submit (t/tr)");
            System.out.println("-  3 submit multi (t/tr)");
            System.out.println("-  4 data (t/tr)");
            System.out.println("-  5 query (t/tr)");
            System.out.println("-  6 replace (t/tr)");
            System.out.println("-  7 cancel (t/tr)");
            System.out.println("-  8 enquire link (t/tr)");
            System.out.println("-  9 unbind");
            System.out.println("- 10 receive message (tr/r)");
            System.out.println("-  0 exit");
            System.out.print("> ");
            optionInt = -1;
            try {
                option = keyboard.readLine();
                optionInt = Integer.parseInt(option);
            } catch (Exception e) {
                debug.write("exception reading keyboard " + e);
                optionInt = -1;
            }
            switch(optionInt) {
                case 1:
                    bind();
                    break;
                case 2:
                    submit();
                    break;
                case 3:
                    submitMulti();
                    break;
                case 4:
                    data();
                    break;
                case 5:
                    query();
                    break;
                case 6:
                    replace();
                    break;
                case 7:
                    cancel();
                    break;
                case 8:
                    enquireLink();
                    break;
                case 9:
                    unbind();
                    break;
                case 10:
                    receive();
                    break;
                case 0:
                    exit();
                    break;
                case -1:
                    break;
                default:
                    System.out.println("Invalid option. Choose between 0 and 10.");
                    break;
            }
        }
    }
}
