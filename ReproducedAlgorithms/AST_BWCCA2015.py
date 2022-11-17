from lib2to3.pgen2.literals import simple_escapes
import os
import javalang
from anytree import AnyNode
from javalang.ast import Node
import pickle
import itertools
import json
import csv
import sys
import time

nodetypedict = {
    'MethodDeclaration': 0,
    'Modifier': 1,
    'FormalParameter': 2,
    'ReferenceType': 3,
    'BasicType': 4,
    'LocalVariableDeclaration': 5,
    'VariableDeclarator': 6,
    'MemberReference': 7,
    'ArraySelector': 8,
    'Literal': 9,
    'BinaryOperation': 10,
    'TernaryExpression': 11,
    'IfStatement': 12,
    'BlockStatement': 13,
    'StatementExpression': 14,
    'Assignment': 15,
    'MethodInvocation': 16,
    'Cast': 17,
    'ForStatement': 18,
    'ForControl': 19,
    'VariableDeclaration': 20,
    'TryStatement': 21,
    'ClassCreator': 22,
    'CatchClause': 23,
    'CatchClauseParameter': 24,
    'ThrowStatement': 25,
    'WhileStatement': 26,
    'ArrayInitializer': 27,
    'ReturnStatement': 28,
    'Annotation': 29,
    'SwitchStatement': 30,
    'SwitchStatementCase': 31,
    'ArrayCreator': 32,
    'This': 33,
    'ConstructorDeclaration': 34,
    'TypeArgument': 35,
    'EnhancedForControl': 36,
    'SuperMethodInvocation': 37,
    'SynchronizedStatement': 38,
    'DoStatement': 39,
    'InnerClassCreator': 40,
    'ExplicitConstructorInvocation': 41,
    'BreakStatement': 42,
    'ClassReference': 43,
    'SuperConstructorInvocation': 44,
    'ElementValuePair': 45,
    'AssertStatement': 46,
    'ElementArrayValue': 47,
    'TypeParameter': 48,
    'FieldDeclaration': 49,
    'SuperMemberReference': 50,
    'ContinueStatement': 51,
    'ClassDeclaration': 52,
    'TryResource': 53,
    'MethodReference': 54,
    'LambdaExpression': 55,
    'InferredFormalParameter': 56
}


def get_token(node):
    token = ''
    # print(isinstance(node, Node))
    # print(type(node))
    if isinstance(node, str):
        token = node
    elif isinstance(node, set):
        token = 'Modifier'
    elif isinstance(node, Node):
        token = node.__class__.__name__
    # print(node.__class__.__name__,str(node))
    # print(node.__class__.__name__, node)
    return token


def get_child(root):
    # print(root)
    if isinstance(root, Node):
        children = root.children
    elif isinstance(root, set):
        children = list(root)
    else:
        children = []

    def expand(nested_list):
        for item in nested_list:
            if isinstance(item, list):
                for sub_item in expand(item):
                    # print(sub_item)
                    yield sub_item
            elif item:
                # print(item)
                yield item

    return list(expand(children))


def createtree(root, node, nodelist, parent=None):
    id = len(nodelist)
    # print(id)
    token, children = get_token(node), get_child(node)
    if id == 0:
        root.token = token
        root.data = node
    else:
        newnode = AnyNode(id=id, token=token, data=node, parent=parent)
    nodelist.append(token)
    for child in children:
        if id == 0:
            createtree(root, child, nodelist, parent=root)
        else:
            createtree(root, child, nodelist, parent=newnode)


def traversal(node, typedict, id2hash):  # 递归遍历所有节点
    if node.children:
        for child in node.children:
            traversal(child)
    else:  # 遍历至叶子节点，往上依次输出父节点。
        try:
            node.token = typedict[node.token]
        except KeyError:
            if node.token not in nodetypedict:
                node.token = 'String'
        id2hash[node.id] = hash(node.token)
        while node.parent:
            node = node.parent


def fsstype(node, childrendict):
    children = []
    for child in node.children:
        children.append(child.id)
    childrendict[node.id] = children
    for child in node.children:
        fsstype(child, childrendict)


def get_hash(childrendict, nodelist, typedict, id2hash, id2number):
    for i in range(len(childrendict) - 1, -1, -1):
        token = nodelist[i]
        if not childrendict[i]:
            try:
                tokentype = typedict[token]
            except KeyError:
                if token not in nodetypedict:
                    tokentype = 'String'
                else:
                    tokentype = token
            if tokentype != 'Operator':
                token = tokentype
            id2hash[i] = hash(token)
            id2number[i] = 1
        else:
            h = hash(token)
            n = 1
            if token == 'BinaryOperation':
                childtoken = []
                for c in childrendict[i]:
                    childtoken.append(nodelist[c])
                if '/' in childtoken or '-' in childtoken:
                    j = 1
                    for child_id in childrendict[i]:
                        h += j * id2hash[child_id]
                        n += id2number[child_id]
                        j += 1
                else:
                    for child_id in childrendict[i]:
                        h += id2hash[child_id]
                        n += id2number[child_id]
            else:
                for child_id in childrendict[i]:
                    h += id2hash[child_id]
                    n += id2number[child_id]
            id2hash[i] = h
            id2number[i] = n
    HashListArray = [[] for i in range(len(nodelist) + 1)]
    for i in range(len(id2number)):
        children_num = id2number[i]
        HashListArray[children_num].append(i)

    return HashListArray


