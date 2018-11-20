# The enumeration algorithm for answering queries on Bayesian networks, without speedups.
import sys
import argparse
import xml.etree.ElementTree as ET
import copy
import time

storage_keys = {}


def formatInput(inputArgv):
    '''format the command line arguments which invoke the program, such as:
    python enumerationAlgo.py aima-alarm.xml B M true J true'''
    X = inputArgv[1]
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


def gen_keys_for_factor(all_vars):
    def helper(max_num, all_vars, keys, complete_keys):
        if len(keys) == max_num:
            complete_keys.append(keys)
            return None

        var = all_vars.pop()
        keys.append(True)
        helper(max_num, copy.deepcopy(all_vars), copy.deepcopy(keys), complete_keys)
        keys.pop()
        keys.append(False)
        helper(max_num, copy.deepcopy(all_vars), copy.deepcopy(keys), complete_keys)

    complete_keys = []
    helper(len(all_vars), all_vars, [], complete_keys)
    return complete_keys


def make_factor(var, e, bn):
    def get_probability_by_factor(var, correct_keys, index, bn):
        var_bool = correct_keys[index][correct_keys[0].index(var)]

        key_pros = bn[var]
        if len(key_pros[0]) == 0:
            if correct_keys[index][0]:
                return key_pros[1][None]
            else:
                return 1 - key_pros[1][None]

        index_var = correct_keys[0].index(var)
        copy_factors = copy.deepcopy(correct_keys[0])
        copy_bool_val = copy.deepcopy(correct_keys[index])
        copy_factors.remove(var)
        del copy_bool_val[index_var]

        zip_val = zip(copy_factors, copy_bool_val)
        sorted_zip = sorted(zip_val, key=lambda pair: key_pros[0].index(pair[0]))

        if var_bool:
            return key_pros[1][zip(*sorted_zip)[1]]
        else:
            return 1 - key_pros[1][zip(*sorted_zip)[1]]

    parents = bn[var][0]

    all_vars = []
    all_vars.extend(parents)
    all_vars.extend(var)

    if len(all_vars) in storage_keys.keys():
        complete_keys = storage_keys[len(all_vars)]
    else:
        complete_keys = gen_keys_for_factor(copy.deepcopy(all_vars))
        storage_keys[len(all_vars)] = complete_keys

    correct_keys = []
    correct_keys.append(all_vars)
    for key in complete_keys:
        single_correct_key = []
        for pair in zip(all_vars, key):
            if pair[0] in e.keys() and pair[1] != e[pair[0]]:
                break
            else:
                single_correct_key.append(pair[1])

        if len(single_correct_key) == len(all_vars):
            correct_keys.append(single_correct_key)

    for i, key in enumerate(correct_keys):
        if i == 0:
            continue
        new_key = get_probability_by_factor(var, correct_keys, i, bn)
        correct_keys[i].append(new_key)
    return correct_keys


def point_wise(result, factor):
    if result == None:
        return factor
    elif set(result[0]) | set(factor[0]) == set(result[0]):
        return result
    elif set(factor[0]) | set(result[0]) == set(factor[0]):
        return factor
    else:
        new_tables = []
        new_tables.append(result[0] + factor[0])
        for row1 in result[1:]:
            for row2 in factor[1:]:
                new_tables.append(row1[:-1] + row2[:-1] + [row1[-1] * row2[-1]])

        del_list = []
        for index, bool_vals in enumerate(new_tables):
            if index == 0:
                continue

            val_map = {}
            for i, val in enumerate(bool_vals):
                if i < (len(bool_vals) - 1) and new_tables[0][i] in val_map.keys() and val != val_map[new_tables[0][i]]:
                    del_list.append(index)
                    break

                if i < (len(bool_vals) - 1):
                    val_map[new_tables[0][i]] = val

        for index in reversed(del_list):
            del new_tables[index]

        reduced_index = []
        valid_indexes = []
        for index, key in enumerate(new_tables[0]):
            try:
                reduced_index.index(key)
            except:
                valid_indexes.append(index)
                reduced_index.append(key)

        reduced_table = []
        reduced_table.append(reduced_index)
        for row in new_tables[1:]:
            single_row = []
            for i in valid_indexes:
                single_row.append(row[i])
            single_row.append(row[-1])
            reduced_table.append(single_row)

        return reduced_table


def sum_out(var, factors):
    result = None

    del_list_index = []
    if len(factors) > 1:
        for index, factor in enumerate(factors):
            if var in factor[0]:
                del_list_index.append(index)
                result = point_wise(result, factor)

    else:
        result = factors[0]
        del_list_index.append(0)

    var_index = result[0].index(var)
    true_var = []
    false_var = []
    for row in result[1:]:
        if row[var_index] == True:
            true_var.append(row)
        else:
            false_var.append(row)

    new_factors = []
    new_keys = copy.deepcopy(result[0])
    new_keys.remove(var)
    new_factors.append(new_keys)
    for row1 in true_var:
        for row2 in false_var:
            if row1[:var_index] + row1[var_index + 1:-1] == row2[:var_index] + row2[var_index + 1:-1]:
                new_factors.append(row1[:var_index] + row1[var_index + 1:-1] + [row1[-1] + row2[-1]])

    for index in reversed(del_list_index):
        del factors[index]
    factors.append(new_factors)


def norm_final_result(final_result):
    total = final_result[0][-1] + final_result[1][-1]
    final_result[0][-1] = final_result[0][-1] / total
    final_result[1][-1] = final_result[1][-1] / total

def elimination_ask(X, e, bn, vars):
    factors = []

    for i, var in enumerate(vars):
        factors.append(make_factor(var, e, bn))

        if var != X and var not in e.keys():
            sum_out(var, factors)

        valid_factors = []
        for factor in factors:
            for val in factor[1:]:
                if val[-1] != 1:
                    valid_factors.append(factor)
                    break

        factors = valid_factors

    factor = factors[0]
    index_X = factor[0].index(X)
    final_result = []
    for val in factor[1:]:
        final_result.append([val[index_X], val[-1]])

    norm_final_result(final_result)

    return final_result


if __name__ == '__main__':
    # get the problem into the program
    # argv = 'python enumerationAlgo.py aima-alarm.xml B M true J true'
    # argv = 'python enumerationAlgo.py aima-wet-grass.xml R S true'
    # argv = raw_input('input the program invoking command, such as "python enumerationAlgo.py aima-alarm.xml B M true J true"')
    time1 = time.time()
    pars = argparse.ArgumentParser()
    pars.add_argument('paras', type=str, nargs='*')
    args = pars.parse_args()
    print(args.paras)

    X, e = formatInput(args.paras)
    print X, e

    bn, vars = parser(args.paras[0])
    print bn, vars

    print(elimination_ask(X, e, bn, vars))
    print(time.time() - time1)
