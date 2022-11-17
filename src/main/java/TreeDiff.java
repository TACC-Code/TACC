import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import myutils.FuncForTreeDiff;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TreeDiff {
    private static Map<Integer, FuncForTreeDiff> allFunctions = new HashMap<>();
    private static Set<Integer> funcVisited = new HashSet<>();
    private static Set<Integer> parseError = new HashSet<>();
    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("/bdata2/yyh/LVMapper_Java/allFunctionMap.csv"))) {
            String DELIMITER = ",";
            String line;
            while ((line = br.readLine()) != null) {
                // 分割
                FuncForTreeDiff tmpFunc = new FuncForTreeDiff();
                //System.out.println(line);
                String[] columns = line.split(DELIMITER);
                try{
                    tmpFunc.funcID = Integer.parseInt(columns[0]);
                    tmpFunc.filePath = columns[1];
                    tmpFunc.startLine = Integer.parseInt(columns[2]);
                    tmpFunc.endLine = Integer.parseInt(columns[3]);
                    allFunctions.put(tmpFunc.funcID, tmpFunc);
                } catch (Exception e){
                    parseError.add(Integer.parseInt(columns[0]));
                    funcVisited.add(Integer.parseInt(columns[0]));
                    System.out.println(line);
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        List<String> treeDiff = new LinkedList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("/bdata2/wyk/analysis/sample1000w.csv"))) {
            String DELIMITER = ",";
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                int aFuncIndex = Integer.parseInt(columns[0]);
                int bFuncIndex = Integer.parseInt(columns[1]);

                if (!funcVisited.contains(aFuncIndex)) {
                    FuncForTreeDiff tmpFunc = allFunctions.get(aFuncIndex);
                    loadFunction(tmpFunc.filePath, tmpFunc.startLine, tmpFunc.endLine, aFuncIndex);
                    funcVisited.add(aFuncIndex);
                }
                if (!funcVisited.contains(bFuncIndex)) {
                    FuncForTreeDiff tmpFunc = allFunctions.get(bFuncIndex);
                    loadFunction(tmpFunc.filePath, tmpFunc.startLine, tmpFunc.endLine, bFuncIndex);
                    funcVisited.add(bFuncIndex);
                }
                if (!parseError.contains(aFuncIndex) && !parseError.contains(bFuncIndex)) {
                    float a1diffScore = astOneVerify(aFuncIndex, bFuncIndex);
                    float a2diffScore = astTwoVerify(aFuncIndex, bFuncIndex);
                    treeDiff.add(aFuncIndex+","+bFuncIndex+","+a1diffScore+","+a2diffScore);
                    System.out.println(a1diffScore+":"+a2diffScore);
                } else {
                    System.out.println(aFuncIndex+" or "+bFuncIndex + "error!");
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        File functionMap = new File("/bdata2/yyh/LVMapper_Java/treeDiff.csv");
        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(functionMap));

            for (String line : treeDiff) {
                writeText.write(line);
                writeText.newLine();    //换行
            }
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        }catch (IOException e){
            System.out.println("文件读写出错");
        }
    }

    private static float astOneVerify(int aFuncIndex, int bFuncIndex) {
        FuncForTreeDiff aFunc = allFunctions.get(aFuncIndex);
        FuncForTreeDiff bFunc = allFunctions.get(bFuncIndex);
        int commonNodes = getSubtreeCommonNode(aFunc.funcCU, bFunc.funcCU);

        return 1.0f - 1.0f*commonNodes/(aFunc.astNodeNum + bFunc.astNodeNum - commonNodes);
    }

    private static int getSubtreeCommonNode(Node root1, Node root2) {
        if (root1 == null || root2 == null || !root1.getClass().getSimpleName().equals(root2.getClass().getSimpleName())) {
            return 0;
        }
        List<Node> ch1 = root1.getChildNodes();
        List<Node> ch2 = root2.getChildNodes();
        int m = ch1.size(), n = ch2.size();

        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.max(dp[i-1][j],
                        Math.max(dp[i][j-1],
                                dp[i-1][j-1]+getSubtreeCommonNode(ch1.get(i-1), ch2.get(j-1))));
            }
        }

        return dp[m][n];
    }

    private static float astTwoVerify(int aFuncIndex, int bFuncIndex) {
        FuncForTreeDiff aFunc = allFunctions.get(aFuncIndex);
        FuncForTreeDiff bFunc = allFunctions.get(bFuncIndex);
        Map<Integer, Integer> aHashCnt = aFunc.astNodeHashCnt;
        Map<Integer, Integer> bHashCnt = bFunc.astNodeHashCnt;
        if(aHashCnt == null || bHashCnt == null) {
            return 0;
        }
        int overlap = 0;
        for (Map.Entry<Integer, Integer> entry: aHashCnt.entrySet()){
            if (bHashCnt.containsKey(entry.getKey())) {
                overlap += Math.min(entry.getValue(), bHashCnt.get(entry.getKey()));
            }
        }
        return 1.0f - 1.0f*overlap/(aFunc.astNodeNum + bFunc.astNodeNum - overlap);
    }

    private static void loadFunction(String filePath, int startLine, int endLine, int funcID) {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            sb.append("class a_ {\n");
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                if (line >= startLine) {
                    break;
                } else {
                    line++;
                }
            }
            sb.append(tempString+"\n");
            while(line>=startLine && line<endLine && (tempString = reader.readLine()) != null) {
                sb.append(tempString+"\n");
                line++;
            }
            reader.close();
            sb.append("}");
            
        } catch (IOException e) {
            System.out.println("error");
            funcVisited.add(funcID);
            parseError.add(funcID);
            return;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        CompilationUnit funcCU = null;
        try{
            funcCU = StaticJavaParser.parse(sb.toString());
        } catch(Exception e) {
            System.out.println(allFunctions.get(funcID).filePath+" "+allFunctions.get(funcID).startLine);
            funcVisited.add(funcID);
            parseError.add(funcID);
            return;
        }
        FuncForTreeDiff tmpFunc = allFunctions.get(funcID);
        tmpFunc.funcCU = funcCU;
        Map<Node, Integer> astNodeHash = getASTHashDict(funcCU);
        tmpFunc.astNodeNum = astNodeHash.size();
        tmpFunc.astNodeHashCnt = getHashCount(astNodeHash);
    }

    private static Map<Node, Integer> getASTHashDict(CompilationUnit cu) {
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

    private static Map<Integer, Integer> getHashCount(Map<Node, Integer> ASTHash) {
        Map<Integer, Integer> hashCount = new HashMap<>();
        for (int v: ASTHash.values()) {
            hashCount.put(v, hashCount.getOrDefault(v, 0)+1);
        }
        return hashCount;
    }
}
