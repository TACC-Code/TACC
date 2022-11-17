package clonedetect;

import myutils.Func;
import myutils.FunctionExtractor;
import myutils.JavaLexicalAnalyzer;
import myutils.JavaLexicalAnalyzerNorm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.google.common.hash.Hashing;

public class PreProcess extends Thread{

    public Thread t;
    int start_file;
    int end_file;
    Data data;
    private final CountDownLatch latch;

    public PreProcess(int start_file, int end_file, Data data, CountDownLatch latch) {
        this.start_file = start_file;
        this.end_file = end_file;
        this.data = data;
        t = new Thread(this, "pre"+start_file);
        this.latch = latch;
    }

    @Override
    public void run(){
        FunctionExtractor fe = new FunctionExtractor();
        for (int i = start_file; i < end_file; i++) {
            Map<Integer, Func> tmpFuncs = fe.loadFile(data.files.get(i));
            this.data.addFunctions(tmpFuncs);
            setFuncNormLines(tmpFuncs);
            this.data.setFuncNGram(tmpFuncs);
            //对token进行排序
            this.data.updateGPT(tmpFuncs, new JavaLexicalAnalyzer());
            setFunctionASTRepresentation(tmpFuncs);
            // if (i % 10000 == 0) {
            //     System.out.println("File "+i + "processing done!");
            // }
        }
        latch.countDown();
    }

    private void setFuncNormLines(Map<Integer, Func> funcs) {
        JavaLexicalAnalyzerNorm jln = new JavaLexicalAnalyzerNorm();
        for (Func func: funcs.values()) {
            List<String> tokenSequence = func.tokenSequence;
            List<String> normTokenSequence = new ArrayList<>();
            for (String line : tokenSequence) {
                List<String> divLine = jln.analyze(line);
                StringBuffer sb = new StringBuffer();
                for(String token : divLine) {
                    sb.append(token);
                }
                //token之间没有空格,更换hash函数节省内存
                normTokenSequence.add(Hashing.murmur3_128().hashBytes(sb.toString().getBytes()).toString());
            }
            func.normTokenSequence = normTokenSequence;
        }
    }

    private void setFunctionASTRepresentation(Map<Integer, Func> funcs) {
        for (Func tmpFunc: funcs.values()) {
            // if(tmpFunc.funcID % 10000 == 0) {
            //     System.out.println(tmpFunc.funcID);
            //     System.out.println("使用内存"+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024); 
            // }
            StringBuffer sb = new StringBuffer();
            sb.append("class a_ {");
            for (String line : tmpFunc.tokenSequence) {
                sb.append(line);
            }
            sb.append("}");
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(sb.toString());
                //tmpFunc.funcCU = cu;
            } catch (Exception e){
                //System.out.println(tmpFunc.fileName+" startLine:"+tmpFunc.startLine+" endLine:"+tmpFunc.endLine+" parse ERROR!!!");
                return;
            }
            Map<Node, Integer> astNodeHash = getASTHashDict(cu);
            tmpFunc.astNodeNum = (short)astNodeHash.size();
            tmpFunc.astNodeHashCnt = getHashCount(astNodeHash);
            tmpFunc.astNodeAllType = getAstVector(cu);
            //之后不再用到，直接移除
            cu = null;
            tmpFunc.tokenSequence = null;
            tmpFunc.normTokenSequence = null;
        }
    }

    private Map<Node, Integer> getASTHashDict(CompilationUnit cu) {
        Deque<Node> stack = new ArrayDeque<>();
        Map<Node, Integer> visited = new HashMap<>();
        //Map<Node, Integer> nodeHeight = new HashMap<>();
        stack.push(cu);
        //nodeHeight.put(cu, 1); //存放节点权重: 2^q - 1
        while (!stack.isEmpty()) {
            Node node = stack.peek();
            //int h = nodeHeight.get(node);
            List<Node> childrenNodes = node.getChildNodes();
            /* 如果当前节点为叶子节点或者当前节点的子节点已经遍历过 */
            if (childrenNodes.size() == 0 || visited.containsKey(node)) {
                stack.pop();
                int tmpHash = node.getClass().getSimpleName().hashCode();
                for (Node child : childrenNodes) {
                    tmpHash += visited.get(child);
                }
                visited.put(node, tmpHash);
                continue;
            }
            for (int i = childrenNodes.size() - 1; i >= 0; --i) {
                stack.push(childrenNodes.get(i));
                //nodeHeight.put(childrenNodes.get(i), ((h+1)<<1) - 1);
            }
            visited.put(node, 0);
        }
        return visited;
    }

    private Map<Integer, Short> getHashCount(Map<Node, Integer> ASTHash) {
        Map<Integer, Short> hashCount = new HashMap<>();
        short defualtValue = 0;
        for (int v: ASTHash.values()) {
            hashCount.put(v, (short)(hashCount.getOrDefault(v, defualtValue)+1));
        }
        return hashCount;
    }

    private List<Short> getAstVector(CompilationUnit cu) {
        List<Short> vector = new ArrayList<>();
        int[] tmpNum = new int[76];
        List<Node> nodes = cu.getChildNodes();
        Queue<Node> queen = new LinkedList<>(nodes);
        while (!queen.isEmpty()) {
            Node tmpNode = queen.poll();
            String nodeName = tmpNode.getClass().getSimpleName();
            int label = (int)data.nodeAllType75.getOrDefault(nodeName, 75);
            tmpNum[label] += 1;
            for (Node n : tmpNode.getChildNodes()) {
                queen.offer(n);
            }
        }
        for (int i = 0; i < 76; i++) {
            vector.add((short)tmpNum[i]);
        }
        return vector;
    }

}
