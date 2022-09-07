class BackupThread extends Thread {
    public ArrayList<String> readBins(File data, NarrWriter narr) {
        ArrayList<String> output = new ArrayList<String>();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(data));
            String nextLine = input.readLine();
            if (nextLine == null) {
                System.out.println("Invalid input file first line null");
                return null;
            }
            String storage = nextLine;
            StringTokenizer a;
            String x;
            int value;
            while (nextLine != null) {
                a = new StringTokenizer(nextLine);
                a.nextToken();
                if (!a.hasMoreTokens()) {
                    System.out.println("Invalid input file one line doesn't have 2 tokens");
                    return null;
                }
                value = new Integer(a.nextToken()).intValue();
                if (value > 1) {
                    break;
                }
                narr.println(nextLine);
                storage = nextLine;
                nextLine = input.readLine();
            }
            output.add(storage);
            while (nextLine != null) {
                output.add(nextLine);
                narr.println(nextLine);
                nextLine = input.readLine();
            }
        } catch (IOException e) {
            System.out.println("Invalid input file file won't open");
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error closing input file!");
                }
            }
        }
        return output;
    }
}
