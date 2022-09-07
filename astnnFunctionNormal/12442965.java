class BackupThread extends Thread {
    protected void retrieveLayoutInformation(org.eclipse.emf.ecore.EObject element, tudresden.ocl20.pivot.language.ocl.resource.ocl.grammar.OclSyntaxElement syntaxElement, Object object, boolean ignoreTokensAfterLastVisibleToken) {
        if (element == null) {
            return;
        }
        boolean isElementToStore = syntaxElement == null;
        isElementToStore |= syntaxElement instanceof tudresden.ocl20.pivot.language.ocl.resource.ocl.grammar.OclPlaceholder;
        isElementToStore |= syntaxElement instanceof tudresden.ocl20.pivot.language.ocl.resource.ocl.grammar.OclKeyword;
        isElementToStore |= syntaxElement instanceof tudresden.ocl20.pivot.language.ocl.resource.ocl.grammar.OclEnumerationTerminal;
        isElementToStore |= syntaxElement instanceof tudresden.ocl20.pivot.language.ocl.resource.ocl.grammar.OclBooleanTerminal;
        if (!isElementToStore) {
            return;
        }
        tudresden.ocl20.pivot.language.ocl.resource.ocl.mopp.OclLayoutInformationAdapter layoutInformationAdapter = getLayoutInformationAdapter(element);
        for (org.antlr.runtime3_2_0.CommonToken anonymousToken : anonymousTokens) {
            layoutInformationAdapter.addLayoutInformation(new tudresden.ocl20.pivot.language.ocl.resource.ocl.mopp.OclLayoutInformation(syntaxElement, object, anonymousToken.getStartIndex(), anonymousToken.getText(), null));
        }
        anonymousTokens.clear();
        int currentPos = getTokenStream().index();
        if (currentPos == 0) {
            return;
        }
        int endPos = currentPos - 1;
        if (ignoreTokensAfterLastVisibleToken) {
            for (; endPos >= this.lastPosition2; endPos--) {
                org.antlr.runtime3_2_0.Token token = getTokenStream().get(endPos);
                int _channel = token.getChannel();
                if (_channel != 99) {
                    break;
                }
            }
        }
        StringBuilder hiddenTokenText = new StringBuilder();
        StringBuilder visibleTokenText = new StringBuilder();
        org.antlr.runtime3_2_0.CommonToken firstToken = null;
        for (int pos = this.lastPosition2; pos <= endPos; pos++) {
            org.antlr.runtime3_2_0.Token token = getTokenStream().get(pos);
            if (firstToken == null) {
                firstToken = (org.antlr.runtime3_2_0.CommonToken) token;
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
        layoutInformationAdapter.addLayoutInformation(new tudresden.ocl20.pivot.language.ocl.resource.ocl.mopp.OclLayoutInformation(syntaxElement, object, offset, hiddenTokenText.toString(), visibleTokenText.toString()));
        this.lastPosition2 = (endPos < 0 ? 0 : endPos + 1);
    }
}
