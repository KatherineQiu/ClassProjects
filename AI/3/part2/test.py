import sys

import random
import xml.etree.ElementTree as ET
import time
import argparse
from part1.extra import elimination_ask
import math


def formatInput(inputArgv):
    '''format the command line arguments which invoke the program, such as:
    python enumerationAlgo.py aima-alarm.xml B M true J true'''
    X = inputArgv[2]
    e = {}

    for i in range(0, len(inputArgv)):
        if inputArgv[i] == 'true':
            # if inputArgv[i].upper == 'TRUE':
            e.update({inputArgv[i - 1]: True})
        elif inputArgv[i] == 'false':
            e.update({inputArgv[i - 1]: False})
    return X, e


def parser(file_name):
    '''parse .xml file - extract variables, CPT...'''
    tree = ET.parse(file_name)
    root = tree.getroot()

    bn = {}
    vars = []

    if root.tag != 'BIF':
        print 'Only the semi-standard XMLBIF representation can be processed.'
    else:
        root = root[0]
        if root.tag != 'NETWORK':
            print 'The file misses a NETWORK tag.'
        else:
            for i in range(1, len(root)):  # skip the network name

                if root[i].tag == 'VARIABLE':
                    # assumption: all variables have Boolean value
                    vars.append(root[i][0].text)

                elif root[i].tag == 'DEFINITION':

                    for j in range(0, len(root[i])):
                        parents = []
                        values = {}
                        if root[i][j].tag == 'FOR':
                            var = root[i][j].text

                            val = [parents, values]
                            if len(root[i]) == 2:  # no parents
                                if root[i][1].tag == 'TABLE':
                                    # print root[i][1].text
                                    prob = float(root[i][1].text.split(' ')[0])
                                    bn.update({var: [[], {None: prob}]})

                            elif len(root[i]) != 2:
                                for g in range(1, len(root[i])):
                                    if root[i][g].tag == 'GIVEN':
                                        parents.append(root[i][g].text)
                                        bn.update({var: val})

                                    elif root[i][g].tag == 'TABLE':
                                        prob = [x.strip() for x in root[i][g].text.strip().split('\n')]
                                        for r in range(0, len(prob)):
                                            p = float(prob[r].strip().split(' ')[0])
                                            if len(prob) == 2:
                                                if r == 0:
                                                    values.update({(True,): p})
                                                if r == 1:
                                                    values.update({(False,): p})

                                            if len(prob) == 4:
                                                if r == 0:
                                                    values.update({(True, True): p})
                                                if r == 1:
                                                    values.update({(True, False): p})
                                                if r == 2:
                                                    values.update({(False, True): p})
                                                if r == 3:
                                                    values.update({(False, False): p})
    return bn, vars[::-1]


def get_all_related_node(target, bn):
    all_nodes = []
    if target in bn.keys():
        all_nodes.extend(bn[target][0])

    children = []
    sibling = []
    for key in bn.keys():
        if target in bn[key][0]:
            children.extend(key)

    for child in children:
        sibling.extend(bn[child][0])

    all_nodes.extend(children)
    all_nodes.extend(sibling)

    final = list(set(all_nodes))
    final.remove(target)
    return final


def random_sample(result, num):
    true_num = num * result[True]
    false_num = num * result[False]

    if true_num > false_num:
        true_num = int(math.floor(true_num))
        false_num = int(num - true_num)
    else:
        true_num = int(math.ceil(true_num))
        false_num = int(num - true_num)

    randint = random.randint(0, true_num + false_num)

    if randint < true_num:
        return True
    else:
        return False


def get_val(X, e, bn, vars, table):
    if len(table) == 1:
        single_row = []
        for index, var in enumerate(vars):
            if var in e.keys():
                single_row.append(e[var])
            else:
                single_row.append(False)
        table.append(single_row)

    else:
        last_row = table[-1]
        single_row = []
        for var in vars:
            if var not in e.keys():
                all_nodes = get_all_related_node(var, bn)
                tmp_e = {}
                for node in all_nodes:
                    tmp_e[node] = last_row[table[0].index(node)]

                sample_result = elimination_ask(var, tmp_e, bn, vars)

                single_row.append(random_sample(sample_result, 2e6))

            else:
                single_row.append(e[var])

        table.append(single_row)



def gibbs_ask(X, e, bn, vars, N):
    init_table = [[var for var in vars]]
    for i in range(N):
        if i!=0 and i%1000==0:
            print(i)
        get_val(X, e, bn, vars, init_table)

    result_map = {True: 0, False: 0}
    for row in init_table[1:]:
        if row[init_table[0].index(X)]:
            result_map[True] = result_map[True] + 1
        else:
            result_map[False] = result_map[False] + 1

    result_map[True]=float(result_map[True])/(result_map[True]+result_map[False])
    result_map[False]=1-result_map[True]

    return result_map


if __name__ == '__main__':
    time1 = time.time()
    pars = argparse.ArgumentParser()
    pars.add_argument('paras', type=str, nargs='*')
    args = pars.parse_args()
    print(args.paras)

    X, e = formatInput(args.paras)
    print X, e

    bn, vars = parser(args.paras[1])
    print bn, vars

    print(gibbs_ask(X, e, bn, vars, int(args.paras[0])))
