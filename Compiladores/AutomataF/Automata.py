class AF:
    def __init__(self,nArchivo):
        self.nArchivo = nArchivo
        self.Q = self.obtenerTupla(0,False)
        self.E = self.obtenerTupla(1,False)
        self.S = self.obtenerTupla(2,False)
        self.F = self.obtenerTupla(3,False)
        self.T = self.obtenerTupla(4,True)
        self.mostrarTuplas()
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
    def mostrarTuplas(self):
        print('Estados del atomata: '+','.join(self.Q))
        print('Alfabeto: '+','.join(self.E))
        print('Estado Inicial: '+','.join(self.S))
        print('Estado(s) final(es): '+','.join(self.F))
        print('Transiciones: \n\t'+' \n\t'.join(self.T))
        

AF('AF.txt')