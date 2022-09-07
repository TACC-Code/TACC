class BackupThread extends Thread {
    public java.util.List<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal> parseToExpectedElements(org.eclipse.emf.ecore.EClass type, fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfTextResource dummyResource, int cursorOffset) {
        this.rememberExpectedElements = true;
        this.parseToIndexTypeObject = type;
        this.cursorOffset = cursorOffset;
        this.lastStartIncludingHidden = -1;
        final org.antlr.runtime3_3_0.CommonTokenStream tokenStream = (org.antlr.runtime3_3_0.CommonTokenStream) getTokenStream();
        fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfParseResult result = parse();
        for (org.eclipse.emf.ecore.EObject incompleteObject : incompleteObjects) {
            org.antlr.runtime3_3_0.Lexer lexer = (org.antlr.runtime3_3_0.Lexer) tokenStream.getTokenSource();
            int endChar = lexer.getCharIndex();
            int endLine = lexer.getLine();
            setLocalizationEnd(result.getPostParseCommands(), incompleteObject, endChar, endLine);
        }
        if (result != null) {
            org.eclipse.emf.ecore.EObject root = result.getRoot();
            if (root != null) {
                dummyResource.getContentsInternal().add(root);
            }
            for (fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfCommand<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfTextResource> command : result.getPostParseCommands()) {
                command.execute(dummyResource);
            }
        }
        expectedElements = expectedElements.subList(0, expectedElementsIndexOfLastCompleteElement + 1);
        int lastFollowSetID = expectedElements.get(expectedElementsIndexOfLastCompleteElement).getFollowSetID();
        java.util.Set<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal> currentFollowSet = new java.util.LinkedHashSet<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal>();
        java.util.List<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal> newFollowSet = new java.util.ArrayList<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal>();
        for (int i = expectedElementsIndexOfLastCompleteElement; i >= 0; i--) {
            fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal expectedElementI = expectedElements.get(i);
            if (expectedElementI.getFollowSetID() == lastFollowSetID) {
                currentFollowSet.add(expectedElementI);
            } else {
                break;
            }
        }
        int followSetID = 37;
        int i;
        for (i = tokenIndexOfLastCompleteElement; i < tokenStream.size(); i++) {
            org.antlr.runtime3_3_0.CommonToken nextToken = (org.antlr.runtime3_3_0.CommonToken) tokenStream.get(i);
            if (nextToken.getType() < 0) {
                break;
            }
            if (nextToken.getChannel() == 99) {
            } else {
                for (fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal nextFollow : newFollowSet) {
                    lastTokenIndex = 0;
                    setPosition(nextFollow, i);
                }
                newFollowSet.clear();
                for (fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal nextFollow : currentFollowSet) {
                    if (nextFollow.getTerminal().getTokenNames().contains(getTokenNames()[nextToken.getType()])) {
                        java.util.Collection<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.util.AlfPair<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]>> newFollowers = nextFollow.getTerminal().getFollowers();
                        for (fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.util.AlfPair<fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]> newFollowerPair : newFollowers) {
                            fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.IAlfExpectedElement newFollower = newFollowerPair.getLeft();
                            fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal newFollowTerminal = new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal(newFollower, followSetID, newFollowerPair.getRight());
                            newFollowSet.add(newFollowTerminal);
                            expectedElements.add(newFollowTerminal);
                        }
                    }
                }
                currentFollowSet.clear();
                currentFollowSet.addAll(newFollowSet);
            }
            followSetID++;
        }
        for (fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfExpectedTerminal nextFollow : newFollowSet) {
            lastTokenIndex = 0;
            setPosition(nextFollow, i);
        }
        return this.expectedElements;
    }
}
