package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import shared.Request;
import shared.Pokemon;

/**
 * This class represents the server application, which is a Pokémon Bank.
 * It is a shared account: everyone's Pokémons are stored together.
 * @author strift
 *
 */
public class PokemonBank {
	
	public static final int SERVER_PORT = 3000;
	public static final String DB_FILE_NAME = "pokemons.db";
	
	/**
	 * The database instance
	 */
	private Database db;
	
	/**
	 * The ServerSocket instance
	 */
	private ServerSocket server;
	
	/**
	 * The Pokémons stored in memory
	 */
	private ArrayList<Pokemon> pokemons;
	
	/**
	 * Constructor
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public PokemonBank() throws IOException, ClassNotFoundException {
		/*
		 * TODO
		 * Here, you should initialize the Database and ServerSocket instances.
		 */
		db= new Database(DB_FILE_NAME);
		server= new ServerSocket(SERVER_PORT, 1, InetAddress.getLocalHost());
                
		System.out.println("Banque Pokémon (" + DB_FILE_NAME + ") connexion sur  " + SERVER_PORT);
		
		// Let's load all the Pokémons stored in database
		this.pokemons = this.db.loadPokemons();
		this.printState();
	}
	
	/**
	 * The main loop logic of your application goes there.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void handleClient() throws IOException, ClassNotFoundException {
		System.out.println("Attente de connexion");
		/*
		 * TODO
		 * Here, you should wait for a client to connect.
		 */
                Socket client=server.accept();
		
		/*
		 * TODO
		 * You will one stream to read and one to write.
		 * Classes you can use:
		 * - ObjectInputStream
		 * - ObjectOutputStream
		 * - BankOperation
		 */
		ObjectInputStream in=new ObjectInputStream(client.getInputStream());
		ObjectOutputStream out=new ObjectOutputStream(client.getOutputStream());
		
		// For as long as the client wants it
		boolean running = true;
		while (running) {
			/*
			 * TODO
			 * Here you should read the stream to retrieve a Request object
			 */
			Request request;
			request= (Request) in.readObject();
			/*
			 * Note: the server will only respond with String objects.
			 */
			switch(request) {
			case LIST:
				System.out.println("Request: LIST");
				if (this.pokemons.size() == 0) {
					/*
* TODO
					 * There is no Pokémons, so just send a message to the client using the output stream.
					 */
					out.writeObject("Aucun pokemon dans la base de données");
                                        out.flush();
				} else {
					/*
* TODO
					 * Here you need to build a String containing the list of Pokémons, then write this String
					 * in the output stream.
					 * Classes you can use:
					 * - StringBuilder
					 * - String
					 * - the output stream
					 */
					pokemons=db.loadPokemons();
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < pokemons.size(); i++) {
                                            sb.append(pokemons.get(i));
                                            sb.append("\n");
                                        }
                                        out.writeObject(sb);
                                        out.flush();
				}
				break;
				
			case STORE:
				System.out.println("Request: STORE");
				/*
* TODO ok
				 * If the client sent a STORE request, the next object in the stream should be a Pokémon.
				 * You need to retrieve that Pokémon and add it to the ArrayList.
				 */
				
                                pokemons.add((Pokemon) in.readObject());
                                out.flush();
				
				/*
* TODO ok
				 * Then, send a message to the client so he knows his Pokémon is safe.
				 */
				out.writeObject("Le pokemon est ok");
                                out.flush();

				break;
				
			case CLOSE:
				System.out.println("Request: CLOSE");
                                out.writeObject("Au revoir Mr.LeClient");
				/*
* TODO
				 * Here, you should use the output stream to send a nice 'Au revoir !' message to the client. 
				 */
				
				// Closing the connection
				System.out.println("Fermeture de la connexion...");
				running = false;
				break;
			}
			this.printState();
		};
		
		/*
* TODO
		 * Now you can close both I/O streams, and the client socket.
		 */
		out.close();
                in.close();
                client.close();
		/*
* TODO
		 * Now that everything is done, let's update the database.
		 */
		db.savePokemons(pokemons);
	}
	
	/**
	 * Print the current state of the bank
	 */
	private void printState() {
		System.out.print("[");
		for (int j = 0; j < this.pokemons.size(); j++) {
			if (j > 0) {
				System.out.print(", ");
			}
			System.out.print(this.pokemons.get(j));
		}
		System.out.println("]");
	}
	
	/**
	 * Stops the server.
	 * Note: This function will never be called in this project.
	 * @throws IOException 
	 */
	public void stop() throws IOException {
            //fermeture
		this.server.close();
	}
}