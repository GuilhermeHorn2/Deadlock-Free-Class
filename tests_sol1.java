package misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<List<ReentrantLock>> locks = new ArrayList<>();
	
		Thread_exs t1 = new Thread_exs();
		locks.add(new ArrayList<>());
		Thread_exs t2 = new Thread_exs();
		locks.add(new ArrayList<>());
		
		ReentrantLock l1 = new ReentrantLock();
		ReentrantLock l2 = new ReentrantLock();
		
		//I plan to use for t1: l1->l2,and for t2: l2->l1 
		
		locks.get(0).add(l1);
		locks.get(0).add(l2);
		locks.get(1).add(l2);
		locks.get(1).add(l1);
		
		//I want a new lock l3 that t1 will use and need that lock to complete the task:
		ReentrantLock test_l3 = new ReentrantLock();
		locks.get(0).add(test_l3);
		
		No_DeadLock l3_inst = new No_DeadLock(locks);
		test_l3 = l3_inst.get_lock(false);//true for fairness,false for not,fairness is usefull when there is a lot o threads
		
		
	
}
	
	
}	
