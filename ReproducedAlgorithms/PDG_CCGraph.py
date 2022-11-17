# -*- coding: utf-8 -*-
import os
import sys
import pydot
from igraph import Graph
import graphkernels.kernels as gk
from PDG_modify import modifypdg
import pickle


# def dotchange(fn):
#     graph = nx.drawing.nx_pydot.read_dot(fn)
#     graph = nx.DiGraph(graph)
#     nodelist = graph.nodes()
#     edgelist = graph.edges()
#     print(nodelist)
#     print(edgelist)


def dotchange(fn):
    tmp1=fn
    print(fn.split('/')[-1].split('.')[0])
    graph = pydot.graph_from_dot_file(tmp1)# return a list of graph object
    # print(fn)
    nodeList = graph[0].get_node_list()
    del nodeList[-1]
    edgeList = graph[0].get_edge_list()
    
    # print(nodeList)
    # print(edgeList)
    node_list = []
		
	
    edge_list = []
    for e in edgeList:
        tem_s = e.get_source()
        tem_d = e.get_destination()
        tem2={}
        tem2["sour"] = tem_s
        tem2["dest"] =  tem_d
        if e.get("label")=="\"CDG\"":
            tem2['e_label'] = 1#1 for Ctrl				
        elif e.get("label")=="\"DDG\"":
            tem2['e_label'] = 2#2 for Data
        else:
            tem2['e_label'] = 3#3 for Execution
        #tem2.pop('color')
        edge_list.append(tem2)
    for n in nodeList:		
        tem2 = {}# tem2 is a dict
        tem2['nodenum'] =n.get_name()
        count = 0
        for edge in edge_list:
            if edge['sour'] == tem2['nodenum']:
                count += 1
        if(count >= 2):
            tem2['v_label'] = 1#1 for CtrlNode
        # elif(n.get("shape") =="box"):
        #     tem2['v_label']  = 2#2 for ParaNode
        # elif(n.get("shape") =="ellipse" and n.get("fillcolor") =="white"):
        #     tem2['v_label']  = 3#3 for StateNode
        else:
            tem2['v_label'] = 2#4 for Other
                #tem2.pop('shape')
                #tem2.pop('fillcolor')
                #tem2.pop('label')
        node_list.append(tem2)
        # print(enumerate(node_list))	
    return trans2igraph(node_list,edge_list,fn)
# dotchange('./lastdot/addPages.dot')


def trans2igraph(node_list,edge_list,fn):
    v_num = len(node_list)
    v_label_list = [node.get('v_label') for node in node_list]

    node_dct = {}
    for i,node in enumerate(node_list):#这里把nodenum 和 实际图中所使用的number对应上来
        #print(node['nodenum'],i)
        node_dct[node['nodenum']] = i

	#print(node_dct)
    e_label_list = [edge.get('e_label') for edge in edge_list]
    #print(e_label_list)
    edge_L=[]
    for edge in edge_list:
        #print(node_dct.get(edge['sour']))
        #print(node_dct.get(edge['dest']))
        edge_L.append((node_dct.get(edge['sour']),node_dct.get(edge['dest'])))
    #edge_L = [ ( node_dct.get(edge.get_source()),node_dct.get(edge.get_destination()) )  for edge in edge_list ]
    #print(edge_L)
    del node_list
    del edge_list
    g = Graph(directed = True)
    g.add_vertices(v_num)
    g.vs['id']=v_label_list
    g.vs['label'] = v_label_list
    g.add_edges(edge_L)
    g.es['label'] = e_label_list
    #print(g)
    #print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
    #g.write_graphml('./graph_data/'+fn+'.graphml')
    return g


def CCGraph(sourcefile1,sourcefile2):
    rawdot1 = './pdg-dot/' + sourcefile1.split('/')[-1].split('.')[0] + '.dot'
    rawdot2 = './pdg-dot/' + sourcefile2.split('/')[-1].split('.')[0] + '.dot'
    dotfile1 = './newpdg/' + sourcefile1.split('/')[-1].split('.')[0] + '.dot'
    dotfile2 = './newpdg/' + sourcefile2.split('/')[-1].split('.')[0] + '.dot'
    if not os.path.exists(dotfile1):
        dotfile1 = modifypdg(rawdot1)
    if not os.path.exists(dotfile2):
        dotfile2 = modifypdg(rawdot2)
    test = []
    test.append(dotchange(dotfile1))
    test.append(dotchange(dotfile2))
    # file = open('size/p1.txt','ab')
    # pickle.dump((test[0],test[1]),file)
    # file.close()
    # size = sys.getsizeof(test[0]) + sys.getsizeof(test[1])
    k=gk.CalculateWLKernel(test,par=5)
    if k[0][0] >= k[1][1]:
        sim = float(k[1][1])/float(k[0][0])
        return round(sim,4)#,size
    else:
        sim = float(k[0][0])/float(k[1][1])
        return round(sim,4)#,size
# if __name__== '__main__':
#     sim = CCgraph(1,2)
#     print(sim)
# import time
# time1 = time.time()
# from collections import OrderedDict
# import numpy as np
# import os


# name_list = list()
# filepath='./testfile/'
# count=0

# K0=[]
# data_list=[]
# for filename in os.listdir(filepath):
#         #print(filename)
#         data_list.append(dotchange(filepath+filename))
#         name_list.append(os.path.splitext(filename)[0])

# # print(data_list)
# # print('***')
# # print(name_list)

# data_list_new=list_of_groups(data_list,1000)
# for data in data_list_new:
#     print(data)
#     k=gk.CalculateWLKernel(data,par=5)
#     print(k)
#     for i in range(len(k)):
#         K0.append(k[i][i])