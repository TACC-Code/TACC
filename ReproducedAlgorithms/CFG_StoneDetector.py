import hashlib
import networkx as nx
import ast
from enum import Enum
import re
import javalang
import sys
import os
import copy
from CFG_generation import cfg_generation
import pickle
# Constant Pool is used to convert from Domonator Tree to Abstract Dominator Tree
class ConstantPool(Enum):
    # Constant Pool in the paper
    StringBuffer = "StringBuffer" #0
    length = "length" #1
    indexOf = "indexOf" #2
    append = "append" #3
    substringint = "substring" #4
    substringintint = "substring" #5
    toString = "toString" #6
    
    # expand the ConstantPool 
    File = "File" #7
    getName = "getName" #8
    exists = "exists" #9
    createNewFile = "createNewFile" #10
    FileInputStream = "FileInputStream" #11
    getChannel = "getChannel" #12
    FileOutputStream = "FileOutputStream" #13
    transferFrom = "transferFrom" #14
    size = "size" #15
    close = "close" #16
    printStackTrace = "printStackTrace" #17
    mkdirs = "mkdirs" #18
    transferTo = "transferTo" #19
    allocateDirect = "allocateDirect" #20
    setState = "setState" #21
    read = "read" #22
    flip = "flip" #23
    write = "write" #24
    clear = "clear" #25
    FileReader ="FileReader" #26
    FileWriter = "FileWriter" #27
    copy = "copy" #28
    InvocationTargetException = "InvocationTargetException" #29
    ManagedMemoryDataSource = "ManagedMemoryDataSource" #30
    DataHandler = "DataHandler" #31
    getInputStream = "getInputStream" #32
    error = "error" #33
    getMessage = "getMessage" #34
    RestartInputStream = "RestartInputStream" #35
    identifyResource = "identifyResource" #36
    restart = "restart" #37
    reportNotification = "reportNotification" #38
    createLocalizedNotification = "createLocalizedNotification" #39
    Object = "Object" #40
    getURI = "getURI" #41
    getTypeName = "getTypeName" #42
    getVersionName = "getVersionName" #43
    processMigrationSteps = "processMigrationSteps" #44
    copyAndClose = "copyAndClose" #45
    createOutputStream = "createOutputStream" #46
    getProperty = "getProperty" #47
    println = "System.out.println" #48
    GZIPInputStream = "GZIPInputStream" #49


def ReadDotFile (path):

    # read .dot (pydot)
    CFG = nx.drawing.nx_pydot.read_dot(path)
    CFG = nx.DiGraph(CFG)
    return CFG

def DominatorTreeGen(CFG,filename):
    entry_node = []
    for node,indegree in CFG.in_degree():
        if indegree == 0:
            entry_node.append(node)
    CFG.add_node('0')
    CFG.nodes['0']['label'] = '"-1"'
    for node in entry_node:
        CFG.add_edge('0',node)
    s_node = list(CFG.nodes())[-1]
    dominator_tree_edges = []
    # dominator_tree_edges = sorted(nx.immediate_dominators(CFG,s_node).items())
    # print(dominator_tree_edges)
    dom_dict = nx.immediate_dominators(CFG,s_node)
    del dom_dict['0']
    for v,idomv in dom_dict.items():
        dominator_tree_edges.append((idomv,v))
    # print(dominator_tree_edges)
    DominatorTree = nx.DiGraph()
    for node in CFG.nodes(data=True):
    #if node != ('\\n', {}):
        label = ast.literal_eval(node[1]['label'])
        node[1]['label'] = label
    DominatorTree.add_nodes_from(CFG.nodes(data=True))
    DominatorTree.add_edges_from(dominator_tree_edges)
    
    return DominatorTree

