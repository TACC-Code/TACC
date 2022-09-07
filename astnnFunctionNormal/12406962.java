class BackupThread extends Thread {
    @Primitive
    public static Value caml_parse_engine(final CodeRunner ctxt, final Value parserTables, final Value parserEnv, final Value cmd, final Value arg) {
        final Context cnt = ctxt.getContext();
        final PrintStream err;
        if (cnt.isParserTraceEnabled()) {
            err = new PrintStream(cnt.getChannel(Channel.STDERR).asOutputStream());
        } else {
            err = null;
        }
        final Block tables = parserTables.asBlock();
        final Block env = parserEnv.asBlock();
        int state = 0;
        int sp = 0;
        int asp = 0;
        int errFlag = 0;
        int n = 0;
        int n1 = 0;
        int n2 = 0;
        int m = 0;
        int state1 = 0;
        int gto = Parsing.NO_GOTO;
        switch(cmd.asLong()) {
            case Parsing.START:
                state = 0;
                sp = env.get(SP).asLong();
                errFlag = 0;
                gto = Parsing.GOTO_LOOP;
                break;
            case Parsing.TOKEN_READ:
                {
                    sp = env.get(Parsing.SP).asLong();
                    state = env.get(Parsing.STATE).asLong();
                    errFlag = env.get(Parsing.ERRFLAG).asLong();
                }
                if (arg.isBlock()) {
                    final Block bl = arg.asBlock();
                    env.set(Parsing.CURR_CHAR, tables.get(Parsing.TRANSL_BLOCK).asBlock().get(bl.getTag()));
                    env.set(Parsing.LVAL, bl.get(0));
                } else {
                    env.set(Parsing.CURR_CHAR, tables.get(Parsing.TRANSL_CONST).asBlock().get(arg.asLong()));
                    env.set(Parsing.LVAL, Value.ZERO);
                }
                if (err != null) printToken(err, tables, state, arg);
                gto = Parsing.GOTO_TESTSHIFT;
                break;
            case Parsing.ERROR_DETECTED:
                {
                    sp = env.get(Parsing.SP).asLong();
                    state = env.get(Parsing.STATE).asLong();
                    errFlag = env.get(Parsing.ERRFLAG).asLong();
                }
                gto = Parsing.GOTO_RECOVER;
                break;
            case Parsing.STACKS_GROWN_1:
                {
                    sp = env.get(Parsing.SP).asLong();
                    state = env.get(Parsing.STATE).asLong();
                    errFlag = env.get(Parsing.ERRFLAG).asLong();
                }
                gto = Parsing.GOTO_PUSH;
                break;
            case Parsing.STACKS_GROWN_2:
                {
                    sp = env.get(Parsing.SP).asLong();
                    state = env.get(Parsing.STATE).asLong();
                    errFlag = env.get(Parsing.ERRFLAG).asLong();
                }
                gto = Parsing.GOTO_SEMANTIC_ACTION;
                break;
            case Parsing.SEMANTIC_ACTION_COMPUTED:
                {
                    sp = env.get(Parsing.SP).asLong();
                    state = env.get(Parsing.STATE).asLong();
                    errFlag = env.get(Parsing.ERRFLAG).asLong();
                }
                env.get(Parsing.S_STACK).asBlock().set(sp, Value.createFromLong(state));
                env.get(Parsing.V_STACK).asBlock().set(sp, arg);
                asp = env.get(Parsing.ASP).asLong();
                env.get(Parsing.SYMB_END_STACK).asBlock().set(sp, env.get(Parsing.SYMB_END_STACK).asBlock().get(asp));
                if (sp > asp) {
                    env.get(Parsing.SYMB_START_STACK).asBlock().set(sp, env.get(Parsing.SYMB_END_STACK).asBlock().get(asp));
                }
                gto = Parsing.GOTO_LOOP;
                break;
            default:
                assert false : "invalid command";
                return Parsing.RAISE_PARSE_ERROR;
        }
        while (true) {
            switch(gto) {
                case Parsing.GOTO_LOOP:
                    n = Lexing.getShort(tables.get(Parsing.DEFRED), state);
                    if (n != 0) {
                        gto = Parsing.GOTO_REDUCE;
                        break;
                    }
                    if (env.get(Parsing.CURR_CHAR).asLong() >= 0) {
                        gto = Parsing.GOTO_TESTSHIFT;
                        break;
                    }
                    {
                        env.set(Parsing.SP, Value.createFromLong(sp));
                        env.set(Parsing.STATE, Value.createFromLong(state));
                        env.set(Parsing.ERRFLAG, Value.createFromLong(errFlag));
                    }
                    return Parsing.READ_TOKEN;
                case Parsing.GOTO_REDUCE:
                    if (err != null) err.printf("State %d: reduce by rule %d\n", state, n);
                    m = Lexing.getShort(tables.get(Parsing.LEN), n);
                    env.set(Parsing.ASP, Value.createFromLong(sp));
                    env.set(Parsing.RULE_NUMBER, Value.createFromLong(n));
                    env.set(Parsing.RULE_LEN, Value.createFromLong(m));
                    sp = sp - m + 1;
                    m = Lexing.getShort(tables.get(Parsing.LHS), n);
                    state1 = env.get(Parsing.S_STACK).asBlock().get(sp - 1).asLong();
                    n1 = Lexing.getShort(tables.get(Parsing.GINDEX), m);
                    n2 = n1 + state1;
                    if ((n1 != 0) && (n2 >= 0) && (n2 <= tables.get(Parsing.TABLESIZE).asLong()) && (Lexing.getShort(tables.get(Parsing.CHECK), n2) == state1)) {
                        state = Lexing.getShort(tables.get(Parsing.TABLE), n2);
                    } else {
                        state = Lexing.getShort(tables.get(Parsing.DGOTO), m);
                    }
                    if (sp < env.get(Parsing.STACKSIZE).asLong()) {
                        gto = Parsing.GOTO_SEMANTIC_ACTION;
                        break;
                    }
                    {
                        env.set(Parsing.SP, Value.createFromLong(sp));
                        env.set(Parsing.STATE, Value.createFromLong(state));
                        env.set(Parsing.ERRFLAG, Value.createFromLong(errFlag));
                    }
                    return Parsing.GROW_STACKS_2;
                case Parsing.GOTO_TESTSHIFT:
                    n1 = Lexing.getShort(tables.get(Parsing.SINDEX), state);
                    n2 = n1 + env.get(Parsing.CURR_CHAR).asLong();
                    if ((n1 != 0) && (n2 >= 0) && (n2 <= tables.get(Parsing.TABLESIZE).asLong()) && (Lexing.getShort(tables.get(Parsing.CHECK), n2) == env.get(Parsing.CURR_CHAR).asLong())) {
                        gto = Parsing.GOTO_SHIFT;
                        break;
                    }
                    n1 = Lexing.getShort(tables.get(Parsing.RINDEX), state);
                    n2 = n1 + env.get(Parsing.CURR_CHAR).asLong();
                    if ((n1 != 0) && (n2 >= 0) && (n2 <= tables.get(TABLESIZE).asLong()) && (Lexing.getShort(tables.get(Parsing.CHECK), n2) == env.get(Parsing.CURR_CHAR).asLong())) {
                        n = Lexing.getShort(tables.get(Parsing.TABLE), n2);
                        gto = Parsing.GOTO_REDUCE;
                        break;
                    }
                    if (errFlag > 0) {
                        gto = Parsing.GOTO_RECOVER;
                        break;
                    }
                    {
                        env.set(Parsing.SP, Value.createFromLong(sp));
                        env.set(Parsing.STATE, Value.createFromLong(state));
                        env.set(Parsing.ERRFLAG, Value.createFromLong(errFlag));
                    }
                    return Parsing.CALL_ERROR_FUNCTION;
                case Parsing.GOTO_SHIFT:
                    env.set(Parsing.CURR_CHAR, Value.MINUS_ONE);
                    if (errFlag > 0) {
                        errFlag--;
                    }
                    gto = Parsing.GOTO_SHIFT_RECOVER;
                    break;
                case Parsing.GOTO_RECOVER:
                    if (errFlag < 3) {
                        errFlag = 3;
                        while (true) {
                            state1 = env.get(Parsing.S_STACK).asBlock().get(sp).asLong();
                            n1 = Lexing.getShort(tables.get(Parsing.SINDEX), state1);
                            n2 = n1 + Parsing.ERRCODE;
                            if ((n1 != 0) && (n2 >= 0) && (n2 <= tables.get(Parsing.TABLESIZE).asLong()) && (Lexing.getShort(tables.get(Parsing.CHECK), n2) == Parsing.ERRCODE)) {
                                if (err != null) err.printf("Recovering in state %d\n", state1);
                                gto = Parsing.GOTO_SHIFT_RECOVER;
                                break;
                            } else {
                                if (err != null) err.printf("Discarding state %d\n", state1);
                                if (sp <= env.get(Parsing.STACKBASE).asLong()) {
                                    if (err != null) err.printf("No more states to discard\n");
                                    return Parsing.RAISE_PARSE_ERROR;
                                }
                                sp--;
                            }
                        }
                    } else {
                        if (env.get(Parsing.CURR_CHAR).asLong() == 0) {
                            return Parsing.RAISE_PARSE_ERROR;
                        }
                        if (err != null) err.printf("Discarding last token read\n");
                        env.set(Parsing.CURR_CHAR, Value.MINUS_ONE);
                        gto = Parsing.GOTO_LOOP;
                        break;
                    }
                    gto = Parsing.GOTO_SHIFT;
                    break;
                case Parsing.GOTO_SHIFT_RECOVER:
                    if (err != null) err.printf("State %d: shift to state %d\n", state, Lexing.getShort(tables.get(Parsing.TABLE), n2));
                    state = Lexing.getShort(tables.get(Parsing.TABLE), n2);
                    sp++;
                    if (sp < env.get(Parsing.STACKSIZE).asLong()) {
                        gto = Parsing.GOTO_PUSH;
                        break;
                    }
                    {
                        env.set(Parsing.SP, Value.createFromLong(sp));
                        env.set(Parsing.STATE, Value.createFromLong(state));
                        env.set(Parsing.ERRFLAG, Value.createFromLong(errFlag));
                    }
                    return Parsing.GROW_STACKS_1;
                case Parsing.GOTO_PUSH:
                    env.get(Parsing.S_STACK).asBlock().set(sp, Value.createFromLong(state));
                    env.get(Parsing.V_STACK).asBlock().set(sp, env.get(Parsing.LVAL));
                    env.get(Parsing.SYMB_START_STACK).asBlock().set(sp, env.get(Parsing.SYMB_START));
                    env.get(Parsing.SYMB_END_STACK).asBlock().set(sp, env.get(Parsing.SYMB_END));
                    gto = Parsing.GOTO_LOOP;
                    break;
                case Parsing.GOTO_SEMANTIC_ACTION:
                    {
                        env.set(Parsing.SP, Value.createFromLong(sp));
                        env.set(Parsing.STATE, Value.createFromLong(state));
                        env.set(Parsing.ERRFLAG, Value.createFromLong(errFlag));
                    }
                    return Parsing.COMPUTE_SEMANTIC_ACTION;
                default:
                    assert false : "invalid goto";
                    return Parsing.RAISE_PARSE_ERROR;
            }
        }
    }
}
