package misc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class No_DeadLock{
	
	//This class avoids will avoid circular wait.
	
	/*
	 * List of locks that will be used by each thread in order including this lock,to do this you can just input a normal lock
	 * and put that into the constructor the check fi there is a circular wait
	 */
	private List<List<ReentrantLock>> locks;
	
	private boolean can_lock = false;
	
	//indexes of threads that can possibly start a deadlock by circular wait
	private int t1 = -1;
	private int t2 = -1;
	
	//I will index each lock and place it here
	private List<List<Integer>> indexed = new ArrayList<>();
	
	No_DeadLock(List<List<ReentrantLock>> locks){
		this.locks = locks;
		
		HashMap<ReentrantLock,Integer> helper = new HashMap<>();
		
		int c = 0;
		for(int i = 0;i < locks.size();i++){
			indexed.add(new ArrayList<>());
			for(int j = 0;j < locks.get(i).size();j++){
				ReentrantLock l = locks.get(i).get(j);
				if(helper.containsKey(l)){
					indexed.get(i).add(helper.get(l));
				}
				else {
					helper.put(l, c);
					indexed.get(i).add(c);
					c++;
				}
				
			}
		}
		
		
	}
	
	
	private boolean check_tupla(int i,int j,int a,int b,List<List<Integer>> indexed){
		
		// i and j --> check [i,i+1] and [j,j+1]
		if(indexed.get(i).get(j) == indexed.get(a).get(b+1) && indexed.get(i).get(j+1) == indexed.get(a).get(b)){
			return true;
		} 
		return false;
		
	}
	
	private boolean has_circular(){
		
		//check for (m,n) and (n,m) in the indexed lists --> cycle in the graph
		
		for(int i = 0;i < locks.size();i++){
			
			for(int j = 0;j < locks.get(i).size()-1;j++) {
				
				for(int a = 0;a < locks.size();a++) {
					for(int b = 0;b < locks.get(a).size()-1;b++){
						if(a == i) {
							continue;
						}
						if(this.check_tupla(i,j,a,b,indexed)) {
							t1 = i;
							t2 = a;
							return true;
						}
					}
				}
				
			}
			
		}
		
		return false;
		
	}
	
	public ReentrantLock get_lock(boolean fairness){
		
		//System.out.println(indexed);
		
		if(this.has_circular()){
			System.out.println("Threads "+t1+" and " + t2 + " can start a deadlock");
			System.out.println("The Lock returned is null.");
			return null;
		}
		System.out.println("Lock returned.");
		return new ReentrantLock(fairness);
		
	}
	
	
	

}
