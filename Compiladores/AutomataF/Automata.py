from pickle import APPEND
from re import T
from sys import api_version


class AF:
    def __init__(self,nArchivo,cadena):
        self.nArchivo = nArchivo
        self.cadena = cadena
        self.Q = self.obtenerTupla(0,False)
        self.E = self.obtenerTupla(1,False)
        self.S = self.obtenerTupla(2,False)
        self.F = self.obtenerTupla(3,False)
        self.T = self.separarTransiciones()
        self.Trancision(self.S[0],cadena)
    def leerArchivo(self):
        with open(self.nArchivo) as file:
            contenido = file.readlines()
        return contenido
    def obtenerTupla(self,x,flag):
        if flag :
            cadena = self.leerArchivo()[x:]
            lista = []
            for i in range(len(cadena)):
                lista.append(cadena[i].rstrip())  
        else:
            cadena = self.leerArchivo()[x].rstrip()
            lista = cadena.split(',')
        return lista
    def separarTransiciones(self):
        x = []
        auxT = self.obtenerTupla(4,True)
        for i in range(len(auxT)):
            x.append(auxT[i].split(','))
        return x
    def mostrarTuplas(self):
        print('Estados del atomata: '+','.join(self.Q))
        print('Alfabeto: '+','.join(self.E))
        print('Estado Inicial: '+','.join(self.S))
        print('Estado(s) final(es): '+','.join(self.F))
    def Trancision(self,inicio,string):
        print(f'Cadena:{string} , Longitud:{len(string)}')
        if string!='' and len(string)!=0:
            for tRange in range(len(self.T)):
                filaTrancision = self.T[tRange]
                ##print(filaTrancision)
                if filaTrancision[0] == inicio and filaTrancision[1] == string[0]:
                    print(f'q{filaTrancision[0]} -{filaTrancision[1]}-> q{filaTrancision[2]}')
                    cadenaTemp = string[1:]
                    self.Trancision(filaTrancision[2],cadenaTemp)
        if string=='':
            print('No hay caracter a evaluar')


AF('AF.txt','aaab')