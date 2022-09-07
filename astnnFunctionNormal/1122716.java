class BackupThread extends Thread {
    public static void main(String args[]) {
        ArrayList<String> loc;
        loc = new ArrayList();
        Locator locator;
        locator = new Locator("XML", "TopicalGuide", true);
        Locator noprocess = new Locator("XML", "TopicalGuide", false);
        noprocess.addQuery("//topic[topic_name='Name of Topic']/scripture[reference/book_name='3 Nephi' and reference/chapter='11' and reference/verse='11']/scripture_note");
        loc.add("XML");
        loc.add("TopicalGuide");
        loc.add("/home/josh/documents/devel/workspace/dataobject/bin/scriptures.xml");
        DataManager dm = new DataManager();
        dm.openDataLoader(loc);
        locator.addQueryElement("2", "topic", "\"\"");
        locator.addQueryElement("2", "topic_name", "\"Name of Topic\"");
        locator.addQueryElement("2", "scripture", "\"\"");
        locator.addQueryElement("2", "reference/book_name", "\"3 Nephi\"");
        locator.addQueryElement("2", "reference/chapter", "\"11\"");
        locator.addQueryElement("2", "reference/verse", "\"11\"");
        locator.addQueryElement("2", "scripture_note", "\"\"");
        System.out.println("before Query");
        System.out.println(dm.readData(locator));
        System.out.println("after Query");
        System.out.println(dm.readData(noprocess));
        Locator writehere = new Locator("XML", "TopicalGuide", true);
        writehere.addQueryElement("2", "topic", "\"\"");
        writehere.addQueryElement("2", "topic_name", "\"Name of Topic\"");
        writehere.addQueryElement("2", "scripture", "\"\"");
        writehere.addQueryElement("2", "reference/book_name", "\"3 Nephi\"");
        writehere.addQueryElement("2", "reference/chapter", "\"12\"");
        writehere.addQueryElement("2", "reference/verse", "\"12\"");
        writehere.addQueryElement("2", "scripture_note", "\"\"");
        dm.writeData(writehere, "I have written");
        System.out.println(dm.readData(writehere));
    }
}
