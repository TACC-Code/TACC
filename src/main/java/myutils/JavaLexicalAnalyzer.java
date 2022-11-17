package myutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaLexicalAnalyzer {
    /*
     * 1表示关键字
     * 2表示标识符
     * 3表示常数
     * 4表示运算符
     * 5表示界符
     * 6表示字符串
     * */

    //关键字
    static String []keyWord={"private","protected","public","abstract","class","extends","final","implements",
            "interface","native","new","static","strictfp","break","continue","return","do","while","if","else","for",
            "instanceof","switch","case","default","boolean","byte","char","double","float","int","long","short",
            "String","null","true","false","void","this"};
    //运算符
    static String []operation={"+","-","*","/","%","++","--","-=","*=","/=","&","|","^","~","<<",">>",">>>","==","!=",
            ">","<","=",">=","<=","&&","||","!","."};
    //界符
    static String []symbol={",",";",":","(",")","{","}"};
    ArrayList<String> keyWords=null;
    ArrayList<String> operations=null;
    ArrayList<String> symbols=null;

    //指向当前所读到字符串的位置的指针
    int p;

    public JavaLexicalAnalyzer() {
        init();
    }

    //初始化把数组转换为ArrayList
    public void init(){
        keyWords=new ArrayList<>();
        operations=new ArrayList<>();
        symbols=new ArrayList<>();
        Collections.addAll(keyWords, keyWord);
        Collections.addAll(operations, operation);
        Collections.addAll(symbols, symbol);
    }

    public List<Integer> analyze(String str){
        List<Integer> res = new ArrayList<>();
        p=0;
        char ch;
        str=str.trim();
        for (;p<str.length();p++){
            ch=str.charAt(p);
            if (Character.isDigit(ch)){
                res.add(digitCheck(str).hashCode());
            }else if (Character.isLetter(ch)||ch=='_'){
                res.add(letterCheck(str).hashCode());
            }else if (ch=='"'){
                res.add(stringCheck(str).hashCode());
            }
            else if (ch==' '){
                continue;
            }else {
                res.add(symbolCheck(str).hashCode());
            }
        }
        return res;
    }

    /*数字的识别
     * 1、识别退出：
     *   1.1、遇到空格符
     *   1.2、遇到运算符或者界符
     * 2、错误情况：
     *   2.1、两个及以上小数点
     *   2.2、掺杂字母
     * */
    public String digitCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        //判断数字的小数点是否有且是否大于1
        int flag=0;
        boolean err=false;
        char ch;
        for (;p<str.length();p++) {
            ch = str.charAt(p);
            if (ch==' '||(!Character.isLetterOrDigit(ch)&&ch!='.')) {
                break;
            }else if (err){
                token+=ch;
            }
            else {
                token+=ch;
                if (ch == '.') {
                    if (flag == 1) {
                        err = true;
                    } else {
                        flag++;
                    }
                }else if (Character.isLetter(ch)){
                    err=true;
                }
            }
        }
        if (token.charAt(token.length()-1)=='.'){
            err=true;
        }
        if (p!=str.length()-1||(p==str.length()-1&&!Character.isDigit(str.charAt(p)))){
            p--;
        }
        return token;
    }

    //标识符，关键字的识别
    public String letterCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            if (!Character.isLetterOrDigit(ch)&&ch!='_'){
                break;
            }else{
                token+=ch;
            }
        }
        if (p!=str.length()-1||(p==str.length()-1&&(!Character.isLetterOrDigit(str.charAt(p))&&str.charAt(p)!='_'))){
            p--;
        }

        return token;
    }

    //符号的识别
    public String symbolCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        if (symbols.contains(token)){
            p--;
            return token;
        }else {
            if (operations.contains(token)){
                if (p<str.length()){
                    ch=str.charAt(p);
                    if (operations.contains(token+ch)){
                        token+=ch;
                        p++;
                        if (p<str.length()){
                            ch=str.charAt(p);
                            if (operations.contains(token+ch)){
                                token+=ch;
                                return token;
                            }else{
                                p--;
                                return token;
                            }
                        }else{
                            return token;
                        }
                    }else {
                        p--;
                        return token;
                    }
                }
            }else {
                p--;
                return token;
            }
        }
        return token;
    }

    //字符串检查
    public String stringCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            token+=ch;
            if (ch=='"'){
                break;
            }
        }
        return token;
    }
}

