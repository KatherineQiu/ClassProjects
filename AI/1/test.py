import sys


class Compete:
    def __init__(self, size=3):
        self.pos_size = size ** 2
        self.arr = ['' for _ in range(size ** 2)]
        self.HUMAN_TURN = None
        self.HUMAN_MARK, self.AI_MARK = None, None
        self.HUMAN_WIN = False
        self.AI_WIN = False

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

    def check_first(self):
        sys.stderr.write('Would u like first(1) or second(2):')
        if int(input()) == 1:
            self.HUMAN_TURN = True
            self.HUMAN_MARK, self.AI_MARK = 'X', 'O'
        else:
            self.HUMAN_TURN = False
            self.HUMAN_MARK, self.AI_MARK = 'O', 'X'

    def is_win(self):
        if len(self.empty_pos()) == 0:
            return 'fair'

        win_cases = [[0, 1, 2], [0, 3, 6], [0, 4, 8],
                     [3, 4, 5], [1, 4, 7], [2, 4, 6],
                     [6, 7, 8], [2, 5, 8]]

        for win_case in win_cases:
            win_pool = set()
            for index in win_case:
                win_pool.add(self.arr[index])
            if len(win_pool) == 1 and '' not in win_pool:
                if self.HUMAN_MARK in win_pool:
                    return 'HUMAN'
                else:
                    return 'AI'

        return False

    def mark(self, pos):
        if self.HUMAN_TURN:
            self.arr[int(pos) - 1] = self.HUMAN_MARK
            self.HUMAN_TURN = False
        else:
            self.arr[int(pos) - 1] = self.AI_MARK
            self.HUMAN_TURN = True

    def clear_mark(self, pos):
        self.arr[int(pos) - 1] = ''
        self.HUMAN_TURN = not self.HUMAN_TURN

    def turn(self):
        if self.HUMAN_TURN:
            sys.stderr.write('please input position:')
            pos = input()

            if int(pos) not in list(range(1, 10)):
                sys.stderr.write('too large number for input!\n')
            elif self.arr[int(pos) - 1] != '':
                sys.stderr.write('this place has been marked!\n')
            else:
                self.mark(pos)
                self.plot_block()

        else:
            sys.stderr.write('----AI turn--------')
            sys.stderr.write('\n')
            zero_score_arr = []
            for pos in self.empty_pos():
                self.mark(pos)
                score = self.ai_minmax(-1, 1)
                self.clear_mark(pos)

                if score == 1:
                    self.mark(pos)
                    self.plot_block()
                    sys.stdout.write(str(pos))
                    sys.stderr.write('\n')
                    break
                elif score == 0:
                    zero_score_arr.append(score)

            if not self.HUMAN_TURN:
                self.mark(pos)
                self.plot_block()
                sys.stdout.write(str(pos))
                sys.stderr.write('\n')

    def empty_pos(self):

        return [i + 1 for i in range(len(self.arr)) if self.arr[i] == '']

    def ai_minmax(self, min, max):
        if self.is_win() and self.is_win() == 'HUMAN':
            return -1
        elif self.is_win() and self.is_win() == 'AI':
            return 1
        elif len(self.empty_pos()) == 0:
            return 0

        for pos in self.empty_pos():
            self.mark(pos)
            score = self.ai_minmax(min, max)
            self.clear_mark(pos)
            if self.HUMAN_TURN and score < max:
                max = score
                if min == max:
                    return max
            if not self.HUMAN_TURN and score > min:
                min = score
                if min == max:
                    return min

        if self.HUMAN_TURN:
            return max
        else:
            return min

    def play(self):
        while not self.is_win():
            self.turn()
        if 'HUMAN' in self.is_win():
            sys.stderr.write('human win')
        elif 'AI' in self.is_win():
            sys.stderr.write('AI win')
        else:
            sys.stderr.write('fair')


if __name__ == '__main__':
    test = Compete()
    test.check_first()
    test.play()
