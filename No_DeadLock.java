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
	
	
	

	private boolean search_cicle(List<Integer> a,List<Integer> b){
		
		
		for(int i = 0;i < a.size();i++){
			for(int j = i+1;j < b.size();j++){
				
				//search for a[i] and a[j] in b
				//i want to find [a[j],a[i]]
				
				int idx_1 = -1;
				int idx_2 = -1;
				for(int k = 0;k < b.size();k++){
					if(b.get(k) == a.get(i)){
						idx_1 = k;
					}
					if(b.get(k) == a.get(j)) {
						idx_2 = k;
					}
				}
				if(idx_1 != -1 && idx_2 != -1){
					
					if(idx_2 < idx_1) {
						//a[i] comes after a[j] in b->...,a[j],...,a[i],...
						return true;
					}
					
				}
				
			}
		}
		return false;
		
	}
	
	private boolean has_circular(){
		
		//check for (m,n) and (n,m) in the indexed lists --> cycle in the graph
		
		for(int i = 0;i < indexed.size();i++){
			
			for(int j = i+1;j < indexed.size();j++){
				if(search_cicle(indexed.get(i),indexed.get(j))){
					t1 = i;
					t2 = j;
					return true;
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
