class BackupThread extends Thread {
                @Override
                public void run() {
                    for (ListIterator<RowInfo> iterator = model.getRowInfos().listIterator(); iterator.hasNext(); ) {
                        RowInfo rowInfo = iterator.next();
                        if (!rowInfo.isChecked()) {
                            continue;
                        }
                        switch(rowInfo.getType()) {
                            case SOURCE_ONLY:
                                {
                                    PathInfo pathInfo = rowInfo.getSourcePathInfo();
                                    File sourceFile = pathInfo.applyToFolder(model.getFolderDiff().getSourceFolder());
                                    File targetFile = pathInfo.applyToFolder(model.getFolderDiff().getTargetFolder());
                                    try {
                                        if (sourceFile.isFile()) {
                                            FileUtils.copyFile(sourceFile, targetFile);
                                        } else if (sourceFile.isDirectory()) {
                                            FileUtils.copyDirectory(sourceFile, targetFile);
                                        }
                                        iterator.remove();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                }
                            case DIFFERENT:
                                {
                                    PathInfo pathInfo = rowInfo.getSourcePathInfo();
                                    File sourceFile = pathInfo.applyToFolder(model.getFolderDiff().getSourceFolder());
                                    File targetFile = pathInfo.applyToFolder(model.getFolderDiff().getTargetFolder());
                                    try {
                                        if (sourceFile.isFile()) {
                                            targetFile.delete();
                                            FileUtils.copyFile(sourceFile, targetFile);
                                        } else if (sourceFile.isDirectory()) {
                                            FileUtils.deleteDirectory(targetFile);
                                            FileUtils.copyDirectory(sourceFile, targetFile);
                                        }
                                        iterator.remove();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                }
                            case TARGET_ONLY:
                                {
                                    PathInfo pathInfo = rowInfo.getTargetPathInfo();
                                    File targetFile = pathInfo.applyToFolder(model.getFolderDiff().getTargetFolder());
                                    try {
                                        if (targetFile.isFile()) {
                                            targetFile.delete();
                                        } else if (targetFile.isDirectory()) {
                                            FileUtils.deleteDirectory(targetFile);
                                        }
                                        iterator.remove();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                }
                        }
                    }
                    model.fireTableDataChanged();
                    _btnScan.setEnabled(true);
                    _btnSync.setEnabled(true);
                }
}
