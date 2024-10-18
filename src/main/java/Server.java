import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server{

	int count = 1;
	ArrayList<Integer> clientNumber = new ArrayList<>();
	HashMap<Integer, ClientThread> Clients = new HashMap<>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clientNumber.add(count);
				Clients.put(count,c);
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			public void updateClientList()
			{
				Set<Integer> numbers =  Clients.keySet();
				clientNumber = new ArrayList<>(numbers);
				for(int num:clientNumber)
				{
					ClientThread t = Clients.get(num);
					try {
						t.out.writeObject(clientNumber);
					}
					catch(Exception e) {}
				}
			}
			public void updateClients(String message)
			{
				for(int num:clientNumber)
				{
					ClientThread t = Clients.get(num);
					try {
						t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}
			public void updateSome(String message, int num,String data)
			{
				if(Clients.containsKey(num)) {
					if(num != count)
					{
						ClientThread t = Clients.get(num);
						try {
							callback.accept("client " + count + " to client " + num + " : " + data);
							t.out.writeObject(message);
						} catch (Exception e) {
						}
					}
				}
				else
				{
					ClientThread t = Clients.get(count);
					try {
						callback.accept("client " + count + "sent '"+ data +"' to a client not connected");
						t.out.writeObject("client # "+num+" not connected");
					} catch (Exception e) {
					}
				}
			}

			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				updateClientList();
				updateClients("new client on server: client #"+count);
					
				 while(true) {
					    try {
					    	String data = in.readObject().toString();
							int num = (Integer) in.readObject();
							updateSome("client #"+count+" said: "+data,num, data);

//							callback.accept("client: " + count + " sent: " + data);
//					    	updateClients("client #"+count+" said: "+data);
					    	
					    	}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	updateClients("Client #"+count+" has left the server!");
							clientNumber.remove(Integer.valueOf(count));
					    	Clients.remove(count);
							updateClientList();
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	
