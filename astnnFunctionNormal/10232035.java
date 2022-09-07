class BackupThread extends Thread {
        private String getHarnessMetaBlock(File inFile) throws FileNotFoundException, IOException {
            int i;
            int writePointer = 0;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
            while ((i = bis.read(ba, writePointer, ba.length - writePointer)) > 0) {
                writePointer += i;
            }
            if (i > -1) {
                throw new IOException(inFile.toString() + " is larger than " + (ba.length - 1) + " bytes.");
            }
            StringBuffer sb1 = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();
            Pattern commentPattern = Pattern.compile("(?s)(?<=/\\*).*?(?=\\*/)");
            Pattern mdPattern = Pattern.compile("(?m)(^\\s*HARNESS_METADATA\\s+BEGIN\\s*^)(?s)(.*?$)(?-s)" + "(\\s*HARNESS_METADATA\\s+END\\s*$)");
            Matcher commentMatcher = commentPattern.matcher(new String(ba, 0, writePointer));
            while (commentMatcher.find()) {
                sb1.append(commentMatcher.group() + '\n');
            }
            Matcher mdMatcher = mdPattern.matcher(sb1);
            while (mdMatcher.find()) {
                if (mdMatcher.groupCount() != 3) {
                    continue;
                }
                sb2.append(mdMatcher.group(2) + '\n');
            }
            return sb2.toString();
        }
}