def Old_DominatorTreeGen(CFG,filename):
    dominator_tree_edges = []
    # find start node
    for node,in_degree in CFG.in_degree(): 
        if in_degree == 0:# and node != '\\n'
            s_node = node
    #s_node = 'v1'
    # build Dominator Tree
    for node in CFG.nodes():
        # strat node is not dominator node
        if node == s_node:# or node == '\\n'
            continue
        else:  
            path_list = []
            dominate_nodes = []
            for path in nx.all_simple_paths(CFG,source=s_node,target=node):
                path_list.append(path)
            # find dominator node
            if len(path_list) == 1 :
                dominate_nodes =  path_list[0]
            else:
                dominate_nodes =  path_list[0]
                for path in path_list:
                    common_list = []
                    for ele1 in dominate_nodes:
                        for ele2 in path:
                            if ele1 == ele2:
                                common_list.append(ele1)
                    dominate_nodes = common_list
            
            # build Dominator Tree edges set (same nodes) 
            i = 0
            while i+1<len(dominate_nodes):
                (u,v) = (dominate_nodes[i],dominate_nodes[i+1])
                if (u,v) in CFG.edges():
                   dominator_tree_edges.append((u,v)) 
                i = i + 1
            dominator_tree_edges = list(set(dominator_tree_edges))
    # entry_node = []
    # for node,in_degree in CFG.in_degree(): 
    #     if in_degree == 0:# and node != '\\n'
    #         entry_node.append(node)
    # CFG.add_node('-1')
    # CFG.nodes['-1']['label'] = '"URL,URL"'
    # for node in entry_node:
    #     CFG.add_edge('-1',node)
    # s_node = list(CFG.nodes())[-1]
    # dominator_tree_edges = []
    # for node in CFG.nodes():
    #     if node == s_node:# or node == '\\n'
    #         continue
    #     else: 
    #         path = list(nx.all_simple_paths(CFG,source=s_node,target=node))[0]
    #         for ifdomnode in path:
    #             tempcfg = copy.deepcopy(CFG)
    #             if ifdomnode == node:
    #                 continue
    #             elif ifdomnode == s_node:
    #                 idomnode = s_node
    #                 continue
    #             else:
    #                 tempcfg.remove_node(str(ifdomnode))
    #                 nowpath = nx.all_simple_paths(tempcfg,source=s_node,target=node)
    #                 if len(list(nowpath)) == 0:
    #                     idomnode = ifdomnode
    #         dominator_tree_edges.append((idomnode,node))
    DominatorTree = nx.DiGraph()
    for node in CFG.nodes(data=True):
        #if node != ('\\n', {}):
        label = ast.literal_eval(node[1]['label'])
        node[1]['label'] = label
    DominatorTree.add_nodes_from(CFG.nodes(data=True))
    DominatorTree.add_edges_from(dominator_tree_edges)

    # draw Dominator Tree, and save in ./DominatorTree/ with name XXXX_DominatorTree.png
    # DominatorTreeGraph = nx.drawing.nx_agraph.to_agraph(DominatorTree)
    # DominatorTreeGraph.layout('dot')
    # picturename = "./DominatorTree/"+filename+'_DominatorTree.png'
    # DominatorTreeGraph.draw(picturename)
    
    return DominatorTree

def JavaSrc2tokens(path):
    # this function is used to get token to help build Abstract Dominator Tree
    file = open(path,"r",encoding="utf-8")
    tokens = list(javalang.tokenizer.tokenize(file.read()))
    type_dict = {}
    for token in tokens:
        token_type = str(type(token))[:-2].split(".")[-1]
        token_value = token.value
        if token_value not in type_dict:
            type_dict[token_value] = token_type
        else:
            if type_dict[token_value] != token_type:
                # same token value has different token type
                print("!!!!!!!!!")
    file.close()
    return type_dict

