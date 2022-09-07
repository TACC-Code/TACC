class BackupThread extends Thread {
    private void DoCompareFiles() {
        appendToLog("*** First file\n");
        ArrayList Meanings1 = new ArrayList();
        String sDataString = "";
        try {
            ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
            FileInputStream fiIn = new FileInputStream(SelectInputFileEdt.getText());
            int iData = 0;
            while ((iData = fiIn.read()) > -1) bsOut.write(iData);
            sDataString = bsOut.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        List<String> saSubtexts = Chunker.chunkString(sDataString);
        Iterator iStrIter = saSubtexts.iterator();
        int iCnt = 0, iMax = saSubtexts.size();
        final gr.demokritos.iit.jinsect.gui.StatusFrame fStatus = new gr.demokritos.iit.jinsect.gui.StatusFrame();
        fStatus.setVisible(true);
        while (iStrIter.hasNext()) {
            String sStr = (String) iStrIter.next();
            fStatus.setStatus("Checking: " + sStr, (double) iCnt++ / iMax);
            List lSubStrings = utils.getSubStrings(sStr, sStr.length(), this);
            if (lSubStrings.size() == 0) continue;
            appendToLog(utils.printList(lSubStrings));
            ArrayList lOptions = new ArrayList();
            lOptions.addAll(lSubStrings);
            Iterator iIter = lOptions.iterator();
            HashMap hSubstringSet = new HashMap();
            while (iIter.hasNext()) {
                Object oNext = iIter.next();
                List lNext;
                if (oNext instanceof List) {
                    lNext = (List) oNext;
                } else {
                    lNext = new ArrayList();
                    lNext.add(oNext);
                }
                if (hSubstringSet.containsKey(lNext.toString())) continue;
                appendToLog("Case " + utils.printList(lNext));
                hSubstringSet.put(lNext.toString(), 1);
                List lNodes = new ArrayList();
                Iterator iSubstrings = lNext.iterator();
                while (iSubstrings.hasNext()) {
                    lNodes.add(new VertexImpl(iSubstrings.next()));
                }
                Iterator iNodes = lNodes.iterator();
                String sUnionMeaning = "";
                while (iNodes.hasNext()) {
                    String sCur = ((Vertex) iNodes.next()).toString();
                    Object oTxt = siIndex.getMeaning(new VertexImpl(sCur));
                    if (oTxt != null) {
                        sUnionMeaning += "-" + SemanticIndex.meaningToString(oTxt) + "-";
                        Meanings1.add(oTxt);
                    } else appendToLog("No meaning found...");
                }
            }
        }
        fStatus.setVisible(false);
        appendToLog("*** Second file\n");
        ArrayList Meanings2 = new ArrayList();
        sDataString = "";
        try {
            ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
            FileInputStream fiIn = new FileInputStream(SelectSecondInputFileEdt.getText());
            int iData = 0;
            while ((iData = fiIn.read()) > -1) bsOut.write(iData);
            sDataString = bsOut.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        saSubtexts = Chunker.chunkString(sDataString);
        iStrIter = saSubtexts.iterator();
        while (iStrIter.hasNext()) {
            String sStr = (String) iStrIter.next();
            ArrayList lOptions = new ArrayList();
            List lSubStrings = utils.getSubStrings(sStr, sStr.length(), this, 2 * NGramSizeSldr.getValue());
            appendToLog(utils.printList(lSubStrings));
            lOptions.addAll(lSubStrings);
            Iterator iIter = lOptions.iterator();
            HashMap hSubstringSet = new HashMap();
            while (iIter.hasNext()) {
                Object oNext = iIter.next();
                List lNext;
                if (oNext instanceof List) {
                    lNext = (List) oNext;
                } else {
                    lNext = new ArrayList();
                    lNext.add(oNext);
                }
                if (hSubstringSet.containsKey(lNext.toString())) continue;
                appendToLog("Case " + utils.printList(lNext));
                hSubstringSet.put(lNext.toString(), 1);
                List lNodes = new ArrayList();
                Iterator iSubstrings = lNext.iterator();
                while (iSubstrings.hasNext()) {
                    lNodes.add(new VertexImpl(iSubstrings.next()));
                }
                Iterator iNodes = lNodes.iterator();
                String sUnionMeaning = "";
                while (iNodes.hasNext()) {
                    String sCur = ((Vertex) iNodes.next()).toString();
                    Object oTxt = siIndex.getMeaning(new VertexImpl(sCur));
                    if (oTxt != null) {
                        sUnionMeaning += "-" + siIndex.meaningToString(oTxt) + "-";
                        Meanings2.add(oTxt);
                    } else appendToLog("No meaning found...");
                }
            }
        }
        double dRes = 0.0;
        Iterator iIter1 = Meanings1.iterator();
        while (iIter1.hasNext()) {
            double dMaxSim = 0.0;
            Iterator iIter2 = Meanings2.iterator();
            WordDefinition d1 = (WordDefinition) iIter1.next();
            while (iIter2.hasNext()) {
                WordDefinition d2 = (WordDefinition) iIter2.next();
                dMaxSim = Math.max(dMaxSim, siIndex.compareWordDefinitions(d1, d2));
            }
            appendToLog("Concluded similarity of " + dMaxSim);
            dRes += dMaxSim;
        }
        dRes = 2 * dRes / (Meanings1.size() + Meanings2.size());
        appendToLog("Final Similarity : " + dRes);
    }
}
