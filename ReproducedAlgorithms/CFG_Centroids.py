# -*- coding: utf-8 -*- #
 
# ------------------------------------------------------------------
# File Name:        codeSimilarV1.py
# Author:           km
# Version:          ver1.0
# Created:          2022/05/11
# Description:      SSEPRW17 code similarity compare
# Input：           由joern生成java源码的CFG(dot文件)，
# Output：          1. 该CFG对应的特征向量/质心CenList 2.Cenlist1和CenList2之间的相似度，以及最终判断结果codeSimilar(True/False)
# ------------------------------------------------------------------
import networkx as nx
import numpy as np
import os
#import matplotlib.pyplot as plt
#tarjan for DIG Loop Compute
#import tarjan
from tarjan import tarjan
from CFG_generation import cfg_generation
import sys
import pickle
######choose CFG compute path###### and compare CFG value#####
#dotFileName = "./pairs/pair11/0-cfg.dot"
#CenList1 = [14.216666666666667, 1.0833333333333333, 1.0, 60] #pair11 
#CenList2 = [16.52857142857143, 1.1, 1.0, 70]  #pair12
# 下面是其他几个测试例子
#CenList1 =  [14.216666666666667, 1.0833333333333333, 1.0, 60] #pair11
#CenList2 = [16.52857142857143, 1.1, 1.0, 70]  #pair12
#[13.017857142857142, 1.125, 1.5714285714285714, 56] #pair21
#[13.75, 1.0178571428571428, 1.0, 56] #pair61

# 相似性阈值按SSEPRW17 论文设置为0.4

def cfgReadDotFile (dotFile):
    CFG1 = nx.drawing.nx_pydot.read_dot(dotFile)
    CFG2 = nx.DiGraph(CFG1)
    if '\\n' in CFG2.nodes():
        CFG2.remove_node('\\n')
    #print("nodes:\n",CFG2.nodes())
    #print(CFG2.nodes(data = True)) # set true to show label
    #print("edges:\n",CFG2.edges())
    entry_node = []
    for node,indegree in CFG2.in_degree():
        if indegree == 0:
            entry_node.append(node)
    if len(entry_node) > 1:
        CFG2.add_node('0')
        CFG2.nodes['0']['label'] = '"-1"'
        for node in entry_node:
            CFG2.add_edge('0',node)   
    nodes = CFG2.nodes()
    edges = CFG2.edges()
    return CFG2,nodes,edges

def extractNodes(CFG):
    nodes = CFG.nodes()
    return nodes

def extractEdges(CFG):
    edges = CFG.edges()
    return edges

def nodeOutDegreeDictGen(nodes,edges):
    nodeOutDict = {} # record outgoing edges degree
    nodeOutDegree = []
    for node in nodes:
        outDegree = 0
        for edge in edges:
            if (node == edge[0]):
                outDegree += 1
        nodeOutDict[node] = outDegree
        nodeOutDegree.append(outDegree)
    return nodeOutDict,nodeOutDegree


# 每个节点对应的出边 （其实不如直接用邻接矩阵算）
def nodeOutEdgesGen(nodes,edges):
    nodeList = list(nodes)
    
    nodeEdge = []  # 如果是[[]]，程序会认为第一个子Arr是空的
    nodeEdgeArr = []
    nodeOutIndexList = []
    nodeOutIndexTemp = []
    
    for node in nodes:
        for edge in edges:
            if (node == edge[0]):
                nodeEdgeArr.append(edge[1])
                nodeOutIndexTemp.append(nodeList.index(edge[1]))
                
        nodeEdge.append(nodeEdgeArr)
        nodeOutIndexList.append(nodeOutIndexTemp)
        # reset to zero
        nodeEdgeArr = []
        nodeOutIndexTemp = []
    # print(nodeOutIndexList)
    return nodeEdge,nodeOutIndexList