def ADTGen(DT,typedict,filename):
    # this function is used to build Abstract Dominator Tree
    AbstractDominatorTree = nx.DiGraph()
    AbstractDominatorTree.add_edges_from(DT.edges())
    ADT_node_list = []
    condnode = []
    # find condition node
    for node,out_degree in DT.out_degree():
        if out_degree > 1:
            condnode.append(node)
    for node in DT.nodes(data=True):
        abstract_label = []
        
        # get label of each Dominator Tree node
        label = node[1]['label']
        
        # find parameter list
        par_list = []
        p1 = re.compile(r'[(](.*)[)]', re.S)#Greed match
        if len(re.findall(p1, label)) !=0 and re.findall(p1, label)[0] != "":
            # match outer parameters
            parameter1 = re.findall(p1, label)[0]
            # print('parameter1:',parameter1)
            if len(re.findall(p1, parameter1)) != 0 and re.findall(p1, parameter1)[0] != "":
                # match inner parameters
                parameter2 = re.findall(p1, parameter1)[0]
                # print('parameter2:',parameter2)
                if ", " in parameter2:
                    par_list = parameter2.split(", ")
                else:
                    par_list.append(parameter2)
            else:
                if ", " in parameter1:
                    par_list = parameter1.split(", ")
                else:
                    par_list.append(parameter1)
        else:
            par_list = []

        # condition node contain "COND"
        if node[0] in condnode:
            abstract_label.append("COND")
        if "!" in label:
            abstract_label.append("NOT")
        instruction = label.split(" ")

        # keyword is reserved and converted to uppercase
        # process operator
        for words in instruction:
            if words in typedict.keys():
                if typedict[words] == 'Operator':
                    if words == "+=":
                        abstract_label.append("=") 
                        abstract_label.append("V") 
                        abstract_label.append("+")
                    elif words == "-=":
                        abstract_label.append("=") 
                        abstract_label.append("V") 
                        abstract_label.append("-")
                    else:    
                        abstract_label.append(words)
                # print('par_list:',par_list)
                for par in par_list:
                    if words in par and typedict[words] == 'Operator':
                        # print('par',par)
                        # print('words:',words)
                        # print('abstract_label:',abstract_label)
                        if str(words) in abstract_label:
                            abstract_label.remove(str(words))

        # process "return" & "empty" & "ANY" 
        for words in instruction:            
            if words in typedict.keys() and words == "return": 
                if typedict[words] == 'Keyword':
                    abstract_label.append(words.upper())
            if words == "empty" or words == "ANY":
                abstract_label.append(words.upper())
        for words in instruction:
            if words in typedict.keys():
                if typedict[words] == 'Identifier':
                    abstract_label.append("V")
                for par in par_list:
                    if words in par and typedict[words] == 'Identifier':
                        if 'V' in abstract_label:
                            abstract_label.remove("V")

        # process type Keyword and covert to uppercase
        for words in instruction:            
            if words in typedict.keys() and words != "return": 
                if typedict[words] == 'Keyword':
                    abstract_label.append(words.upper()) 

        # process type Literals
        for words in instruction:                     
            if words in typedict.keys(): 
                if typedict[words] == 'DecimalInteger':
                    abstract_label.append("L")
            if words in typedict.keys(): 
                if typedict[words] == 'String':
                    abstract_label.append("L")

        # process function and negative number
        for words in instruction:
            if words == "true":
                abstract_label.append("L")
            if words == "false":
                abstract_label.append("L")
            
            # negative number consist of operator and number
            if is_number(words):
                if float(words) < 0:
                    abstract_label.append("-")
                    abstract_label.append("L")
            
            # Constructor  var = new XXX();
            if "(" in words and ")" in words and "new" in label:
                constructor = words.split("(")[0]
                
                # find function list 
                match_label_list = ConstantPoolMatch(constructor,par_list)
                for match_label in match_label_list:
                    abstract_label.append(match_label)
                if len(par_list) != 0:
                    for par in par_list:
                        if par in typedict.keys():
                            if typedict[par] == 'Identifier':
                                abstract_label.append("V") 
                            if typedict[par] == 'Keyword':
                                abstract_label.append(words.upper())
                            if typedict[par] == 'DecimalInteger':
                                abstract_label.append("L")
                            if typedict[par] == 'String':
                                abstract_label.append("L")
                        else:
                            if len(re.findall("\.(.*?)\(", par)) != 0:
                                functionlist = re.findall("\.(.*?)\(", par)
                                for function in functionlist:
                                    match_label_list = ConstantPoolMatch(function,par_list)
                                    for match_label in match_label_list:
                                        abstract_label.append(match_label)
                        if "." in par :
                            if par.split(".")[1] in typedict.keys():
                                if typedict[par.split(".")[1]] == 'Identifier':
                                    abstract_label.append("V") 
                                if typedict[par.split(".")[1]] == 'Keyword':
                                    abstract_label.append(words.upper())
                                if typedict[par.split(".")[1]] == 'DecimalInteger':
                                    abstract_label.append("L")
                                if typedict[par.split(".")[1]] == 'String':
                                    abstract_label.append("L")
 
                    continue    
                       
            # function  .XXX()
            elif len(re.findall("\.(.*?)\(", words)) != 0:
                functionlist = []  
                functionlist = re.findall("\.(.*?)\(", words)
                if "(" in label :
                    functionlist.append(label.split("(")[0])   
                    
                # delete function in par list
                for function in functionlist:
                    for par in par_list:
                        if function in par:
                            if function in functionlist:
                                functionlist.remove(function)
                
                # match function in Constant Pool
                for function in functionlist:
                    match_label_list = ConstantPoolMatch(function,par_list)
                    for match_label in match_label_list:
                        abstract_label.append(match_label)
                
                # process parameter
                for par in par_list:
                    if par in typedict.keys():
                        if typedict[par] == 'Identifier':
                            abstract_label.append("V") 
                        if typedict[par] == 'Keyword':
                            abstract_label.append(words.upper())
                        if typedict[par] == 'DecimalInteger':
                            abstract_label.append("L")
                        if typedict[par] == 'String':
                            abstract_label.append("L")
                    else:
                        functionlist = re.findall("\.(.*?)\(", par)
                        for function in functionlist:
                            match_label_list = ConstantPoolMatch(function,par_list)
                            for match_label in match_label_list:
                                abstract_label.append(match_label)
                    if "." in par :
                        if par.split(".")[1] in typedict.keys():
                            if typedict[par.split(".")[1]] == 'Identifier':
                                abstract_label.append("V") 
                            if typedict[par.split(".")[1]] == 'Keyword':
                                abstract_label.append(words.upper())
                            if typedict[par.split(".")[1]] == 'DecimalInteger':
                                abstract_label.append("L")
                            if typedict[par.split(".")[1]] == 'String':
                                abstract_label.append("L")

            # function XXX()
            elif "()" in words :
                if  words.split("()")[0] in typedict.keys():
                    function = words.split("()")[0]
                    match_label_list = ConstantPoolMatch(function,par_list)
                    for match_label in match_label_list:
                        if match_label != "CALL":
                            abstract_label.append(match_label)
            
            # var XXX.XXX
            elif "(" not in words and ")" not in words and "." in words:
                if  words.split(".")[1] in typedict.keys():
                    if typedict[words.split(".")[1] ] == 'Identifier':
                        abstract_label.append("V") 
                    if typedict[words.split(".")[1] ] == 'Keyword':
                        abstract_label.append(words.upper())
                    if typedict[words.split(".")[1] ] == 'DecimalInteger':
                        abstract_label.append("L")
                    if typedict[words.split(".")[1] ] == 'String':
                        abstract_label.append("L")
                    # elif "&quot;" in string:
                    #     if "\""+string.split("&quot;")[1]+"\"" in typedict.keys():
                    #         if typedict["\""+string.split("&quot;")[1]+"\""] == 'Identifier':
                    #             abstract_label.append("V") 
                    #         if typedict["\""+string.split("&quot;")[1]+"\""] == 'Keyword':
                    #             abstract_label.append("\""+string.split("&quot;")[1]+"\"".upper())
                    #         if typedict["\""+string.split("&quot;")[1]+"\""] == 'DecimalInteger':
                    #             abstract_label.append("L")
                    #         if typedict["\""+string.split("&quot;")[1]+"\""] == 'String':
                    #             abstract_label.append("L")
        
        # process join parameter  
        for par in par_list:    
            if " + " in par :
                count = par.count(" + ")
                for i in range(0,count):
                    abstract_label.append("+") 
                string_list = par.split(" + ")
                for string in string_list:
                    if string in typedict.keys():
                        if typedict[string] == 'Identifier':
                            abstract_label.append("V") 
                        if typedict[string] == 'Keyword':
                            abstract_label.append(words.upper())
                        if typedict[string] == 'DecimalInteger':
                            abstract_label.append("L")
                        if typedict[string] == 'String':
                            abstract_label.append("L") 
                    elif "." in string and "(" not in string and ")" not in string and "&quot;" not in string:
                        if string.split(".")[1] in typedict.keys():
                            if typedict[string.split(".")[1]] == 'Identifier':
                                abstract_label.append("V") 
                            if typedict[string.split(".")[1]] == 'Keyword':
                                abstract_label.append(words.upper())
                            if typedict[string.split(".")[1]] == 'DecimalInteger':
                                abstract_label.append("L")
                            if typedict[string.split(".")[1]] == 'String':
                                abstract_label.append("L") 
                    elif "&quot;" in string:
                        if "\""+string.split("&quot;")[1]+"\"" in typedict.keys():
                            if typedict["\""+string.split("&quot;")[1]+"\""] == 'Identifier':
                                abstract_label.append("V") 
                            if typedict["\""+string.split("&quot;")[1]+"\""] == 'Keyword':
                                abstract_label.append("\""+string.split("&quot;")[1]+"\"".upper())
                            if typedict["\""+string.split("&quot;")[1]+"\""] == 'DecimalInteger':
                                abstract_label.append("L")
                            if typedict["\""+string.split("&quot;")[1]+"\""] == 'String':
                                abstract_label.append("L")

        # from label to abstract label
        ADT_node = node
        ADT_node[1]["label"] = abstract_label
        ADT_node_list.append(ADT_node)

    # remove quote in abstract label list and convert to string
    for node in ADT_node_list:
        node[1]["label"] = str(node[1]["label"])
        # here we transform ['XXX','XXX','XXX'] to str '[XXX,XXX,XXX]'
        node[1]["label"] = QuoteRemove(node[1]["label"])

    # Abstract Dominator Tree and Dominator Tree has the same edges
    AbstractDominatorTree.add_nodes_from(ADT_node_list)

    # draw Abstrate Dominator Tree, and save in ./AbstractDominatorTree/ with name XXXX_ADTree.png
    # AbstractDominatorTreeGraph = nx.drawing.nx_agraph.to_agraph(AbstractDominatorTree)
    # AbstractDominatorTreeGraph.layout('dot')
    # picturename = "./AbstractDominatorTree/"+filename+"_ADTree.png"
    # AbstractDominatorTreeGraph.draw(picturename)
    # print()
    return AbstractDominatorTree

