#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
@author: Ziyi Kou, Ziqiu Wu
"""

from functions import *
import time


class Compete:
    '''Initialize parameters'''

    def __init__(self, size=3):
        self.pos_size = size ** 2  # game board size 3*3
        self.arr = ['' for _ in range(size ** 2)]  # index of board [0, 9)
        self.HUMAN_TURN = None
        self.HUMAN_MARK, self.AI_MARK = None, None
        self.HUMAN_WIN = False
        self.AI_WIN = False

    '''plot the board block'''

    def plot_block(self):
        for i, str in enumerate(self.arr):
            if (i + 1) % 3 != 0:
                sys.stderr.write(self.arr[i])
                sys.stderr.write(' | ')
            else:
                sys.stderr.write(self.arr[i])
                sys.stderr.write('\n')
                sys.stderr.write('______')
                sys.stderr.write('\n')

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
                     [6, 7, 8], [2, 5, 8]]  # three co-linear cells * 8 situations

        for win_case in win_cases:
            win_pool = set()
            for index in win_case:
                win_pool.add(self.arr[index])  # get the markers of these positions
            if len(win_pool) == 1 and '' not in win_pool:  # containing the same (non-empty) mark
                if self.HUMAN_MARK in win_pool:
                    return 'HUMAN'
                else:
                    return 'AI'

        if len(self.empty_pos()) == 0:  # no winner and no empty spaces
            return 'fair'

        return False

    '''mark the last move position with specific player's marker X or O'''

    def mark(self, pos):
        if self.HUMAN_TURN:
            self.arr[int(pos) - 1] = self.HUMAN_MARK
            self.HUMAN_TURN = False
        else:
            self.arr[int(pos) - 1] = self.AI_MARK
            self.HUMAN_TURN = True

    '''remove the marker from the last move position'''

    def clear_mark(self, pos):
        self.arr[int(pos) - 1] = ''
        self.HUMAN_TURN = not self.HUMAN_TURN  # switch turn

    '''human and AI take turns to mark position'''

    def turn(self):
        if self.HUMAN_TURN:
            try:
                if len(self.empty_pos()) == 9:
                    self.plot_block()
                sys.stderr.write('please input position(1-9):')
                sys.stderr.flush()
                pos = sys.stdin.readline().strip().lower()

                while True:
                        pos = int(pos)
                        if int(pos) not in list(range(1, 10)):  # check if input number range is valid
                            sys.stderr.write('too large number for input!\n')
                            sys.stderr.write('please input position(1-9):')
                            sys.stderr.flush()
                            pos = sys.stdin.readline().strip().lower()
                        elif self.arr[int(pos) - 1] != '':  # check if input position has been marked
                            sys.stderr.write('this position has been marked!\n')
                            sys.stderr.write('please input position(1-9):')
                            sys.stderr.flush()
                            pos = sys.stdin.readline().strip().lower()
                        else:
                            break

            except KeyboardInterrupt:
                sys.stderr.write('\nbye\n')
                exit()
            except:
                sys.stderr.write('invalid input!\n')
                sys.stderr.write('please input position(1-9):')
                sys.stderr.flush()
                pos = sys.stdin.readline().strip().lower()

            self.mark(pos)
            self.plot_block()

            sys.stderr.flush()

        else:
            sys.stderr.write('----AI turn--------\n')
            sys.stderr.flush()
            start = time.clock()
            if len(self.empty_pos()) == 9:
                self.mark(5)
                sys.stdout.write(str(5) + '\n')
            else:
                zero_score_arr = []
                for pos in self.empty_pos():  # try out all possible positions and get score values
                    self.mark(pos)
                    score = self.ai_minmax(-1, 1)  # final score
                    self.clear_mark(pos)

                    output_procedure(pos, score)
                    if score == 1:  # mark the best move position and give turn to human
                        self.mark(pos)
                        sys.stdout.write(str(pos) + '\n')
                        break
                    elif score == 0:
                        zero_score_arr.append(pos)

                if not self.HUMAN_TURN:  # if it's still AI's turn, take the last move of zero score
                    self.mark(zero_score_arr[-1])
                    sys.stdout.write(str(zero_score_arr[-1]) + '\n')

            elapsed = (time.clock() - start)
            sys.stderr.write('Time used: ' + str(round(elapsed, 5)) + ' seconds\n')
            self.plot_block()
            sys.stdout.flush()

    '''get the list of available positions'''

    def empty_pos(self):

        return [i + 1 for i in range(len(self.arr)) if self.arr[i] == '']

    '''find the best move position by using minimax algorithm'''

    def ai_minmax(self, alpha, beta):
        if self.is_win() and self.is_win() == 'HUMAN':
            return -1
        elif self.is_win() and self.is_win() == 'AI':
            return 1
        elif len(self.empty_pos()) == 0:
            return 0

        selected_score = []
        for pos in self.empty_pos():
            self.mark(pos)
            score = self.ai_minmax(alpha, beta)  # use alpha-beta to reduce calculation
            self.clear_mark(pos)
            if self.HUMAN_TURN:
                if score <= alpha:
                    return score
                beta = min(score, beta)

            if not self.HUMAN_TURN:
                if score >= beta:
                    return score
                alpha = max(score, alpha)

            selected_score.append(score)

        if self.HUMAN_TURN:  # when it's human's turn, return the minimum score value
            return min(selected_score)

        if not self.HUMAN_TURN:  # when it's AI's turn, return the maximum score value
            return max(selected_score)

    '''execute the game play with players'''

    def play(self):
        while not self.is_win():  # as long as game is not over, the next player takes turn
            self.turn()
        if 'HUMAN' in self.is_win():
            sys.stderr.write('human win\n')
        elif 'AI' in self.is_win():
            sys.stderr.write('AI win\n')
        else:
            sys.stderr.write('draw\n')
        sys.stderr.flush()


if __name__ == '__main__':
    while True:  # automatically start a new game when the game is over
        sys.stderr.write('------------new game-------------\n')
        test = Compete()
        test.check_first()
        test.play()
