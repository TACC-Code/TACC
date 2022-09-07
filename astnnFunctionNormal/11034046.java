class BackupThread extends Thread {
    public static String writeTestMethod(String fileName) {
        return "\t@Test\n" + "\tpublic void testReadModel(){\n" + "\t\tFile fleIn = new File(\"" + fileName + "\");\n" + "\t\taxlModel1 = readFromFile(fleIn);\n" + "\t\tassertNotNull(\"axlModel1 was null, but expected it not to be.\",axlModel1);\n" + "\t\tAXLFile theAXLFile = new AXLFile(new File(\"../aXLangModel/src/de/fraunhofer/isst/axbench/test/autoTests/generated.axl\").getPath(), axlModel1);\n" + "\t\twriteToFile(theAXLFile);\n" + "\t\tfleIn = new File(\"../aXLangModel/src/de/fraunhofer/isst/axbench/test/autoTests/generated.axl\");\n" + "\t\taxlModel2 = readFromFile(fleIn);\n" + "\t\tcheckModels(axlModel1, axlModel2);\n" + "\t}\n\n";
    }
}