def ConstantPoolMatch(function,par_list):
    # this function is used to process function match
    # be divided into constructors and class methods
    # constructors consist of concrete function serial number in Constant Pool
    # class methods consist of "CALL" and concrete function serial number in Constant Pool

    match_label = []
    if ConstantPool.StringBuffer.value == function:
        match_label.append("#0")
    if ConstantPool.length.value == function:
        match_label.append("CALL")
        match_label.append("#1")
    if ConstantPool.indexOf.value == function:
        match_label.append("CALL")
        match_label.append("#2")
    if ConstantPool.append.value == function:
        match_label.append("CALL")
        match_label.append("#3")
    # function reconstruction of substringint
    if ConstantPool.substringint.value == function and len(par_list) == 1:
        match_label.append("CALL")
        match_label.append("#4")
    if ConstantPool.substringintint.value == function and len(par_list) == 2:
        match_label.append("CALL")
        match_label.append("#5")
    if ConstantPool.toString.value == function:
        match_label.append("CALL")
        match_label.append("#6")
    if ConstantPool.File.value == function :
        match_label.append("#7")
    if ConstantPool.getName.value == function:
        match_label.append("CALL")
        match_label.append("#8")
    if ConstantPool.exists.value == function:
        match_label.append("CALL")
        match_label.append("#9")
    if ConstantPool.createNewFile.value == function:
        match_label.append("CALL")
        match_label.append("#10")
    if ConstantPool.FileInputStream.value == function:
        match_label.append("#11")
    if ConstantPool.getChannel.value == function :
        match_label.append("CALL")
        match_label.append("#12")
    if ConstantPool.FileOutputStream.value == function:
        match_label.append("#13")
    if ConstantPool.transferFrom.value == function:
        match_label.append("CALL")
        match_label.append("#14")
    if ConstantPool.size.value == function:
        match_label.append("CALL")
        match_label.append("#15")
    if ConstantPool.close.value == function:
        match_label.append("CALL")
        match_label.append("#16")
    if ConstantPool.printStackTrace.value == function:
        match_label.append("CALL")
        match_label.append("#17")
    if ConstantPool.mkdirs.value == function:
        match_label.append("CALL")
        match_label.append("#18")
    if ConstantPool.transferTo.value == function:
        match_label.append("CALL")
        match_label.append("#19")
    if ConstantPool.allocateDirect.value == function:
        match_label.append("CALL")
        match_label.append("#20")
    if ConstantPool.setState.value == function:
        match_label.append("CALL")
        match_label.append("#21")
    if ConstantPool.read.value == function:
        match_label.append("CALL")
        match_label.append("#22")
    if ConstantPool.flip.value == function:
        match_label.append("CALL")
        match_label.append("#23")
    if ConstantPool.write.value == function:
        match_label.append("CALL")
        match_label.append("#24")
    if ConstantPool.clear.value == function:
        match_label.append("CALL")
        match_label.append("#25")
    if ConstantPool.FileReader.value == function:
        match_label.append("#26")
    if ConstantPool.FileWriter.value == function:
        match_label.append("#27") 
    if ConstantPool.copy.value == function:
        match_label.append("CALL")
        match_label.append("#28") 
    if ConstantPool.InvocationTargetException.value == function:
        match_label.append("#29") 
    if ConstantPool.ManagedMemoryDataSource.value == function:
        match_label.append("#30") 
    if ConstantPool.DataHandler.value == function:
        match_label.append("CALL")
        match_label.append("#31")
    if ConstantPool.getInputStream.value == function:
        match_label.append("CALL")
        match_label.append("#32") 
    if ConstantPool.error.value == function:
        match_label.append("CALL")
        match_label.append("#33")  
    if ConstantPool.getMessage.value == function:
        match_label.append("CALL")
        match_label.append("#34")  
    if ConstantPool.RestartInputStream.value == function:
        match_label.append("#35")    
    if ConstantPool.identifyResource.value == function:
        match_label.append("CALL")
        match_label.append("#36")  
    if ConstantPool.restart.value == function:
        match_label.append("CALL")
        match_label.append("#37")  
    if ConstantPool.reportNotification.value == function:
        match_label.append("CALL")
        match_label.append("#38")  
    if ConstantPool.createLocalizedNotification.value == function:
        match_label.append("CALL")
        match_label.append("#39")  
    if ConstantPool.Object.value == function:
        match_label.append("#40")  
    if ConstantPool.getURI.value == function:
        match_label.append("CALL")
        match_label.append("#41")  
    if ConstantPool.getTypeName.value == function:
        match_label.append("CALL")
        match_label.append("#42")  
    if ConstantPool.getVersionName.value == function:
        match_label.append("CALL")
        match_label.append("#43")  
    if ConstantPool.processMigrationSteps.value == function:
        match_label.append("CALL")
        match_label.append("#44")  
    if ConstantPool.copyAndClose.value == function:
        match_label.append("CALL")
        match_label.append("#45")  
    if ConstantPool.createOutputStream.value == function:
        match_label.append("CALL")
        match_label.append("#46")  
    if ConstantPool.getProperty.value == function:
        match_label.append("CALL")
        match_label.append("#47")  
    if ConstantPool.println.value == function:
        match_label.append("CALL")
        match_label.append("#48")  
    if ConstantPool.GZIPInputStream.value == function:
        match_label.append("CALL")
        match_label.append("#48") 

    return match_label