# 邻接矩阵生成
def adjMatrixGen(nodes,edges):
    adjMatrix = np.zeros([len(nodes),len(nodes)] , dtype=int)
    i, j = 0, 0
    for node1 in nodes:
        for node2 in nodes:
            for edge in edges:
                if (node1 == edge[0] and node2 == edge[1]):
                    adjMatrix[i,j] = 1           
            j += 1       
        i += 1
        j = 0
        
    return adjMatrix

# dfs to find adjMatrix digraph
def dfsGraph (cfg):
    pass

# node Sort
# def nodeSort(index, adjMatrix):
#     lenNodes = len(adjMatrix)
#     # outNode = np.nonzero(adjMatrix)
#     outNodeList = []
      
#     for j in range(0, lenNodes):    
#         if (adjMatrix[index, j] == 1):
#             outNodeList.append(j)
#     for j in range(0, lenNodes):
        
#     outNode = 0
#     return outNode


#cdd Dfs Traversal
def cddDfs(CandiStack, nodeVisit, nodeTraversalList, nodeOutIndexList, nodeOutDegree):
 
    if ( len(CandiStack) == 0 ): #stack empty , dfs over
        return None
    
    else:
        #print("dfs-loop")
        next = CandiStack.pop()  #nextNode
        #print(next)
        
        if (nodeVisit[next] == 1):
            # return None #has been visited
            cddDfs(CandiStack, nodeVisit, nodeTraversalList, nodeOutIndexList, nodeOutDegree)
        else:
            nodeVisit[next] = 1
            # print(next)
            nodeTraversalList.append(next)  #add node 
            
            # push child nodes to Stack
            if ( len(nodeOutIndexList[next]) == 0):
                cddDfs(CandiStack, nodeVisit, nodeTraversalList, nodeOutIndexList, nodeOutDegree) #还要继续dfs递归
                #return None  #reach leaf node 
            else:
                childNode = nodeOutIndexList[nodeTraversalList[-1]]
                childNodeDegree = []
                # [x for _,x in sorted(zip(Y,X))] 按照Y的顺序给X排序
                for node in childNode:
                    childNodeDegree.append(nodeOutDegree[node])
                
                childNodeSort = [x for _, x in sorted(zip(childNodeDegree, childNode))] #升序排列，分支最多的在最后先出栈
                #print(childNodeSort)
                CandiStack.extend(childNodeSort)  #push to stack
                #print(CandiStack)

            cddDfs(CandiStack, nodeVisit, nodeTraversalList, nodeOutIndexList, nodeOutDegree)
    return


# cdd遍历初始化
def dfsInit (adjMatrix, CandiStack):
    # print(adjMatrix)
    lenNodes = len(adjMatrix)
    # indgree = []
    sum = 0
    entry = -1
    # find entry node
    for j in range(0, lenNodes):    
        for i in range(0, lenNodes):
            sum += adjMatrix[i,j]
        if (sum == 0):
            entry = j
            # indgree.append(j)
            break
        sum = 0
    CandiStack.append(entry)
    # print(CandiStack)
    #nodeVisit[entry] = 1 #entry have been visisted
    return 

# Count Centroid of a Cfg
def CentroidGen(nodes, adjMatrix, axisW, axisX, axisY, axisZ):
    sumX,sumY,sumZ,sumW = 0,0,0,0
    n = len(nodes)
    for i in range(0,n):
        for j in range(0,n):
            if (adjMatrix[i,j] == 1):
                sumW += ( axisW[i] + axisW[j] )
                sumX += ( (axisX[i] * axisW[i]) + (axisX[j] * axisW[j]) )
                sumY += ( (axisY[i] * axisW[i]) + (axisY[j] * axisW[j]) )
                sumZ += ( (axisZ[i] * axisW[i]) + (axisZ[j] * axisW[j]) )
    CenX = (sumX / sumW)
    CenY = (sumY / sumW)
    CenZ = (sumZ / sumW)
    CenW = sumW
    
    CenList = []
    CenList.extend((CenX, CenY, CenZ, CenW))
    return CenList
    #return CenX, CenY, CenZ, CenW


