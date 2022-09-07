class BackupThread extends Thread {
    public void run() {
        Proxy p = null;
        if (mProxyType == 0) p = Proxy.NO_PROXY; else {
            try {
                p = new Proxy(PROXY_TYPES[mProxyType], new InetSocketAddress(jTextFieldProxyURL.getText(), Integer.parseInt(jTextFieldProxyPort.getText())));
            } catch (Exception e) {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
                return;
            }
        }
        String setName = jTextFieldSetName.getText();
        String setNameUrl = csa.util.UtilityString.replace(setName, " ", "%20");
        setName = csa.util.UtilityString.replace(setName, "\"", "");
        setName = csa.util.UtilityString.replace(setName, "'", "");
        setName = csa.util.UtilityString.replace(setName, "´", "");
        setName = csa.util.UtilityString.replace(setName, "`", "");
        setName = csa.util.UtilityString.replace(setName, ":", "");
        String base = jTextFieldBaseUrl.getText();
        mLoadedCardSet.getData().mOrigin = CardSet.GATHERER_IMPORTED;
        mLoadedCardSet.getData().mSetName = setName;
        try {
            String searcher = "Search/Default.aspx?output=spoiler&method=text&set=[%22" + setNameUrl + "%22] ";
            URL url = new URL(base + searcher);
            URLConnection conn = url.openConnection(p);
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            boolean save = false;
            String[] dontLike = { "<tr>", "</tr>", "<td", "</td>", "<table>", "</table>" };
            int NAME = 0;
            int COST = 1;
            int TYPE = 2;
            int POW = 3;
            int TEXT = 4;
            int RARITY = 5;
            String[] textTags = { "Name:", "Cost:", "Type:", "Pow/Tgh:", "Rules Text:", "Set/Rarity:" };
            Card card = new Card();
            int currentTag = -1;
            String dbg = "";
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("class=\"textspoiler\"") != -1) save = true;
                if (line.indexOf("</div>") != -1) save = false;
                if (save) {
                    boolean goOn = true;
                    for (int i = 0; i < dontLike.length; i++) {
                        if (line.indexOf(dontLike[i]) != -1) {
                            goOn = false;
                            break;
                        }
                    }
                    if (goOn) {
                        for (int i = 0; i < textTags.length; i++) {
                            if (line.indexOf(textTags[i]) != -1) {
                                currentTag = i;
                                goOn = false;
                                break;
                            }
                        }
                    }
                    if (goOn) {
                        if (NAME == currentTag) {
                            dbg = "";
                            String id = line.substring(line.indexOf("multiverseid=") + 13);
                            id = id.substring(0, id.indexOf("\""));
                            String name = line.substring(line.indexOf(">") + 1);
                            name = name.substring(0, name.indexOf("<"));
                            card = new Card();
                            card.getData().setId(id);
                            card.getData().setCardName(name);
                            card.getData().setSetName(setName);
                            dbg += "Card: " + name + "(" + id + ")\n";
                            currentTag = -1;
                        }
                        if (COST == currentTag) {
                            String cost = line.trim();
                            cost = csa.util.UtilityString.replace(cost, "G", "{G}");
                            cost = csa.util.UtilityString.replace(cost, "B", "{B}");
                            cost = csa.util.UtilityString.replace(cost, "W", "{W}");
                            cost = csa.util.UtilityString.replace(cost, "R", "{R}");
                            cost = csa.util.UtilityString.replace(cost, "U", "{U}");
                            cost = csa.util.UtilityString.replace(cost, "X", "{X}");
                            cost = csa.util.UtilityString.replace(cost, "1", "{1}");
                            cost = csa.util.UtilityString.replace(cost, "2", "{2}");
                            cost = csa.util.UtilityString.replace(cost, "3", "{3}");
                            cost = csa.util.UtilityString.replace(cost, "4", "{4}");
                            cost = csa.util.UtilityString.replace(cost, "5", "{5}");
                            cost = csa.util.UtilityString.replace(cost, "6", "{6}");
                            cost = csa.util.UtilityString.replace(cost, "7", "{7}");
                            cost = csa.util.UtilityString.replace(cost, "8", "{8}");
                            cost = csa.util.UtilityString.replace(cost, "9", "{9}");
                            card.getData().setMana(cost);
                            dbg += "Cost: " + cost + "\n";
                            currentTag = -1;
                        }
                        if (TYPE == currentTag) {
                            String type = line.trim();
                            int typeEnd = -1;
                            int subtypeStart = -1;
                            for (int i = 0; i < type.length(); i++) {
                                if (((int) (type.charAt(i))) > 250) {
                                    typeEnd = i - 1;
                                    for (int c = i; c < type.length(); c++) {
                                        if (((int) (type.charAt(c))) == 32) {
                                            subtypeStart = c;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            if (typeEnd != -1) {
                                card.getData().setType(type.substring(0, typeEnd).trim());
                                dbg += "Type: " + type.substring(0, typeEnd).trim() + " ";
                            } else {
                                card.getData().setType(type);
                                dbg += "Type: " + type + " ";
                            }
                            if (subtypeStart != -1) {
                                String subType = type.substring(subtypeStart).trim();
                                card.getData().setSubtype(subType.trim());
                                dbg += "SubType: " + subType.trim() + "\n";
                            }
                            currentTag = -1;
                        }
                        if (POW == currentTag) {
                            String powerToughness = line.trim();
                            if (powerToughness.length() == 0) {
                                card.getData().setPower("");
                                card.getData().setToughness("");
                            } else {
                                powerToughness = csa.util.UtilityString.replace(powerToughness, "(", "");
                                powerToughness = csa.util.UtilityString.replace(powerToughness, ")", "");
                                String power = powerToughness.substring(0, powerToughness.indexOf("/"));
                                String toughness = powerToughness.substring(powerToughness.indexOf("/") + 1);
                                card.getData().setPower(power);
                                card.getData().setToughness(toughness);
                            }
                            dbg += "Pow/Tough: " + card.getData().getPower() + "/" + card.getData().getToughness() + "\n";
                            currentTag = -1;
                        }
                        if (TEXT == currentTag) {
                            String text = line.trim();
                            text = csa.util.UtilityString.replace(text, "<br />", " ");
                            card.addText(text);
                        }
                        if (RARITY == currentTag) {
                            dbg += "Text: " + card.getText() + "\n";
                            String r = "";
                            String rarity = line.substring(line.indexOf(setName) + setName.length());
                            int l = rarity.length();
                            if (rarity.indexOf(",") != -1) l = rarity.indexOf(",");
                            rarity = rarity.substring(0, l);
                            if (rarity.indexOf("Common") != -1) r = "C";
                            if (rarity.indexOf("Uncommon") != -1) r = "U";
                            if (rarity.indexOf("Rare") != -1) r = "R";
                            if (rarity.indexOf("Land") != -1) r = "L";
                            card.getData().setRarity(r);
                            dbg += "Rarity: " + r + "\n";
                            dbg += "----\n";
                            if (card.getType().equalsIgnoreCase("Basic Land")) {
                                if (card.getText().indexOf("{T}") == -1) card.addfText("{T}:");
                            }
                            mLoadedCardSet.addCard(card);
                            currentTag = -1;
                            synchronized (this) {
                                jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
                                jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
                                Configuration.getConfiguration().getLogEntity().addLog(dbg);
                            }
                        }
                    }
                }
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
            return;
        }
        String dirName = "sets" + File.separator + setName;
        File dir = new File(dirName);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (Throwable e) {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
            }
        }
        mLoadedCardSet.getData().mImagePath = dirName;
        HashMap<String, String> imageUrls = new HashMap<String, String>();
        try {
            String secondBase = "Search/";
            String searcher = new String("Default.aspx?output=spoiler&method=visual&set=[%22" + setNameUrl + "%22] ");
            URL url = new URL(base + secondBase + searcher);
            URLConnection conn = url.openConnection(p);
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            boolean save = false;
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("class=\"visualspoiler\"") != -1) save = true;
                if (line.indexOf("</div>") != -1) save = false;
                if (save) {
                    if (line.indexOf("<img src=") != -1) {
                        line = line.substring(line.indexOf("<img src=") + "<img src=".length() + 1);
                        line = line.substring(0, line.indexOf("\""));
                        String id = line.substring(line.indexOf("=") + 1);
                        id = id.substring(0, id.indexOf("&"));
                        String imageUrl = csa.util.UtilityString.fromXML(line);
                        if (imageUrl.startsWith(".")) {
                            imageUrl = base + secondBase + imageUrl;
                        }
                        imageUrls.put(id, imageUrl);
                        synchronized (this) {
                            String dbg = "ID: " + id + " ImageUrl: " + imageUrl + " got!\n";
                            jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
                            jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length() - dbg.length());
                            Configuration.getConfiguration().getLogEntity().addLog(dbg);
                        }
                    }
                }
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
        }
        BufferedInputStream in;
        BufferedOutputStream out;
        Set entries = imageUrls.entrySet();
        Iterator it = entries.iterator();
        int c = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String imageUrl = (String) entry.getValue();
            String id = (String) entry.getKey();
            String imagePath = dirName + File.separator + id + ".jpg";
            File f = new File(imagePath);
            if (f.exists()) continue;
            try {
                in = new BufferedInputStream(new URL(imageUrl).openConnection(p).getInputStream());
                Image image = javax.imageio.ImageIO.read(in);
                Icon i = new ImageIcon(image);
                jLabelImage.setIcon(i);
                out = new BufferedOutputStream(new FileOutputStream(f));
                BufferedImage bimage = csa.util.UtilityImage.toBufferedImage(image);
                if (bimage.getHeight() != 285) {
                    BufferedImage bimageCropped = bimage.getSubimage(11, 12, 200, 285);
                    Icon iCropped = new ImageIcon(bimageCropped);
                    jLabel4.setIcon(iCropped);
                    javax.imageio.ImageIO.write(bimageCropped, "jpg", f);
                } else javax.imageio.ImageIO.write(bimage, "jpg", f);
                in.close();
                c++;
                synchronized (this) {
                    String dbg = "(" + c + "/" + imageUrls.size() + ")Image successfully saved to: " + imagePath + "\n";
                    jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
                    jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length() - dbg.length());
                    Configuration.getConfiguration().getLogEntity().addLog(dbg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e, Logable.LOG_ERROR);
            }
        }
        jButtonStartImport.setEnabled(true);
        jButtonEditSet.setEnabled(true);
        Card.addImagePathForSet(setName, dirName + File.separator);
        mLoadedCardSet.getData().mClass = "MagicSets";
        mLoadedCardSet.getData().mName = setName;
        mLoadedCardSet.joinedSave();
        if (jCheckBoxJoin.isSelected()) {
            jTextAreaOut.insert("\nLooking if there is anything to clone...", jTextAreaOut.getText().length());
            jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
            int s = seekForKnown(setName);
            String dbg = "\n" + setName + " ->" + "Supported Cards found: " + s;
            jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
            jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
            System.out.println(dbg);
        }
        if (jCheckBoxFindSimilar.isSelected()) {
            jTextAreaOut.insert("\nLooking if there is anything to convert and clone...", jTextAreaOut.getText().length());
            jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
            int s = seekForSimilar(setName);
            String dbg = "\n" + setName + " ->" + "Supported Cards found: " + s;
            jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
            jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
            System.out.println(dbg);
        }
        String dbg = "Import finished!\n";
        dbg = "\nImages and set is saved, press above \"Edit Set\" button to enter set editing.";
        jTextAreaOut.insert(dbg, jTextAreaOut.getText().length());
        jTextAreaOut.setCaretPosition(jTextAreaOut.getText().length());
        Configuration.getConfiguration().getLogEntity().addLog(dbg);
        if (msk != null) {
            msk.importFinished();
        }
    }
}