def DsetGen(ADT):
    # this function is used to process Dset
    DsetGen = []
    ADT_leaves_list = FindLeaves(ADT)
    s_node = FindSnode(ADT)
    # find all path from start node to leaf node
    for leaf in ADT_leaves_list:
        for path in nx.all_simple_paths(ADT,source=s_node,target=leaf):
            label_path = []
            for node in path:
                for (nodename,label) in ADT.nodes(data=True):
                    if node == nodename:
                        label_path.append(label["label"])
            DsetGen.append(label_path)
    return DsetGen    

def FindSnode(Graph):
    # find start node in graph (in degree = 0)
    for node,in_degree in Graph.in_degree():
        if in_degree == 0:
            s_node = node
    return s_node 

def FindLeaves(ADT):
    # find leaf node in graph (out degree = 0)
    ADT_leaves_list = []
    for node,out_degree in ADT.out_degree():
        if out_degree ==0 :
            ADT_leaves_list.append(node)
    return(ADT_leaves_list)
    

def FingerPrintGen(Dset):
    # convert node from str to hash (MD5, 16 Byte)
    finger_print_set = []
    for path in Dset:
        finger_path = []
        for node in path:
            finger_path.append(NodeToHash(node))
        finger_print_set.append(finger_path)
    # print("Dset:",Dset)
    # print("fingerprint",finger_print_set)
    return finger_print_set


