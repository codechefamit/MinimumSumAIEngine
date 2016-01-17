import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends  Thread {
	  public BufferedReader is = null;
	  public PrintStream os = null;
	  public Socket clientSocket = null;
	  private final ClientThread[] threads;
	  private int maxClientsCount,myTurn;
	  private final Turn turn;
	  private final SumGame sg;
	  public String name;
	  public int total;
	  public ClientThread(Socket clientSocket, ClientThread[] threads,Turn turn,int myTurn,SumGame sg) {
		    this.clientSocket = clientSocket;
		    this.threads = threads;
		    this.turn=turn;
		    maxClientsCount = threads.length;
		    this.myTurn=myTurn;
		    this.sg=sg;
		   try {
				os = new PrintStream(clientSocket.getOutputStream());  
				is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	  
	  public ClientThread(Socket clientSocket, ClientThread[] threads,Turn turn,int myTurn,SumGame sg,int total,String name,BufferedReader is,PrintStream os) {
		    this.clientSocket = clientSocket;
		    this.threads = threads;
		    this.turn=turn;
		    maxClientsCount = threads.length;
		    this.myTurn=myTurn;
		    this.sg=sg;
		    this.total=total;
		    this.name=name;
		    this.os=os;
		    this.is=is;
		  }
	  
	  public void run(){
		  int maxClientsCount = this.maxClientsCount,max=0;
		  boolean game=true;
		   ClientThread[] threads = this.threads;
		   try {
			  if(name==null)
		   name = is.readLine().trim();
		  while(game){
		  if(myTurn==turn.whoseTurn()){
			  for (int i = 0; i < maxClientsCount; i++) {
		          if (threads[i] != null && threads[i] != this) {
		            threads[i].os.println("<" + name + "'s> turn");
		          }
		        }
			  for (int i = 0; i < maxClientsCount; i++) {
		          if (threads[i] != null && threads[i] != this) {
		            threads[i].os.println("faceUp-"+sg.faceUp);
		            Card cd[]=sg.players.get(i);
		            for(Card c:cd){
						if(c!=null)
						threads[i].os.println(c);
					}
		            threads[i].os.println("end");
		          }
		        }
			  
			  os.println("<Your> turn");
			  os.println("faceUp-"+sg.faceUp);
			  Card cd[]=sg.players.get(myTurn-1);
			  for(Card c:cd){
					if(c!=null)
					os.println(c);
				}
			  os.println("end");
			  String move = is.readLine().trim();
			  if(move.length()>1){
					int r=Integer.parseInt(move.split("-")[0]);
					int ch=Integer.parseInt(move.split("-")[1]);
					sg.faceUp=sg.replace(r, ch, sg.players.get(myTurn-1),myTurn-1);
					  for (int i = 0; i < maxClientsCount; i++) {
						  if (threads[i] != null && threads[i] != this) {
			                threads[i].os.println("<" + name + ">" +r+"-"+ch);
						  }
			            }
					  turn.next();  
				}
			  else{
					 for (int i = 0,k=0; i < maxClientsCount; i++,k++) {
			              threads[i].os.println("<"+ name + "'s> Show" );
			              if (threads[i] != null) {
			            	  int min=sg.sum(0);
			            	  for (int j = 0; j < maxClientsCount; j++) {
			            		  threads[i].os.println("<"+threads[j].name+"'s> Sum :"+sg.sum(j));
			            		  if(min>sg.sum(j)){
			            			  min=sg.sum(j);
			            		  }
			            	  }
			            	  int plWon=0;
			            	  for (int j = 0; j < maxClientsCount; j++) {
			            		  if(threads[j]!=this){
			            			  if(min==sg.sum(j)){
			            					  	threads[i].os.println("<"+threads[j].name+"> Won");
			            			  }
			            			  else if(min<sg.sum(j) && k==0){
			            				  threads[j].total+=sg.sum(j);
			            			  }
			            			  if(sg.sum(j)<sg.sum(myTurn-1) && k==0){
			            				  plWon++;
			            			  }
			            		  }
			            	  }
			            	  if(min<sg.sum(myTurn-1) && k==0){
			            		  threads[myTurn-1].total=threads[myTurn-1].total+(plWon*50);
			            		  k++;
			            	  }
			            	  max=maxClientsCount;
			            	  for (int j = 0; j < maxClientsCount; j++) {
		            			  if(threads[j].total>100){
		            					  	threads[i].os.println("<"+threads[j].name+"> has been Eliminated");
		            					  	max--;
		            			  }
		            				  threads[i].os.println("<"+threads[j].name+"'s> total :"+threads[j].total);
		            	  }
			            	  threads[i].os.println("round");
			              }
			            }
					game=false;
					if(max>1)
					newGame(max);
	  		}
		     }
		   }
		   }catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  
	  private void newGame(int m) throws IOException {
			int max=m;
			ClientThread[] newThreads=new ClientThread[max];
			SumGame newSg=new SumGame(max);
			Turn newTurn=new Turn(max);
			int count=0,i=0;
			while(count<maxClientsCount){
				if(threads[count].total<=100){
					newThreads[i]=new ClientThread(threads[count].clientSocket,newThreads,newTurn,i+1,newSg,threads[count].total,threads[count].name,threads[count].is,threads[count].os);
					i++;
				}
				else{
					threads[count].os.println("Bye");
				}
				count++;
			}
			 for (int j = 0; j < max; j++) 
		            newThreads[j].start();
		}

}
