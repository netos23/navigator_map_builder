import os


def rem_dir():
    for root, dirs, files in os.walk(".", topdown=False):
        for name in files:
            filename = os.path.join(root, name)
            print('removed ', filename)
            os.remove(filename)


curPath = os.curdir
os.chdir('history')
rem_dir()
print('history cleared')