def NodeToHash(node):
    # MD5 , 16 Byte, and in the papar used 4 Byte hash for test 
    result = hashlib.md5(node.encode(encoding='utf-8'))
    return result.hexdigest()

def is_number(s):
    # this function is used to test whether str is float or not
    try:
        float(s)
        return True
    except ValueError:
        pass
 
    try:
        import unicodedata
        unicodedata.numeric(s)
        return True
    except (TypeError, ValueError):
        pass
 
    return False

def QuoteRemove(string):
    # this function is used to remove quote
    new_string = ""
    for s in string:
        if s != "\'" and s != " ":
         new_string = new_string +s
    return new_string

def Hamming(p,q):
    lengthp = len(p)
    lengthq = len(q)
    odd = 0
    min_ = min(lengthp,lengthq)  #find the longer one
    for index in range(min_):
        if p[index] != q[index]:
            p[index] = q[index] #find the difference and repalce
            odd += 1
    odd += max(lengthp,lengthq) - min_
    return odd

def Levenshtein(p,q):
    len_p = len(p) + 1
    len_q = len(q) + 1
    # 创建矩阵
    matrix = [0 for n in range(len_p * len_q)]
    #矩阵的第一行
    for i in range(len_p):
        matrix[i] = i
    # 矩阵的第一列
    for j in range(0, len(matrix), len_p):
        if j % len_p == 0:
            matrix[j] = j // len_p
    # 根据状态转移方程逐步得到编辑距离
    for i in range(1, len_p):
        for j in range(1, len_q):
            if p[i-1] == q[j-1]:
                cost = 0
            else:
                cost = 1
                matrix[j*len_p+i] = min(matrix[(j-1)*len_p+i]+1,
                    matrix[j*len_p+(i-1)]+1,
                    matrix[(j-1)*len_p+(i-1)] + cost)
    return matrix[-1]

