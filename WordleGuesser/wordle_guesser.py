import os

n = 0
guess_count = 0
possible_guesses = []
known_greens = []
known_golds = []

class Square:
    def __init__(self, letter: str, index: int, color: str):
        self.letter = letter
        self.index = index
        self.color = color

def get_word_length_input() -> int:
    while True:
        try:
            n = int(input("Enter the number of letters in the word you are trying to solve: "))
            if n < 4 or n > 10:
                raise ValueError
            break
        except ValueError:
                print("Please enter an integer between 4 and 11.")
    return n
 
def load_possible_guesses():
    '''Read the file containing words and their scores, and copy it to a list.'''
    dir_path = os.path.dirname(os.path.realpath(__file__))
    filename = os.path.join(dir_path, str(n) + "_letter_words_and_scores.txt")
    arr = []
    with open(filename, 'r', ) as fr:
        lines = fr.readlines()
        for line in lines:
            word = line.strip('\n')
            arr.append(word)
    return arr

def get_word_input() -> list:
    '''Prompt the user for input about n squares and return a list of Squares.'''
    squares_arr = []
    for i in range(n):
        square = get_square_input(i)
        squares_arr.append(square)
    return squares_arr

def get_square_input(i: int) -> Square:
    '''Prompt the user for input about a square and return a Square.'''
    letter = str(input("Enter the " + guess_number(i) + " letter of your Wordle guess: "))
    index = i
    color = str(input("Enter the color of the letter's square (green, gold, or gray): "))
    square = Square(letter, index, color)
    return square

def guess_number(num: int) -> str:
    ordinal = ''
    if num == 0:
        ordinal = "1st"
    elif num == 1:
        ordinal = "2nd"
    elif num == 2: 
        ordinal = "3rd"
    else:
        ordinal = str(num+1) + "th"
    return ordinal
        
def filter_possible_guesses(arr: list, squares_arr: list) -> list:
    '''Reduce the list of possible guesses based on the information in squares_arr and return a new list.'''
    new_list = arr
    for s in squares_arr:       #what's the difference between this and "for s in range(len(squares_arr))"?
        if s.color == 'green':
            new_list = filter_greens(new_list, s)
            if s not in known_greens:
                add_known_green(s)
    for s in squares_arr:
        if s.color == 'gold':
            new_list = filter_golds(new_list, s)
            add_known_gold(s)
    for s in squares_arr:        
        if s.color == 'gray':
            new_list = filter_grays(new_list, s)      # Make sure these are the only 3 possible colors for the user to input
    return new_list

def filter_greens(arr: list, s: Square) -> list:
    '''Return a list of the words from arr that contain s.letter at s.index.'''
    new_list = []
    for i in range(len(arr)):
        if arr[i][s.index] == s.letter:
            new_list.append(arr[i])
    return new_list

def filter_golds(arr: list, s: Square) -> list:
    '''Return a list of the words from arr that contain s.letter, and where s.letter is not at s.index.'''
    new_list = []
    for i in range(len(arr)):
        if arr[i].count(s.letter) > 0:
            if arr[i][s.index] != s.letter:
                new_list.append(arr[i])
    return new_list

def filter_grays(arr: list, s: Square) -> list:
    if contains_letter(known_golds, s):
        new_list = filter_gray_with_gold(arr, s)
        return new_list
    else:
        if contains_letter(known_greens, s):
            new_list = filter_gray_with_green(arr, s)
            return new_list
        else:
            new_list = filter_grays_only(arr, s)
            return new_list

def filter_grays_only(arr: list, s: Square) -> list:
    '''Return a list of the words from arr that do not contain s.letter.'''
    new_list = []
    for i in range(0, len(arr)):
        if arr[i].count(s.letter) == 0:         # What's the difference between this and "not (s.letter in arr[i])"?
            new_list.append(arr[i])
    return new_list

def filter_gray_with_gold(arr: list, s: Square) -> list:
    '''Add to the new list the words that don't contain s.letter at s.index.'''
    new_list = []
    for i in range(len(arr)):
        if arr[i][s.index] != s.letter:
            new_list.append(arr[i])
    return new_list

def filter_gray_with_green(arr: list, s: Square) -> list:
    '''Add to the new list the words that don't contain s.letter at any of the non-green indices.'''
    new_list = []

    indices = [i for i in range(n)] # Number depends on how many letters are in the Wordle variant

    # Needs clearer code
    for i in range(len(known_greens)):
        x = known_greens[i].index
        try:                            # Need to deal with this
            indices.remove(x)
        except: ValueError

    
    for i in range(len(arr)):
        '''Create a sub-string with only the characters from indices.'''
        sub = ""
        for j in (range(len(indices))):
            sub += arr[i][j]
        if sub.count(s.letter) == 0:
            new_list.append(arr[i])
    
    return new_list

def contains_letter(arr: list, s: Square) -> bool:
    for i in range(len(arr)):
        if s.letter == arr[i].letter:
            return True
    return False

def add_known_green(s: Square):
    known_greens.append(s)
    if contains_letter(known_golds, s):
        # Remove the first square whose letter matches s.letter
        for i in range(len(known_golds)):
            if known_golds[i].letter == s.letter:
                known_golds.remove(known_golds[i])
                break

def add_known_gold(s: Square):
    known_golds.append(s)

def print_best_guesses():
    '''Print the first 10 items in the list of possible guesses.'''
    print("Here are your best guesses:")
    for i in range(10):
        print(possible_guesses[i])
    print('')

def print_all_guesses():
    size = len(possible_guesses)
    print("Here are the possible", size, "guesses:")
    for i in range(size):
        print(possible_guesses[i])
    print('')

def print_square(s: Square):
    print("Letter:", s.letter, "Index:", str(s.index), "Color:", s.color)
    
# Main program starts here.
n = get_word_length_input()
possible_guesses = load_possible_guesses()
print_best_guesses()
guess_count += 1

while guess_count < 6:
    squares_arr = get_word_input()
    possible_guesses = filter_possible_guesses(possible_guesses, squares_arr)
    print_all_guesses()
    guess_count += 1
