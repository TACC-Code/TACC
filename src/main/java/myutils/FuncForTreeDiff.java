package myutils;

import com.github.javaparser.ast.CompilationUnit;

import java.util.Map;

public class FuncForTreeDiff {
    public String filePath;
    public int startLine;
    public int endLine;
    public int funcID;
    public CompilationUnit funcCU;
    public Map<Integer, Integer> astNodeHashCnt;
    public int astNodeNum;
}
