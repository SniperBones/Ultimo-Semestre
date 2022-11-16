class Automata:
    def __init__(self, nombreDelArchivo, cadenaAEvaluar):
        self.nombreArchivo = nombreDelArchivo
        self.cadena = cadenaAEvaluar
        self.estadosQ = self.obtenerEstados()
        self.estadosFinales = self.obtenerEstadosFinales()
        self.estadoInicial = self.obtenerEstadoInicial()
        self.alfabeto = self.obtenerAlfabeto()
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
        estadosFinales = self.leerTXT()[3].rstrip()
        ESTADOS_FINALES = estadosFinales.split(',')
        return ESTADOS_FINALES

    def obtenerEstadoInicial(self):
        estadoInicial = self.leerTXT()[2].rstrip()
        INICIAL = estadoInicial.split()
        return INICIAL

    def obtenerAlfabeto(self):
        alfabeto = self.leerTXT()[1].rstrip()
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
        # f = open(txtFile, "a")
        # f.write(f'\n')
        # f.close()
        for item in range(len(self.listaTrans)):
            deltas_separados.append(self.listaTrans[item].split(','))
        for state in range(len(self.estadosQ)):  # Este for es para recorrer todos los estados
            for letter in range(len(self.alfabeto)):  # Este for es para recorrer el alfabeto
                SiHay = False
                for i in range(len(deltas_separados)):  # Este for es para recorrer las transiciones
                    esteEstado = False
                    estaLetra = False
                    if int(self.estadosQ[state]) == int(deltas_separados[i][0]):
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
                    # f = open(txtFile, "a")
                    # f.write(f'{self.estadosQ[state]},{self.alfabeto[letter]},-1\n')
                    # f.close()
        # Aqui van las transiciones del estado de error
        for letter in range(len(self.alfabeto)):
            deltas_separados.append(['-1', self.alfabeto[letter], '-1'])
            # f = open(txtFile, "a")
            # f.write(f'-1,{self.alfabeto[letter]},-1\n')
            # f.close()
        print(deltas_separados)
        #   Aqui podria escribir en el TXT para completar el automata
        return deltas_separados

    def revisarCadena(self,letraARevisar):
        self.letraARevisar = letraARevisar
        #for letra in range(len(self.cadena)):
        if letraARevisar in self.alfabeto:
            #print(f'la letra \'{letraARevisar}\' SI esta en el alfabeto')
            return True
        else:
            #print(f'la letra \'{letraARevisar}\' NO esta en el alfabeto:')
            return False

    def transition(self, cadena, empiezoEn, lista, deltasCompletos, ListaIndividual, iteracion):
        if cadena == '':
            # si el parameto cadena llega vacio, hay que ver si el estado en el que estoy es final, si si, que chingon
            # si no lo es, revisar si tiene alguna transicion epsilon que me lleve al siguiente estado, (ver si es final
            # y si no nuevamente revisar si tiene alguna transicion epsilon que me lleve al siguiente estado) etc.
            if empiezoEn in self.estadosFinales:
                #print(f'si estas en un estado final, nais')
                lista.append(ListaIndividual)
            else: #revisar a detalle este ELSE y borrar este comentario una vez hecho eso
                for transicion in range(len(deltasCompletos)):
                    trans = deltasCompletos[transicion]
                    if trans[0] == empiezoEn and trans[1] == 'E':
                        ListaIndividual.append(f'#{iteracion} {trans[0]}({trans[1]})->{trans[2]}')
                        self.transition(cadena, trans[2], lista, deltasCompletos, ListaIndividual, iteracion + 1)
            #print('Se Acabo la cadena')

        else:
            #  Si el primer caracter de la cadena esta en el alfabeto proseguimos
            if self.revisarCadena(cadena[0]):
                primerletraCadena = cadena[0]
                for transicion in range(len(deltasCompletos)):
                    trans = deltasCompletos[transicion]
                    # if empiezoEn == '-1':
                    #     print('Estoy en el estado de error')
                    #  Creo que aqui deberiamos revisar si hay una transicion E
                    if trans[0] == empiezoEn and trans[1] == 'E':
                        ListaIndividual.append(f'#{iteracion} {trans[0]}({trans[1]})->{trans[2]}')
                        self.transition(cadena, trans[2], lista, deltasCompletos, ListaIndividual, iteracion + 1)

                    if trans[0] == empiezoEn and trans[1] == primerletraCadena:
                        #if trans[2] in self.estadosFinales:
                        ListaIndividual.append(f'#{iteracion} {trans[0]}({trans[1]})->{trans[2]}')
                        cadenaTemporal = cadena[1:]
                        self.transition(cadenaTemporal, trans[2], lista, deltasCompletos, ListaIndividual, iteracion + 1)
            #  Si el primer caracter de la cadena NO esta en el alfabeto, pasamos a la siguiente
            #  letra sin cambiar de estado
            else:
                cadenaTemporal = cadena[1:]
                ListaIndividual.append(f'#{iteracion} {empiezoEn}({cadena[0]})')
                #iteracion += 1
                self.transition(cadenaTemporal, empiezoEn, lista, deltasCompletos, ListaIndividual, iteracion)

    def acomodarCaminos(self, listaCaminos):
        tab = '\t'
        for camino in range(len(listaCaminos)):
            factor = int(listaCaminos[camino][1])
            print(factor*tab+'└─>'+listaCaminos[camino])

automata1 = Automata('AT1.txt', 'abcab')
print(f'Estados del automata: {automata1.estadosQ}')
print(f'Estado inicial: {automata1.estadoInicial}')
print(f'Estado(s) final(es): {automata1.estadosFinales}')
print(f'Alfabeto: {automata1.alfabeto}')
print(f'Transiciones delta: {automata1.listaTrans}')

lista = []
ListaIndividual = []
listaErrores = []
deltasCompletos = automata1.completarDeltas('thom2.txt')
iteracion = 0
automata1.transition(automata1.cadena, automata1.estadoInicial[0], lista, deltasCompletos, ListaIndividual, iteracion)
ListaUnica = lista[0]

automata1.acomodarCaminos(ListaUnica)