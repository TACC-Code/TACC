class BackupThread extends Thread {
    private void DoBreakFileDown() {
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
        List saSubtexts = Chunker.chunkString(sDataString);
        final gr.demokritos.iit.jinsect.gui.StatusFrame fStatus = new gr.demokritos.iit.jinsect.gui.StatusFrame();
        fStatus.setVisible(true);
        int iCnt = 0;
        try {
            Iterator iStrIter = saSubtexts.iterator();
            while (iStrIter.hasNext()) {
                String sStr = (String) iStrIter.next();
                fStatus.setStatus("Extracting meanings...", (double) (++iCnt) / saSubtexts.size());
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
                        if (oTxt != null) sUnionMeaning += "-" + siIndex.meaningToString(oTxt) + "-"; else appendToLog("No meaning found...");
                    }
                }
            }
        } finally {
            fStatus.setVisible(false);
        }
    }
}
