package br.com.pinpeople.teste;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Anagrama {
	
	private char[] palavra;
	private char[][] matriz;
	private int[][] guardaIndice;
	private String anagrama;
	
	/*
	 * 
	 * 	A classe Anagrama recebe um arquivo de texto chamado Input.txt e,
	 *  palavra por palavra, gera anagramas na forma de matrizes, que ao
	 *  final são concatenadas e exportadas para o arquivo Output.txt.
	 * 
	 * 	
	 * A matriz guardaIndice possui um papel muito importante, pois
	 * garante que os caracteres adcionados à matriz estão sendo sempre
	 * referenciados ao seu índice no vetor palavra. Do contrário, se essa
	 * não existisse, poderia haver problemas quando houvesse caracteres
	 * repetidos na mesma palavra.  
	 * 
	 * 
	 */
	
	public Anagrama(){
		anagrama = new String();
	}

	public void geraAnagrama() throws IOException{
		
		String path = new File("").getAbsolutePath();
		FileReader arquivo = new FileReader(path + "/arquivos/input.txt");
		BufferedReader dados = new BufferedReader(arquivo);
		
		// conforme o enunciado, a quantidade de palavras
		// é definida pelo numero na primeira linha do arquivo input,
		// e palavras além desde número são ignoradas.
		int qtdPalavras = 1;
		int contadorPalavras = 0;
		boolean leuQuantidade = false;
		
		while(dados.ready() && contadorPalavras < qtdPalavras){
			
			if(leuQuantidade == false){
				qtdPalavras = Integer.parseInt(dados.readLine());
				leuQuantidade = true;
			}			
			
			String p = dados.readLine();
			System.out.println(p);
			setPalavra(p);
			setMatrizes();
			
			geraMatriz();
			
			anagrama = anagrama.concat(getMatrizString());
			
			contadorPalavras++;
		}
		dados.close();
		
		exportaArquivo();
		
		System.out.println("\tAnagrama: ");
		System.out.println("--------------------");
		System.out.println(anagrama);
		
	}
	
	public void exportaArquivo() throws IOException{

		String path = new File("").getAbsolutePath();
		FileWriter arquivo = new FileWriter(path + "/arquivos/output.txt");
		PrintWriter dados = new PrintWriter(arquivo);
		dados.print(anagrama);
		dados.close();
		arquivo.close();
		System.out.println("Gravado!");
		
	}


	public void setPalavra(String linha){
		
		palavra = selectionSort(linha.toCharArray());
		
	}
	
	public void setMatrizes(){
		
		int maxCombinacoes = calculaPossibilidades(0, palavra.length);
		
		matriz = new char[maxCombinacoes][palavra.length];;
		guardaIndice = new int[maxCombinacoes][palavra.length];;
		preencheMatrizVazia(guardaIndice);
		
	}
	
	
	
	
	
	
	
	
	public void geraMatriz(){
		
		preenchePrimeiraFileira();
		
		for (int coluna = 1; coluna < matriz[0].length; coluna++) {

			int linha = 0;
			
			

			while(matriz[matriz.length-1][coluna] == 0){

				int[] novaFileira = criaNovaFileira(coluna, linha);
				// PREENCHE

				for (int i = 0; i < novaFileira.length; i++) {
					
					matriz[linha][coluna] = palavra[novaFileira[i]];
					guardaIndice[linha][coluna] = novaFileira[i];
					
					linha++;
				}
				
			}

		}
		
		imprimirMatrizInt(guardaIndice);
	
	}
	
	
	
	public int[] criaNovaFileira(int coluna, int linha){
		
		// Determina o tamanho do vetor baseado na quantidade de vezes
		// que a letra imediatamente à sua esquerda pode aparecer se o
		// vetor estiver em ordem alfabética.
		int[] novaFileira = new int[calculaPossibilidades(coluna-1, palavra.length-1)]; // no caso, 6
		
		List<Integer> listaLetrasUsadas = new ArrayList<>();
		List<Integer> listaPalavra = new ArrayList<>();
		
		
		
		// add os índices das letras usadas na linha a listaLetrasUsadas
		for(int j = 0; j < coluna; j++){

			listaLetrasUsadas.add(guardaIndice[linha][j]);

		}
		// add todas as letras da palavra atual à listaPalavra
		for (int i = 0; i < palavra.length; i++) {
			listaPalavra.add(i);
		}
		// remove os índices das letras usadas de listaPalavra
		listaPalavra.removeAll(listaLetrasUsadas);
		
		
		
		
		// cria uma nova fileira de vetor usando as letras que ainda
		// não foram usadas.
		int contador = 0;
		int i = 0;
		while(contador < listaPalavra.size()){
			
			
			for (int j = 0; j < calculaPossibilidades(coluna, palavra.length-1); j++) {
				
				novaFileira[i] = listaPalavra.get(contador);
				i++;
				
			}
			
			contador++;
		}
		
		return novaFileira;
	}
	
	
	
	
	// como a primeira matriz não pode ler nenhuma coluna esquerda,
	// ela precisa ser criada de maneira separada.
	public void preenchePrimeiraFileira(){

		int linha = 0;
		int posPalavra = 0;
		while(matriz[matriz.length-1][0] == 0){

			for(int i = 0; i < calculaPossibilidades(0, palavra.length - 1); i++){
				
				matriz[linha][0] = palavra[posPalavra];
				guardaIndice[linha][0] = posPalavra;
				
				linha++;
			}
			posPalavra++;			
		}

	}
	
	
	
	public void preencheMatrizVazia(int[][] matrizIndice){
		for (int i = 0; i < matrizIndice.length; i++) {
			for (int j = 0; j < matrizIndice[0].length; j++) {
				
				matrizIndice[i][j] = -1;
				
			}
		}
	}
	
	
	

	public String getMatrizString(){
		
		String m = new String();
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				m = m.concat(String.valueOf(matriz[i][j]));
			}
			m = m.concat("\n");
			
		}
		return m;
	}
	
	
	

	
	// Calcula a possibilidade de vezes seguidas em que uma letra pode cair
	// na mesma coluna em ordem alfabética baseado na expressão fatorial.
	public int calculaPossibilidades(int posicaoAtual, int tamanhoPalavra){
		
		int tamanho = tamanhoPalavra - posicaoAtual; 
		
		int valor = 1; //base de cálculo.
		 
		for (int i = 0; i < tamanho; i++) {
			
			valor = valor * (i + 1);
			
		}
		
		return valor;
	}
	
	

	
	
	
	
	
	// COLOCA PALAVRA EM ORDEM ALFABETICA

	public char[] selectionSort(char[] v) {
		for(int i = 0; i < v.length - 1; i ++) {
			int j = menor(v, i);
			char aux = v[i];
			v[i] = v[j];
			v[j] = aux ;
		}
		
		return v;
	}

	public int menor(char[] v, int i){
		int pos = i;
		i ++;
		while (i < v.length ) {
			if (v[i] < v[pos]){
				pos = i;
			}
			i++;
		}
		return pos;
	}


	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 	É interessante usar os método de impressão de matriz para testes
	 *  para entender melhor principalmente o funcionamento da matriz
	 *  guardaIndice.
	 * 
	 * 
	 */
	
	
	
	
	// PARA TESTES
	public void imprimirMatriz(){
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				
				System.out.print(matriz[i][j]);
				
			}
			System.out.println();
			
		}
	}
	
	public void imprimirMatrizInt(int[][] matriz){
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				
				System.out.print("\t"+matriz[i][j]);
				
			}
			System.out.println();
			
		}
	}
	

}
