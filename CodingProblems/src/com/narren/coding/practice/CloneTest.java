package com.narren.coding.practice;
import java.util.Iterator;
import java.util.TreeSet;


public class CloneTest {

	TreeSet<Integer> mTreeSet = new TreeSet<Integer>();
	
	public static void main(String[] args) {
		final CloneTest ct = new CloneTest();
		ct.populateSet();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ct.processQueue();
			}
		});
		t1.start();
		ct.removeElement();
	}
	private void populateSet () {
		mTreeSet.add(1);
		mTreeSet.add(2);
		mTreeSet.add(3);
		mTreeSet.add(4);
		mTreeSet.add(5);
		mTreeSet.add(6);
		mTreeSet.add(7);
		mTreeSet.add(8);
		mTreeSet.add(9);
		mTreeSet.add(10);
	}
	private void processQueue() {
		System.out.println("ProcessQueue " );
//		try {
//			//Thread.sleep(1000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		synchronized (this) {
			System.out.println("ProcessQueue 1" );
			TreeSet<Integer> cloneSet = (TreeSet<Integer>) mTreeSet.clone();
			Iterator<Integer> iterator = cloneSet.descendingIterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				System.out.println("ProcessQueue " + element);
				try {
					Thread.sleep(1000);
					removeElement(element);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void removeElement () {
		TreeSet<Integer> cloneSet = (TreeSet<Integer>) mTreeSet.clone();
		Iterator<Integer> iterator = cloneSet.descendingIterator();
		System.out.println("cloneSet == mTreeSet=" + (cloneSet == mTreeSet));
		while (iterator.hasNext()) {
			Integer element = iterator.next();
			System.out.println("Removing element " + element);
			mTreeSet.remove(element);
		}
	}
		private void removeElement (int element) {

				System.out.println("removing "+mTreeSet.remove(234213));
	}
	
}
