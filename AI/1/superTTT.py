#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
@author: Ziyi Kou, Ziqiu Wu
"""

import time
from functions import *


class Compete:
    '''Initialize parameters'''
    def __init__(self):
        self.chess_arr = [['*' for i in range(9)] for j in range(9)] # large 9*3*3 board
        self.board_win_arr = ['*' for _ in range(9)] # small 3*3 board that represents the win/draw on each 3*3 board
        self.pre_move = [None, None]
        self.HUMAN_TURN = None

        self.HUMAN_MARK = None
        self.AI_MARK = None

        self.MAX_DEPTH = 5

    '''check whether human first to go and allocate the player marker, assume X plays first'''
    def check_first(self):
        sys.stderr.write('Would u like first(x) or second(o):')
        sys.stderr.flush()
        try:
            while True:
                input = sys.stdin.readline().strip().lower()
                if input == 'x':
                    self.HUMAN_TURN = True
                    self.HUMAN_MARK, self.AI_MARK = 'X', 'O'
                    break
                elif input == 'o':
                    self.HUMAN_TURN = False
                    self.HUMAN_MARK, self.AI_MARK = 'O', 'X'
                    break
                else:
                    sys.stderr.write('invalid input!\n')
                    sys.stderr.write('Would u like first(x) or second(o):')
                    sys.stderr.flush()
        except KeyboardInterrupt:
            sys.stderr.write('\nbye\n')
            exit()

    '''check which player wins or whether game is ended with draw'''
    def is_win(self):
        win_cases = [[0, 1, 2], [0, 3, 6], [0, 4, 8],
                     [3, 4, 5], [1, 4, 7], [2, 4, 6],
                     [6, 7, 8], [2, 5, 8]]

        for win_case in win_cases:
            win_pool = set()
            for index in win_case:
                win_pool.add(self.board_win_arr[index])
            if len(win_pool) == 1 and '*' not in win_pool:
                if self.HUMAN_MARK in win_pool:
                    return 'HUMAN'
                else:
                    return 'AI'

        if len(self.empty_pos()) == 0:
            return 'Fair'

        return False

    '''get the list of available positions, if the specific box is invalid, return whole valid postions on large board'''
    def empty_pos(self):
        if None in self.pre_move:
            return [1 for _ in range(81)]

        empty_pos_arr = []
        single_arr = self.chess_arr[self.pre_move[-1]]
        for i in range(len(single_arr)):
            if '*' in single_arr[i]:
                empty_pos_arr.append([self.pre_move[-1], i])

        if len(empty_pos_arr) != 0:
            return empty_pos_arr
        else:
            for i in range(len(self.chess_arr)):
                for j in range(len(self.chess_arr[i])):
                    if '*' in self.chess_arr[i][j]:
                        empty_pos_arr.append([i, j])

        return empty_pos_arr

    def print_chess(self):
        sys.stderr.write(str(self.chess_arr[0][0]) + ' ' + str(self.chess_arr[0][1]) + ' ' + str(self.chess_arr[0][2])
                         + ' | ' + str(self.chess_arr[1][0]) + ' ' + str(self.chess_arr[1][1]) + ' ' + str(
            self.chess_arr[1][2])
                         + ' | ' + str(self.chess_arr[2][0]) + ' ' + str(self.chess_arr[2][1]) + ' ' + str(
            self.chess_arr[2][
                2]) + '\n')
        sys.stderr.write(str(self.chess_arr[0][3]) + ' ' + str(self.chess_arr[0][4]) + ' ' + str(self.chess_arr[0][5])
                         + ' | ' + str(self.chess_arr[1][3]) + ' ' + str(self.chess_arr[1][4]) + ' ' + str(
            self.chess_arr[1][5])
                         + ' | ' + str(self.chess_arr[2][3]) + ' ' + str(self.chess_arr[2][4]) + ' ' + str(
            self.chess_arr[2][
                5]) + '\n')
        sys.stderr.write(str(self.chess_arr[0][6]) + ' ' + str(self.chess_arr[0][7]) + ' ' + str(self.chess_arr[0][8])
                         + ' | ' + str(self.chess_arr[1][6]) + ' ' + str(self.chess_arr[1][7]) + ' ' + str(
            self.chess_arr[1][8])
                         + ' | ' + str(self.chess_arr[2][6]) + ' ' + str(self.chess_arr[2][7]) + ' ' + str(
            self.chess_arr[2][
                8]) + '\n')
        sys.stderr.write('- - - - - - - - - - - \n')
        sys.stderr.write(str(self.chess_arr[3][0]) + ' ' + str(self.chess_arr[3][1]) + ' ' + str(self.chess_arr[3][2])
                         + ' | ' + str(self.chess_arr[4][0]) + ' ' + str(self.chess_arr[4][1]) + ' ' + str(
            self.chess_arr[4][2])
                         + ' | ' + str(self.chess_arr[5][0]) + ' ' + str(self.chess_arr[5][1]) + ' ' + str(
            self.chess_arr[5][
                2]) + '\n')
        sys.stderr.write(str(self.chess_arr[3][3]) + ' ' + str(self.chess_arr[3][4]) + ' ' + str(self.chess_arr[3][5])
                         + ' | ' + str(self.chess_arr[4][3]) + ' ' + str(self.chess_arr[4][4]) + ' ' + str(
            self.chess_arr[4][5])
                         + ' | ' + str(self.chess_arr[5][3]) + ' ' + str(self.chess_arr[5][4]) + ' ' + str(
            self.chess_arr[5][
                5]) + '\n')
        sys.stderr.write(str(self.chess_arr[3][6]) + ' ' + str(self.chess_arr[3][7]) + ' ' + str(self.chess_arr[3][8])
                         + ' | ' + str(self.chess_arr[4][6]) + ' ' + str(self.chess_arr[4][7]) + ' ' + str(
            self.chess_arr[4][8])
                         + ' | ' + str(self.chess_arr[5][6]) + ' ' + str(self.chess_arr[5][7]) + ' ' + str(
            self.chess_arr[5][
                8]) + '\n')
        sys.stderr.write('- - - - - - - - - - - \n')
        sys.stderr.write(str(self.chess_arr[6][0]) + ' ' + str(self.chess_arr[6][1]) + ' ' + str(self.chess_arr[6][2])
                         + ' | ' + str(self.chess_arr[7][0]) + ' ' + str(self.chess_arr[7][1]) + ' ' + str(
            self.chess_arr[7][2])
                         + ' | ' + str(self.chess_arr[8][0]) + ' ' + str(self.chess_arr[8][1]) + ' ' + str(
            self.chess_arr[8][
                2]) + '\n')
        sys.stderr.write(str(self.chess_arr[6][3]) + ' ' + str(self.chess_arr[6][4]) + ' ' + str(self.chess_arr[6][5])
                         + ' | ' + str(self.chess_arr[7][3]) + ' ' + str(self.chess_arr[7][4]) + ' ' + str(
            self.chess_arr[7][5])
                         + ' | ' + str(self.chess_arr[8][3]) + ' ' + str(self.chess_arr[8][4]) + ' ' + str(
            self.chess_arr[8][
                5]) + '\n')
        sys.stderr.write(str(self.chess_arr[6][6]) + ' ' + str(self.chess_arr[6][7]) + ' ' + str(self.chess_arr[6][8])
                         + ' | ' + str(self.chess_arr[7][6]) + ' ' + str(self.chess_arr[7][7]) + ' ' + str(
            self.chess_arr[7][8])
                         + ' | ' + str(self.chess_arr[8][6]) + ' ' + str(self.chess_arr[8][7]) + ' ' + str(
            self.chess_arr[8][
                8]) + '\n')

    '''get the list of available positions in single targeted box'''
    def is_single_chess_empty(self, chess):

        return len([1 for i in range(len(self.chess_arr[chess])) if self.chess_arr[chess][i] == '*'])

    def minimax(self, depth, alpha, beta):
        if self.is_win() and self.is_win() == 'HUMAN':
            return -100
        if self.is_win() and self.is_win() == 'AI':
            return 100
        if self.is_win() and self.is_win() == 'Fair':
            return 0

        if depth == self.MAX_DEPTH:
            if self.HUMAN_TURN:
                return -1 * self.depth_scores()
            else:
                return self.depth_scores()

        empty_pos_arr = self.empty_pos()
        score_arr = []
        for chess_pos in empty_pos_arr:
            self.mark_pos(chess_pos)
            score = self.minimax(depth + 1, alpha, beta)
            self.clear_chess_pos(chess_pos)
            if self.HUMAN_TURN:
                if score <= alpha:
                    return score
                beta = min(score, beta)

            if not self.HUMAN_TURN:
                if score > beta:
                    return score
                alpha = max(score, alpha)
            score_arr.append(score)

        if self.HUMAN_TURN:
            return min(score_arr)
        else:
            return max(score_arr)

    def clear_chess_pos(self, chess_pos):
        self.chess_arr[chess_pos[0]][chess_pos[1]] = '*'

        self.HUMAN_TURN = not self.HUMAN_TURN
        self.check_single_chess_win()

    def depth_scores(self):
        def single_depth_scores(line):
            human_score = 0
            ai_score = 0
            for digit in line:
                if digit == self.HUMAN_MARK:
                    human_score += 1
                elif digit == self.AI_MARK:
                    ai_score += 1

            if human_score == 2 and ai_score == 0:
                return -2
            elif human_score == 1 and ai_score == 0:
                return -1
            elif human_score == 0 and ai_score == 2:
                return 2
            elif human_score == 0 and ai_score == 1:
                return 1
            else:
                return 0

        def chess_depth_scores(line):
            human_score = 0
            ai_score = 0
            for digit in line:
                if digit == self.HUMAN_MARK:
                    human_score += 1
                elif digit == self.AI_MARK:
                    ai_score += 1

            if human_score == 2 and ai_score == 0:
                return -60
            elif human_score == 1 and ai_score == 0:
                return -30
            elif human_score == 0 and ai_score == 2:
                return 60
            elif human_score == 0 and ai_score == 1:
                return 30
            else:
                return 0

        scores = 0
        score_cases = [[0, 1, 2], [0, 3, 6], [0, 4, 8],
                       [3, 4, 5], [1, 4, 7], [2, 4, 6],
                       [6, 7, 8], [2, 5, 8]]

        for single_chess in self.chess_arr:
            for case in score_cases:
                scores += single_depth_scores([single_chess[case[0]], single_chess[case[1]], single_chess[case[2]]])

        for case in score_cases:
            scores += chess_depth_scores(
                [self.board_win_arr[case[0]], self.board_win_arr[case[1]], self.board_win_arr[case[2]]])

        return scores

    '''check is there is any single board win, if there is, set the mark to small board'''
    def check_single_chess_win(self):
        tmp_chess_win_arr = ['*' for _ in range(9)]
        win_cases = [[0, 1, 2], [0, 3, 6], [0, 4, 8],
                     [3, 4, 5], [1, 4, 7], [2, 4, 6],
                     [6, 7, 8], [2, 5, 8]]

        for i in range(9):
            single_arr = self.chess_arr[i]
            for win_case in win_cases:
                win_pool = set()
                for index in win_case:
                    win_pool.add(single_arr[index])
                if len(win_pool) == 1 and '*' not in win_pool and '-' not in win_pool:
                    if self.HUMAN_MARK in win_pool:
                        tmp_chess_win_arr[i] = self.HUMAN_MARK
                    else:
                        tmp_chess_win_arr[i] = self.AI_MARK

        self.board_win_arr = tmp_chess_win_arr
        for i in range(len(self.chess_arr)):
            if self.board_win_arr[i] == '*':
                for j in range(9):
                    if self.chess_arr[i][j] == '-':
                        self.chess_arr[i][j] = '*'

            else:
                for j in range(9):
                    if self.chess_arr[i][j] == '*':
                        self.chess_arr[i][j] = '-'

    def mark_pos(self, chess_pos):
        if self.HUMAN_TURN:
            self.chess_arr[chess_pos[0]][chess_pos[1]] = self.HUMAN_MARK
        else:
            self.chess_arr[chess_pos[0]][chess_pos[1]] = self.AI_MARK

        self.check_single_chess_win()

        self.pre_move = chess_pos

        self.HUMAN_TURN = not self.HUMAN_TURN

    '''human and AI take turns to mark position'''
    def turn(self):
        if self.HUMAN_TURN:
            while True:
                try:
                    if len(self.empty_pos()) == 81:
                        self.print_chess()
                    if None in self.pre_move:
                        sys.stderr.write('please input chess:')
                        sys.stderr.flush()
                        chess = int(sys.stdin.readline().strip().lower()) - 1
                        sys.stderr.write('please input pos:')
                        sys.stderr.flush()
                        pos = int(sys.stdin.readline().strip().lower()) - 1
                    else:
                        chess = self.pre_move[-1]
                        if self.is_single_chess_empty(chess):
                            sys.stderr.write('you must input at ' + str(self.pre_move[-1] + 1) + ' chess board\n')
                            sys.stderr.write('please input pos:')
                            sys.stderr.flush()
                            pos = int(sys.stdin.readline().strip().lower()) - 1
                        else:
                            sys.stderr.write('chess board' + str(self.pre_move[-1] + 1) + 'targeted chess board full!\n')
                            sys.stderr.write('please input chess:')
                            sys.stderr.flush()
                            chess = int(sys.stdin.readline().strip().lower()) - 1
                            sys.stderr.write('please input pos:')
                            sys.stderr.flush()
                            pos = int(sys.stdin.readline().strip().lower()) - 1

                    if chess not in list(range(0, 9)):
                        sys.stderr.write('please input 1-9 chessboard!\n')
                        continue
                    elif self.chess_arr[chess][pos] != '*':
                        sys.stderr.write('please input valid point!\n')
                        continue
                    else:
                        break

                except KeyboardInterrupt:
                    sys.stderr.write('\nbye\n')
                    exit()
                except:
                    sys.stderr.write('invalid inputs!\n')
                    continue
            self.mark_pos([chess, pos])
            self.print_chess()

        else:
            sys.stderr.write('------------AI TURN------------\n')
            start = time.clock()
            if None in self.pre_move:
                best_chess_pos = [4, 4]
                self.mark_pos(best_chess_pos)
            else:
                best_score = -100
                best_chess_pos = [-1, -1]
                for chess_pos in self.empty_pos():
                    self.mark_pos(chess_pos)
                    score = self.minimax(depth=0, alpha=-100, beta=100) # use minimax to get the best score
                    self.clear_chess_pos(chess_pos)
                    output_procedure([chess_pos[0]+1,chess_pos[1]+1], score)
                    if score == 100:
                        self.mark_pos(chess_pos)
                        break
                    if score >= best_score:
                        best_score = score
                        best_chess_pos = chess_pos

                if not self.HUMAN_TURN:
                    self.mark_pos(best_chess_pos)

            elapsed = (time.clock() - start)
            sys.stderr.write('Time used: ' + str(round(elapsed, 5)) + ' seconds\n')
            self.print_chess()
            sys.stdout.write(str(best_chess_pos[0]+1) + str(best_chess_pos[1]+1) + '\n')

    '''execute the game play with players'''
    def play(self):
        while not self.is_win(): # as long as game is not over, the next player takes turn
            self.turn()

        sys.stderr.write(self.is_win())


if __name__ == "__main__":
    while True:
        compete = Compete()
        compete.check_first()
        compete.play()
