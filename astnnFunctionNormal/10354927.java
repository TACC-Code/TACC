class BackupThread extends Thread {
    public void handleExportPointGeom(GeomReader reader) {
        ResourceBundle resBundle = _app.getResourceBundle();
        try {
            DialogItem item = new DialogItem(resBundle.getString("exptDlgListLabel") + " ", resBundle.getString("exptDlgListExample"));
            List<DialogItem> itemList = new ArrayList();
            itemList.add(item);
            item = new DialogItem(reader.toString() + " " + resBundle.getString("exptDlgSaveLabel") + " ", new File(_app.getPreferences().getLastPath(), resBundle.getString("inputDlgFileExample")));
            item.setLoadFile(false);
            item.setFileExtension(reader.getExtension());
            itemList.add(item);
            InputDialog dialog = new InputDialog(this, resBundle.getString("inputDlgTitle"), "", itemList);
            itemList = dialog.getOutput();
            if (itemList == null) return;
            String listName = (String) itemList.get(0).getElement();
            File theFile = (File) itemList.get(1).getElement();
            if (canWriteFile(theFile)) {
                String cwd = (String) _bsh.get("bsh.cwd");
                String writerName = reader.getClass().toString();
                writerName = writerName.substring(writerName.lastIndexOf(".") + 1);
                String pathName;
                if (theFile.getParent().equals(cwd)) pathName = theFile.getName(); else pathName = theFile.getPath();
                if (AppUtilities.isWindows()) pathName = pathName.replace("\\", "/");
                String command = "writeGeomFile(" + listName + ", pathToFile(\"" + pathName + "\"), new " + writerName + "());";
                _bsh.eval(command);
                _bsh.println(command);
            }
        } catch (EvalError e) {
            e.printStackTrace();
            AppUtilities.showException(this, resBundle.getString("evalErrTitle"), resBundle.getString("unexpectedMsg"), e);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtilities.showException(null, resBundle.getString("unexpectedTitle"), resBundle.getString("unexpectedMsg"), e);
        }
    }
}
