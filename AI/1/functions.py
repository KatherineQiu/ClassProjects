import sys


# output the precedure of how AI makes a decision
def output_procedure(pos, score):
    sys.stderr.write('position:[' + str(pos) + ']--->score:' + str(score) + '\n')

    sys.stderr.flush()
