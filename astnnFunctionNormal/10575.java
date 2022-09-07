class BackupThread extends Thread {
    private String parse(String str) {
        final char[] trans2 = "bcdef".toCharArray();
        final char[] trans1 = "ghij".toCharArray();
        int index1 = 0;
        int index2 = 0;
        str = str.replaceAll("[\n\r]", "");
        char[] input = str.toCharArray();
        for (int i = 0; i < 20; i++) {
            switch(input[i]) {
                case '4':
                    input[i] = input[i + 1] = input[i + 4] = input[i + 5] = 'a';
                    break;
                case '1':
                    input[i] = trans1[index1++];
                    break;
                case '2':
                    input[i] = input[i + 4] = trans2[index2++];
                    break;
                case '3':
                    input[i] = input[i + 1] = (char) (trans2[index2++] - 'a' + 'A');
                    break;
                case '0':
                    input[i] = ' ';
                    break;
                default:
            }
        }
        return new String(input);
    }
}
