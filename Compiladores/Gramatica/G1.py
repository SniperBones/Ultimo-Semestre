Frase = "No es palindromo"
def palindromo(palabra):
    global Frase
    Reglas = ["aSa","bSb","cSc","a","b","c"]
    mitad = 0
    if(len(palabra)%2 == 0):
        mitad = int(len(palabra)/2)
    else:
        mitad = int((len(palabra)/2))+1
    
    def analizar(aux,indice):
        global Frase
        if(indice < mitad):
            for r in Reglas:
                if "S" in aux:
                    auxS = aux.replace("S",r)
                    if auxS == palabra:
                        Frase="Es palindromo"
                    analizar(auxS,indice+1)
        else:
            if "S" in aux:
                auxS = aux.replace("S","")
                if(auxS == palabra):
                    Frase="Es palindromo"
                
    if(len(palabra)==1):
        Frase="Es palindromo"
    else:
        for r in Reglas:
            if palabra[0] in r:
                analizar(r,1)
    print(Frase)
    
palindromo("cc")







