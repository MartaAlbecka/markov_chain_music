# markov_chain_music

Projekt studencki mający na celu stworzenie utworu muzycznego podobnego do zadanego jako dane wejściowe z użyciem łańcuchów Markowa. Porównanie wyników w zależności od rzędu łańcucha.

Łańcuchy Markowa związane są z pojęciem kognitywistyki. Jest to dziedzina nauki zajmującą się modelowaniem działania zmysłów i mózgu, w tym także ludzką twórczością i kreatywnością, na której skupiliśmy się w niniejszym projekcie. Imitujemy przewidywanie następnych wyrazów w ciągu poprzez bazowanie na elementach bezpośrednio poprzedzających dany element. Warty wspomnienia jest fakt, że następny element w ciągu jest wybierany wyłącznie z takich, z którym kombinacja z poprzednikami wystąpiła już w danych wejściowych. 

Prawdopodobieństwa poszczególnych stanów są proporcjonalne do liczby ich wystąpień. Liczba wyrazów poprzedzających branych pod uwagę przy predykcji jest określona przez rząd łańcucha. Im wyższy zadamy rząd, tym bardziej ciąg będzie zbliżony do wejściowego, ponieważ zmniejsza się liczba wystąpień danego stanu następnego. Można powiedzieć, że gotowy ciąg o wysokim rzędzie łańcucha będzie bardziej poprawny, lecz tyczy się to bardziej danych wymagających logicznego szyku. W muzyce wrażenie słuchowe jest raczej abstrakcyjne i trudno stanowić o poprawności utworu. 

Metoda łańcuchów Markowa jest stosunkowo szybka i prosta, co wpływa na to, że jej rezultaty mogą być nielogiczne i nieprzewidywalne. Można jej używać, gdy operujemy na skończonym ciągu jasno określonych, rozróżnialnych elementów ze skończonego przedziału i powtarzających się schematach.
