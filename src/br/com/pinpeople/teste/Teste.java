package br.com.pinpeople.teste;

import java.io.IOException;

public class Teste {
	
	public static void main(String[] args) {

		
		Anagrama ordena = new Anagrama();
		
		try {
			
			
			ordena.geraAnagrama();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
	}
	
	
		

	
	
}