class BackupThread extends Thread {
    public void save(Table table, boolean askOnOverwrite) {
        try {
            Document document;
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
            }
            if (xmlFile.length() == 0) {
                document = createNewDocument();
            } else {
                try {
                    document = getDocument();
                } catch (Exception e) {
                    if (JOptionPane.showConfirmDialog(null, "Unable to parse table content, overwrite " + xmlFile.getName() + "?", "Error reading table file", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        collector.clearMessages();
                        document = createNewDocument();
                    } else {
                        return;
                    }
                }
            }
            Element root = document.getDocumentElement();
            NodeList tables = root.getElementsByTagNameNS(XML_NS_URI, TBL_ELEM_ROOT);
            Node existingTableNode = null;
            for (int i = 0; i < tables.getLength(); i++) {
                if (table.getTableId().equals(tables.item(i).getAttributes().getNamedItem(TBL_ATTR_ID).getNodeValue())) {
                    if (askOnOverwrite && JOptionPane.showConfirmDialog(null, "Overwrite existing table \"" + table.getTableId() + "\" in " + xmlFile.getName() + "?", "Overwrite table?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                        return;
                    }
                    existingTableNode = tables.item(i);
                    break;
                }
            }
            Node newTableNode = createTableNode(table, document);
            if (existingTableNode != null) {
                root.replaceChild(newTableNode, existingTableNode);
            } else {
                root.appendChild(newTableNode);
            }
            validateDocument(document);
            if (!collector.hasProblems()) {
                writeDocument(document, new FileOutputStream(xmlFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
            collector.collectDiagnostic(e.getClass().getName() + " " + e.getMessage(), true);
        } finally {
            checkProblems("Error saving table file " + xmlFile.getName());
        }
    }
}
