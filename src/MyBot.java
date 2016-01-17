import java.awt.EventQueue;
import java.util.Random;
public class MyBot extends Bot {
	MyBot(int no) {
		super(no);
	}
	
	String doMove() {
		String move = null;
		int r=myCards[new Random().nextInt(myCards.length)];
		int ch=new Random().nextBoolean()?faceUp:0;
		int s=sum(myCards);
		if(s<10){
			move="0";
		}
		else
			move=""+r+"-"+ch;
		return move;
	}
	
	int sum(Integer[] n){
		int s=0;
		for(int i:n){
			s+=i;
		}
		return s;
	}
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame window = new ClientFrame(new MyBot(1));
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
