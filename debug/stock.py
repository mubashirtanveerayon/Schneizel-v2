import os,subprocess,time


engine_path = "stockfish-windows-x86-64-avx2.exe"


commands = ["position fen rn2k1nr/1R2b3/8/p1p1p1pp/b2pN3/3P1P1P/qP2P1B1/2BQ1KNR w kq - 1 16","go perft 6"]

# commands = ["position fen r3k1nr/1R2b3/2n5/p1p1p1pp/b2pN3/3P1P1P/qP2P1B1/2BQ1KNR b kq - 4 17","go perft 2"]

engine = subprocess.Popen(engine_path,universal_newlines = True, stdin=subprocess.PIPE,stdout = subprocess.PIPE,bufsize = 1)

def put(command):
    engine.stdin.write(command+'\n')

def get():
# using the 'isready' command (engine has to answer 'readyok')
# to indicate current last line of stdout
    engine.stdin.write('isready\n')
    text = []
    while True:
        line = engine.stdout.readline().strip()
        if line != "":
            text.append(line)
        if line == 'readyok':
            break
    return text

for cmd in commands:
    put(cmd)

time.sleep(10)

sf = get()[1:]
sf = sf[:len(sf)-1]

with open("output.txt","r") as file:
    engine = file.readlines()


engine_dict = {}
for line in engine:
    if "Nodes" not in line:
        move = line.split(": ")[0]
        movenum = int(line.split(": ")[1].split("\n")[0])
        engine_dict[move] = movenum

sf_dict = {}
for line in sf:
    if "Nodes" not in line:
        move = line.split(": ")[0]
        movenum = int(line.split(": ")[1])
        sf_dict[move] = movenum

print(len(engine_dict)==len(sf_dict))
print(len(engine)==len(sf))

print("Engine\n")
print(engine)
print("sf\n")
print(sf)

for engine_move in engine_dict:
    engine_movenum = engine_dict[engine_move]
    try:
        if sf_dict[engine_move] != engine_movenum:
            print(f"engine report: {engine_move}={engine_movenum}")
            print(f"fish report: {engine_move}={sf_dict[engine_move]}")
    except KeyError as error:
        print(f"invalid move {engine_move}")
        continue



for sf_move in sf_dict:
    sf_movenum = sf_dict[sf_move]
    try:
        if engine_dict[sf_move] != sf_movenum:
            print(f"engine report: {sf_move}={engine_dict[sf_move]}")
            print(f"fish report: {sf_move}={sf_dict[sf_move]}")
    except KeyError as error:
        print(f"Couldnt find move {sf_move}")
        continue








