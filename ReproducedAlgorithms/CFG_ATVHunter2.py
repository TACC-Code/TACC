'''
把函数转化成一条超长序列
直接调用getSimilarity函数
input: dot_file_path1, dot_file_path2, bool(是否进行归一化)
output: similarity, 介于0-1之间
'''

from xmlrpc.client import boolean
import networkx as nx
import os
import ssdeep
import javalang
import xlwt
import sys
import pickle
def getGraph(dot_path):
    return nx.DiGraph(nx.nx_pydot.read_dot(dot_path))

def normalizingLine(s): #形如 "30:  Attribute createAttribute(KeyValuePair kvp)"
    try:
        tokens = list(javalang.tokenizer.tokenize(s[1:-1]))
        temp_line = []   
        for i in range(len(tokens)):
            token = tokens[i]
            # if (type(token) == javalang.tokenizer.Identifier):
            #     temp_line += 'id'
            # else:
            #     temp_line += token.value
            temp_line.append(ssdeep.hash(str(type(token)).split('.')[-1][:-2])) #-2是为了去除末尾的['>]
        return temp_line
    except:
        print(s[1:-1], "can't parse!!!")
        return [ssdeep.hash(s[1:-1])]


def commonLine(s): #不进行归一化
    try:
        tokens = list(javalang.tokenizer.tokenize(s[1:-1])) #[1:-1]是为了去除前后双引号
        temp_line = []   
        for i in range(len(tokens)):
            token = tokens[i]
            # if (type(token) == javalang.tokenizer.Identifier):
            #     temp_line += 'id'
            # else:
            #     temp_line += token.value
            temp_line.append(ssdeep.hash(token.value)) 
        return temp_line
    except:
        print(s[1:-1], "can't parse!!!")
        return [ssdeep.hash(s[1:-1])]

def get_root_node(G):
    node = None
    for n in G.nodes(data=True):
        predecessors = G.predecessors(n[0])
        if len(list(predecessors)) == 0:
            node = n
            break
    return node[0]


#  用digraph作为输入
def getPreOrder(dg, isNorm : bool):
    stack = []
    result = []
    vis = [False for _ in range(10000)] #avoid no-end loop, 假设最大节点编号不超过10000
    root = get_root_node(dg)
    stack.append(root)
    while stack:
        curr = stack.pop()
        vis[int(curr)] = True
        # print(dg.nodes[curr]['label'])
        if (isNorm):
            line = normalizingLine(dg.nodes[curr]['label'])
        else:
            line = commonLine(dg.nodes[curr]['label'])
        result.extend(line)
        for nbr in list(dg[curr])[::-1]:
            if(not vis[int(nbr)]):
                stack.append(nbr)
    return result

# 莱温斯坦编辑距离
def editDistance(word1, word2):
    n = len(word1)
    m = len(word2)
    
    # 有一个字符串为空串
    if n * m == 0:
        return n + m
    
    # DP 数组
    D = [ [0] * (m + 1) for _ in range(n + 1)]
    
    # 边界状态初始化
    for i in range(n + 1):
        D[i][0] = i
    for j in range(m + 1):
        D[0][j] = j
    
    # 计算所有 DP 值
    for i in range(1, n + 1):
        for j in range(1, m + 1):
            left = D[i - 1][j] + 1
            down = D[i][j - 1] + 1
            left_down = D[i - 1][j - 1] 
            if word1[i - 1] != word2[j - 1]:
                left_down += 1-ssdeep.compare(word1[i-1], word2[j-1])/100
            D[i][j] = min(left, down, left_down)
    
    return D[n][m]

 
def getSimilarity(dot_path1, dot_path2, isNorm: bool):
    dg1 = getGraph(dot_path1)
    dg2 = getGraph(dot_path2)
    preOrder1 = getPreOrder(dg1, isNorm)
    preOrder2 = getPreOrder(dg2, isNorm)
    # file = open('size/c3.txt','ab')
    # pickle.dump((preOrder1,preOrder2),file)
    # file.close()
    # size_a = sys.getsizeof(preOrder1)
    # size_b = sys.getsizeof(preOrder2)
    # size = size_a + size_b
    edit_dis = editDistance(preOrder1, preOrder2)
    return (1 - edit_dis/max(len(preOrder1), len(preOrder2)))#,size

def ATVHunter2(sourcefile1,sourcefile2):
    dotfile1 = './cfg-dot/'+ sourcefile1.split('/')[-1].split('.')[0] + '.dot'
    dotfile2 = './cfg-dot/'+ sourcefile2.split('/')[-1].split('.')[0] + '.dot'
    return getSimilarity(dotfile1,dotfile2,True)

if __name__ == '__main':
    root_path = 'progex/dot/'
    dot_files = os.listdir('progex/dot/')
    num_of_file = len(dot_files)
    s = [[0 for _ in range(num_of_file)] for _ in range(num_of_file)]
    for i in range(num_of_file):
        for j in range(i+1, num_of_file):
            sim = getSimilarity(os.path.join(root_path ,dot_files[i]), os.path.join(root_path ,dot_files[j]), False)
            s[i][j] = sim
            # print(sim)

    workbook = xlwt.Workbook(encoding = 'utf-8')        #设置一个workbook，其编码是utf-8
    worksheet1 = workbook.add_sheet("filter_sheet")        #新增一个sheet
    worksheet2 = workbook.add_sheet("verify_sheet")

    for i in range(num_of_file):
        worksheet1.write(0,i,dot_files[i])
        worksheet2.write(0,i,dot_files[i])

    for i in range(len(s)):
        for j in range(len(s[i])) :                            
            worksheet1.write(i+1,j,s[i][j])
            worksheet2.write(i+1,j,s[i][j])
    workbook.save('ATVHunter_not_norm_inline.xls') #这里save需要特别注意，文件格式只能是xls，不能是xlsx，不然会报错


