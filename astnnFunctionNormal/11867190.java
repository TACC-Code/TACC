class BackupThread extends Thread {
    protected void retrieveLayoutInformation(org.eclipse.emf.ecore.EObject element, fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfSyntaxElement syntaxElement, Object object, boolean ignoreTokensAfterLastVisibleToken) {
        if (element == null) {
            return;
        }
        boolean isElementToStore = syntaxElement == null;
        isElementToStore |= syntaxElement instanceof fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfPlaceholder;
        isElementToStore |= syntaxElement instanceof fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfKeyword;
        isElementToStore |= syntaxElement instanceof fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfEnumerationTerminal;
        isElementToStore |= syntaxElement instanceof fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfBooleanTerminal;
        if (!isElementToStore) {
            return;
        }
        fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfLayoutInformationAdapter layoutInformationAdapter = getLayoutInformationAdapter(element);
        for (org.antlr.runtime3_3_0.CommonToken anonymousToken : anonymousTokens) {
            layoutInformationAdapter.addLayoutInformation(new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfLayoutInformation(syntaxElement, object, anonymousToken.getStartIndex(), anonymousToken.getText(), null));
        }
        anonymousTokens.clear();
        int currentPos = getTokenStream().index();
        if (currentPos == 0) {
            return;
        }
        int endPos = currentPos - 1;
        if (ignoreTokensAfterLastVisibleToken) {
            for (; endPos >= this.lastPosition2; endPos--) {
                org.antlr.runtime3_3_0.Token token = getTokenStream().get(endPos);
                int _channel = token.getChannel();
                if (_channel != 99) {
                    break;
                }
            }
        }
        StringBuilder hiddenTokenText = new StringBuilder();
        StringBuilder visibleTokenText = new StringBuilder();
        org.antlr.runtime3_3_0.CommonToken firstToken = null;
        for (int pos = this.lastPosition2; pos <= endPos; pos++) {
            org.antlr.runtime3_3_0.Token token = getTokenStream().get(pos);
            if (firstToken == null) {
                firstToken = (org.antlr.runtime3_3_0.CommonToken) token;
            }
            int _channel = token.getChannel();
            if (_channel == 99) {
                hiddenTokenText.append(token.getText());
            } else {
                visibleTokenText.append(token.getText());
            }
        }
        int offset = -1;
        if (firstToken != null) {
            offset = firstToken.getStartIndex();
        }
        layoutInformationAdapter.addLayoutInformation(new fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfLayoutInformation(syntaxElement, object, offset, hiddenTokenText.toString(), visibleTokenText.toString()));
        this.lastPosition2 = (endPos < 0 ? 0 : endPos + 1);
    }
}
