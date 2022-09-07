class BackupThread extends Thread {
    public Expression inline(ApplyExp exp, InlineCalls walker, boolean argsInlined) {
        exp.walkArgs(walker, argsInlined);
        Expression[] args = exp.getArgs();
        int nargs = args.length;
        if (nargs < 2) return exp;
        nargs--;
        Expression proc = args[0];
        boolean procSafeForMultipleEvaluation = !proc.side_effects();
        Expression[] inits1 = new Expression[1];
        inits1[0] = proc;
        LetExp let1 = new LetExp(inits1);
        Declaration procDecl = let1.addDeclaration("%proc", Compilation.typeProcedure);
        procDecl.noteValue(args[0]);
        Expression[] inits2 = new Expression[1];
        LetExp let2 = new LetExp(inits2);
        let1.setBody(let2);
        LambdaExp lexp = new LambdaExp(collect ? nargs + 1 : nargs);
        inits2[0] = lexp;
        Declaration loopDecl = let2.addDeclaration("%loop");
        loopDecl.noteValue(lexp);
        Expression[] inits3 = new Expression[nargs];
        LetExp let3 = new LetExp(inits3);
        Declaration[] largs = new Declaration[nargs];
        Declaration[] pargs = new Declaration[nargs];
        IsEq isEq = Scheme.isEq;
        for (int i = 0; i < nargs; i++) {
            String argName = "arg" + i;
            largs[i] = lexp.addDeclaration(argName);
            pargs[i] = let3.addDeclaration(argName, Compilation.typePair);
            inits3[i] = new ReferenceExp(largs[i]);
            pargs[i].noteValue(inits3[i]);
        }
        Declaration resultDecl = collect ? lexp.addDeclaration("result") : null;
        Expression[] doArgs = new Expression[1 + nargs];
        Expression[] recArgs = new Expression[collect ? nargs + 1 : nargs];
        for (int i = 0; i < nargs; i++) {
            doArgs[i + 1] = walker.walkApplyOnly(SlotGet.makeGetField(new ReferenceExp(pargs[i]), "car"));
            recArgs[i] = walker.walkApplyOnly(SlotGet.makeGetField(new ReferenceExp(pargs[i]), "cdr"));
        }
        if (!procSafeForMultipleEvaluation) proc = new ReferenceExp(procDecl);
        doArgs[0] = proc;
        Expression doit = walker.walkApplyOnly(new ApplyExp(new ReferenceExp(applyFieldDecl), doArgs));
        Expression rec = walker.walkApplyOnly(new ApplyExp(new ReferenceExp(loopDecl), recArgs));
        if (collect) {
            Expression[] consArgs = new Expression[2];
            consArgs[0] = doit;
            consArgs[1] = new ReferenceExp(resultDecl);
            recArgs[nargs] = Invoke.makeInvokeStatic(Compilation.typePair, "make", consArgs);
            lexp.body = rec;
        } else {
            lexp.body = new BeginExp(doit, rec);
        }
        let3.setBody(lexp.body);
        lexp.body = let3;
        Expression[] initArgs = new Expression[collect ? nargs + 1 : nargs];
        QuoteExp empty = new QuoteExp(LList.Empty);
        for (int i = nargs; --i >= 0; ) {
            Expression[] compArgs = new Expression[2];
            compArgs[0] = new ReferenceExp(largs[i]);
            compArgs[1] = empty;
            Expression result = collect ? (Expression) new ReferenceExp(resultDecl) : (Expression) QuoteExp.voidExp;
            lexp.body = new IfExp(walker.walkApplyOnly(new ApplyExp(isEq, compArgs)), result, lexp.body);
            initArgs[i] = args[i + 1];
        }
        if (collect) initArgs[nargs] = empty;
        Expression body = walker.walkApplyOnly(new ApplyExp(new ReferenceExp(loopDecl), initArgs));
        if (collect) {
            Expression[] reverseArgs = new Expression[1];
            reverseArgs[0] = body;
            body = Invoke.makeInvokeStatic(Compilation.scmListType, "reverseInPlace", reverseArgs);
        }
        let2.setBody(body);
        if (procSafeForMultipleEvaluation) return let2; else return let1;
    }
}