# compare similarity between two cfg
def CompareSimilar(Cen1, Cen2, cThreshold):
    similar = 0.0
    similarList = []
    for i in range(0,4):
        if (Cen1[i] + Cen2[i]) == 0: #avoid divide by zero
            temp = 0.0
        else:
            temp = ( abs(Cen1[i] - Cen2[i]) / (Cen1[i] + Cen2[i]) )
        similarList.append(temp)
    
    similar = max(similarList)
    similarFlag = (similar < cThreshold)
    
    return similarList, similar, similarFlag    


#Gen tarjan Dict ,and compute loop depth
def tarjanGen(nodes, adjMatrix):
    # example {1:[2],2:[1,5],3:[4],4:[3,5],5:[6],6:[7],7:[8],8:[6,9],9:[]}
    tarjanDict = {}
    n = len(nodes)
    nodeZ = [0]*len(nodes)
    
    for i in range(n):
        listTemp = []
        
        for j in range(n):
            if ( adjMatrix[i,j] == 1 ):
                listTemp.append(j)  
        tarjanDict[i] = listTemp          
    
    connectGraList = tarjan(tarjanDict)   #compute connected Graph of CFG
    
    #######计算loop depth#####
    for conG in connectGraList:
        if ( len(conG) > 1 ):
            for index in conG:
                nodeZ[index] += 1
                
    # 嵌套循环 depth值还需要增加
    for conG1 in connectGraList:
        for conG2 in connectGraList:
            # 若conG2为conG1的子集，depath继续增加
            if ( set(conG2).issubset(set(conG1)) ):
                for index in conG2:
                    nodeZ[index] += 1
                              
    return tarjanDict, connectGraList, nodeZ 

def listdir(path, list_name):  # 传入存储的list
    for file in os.listdir(path):
        file_path = os.path.join(path, file)
        if os.path.isdir(file_path):
            listdir(file_path, list_name)
        else:
            list_name.append(file)

def Cosine_sim(vec1,vec2):
    dot = sum(a*b for a,b in zip(vec1,vec2))
    norm1 = sum(a*a for a in vec1) ** 0.5
    norm2 = sum(b*b for b in vec2) ** 0.5
    cos_sim = dot/(norm1 * norm2)
    return cos_sim

