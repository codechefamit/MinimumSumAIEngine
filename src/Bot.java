import java.util.ArrayList;
import java.util.List;
public abstract class Bot {
		Integer[] myCards,discarded,kept,total;
		int faceUp,players;
		
		Bot(int no){
			players=no;
		}
		
		protected final String getMove(List<String> my,List<String> others){
			List<Integer> cards=new ArrayList<Integer>();
			for(String e:my){
				if(e.startsWith("faceUp")){
					Card cd=Card.convertToCard(e.split("-")[1]);
					faceUp=cd.getValue();
				}
				else if(!e.contains("<")){
					Card cd=Card.convertToCard(e);
					cards.add(cd.getValue());
				}
			}
			myCards=cards.toArray(new Integer[cards.size()]);
			if(others.isEmpty()){
				discarded=null;
				kept=null;
			}
			else
			{
				List<String> mo=new ArrayList<String>();
				List<String>to=new ArrayList<String>();
				for(String e:others){
					if(e.contains("-")){
						mo.add(e);
					}
					if(e.contains("total")){
						to.add(e);
					}
				}
				List<Integer> rto=new ArrayList<Integer>();
				for(int i=0;i<to.size();i++){
					String tono=to.get(i).split(":")[1];
					int j=Integer.parseInt(tono);
					if(j<=100)
					rto.add(j);
				}
				total=rto.toArray(new Integer[rto.size()]);
				List<Integer> di=new ArrayList<Integer>();
				List<Integer> ke=new ArrayList<Integer>();
				for(int i=0;i<mo.size();i++){
					String move=mo.get(i).split(">")[1];
					int r=Integer.parseInt(move.split("-")[0]);
					int k=Integer.parseInt(move.split("-")[1]);
					di.add(r);
					ke.add(k);
				}
				discarded=di.toArray(new Integer[di.size()]);
				kept=ke.toArray(new Integer[di.size()]);
			}
			return doMove();	
		}
		
		abstract String doMove();	
}
