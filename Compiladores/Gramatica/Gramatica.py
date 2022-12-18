def es_palindromo(palabra):
  # Definimos la gramática para palíndromos
  gramatica = """
  S -> aSa | bSb 
  """
  
  # Separamos la gramática en líneas
  reglas = gramatica.split("\n")
  # Creamos un diccionario para almacenar las reglas de la gramática
  diccionario = {}
  for regla in reglas:
    # Separamos la regla en el símbolo no terminal y la producción
    simbolo, produccion = regla.split(" -> ")
    # Añadimos la regla al diccionario
    diccionario[simbolo] = produccion.split(" | ")
  
  # Creamos una función de análisis de gramáticas recursiva
  def analizar(simbolo, palabra):
    # Si el símbolo es una palabra terminal, comparamos con la palabra
    if simbolo in "ab":
      return simbolo == palabra
    # Si no es una palabra terminal, comparamos con cada producción del símbolo
    for produccion in diccionario[simbolo]:
      # Si la producción es una palabra terminal, comparamos con la palabra
      if produccion in "ab":
        if produccion == palabra:
          return True
      # Si la producción es un símbolo no terminal, analizamos recursivamente
      else:
        if analizar(produccion, palabra):
          return True
    # Si no se encontró ninguna coincidencia, la palabra no es un palíndromo
    return False
  
  # Analizamos la palabra con la gramática
  return analizar("S", palabra)

# Pruebas del programa
print(es_palindromo("aa")) # True