def Centroids(f1, f2):
    #print(f1)
    file1name = f1.split('/')[-1]
    file2name = f2.split('/')[-1]
    #print(file1name)
    cfg1name = file1name.split('.')[0] + '.dot'
    cfg2name = file2name.split('.')[0] + '.dot'
    #print(cfg1name)
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


    dotFileName1 = dotdict + cfg1name
    cfg1, nodes1, edges1 = cfgReadDotFile(dotFileName1)

    ##############################
    ###遍历和计算相似性数据
    nodeTraversalList1 = []
    CandiStack1 = []  # 候选节点
    nodeVisit1 = [0] * len(nodes1)
    # dfsOff = 0  #dfs FLag , set 1 to stop

    ############################
    nodeOutDict1, nodeOutDegree1 = nodeOutDegreeDictGen(nodes1, edges1)


    nodeOutEdgesList1, nodeOutIndexList1 = nodeOutEdgesGen(nodes1, edges1)


    adjMatrix1 = adjMatrixGen(nodes1, edges1)
    # print(edges1)

    ####test value
    # nodeOutDegree = [1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 6, 5, 2, 1, 1, 1, 0]

    ############
    dfsInit(adjMatrix1, CandiStack1)
    # print(CandiStack1)
    # print(nodeVisit1)
    # print(nodeTraversalList1)
    # print(nodeOutIndexList1)
    # print(nodeOutDegree1)
    cddDfs(CandiStack1, nodeVisit1, nodeTraversalList1, nodeOutIndexList1, nodeOutDegree1)

    nodeNum1 = []  # 编号 即X坐标
    # print(nodeTraversalList1)
    for i in range(0, len(nodes1)):
        # print(nodeTraversalList1.index(i))
        nodeNum1.append(nodeTraversalList1.index(i))

    #####计算loop-depth 即Z坐标

    tarjanDict1, connectGraList1, nodeZ1 = tarjanGen(nodes1, adjMatrix1)

    #####计算质心
    axisW1 = [1] * len(nodes1)

    axisX1 = nodeNum1
    axisY1 = nodeOutDegree1
    axisZ1 = nodeZ1

    CenList1 = CentroidGen(nodes1, adjMatrix1, axisW1, axisX1, axisY1, axisZ1)
    #print("CenList:\n", CenList1)

    dotFileName2 = dotdict + cfg2name
    cfg2, nodes2, edges2 = cfgReadDotFile(dotFileName2)

    ##############################
    ###遍历和计算相似性数据
    nodeTraversalList2 = []
    CandiStack2 = []  # 候选节点
    nodeVisit2 = [0] * len(nodes2)
    # dfsOff = 0  #dfs FLag , set 1 to stop
    ############################
    nodeOutDict2, nodeOutDegree2 = nodeOutDegreeDictGen(nodes2, edges2)

    nodeOutEdgesList2, nodeOutIndexList2 = nodeOutEdgesGen(nodes2, edges2)

    adjMatrix2 = adjMatrixGen(nodes2, edges2)

    ############
    dfsInit(adjMatrix2, CandiStack2)
    cddDfs(CandiStack2, nodeVisit2, nodeTraversalList2, nodeOutIndexList2, nodeOutDegree2)

    nodeNum2 = []  # 编号 即X坐标
    for i in range(0, len(nodes2)):
        nodeNum2.append(nodeTraversalList2.index(i))

    #####计算loop-depth 即Z坐标

    tarjanDict2, connectGraList2, nodeZ2 = tarjanGen(nodes2, adjMatrix2)

    #####计算质心
    axisW2 = [1] * len(nodes2)

    axisX2 = nodeNum2
    axisY2 = nodeOutDegree2
    axisZ2 = nodeZ2

    CenList2 = CentroidGen(nodes2, adjMatrix2, axisW2, axisX2, axisY2, axisZ2)
    # file = open('size/c2.txt','ab')
    # pickle.dump((CenList1,CenList2),file)
    # file.close()
    # size_a = sys.getsizeof(CenList1)
    # size_b = sys.getsizeof(CenList2)
    # size = size_a + size_b
    #print("CenList:\n", CenList2)
    # similar = Cosine_sim(CenList1,CenList2)
    cThreshold = 0.4  # cThreshold按SSEPRW17论文 设置为0.4 ，即similar低于0.4才能看作相似
    # print(CenList1,CenList2)
    similarList, similar, similarFlag = CompareSimilar(CenList1, CenList2, cThreshold)

    print(1-similar)
    # SSRPRW17 需要计算的三个指标
    #CDD = similarFlag
    # 下面两个指标需要定义安卓系统中的Risk API 才能计算，现在预定为True，只用CDD一个指标判断
    # BVE = True
    #VDD = True

    #codeSimilar = CDD and BVE and VDD
    # print("Similar:\n", 1-similar)
    return (1 - similar)#,size

