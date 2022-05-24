from graphviz import Source, Digraph
import networkx as nx
from networkx import DiGraph
from networkx.drawing.nx_pydot import read_dot, write_dot
import matplotlib.pyplot as plt
from networkx import drawing
import os

 
def read_Corana(dotfile):
    H = DiGraph(read_dot(dotfile), strict=True)
    return H


#x-special/nautilus-clipboard
#dot -Tsvg compareG-H.dot > compareGH.svg

def main():
    for _file_in in os.listdir('result/samples_cfg/'):
        try:
            defined_funcs()
            H = read_Corana('result/samples_cfg/' + _file_in)
            print(_file_in, H.number_of_nodes(), H.number_of_edges())
       
        except:
            pass

if __name__ == "__main__":
    main()
