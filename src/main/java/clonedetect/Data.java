package clonedetect;

import com.google.common.collect.ImmutableMap;
import myutils.Func;
import myutils.JavaLexicalAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Data {
    public Map<String, HashSet<Integer>> invertedIndex;
    public Map<Integer, Func> allFuncs;
    public int N;
    public String projectPath;
    public LinkedList<String> files;
    public static float filter_score = 0.1f;
    public static float verify_score = 0.5f;
    public static float notNeedASTVerify_score = 0.7f;
    public static float astFour_score = 0.85f;
    public static float astTwo_score = 0.65f;
    //public Map<String, Integer> GPT;
    public Map<Integer, HashSet<Integer>> clonePairs;
    public final static short edgeTypeNum = 100;
    public static Map<Integer, Short> edgeInfo = new HashMap<>(edgeTypeNum);
    //后续删除
    public Map<Integer, Map<Integer, Double>> edgeScores = new HashMap<>();


        public ImmutableMap<Object, Object> nodeAllType75= ImmutableMap.builder().put("ObjectCreationExpr",0 )
            .put("IntegerLiteralExpr",1 ).put("TypeParameter",2).put("SimpleName",3)
            .put("ExplicitConstructorInvocationStmt",4).put("LineComment",5).put("ExpressionStmt",6)
            .put("VariableDeclarator",7).put("Name",8).put("ArrayType",9).put("BreakStmt",10)
            .put("BooleanLiteralExpr",11).put("CastExpr",12).put("LongLiteralExpr",13).put("PrimitiveType",14)
            .put("SwitchEntry",15).put("NormalAnnotationExpr",16).put("BlockComment",17)
            .put("SwitchStmt",18).put("BlockStmt",19).put("CharLiteralExpr",20).put("ForEachStmt",21)
            .put("CatchClause",22).put("UnaryExpr",23).put("InstanceOfExpr",24).put("LocalClassDeclarationStmt",25)
            .put("WildcardType",26).put("ClassExpr",27).put("AssertStmt",28).put("ThrowStmt",29)
            .put("ReturnStmt",30).put("VoidType",31).put("ClassOrInterfaceDeclaration",32)
            .put("NameExpr",33).put("StringLiteralExpr",34).put("ClassOrInterfaceType",35).put("SynchronizedStmt",36)
            .put("FieldDeclaration",37).put("AssignExpr",38).put("SingleMemberAnnotationExpr",39)
            .put("MemberValuePair",40).put("WhileStmt",41).put("ArrayInitializerExpr",42)
            .put("ConstructorDeclaration",43).put("FieldAccessExpr",44).put("EmptyStmt",45)
            .put("Modifier",46).put("ArrayCreationExpr",47).put("ForStmt",48).put("ThisExpr",49)
            .put("DoStmt",50).put("VariableDeclarationExpr",51).put("SuperExpr",52).put("MethodDeclaration",53)
            .put("ArrayAccessExpr",54).put("TryStmt",55).put("BinaryExpr",56).put("MarkerAnnotationExpr",57)
            .put("ConditionalExpr",58).put("NullLiteralExpr",59).put("ContinueStmt",60).put("IfStmt",61)
            .put("DoubleLiteralExpr",62).put("Parameter",63).put("ArrayCreationLevel",64)
            .put("EnclosedExpr",65).put("MethodCallExpr",66).put("Expression",67).put("LambdaExpr", 68)
            .put("LiteralStringValueExpr",69).put("MethodReferenceExpr", 70).put("TextBlockLiteralExpr",71)
            .put("YieldStmt", 72).put("LocalRecordDeclarationStmt",73).put("LabeledStmt",74).build();

    public Data(int N, String projectPath, LinkedList<String> files) {
        // invertedIndex = Collections.synchronizedMap(new HashMap<>());
        // allFuncs = Collections.synchronizedMap(new HashMap<>());
        invertedIndex = new HashMap<>();
        allFuncs = new HashMap<>();
        this.N = N;
        this.projectPath = projectPath;
        this.files = files;
        this.clonePairs = new HashMap<>();
        Data.loadEdgeInfo("../../../data/edges1M.csv");
        //Data.loadEdgeInfo("/bdata2/yyh/LVMapper_Java/data/edges1M.csv");
        //this.clonePairs = Collections.synchronizedMap(new HashMap<>());
        //this.GPT = Collections.synchronizedMap(new HashMap<>());
    }


    public void addFunctions(Map<Integer, Func> funcs) {
        allFuncs.putAll(funcs);
    }

    public void updateInvertedIndex(Map<Integer, Func> funcs) {
        for (Map.Entry<Integer, Func> entry: funcs.entrySet()) {
            int k = entry.getKey();
            Func func = entry.getValue();
            for (String hashStr: func.nGramSequences) {
                if(invertedIndex.containsKey(hashStr)) {
                    invertedIndex.getOrDefault(hashStr, new HashSet<Integer>()).add(k);
                } else {
                    HashSet<Integer> tmpLS = new HashSet<>();
                    tmpLS.add(k);
                    invertedIndex.put(hashStr, tmpLS);
                }
            }
        }
    }

    public void setFuncNGram(Map<Integer, Func> funcs) {
        for (Func func: funcs.values()) {
            List<String> tokenSequence = func.normTokenSequence;
            int len = tokenSequence.size() - N + 1;
            for (int i = 0; i < len; i++) {
                StringBuffer temp_str = new StringBuffer();
                for (int j = 0; j < N; j++) {
                    temp_str.append(tokenSequence.get(i + j));
                }
                //func.nGramSequences.add(Hashing.murmur3_128().hashBytes(temp_str.getBytes()).toString());
                func.nGramSequences.add(String.valueOf(temp_str.toString().hashCode()));
            }
            //System.out.println(func.funcID+"  "+func.nGramSequences);
        }
    }

    //同时会得到func的token序列
    public void updateGPT(Map<Integer, Func> funcs, JavaLexicalAnalyzer javaLA) {
        for (Func func: funcs.values()) {
            for(String line: func.tokenSequence) {
                func.divTokenSequence.addAll(javaLA.analyze(line));
            }
            Collections.sort(func.divTokenSequence);
            // for (String token : func.divTokenSequence) {
            //     GPT.put(token, GPT.getOrDefault(token, 0)+1);
            // }
        }
    }

    // public void sortAllFuncDivTokens(){
    //     for (Func func : allFuncs.values()) {
    //         Collections.sort(func.divTokenSequence, new Comparator<String>() {
    //             @Override
    //             public int compare(String o1, String o2) {
    //                 if (GPT.getOrDefault(o1, 0) < GPT.getOrDefault(o2, 0)) {
    //                     return -1;
    //                 } else if (GPT.getOrDefault(o1, 0).equals(GPT.getOrDefault(o2, 0))) {
    //                     return 0;
    //                 } else {
    //                     return 1;
    //                 }
    //             }
    //         });
    //     }
    // }
    public static void loadEdgeInfo(String edgePath) {
        // 创建 reader
        try (BufferedReader br = new BufferedReader(new FileReader(edgePath))) {
            String DELIMITER = ",";
            String line;
            for (short i = 0; i < edgeTypeNum; i++) {
                line = br.readLine();
                String[] columns = line.split(DELIMITER);
                int edgeHash = (columns[0].hashCode()<<1) + columns[1].hashCode();
                edgeInfo.put(edgeHash, i);
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