if __name__ == "__main__":

    #print("*************************extract centroid of cfg**********************************")
    dotFileName1 = "./pairs/pair11/0-cfg.dot"
   #  cfg1,nodes1,edges1 = cfgReadDotFile(dotFileName1)
   #  #print("nodes:\n",nodes)
   # # print("edges:\n",edges)
   #
   #  ##############################
   #  ###遍历和计算相似性数据
   #  nodeTraversalList1 = []
   #  CandiStack1 = []  #候选节点
   #  nodeVisit1 = [0] * len(nodes1)
   #  #dfsOff = 0  #dfs FLag , set 1 to stop
   #  cThreshold = 0.4   #cThreshold按SSEPRW17论文 设置为0.4 ，即similar低于0.4才能看作相似
   #  ############################
   #  nodeOutDict1, nodeOutDegree1 = nodeOutDegreeDictGen(nodes1,edges1)
   #  #print("nodeOutDict:\n",nodeOutDict)
   #  #print("nodeOutDegree:\n",nodeOutDegree) #Y坐标值
   #
   #  nodeOutEdgesList1, nodeOutIndexList1= nodeOutEdgesGen(nodes1,edges1)
   # # print("nodeOutEdgesList:\n",nodeOutEdgesList)
   #  #print("nodeOutIndexList:\n",nodeOutIndexList)
   #
   #  adjMatrix1 = adjMatrixGen(nodes1,edges1)
   # # print("nodes adjMatrix:\n",adjMatrix)
   #
   #  ####test value
   #  #nodeOutDegree = [1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 6, 5, 2, 1, 1, 1, 0]
   #
   #  ############
   #  dfsInit(adjMatrix1, CandiStack1)
   #  cddDfs(CandiStack1, nodeVisit1, nodeTraversalList1, nodeOutIndexList1, nodeOutDegree1)
   #  #print("nodeTraversalList:\n",nodeTraversalList)
   # # print("nodeVisit:\n",nodeVisit)
   #  #print("nodeTraversalList:\n",nodeTraversalList)
   #
   #  nodeNum1 = []  #编号 即X坐标
   #  for i in range(0, len(nodes1)):
   #      nodeNum1.append(nodeTraversalList1.index(i))
   #  #print("nodeNum:\n",nodeNum)
   #
   #  #####计算loop-depth 即Z坐标
   #  #test1 = tarjan({1:[2],2:[1,5],3:[4],4:[3,5],5:[6],6:[7],7:[8],8:[6,9],9:[]}) #tarjan example
   #  #print(test1)
   #  tarjanDict1, connectGraList1, nodeZ1 = tarjanGen(nodes1, adjMatrix1)
   #  #print("tarjanDict:\n", tarjanDict)
   #  #print("connectGraList:\n", connectGraList)
   #  #print("nodeZ:\n", nodeZ)
   #
   #  #####计算质心
   #  axisW1 = [1]*len(nodes1)
   #
   #  axisX1 = nodeNum1
   #  axisY1 = nodeOutDegree1
   #  axisZ1 = nodeZ1
   #
   #  # CenX, CenY, CenZ ,CenW = CentroidGen()
   #  # print(CenX, CenY, CenZ, CenW)
   #
   #  CenList1 = CentroidGen(nodes1, adjMatrix1, axisW1, axisX1, axisY1, axisZ1)
   #  print("CenList:\n", CenList1)
   #
   #  #############Cen compare test
   #  CenList1 = [8.473684210526315, 1.1578947368421053, 0.0, 38]
   #  CenList2 = [10, 1.1578947368421053, 0.0, 38]
   #
   #  # similar代表相似性，0.0-1.0 float 越小越相似
   #  # cThreshold按SSEPRW17论文 设置为0.4 ，即similar低于0.4才能看作相似
   #  print("*************************compare similarity between two cfg************************************")
   #  print("CenList1 and CenList2:\n", CenList1,CenList2)
   #  similarList, similar, similarFlag  = CompareSimilar(CenList1, CenList2, cThreshold)
   #  print("similarList:\n", similarList)
   #  print("similar:\n", similar)
   #  print("similarFlag:\n", similarFlag)
   #
   #  # SSRPRW17 需要计算的三个指标
   #  CDD = similarFlag
   #  #下面两个指标需要定义安卓系统中的Risk API 才能计算，现在预定为True，只用CDD一个指标判断
   #  BVE = True
   #  VDD = True
   #
   #  codeSimilar = CDD and BVE and VDD
   #  print("codeSimilar:\n", codeSimilar)
   #