def LCS(s1, s2):
    m = [[0 for i in range(len(s2) + 1)] for j in range(len(s1) + 1)]  # 生成0矩阵，为方便后续计算，比字符串长度多了一列
    mmax = 0  # 最长匹配的长度
                     # 最长匹配对应在s1中的最后一位
    for i in range(len(s1)):
        for j in range(len(s2)):
            if s1[i] == s2[j]: # 如果相等，则加入现有的公共子串
                m[i + 1][j + 1] = m[i][j] + 1
                if m[i + 1][j + 1] > mmax:
                    mmax = m[i + 1][j + 1]
    return  mmax  # 返回最长子串及其长度

# def LCS(p,q):
#     a = [x for x in p if x in q] 
#     # dis = max(len(p),len(q)) - len(a) 
#     return len(a)

def delt(simp,simq):  #simp与simq为二维list
    sim_total = 0
    len_p = len(simp)  # p 基数
    len_q = len(simq)  # q 基数
    if len_p > len_q:
        for index_q in range(len_q):  # 除以simQ
            max_sim = 0
            for index_p in range(len_p):
                tmp = LCS(simp[index_p],simq[index_q])  #find the min delt(p,q),可以更改为其他的函数
                tmp_sim = tmp/min(len(simp[index_p]),len(simq[index_q]))
                if tmp_sim > max_sim:
                    max_sim = tmp_sim
            sim_total += max_sim
        sim_total /=  len_q
        return sim_total
    else :
        for index_p in range(len_p):  #除以simP
            max_sim = 0
            for index_q in range(len_q):
                tmp = LCS(simp[index_p],simq[index_q])  #find the min delt(p,q)，可以更改为其他函数
                # print('tmp:',tmp)
                # print(min(len(simp[index_p]),len(simq[index_q])))
                tmp_sim = tmp/min(len(simp[index_p]),len(simq[index_q]))
                if tmp_sim > max_sim:
                    max_sim = tmp_sim
                    # print(max_sim)
            sim_total += max_sim
        # print(sim_total)
        sim_total /=  len_p
    return sim_total

def listdir(path, list_name):  # 传入存储的list
    for file in os.listdir(path):
        file_path = os.path.join(path, file)
        if os.path.isdir(file_path):
            listdir(file_path, list_name)
        else:
            list_name.append(file)


