class BackupThread extends Thread {
    public void getQuestions() {
        BufferedReader input;
        String line;
        ArrayList<String> lineList;
        lineList = new ArrayList<String>();
        int count = 1;
        try {
            input = new BufferedReader(new FileReader("easyplay//history//questions//easy.txt"));
            if (difficulty == 1) {
                input.close();
                input = new BufferedReader(new FileReader("easyplay//history//questions//standard.txt"));
            } else if (difficulty == 2) {
                input.close();
                input = new BufferedReader(new FileReader("easyplay//history//questions//hard.txt"));
            }
            line = input.readLine();
            count--;
            while (line != null) {
                count++;
                lineList.add(line);
                line = input.readLine();
            }
            input.close();
        } catch (IOException err) {
        }
        String[] lines = new String[lineList.size()];
        lineList.toArray(lines);
        int i = 0;
        int j = 0;
        int numberofqs = count / 5;
        questions = new String[numberofqs];
        answers = new String[numberofqs];
        wrong1 = new String[numberofqs];
        wrong2 = new String[numberofqs];
        wrong3 = new String[numberofqs];
        while (j < count) {
            questions[i] = lines[j];
            answers[i] = lines[j + 1];
            wrong1[i] = lines[j + 2];
            wrong2[i] = lines[j + 3];
            wrong3[i] = lines[j + 4];
            i++;
            j = j + 5;
        }
    }
}
