package myutils;

import com.github.javaparser.ast.CompilationUnit;

import clonedetect.Data;

import java.util.*;
public class Func {
    public short startLine;
    public short endLine;
    public List<String> tokenSequence;
    public List<String> normTokenSequence;
    public String fileName;
    public int funcID;
    public List<String> nGramSequences;
    public List<Short> transformAST;
    public Map<Integer, Short> astNodeHashCnt;
    public List<Integer> divTokenSequence;
    private static int funcCount = 0;
    public List<Short> astNodeAllType;
    public short astNodeNum = 1;
    public boolean astNotVisted = true;
    public short funcLen = 1;
    public Func() {
        funcID = funcCount++;
        tokenSequence = new ArrayList<>();
        nGramSequences = new ArrayList<>();
        divTokenSequence = new ArrayList<>();
        normTokenSequence = new ArrayList<>();
        astNodeAllType = new ArrayList<>();
        astNodeHashCnt = new HashMap<>();
        //transformAST = Collections.synchronizedList(new ArrayList<>());
        transformAST = new ArrayList<>();
        edgeInfoArr = new int[Data.edgeTypeNum];
    }

    public int getFuncLen(){
        return endLine - startLine;
    }

    public int getCuNodeNum() {
        return transformAST.size();
    }

}