def one_feature(path):

    # 生成ast和token
    programfile = open(path, encoding='utf-8')
    programtext = programfile.read()
    programtokens = javalang.tokenizer.tokenize(programtext)
    parser = javalang.parse.Parser(programtokens)
    tree = parser.parse_member_declaration()

    programfile.close()

    file = open(path, "r", encoding='utf-8')
    tokens = list(javalang.tokenizer.tokenize(file.read()))
    # print("programtokens", list(tokens))
    file.close()

    # 生成树
    nodelist = []
    newtree = AnyNode(id=0, token=None, data=None)
    createtree(newtree, tree, nodelist)

    # 生成类型字典
    typedict = {}
    for token in tokens:
        token_type = str(type(token))[:-2].split(".")[-1]
        token_value = token.value
        if token_value not in typedict:
            typedict[token_value] = token_type
        else:
            if typedict[token_value] != token_type:
                print('!!!!!!!!')
    childrendict = {}
    fsstype(newtree, childrendict)
    id2hash = {}
    id2number = {}
    HashListArray = get_hash(childrendict, nodelist, typedict, id2hash,
                             id2number)
    # print(id2hash)
    # print(id2number)
    return HashListArray, id2hash, childrendict

def mov(delnode, childrendict, id):
    for i in childrendict[id]:
        delnode.add(i)
        mov(delnode, childrendict, i)


def compare(pair1, pair2):
    datanode = []
    # 不再对比的节点
    delnode1 = set()
    delnode2 = set()
    start = time.time()
    HashListArray1, id2hash1, childrendict1 = one_feature(pair1)
    HashListArray2, id2hash2, childrendict2 = one_feature(pair2)
    end = time.time()
    # file = open('size/a2.txt','ab')
    # pickle.dump((HashListArray1,id2hash1,childrendict1,HashListArray2,id2hash2,childrendict2),file)
    # file.close()
    # size_a = sys.getsizeof(HashListArray1) + sys.getsizeof(id2hash1) +sys.getsizeof(childrendict1)
    # size_b = sys.getsizeof(HashListArray2) + sys.getsizeof(id2hash2) +sys.getsizeof(childrendict2)
    # size = size_a + size_b
    # # print(HashListArray1)
    # print(HashListArray2)
    # print(childrendict1)
    intersection = 0  # 交集
    x = min(len(HashListArray1), len(HashListArray2)) - 1
    while x > 0:
        if HashListArray1[x] == [] or HashListArray2[x] == []:
            x = x - 1
            continue
        hashdict1 = {}
        hashdict2 = {}
        for i in HashListArray1[x]:
            num = 0
            for j in HashListArray2[x]:

                if i in delnode1 or j in delnode2:
                    continue
                elif id2hash1[i] == id2hash2[j]:
                    num += 1
                    datanode.append((i, j, id2hash1[i]))
                    mov(delnode1, childrendict1, i)
                    mov(delnode2, childrendict2, j)
            if num != 0:
                hashdict1[id2hash1[i]] = num
                if id2hash1[i] not in hashdict2:
                    hashdict2[id2hash1[i]] = 1
                else:
                    hashdict2[id2hash1[i]] += 1
        for a in hashdict1:
            intersection += x * min(hashdict1[a], hashdict2[a])

        x = x - 1

    union = len(id2hash1) + len(id2hash2) - intersection  # 并集
    sim = float(intersection) / union if union != 0 else 0  # 相似性
    # print(intersection)
    return datanode, sim#,size


def main(pairspath):
    pairs = csv.reader(open(pairspath, 'r', encoding='gbk'))
    path = '../pairs/'

    for pair in pairs:
        pair1 = path + pair[0] + '.java'
        pair2 = path + pair[1] + '.java'
        datanode, similarity = compare(pair1, pair2)
        print(pair1, pair2)
        # print(datanode)
        print(similarity)


def main1():
    # pair1 = 'test.java'
    # pair2 = 'test2.java'
    pair1 = '11627705.java'
    pair2 = '10601019.java'
    datanode, sim = compare(pair1, pair2)
    print(pair1)
    print(pair2)
    # print(datanode)
    print(sim)

def BWCCA2015(f1,f2):
    sim = compare(f1, f2)
    #print(sim[-1])
    return sim[-1]# ,sim[-1]
    # main('C:\Users\123\Desktop\Projects\codeclone\AST2014\pairs.csv')
    # main1()
    # main（）的参数是克隆对的路径

if __name__=="__main__":
    # source_path = os.path.abspath(".") + '\pairs\\'
    # file1name = source_path + sys.argv[1].split('\\')[-1]
    # file2name = source_path + sys.argv[2].split('\\')[-1]
    # sim = compare(file1name, file2name)
    # print(sim[-1])
    main('../types/type-1.csv')
    #main1()