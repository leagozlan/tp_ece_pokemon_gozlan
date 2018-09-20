package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shared.Pokemon;

/**
 * This class represents the server database.
 * In this project, it will simply provides an API for the server to interact with the file system.
 * @author strift
 *
 */

public class Database {

	/**
	 * The name of the file used to store the data
	 */
	private File file;
	
	/**
	 * Constructor
	 * @param fileName the name of the file used to store the data
	 */
	public Database(String fileName) {
		this.file = new File(fileName);
	}
	
	/**
	 * Load the list of Pokemons stored inside the file and returns it
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Pokemon> loadPokemons() throws IOException, ClassNotFoundException {
		ArrayList<Pokemon> data = new ArrayList<Pokemon>();

		// This checks if the file actually exists
		if(this.file.exists() && !this.file.isDirectory()) { 
			/*
			 * TODO
			 * Classes you can use:
			 * - ArrayList<Pokemon>
			 * - FileInputStream
			 * - ObjectInputStream
			 */
                        
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.file.getName())); //on créé un objectinputstream
                        data= (ArrayList<Pokemon>) ois.readObject(); 
                        ois.close(); //on ferme le fichier

		} else {
			System.out.println("Le fichier de sauvegarde est inexistant ");
		}
		
		System.out.println(data.size() + " Pokémons chargés grâce à la sauvegarde.");
		return data;
	}
	
	/**
	 * Save the list of Pokémons received in parameters
	 * @param data the list of Pokémons
	 * @throws IOException 
	 */
	public void savePokemons(ArrayList<Pokemon> data) throws IOException {
		/*
		 * TODO
		 * Classes you can use:
		 * - FileOutputStream
		 * - ObjectOutputStream
		 */
		
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.file.getName())); 
                oos.writeObject(data);
                oos.close(); //fermeture du fichier IMPORTANT

                //Affichage du nombre de pokemons de notre base de donnees
		System.out.println("Sauvegarde faite " + data.size() + " Pokemons enregistrés dans la banque de données");
	}
}