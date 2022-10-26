class AF:
    def __init__(self,nArchivo,cadena):
        self.nArchivo = nArchivo
        self.cadena = cadena
        self.Q = self.obtenerTupla(0,False)
        self.E = self.obtenerTupla(1,False)
        self.S = self.obtenerTupla(2,False)
        self.F = self.obtenerTupla(3,False)
        self.T = self.separarTransiciones()
        self.completarTransicion()
        lista=[]
        listaRama=[]
        self.listaNoE=[]
        self.numero=0
        self.Transicion(self.Q[0],self.cadena,lista,listaRama,self.numero)
        self.mostrarTuplas()
        self.acomodarCaminos(lista[0])

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
        print('=======================================')
        print('===            DEFINICION           ===')
        print('=======================================')
        print('Estados del atomata: '+','.join(self.Q))
        print('Alfabeto: '+','.join(self.E))
        print('Estado Inicial: '+','.join(self.S))
        print('Estado(s) final(es): '+','.join(self.F))
        print('Transiciones:')
        for i in range(len(self.T)):
            print(f'\tq{self.T[i][0]}({self.T[i][1]})->q{self.T[i][2]}')
    
    def revisarCaracter(self,letraARevisar):
        self.letraARevisar = letraARevisar
        if letraARevisar in self.E:
            #print(f'la letra \'{letraARevisar}\' SI esta en el alfabeto')
            return True
        else:
            if not letraARevisar in self.listaNoE:
                print(f'la letra \'{letraARevisar}\' NO esta en el alfabeto:')
                self.listaNoE.append(letraARevisar)
            return False

    def completarTransicion(self):
        print('=======================================')
        print('===      TRANSICIONES FALTANTES     ===')
        print('=======================================')
        for numEstado in range(len(self.Q)):
            for numLetra in range(len(self.E)):
                flagExiste = False
                for i in range(len(self.T)):
                    thisEstado = False
                    thisLetra = False
                    if self.Q[numEstado] in self.T[i][0]:
                        thisEstado = True
                        if self.E[numLetra] in self.T[i][1]:
                            thisLetra = True
                        else:
                            thisLetra = False
                    else:
                        thisEstado = False
                    if thisEstado and thisLetra:
                        flagExiste = True
                        break
                    else:
                        flagExiste = False
                if not flagExiste:
                    print(f'Falta: q{self.Q[numEstado]}({self.E[numLetra]})')
                    self.T.append([self.Q[numEstado],self.E[numLetra],'-1'])
                    #print('Agregado a las transiciones')
        for error in range(len(self.E)):
            self.T.append(['-1', self.E[error], '-1'])
            print(f'Falta: q-1({self.E[error]})')

    def Transicion(self,inicio,cadena,lista,listaRama,numCamino):
        if cadena == '':
            lista.append(listaRama)
        else:
            if self.revisarCaracter(cadena[0]):
                letra=cadena[0]
                for t in range(len(self.T)):
                    regla = self.T[t]
                    #if inicio == '-1':
                    #    print('Estado de error')
                    if regla[0] == inicio and regla[1] == letra and not regla[0]=='-1':
                        if (len(cadena) == 1 and regla[2] in self.F ) or (len(cadena) == 2 and cadena[1] in self.listaNoE and regla[2] in self.F):
                            listaRama.append(f'C({numCamino}): q{regla[0]}({regla[1]})->q{regla[2]} VALIDO')
                        else:
                            if (len(cadena) == 1 and not regla[2] in self.F) or (len(cadena) == 2 and cadena[1] in self.listaNoE and not regla[2] in self.F) or (regla[2]=='-1'):
                                listaRama.append(f'C({numCamino}): q{regla[0]}({regla[1]})->q{regla[2]} NO VALIDO')
                            else:
                                listaRama.append(f'C({numCamino}): q{regla[0]}({regla[1]})->q{regla[2]}')
                        cadenaTemp = cadena[1:]
                        self.Transicion(regla[2],cadenaTemp,lista,listaRama,numCamino+1)
            else:
                listaRama.append(f'C({numCamino}): q{inicio}({cadena[0]})->ME')
                cadenaTemp = cadena[1:]
                self.Transicion(inicio,cadenaTemp,lista,listaRama,numCamino)

    def acomodarCaminos(self, listaCaminos):
        print('=======================================')
        print('===             CAMINOS             ===')
        print('=======================================')
        print(f'  Cadena: {self.cadena}')
        print(f'  !Alfabeto: '+','.join(self.listaNoE))
        print('=======================================')
        tab = '\t'
        for camino in range(len(listaCaminos)):
            if listaCaminos[camino][3] == ')':
                factor = int(listaCaminos[camino][2])
            else:
                factor = (int(listaCaminos[camino][2])*10)+int(listaCaminos[camino][3])
            print((factor)*tab+'╚»'+listaCaminos[camino])
                
#AF('AF.txt','ababzbabababbbbaaaabbaab')
#AF('AF2.txt','acaabcbcab')
#AF('AF3.txt','acbbabab')
AF('AF4.txt','aaabbababa')
