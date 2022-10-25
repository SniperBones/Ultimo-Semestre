class Automata:
    def __init__(self, nombreDelArchivo, cadenaAEvaluar):
        self.nombreArchivo = nombreDelArchivo
        self.cadena = cadenaAEvaluar
        self.estadosQ = self.obtenerEstados()
        self.estadosFinales = self.obtenerAlfabeto()
        self.estadoInicial = self.obtenerEstadoInicial()
        self.alfabeto = self.obtenerEstadosFinales()
        self.deltas = self.obtenerDeltas()
        self.listaTrans = self.transiciones()

    def mostrarInputs(self):
        print(self.nombreArchivo)
        print(self.cadena)

    def leerTXT(self):
        with open(self.nombreArchivo) as f:
            contenido = f.readlines()
        return contenido

    def obtenerEstados(self):
        estadosQ = self.leerTXT()[0].rstrip()
        ESTADOS_Q = estadosQ.split(',')
        return ESTADOS_Q

    def obtenerEstadosFinales(self):
        estadosFinales = self.leerTXT()[1].rstrip()
        ESTADOS_FINALES = estadosFinales.split(',')
        return ESTADOS_FINALES

    def obtenerEstadoInicial(self):
        estadoInicial = self.leerTXT()[2].rstrip()
        INICIAL = estadoInicial.split()
        return INICIAL

    def obtenerAlfabeto(self):
        alfabeto = self.leerTXT()[3].rstrip()
        SIGMA = alfabeto.split(',')
        return SIGMA

    def obtenerDeltas(self):
        deltas = self.leerTXT()[4:]
        return deltas

    def transiciones(self):
        listaTransiciones = []
        for i in range(len(self.deltas)):
            listaTransiciones.append(self.deltas[i].rstrip())
        return listaTransiciones

    def completarDeltas(self,txtFile):
        deltas_separados = []
        f = open(txtFile, "a")
        f.write(f'\n')
        f.close()
        for item in range(len(self.listaTrans)):
            deltas_separados.append(self.listaTrans[item].split(','))
        for state in range(len(self.estadosQ)):  # Este for es para recorrer todos los estados
            for letter in range(len(self.alfabeto)):  # Este for es para recorrer el alfabeto
                SiHay = False
                for i in range(len(deltas_separados)):  # Este for es para recorrer las transiciones
                    esteEstado = False
                    estaLetra = False
                    if self.estadosQ[state] in deltas_separados[i][0]:
                        esteEstado = True
                        if self.alfabeto[letter] in deltas_separados[i][1]:
                            estaLetra = True
                            print(
                                f'el estado {self.estadosQ[state]} y la letra {self.alfabeto[letter]} si estan en la lista de transiciones')
                        else:
                            estaLetra = False
                    else:
                        esteEstado = False
                    if esteEstado and estaLetra:
                        SiHay = True
                        break
                    else:
                        SiHay = False
                if not SiHay:
                    print(f'Falta: {self.estadosQ[state]} -> {self.alfabeto[letter]}')
                    deltas_separados.append([self.estadosQ[state], self.alfabeto[letter], '-1'])
                    print('ya no falta...')
                    f = open(txtFile, "a")
                    f.write(f'{self.estadosQ[state]},{self.alfabeto[letter]},-1\n')
                    f.close()
        # Aqui van las transiciones del estado de error
        for letter in range(len(self.alfabeto)):
            deltas_separados.append(['-1', self.alfabeto[letter], '-1'])
            f = open(txtFile, "a")
            f.write(f'-1,{self.alfabeto[letter]},-1\n')
            f.close()
        print(deltas_separados)
        #   Aqui podria escribir en el TXT para completar el automata
        return deltas_separados

    def revisarCadena(self,letraARevisar):
        self.letraARevisar = letraARevisar
        #for letra in range(len(self.cadena)):
        if letraARevisar in self.alfabeto:
            print(f'la letra \'{letraARevisar}\' SI esta en el alfabeto')
            return True
        else:
            print(f'la letra \'{letraARevisar}\' NO esta en el alfabeto:')
            return False

    def transition(self, cadena, empiezoEn, lista, listaErrores, deltasCompletos, ListaIndividual, iteracion):
        if cadena == '':
            # si el parameto cadena llega vacio, se termino el programa, no hay nada mas que hacer
            print('Se Acabo la cadena')
            lista.append(ListaIndividual)
        else:
            #  Si el primer caracter de la cadena esta en el alfabeto proseguimos
            if self.revisarCadena(cadena[0]):
                primerletraCadena = cadena[0]
                for transicion in range(len(deltasCompletos)):
                    trans = deltasCompletos[transicion]
                    if empiezoEn == '-1':
                        print('Estoy en el estado de error')

                    if trans[0] == empiezoEn and trans[1] == primerletraCadena:
                        #if trans[2] in self.estadosFinales:
                        ListaIndividual.append(f'#{iteracion} {trans[0]}({trans[1]})->{trans[2]}')
                        cadenaTemporal = cadena[1:]
                        self.transition(cadenaTemporal, trans[2], lista, listaErrores, deltasCompletos,ListaIndividual, iteracion+1)
            #  Si el primer caracter de la cadena NO esta en el alfabeto, pasamos a la siguiente
            #  letra sin cambiar de estado
            else:
                cadenaTemporal = cadena[1:]
                ListaIndividual.append(f'#{iteracion} {empiezoEn}({cadena[0]})')
                #iteracion += 1
                self.transition(cadenaTemporal, empiezoEn, lista, listaErrores, deltasCompletos,ListaIndividual, iteracion)

    def acomodarCaminos(self, listaCaminos):
        tab = '\t'
        for camino in range(len(listaCaminos)):
            factor = int(listaCaminos[camino][1])
            print(factor*tab+'└─>'+listaCaminos[camino])

automata1 = Automata('AF.txt', 'azbabb')
print(f'Estados del automata: {automata1.estadosQ}')
print(f'Estado inicial: {automata1.estadoInicial}')
print(f'Estado(s) final(es): {automata1.estadosFinales}')
print(f'Alfabeto: {automata1.alfabeto}')
print(f'Transiciones delta: {automata1.listaTrans}')

lista = []
ListaIndividual = []
listaErrores = []
deltasCompletos = automata1.completarDeltas('3.txt')
iteracion = 0
automata1.transition(automata1.cadena, automata1.estadoInicial[0], lista, listaErrores, deltasCompletos, ListaIndividual, iteracion)
ListaUnica = lista[0]

automata1.acomodarCaminos(ListaUnica)