def StoneDetector(f1, f2, tau):
    file1name = f1.split('/')[-1]
    file2name = f2.split('/')[-1]

    cfg1name = file1name.split('.')[0] + '.dot'
    cfg2name = file2name.split('.')[0] + '.dot'

    dotdict = os.path.abspath(".") + '/cfg-dot/'
    if os.path.exists(dotdict):
        pass
    else:
        os.mkdir(dotdict)
    existdot = []
    listdir(dotdict, existdot)

    # if cfg1name not in existdot or cfg2name not in existdot:
    #     if cfg1name not in existdot:
    #         cfg1path = cfg_generation(f1, dotdict)

    #     if cfg2name not in existdot:
    #         cfg2path = cfg_generation(f2, dotdict)


    # get file 2 cfg .dot file path and java source code path
    path1 = dotdict + cfg1name
    javasourcepath1 = f1

    CFG1 = ReadDotFile(path1)
    filename1 = (javasourcepath1.split("/")[-1]).split(".")[0]
    DT1 = DominatorTreeGen(CFG1, filename1)
    typedict1 = JavaSrc2tokens(javasourcepath1)
    ADT1 = ADTGen(DT1, typedict1, filename1)
    # print(ADT1)
    Dset1 = DsetGen(ADT1)
    # print(Dset1)
    Fingerprint1 = FingerPrintGen(Dset1)

    # get file 2 cfg .dot file path and java source code path
    path2 = dotdict + cfg2name
    javasourcepath2 = f2

    filename2 = (javasourcepath2.split("/")[-1]).split(".")[0]
    CFG2 = ReadDotFile(path2)
    DT2 = DominatorTreeGen(CFG2, filename2)
    typedict2 = JavaSrc2tokens(javasourcepath2)
    ADT2 = ADTGen(DT2, typedict2, filename2)
    print(ADT1)
    print(ADT2)
    
    Dset2 = DsetGen(ADT2)
    # print(Dset2)

    Fingerprint2 = FingerPrintGen(Dset2)
    # file = open('size/c1.txt','ab')
    # pickle.dump((list(Fingerprint1),list(Fingerprint2)),file)
    # file.close()
    # size_a = sys.getsizeof(Fingerprint1)
    # size_b = sys.getsizeof(Fingerprint2)
    # size = size_a + size_b
    delta_dis = delt(Fingerprint1, Fingerprint2)
    # print(delta_dis)
    # print("Δ = {} and τ = {}".format(delta_dis, tau))

    # if delta_dis < tau:
    #     print("the two code fragments {}.java and {}.java form a code clone".format(f1, f2))
    # else:
    #     print("the two code fragments {}.java and {}.java don't form a code clone".format(f1, f2))
    # print(delta_dis)
    return delta_dis#,size


if __name__=="__main__":

    """
    path = './cfg/20924120/0-cfg.dot'
    javasourcepath = './pairs/20924120.java'
    # path = './cfg-java-test.dot'
    # javasourcepath = './test.java'
    CFG = ReadDotFile(path)
    filename = (javasourcepath.split("/")[-1]).split(".")[0]
    DT = DominatorTreeGen(CFG,filename)
    typedict = JavaSrc2tokens(javasourcepath)
    print(typedict)
    ADT = ADTGen(DT,typedict,filename)
    Dset = DsetGen(ADT)
    Fingerprint = FingerPrintGen(Dset)
    """

    if len(sys.argv) < 4:
        sys.exit("Usage: python StoneDetector [file1] [file2] [τ]") 
    else:
        file1 = sys.argv[1]
        file2 = sys.argv[2]
        tau = float(sys.argv[3])
        
        # get file1 cfg .dot file path and java source code path
        path1 = './CFG/ICSME2021/cfg/' + file1 + '/0-cfg.dot' 
        javasourcepath1 = './pairs/' + file1 + '.java'

        CFG1 = ReadDotFile(path1)
        filename1 = (javasourcepath1.split("/")[-1]).split(".")[0]
        DT1 = DominatorTreeGen(CFG1,filename1)
        typedict1 = JavaSrc2tokens(javasourcepath1)
        ADT1 = ADTGen(DT1,typedict1,filename1)
        Dset1 = DsetGen(ADT1)
        Fingerprint1 = FingerPrintGen(Dset1)

        # get file 2 cfg .dot file path and java source code path
        path2 = './CFG/ICSME2021/cfg/' + file2 + '/0-cfg.dot' 
        javasourcepath2 = './pairs/' + file2 + '.java'

        filename2 = (javasourcepath2.split("/")[-1]).split(".")[0]
        CFG2 = ReadDotFile(path2)
        DT2 = DominatorTreeGen(CFG2,filename2)
        typedict2 = JavaSrc2tokens(javasourcepath2)
        ADT2 = ADTGen(DT2,typedict2,filename2)
        
        Dset2 = DsetGen(ADT2)
        Fingerprint2 = FingerPrintGen(Dset2)

        delta_dis = delt(Fingerprint1,Fingerprint2)
        print("Δ = {} and τ = {}".format(delta_dis,tau))
        
        if delta_dis < tau:
            print("the two code fragments {}.java and {}.java form a code clone".format(file1,file2))
        else:
            print("the two code fragments {}.java and {}.java don't form a code clone".format(file1,file2))
