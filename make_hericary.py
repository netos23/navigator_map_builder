import os


def mkdir(name):
    try:
        os.mkdir(name)
        print('created ', name)
    except FileExistsError:
        print(name, 'exist')


mkdir('history')
mkdir('tmp')
