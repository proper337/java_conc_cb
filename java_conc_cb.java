//./Chapter_9/ch9_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe06/task/OneSecondLongTask.java
package com.packtpub.java7.concurrency.chapter5.recipe06.task;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * This is the Task used in this example. It does nothing. Only
 * sleeps the thread during 1 second
 *
 */
public class OneSecondLongTask extends RecursiveAction{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Method that executes the action of the Task. It sleeps
	 * the thread during one second
	 */
	@Override
	protected void compute() {
		System.out.printf("Task: Starting.\n");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Task: Finish.\n");
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe06/task/AlwaysThrowsExceptionWorkerThread.java
package com.packtpub.java7.concurrency.chapter5.recipe06.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * This class implements a worker thread. This is a thread that
 * is going to execute ForkJoinTask objects in a ForkJoinPool.
 * 
 * Extends the basic class ForkJoinWorkerThread
 */
public class AlwaysThrowsExceptionWorkerThread extends ForkJoinWorkerThread {

	/**
	 * Constructor of the class. Call the constructor of the 
	 * parent class
	 * @param pool ForkJoinPool where the thread is going to execute
	 */
	protected AlwaysThrowsExceptionWorkerThread(ForkJoinPool pool) {
		super(pool);
	}

	/**
	 * Method that is going to execute where the Worker Thread
	 * begins its execution
	 */
	@Override
	protected void onStart() {
		super.onStart();
		throw new RuntimeException("Exception from worker thread");
	}
}

//=*=*=*=*
//./Chapter_9/ch9_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe06/task/AlwaysThrowsExceptionWorkerThreadFactory.java
package com.packtpub.java7.concurrency.chapter5.recipe06.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

/**
 * The ForkJoinPool uses a Factory to create its Working Threads.
 * As we want to use our Working Threads, we need to use our own
 * Factory to create those threads.
 * 
 * It implements the interface that every factory has to implement. The
 * ForkJoinWorkerThreadFactory
 */
public class AlwaysThrowsExceptionWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

	/**
	 * This method creates a new Worker Thread.
	 * @param pool The ForkJoinPool where the thread that is creater
	 * is going to execute
	 */
	@Override
	public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
		return new AlwaysThrowsExceptionWorkerThread(pool);
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe06/task/Handler.java
package com.packtpub.java7.concurrency.chapter5.recipe06.task;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * This class manages the exceptions thrown by the worker threads.
 * 
 * Implements the interface every class of this kind has to implement
 *
 */
public class Handler implements UncaughtExceptionHandler {

	/**
	 * This method process the uncaught exceptions thrown in a 
	 * worker thread. 
	 * @param t The thread that throws the exception
	 * @param e The exception it was thrown
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.printf("Handler: Thread %s has thrown an Exception.\n",t.getName());
		System.out.printf("%s\n",e);
		System.exit(-1);
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe06/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe06.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe06.task.Handler;
import com.packtpub.java7.concurrency.chapter5.recipe06.task.AlwaysThrowsExceptionWorkerThreadFactory;
import com.packtpub.java7.concurrency.chapter5.recipe06.task.OneSecondLongTask;

/**
 * Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Creates a task
		OneSecondLongTask task=new OneSecondLongTask();
		
		// Creates a new Handler
		Handler handler = new Handler();
		// Creates a Factory
		AlwaysThrowsExceptionWorkerThreadFactory factory=new AlwaysThrowsExceptionWorkerThreadFactory();
		// Creates a new ForkJoinPool
		ForkJoinPool pool=new ForkJoinPool(2,factory,handler,false);
		
		// Execute the task in the pool
		pool.execute(task);
	
		// Shutdown the pool
		pool.shutdown();
		
		// Wait for the finalization of the tasks
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: The program has finished.\n");
		
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe01/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Sensor2.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a sensor in the building
 */
public class Sensor2 implements Runnable {

	/**
	 * Object with the statistics of the building
	 */
	private BuildStats stats;
	
	/**
	 * Constructor of the class
	 * @param stats object with the statistics of the building
	 */
	public Sensor2(BuildStats stats){
		this.stats=stats;
	}
	
	/**
	 * Core method of the Runnable. Simulates inputs and outputs in the building
	 */
	@Override
	public void run() {
		stats.comeIn();
		stats.comeIn();
		stats.goOut();
		stats.goOut();
		stats.goOut();
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe01/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Sensor1.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a sensor in the building
 */
public class Sensor1 implements Runnable {

	/**
	 * Object with the statistics of the building
	 */
	private BuildStats stats;
	
	/**
	 * Constructor of the class
	 * @param stats object with the statistics of the building
	 */
	public Sensor1(BuildStats stats){
		this.stats=stats;
	}
	
	/**
	 * Core method of the Runnable. Simulates inputs and outputs in the building
	 */
	@Override
	public void run() {
		stats.comeIn();
		stats.comeIn();
		stats.comeIn();
		stats.goOut();
		stats.comeIn();
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe01/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/BuildStats.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

import java.util.concurrent.TimeUnit;

/**
 * 
 * This class simulates a control class that stores the statistics of
 * access to a building, controlling the number of people inside the building
 *
 */
public class BuildStats {

	/**
	 * Number of people inside the building
	 */
	private long numPeople;
	
	/**
	 * Method that simulates when people come in into the building
	 */
	public /*synchronized*/ void comeIn(){
		System.out.printf("%s: A person enters.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople++;
		}
		generateCard();
	}
	
	/**
	 * Method that simulates when people leave the building
	 */
	public /*synchronized*/ void goOut(){
		System.out.printf("%s: A person leaves.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople--;
		}
		generateReport();
	}
	
	/**
	 * Method that simulates the generation of a card when people come in into the building
	 */
	private void generateCard(){
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method that simulates the generation of a report when people leaves the building
	 */
	private void generateReport(){
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that print the number of people inside the building
	 */
	public void printStats(){
		System.out.printf("%d persons in the building.\n",numPeople);
	}
	
}

//=*=*=*=*
//./Chapter_9/ch9_recipe01/src/com/packtpub/java7/concurrency/chapter2/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import java.util.Date;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.BuildStats;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor1;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor2;

/**
 * Main class of the example. Creates an object with the statistics of the
 * building and executes two threads that simulates two sensors in the building
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a new object for the statistics
		BuildStats stats=new BuildStats();

		// Create a Sensor1 object and a Thread to run it
		Sensor1 sensor1=new Sensor1(stats);
		Thread thread1=new Thread(sensor1,"Sensor 1");

		// Create a Sensor 2 object and a Thread to run it
		Sensor2 sensor2=new Sensor2(stats);
		Thread thread2=new Thread(sensor2,"Sensor 2");
		
		// Get the actual time
		Date date1=new Date();
		
		//Starts the threads
		thread1.start();
		thread2.start();
		
		try {
			// Wait for the finalization of the threads
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Get the actual time and print the execution time
		Date date2=new Date();
		stats.printStats();
		System.out.println("Execution Time: "+((date2.getTime()-date1.getTime())/1000));

	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe01/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe01.task;

import java.util.concurrent.TimeUnit;

/**
 * Task used to get information about a Thread. Makes a for loop with 100 steps. In
 * each step, sleeps for 100 milliseconds. Total execution time: 10 seconds
 *
 */
public class Task implements Runnable {

	/**
	 * Main method of the task
	 */
	@Override
	public void run() {
		for (int i=0; i<100; i++) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("%s: %d\n",Thread.currentThread().getName(),i);		
		}
	}
}

//=*=*=*=*
//./Chapter_9/ch9_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe01/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe01.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe01.task.Task;

/**
 * Main class of the example. Creates five threads and writes information about them
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Create a task object
		 */
		Task task = new Task();
		
		/*
		 * Create an array to store the threads
		 */
		Thread threads[] = new Thread[5];

		/*
		 * Create and start the five threads
		 */
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(task);
			threads[i].setPriority(i + 1);
			threads[i].start();
		}

		/*
		 * Write threads information
		 */
		for (int j = 0; j < 10; j++) {
			System.out.printf("Main: Logging threads\n");
			for (int i = 0; i < threads.length; i++) {
				System.out.printf("**********************\n");
				System.out.printf("Main: %d: Id: %d Name: %s: Priority: %d\n",i,threads[i].getId(),threads[i].getName(),threads[i].getPriority());
				System.out.printf("Main: Status: %s\n",threads[i].getState());
				System.out.printf("Main: Thread Group: %s\n",threads[i].getThreadGroup());
				System.out.printf("Main: Stack Trace: \n");
				for (int t=0; t<threads[i].getStackTrace().length; t++) {
					System.out.printf("Main: %s\n",threads[i].getStackTrace()[t]);
				}
				System.out.printf("**********************\n");
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
}

//=*=*=*=*
//./Chapter_9/ch9_recipe06/src/com/packtpub/java7/concurrency/chapter8/recipe03/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe03.task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Task used to write information about a Semaphore
 *
 */
public class Task implements Runnable {

	/**
	 * Semaphore shared by all the tasks
	 */
	private Semaphore semaphore;
	
	/**
	 * Constructor of the class. Initializes its attribute
	 * @param semaphore Semaphore by all the tasks
	 */
	public Task(Semaphore semaphore){
		this.semaphore=semaphore;
	}
	
	/**
	 * Main method of the task. Acquire the semaphore, sleep the thread for
	 * two seconds and release the semaphore
	 */
	@Override
	public void run() {
		try {
			/*
			 * Acquire the semaphore and write a message in the console
			 */
			semaphore.acquire();
			System.out.printf("%s: Get the semaphore.\n",Thread.currentThread().getName());
			/*
			 * Sleep the thread
			 */
			TimeUnit.SECONDS.sleep(2);
			System.out.println(Thread.currentThread().getName()+": Release the semaphore.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			/*
			 * Release the semaphore and write a message
			 */
			semaphore.release();			
		}
	}
}

//=*=*=*=*
//./Chapter_9/ch9_recipe06/src/com/packtpub/java7/concurrency/chapter8/recipe03/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe03.core;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe03.task.Task;

/**
 * Main class of the example. Create ten threads to execute ten
 * task objects and write information about the semaphore
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
		/*
		 * Create a semaphore
		 */
		Semaphore semaphore=new Semaphore(3);
		
		/*
		 * Create an array for ten threads 
		 */
		Thread threads[]=new Thread[10];
		
		/*
		 * Create and launch a thread every 200 milliseconds. After each creation,
		 * show information about the semaphore
		 */
		for (int i=0; i<threads.length; i++) {
			Task task=new Task(semaphore);
			threads[i]=new Thread(task);
			threads[i].start();
			
			TimeUnit.MILLISECONDS.sleep(200);
			
			showLog(semaphore);
		}
		
		/*
		 * Write information about the semaphore five times
		 */
		for (int i=0; i<5; i++) {
			showLog(semaphore);
			TimeUnit.SECONDS.sleep(1);
		}
	}

	/**
	 * Method that writes information about a semaphore
	 * @param semaphore Semaphore to write its log information
	 */
	private static void showLog(Semaphore semaphore) {
		System.out.printf("********************\n");
		System.out.printf("Main: Semaphore Log\n");
		System.out.printf("Main: Semaphore: Avalaible Permits: %d\n",semaphore.availablePermits());
		System.out.printf("Main: Semaphore: Queued Threads: %s\n",semaphore.hasQueuedThreads());
		System.out.printf("Main: Semaphore: Queue Length: %d\n",semaphore.getQueueLength());
		System.out.printf("Main: Semaphore: Fairness: %s\n",semaphore.isFair());
		System.out.printf("********************\n");
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe03/task/Consumer.java
package com.packtpub.java7.concurrency.chapter6.recipe03.task;

import java.util.concurrent.LinkedTransferQueue;

/**
 * This class implements a Consumer of Strings. It takes
 * 10000 Strings from the buffer
 *
 */
public class Consumer implements Runnable {

	/**
	 * Buffer to take the Strings
	 */
	private LinkedTransferQueue<String> buffer;
	
	/**
	 * Name of the Consumer
	 */
	private String name;
	
	/**
	 * Constructor of the class. It initializes all its attributes
	 * @param name Name of the consumer
	 * @param buffer Buffer to take the Strings
	 */
	public Consumer(String name, LinkedTransferQueue<String> buffer){
		this.name=name;
		this.buffer=buffer;
	}
	
	/**
	 * Main method of the consumer. It takes 10000 Strings from the 
	 * buffer
	 */
	@Override
	public void run() {
		for (int i=0; i<10000; i++){
			try {
				buffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Consumer: %s: Consumer done\n",name);
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe03/task/Producer.java
package com.packtpub.java7.concurrency.chapter6.recipe03.task;

import java.util.concurrent.LinkedTransferQueue;

/**
 * This class implements a Producer of Strings. It generates
 * 10000 strings and stores them in the buffer.
 *
 */
public class Producer implements Runnable {

	/**
	 * Buffer to store the Strings
	 */
	private LinkedTransferQueue<String> buffer;
	
	/**
	 * Name of the producer
	 */
	private String name;
	
	/**
	 * Constructor of the class. Initializes its parameters
	 * @param name Name of the producer
	 * @param buffer Buffer to store the objects
	 */
	public Producer(String name, LinkedTransferQueue<String> buffer){
		this.name=name;
		this.buffer=buffer;
	}

	/**
	 * Main method of the producer. Generates 10000 of Strings
	 * and stores them in the buffer
	 */
	@Override
	public void run() {
		for (int i=0; i<10000; i++) {
			buffer.put(name+": Element "+i);
		}
		System.out.printf("Producer: %s: Producer done\n",name);
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe03/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe03.core;

import java.util.concurrent.LinkedTransferQueue;

import com.packtpub.java7.concurrency.chapter6.recipe03.task.Consumer;
import com.packtpub.java7.concurrency.chapter6.recipe03.task.Producer;

/**
 * Main class of the example. It executes 100 producers and 100 consumers
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final int THREADS=100;
		/*
		 * Create a Linked TransferQueue of Strings to store the objects
		 * generated by the producers and consumed by the consumer
		 */
		LinkedTransferQueue<String> buffer=new LinkedTransferQueue<>();
		/*
		 * An array to store the Thread objects that execute the producers
		 */
		Thread producerThreads[]=new Thread[THREADS];
		
		/*
		 * An array to store the Thread objects that execute the consumers
		 */
		Thread consumerThreads[]=new Thread[THREADS];
		
		/*
		 * Launch 100 Consumer tasks
		 */
		for (int i=0; i<THREADS; i++){
			Consumer consumer=new Consumer("Consumer "+i,buffer);
			consumerThreads[i]=new Thread(consumer);
			consumerThreads[i].start();
		}
		
		/*
		 * Launch 100 Producer tasks 
		 */
		for (int i=0; i<THREADS; i++) {
			Producer producer=new Producer("Producer: "+i,buffer);
			producerThreads[i]=new Thread(producer);
			producerThreads[i].start();
		}
		
		/*
		 * Wait for the finalization of the threads
		 */
		for (int i=0; i<THREADS; i++){
			try {
				producerThreads[i].join();
				consumerThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Write the size of the buffer in the Console
		 */
		System.out.printf("Main: Size of the buffer: %d\n",buffer.size());
		System.out.printf("Main: End of the example\n");
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe4/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe4.task;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * This class encapsulates a FileSearch object. The objective
 * is execute that task and returns the result that it generates
 * as it was a Callable object 
 *
 */
public class Task extends FutureTask<List<String>> {

	private FileSearch fileSearch;
	
	/**
	 * Constructor of the class
	 * @param runnable FileSearh object that is going to execute
	 * @param result Object to return as result. We are going to ignore this structure
	 */
	public Task(Runnable runnable, List<String> result) {
		super(runnable, result);
		this.fileSearch=(FileSearch)runnable;
	}

	/**
	 * Override the set method. As we are going to execute a Runnable object, this
	 * method establish the null value as result. We change this behavior returning
	 * the result list generated by the FileSearch task
	 */
	protected void set(List<String> v) {
		if (v==null) {
			v=fileSearch.getResults();
		}
		super.set(v);
	}
}

//=*=*=*=*
//./Chapter_9/ch9_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe4/task/FileSearch.java
package com.packtpub.java7.concurrency.chapter4.recipe4.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class search for files with an extension in a directory
 */
public class FileSearch implements Runnable {

	/**
	 * Initial path for the search
	 */
	private String initPath;
	
	/**
	 * Extension of the file we are searching for
	 */
	private String end;
	
	/**
	 * List that stores the full path of the files that have the extension we are searching for
	 */
	private List<String> results;
	

	/**
	 * Constructor of the class. Initializes its attributes
	 * @param initPath Initial path for the search
	 * @param end Extension of the files we are searching for
	 * @param phaser Phaser object to control the execution
	 */
	public FileSearch(String initPath, String end) {
		this.initPath = initPath;
		this.end = end;
		results=new ArrayList<>();
	}

	/**
	 * Method that returns the list or results
	 * @return
	 */
	public List<String> getResults() {
		return results;
	}

	/**
	 * Main method of the class. See the comments inside to a better description of it
	 */
	@Override
	public void run() {
		
		System.out.printf("%s: Starting\n",Thread.currentThread().getName());
		
		// 1st Phase: Look for the files
		File file = new File(initPath);
		if (file.isDirectory()) {
			directoryProcess(file);
		}
	}

	/**
	 * Method that process a directory
	 * 
	 * @param file
	 *            : Directory to process
	 */
	private void directoryProcess(File file) {

		// Get the content of the directory
		File list[] = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					// If is a directory, process it
					directoryProcess(list[i]);
				} else {
					// If is a file, process it
					fileProcess(list[i]);
				}
			}
		}
	}

	/**
	 * Method that process a File
	 * 
	 * @param file
	 *            : File to process
	 */
	private void fileProcess(File file) {
		if (file.getName().endsWith(end)) {
			results.add(file.getAbsolutePath());
		}
	}

}

//=*=*=*=*
//./Chapter_9/ch9_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe4/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe4.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe4.task.FileSearch;
import com.packtpub.java7.concurrency.chapter4.recipe4.task.Task;

/**
 * Main class of the example. Create three FileSearch objects, encapsulate inside
 * three Task objects and execute them as they were callable objects
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new Executor
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();

		// Create three FileSearch objects
		FileSearch system=new FileSearch("C:\\Windows", "log");
		FileSearch apps=new FileSearch("C:\\Program Files","log");
		FileSearch documents=new FileSearch("C:\\Documents And Settings","log");
		
		// Create three Task objects
		Task systemTask=new Task(system,null);
		Task appsTask=new Task(apps,null);
		Task documentsTask=new Task(documents,null);
		
		// Submit the Tasks to the Executor
		executor.submit(systemTask);
		executor.submit(appsTask);
		executor.submit(documentsTask);
		
		// Shutdown the executor and wait for the end of the tasks
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Write to the console the number of results
		try {
			System.out.printf("Main: System Task: Number of Results: %d\n",systemTask.get().size());
			System.out.printf("Main: App Task: Number of Results: %d\n",appsTask.get().size());
			System.out.printf("Main: Documents Task: Number of Results: %d\n",documentsTask.get().size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe2/src/com/packtpub/java7/concurrency/chapter3/recipe2/task/Job.java
package com.packtpub.java7.concurrency.chapter3.recipe2.task;

/**
 * This class simulates a job that send a document to print.
 *
 */
public class Job implements Runnable {

	/**
	 * Queue to print the documents
	 */
	private PrintQueue printQueue;
	
	/**
	 * Constructor of the class. Initializes the queue
	 * @param printQueue
	 */
	public Job(PrintQueue printQueue){
		this.printQueue=printQueue;
	}
	
	/**
	 * Core method of the Job. Sends the document to the print queue and waits
	 *  for its finalization
	 */
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job\n",Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());		
	}
}

//=*=*=*=*
//./Chapter_3/ch3_recipe2/src/com/packtpub/java7/concurrency/chapter3/recipe2/task/PrintQueue.java
package com.packtpub.java7.concurrency.chapter3.recipe2.task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements a PrintQueue that have access to three printers.
 * 
 * We use a Semaphore to control the access to one of the printers. When
 * a job wants to print, if there is one or more printers free, it has access
 * to one of the free printers. If not, it sleeps until one of the printers
 * is free.
 *
 */
public class PrintQueue {
	
	/**
	 * Semaphore to control the access to the printers
	 */
	private Semaphore semaphore;
	
	/**
	 * Array to control what printer is free
	 */
	private boolean freePrinters[];
	
	/**
	 * Lock to control the access to the freePrinters array
	 */
	private Lock lockPrinters;
	
	/**
	 * Constructor of the class. It initializes the three objects
	 */
	public PrintQueue(){
		semaphore=new Semaphore(3);
		freePrinters=new boolean[3];
		for (int i=0; i<3; i++){
			freePrinters[i]=true;
		}
		lockPrinters=new ReentrantLock();
	}
	
	public void printJob (Object document){
		try {
			// Get access to the semaphore. If there is one or more printers free,
			// it will get the access to one of the printers
			semaphore.acquire();
			
			// Get the number of the free printer
			int assignedPrinter=getPrinter();
			
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job in Printer %d during %d seconds\n",Thread.currentThread().getName(),assignedPrinter,duration);
			TimeUnit.SECONDS.sleep(duration);
			
			// Free the printer
			freePrinters[assignedPrinter]=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Free the semaphore
			semaphore.release();			
		}
	}

	private int getPrinter() {
		int ret=-1;
		
		try {
			// Get the access to the array
			lockPrinters.lock();
			// Look for the first free printer
			for (int i=0; i<freePrinters.length; i++) {
				if (freePrinters[i]){
					ret=i;
					freePrinters[i]=false;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Free the access to the array
			lockPrinters.unlock();
		}
		return ret;
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe2/src/com/packtpub/java7/concurrency/chapter3/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe2.core;

import com.packtpub.java7.concurrency.chapter3.recipe2.task.Job;
import com.packtpub.java7.concurrency.chapter3.recipe2.task.PrintQueue;

/**
 * Main class of the example.
 *
 */
public class Main {

	/**
	 * Main method of the class. Run ten jobs in parallel that
	 * send documents to the print queue at the same time.
	 */
	public static void main (String args[]){
		
		// Creates the print queue
		PrintQueue printQueue=new PrintQueue();
		
		// Creates ten Threads
		Thread thread[]=new Thread[12];
		for (int i=0; i<12; i++){
			thread[i]=new Thread(new Job(printQueue),"Thread "+i);
		}
		
		// Starts the Threads
		for (int i=0; i<12; i++){
			thread[i].start();
		}
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe3/src/com/packtpub/java7/concurrency/chapter3/recipe3/task/Videoconference.java
package com.packtpub.java7.concurrency.chapter3.recipe3.task;

import java.util.concurrent.CountDownLatch;

/**
 * This class implements the controller of the Videoconference
 * 
 * It uses a CountDownLatch to control the arrival of all the 
 * participants in the conference.
 *
 */
public class Videoconference implements Runnable{

	/**
	 * This class uses a CountDownLatch to control the arrivel of all
	 * the participants
	 */
	private final CountDownLatch controller;
	
	/**
	 * Constructor of the class. Initializes the CountDownLatch
	 * @param number The number of participants in the videoconference
	 */
	public Videoconference(int number) {
		controller=new CountDownLatch(number);
	}

	/**
	 * This method is called by every participant when he incorporates to the VideoConference
	 * @param participant
	 */
	public void arrive(String name){
		System.out.printf("%s has arrived.\n",name);
		// This method uses the countDown method to decrement the internal counter of the
		// CountDownLatch
		controller.countDown();
		System.out.printf("VideoConference: Waiting for %d participants.\n",controller.getCount());
	}
	
	/**
	 * This is the main method of the Controller of the VideoConference. It waits for all
	 * the participants and the, starts the conference
	 */
	@Override
	public void run() {
		System.out.printf("VideoConference: Initialization: %d participants.\n",controller.getCount());
		try {
			// Wait for all the participants
			controller.await();
			// Starts the conference
			System.out.printf("VideoConference: All the participants have come\n");
			System.out.printf("VideoConference: Let's start...\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}

//=*=*=*=*
//./Chapter_3/ch3_recipe3/src/com/packtpub/java7/concurrency/chapter3/recipe3/task/Participant.java
package com.packtpub.java7.concurrency.chapter3.recipe3.task;

import java.util.concurrent.TimeUnit;

/**
 * This class implements a participant in the VideoConference
 *
 */
public class Participant implements Runnable {

	/**
	 * VideoConference in which this participant will take part off
	 */
	private Videoconference conference;
	
	/**
	 * Name of the participant. For log purposes only
	 */
	private String name;
	
	/**
	 * Constructor of the class. Initialize its attributes
	 * @param conference VideoConference in which is going to take part off
	 * @param name Name of the participant
	 */
	public Participant(Videoconference conference, String name) {
		this.conference=conference;
		this.name=name;
	}

	/**
	 * Core method of the participant. Waits a random time and joins the VideoConference 
	 */
	@Override
	public void run() {
		Long duration=(long)(Math.random()*10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		conference.arrive(name);

	}
}

//=*=*=*=*
//./Chapter_3/ch3_recipe3/src/com/packtpub/java7/concurrency/chapter3/recipe3/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe3.core;

import com.packtpub.java7.concurrency.chapter3.recipe3.task.Participant;
import com.packtpub.java7.concurrency.chapter3.recipe3.task.Videoconference;

/**
 * Main class of the example. Create, initialize and execute all the objects
 * necessaries for the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Creates a VideoConference with 10 participants.
		Videoconference conference=new Videoconference(10);
		// Creates a thread to run the VideoConference and start it.
		Thread threadConference=new Thread(conference);
		threadConference.start();
		
		// Creates ten participants, a thread for each one and starts them
		for (int i=0; i<10; i++){
			Participant p=new Participant(conference, "Participant "+i);
			Thread t=new Thread(p);
			t.start();
		}

	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe4/src/com/packtpub/java7/concurrency/chapter3/recipe4/task/Searcher.java
package com.packtpub.java7.concurrency.chapter3.recipe4.task;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.packtpub.java7.concurrency.chapter3.recipe4.utils.MatrixMock;
import com.packtpub.java7.concurrency.chapter3.recipe4.utils.Results;

/**
 * Class that search for a number in a set of rows of the bi-dimensional array
 *
 */
public class Searcher implements Runnable {

	/**
	 * First row where look for
	 */
	private int firstRow;
	
	/**
	 * Last row where look for
	 */
	private int lastRow;
	
	/**
	 * Bi-dimensional array with the numbers
	 */
	private MatrixMock mock;
	
	/**
	 * Array to store the results
	 */
	private Results results;
	
	/**
	 * Number to look for
	 */
	private int number;
	
	/**
	 * CyclicBarrier to control the execution
	 */
	private final CyclicBarrier barrier;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param firstRow First row where look for
	 * @param lastRow Last row where fook for
	 * @param mock Object with the array of numbers
	 * @param results Array to store the results
	 * @param number Number to look for
	 * @param barrier CyclicBarrier to control the execution
	 */
	public Searcher(int firstRow, int lastRow, MatrixMock mock, Results results, int number, CyclicBarrier barrier){
		this.firstRow=firstRow;
		this.lastRow=lastRow;
		this.mock=mock;
		this.results=results;
		this.number=number;
		this.barrier=barrier;
	}

	/**
	 * Main method of the searcher. Look for the number in a subset of rows. For each row, saves the
	 * number of occurrences of the number in the array of results
	 */
	@Override
	public void run() {
		int counter;
		System.out.printf("%s: Processing lines from %d to %d.\n",Thread.currentThread().getName(),firstRow,lastRow);
		for (int i=firstRow; i<lastRow; i++){
			int row[]=mock.getRow(i);
			counter=0;
			for (int j=0; j<row.length; j++){
				if (row[j]==number){
					counter++;
				}
			}
			results.setData(i, counter);
		}
		System.out.printf("%s: Lines processed.\n",Thread.currentThread().getName());		
		try {
			barrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe4/src/com/packtpub/java7/concurrency/chapter3/recipe4/task/Grouper.java
package com.packtpub.java7.concurrency.chapter3.recipe4.task;

import com.packtpub.java7.concurrency.chapter3.recipe4.utils.Results;


/**
 * Group the results of each Searcher. Sum the values stored in the Results object 
 * An object of this class is executed automatically by the CyclicBarrier when
 * all the Searchers finish its job
 */
public class Grouper implements Runnable {

	/**
	 * Results object with the occurrences of the number in each row
	 */
	private Results results;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param results Results object with the ocurrences of the number in each row
	 */
	public Grouper(Results results){
		this.results=results;
	}
	
	/**
	 * Main method of the Grouper. Sum the values stored in the Results object 
	 */
	@Override
	public void run() {
		int finalResult=0;
		System.out.printf("Grouper: Processing results...\n");
		int data[]=results.getData();
		for (int number:data){
			finalResult+=number;
		}
		System.out.printf("Grouper: Total result: %d.\n",finalResult);
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe4/src/com/packtpub/java7/concurrency/chapter3/recipe4/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe4.core;

import java.util.concurrent.CyclicBarrier;

import com.packtpub.java7.concurrency.chapter3.recipe4.task.Grouper;
import com.packtpub.java7.concurrency.chapter3.recipe4.task.Searcher;
import com.packtpub.java7.concurrency.chapter3.recipe4.utils.MatrixMock;
import com.packtpub.java7.concurrency.chapter3.recipe4.utils.Results;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		 * Initializes the bi-dimensional array of data
		 * 		10000 rows
		 * 		1000 numbers in each row
		 * 		Looking for number 5
		 */
		final int ROWS=10000;
		final int NUMBERS=1000;
		final int SEARCH=5; 
		final int PARTICIPANTS=5;
		final int LINES_PARTICIPANT=2000;
		MatrixMock mock=new MatrixMock(ROWS, NUMBERS,SEARCH);
		
		// Initializes the object for the results
		Results results=new Results(ROWS);
		
		// Creates an Grouper object
		Grouper grouper=new Grouper(results);
		
		// Creates the CyclicBarrier object. It has 5 participants and, when
		// they finish, the CyclicBarrier will execute the grouper object
		CyclicBarrier barrier=new CyclicBarrier(PARTICIPANTS,grouper);
		
		// Creates, initializes and starts 5 Searcher objects
		Searcher searchers[]=new Searcher[PARTICIPANTS];
		for (int i=0; i<PARTICIPANTS; i++){
			searchers[i]=new Searcher(i*LINES_PARTICIPANT, (i*LINES_PARTICIPANT)+LINES_PARTICIPANT, mock, results, 5,barrier);
			Thread thread=new Thread(searchers[i]);
			thread.start();
		}
		System.out.printf("Main: The main thread has finished.\n");
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe4/src/com/packtpub/java7/concurrency/chapter3/recipe4/utils/Results.java
package com.packtpub.java7.concurrency.chapter3.recipe4.utils;

/**
 * This class is used to store the number of occurrences of the number
 * we are looking for in each row of the bi-dimensional array
 *
 */
public class Results {
	
	/**
	 * Array to store the number of occurrences of the number in each row of the array
	 */
	private int data[];

	/**
	 * Constructor of the class. Initializes its attributes
	 * @param size Size of the array to store the results
	 */
	public Results(int size){
		data=new int[size];
	}

	/**
	 * Sets the value of one position in the array of results
	 * @param position Position in the array
	 * @param value Value to set in that position
	 */
	public void  setData(int position, int value){
		data[position]=value;
	}
	
	/**
	 * Returns the array of results
	 * @return the array of results
	 */
	public int[] getData(){
		return data;
	}
}

//=*=*=*=*
//./Chapter_3/ch3_recipe4/src/com/packtpub/java7/concurrency/chapter3/recipe4/utils/MatrixMock.java
package com.packtpub.java7.concurrency.chapter3.recipe4.utils;

import java.util.Random;

/**
 * This class generates a random matrix of integer numbers between 1 and 10
 *
 */
public class MatrixMock {
	
	/**
	 * Bi-dimensional array with the random numbers
	 */
	private int data[][];
	
	/**
	 * Constructor of the class. Generates the bi-dimensional array of numbers.
	 * While generates the array, it counts the times that appears the number we are going
	 * to look for so we can check that the CiclycBarrier class does a good job
	 * @param size Number of rows of the array
	 * @param length Number of columns of the array
	 * @param number Number we are going to look for
	 */
	public MatrixMock(int size, int length, int number){

		int counter=0;
		data=new int[size][length];
		Random random=new Random();
		for (int i=0; i<size; i++) {
			for (int j=0; j<length; j++){
				data[i][j]=random.nextInt(10);
				if (data[i][j]==number){
					counter++;
				}
			}
		}
		System.out.printf("Mock: There are %d ocurrences of number in generated data.\n",counter,number);
	}
	
	/**
	 * This methods returns a row of the bi-dimensional array
	 * @param row the number of the row to return
	 * @return the selected row
	 */
	public int[] getRow(int row){
		if ((row>=0)&&(row<data.length)){
			return data[row];
		}
		return null;
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe1/src/com/packtpub/java7/concurrency/chapter3/recipe1/task/Job.java
package com.packtpub.java7.concurrency.chapter3.recipe1.task;

/**
 * This class simulates a job that send a document to print.
 *
 */
public class Job implements Runnable {

	/**
	 * Queue to print the documents
	 */
	private PrintQueue printQueue;
	
	/**
	 * Constructor of the class. Initializes the queue
	 * @param printQueue
	 */
	public Job(PrintQueue printQueue){
		this.printQueue=printQueue;
	}
	
	/**
	 * Core method of the Job. Sends the document to the print queue and waits
	 *  for its finalization
	 */
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job\n",Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());		
	}
}

//=*=*=*=*
//./Chapter_3/ch3_recipe1/src/com/packtpub/java7/concurrency/chapter3/recipe1/task/PrintQueue.java
package com.packtpub.java7.concurrency.chapter3.recipe1.task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * This class implements the PrintQueue using a Semaphore to control the
 * access to it. 
 *
 */
public class PrintQueue {
	
	/**
	 * Semaphore to control the access to the queue
	 */
	private final Semaphore semaphore;
	
	/**
	 * Constructor of the class. Initializes the semaphore
	 */
	public PrintQueue(){
		semaphore=new Semaphore(1);
	}
	
	/**
	 * Method that simulates printing a document
	 * @param document Document to print
	 */
	public void printJob (Object document){
		try {
			// Get the access to the semaphore. If other job is printing, this
			// thread sleep until get the access to the semaphore
			semaphore.acquire();
			
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),duration);
			Thread.sleep(duration);			
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Free the semaphore. If there are other threads waiting for this semaphore,
			// the JVM selects one of this threads and give it the access.
			semaphore.release();			
		}
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe1/src/com/packtpub/java7/concurrency/chapter3/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe1.core;

import com.packtpub.java7.concurrency.chapter3.recipe1.task.Job;
import com.packtpub.java7.concurrency.chapter3.recipe1.task.PrintQueue;

/**
 * Main class of the example.
 *
 */
public class Main {

	/**
	 * Main method of the class. Run ten jobs in parallel that
	 * send documents to the print queue at the same time.
	 */
	public static void main (String args[]){
		
		// Creates the print queue
		PrintQueue printQueue=new PrintQueue();
		
		// Creates ten Threads
		Thread thread[]=new Thread[10];
		for (int i=0; i<10; i++){
			thread[i]=new Thread(new Job(printQueue),"Thread "+i);
		}
		
		// Starts the Threads
		for (int i=0; i<10; i++){
			thread[i].start();
		}
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe7/src/com/packtpub/java7/concurrency/chapter3/recipe7/task/Consumer.java
package com.packtpub.java7.concurrency.chapter3.recipe7.task;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * This class implements the consumer of the example
 *
 */
public class Consumer implements Runnable {

	/**
	 * Buffer to save the events produced
	 */
	private List<String> buffer;
	
	/**
	 * Exchager to synchronize with the consumer
	 */
	private final Exchanger<List<String>> exchanger;

	/**
	 * Constructor of the class. Initializes its attributes
	 * @param buffer Buffer to save the events produced
	 * @param exchanger Exchanger to syncrhonize with the consumer
	 */
	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger){
		this.buffer=buffer;
		this.exchanger=exchanger;
	}
	
	/**
	 * Main method of the producer. It consumes all the events produced by the Producer. After
	 * processes ten events, it uses the exchanger object to synchronize with 
	 * the producer. It sends to the producer an empty buffer and receives a buffer with ten events
	 */
	@Override
	public void run() {
		int cycle=1;
		
		for (int i=0; i<10; i++){
			System.out.printf("Consumer: Cycle %d\n",cycle);

			try {
				// Wait for the produced data and send the empty buffer to the producer
				buffer=exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.printf("Consumer: %d\n",buffer.size());
			
			for (int j=0; j<10; j++){
				String message=buffer.get(0);
				System.out.printf("Consumer: %s\n",message);
				buffer.remove(0);
			}
			
			cycle++;
		}
		
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe7/src/com/packtpub/java7/concurrency/chapter3/recipe7/task/Producer.java
package com.packtpub.java7.concurrency.chapter3.recipe7.task;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * This class implements the producer
 *
 */
public class Producer implements Runnable {

	/**
	 * Buffer to save the events produced
	 */
	private List<String> buffer;
	
	/**
	 * Exchager to synchronize with the consumer
	 */
	private final Exchanger<List<String>> exchanger;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param buffer Buffer to save the events produced
	 * @param exchanger Exchanger to syncrhonize with the consumer
	 */
	public Producer (List<String> buffer, Exchanger<List<String>> exchanger){
		this.buffer=buffer;
		this.exchanger=exchanger;
	}
	
	/**
	 * Main method of the producer. It produces 100 events. 10 cicles of 10 events.
	 * After produce 10 events, it uses the exchanger object to synchronize with 
	 * the consumer. The producer sends to the consumer the buffer with ten events and
	 * receives from the consumer an empty buffer
	 */
	@Override
	public void run() {
		int cycle=1;
		
		for (int i=0; i<10; i++){
			System.out.printf("Producer: Cycle %d\n",cycle);
			
			for (int j=0; j<10; j++){
				String message="Event "+((i*10)+j);
				System.out.printf("Producer: %s\n",message);
				buffer.add(message);
			}
			
			try {
				/*
				 * Change the data buffer with the consumer
				 */
				buffer=exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.printf("Producer: %d\n",buffer.size());
			
			cycle++;
		}
		
	}
	
	

}

//=*=*=*=*
//./Chapter_3/ch3_recipe7/src/com/packtpub/java7/concurrency/chapter3/recipe7/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe7.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import com.packtpub.java7.concurrency.chapter3.recipe7.task.Consumer;
import com.packtpub.java7.concurrency.chapter3.recipe7.task.Producer;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Creates two buffers
		List<String> buffer1=new ArrayList<>();
		List<String> buffer2=new ArrayList<>();
		
		// Creates the exchanger
		Exchanger<List<String>> exchanger=new Exchanger<>();
		
		// Creates the producer
		Producer producer=new Producer(buffer1, exchanger);
		// Creates the consumer
		Consumer consumer=new Consumer(buffer2, exchanger);
		
		// Creates and starts the threads
		Thread threadProducer=new Thread(producer);
		Thread threadConsumer=new Thread(consumer);
		
		threadProducer.start();
		threadConsumer.start();

	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe6/src/com/packtpub/java7/concurrency/chapter3/recipe6/task/MyPhaser.java
package com.packtpub.java7.concurrency.chapter3.recipe6.task;

import java.util.concurrent.Phaser;

/**
 * Implements a subclass of the Phaser class. Overrides the onAdvance method to control
 * the change of phase 
 *
 */
public class MyPhaser extends Phaser {

	/**
	 * This method is called when the last register thread calls one of the advance methods
	 * in the actual phase
	 * @param phase Actual phase
	 * @param registeredParties Number of registered threads
	 * @return false to advance the phase, true to finish
	 */
	@Override
	protected boolean onAdvance(int phase, int registeredParties) {
		switch (phase) {
		case 0:
			return studentsArrived();
		case 1:
			return finishFirstExercise();
		case 2:
			return finishSecondExercise();
		case 3:
			return finishExam();
		default:
			return true;
		}
	}

	/**
	 * This method is called in the change from phase 0 to phase 1
	 * @return false to continue with the execution
	 */
	private boolean studentsArrived() {
		System.out.printf("Phaser: The exam are going to start. The students are ready.\n");
		System.out.printf("Phaser: We have %d students.\n",getRegisteredParties());
		return false;
	}

	/**
	 * This method is called in the change from phase 1 to phase 2
	 * @return false to continue with the execution
	 */
	private boolean finishFirstExercise() {
		System.out.printf("Phaser: All the students has finished the first exercise.\n");
		System.out.printf("Phaser: It's turn for the second one.\n");
		return false;
	}

	/**
	 * This method is called in the change form phase 2 to phase 3
	 * @return false to continue with the execution
	 */
	private boolean finishSecondExercise() {
		System.out.printf("Phaser: All the students has finished the second exercise.\n");
		System.out.printf("Phaser: It's turn for the third one.\n");
		return false;
	}

	/**
	 * This method is called in the change from phase 3 to phase 4
	 * @return true. There are no more phases
	 */
	private boolean finishExam() {
		System.out.printf("Phaser: All the students has finished the exam.\n");
		System.out.printf("Phaser: Thank you for your time.\n");
		return true;
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe6/src/com/packtpub/java7/concurrency/chapter3/recipe6/task/Student.java
package com.packtpub.java7.concurrency.chapter3.recipe6.task;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * This class implements an student in the exam 
 *
 */
public class Student implements Runnable {

	/**
	 * Phaser to control the execution
	 */
	private Phaser phaser;
	
	/**
	 * Constructor of the class. Initialize its objects
	 * @param phaser Phaser to control the execution
	 */
	public Student(Phaser phaser) {
		this.phaser=phaser;
	}

	/**
	 * Main method of the student. It arrives to the exam and does three exercises. After each
	 * exercise, it calls the phaser to wait that all the students finishes the same exercise
	 */
	public void run() {
		System.out.printf("%s: Has arrived to do the exam. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the first exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise1();
		System.out.printf("%s: Has done the first exercise. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the second exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise2();
		System.out.printf("%s: Has done the second exercise. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the third exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise3();
		System.out.printf("%s: Has finished the exam. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
	}

	/**
	 * Does an exercise is to wait a random time 
	 */
	private void doExercise1() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Does an exercise is wait a random time 
	 */
	private void doExercise2() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Does an exercise is wait a random time 
	 */
	private void doExercise3() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}

//=*=*=*=*
//./Chapter_3/ch3_recipe6/src/com/packtpub/java7/concurrency/chapter3/recipe6/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe6.core;

import com.packtpub.java7.concurrency.chapter3.recipe6.task.MyPhaser;
import com.packtpub.java7.concurrency.chapter3.recipe6.task.Student;

/**
 * Main class of the example 
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Creates the Phaser
		MyPhaser phaser=new MyPhaser();
		
		// Creates 5 students and register them in the phaser
		Student students[]=new Student[5];
		for (int i=0; i<students.length; i++){
			students[i]=new Student(phaser);
			phaser.register();
		}
		
		// Create 5 threads for the students and start them
		Thread threads[]=new Thread[students.length];
		for (int i=0; i<students.length; i++) {
			threads[i]=new Thread(students[i],"Student "+i);
			threads[i].start();
		}
		
		// Wait for the finalization of the threads
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Check that the Phaser is in the Terminated state
		System.out.printf("Main: The phaser has finished: %s.\n",phaser.isTerminated());
		
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe5/src/com/packtpub/java7/concurrency/chapter3/recipe5/task/FileSearch.java
package com.packtpub.java7.concurrency.chapter3.recipe5.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * This class search for files with an extension in a directory
 */
public class FileSearch implements Runnable {

	/**
	 * Initial path for the search
	 */
	private String initPath;
	
	/**
	 * Extension of the file we are searching for
	 */
	private String end;
	
	/**
	 * List that stores the full path of the files that have the extension we are searching for
	 */
	private List<String> results;
	
	/**
	 * Phaser to control the execution of the FileSearch objects. Their execution will be divided
	 * in three phases
	 *  1st: Look in the folder and its subfolders for the files with the extension
	 *  2nd: Filter the results. We only want the files modified today
	 *  3rd: Print the results
	 */	
	private Phaser phaser;


	/**
	 * Constructor of the class. Initializes its attributes
	 * @param initPath Initial path for the search
	 * @param end Extension of the files we are searching for
	 * @param phaser Phaser object to control the execution
	 */
	public FileSearch(String initPath, String end, Phaser phaser) {
		this.initPath = initPath;
		this.end = end;
		this.phaser=phaser;
		results=new ArrayList<>();
	}

	/**
	 * Main method of the class. See the comments inside to a better description of it
	 */
	@Override
	public void run() {
		
		// Waits for the creation of all the FileSearch objects
		phaser.arriveAndAwaitAdvance();
		
		System.out.printf("%s: Starting.\n",Thread.currentThread().getName());
		
		// 1st Phase: Look for the files
		File file = new File(initPath);
		if (file.isDirectory()) {
			directoryProcess(file);
		}
		
		// If no results, deregister in the phaser and ends
		if (!checkResults()){
			return;
		}
		
		// 2nd Phase: Filter the results
		filterResults();
		
		// If no results after the filter, deregister in the phaser and ends
		if (!checkResults()){
			return;
		}
		
		// 3rd Phase: Show info
		showInfo();
		phaser.arriveAndDeregister();
		System.out.printf("%s: Work completed.\n",Thread.currentThread().getName());

	}

	/**
	 * This method prints the final results of the search
	 */
	private void showInfo() {
		for (int i=0; i<results.size(); i++){
			File file=new File(results.get(i));
			System.out.printf("%s: %s\n",Thread.currentThread().getName(),file.getAbsolutePath());
		}
		// Waits for the end of all the FileSearch threads that are registered in the phaser
		phaser.arriveAndAwaitAdvance();
	}

	/**
	 * This method checks if there are results after the execution of a phase. If there aren't
	 * results, deregister the thread of the phaser.
	 * @return true if there are results, false if not
	 */
	private boolean checkResults() {
		if (results.isEmpty()) {
			System.out.printf("%s: Phase %d: 0 results.\n",Thread.currentThread().getName(),phaser.getPhase());
			System.out.printf("%s: Phase %d: End.\n",Thread.currentThread().getName(),phaser.getPhase());
			// No results. Phase is completed but no more work to do. Deregister for the phaser
			phaser.arriveAndDeregister();
			return false;
		} else {
			// There are results. Phase is completed. Wait to continue with the next phase
			System.out.printf("%s: Phase %d: %d results.\n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
			phaser.arriveAndAwaitAdvance();
			return true;
		}		
	}

	/**
	 * Method that filter the results to delete the files modified more than a day before now
	 */
	private void filterResults() {
		List<String> newResults=new ArrayList<>();
		long actualDate=new Date().getTime();
		for (int i=0; i<results.size(); i++){
			File file=new File(results.get(i));
			long fileDate=file.lastModified();
			
			if (actualDate-fileDate<TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS)){
				newResults.add(results.get(i));
			}
		}
		results=newResults;
	}

	/**
	 * Method that process a directory
	 * 
	 * @param file
	 *            : Directory to process
	 */
	private void directoryProcess(File file) {

		// Get the content of the directory
		File list[] = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					// If is a directory, process it
					directoryProcess(list[i]);
				} else {
					// If is a file, process it
					fileProcess(list[i]);
				}
			}
		}
	}

	/**
	 * Method that process a File
	 * 
	 * @param file
	 *            : File to process
	 */
	private void fileProcess(File file) {
		if (file.getName().endsWith(end)) {
			results.add(file.getAbsolutePath());
		}
	}

}

//=*=*=*=*
//./Chapter_3/ch3_recipe5/src/com/packtpub/java7/concurrency/chapter3/recipe5/core/Main.java
package com.packtpub.java7.concurrency.chapter3.recipe5.core;

import java.util.concurrent.Phaser;

import com.packtpub.java7.concurrency.chapter3.recipe5.task.FileSearch;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Creates a Phaser with three participants
		Phaser phaser=new Phaser(3);
		
		// Creates 3 FileSearch objects. Each of them search in different directory
		FileSearch system=new FileSearch("C:\\Windows", "log", phaser);
		FileSearch apps=new FileSearch("C:\\Program Files","log",phaser);
		FileSearch documents=new FileSearch("C:\\Documents And Settings","log",phaser);
		
		// Creates a thread to run the system FileSearch and starts it
		Thread systemThread=new Thread(system,"System");
		systemThread.start();
		
		// Creates a thread to run the apps FileSearch and starts it
		Thread appsThread=new Thread(apps,"Apps");
		appsThread.start();
		
		// Creates a thread to run the documents  FileSearch and starts it
		Thread documentsThread=new Thread(documents,"Documents");
		documentsThread.start();
		try {
			systemThread.join();
			appsThread.join();
			documentsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Terminated: %s\n",phaser.isTerminated());

	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe02/src/com/packtpub/java7/concurrency/chapter5/recipe02/task/DocumentTask.java
package com.packtpub.java7.concurrency.chapter5.recipe02.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Task that will process part of the document and calculate the number of 
 * appearances of the word in that block. If it has to process
 * more that 10 lines, it divides its part in two and throws two DocumentTask
 * to calculate the number of appearances in each block.
 * In other case, it throws LineTasks to process the lines of the block
 *
 */
public class DocumentTask extends RecursiveTask<Integer> {

	/**
	 * Serial Version of the class. You have to include it because
	 * the ForkJoinTask class implements the Serializable interface
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Document to process
	 */
	private String document[][];
	
	/**
	 * Range of lines of the document this task has to process
	 */
	private int start, end;
	
	/**
	 * Word we are looking for
	 */
	private String word;
	
	/**
	 * Constructor of the class
	 * @param document Document to process
	 * @param start Starting position of the block of the document this task has to process
	 * @param end End position of the block of the document this task has to process
	 * @param word Word we are looking for
	 */
	public DocumentTask (String document[][], int start, int end, String word){
		this.document=document;
		this.start=start;
		this.end=end;
		this.word=word;
	}
	
	/**
	 * If the task has to process more that ten lines, it divide
	 * the block of lines it two subblocks and throws two DocumentTask
	 * two process them.
	 * In other case, it throws LineTask tasks to process each line of its block
	 */
	@Override
	protected Integer compute() {
		Integer result=null;
		if (end-start<10){
			result=processLines(document, start,end,word);
		} else {
			int mid=(start+end)/2;
			DocumentTask task1=new DocumentTask(document,start,mid,word);
			DocumentTask task2=new DocumentTask(document,mid,end,word);
			invokeAll(task1,task2);
			try {
				result=groupResults(task1.get(),task2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Throws a LineTask task for each line of the block of lines this task has to process
	 * @param document Document to process
	 * @param start Starting position of the block of lines it has to process
	 * @param end Finish position of the block of lines it has to process
	 * @param word Word we are looking for
	 * @return
	 */
	private Integer processLines(String[][] document, int start, int end,
			String word) {
		List<LineTask> tasks=new ArrayList<LineTask>();
		
		for (int i=start; i<end; i++){
			LineTask task=new LineTask(document[i], 0, document[i].length, word);
			tasks.add(task);
		}
		invokeAll(tasks);
		
		int result=0;
		for (int i=0; i<tasks.size(); i++) {
			LineTask task=tasks.get(i);
			try {
				result=result+task.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return new Integer(result);
	}

	/**
	 * Method that group the results of two DocumentTask tasks
	 * @param number1 Result of the first DocumentTask
	 * @param number2 Result of the second DocumentTask
	 * @return The sum of the two results
	 */
	private Integer groupResults(Integer number1, Integer number2) {
		Integer result;
		
		result=number1+number2;
		return result;
	}
	
	
}

//=*=*=*=*
//./Chapter_5/ch5_recipe02/src/com/packtpub/java7/concurrency/chapter5/recipe02/task/LineTask.java
package com.packtpub.java7.concurrency.chapter5.recipe02.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;


/**
 * Task that will process a fragment of a line of the document. If the 
 * fragment is too big (100 words or more), it split it in two parts
 * and throw to tasks to process each of the fragments.
 * 
 * It returns the number of appearances of the word in the fragment it has
 * to process.
 *
 */
public class LineTask extends RecursiveTask<Integer>{

	/**
	 * Serial Version of the class. You have to add it because the
	 * ForkJoinTask class implements the serializable interface
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A line of the document
	 */
	private String line[];
	
	/**
	 * Range of positions the task has to process
	 */
	private int start, end;
	
	/**
	 * Word we are looking for
	 */
	private String word;
	
	/**
	 * Constructor of the class
	 * @param line A line of the document
	 * @param start Position of the line where the task starts its process
	 * @param end Position of the line where the task starts its process
	 * @param word Work we are looking for
	 */
	public LineTask(String line[], int start, int end, String word) {
		this.line=line;
		this.start=start;
		this.end=end;
		this.word=word;
	}

	/**
	 * If the part of the line it has to process is smaller that 100, it
	 * calculates the number of appearances of the word in the block. Else,
	 * it divides the block in two blocks and throws to LineTask to calculate
	 * the number of appearances.
	 */
	@Override
	protected Integer compute() {
		Integer result=null;
		if (end-start<100) {
			result=count(line, start, end, word);
		} else {
			int mid=(start+end)/2;
			LineTask task1=new LineTask(line, start, mid, word);
			LineTask task2=new LineTask(line, mid, end, word);
			invokeAll(task1, task2);
			try {
				result=groupResults(task1.get(),task2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Groups the results of two LineTasks
	 * @param number1 The result of the first LineTask
	 * @param number2 The result of the second LineTask
	 * @return The sum of the numbers
	 */
	private Integer groupResults(Integer number1, Integer number2) {
		Integer result;
		
		result=number1+number2;
		return result;
	}

	/**
	 * Count the appearances of a word in a part of a line of a document
	 * @param line A line of the document
	 * @param start Position of the line where the method begin to count
	 * @param end Position of the line where the method finish the count
	 * @param word Word the method looks for
	 * @return The number of appearances of the word in the part of the line
	 */
	private Integer count(String[] line, int start, int end, String word) {
		int counter;
		counter=0;
		for (int i=start; i<end; i++){
			if (line[i].equals(word)){
				counter++;
			}
		}
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return counter;
	}
	
	

}

//=*=*=*=*
//./Chapter_5/ch5_recipe02/src/com/packtpub/java7/concurrency/chapter5/recipe02/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe02.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe02.task.DocumentTask;
import com.packtpub.java7.concurrency.chapter5.recipe02.utils.DocumentMock;

/**
 * Main class of the example. 
 */
public class Main {

	/**
	 * Main method of the class
	 */
	public static void main(String[] args) {
		
		// Generate a document with 100 lines and 1000 words per line
		DocumentMock mock=new DocumentMock();
		String[][] document=mock.generateDocument(100, 1000, "the");
	
		// Create a DocumentTask
		DocumentTask task=new DocumentTask(document, 0, 100, "the");
		
		// Create a ForkJoinPool
		ForkJoinPool pool=new ForkJoinPool();
		
		// Execute the Task
		pool.execute(task);
		
		// Write statistics about the pool
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
			System.out.printf("******************************************\n");

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while (!task.isDone());

		// Shutdown the pool
		pool.shutdown();
		
		// Wait for the finalization of the tasks
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Write the results of the tasks
		try {
			System.out.printf("Main: The word appears %d in the document",task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe02/src/com/packtpub/java7/concurrency/chapter5/recipe02/utils/DocumentMock.java
package com.packtpub.java7.concurrency.chapter5.recipe02.utils;

import java.util.Random;

/**
 * This class will simulate a document generating a String array with a determined number
 * of rows (numLines) and columns(numWords). The content of the document will be generated
 * selecting in a random way words from a String array.
 *
 */
public class DocumentMock {
	
	/**
	 * String array with the words of the document
	 */
	private String words[]={"the","hello","goodbye","packt","java","thread","pool","random","class","main"};

	/**
	 * Method that generates the String matrix
	 * @param numLines Number of lines of the document
	 * @param numWords Number of words of the document
	 * @param word Word we are going to search for
	 * @return The String matrix
	 */
	public String[][] generateDocument(int numLines, int numWords, String word){
		
		int counter=0;
		String document[][]=new String[numLines][numWords];
		Random random=new Random();
		for (int i=0; i<numLines; i++){
			for (int j=0; j<numWords; j++) {
				int index=random.nextInt(words.length);
				document[i][j]=words[index];
				if (document[i][j].equals(word)){
					counter++;
				}
			}
		}
		System.out.printf("DocumentMock: The word appears %d times in the document.\n",counter);
		return document;
	}
}

//=*=*=*=*
//./Chapter_5/ch5_recipe05/src/com/packtpub/java7/concurrency/chapter5/recipe05/task/SearchNumberTask.java
package com.packtpub.java7.concurrency.chapter5.recipe05.task;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe05.util.TaskManager;

/**
 * This task look for a number in an array of integer numbers.
 * If the part of the array it has to process has more than
 * 10 elements, it creates two subtasks and executes then asynchronously
 * with the fork method. Otherwise, look for the number in the block
 * it has to process.
 * 
 * If the task found the number, return the position where the number has
 * been found. Else, return the -1 value. If a subtask found the number,
 * the tasks suspend the other subtask and return the position where the number
 * has been found. If none of the two subtasks found the number, return the -1
 * value.
 *
 */
public class SearchNumberTask extends RecursiveTask<Integer> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Valued returned when the number has not been found by the task
	 */
	private final static int NOT_FOUND=-1;

	/**
	 * Array of numbers
	 */
	private int numbers[];
	
	/**
	 * Start and end positions of the block of numbers
	 * this task has to process
	 */
	private int start, end;
	
	/**
	 * Number this task is going to look for
	 */
	private int number;
	
	/**
	 * Object that allows the cancellation of all the tasks
	 */
	private TaskManager manager;
	
	/**
	 * Constructor of the class
	 * @param array Array of numbers
	 * @param start Start position of the block of numbers this task has to process 
	 * @param end End position of the block of numbers this task has to process
	 * @param number Number this task is going to look for
	 * @param manager 
	 */
	public SearchNumberTask(int numbers[], int start, int end, int number, TaskManager manager){
		this.numbers=numbers;
		this.start=start;
		this.end=end;
		this.number=number;
		this.manager=manager;
	}
	
	/**
	 * If the block of number this task has to process has more than
	 * ten elements, divide that block in two parts and create two
	 * new Tasks using the launchTasks() method.
	 * Else, looks for the number in the block assigned to it using
	 * the lookForNumber() method
	 */
	@Override
	protected Integer compute() {
		System.out.println("Task: "+start+":"+end);
		int ret;
		if (end-start>10) {
			ret=launchTasks();
		} else {
			ret=lookForNumber();
		}
		return new Integer(ret);
	}

	/**
	 * Looks for the number in the block of numbers assigned to this task
	 * @return The position where it found the number or -1 if it doesn't find it
	 */
	private int lookForNumber() {
		for (int i=start; i<end; i++){
			if (numbers[i]==number) {
				System.out.printf("Task: Number %d found in position %d\n",number,i);
				manager.cancelTasks(this);
				return i;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return NOT_FOUND;
	}

	/**
	 * Divide the block of numbers assigned to this task in two and 
	 * execute to new Task objects to process that blocks 
	 * @return The position where the number has been found of -1
	 * if the number haven't been found in the subtasks
	 */
	private int launchTasks() {
		int mid=(start+end)/2;
		
		SearchNumberTask task1=new SearchNumberTask(numbers,start,mid,number,manager);
		SearchNumberTask task2=new SearchNumberTask(numbers,mid,end,number,manager);
		
		manager.addTask(task1);
		manager.addTask(task2);

		task1.fork();
		task2.fork();
		int returnValue;
		
		returnValue=task1.join();
		if (returnValue!=-1) {
			return returnValue;
		}
		
		returnValue=task2.join();
		return returnValue;
	}
	
	public void writeCancelMessage(){
		System.out.printf("Task: Cancelled task from %d to %d\n",start,end);
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe05/src/com/packtpub/java7/concurrency/chapter5/recipe05/util/TaskManager.java
package com.packtpub.java7.concurrency.chapter5.recipe05.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import com.packtpub.java7.concurrency.chapter5.recipe05.task.SearchNumberTask;

/**
 * Class that stores all the tasks that have been sent to
 * a ForkJoinPool. Provides a method for the cancellation of
 * all the tasks
 *
 */
public class TaskManager {

	/**
	 * List of tasks
	 */
	private List<ForkJoinTask<Integer>> tasks;
	
	/**
	 * Constructor of the class. Initializes the list of tasks
	 */
	public TaskManager(){
		tasks=new ArrayList<>();
	}
	
	/**
	 * Method to add a new Task in the list
	 * @param task The new task
	 */
	public void addTask(ForkJoinTask<Integer> task){
		tasks.add(task);
	}

	/**
	 * Method that cancel all the tasks in the list
	 * @param cancelTask 
	 */
	public void cancelTasks(ForkJoinTask<Integer> cancelTask){
		for (ForkJoinTask<Integer> task  :tasks) {
			if (task!=cancelTask) {
				task.cancel(true);
				((SearchNumberTask)task).writeCancelMessage();
			}
		}
	}
}

//=*=*=*=*
//./Chapter_5/ch5_recipe05/src/com/packtpub/java7/concurrency/chapter5/recipe05/util/ArrayGenerator.java
package com.packtpub.java7.concurrency.chapter5.recipe05.util;

import java.util.Random;

/**
 * Class that generates an array of integer numbers between 0 and 10
 * with a size specified as parameter
 *
 */
public class ArrayGenerator {

	/**
	 * Method that generates an array of integer numbers between 0 and 10
	 * with the specified size
	 * @param size The size of the array
	 * @return An array of random integer numbers between 0 and 10
	 */
	public int[] generateArray(int size) {
		int array[]=new int[size];
		Random random=new Random();
		for (int i=0; i<size; i++){
			array[i]=random.nextInt(10);
		}
		return array;
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe05/src/com/packtpub/java7/concurrency/chapter5/recipe05/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe05.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe05.task.SearchNumberTask;
import com.packtpub.java7.concurrency.chapter5.recipe05.util.ArrayGenerator;
import com.packtpub.java7.concurrency.chapter5.recipe05.util.TaskManager;

/**
 * Main class of the program. 
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Generate an array of 1000 integers
		ArrayGenerator generator=new ArrayGenerator();
		int array[]=generator.generateArray(1000);
		
		// Create a TaskManager object
		TaskManager manager=new TaskManager();
		
		// Create a ForkJoinPool with the default constructor
		ForkJoinPool pool=new ForkJoinPool();
		
		// Create a Task to process the array
		SearchNumberTask task=new SearchNumberTask(array,0,1000,5,manager);
		
		// Execute the task
		pool.execute(task);

		// Shutdown the pool
		pool.shutdown();
		
		
		// Wait for the finalization of the task
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Write a message to indicate the end of the program
		System.out.printf("Main: The program has finished\n");
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe03/task/FolderProcessor.java
package com.packtpub.java7.concurrency.chapter5.recipe03.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Task that process a folder. Throw a new FolderProcesor task for each
 * subfolder. For each file in the folder, it checks if the file has the extension
 * it's looking for. If it's the case, it add the file name to the list of results.
 *
 */
public class FolderProcessor extends RecursiveTask<List<String>> {

	/**
	 * Serial Version of the class. You have to add it because the 
	 * ForkJoinTask class implements the Serializable interfaces
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Path of the folder this task is going to process
	 */
	private String path;
	
	/**
	 * Extension of the file the task is looking for
	 */
	private String extension;
	
	/**
	 * Constructor of the class
	 * @param path Path of the folder this task is going to process
	 * @param extension Extension of the files this task is looking for
	 */
	public FolderProcessor (String path, String extension) {
		this.path=path;
		this.extension=extension;
	}
	
	/**
	 * Main method of the task. It throws an additional FolderProcessor task
	 * for each folder in this folder. For each file in the folder, it compare
	 * its extension with the extension it's looking for. If they are equals, it
	 * add the full path of the file to the list of results
	 */
	@Override
	protected List<String> compute() {
		List<String> list=new ArrayList<>();
		List<FolderProcessor> tasks=new ArrayList<>();
		
		File file=new File(path);
		File content[] = file.listFiles();
		if (content != null) {
			for (int i = 0; i < content.length; i++) {
				if (content[i].isDirectory()) {
					// If is a directory, process it. Execute a new Task
					FolderProcessor task=new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				} else {
					// If is a file, process it. Compare the extension of the file and the extension
					// it's looking for
					if (checkFile(content[i].getName())){
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			
			// If the number of tasks thrown by this tasks is bigger than 50, we write a message
			if (tasks.size()>50) {
				System.out.printf("%s: %d tasks ran.\n",file.getAbsolutePath(),tasks.size());
			}
			
			// Include the results of the tasks
			addResultsFromTasks(list,tasks);
		}
		return list;
	}

	/**
	 * Add the results of the tasks thrown by this task to the list this
	 * task will return. Use the join() method to wait for the finalization of
	 * the tasks
	 * @param list List of results
	 * @param tasks List of tasks
	 */
	private void addResultsFromTasks(List<String> list,
			List<FolderProcessor> tasks) {
		for (FolderProcessor item: tasks) {
			list.addAll(item.join());
		}
	}

	/**
	 * Checks if a name of a file has the extension the task is looking for
	 * @param name name of the file
	 * @return true if the name has the extension or false otherwise
	 */
	private boolean checkFile(String name) {
		if (name.endsWith(extension)) {
			return true;
		}
		return false;
	}

	
}

//=*=*=*=*
//./Chapter_5/ch5_recipe03/src/com/packtpub/java7/concurrency/chapter5/recipe03/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe03.core;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe03.task.FolderProcessor;

/**
 * Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	*/
	public static void main(String[] args) {
		// Create the pool
		ForkJoinPool pool=new ForkJoinPool();
		
		// Create three FolderProcessor tasks for three diferent folders
		FolderProcessor system=new FolderProcessor("C:\\Windows", "log");
		FolderProcessor apps=new FolderProcessor("C:\\Program Files","log");
		FolderProcessor documents=new FolderProcessor("C:\\Documents And Settings","log");
		
		// Execute the three tasks in the pool
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		
		// Write statistics of the pool until the three tasks end
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((!system.isDone())||(!apps.isDone())||(!documents.isDone()));
		
		// Shutdown the pool
		pool.shutdown();
		
		// Write the number of results calculate by each task
		List<String> results;
		
		results=system.join();
		System.out.printf("System: %d files found.\n",results.size());
		
		results=apps.join();
		System.out.printf("Apps: %d files found.\n",results.size());
		
		results=documents.join();
		System.out.printf("Documents: %d files found.\n",results.size());
		

	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe04/src/com/packtpub/java7/concurrency/chapter5/recipe04/task/Task.java
package com.packtpub.java7.concurrency.chapter5.recipe04.task;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * This task throws and exception. It process an array of elements. If the
 * block of elements it has to process has 10 or more elements, it divides
 * the block in two and executes two subtasks to process those blocks. Else, 
 * sleeps the task one second. Additionally,  If the block of elements it 
 * has to process has the third position, it throws an exception.
 * 
 */
public class Task extends RecursiveTask<Integer> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Array to process
	 */
	private int array[];
	
	/**
	 * Start and end positions of the block of elements this task
	 * has to process
	 */
	private int start, end;
	
	/**
	 * Constructor of the class
	 * @param array Array to process
	 * @param start Start position of the block of elements this task has to process
	 * @param end End position of the block of elements this task has to process
	 */
	public Task(int array[], int start, int end){
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	/**
	 * Main method of the task. If the block of elements it has to process has 10
	 *  or more elements, it divides the block in two and executes two subtasks 
	 *  to process those blocks. Else, sleeps the task one second. Additionally,
	 *  If the block of elements it has to process has the third position, it 
	 *  throws an exception.
	 */
	@Override
	protected Integer compute() {
		System.out.printf("Task: Start from %d to %d\n",start,end);
		if (end-start<10) {
			if ((3>start)&&(3<end)){
				throw new RuntimeException("This task throws an Exception: Task from  "+start+" to "+end);
			}
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} else {
			int mid=(end+start)/2;
			Task task1=new Task(array,start,mid);
			Task task2=new Task(array,mid,end);
			invokeAll(task1, task2);
			System.out.printf("Task: Result form %d to %d: %d\n",start,mid,task1.join());
			System.out.printf("Task: Result form %d to %d: %d\n",mid,end,task2.join());
		}
		System.out.printf("Task: End form %d to %d\n",start,end);
		return new Integer(0);
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe04/src/com/packtpub/java7/concurrency/chapter5/recipe04/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe04.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe04.task.Task;

/**
 * Main class of the example. Creates a ForkJoinPool, an array of 100
 * elements and a Task object. Executes the Task object in the pool
 * and process the exception thrown by the Task
 *
 */
public class Main {

	/**
	 * Main method of the class
	 */
	public static void main(String[] args) {
		// Array of 100 integers
		int array[]=new int[100];
		// Task to process the array
		Task task=new Task(array,0,100);
		// ForkJoinPool to execute the Task
		ForkJoinPool pool=new ForkJoinPool();
		
		// Execute the task
		pool.execute(task);
	
		// Shutdown the ForkJoinPool
		pool.shutdown();
		
		// Wait for the finalization of the task
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Check if the task has thrown an Exception. If it's the case, write it
		// to the console
		
		if (task.isCompletedAbnormally()) {
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n",task.getException());
		}
		
		System.out.printf("Main: Result: %d",task.join());
	}
}

//=*=*=*=*
//./Chapter_5/ch5_recipe01/src/com/packtpub/java7/concurrency/chapter5/recipe01/task/Task.java
package com.packtpub.java7.concurrency.chapter5.recipe01.task;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import com.packtpub.java7.concurrency.chapter5.recipe01.util.Product;

/**
 * This class implements the tasks that are going to update the
 * price information. If the assigned interval of values is less that 10, it
 * increases the prices of the assigned products. In other case, it divides
 * the assigned interval in two, creates two new tasks and execute them
 *
 */
public class Task extends RecursiveAction {

	/**
	 * serial version UID. The ForkJoinTask class implements the serializable interface.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of products
	 */
	private List<Product> products;
	
	/**
	 * Fist and Last position of the interval assigned to the task
	 */
	private int first;
	private int last;
	
	/**
	 * Increment in the price of products this task has to apply
	 */
	private double increment;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param products list of products
	 * @param first first element of the list assigned to the task
	 * @param last last element of the list assigned to the task
	 * @param increment price increment that this task has to apply
	 */
	public Task (List<Product> products, int first, int last, double increment) {
		this.products=products;
		this.first=first;
		this.last=last;
		this.increment=increment;
	}
	
	/**
	 * Method that implements the job of the task
	 */
	@Override
	protected void compute() {
		if (last-first<10) {
			updatePrices();
		} else {
			int middle=(last+first)/2;
			System.out.printf("Task: Pending tasks: %s\n",getQueuedTaskCount());
			Task t1=new Task(products, first,middle+1, increment);
			Task t2=new Task(products, middle+1,last, increment);
			invokeAll(t1, t2);	
		}
	}

	/**
	 * Method that updates the prices of the assigned products to the task
	 */
	private void updatePrices() {
		for (int i=first; i<last; i++){
			Product product=products.get(i);
			product.setPrice(product.getPrice()*(1+increment));
		}
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe01/src/com/packtpub/java7/concurrency/chapter5/recipe01/util/ProductListGenerator.java
package com.packtpub.java7.concurrency.chapter5.recipe01.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates a product list of a determined size.
 * Each product is initialized with a predefined name and price.
 *
 */
public class ProductListGenerator {

	/**
	 * This method generates the list of products
	 * @param size the size of the product list
	 * @return the generated list of products
	 */
	public List<Product> generate (int size) {
		List<Product> ret=new ArrayList<Product>();
		
		for (int i=0; i<size; i++){
			Product product=new Product();
			product.setName("Product "+i);
			product.setPrice(10);
			ret.add(product);
		}
		
		return ret;
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe01/src/com/packtpub/java7/concurrency/chapter5/recipe01/util/Product.java
package com.packtpub.java7.concurrency.chapter5.recipe01.util;

/**
 * This class stores the data of a Product. It's name and it's price
 *
 */
public class Product {
	
	/**
	 * Name of the product
	 */
	private String name;
	
	/**
	 * Price of the product
	 */
	private double price;
	
	/**
	 * This method returns the name of the product
	 * @return the name of the product
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method establish the name of the product
	 * @param name the name of the product
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * This method returns the price of the product
	 * @return the price of the product
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * This method establish the price of the product
	 * @param price the price of the product
	 */
	public void setPrice(double price) {
		this.price = price;
	}

}

//=*=*=*=*
//./Chapter_5/ch5_recipe01/src/com/packtpub/java7/concurrency/chapter5/recipe01/core/Main.java
package com.packtpub.java7.concurrency.chapter5.recipe01.core;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe01.task.Task;
import com.packtpub.java7.concurrency.chapter5.recipe01.util.Product;
import com.packtpub.java7.concurrency.chapter5.recipe01.util.ProductListGenerator;

/**
 * Main class of the example. It creates a list of products, a ForkJoinPool and 
 * a task to execute the actualization of products. 
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a list of products
		ProductListGenerator generator=new ProductListGenerator();
		List<Product> products=generator.generate(10000);
		
		// Craete a task
		Task task=new Task(products, 0, products.size(), 0.20);
		
		// Create a ForkJoinPool
		ForkJoinPool pool=new ForkJoinPool();
		
		// Execute the Task
		pool.execute(task);

		// Write information about the pool
		do {
			System.out.printf("Main: Thread Count: %d\n",pool.getActiveThreadCount());
			System.out.printf("Main: Thread Steal: %d\n",pool.getStealCount());
			System.out.printf("Main: Paralelism: %d\n",pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());
	
		// Shutdown the pool
		pool.shutdown();
		
		// Check if the task has completed normally
		if (task.isCompletedNormally()){
			System.out.printf("Main: The process has completed normally.\n");
		}

		// Expected result: 12. Write products which price is not 12
		for (int i=0; i<products.size(); i++){
			Product product=products.get(i);
			if (product.getPrice()!=12) {
				System.out.printf("Product %s: %f\n",product.getName(),product.getPrice());
			}
		}
		
		// End of the program
		System.out.println("Main: End of the program.\n");

	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe07/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe07.task;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.packtpub.java7.concurrency.chapter8.recipe07.logger.MyLogger;

/**
 * This class is the Task you're going to use to test the
 * Logger system you have implemented. It simply write a log
 * message indicating the start of the task, sleeps the thread for
 * two seconds and write another log message indicating the end of the
 * task.
 *
 */
public class Task implements Runnable {

	/**
	 * Main method of the task
	 */
	@Override
	public void run() {
		/*
		 * Get the Logger
		 */
		Logger logger=MyLogger.getLogger(this.getClass().getName());
		
		/*
		 * Write a message indicating the start of the task
		 */
		logger.entering(Thread.currentThread().getName(), "run()");
		
		/*
		 * Sleep the task for two seconds
		 */
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Write a message indicating the end of the task
		 */
		logger.exiting(Thread.currentThread().getName(), "run()",Thread.currentThread());
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe07/logger/MyFormatter.java
package com.packtpub.java7.concurrency.chapter8.recipe07.logger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class is used to format the log messages. The Logger class call the format() method
 * to get the formatted log message that it's going to write. The format method receives a
 * LogRecord object as parameter. That object has all the information about the message.
 *
 */
public class MyFormatter extends Formatter {

	/**
	 * Method that formats the log message. It's declared as abstract in the
	 * Formatter class. It's called by the Logger class. It receives a LogRecord
	 * object as parameter with all the information of the log message
	 */
	@Override
	public String format(LogRecord record) {
		
		/*
		 * Create a string buffer to construct the message.
		 */
		StringBuilder sb=new StringBuilder();
		
		/*
		 * Add the parts of the message with the desired format.
		 */
		sb.append("["+record.getLevel()+"] - ");
		sb.append(new Date(record.getMillis())+" : ");
		sb.append(record.getSourceClassName()+"."+record.getSourceMethodName()+" : ");
		sb.append(record.getMessage()+"\n");
		
		/*
		 * Convert the string buffer to string and return it
		 */
		return sb.toString();
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe07/logger/MyLogger.java
package com.packtpub.java7.concurrency.chapter8.recipe07.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to create a Logger object with the configuration
 * you want. In this case, you're going to write all the log messages generated
 * in the application to the recipe8.log file and with the format specified in the
 * MyFormatter class. It uses the Logger class to create the Logger object. This class
 * creates a Logger object per name that is passed as parameter. 
 *
 */
public class MyLogger {
	
	/**
	 * Handler to control that the log messages are written in the recipe8.log file
	 */
	private static Handler handler;
	
	/**
	 * Static method that returns the log object associated with the name received
	 * as parameter. If it's a new Logger object, this method configures it with your
	 * configuration. 
	 * @param name Name of the Logger object you want to obtain.
	 * @return The Logger object generated.
	 */
	public static Logger getLogger(String name){
		/*
		 * Get the logger
		 */
		Logger logger=Logger.getLogger(name);
		/*
		 * Set the level to show all the messages
		 */
		logger.setLevel(Level.ALL);
		try {
			/*
			 * If the Handler object is null, we create one to
			 * write the log messages in the recipe8.log file
			 * with the format specified by the MyFormatter class
			 */
			if (handler==null) {
				handler=new FileHandler("recipe8.log");
				Formatter format=new MyFormatter();
				handler.setFormatter(format);
			}
			/*
			 * If the Logger object hasn't handler, we add the Handler object
			 * to it
			 */
			if (logger.getHandlers().length==0) {
				logger.addHandler(handler);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Return the Logger object.
		 */
		return logger;
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe05/src/com/packtpub/java7/concurrency/chapter8/recipe07/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe07.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.packtpub.java7.concurrency.chapter8.recipe07.logger.MyLogger;
import com.packtpub.java7.concurrency.chapter8.recipe07.task.Task;

/**
 * Main class of the example. It launch five Task objects and write
 * some log messages indicating the evolution of the execution of the program
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		 * Get the Logger object
		 */
		Logger logger=MyLogger.getLogger("Core");
		
		/*
		 * Write a message indicating the start of the execution
		 */
		logger.entering("Core", "main()",args);
		
		/*
		 * Create and launch five Task objects
		 */
		Thread threads[]=new Thread[5];
		for (int i=0; i<threads.length; i++) {
			logger.log(Level.INFO,"Launching thread: "+i);
			Task task=new Task();
			threads[i]=new Thread(task);
			logger.log(Level.INFO,"Thread created: "+threads[i].getName());
			threads[i].start();
		}
		
		/*
		 * Write a log message indicating that the threads have been created
		 */
		logger.log(Level.INFO,"Ten Threads created. Waiting for its finalization");
		
		/*
		 * Wait for the finalization of the threads
		 */
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
				logger.log(Level.INFO,"Thread has finished its execution",threads[i]);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Exception", e);
			}
		}
		
		/*
		 * Write a log message indicating the end of the program
		 */
		logger.exiting("Main", "main()");
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe04/src/com/packtpub/java7/concurrency/chapter8/recipe06/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe06.task;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a task used to show how to monitor the
 * behavior of a Fork/Join pool. The main objective of the task
 * is increment all the elements of an array. Every task has to
 * process a set of elements of this array. If the task has to process
 * more than 100 elements, it divides the set it has two process in two
 * subsets and create two tasks to execute them. Otherwise, it process
 * the elements of the subset it has to process
 */
public class Task extends RecursiveAction{

	/**
	 * Declare and initialize the serial version uid of the class
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Array of elements you want to increment
	 */
	private int array[];
	
	/**
	 * Start position of the set of elements this task has to process
	 */
	private int start;
	
	/**
	 * End position of the set of elements this task has to process
	 */
	private int end;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param array Array of elements this task has to process
	 * @param start Start position of the set of elements this task has to process
	 * @param end End position of the set of elements this task has to process 
	 */
	public Task (int array[], int start, int end) {
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	/**
	 * Main method of the task. If it has to process more that 100 elements, it
	 * divides that set it two sub-sets and creates two task to process them.
	 * Otherwise, it increments directly the elements it has to process.
	 */
	@Override
	protected void compute() {
		if (end-start>100) {
			int mid=(start+end)/2;
			Task task1=new Task(array,start,mid);
			Task task2=new Task(array,mid,end);
			
			/*
			 * Start the sub-tasks
			 */
			task1.fork();
			task2.fork();
			
			/*
			 * Wait for the finalization of the sub-tasks
			 */
			task1.join();
			task2.join();
		} else {
			for (int i=start; i<end; i++) {
				array[i]++;
				
				try {
					TimeUnit.MILLISECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe04/src/com/packtpub/java7/concurrency/chapter8/recipe06/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe06.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe06.task.Task;

/**
 * Main class of the example. It creates all the elements for the
 * execution and writes information about the Fork/Join pool that
 * executes the task
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * Create the Fork/Join pool
		 */
		ForkJoinPool pool=new ForkJoinPool();
		
		/*
		 * Create a new Array of integer numbers with 10000 elements
		 */
		int array[]=new int[10000];
		
		/*
		 * Create a new task
		 */
		Task task1=new Task(array,0,array.length);
		
		/*
		 * Execute the task in the Fork/Join pool
		 */
		pool.execute(task1);
		
		/*
		 * Wait for the finalization of the task writing information
		 * about the pool every second
		 */
		while (!task1.isDone()) {
			showLog(pool);
			TimeUnit.SECONDS.sleep(1);
		}
		
		/*
		 * Shutdown the pool
		 */
		pool.shutdown();
		
		/*
		 * Wait for the finalization of the pool
		 */
		pool.awaitTermination(1, TimeUnit.DAYS);
		
		/*
		 * End of the program
		 */
		showLog(pool);
		System.out.printf("Main: End of the program.\n");

	}

	/*
	 * This method writes information about a Fork/Join pool
	 */
	private static void showLog(ForkJoinPool pool) {
		System.out.printf("**********************\n");
		System.out.printf("Main: Fork/Join Pool log\n");
		System.out.printf("Main: Fork/Join Pool: Parallelism: %d\n",pool.getParallelism());
		System.out.printf("Main: Fork/Join Pool: Pool Size: %d\n",pool.getPoolSize());
		System.out.printf("Main: Fork/Join Pool: Active Thread Count: %d\n",pool.getActiveThreadCount());
		System.out.printf("Main: Fork/Join Pool: Running Thread Count: %d\n",pool.getRunningThreadCount());
		System.out.printf("Main: Fork/Join Pool: Queued Submission: %d\n",pool.getQueuedSubmissionCount());
		System.out.printf("Main: Fork/Join Pool: Queued Tasks: %d\n",pool.getQueuedTaskCount());
		System.out.printf("Main: Fork/Join Pool: Queued Submissions: %s\n",pool.hasQueuedSubmissions());
		System.out.printf("Main: Fork/Join Pool: Steal Count: %d\n",pool.getStealCount());
		System.out.printf("Main: Fork/Join Pool: Terminated : %s\n",pool.isTerminated());
		System.out.printf("**********************\n");
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe01/src/com/packtpub/java7/concurrency/chapter8/recipe02/task/MyLock.java
package com.packtpub.java7.concurrency.chapter8.recipe02.task;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create a specific class to extend the ReentrantLock class.
 * The main objective of this class is to convert in public two
 * protected methods. 
 * getOwnerName() returns the name of the thread that have the control
 * of the lock and uses the proctected method getOwner();
 * getThreads() returns the list of threads queued in the lock and uses
 * the protected method getQueuedThreads();
 */
public class MyLock extends ReentrantLock {

	/**
	 * Declare the serial version uid of the class
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This method returns the name of the thread that have the control of the Lock of the constant "None"
	 * if the Lock is free 
	 * @return The name of the thread that has the control of the lock
	 */
	public String getOwnerName() {
		if (this.getOwner()==null) {
			return "None";
		}
		return this.getOwner().getName();
	}
	
	/**
	 * This method returns the list of the threads queued in the lock
	 * @return The list of threads queued in the Lock
	 */
	public Collection<Thread> getThreads() {
		return this.getQueuedThreads();
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe01/src/com/packtpub/java7/concurrency/chapter8/recipe02/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe02.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * This task used to write information about a Lock. It takes the control
 * of the Lock, sleeps for 500 milliseconds and free the Lock. Repeat this
 * behavior five times
 *
 */
public class Task implements Runnable {

	/**
	 * Lock shared by all the tasks
	 */
	private Lock lock;
	
	/**
	 * Constructor of the class
	 * @param lock Lock shared by all the tasks
	 */
	public Task (Lock lock) {
		this.lock=lock;
	}
	
	/**
	 * Main method of the task. Takes the control of the Lock, sleeps for 500 milliseconds and free the
	 * lock. Repeats this behavior five times
	 */
	@Override
	public void run() {
		/*
		 * Loop with five steps
		 */
		for (int i=0; i<5; i++) {
			/*
			 * Acquire the lock
			 */
			lock.lock();
			System.out.printf("%s: Get the Lock.\n",Thread.currentThread().getName());
			/*
			 * Sleeps the thread 500 milliseconds
			 */
			try {
				TimeUnit.MILLISECONDS.sleep(500);
				System.out.printf("%s: Free the Lock.\n",Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				/*
				 * Free the lock
				 */
				lock.unlock();
			}
		}

	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe01/src/com/packtpub/java7/concurrency/chapter8/recipe02/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe02.core;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe02.task.MyLock;
import com.packtpub.java7.concurrency.chapter8.recipe02.task.Task;

/**
 * Main class of the example. Create five threads to execute the task and write info
 * about the Lock shared by all the threads
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Create a Lock object
		 */
		MyLock lock=new MyLock();
		
		/*
		 * Create an array for five threads
		 */
		Thread threads[]=new Thread[5];
		
		/*
		 * Create and start five threads
		 */
		for (int i=0; i<5; i++) {
			Task task=new Task(lock);
			threads[i]=new Thread(task);
			threads[i].start();
		}
		
		/*
		 * Create a loop with 15 steps
		 */
		for (int i=0; i<15; i++) {
			/*
			 * Write info about the lock
			 */
			System.out.printf("Main: Logging the Lock\n");
			System.out.printf("************************\n");
			System.out.printf("Lock: Owner : %s\n",lock.getOwnerName());
			System.out.printf("Lock: Queued Threads: %s\n",lock.hasQueuedThreads());
			if (lock.hasQueuedThreads()){
				System.out.printf("Lock: Queue Length: %d\n",lock.getQueueLength());
				System.out.printf("Lock: Queued Threads: ");
				Collection<Thread> lockedThreads=lock.getThreads();
				for (Thread lockedThread : lockedThreads) {
				System.out.printf("%s ",lockedThread.getName());
				}
				System.out.printf("\n");
			}
			System.out.printf("Lock: Fairness: %s\n",lock.isFair());
			System.out.printf("Lock: Locked: %s\n",lock.isLocked());
			System.out.printf("************************\n");
			/*
			 * Sleep the thread for one second
			 */
			TimeUnit.SECONDS.sleep(1);
		}
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe08/src/com/packtpub/java7/concurrenty/chapter8/recipe10/task/Task2.java

package com.packtpub.java7.concurrenty.chapter8.recipe10.task;

import java.util.concurrent.locks.Lock;

/**
 * This task implements the second task of the example
 */
public class Task2 implements Runnable{

    /**
     * Two locks used by the example
     */
    private Lock lock1, lock2;
    
    /**
     * Constructor for the class. Initialize its attributes
     * @param lock1 A lock used by the class
     * @param lock2 A lock used by the class
     */
    public Task2(Lock lock1, Lock lock2) {
        this.lock1=lock1;
        this.lock2=lock2;
    }
    
    /**
     * Main method of the task
     */
    @Override
    public void run() {
        lock2.lock();
        System.out.printf("Task 2: Lock 2 locked\n");
        lock1.lock();
        System.out.printf("Task 2: Lock 1 locked\n");
        lock1.unlock();
        lock2.unlock();
    }
    
}

//=*=*=*=*
//./Chapter_8/ch8_recipe08/src/com/packtpub/java7/concurrenty/chapter8/recipe10/task/Task1.java
package com.packtpub.java7.concurrenty.chapter8.recipe10.task;

import java.util.concurrent.locks.Lock;

/**
 * This class implements the first task of the example
 */
public class Task1 implements Runnable {
    
    /**
     * Two locks that will be used by the example
     */
    private Lock lock1, lock2;
    
    /**
     * Constructor of the class. Initialize its attributes
     * @param lock1 A lock used by the class
     * @param lock2 A lock used by the class
     */
    public Task1 (Lock lock1, Lock lock2) {
        this.lock1=lock1;
        this.lock2=lock2;
    }
    
    /**
     * Main method of the task
     */
    @Override
    public void run() {
        lock1.lock();
        System.out.printf("Task 1: Lock 1 locked\n");
        lock2.lock();
        System.out.printf("Task 1: Lock 2 locked\n");
        lock2.unlock();
        lock1.unlock();
    }
    
}

//=*=*=*=*
//./Chapter_8/ch8_recipe08/src/com/packtpub/java7/concurrenty/chapter8/recipe10/core/Main.java
package com.packtpub.java7.concurrenty.chapter8.recipe10.core;

import com.packtpub.java7.concurrenty.chapter8.recipe10.task.Task1;
import com.packtpub.java7.concurrenty.chapter8.recipe10.task.Task2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the example
 */
public class Main {

    /**
     * Main method of the example
     * @param args 
     */
    public static void main(String args[]) throws Exception{
        /*
         * Create two lock objects
         */
        Lock lock1, lock2;
        lock1=new ReentrantLock();
        lock2=new ReentrantLock();
        
        /*
         * Create two tasks
         */
        Task1 task1=new Task1(lock1, lock2);
        Task2 task2=new Task2(lock1, lock2);
        
        /*
         * Execute the two tasks 
         */
        Thread thread1=new Thread(task1);
        Thread thread2=new Thread(task2);
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        /*
         * While the tasks haven't finished, write a message every 500 milliseconds
         */
        /*while ((thread1.isAlive()) &&(thread2.isAlive())) {
            System.out.println("Core: The example is running");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
              ex.printStackTrace();
            }
        }*/
    }
}

//=*=*=*=*
//./Chapter_8/ch8_recipe03/src/com/packtpub/java7/concurrency/chapter8/recipe05/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe05.task;

import java.util.concurrent.TimeUnit;

/**
 * Task implemented to log data about an Executor 
 *
 */
public class Task implements Runnable {

	/**
	 * Number of milliseconds this task is going to sleep the thread
	 */
	private long milliseconds;
	
	/**
	 * Constructor of the task. Initializes its attributes
	 * @param milliseconds Number of milliseconds this task is going to sleep the thread
	 */
	public Task (long milliseconds) {
		this.milliseconds=milliseconds;
	}
	
	/**
	 * Main method of the task. Sleep the thread the number of millisecons specified by
	 * the milliseconds attribute.
	 */
	@Override
	public void run() {
		
		System.out.printf("%s: Begin\n",Thread.currentThread().getName());
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: End\n",Thread.currentThread().getName());
		
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe03/src/com/packtpub/java7/concurrency/chapter8/recipe05/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe05.core;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe05.task.Task;

/**
 * Main class of the example. Create an Executor and submits ten Task
 * objects for its execution. It writes information about the executor
 * to see its evolution. 
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		/*
		 * Create a new Executor
		 */
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		/*
		 * Create and submit ten tasks
		 */
		Random random=new Random();
		for (int i=0; i<10; i++) {
			Task task=new Task(random.nextInt(10000));
			executor.submit(task);
		}
		
		/*
		 * Write information about the executor
		 */
		for (int i=0; i<5; i++){
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		
		/*
		 * Shutdown the executor
		 */
		executor.shutdown();

		/*
		 * Write information about the executor
		 */
		for (int i=0; i<5; i++){
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		
		/*
		 * Wait for the finalization of the executor
		 */
		executor.awaitTermination(1, TimeUnit.DAYS);
		
		/*
		 * Write a message to indicate the end of the program 
		 */
		System.out.printf("Main: End of the program.\n");
		
	}

	/**
	 * Method that writes in the console information about an executor
	 * @param executor Executor this method is going to process 
	 */
	private static void showLog(ThreadPoolExecutor executor) {
		System.out.printf("*********************");
		System.out.printf("Main: Executor Log");
		System.out.printf("Main: Executor: Core Pool Size: %d\n",executor.getCorePoolSize());
		System.out.printf("Main: Executor: Pool Size: %d\n",executor.getPoolSize());
		System.out.printf("Main: Executor: Active Count: %d\n",executor.getActiveCount());
		System.out.printf("Main: Executor: Task Count: %d\n",executor.getTaskCount());
		System.out.printf("Main: Executor: Completed Task Count: %d\n",executor.getCompletedTaskCount());
		System.out.printf("Main: Executor: Shutdown: %s\n",executor.isShutdown());
		System.out.printf("Main: Executor: Terminating: %s\n",executor.isTerminating());
		System.out.printf("Main: Executor: Terminated: %s\n",executor.isTerminated());
		System.out.printf("*********************\n");
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe09/src/com/packtpub/java7/concurrency/chapter8/recipe11/test/ProducerConsumerTest.java
package com.packtpub.java7.concurrency.chapter8.recipe11.test;

import java.util.concurrent.LinkedTransferQueue;

import edu.umd.cs.mtc.MultithreadedTestCase;

/**
 * This class implements a test of the LinkedTransferQueue. It has
 * two consumers and a producer. First, arrives the first consumer then
 * arrives the second consumer and finally the producer puts two Strings
 * in the buffer. 
 */
public class ProducerConsumerTest extends MultithreadedTestCase {

	/**
	 * Declare the buffer shared by the producer and the consumers 
	 */
	private LinkedTransferQueue<String> queue;
	
	/**
	 * Creates the buffer
	 */
	@Override
	public void initialize() {
		super.initialize();
		queue=new LinkedTransferQueue<String>();
		System.out.printf("Test: The test has been initialized\n");
	}
	
	/**
	 * This is the first consumer. It only consumes a String
	 * @throws InterruptedException
	 */
	public void thread1() throws InterruptedException {
		String ret=queue.take();
		System.out.printf("Thread 1: %s\n",ret);
	}
	
	/**
	 * This is the second consumer. It waits for the first tick that
	 * happens when the first consumer arrives. Then, consumes a String
	 * @throws InterruptedException
	 */
	public void thread2() throws InterruptedException {
		waitForTick(1);
		String ret=queue.take();
		System.out.printf("Thread 2: %s\n",ret);
	}
	
	/**
	 * This is the Producer. It waits for the first tick that happens when
	 * the first consumer arrives. Then, waits for the second tick that 
	 * happens when the second consumer arrives. Finally, put two strings in
	 * the buffer.
	 */
	public void thread3() {
		waitForTick(1);
		waitForTick(2);
		queue.put("Event 1");
		queue.put("Event 2");
		System.out.printf("Thread 3: Inserted two elements\n");
	}
	
	/**
	 * This method is executed when the test finish its execution. It checks that
	 * the buffer is empty
	 */
	@Override
	public void finish() {
		super.finish();
		System.out.printf("Test: End\n");
		assertEquals(true, queue.size()==0);
		System.out.printf("Test: Result: The queue is empty\n");
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe09/src/com/packtpub/java7/concurrency/chapter8/recipe11/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe11.core;

import com.packtpub.java7.concurrency.chapter8.recipe11.test.ProducerConsumerTest;

import edu.umd.cs.mtc.TestFramework;

/**
 * Main class of the example. Executes the test of the LinkedTransferQueue
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {
		
		/*
		 * Create a Test object
		 */
		ProducerConsumerTest test=new ProducerConsumerTest();
		
		/*
		 * Execute the test
		 */
		System.out.printf("Main: Starting the test\n");
		TestFramework.runOnce(test);
		System.out.printf("Main: The test has finished\n");

	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe06/src/com/packtpub/java7/concurrency/chapter8/recipe08/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe08.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Task implemented to test the FindBugs utility 
 *
 */
public class Task implements Runnable {

	/**
	 * Lock used in the task
	 */
	private ReentrantLock lock;
	
	/**
	 * Constructor of the class
	 * @param lock Lock used in the task
	 */
	public Task(ReentrantLock lock) {
		this.lock=lock;
	}
	
	/**
	 * Main method of the task. 
	 */
	@Override
	public void run() {
		lock.lock();

		try {
			TimeUnit.SECONDS.sleep(1);
			/*
			 * There is a problem with this unlock. If the thread is interrupted
			 * while it is sleeping, the lock won't be unlocked and it will cause
			 * that the threads that are waiting for this block will be blocked and
			 * never will get the control of the Lock.
			 */
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe06/src/com/packtpub/java7/concurrency/chapter8/recipe08/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe08.core;

import java.util.concurrent.locks.ReentrantLock;

import com.packtpub.java7.concurrency.chapter8.recipe08.task.Task;

/**
 * Main class of the example. It launch ten Task objects 
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Create a Lock
		 */
		ReentrantLock lock=new ReentrantLock();
		
		/*
		 * Executes the threads. There is a problem with this
		 * block of code. It uses the run() method instead of
		 * the start() method.
		 */
		for (int i=0; i<10; i++) {
			Task task=new Task(lock);
			Thread thread=new Thread(task);
			thread.run();
		}

	}

}

//=*=*=*=*
//./Chapter_8/ch8_recipe02/src/com/packtpub/java7/concurrency/chapter8/recipe04/task/Task.java
package com.packtpub.java7.concurrency.chapter8.recipe04.task;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Task to write information about a Phaser. It executes three phases. In each
 * phase, it sleeps the thread the number of seconds specified by the time attribute
 *
 */
public class Task implements Runnable {
	
	/**
	 * Number of seconds this task is going to sleep the thread in each phase
	 */
	private int time;
	
	/**
	 * Phaser to synchronize the execution of phases
	 */
	private Phaser phaser;
	
	/**
	 * Constructor of the class. Initialize its attributes
	 * @param time Number of seconds this task is going to sleep the thread in each phase
	 * @param phaser Phaser to synchronize the execution of tasks
	 */
	public Task(int time, Phaser phaser) {
		this.time=time;
		this.phaser=phaser;
	}
	
	/**
	 * Main method of the task. Executes three phases. In each phase, sleeps
	 * the thread the number of seconds specified by the time attribute.
	 */
	@Override
	public void run() {
		/*
		 * Arrive to the phaser
		 */
		phaser.arrive();
		/*
		 * Phase 1
		 */
		System.out.printf("%s: Entering phase 1.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 1.\n",Thread.currentThread().getName());
		/*
		 * End of Phase 1
		 */
		phaser.arriveAndAwaitAdvance();
		/*
		 * Phase 2
		 */
		System.out.printf("%s: Entering phase 2.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 2.\n",Thread.currentThread().getName());
		/*
		 * End of Phase 2
		 */
		phaser.arriveAndAwaitAdvance();
		/*
		 * Phase 3
		 */
		System.out.printf("%s: Entering phase 3.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 3.\n",Thread.currentThread().getName());
		/*
		 * End of Phase 3
		 */
		phaser.arriveAndDeregister();
	}
}

//=*=*=*=*
//./Chapter_8/ch8_recipe02/src/com/packtpub/java7/concurrency/chapter8/recipe04/core/Main.java
package com.packtpub.java7.concurrency.chapter8.recipe04.core;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter8.recipe04.task.Task;

/**
 * Maîn class of the example. Creates a Phaser with three participants and
 * Three task objects. Write information about the evolution of the Phaser
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Create a new Phaser for three participants
		 */
		Phaser phaser=new Phaser(3);
		
		/*
		 * Create and launch three tasks
		 */
		for (int i=0; i<3; i++) {
			Task task=new Task(i+1, phaser);
			Thread thread=new Thread(task);
			thread.start();
		}
		
		/*
		 * Write information about the Phaser
		 */
		for (int i=0; i<10; i++) {
			System.out.printf("********************\n");
			System.out.printf("Main: Phaser Log\n");
			System.out.printf("Main: Phaser: Phase: %d\n",phaser.getPhase());
			System.out.printf("Main: Phaser: Registered Parties: %d\n",phaser.getRegisteredParties());
			System.out.printf("Main: Phaser: Arrived Parties: %d\n",phaser.getArrivedParties());
			System.out.printf("Main: Phaser: Unarrived Parties: %d\n",phaser.getUnarrivedParties());
			System.out.printf("********************\n");

			TimeUnit.SECONDS.sleep(1);
		}
	}
}

//=*=*=*=*
//./Chapter_4/ch4_recipe11/src/com/packtpub/java7/concurrency/chapter4/recipe12/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe12.task;

import java.util.concurrent.TimeUnit;

/**
 * This class implements a task executed in this example. It only waits
 * a random perior of time
 *
 */
public class Task implements Runnable{

	/**
	 * Name of the task
	 */
	private String name;

	/**
	 * Constructor of the class. It initializes the attributes of the class
	 * @param name The name of the task
	 */
	public Task(String name){
		this.name=name;
	}
	
	/**
	 * Main method of the task. Waits a random period of time
	 */
	@Override
	public void run() {
		System.out.printf("Task %s: Starting\n",name);
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		System.out.printf("Task %s: Ending\n",name);
	}

	/**
	 * Returns the name of the task
	 */
	public String toString() {
		return name;
	}
	
}

//=*=*=*=*
//./Chapter_4/ch4_recipe11/src/com/packtpub/java7/concurrency/chapter4/recipe12/task/RejectedTaskController.java
package com.packtpub.java7.concurrency.chapter4.recipe12.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class implements the handler for the rejected tasks. Implements
 * the RejectedExecutionHandler interface and will be called for each task
 * sent to an executor after it was finished using the shutdown() method 
 *
 */
public class RejectedTaskController implements RejectedExecutionHandler {

	/**
	 * Method that will be executed for each rejected task
	 * @param r Task that has been rejected
	 * @param executor Executor that has rejected the task
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		System.out.printf("RejectedTaskController: The task %s has been rejected\n",r.toString());
		System.out.printf("RejectedTaskController: %s\n",executor.toString());
		System.out.printf("RejectedTaskController: Terminating: %s\n",executor.isTerminating());
		System.out.printf("RejectedTaksController: Terminated: %s\n",executor.isTerminated());
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe11/src/com/packtpub/java7/concurrency/chapter4/recipe12/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe12.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.packtpub.java7.concurrency.chapter4.recipe12.task.RejectedTaskController;
import com.packtpub.java7.concurrency.chapter4.recipe12.task.Task;


/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create the controller for the Rejected tasks
		RejectedTaskController controller=new RejectedTaskController();
		// Create the executor and establish the controller for the Rejected tasks
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);
		
		// Lauch three tasks
		System.out.printf("Main: Starting.\n");
		for (int i=0; i<3; i++) {
			Task task=new Task("Task"+i);
			executor.submit(task);
		}
		
		// Shutdown the executor
		System.out.printf("Main: Shuting down the Executor.\n");
		executor.shutdown();

		// Send another task
		System.out.printf("Main: Sending another Task.\n");
		Task task=new Task("RejectedTask");
		executor.submit(task);
		
		// The program ends
		System.out.printf("Main: End.\n");
		
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe1/task/Server.java
package com.packtpub.java7.concurrency.chapter4.recipe1.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class simulates a server, for example, a web server, that receives
 * requests and uses a ThreadPoolExecutor to execute those requests
 *
 */
public class Server {

	/**
	 * ThreadPoolExecutors to manage the execution of the request
	 */
	private ThreadPoolExecutor executor;
	
	/**
	 * Constructor of the class. Creates the executor object
	 */
	public Server(){
		executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(5);
	}
	
	/**
	 * This method is called when a request to the server is made. The 
	 * server uses the executor to execute the request that it receives
	 * @param task The request made to the server
	 */
	public void executeTask(Task task){
		System.out.printf("Server: A new task has arrived\n");
		executor.execute(task);
		System.out.printf("Server: Pool Size: %d\n",executor.getPoolSize());
		System.out.printf("Server: Active Count: %d\n",executor.getActiveCount());
		System.out.printf("Server: Task Count: %d\n",executor.getTaskCount());
		System.out.printf("Server: Completed Tasks: %d\n",executor.getCompletedTaskCount());
	}

	/**
	 * This method shuts down the executor
	 */
	public void endServer() {
		executor.shutdown();
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe1/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe1.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a concurrent task
 *
 */
public class Task implements Runnable {

	/**
	 * The start date of the task
	 */
	private Date initDate;
	
	/**
	 * The name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the class. Initializes the name of the task
	 * @param name Name assigned to the task
	 */
	public Task(String name){
		initDate=new Date();
		this.name=name;
	}
	
	/**
	 * This method implements the execution of the task. Waits a random period of time and finish
	 */	
	@Override
	public void run() {
		System.out.printf("%s: Task %s: Created on: %s\n",Thread.currentThread().getName(),name,initDate);
		System.out.printf("%s: Task %s: Started on: %s\n",Thread.currentThread().getName(),name,new Date());
		
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: Task %s: Doing a task during %d seconds\n",Thread.currentThread().getName(),name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("%s: Task %s: Finished on: %s\n",Thread.currentThread().getName(),new Date(),name);
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe02/src/com/packtpub/java7/concurrency/chapter4/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe1.core;

import com.packtpub.java7.concurrency.chapter4.recipe1.task.Server;
import com.packtpub.java7.concurrency.chapter4.recipe1.task.Task;

/**
 * Main class of the example. Creates a server and 100 request of the Task class
 * that sends to the server
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the server
		Server server=new Server();
		
		// Send 100 request to the server and finish		
		for (int i=0; i<100; i++){
			Task task=new Task("Task "+i);
			server.executeTask(task);
		}
		
		server.endServer();

	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe03/src/com/packtpub/java7/concurrency/chapter4/recipe3/task/FactorialCalculator.java
package com.packtpub.java7.concurrency.chapter4.recipe3.task;

import java.util.concurrent.Callable;

/**
 * 
 * This class calculates the factorial of a number. It can be executed
 * in an executor because it implements de Callable interface.
 * The call method() will return an Interger
 *
 */
public class FactorialCalculator implements Callable<Integer> {

	/**
	 * Number to calculate the factorial
	 */
	private Integer number;
	
	/**
	 * Constructor of the class. Initializes the attributes
	 * @param number Number to calculate the factorial
	 */
	public FactorialCalculator(Integer number){
		this.number=number;
	}
	
	/**
	 * Method called by the executor to execute this task and calculate the factorial of a
	 * number
	 */
	@Override
	public Integer call() throws Exception {
		int num, result;
		
		num=number.intValue();
		result=1;
		
		// If the number is 0 or 1, return the 1 value
		if ((num==0)||(num==1)) {
			result=1;
		} else {
			// Else, calculate the factorial
			for (int i=2; i<=number; i++) {
				result*=i;
				Thread.sleep(20);
			}
		}
		System.out.printf("%s: %d\n",Thread.currentThread().getName(),result);
		// Return the value
		return result;
	}
}

//=*=*=*=*
//./Chapter_4/ch4_recipe03/src/com/packtpub/java7/concurrency/chapter4/recipe3/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe3.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.packtpub.java7.concurrency.chapter4.recipe3.task.FactorialCalculator;

/**
 * Main class of the example. Creates and execute ten FactorialCalculator tasks
 * in an executor controlling when they finish to write the results calculated
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a ThreadPoolExecutor with fixed size. It has a maximun of two threads
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		// List to store the Future objects that control the execution of  the task and
		// are used to obtain the results
		List<Future<Integer>> resultList=new ArrayList<>();

		// Create a random number generator
		Random random=new Random();
		// Create and send to the executor the ten tasks
		for (int i=0; i<10; i++){
			Integer number=new Integer(random.nextInt(10));
			FactorialCalculator calculator=new FactorialCalculator(number);
			Future<Integer> result=executor.submit(calculator);
			resultList.add(result);
		}
		
		// Wait for the finalization of the ten tasks
		do {
			System.out.printf("Main: Number of Completed Tasks: %d\n",executor.getCompletedTaskCount());
			for (int i=0; i<resultList.size(); i++) {
				Future<Integer> result=resultList.get(i);
				System.out.printf("Main: Task %d: %s\n",i,result.isDone());
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (executor.getCompletedTaskCount()<resultList.size());
		
		// Write the results
		System.out.printf("Main: Results\n");
		for (int i=0; i<resultList.size(); i++) {
			Future<Integer> result=resultList.get(i);
			Integer number=null;
			try {
				number=result.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.printf("Core: Task %d: %d\n",i,number);
		}
		
		// Shutdown the executor
		executor.shutdown();

	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe06/src/com/packtpub/java7/concurrency/chapter4/recipe7/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe7.task;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * This class implements the task of this example. Writes a
 * message to the console with the actual date and returns the
 * 'Hello, world' string
 *
 */
public class Task implements Callable<String> {

	/**
	 * Name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name Name of the task
	 */
	public Task(String name) {
		this.name=name;
	}
	
	/**
	 * Main method of the task. Writes a message to the console with
	 * the actual date and returns the 'Hello world' string
	 */
	@Override
	public String call() throws Exception {
		System.out.printf("%s: Starting at : %s\n",name,new Date());
		return "Hello, world";
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe06/src/com/packtpub/java7/concurrency/chapter4/recipe7/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe7.core;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe7.task.Task;

/**
 * Main class of the example. Send 5 tasks to an scheduled executor
 *   Task 0: Delay of 1 second
 *   Task 1: Delay of 2 seconds
 *   Task 2: Delay of 3 seconds
 *   Task 3: Delay of 4 seconds
 *   Task 4: Delay of 5 seconds 
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a ScheduledThreadPoolExecutor
		ScheduledExecutorService executor=(ScheduledExecutorService)Executors.newScheduledThreadPool(1);
		
		System.out.printf("Main: Starting at: %s\n",new Date());
		
		// Send the tasks to the executor with the specified delay
		for (int i=0; i<5; i++) {
			Task task=new Task("Task "+i);
			executor.schedule(task,i+1 , TimeUnit.SECONDS);
		}
		
		// Finish the executor
		executor.shutdown();
		
		// Waits for the finalization of the executor
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Writes the finalization message
		System.out.printf("Core: Ends at: %s\n",new Date());
	}
}

//=*=*=*=*
//./Chapter_4/ch4_recipe08/src/com/packtpub/java7/concurrency/chapter4/recipe9/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe9.task;

import java.util.concurrent.Callable;

/**
 * This class implements the task of the example. It simply writes a message
 * to the console every 100 milliseconds 
 *
 */
public class Task implements Callable<String> {

	/**
	 * Main method of the task. It has an infinite loop that writes a message to
	 * the console every 100 milliseconds
	 */
	@Override
	public String call() throws Exception {
		while (true){
			System.out.printf("Task: Test\n");
			Thread.sleep(100);
		}
	}
}

//=*=*=*=*
//./Chapter_4/ch4_recipe08/src/com/packtpub/java7/concurrency/chapter4/recipe9/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe9.core;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe9.task.Task;

/**
 * Main class of the example. Execute a task trough an executor, waits
 * 2 seconds and then cancel the task.
 *
 */
public class Main {

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create an executor
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		// Create a task
		Task task=new Task();
		
		System.out.printf("Main: Executing the Task\n");

		// Send the task to the executor
		Future<String> result=executor.submit(task);
		
		// Sleep during two seconds
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Cancel the task, finishing its execution
		System.out.printf("Main: Cancelling the Task\n");
		result.cancel(true);
		// Verify that the task has been cancelled
		System.out.printf("Main: Cancelled: %s\n",result.isCancelled());
		System.out.printf("Main: Done: %s\n",result.isDone());
		
		// Shutdown the executor
		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe01/src/com/packtpub/java7/concurrency/chapter4/recipe1/task/Server.java
package com.packtpub.java7.concurrency.chapter4.recipe1.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class simulates a server, for example, a web server, that receives
 * requests and uses a ThreadPoolExecutor to execute those requests
 *
 */
public class Server {
	
	/**
	 * ThreadPoolExecutors to manage the execution of the request
	 */
	private ThreadPoolExecutor executor;
	
	/**
	 * Constructor of the class. Creates the executor object
	 */
	public Server(){
		executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	/**
	 * This method is called when a request to the server is made. The 
	 * server uses the executor to execute the request that it receives
	 * @param task The request made to the server
	 */
	public void executeTask(Task task){
		System.out.printf("Server: A new task has arrived\n");
		executor.execute(task);
		System.out.printf("Server: Pool Size: %d\n",executor.getPoolSize());
		System.out.printf("Server: Active Count: %d\n",executor.getActiveCount());
		System.out.printf("Server: Completed Tasks: %d\n",executor.getCompletedTaskCount());
	}

	/**
	 * This method shuts down the executor
	 */
	public void endServer() {
		executor.shutdown();
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe01/src/com/packtpub/java7/concurrency/chapter4/recipe1/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe1.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a concurrent task 
 *
 */
public class Task implements Runnable {

	/**
	 * The start date of the task
	 */
	private Date initDate;
	/**
	 * The name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the class. Initializes the name of the task
	 * @param name name asigned to the task
	 */
	public Task(String name){
		initDate=new Date();
		this.name=name;
	}
	
	/**
	 * This method implements the execution of the task. Waits a random period of time and finish
	 */
	@Override
	public void run() {
		System.out.printf("%s: Task %s: Created on: %s\n",Thread.currentThread().getName(),name,initDate);
		System.out.printf("%s: Task %s: Started on: %s\n",Thread.currentThread().getName(),name,new Date());
		
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: Task %s: Doing a task during %d seconds\n",Thread.currentThread().getName(),name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("%s: Task %s: Finished on: %s\n",Thread.currentThread().getName(),name,new Date());
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe01/src/com/packtpub/java7/concurrency/chapter4/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe1.core;

import com.packtpub.java7.concurrency.chapter4.recipe1.task.Server;
import com.packtpub.java7.concurrency.chapter4.recipe1.task.Task;

/**
 * Main class of the example. Creates a server and 100 request of the Task class
 * that sends to the server
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the server
		Server server=new Server();
		
		// Send 100 request to the server and finish
		for (int i=0; i<100; i++){
			Task task=new Task("Task "+i);
			server.executeTask(task);
		}
		
		server.endServer();

	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe09/src/com/packtpub/java7/concurrency/chapter4/recipe10/task/ExecutableTask.java
package com.packtpub.java7.concurrency.chapter4.recipe10.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * This class implements the task of this example. It waits a random period of time
 *
 */
public class ExecutableTask implements Callable<String> {

	/**
	 * The name of the class
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name The name of the class
	 */
	public ExecutableTask(String name){
		this.name=name;
	}
	
	/**
	 * Main method of the task. It waits a random period of time and returns a message
	 */
	@Override
	public String call() throws Exception {
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: Waiting %d seconds for results.\n",this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
		}		
		return "Hello, world. I'm "+name;
	}

	/**
	 * This method returns the name of the task
	 * @return The name of the task
	 */
	public String getName(){
		return name;
	}
}

//=*=*=*=*
//./Chapter_4/ch4_recipe09/src/com/packtpub/java7/concurrency/chapter4/recipe10/task/ResultTask.java
package com.packtpub.java7.concurrency.chapter4.recipe10.task;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * This class manage the execution of a ExecutableTaks. Overrides
 * the done() method that is called when the task finish its execution 
 *
 */
public class ResultTask extends FutureTask<String> {

	/**
	 * Name of the ResultTask. It's initialized with the name of the
	 * ExecutableTask that manages
	 */
	private String name;
	
	/**
	 * Constructor of the Class. Override one of the constructor of its parent class 
	 * @param callable The task this object manages
	 */
	public ResultTask(Callable<String> callable) {
		super(callable);
		this.name=((ExecutableTask)callable).getName();
	}

	/**
	 * Method that is called when the task finish.
	 */
	@Override
	protected void done() {
		if (isCancelled()) {
			System.out.printf("%s: Has been cancelled\n",name);
		} else {
			System.out.printf("%s: Has finished\n",name);
		}
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe09/src/com/packtpub/java7/concurrency/chapter4/recipe10/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe10.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe10.task.ExecutableTask;
import com.packtpub.java7.concurrency.chapter4.recipe10.task.ResultTask;

/**
 * Main class of the example. Creates five tasks that wait a random period of time.
 * Waits 5 seconds and cancel all the tasks. Then, write the results of that tasks
 * that haven't been cancelled.
 *
 */
public class Main {

	/**
	 * Main method of the class.
	 * @param args
	 */
	public static void main(String[] args) {
		// Create an executor
		ExecutorService executor=(ExecutorService)Executors.newCachedThreadPool();
		
		//Create five tasks
		ResultTask resultTasks[]=new ResultTask[5];
		for (int i=0; i<5; i++) {
			ExecutableTask executableTask=new ExecutableTask("Task "+i);
			resultTasks[i]=new ResultTask(executableTask);
			executor.submit(resultTasks[i]);
		}
		
		// Sleep the thread five seconds
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// Cancel all the tasks. In the tasks that have finished before this moment, this
		// cancellation has no effects
		for (int i=0; i<resultTasks.length; i++) {
			resultTasks[i].cancel(true);
		}
		
		// Write the results of those tasks that haven't been cancelled
		for (int i=0; i<resultTasks.length; i++) {
			try {
				if (!resultTasks[i].isCancelled()){
					System.out.printf("%s\n",resultTasks[i].get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} 
		}
		// Finish the executor.
		executor.shutdown();

	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe05/src/com/packtpub/java7/concurrency/chapter4/recipe6/task/Result.java
package com.packtpub.java7.concurrency.chapter4.recipe6.task;

/**
 * This class stores the result generated by one task
 *
 */
public class Result {
	/**
	 * The name of the task that generates the result
	 */
	private String name;
	/**
	 * The value of the task that generates the result 
	 */
	private int value;
	
	/**
	 * Returns the name of the task
	 * @return Name of the task that generates the result
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Establish the name of the task
	 * @param name The name of the task that generates the result
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the value of the result
	 * @return The value of the result
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Establish the value of the result
	 * @param value The value of the result
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
}

//=*=*=*=*
//./Chapter_4/ch4_recipe05/src/com/packtpub/java7/concurrency/chapter4/recipe6/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe6.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * This class implements the task of this example. It waits during a random
 * period of time and then calculate the sum of five random numbers 
 *
 */
public class Task implements Callable<Result> {

	/**
	 * The name of the Task
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name Initializes the name of the task
	 */
	public Task(String name) {
		this.name=name;
	}
	
	/**
	 * Main method of the task. Waits during a random period of time and then
	 * calculates the sum of five random numbers
	 */
	@Override
	public Result call() throws Exception {
		// Writes a message to the console
		System.out.printf("%s: Staring\n",this.name);
		
		// Waits during a random period of time
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: Waiting %d seconds for results.\n",this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		// Calculates the sum of five random numbers
		int value=0;
		for (int i=0; i<5; i++){
			value+=(int)(Math.random()*100);

		}
		
		// Creates the object with the results
		Result result=new Result();
		result.setName(this.name);
		result.setValue(value);
		System.out.printf("%s: Ends\n",this.name);

		// Returns the result object
		return result;
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe05/src/com/packtpub/java7/concurrency/chapter4/recipe6/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe6.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.packtpub.java7.concurrency.chapter4.recipe6.task.Result;
import com.packtpub.java7.concurrency.chapter4.recipe6.task.Task;

/**
 * Main class of the example. Launch three tasks using the invokeAll() method
 * and then prints their results to the console
 *
 */
public class Main {

	public static void main(String[] args) {

		// Create an executor
		ExecutorService executor=(ExecutorService)Executors.newCachedThreadPool();

		// Create three tasks and stores them in a List
		List<Task> taskList=new ArrayList<>();
		for (int i=0; i<3; i++){
			Task task=new Task("Task-"+i);
			taskList.add(task);
		}

		// Call the invokeAll() method
		List<Future<Result>>resultList=null;
		try {
			resultList=executor.invokeAll(taskList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Finish the executor
		executor.shutdown();
		
		// Writes the results to the console
		System.out.printf("Core: Printing the results\n");
		for (int i=0; i<resultList.size(); i++){
			Future<Result> future=resultList.get(i);
			try {
				Result result=future.get();
				System.out.printf("%s: %s\n",result.getName(),result.getValue());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} 
		}
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe07/src/com/packtpub/java7/concurrency/chapter4/recipe8/task/Task.java
package com.packtpub.java7.concurrency.chapter4.recipe8.task;

import java.util.Date;

/**
 * This class implements the task of the example. Writes a message to
 * the console with the actual date. 
 * 
 *  Is used to explain the utilization of an scheduled executor to
 *  execute tasks periodically
 *
 */
public class Task implements Runnable {

	/**
	 * Name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name the name of the class
	 */
	public Task(String name) {
		this.name=name;
	}

	/**
	 * Main method of the example. Writes a message to the console with the actual
	 * date
	 */
	@Override
	public void run() {
		System.out.printf("%s: Executed at: %s\n",name,new Date());
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe07/src/com/packtpub/java7/concurrency/chapter4/recipe8/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe8.core;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe8.task.Task;

/**
 * Main class of the example. Send a task to the executor that will execute every
 * two seconds. Then, control the remaining time for the next execution of the task 
 *
 */
public class Main {

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a ScheduledThreadPoolExecutor
		ScheduledExecutorService executor=Executors.newScheduledThreadPool(1);
		System.out.printf("Main: Starting at: %s\n",new Date());

		// Create a new task and sends it to the executor. It will start with a delay of 1 second and then
		// it will execute every two seconds
		Task task=new Task("Task");
		ScheduledFuture<?> result=executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
		
		// Controlling the execution of tasks
		for (int i=0; i<10; i++){
			System.out.printf("Main: Delay: %d\n",result.getDelay(TimeUnit.MILLISECONDS));
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Finish the executor
		executor.shutdown();
		System.out.printf("Main: No more tasks at: %s\n",new Date());
		// Verify that the periodic tasks no is in execution after the executor shutdown()
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// The example finish
		System.out.printf("Main: Finished at: %s\n",new Date());
		
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe04/src/com/packtpub/java7/concurrency/chapter4/recipe5/task/TaskValidator.java
package com.packtpub.java7.concurrency.chapter4.recipe5.task;

import java.util.concurrent.Callable;

/**
 * This class encapsulate a user validation system to be executed as a Callable object.
 * If the user is validated, it returns the name of the validation system. If not,
 * it throws an Exception
 *
 */
public class TaskValidator implements Callable<String> {

	/**
	 * The user validator used to validate the user.
	 */
	private UserValidator validator;
	/**
	 * The name of the user
	 */
	private String user;
	/**
	 * The password of the user
	 */
	private String password;
	
	/**
	 * Constructor of the class
	 * @param validator The user validator system used to validate it 
	 * @param user The name of the user
	 * @param password The password of the user
	 */
	public TaskValidator(UserValidator validator, String user, String password){
		this.validator=validator;
		this.user=user;
		this.password=password;
	}
	
	/**
	 * Core method of the Callable interface. Tries to validate the user using the user
	 * validation system. If the user is validated, returns the name of the validation system. 
	 * If not, throws and Exception
	 * @return The name of the user validation system.
	 * @throws Exception An exception when the user is not validated
	 */
	@Override
	public String call() throws Exception {
		if (!validator.validate(user, password)) {
			System.out.printf("%s: The user has not been found\n",validator.getName());
			throw new Exception("Error validating user");
		}
		System.out.printf("%s: The user has been found\n",validator.getName());
		return validator.getName();
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe04/src/com/packtpub/java7/concurrency/chapter4/recipe5/task/UserValidator.java
package com.packtpub.java7.concurrency.chapter4.recipe5.task;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class implement a simulation of a user validation system. It suspend the Thread
 * a random period of time and then returns a random boolean value. We consider that it
 * returns the true value when the user is validated and the false value when it's not
 *
 */
public class UserValidator {
	
	/**
	 * The name of the validation system
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name The name of the user validation system
	 */
	public UserValidator(String name) {
		this.name=name;
	}
	
	/**
	 * Method that validates a user
	 * @param name Name of the user
	 * @param password Password of the user
	 * @return true if the user is validated and false if not
	 */
	public boolean validate(String name, String password) {
		// Create a new Random objects generator
		Random random=new Random();
		
		// Sleep the thread during a random period of time
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("Validator %s: Validating a user during %d seconds\n",this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			return false;
		}
		
		// Return a random boolean value
		return random.nextBoolean();
	}
	
	/**
	 * Return the name of the validation system
	 * @return The name of the validation system
	 */
	public String getName(){
		return name;
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe04/src/com/packtpub/java7/concurrency/chapter4/recipe5/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe5.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.packtpub.java7.concurrency.chapter4.recipe5.task.TaskValidator;
import com.packtpub.java7.concurrency.chapter4.recipe5.task.UserValidator;

/**
 * This is the main class of the example. Creates two user validation systems and execute
 * them in an Executor using the invokeAny() method. If the user is validated by one of the
 * user validation systems, then it shows a message. If both system don't validate the user,
 * the application proccess the ExecutionException throwed by the method
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Initialize the parameters of the user
		String username="test";
		String password="test";
		
		// Create two user validation objects
		UserValidator ldapValidator=new UserValidator("LDAP");
		UserValidator dbValidator=new UserValidator("DataBase");
		
		// Create two tasks for the user validation objects
		TaskValidator ldapTask=new TaskValidator(ldapValidator, username, password);
		TaskValidator dbTask=new TaskValidator(dbValidator,username,password);
		
		// Add the two tasks to a list of tasks
		List<TaskValidator> taskList=new ArrayList<>();
		taskList.add(ldapTask);
		taskList.add(dbTask);
		
		// Create a new Executor
		ExecutorService executor=(ExecutorService)Executors.newCachedThreadPool();
		String result;
		try {
			// Send the list of tasks to the executor and waits for the result of the first task 
			// that finish without throw and Exception. If all the tasks throw and Exception, the
			// method throws and ExecutionException.
			result = executor.invokeAny(taskList);
			System.out.printf("Main: Result: %s\n",result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		// Shutdown the Executor
		executor.shutdown();
		System.out.printf("Main: End of the Execution\n");
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe10/src/com/packtpub/java7/concurrency/chapter4/recipe11/task/ReportRequest.java
package com.packtpub.java7.concurrency.chapter4.recipe11.task;

import java.util.concurrent.CompletionService;

/**
 * This class represents every actor that can request a report. For this example,
 * it simply create three ReportGenerator objects and execute them through a 
 * CompletionService
 *
 */
public class ReportRequest implements Runnable {

	/**
	 * Name of this ReportRequest
	 */
	private String name;
	
	/**
	 * CompletionService used for the execution of the ReportGenerator tasks
	 */
	private CompletionService<String> service;
	
	/**
	 * Constructor of the class. Initializes the parameters
	 * @param name Name of the ReportRequest
	 * @param service Service used for the execution of tasks
	 */
	public ReportRequest(String name, CompletionService<String> service){
		this.name=name;
		this.service=service;
	}

	/**
	 * Main method of the class. Create three ReportGenerator tasks and executes them
	 * through a CompletionService
	 */
	@Override
	public void run() {
			ReportGenerator reportGenerator=new ReportGenerator(name, "Report");
			service.submit(reportGenerator);
	}

}

//=*=*=*=*
//./Chapter_4/ch4_recipe10/src/com/packtpub/java7/concurrency/chapter4/recipe11/task/ReportGenerator.java
package com.packtpub.java7.concurrency.chapter4.recipe11.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * This class simulates the generation of a report. Is a Callable
 * object that will be executed by the executor inside a 
 * CompletionService
 *
 */
public class ReportGenerator implements Callable<String> {

	/**
	 * The sender of the report
	 */
	private String sender;
	/**
	 * The title of the report
	 */
	private String title;
	
	/**
	 * Constructor of the class. Initializes the two attributes
	 * @param sender The sender of the report
	 * @param title The title of the report
	 */
	public ReportGenerator(String sender, String title){
		this.sender=sender;
		this.title=title;
	}

	/**
	 * Main method of the ReportGenerator. Waits a random period of time
	 * and then generates the report as a String.
	 */
	@Override
	public String call() throws Exception {
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s_%s: ReportGenerator: Generating a report during %d seconds\n",this.sender,this.title,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String ret=sender+": "+title;
		return ret;
	}
	
}

//=*=*=*=*
//./Chapter_4/ch4_recipe10/src/com/packtpub/java7/concurrency/chapter4/recipe11/task/ReportProcessor.java
package com.packtpub.java7.concurrency.chapter4.recipe11.task;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This class will take the results of the ReportGenerator tasks executed through
 * a CompletinoService
 *
 */
public class ReportProcessor implements Runnable {

	/**
	 * CompletionService that executes the ReportGenerator tasks
	 */
	private CompletionService<String> service;
	/**
	 * Variable to store the status of the Object. It will executes until the variable
	 * takes the true value
	 */
	private boolean end;
	
	/**
	 * Constructor of the class. It initializes the attributes of the class
	 * @param service The CompletionService used to execute the ReportGenerator tasks
	 */
	public ReportProcessor (CompletionService<String> service){
		this.service=service;
		end=false;
	}

	/**
	 * Main method of the class. While the variable end is false, it
	 * calls the poll method of the CompletionService and waits 20 seconds
	 * for the end of a ReportGenerator task
	 */
	@Override
	public void run() {
		while (!end){
			try {
				Future<String> result=service.poll(20, TimeUnit.SECONDS);
				if (result!=null) {
					String report=result.get();
					System.out.printf("ReportReceiver: Report Recived: %s\n",report);
				}			
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("ReportSender: End\n");
	}

	/**
	 * Method that establish the value of the end attribute
	 * @param end New value of the end attribute.
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}
	
	
}

//=*=*=*=*
//./Chapter_4/ch4_recipe10/src/com/packtpub/java7/concurrency/chapter4/recipe11/core/Main.java
package com.packtpub.java7.concurrency.chapter4.recipe11.core;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter4.recipe11.task.ReportProcessor;
import com.packtpub.java7.concurrency.chapter4.recipe11.task.ReportRequest;

/**
 * Main class of the example creates all the necessary objects and throws the tasks
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the executor and thee CompletionService using that executor
		ExecutorService executor=(ExecutorService)Executors.newCachedThreadPool();
		CompletionService<String> service=new ExecutorCompletionService<>(executor);

		// Crete two ReportRequest objects and two Threads to execute them
		ReportRequest faceRequest=new ReportRequest("Face", service);
		ReportRequest onlineRequest=new ReportRequest("Online", service);
		Thread faceThread=new Thread(faceRequest);
		Thread onlineThread=new Thread(onlineRequest);
		
		// Create a ReportSender object and a Thread to execute  it
		ReportProcessor processor=new ReportProcessor(service);
		Thread senderThread=new Thread(processor);
		
		// Start the Threads
		System.out.printf("Main: Starting the Threads\n");
		faceThread.start();
		onlineThread.start();
		senderThread.start();
		
		// Wait for the end of the ReportGenerator tasks
		try {
			System.out.printf("Main: Waiting for the report generators.\n");
			faceThread.join();
			onlineThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Shutdown the executor
		System.out.printf("Main: Shuting down the executor.\n");
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// End the execution of the ReportSender
		processor.setEnd(true);
		System.out.printf("Main: Ends\n");

	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe03/src/com/packtpub/java7/concurrency/chapter6/recipe04/task/Event.java
package com.packtpub.java7.concurrency.chapter6.recipe04.task;

/**
 * This class stores the attributes of an event. Its thread
 * and is priority. Implements the comparable interface to
 * help the priority queue to decide which event has more priority 
 *
 */
public class Event implements Comparable<Event> {
	
	/**
	 * Number of the thread that generates the event
	 */
	private int thread;
	/**
	 * Priority of the thread
	 */
	private int priority;
	
	/**
	 * Constructor of the thread. It initializes its attributes
	 * @param thread Number of the thread that generates the event
	 * @param priority Priority of the event
	 */
	public Event(int thread, int priority){
		this.thread=thread;
		this.priority=priority;
	}
	
	/**
	 * Method that returns the number of the thread that generates the
	 * event
	 * @return The number of the thread that generates the event
	 */
	public int getThread() {
		return thread;
	}
	
	/**
	 * Method that returns the priority of the event
	 * @return The priority of the event
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Method that compares two events and decide which has more priority
	 */
	@Override
	public int compareTo(Event e) {
		if (this.priority>e.getPriority()) {
			return -1;
		} else if (this.priority<e.getPriority()) {
			return 1; 
		} else {
			return 0;
		}
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe03/src/com/packtpub/java7/concurrency/chapter6/recipe04/task/Task.java
package com.packtpub.java7.concurrency.chapter6.recipe04.task;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class implements a generator of events. It generates
 * 1000 events and stores them in a priory queue
 *
 */
public class Task implements Runnable {

	/**
	 * Id of the task
	 */
	private int id;

	/**
	 * Priority queue to store the events
	 */
	private PriorityBlockingQueue<Event> queue;
	
	/**
	 * Constructor of the class. It initializes its attributes
	 * @param id Id of the task 
	 * @param queue Priority queue to store the events
	 */
	public Task(int id, PriorityBlockingQueue<Event> queue) {
		this.id=id;
		this.queue=queue;
	}
	
	/**
	 * Main method of the task. It generates 1000 events and store
	 * them in the queue
	 */
	@Override
	public void run() {
		for (int i=0; i<1000; i++){
			Event event=new Event(id,i);
			queue.add(event);
		}
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe03/src/com/packtpub/java7/concurrency/chapter6/recipe04/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe04.core;

import java.util.concurrent.PriorityBlockingQueue;

import com.packtpub.java7.concurrency.chapter6.recipe04.task.Event;
import com.packtpub.java7.concurrency.chapter6.recipe04.task.Task;

/**
 * Main class of the example. Executes five threads that
 * store their events in a common priority queue and writes
 * them in the console to verify the correct operation of the
 * PriorityBlockingQueue class
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Priority queue to store the events
		 */
		PriorityBlockingQueue<Event> queue=new PriorityBlockingQueue<>();
		
		/*
		 * An array to store the five Thread objects
		 */
		Thread taskThreads[]=new Thread[5];
		
		/*
		 * Create the five threads to execute five tasks
		 */
		for (int i=0; i<taskThreads.length; i++){
			Task task=new Task(i,queue);
			taskThreads[i]=new Thread(task);
		}
		
		/*
		 * Start the five threads
		 */
		for (int i=0; i<taskThreads.length; i++) {
			taskThreads[i].start();
		}
		
		/*
		 * Wait for the finalization of the five threads
		 */
		for (int i=0; i<taskThreads.length; i++) {
			try {
				taskThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Write the events in the console 
		 */
		System.out.printf("Main: Queue Size: %d\n",queue.size());
		for (int i=0; i<taskThreads.length*1000; i++){
			Event event=queue.poll();
			System.out.printf("Thread %s: Priority %d\n",event.getThread(),event.getPriority());
		}
		System.out.printf("Main: Queue Size: %d\n",queue.size());
		System.out.printf("Main: End of the program\n");
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe08/src/com/packtpub/java7/concurrency/chapter6/recipe09/task/Incrementer.java
package com.packtpub.java7.concurrency.chapter6.recipe09.task;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * This task implements an incrementer. It increments by 1
 * all the elements of an array
 *
 */
public class Incrementer implements Runnable {

	/**
	 * Array that store the elements to increment
	 */
	private AtomicIntegerArray vector; 
	
	/**
	 * Constructor of the class
	 * @param vector Array to store the elements to increment
	 */
	public Incrementer(AtomicIntegerArray vector) {
		this.vector=vector;
	}
	
	/**
	 * Main method of the task. Increment all the elements of the
	 * array
	 */
	@Override
	public void run() {
		
		for (int i=0; i<vector.length(); i++){
			vector.getAndIncrement(i);
		}
		
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe08/src/com/packtpub/java7/concurrency/chapter6/recipe09/task/Decrementer.java
package com.packtpub.java7.concurrency.chapter6.recipe09.task;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * This task implements a decrementer. It decrements by 1 unit all the
 * elements of an array
 *
 */
public class Decrementer implements Runnable {

	/**
	 * The array to decrement the elements
	 */
	private AtomicIntegerArray vector; 
	
	/**
	 * Constructor of the class
	 * @param vector The array to decrement is elements
	 */
	public Decrementer(AtomicIntegerArray vector) {
		this.vector=vector;
	}
	
	/**
	 * Main method of the class. It decrements all the elements of the 
	 * array
	 */
	@Override
	public void run() {
		for (int i=0; i<vector.length(); i++) {
			vector.getAndDecrement(i);
		}	
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe08/src/com/packtpub/java7/concurrency/chapter6/recipe09/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe09.core;

import java.util.concurrent.atomic.AtomicIntegerArray;

import com.packtpub.java7.concurrency.chapter6.recipe09.task.Decrementer;
import com.packtpub.java7.concurrency.chapter6.recipe09.task.Incrementer;

/**
 * Main class of the example. Execute 100 incrementers and 100 decrementers
 * and checks that the results are the expected
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final int THREADS=100;
		/**
		 * Atomic array whose elements will be incremented and decremented
		 */
		AtomicIntegerArray vector=new AtomicIntegerArray(1000);
		/*
		 * An incrementer task
		 */
		Incrementer incrementer=new Incrementer(vector);
		/*
		 * A decrementer task
		 */
		Decrementer decrementer=new Decrementer(vector);
		
		/*
		 * Create and execute 100 incrementer and 100 decrementer tasks
		 */
		Thread threadIncrementer[]=new Thread[THREADS];
		Thread threadDecrementer[]=new Thread[THREADS];
		for (int i=0; i<THREADS; i++) {
			threadIncrementer[i]=new Thread(incrementer);
			threadDecrementer[i]=new Thread(decrementer);
			
			threadIncrementer[i].start();
			threadDecrementer[i].start();
		}
		
		/*
		 * Wait for the finalization of all the tasks
		 */
		for (int i=0; i<THREADS; i++) {
			try {
				threadIncrementer[i].join();
				threadDecrementer[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Write the elements different from 0
		 */
		for (int i=0; i<vector.length(); i++) {
			if (vector.get(i)!=0) {
				System.out.println("Vector["+i+"] : "+vector.get(i));
			}
		}
		
		System.out.println("Main: End of the example");
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe02/src/com/packtpub/java7/concurrency/chapter6/recipe02/task/Client.java
package com.packtpub.java7.concurrency.chapter6.recipe02.task;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable{

	private LinkedBlockingDeque<String> requestList;
	
	public Client (LinkedBlockingDeque<String> requestList) {
		this.requestList=requestList;
	}
	
	@Override
	public void run() {
		for (int i=0; i<3; i++) {
			for (int j=0; j<5; j++) {
				StringBuilder request=new StringBuilder();
				request.append(i);
				request.append(":");
				request.append(j);
				try {
					requestList.put(request.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.printf("Client: %s at %s.\n",request,new Date());
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Client: End.\n");
	}
	

}

//=*=*=*=*
//./Chapter_6/ch6_recipe02/src/com/packtpub/java7/concurrency/chapter6/recipe02/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe02.core;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter6.recipe02.task.Client;

/**
 * Main class of the example. First, execute 100 AddTask objects to
 * add 1000000 elements to the list and the execute 100 PollTask objects
 * to delete all those elements.
 *
 */
public class Main {

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// Create a ConcurrentLinkedDeque to work with it in the example
		LinkedBlockingDeque<String> list=new LinkedBlockingDeque<>(3);
		
		Client client=new Client(list);
		Thread thread=new Thread(client);
		thread.start();
		
		for (int i=0; i<5 ; i++) {
			for (int j=0; j<3; j++) {
				String request=list.take();
				System.out.printf("Main: Request: %s at %s. Size: %d\n",request,new Date(),list.size());
			}
			TimeUnit.MILLISECONDS.sleep(300);
		}
		
		System.out.printf("Main: End of the program.\n");
	
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe06/src/com/packtpub/java7/concurrency/chapter6/recipe07/task/TaskLocalRandom.java
package com.packtpub.java7.concurrency.chapter6.recipe07.task;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Task that generates random numbers
 *
 */
public class TaskLocalRandom implements Runnable {

	/**
	 * Constructor of the class. Initializes the randoom number generator
	 * for this task
	 */
	public TaskLocalRandom() {
		ThreadLocalRandom.current();
	}
	
	/**
	 * Main method of the class. Generate 10 random numbers and write them
	 * in the console
	 */
	@Override
	public void run() {
		String name=Thread.currentThread().getName();
		for (int i=0; i<10; i++){
			System.out.printf("%s: %d\n",name,ThreadLocalRandom.current().nextInt(10));
		}
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe06/src/com/packtpub/java7/concurrency/chapter6/recipe07/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe07.core;

import com.packtpub.java7.concurrency.chapter6.recipe07.task.TaskLocalRandom;

/**
 * Main class of the example. It creates three task and execute them
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		 * Create an array to store the threads 
		 */
		Thread threads[]=new Thread[3];
		
		/*
		 * Launch three tasks
		 */
		for (int i=0; i<threads.length; i++) {
			TaskLocalRandom task=new TaskLocalRandom();
			threads[i]=new Thread(task);
			threads[i].start();
		}

	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe05/task/Event.java
package com.packtpub.java7.concurrency.chapter6.recipe05.task;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * This class implements an event for a delayed queue.
 *
 */
public class Event implements Delayed {

	/**
	 * Date when we want to activate the event
	 */
	private Date startDate;
	
	/**
	 * Constructor of the class
	 * @param startDate Date when we want to activate the event
	 */
	public Event (Date startDate) {
		this.startDate=startDate;
	}
	
	/**
	 * Method to compare two events
	 */
	@Override
	public int compareTo(Delayed o) {
		long result=this.getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS);
		if (result<0) {
			return -1;
		} else if (result>0) {
			return 1;
		}
		return 0;
	}

	/**
	 * Method that returns the remaining time to the activation of the event
	 */
	@Override
	public long getDelay(TimeUnit unit) {	
		Date now=new Date();
		long diff=startDate.getTime()-now.getTime();
		return unit.convert(diff,TimeUnit.MILLISECONDS);
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe05/task/Task.java
package com.packtpub.java7.concurrency.chapter6.recipe05.task;

import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * This class implements a taks that store events in a delayed queue
 *
 */
public class Task implements Runnable {

	/**
	 * Id of the task
	 */
	private int id;
	
	/**
	 * Delayed queue to store the events
	 */
	private DelayQueue<Event> queue;
	
	/**
	 * Constructor of the class. It initializes its attributes
	 * @param id Id of the task
	 * @param queue Delayed queue to store the events
	 */
	public Task(int id, DelayQueue<Event> queue) {
		this.id=id;
		this.queue=queue;
	}
	

	/**
	 * Main method of the task. It generates 100 events with the
	 * same activation time. The activation time will be the execution
	 * time of the thread plus the id of the thread seconds
	 */
	@Override
	public void run() {

		Date now=new Date();
		Date delay=new Date();
		delay.setTime(now.getTime()+(id*1000));
		
		System.out.printf("Thread %s: %s\n",id,delay);
		
		for (int i=0; i<100; i++) {
			Event event=new Event(delay);
			queue.add(event);
		}
	}
	
}

//=*=*=*=*
//./Chapter_6/ch6_recipe04/src/com/packtpub/java7/concurrency/chapter6/recipe05/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe05.core;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter6.recipe05.task.Event;
import com.packtpub.java7.concurrency.chapter6.recipe05.task.Task;

/**
 * Main method of the example. Execute five tasks and then
 * take the events of the delayed queue when they are activated
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * Delayed queue to store the events
		 */
		DelayQueue<Event> queue=new DelayQueue<>();
		
		/*
		 * An array to store the Thread objects that execute the tasks 
		 */
		Thread threads[]=new Thread[5];
		
		/*
		 * Create the five tasks
		 */
		for (int i=0; i<threads.length; i++){
			Task task=new Task(i+1, queue);
			threads[i]=new Thread(task);
		}

		/*
		 * Execute the five tasks
		 */
		for (int i=0; i<threads.length; i++) {
			threads[i].start();
		}
		
		/*
		 * Wait for the finalization of the five tasks 
		 */
		for (int i=0; i<threads.length; i++) {
			threads[i].join();
		}
		
		/*
		 * Write the results to the console
		 */
		do {
			int counter=0;
			Event event;
			do {
				event=queue.poll();
				if (event!=null) counter++;
			} while (event!=null);
			System.out.printf("At %s you have read %d events\n",new Date(),counter);
			TimeUnit.MILLISECONDS.sleep(500);
		} while (queue.size()>0);
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe05/src/com/packtpub/java7/concurrency/chapter6/recipe06/task/Task.java
package com.packtpub.java7.concurrency.chapter6.recipe06.task;

import java.util.concurrent.ConcurrentSkipListMap;

import com.packtpub.java7.concurrency.chapter6.recipe06.util.Contact;

/**
 * This class implements a task that store contacts in a navigable map
 *
 */
public class Task implements Runnable {

	/**
	 * Navigable map to store the contacts
	 */
	private ConcurrentSkipListMap<String, Contact> map;
	
	/**
	 * Id of the task
	 */
	private String id;
	
	/**
	 * Constructor of the class that initializes its events
	 * @param map Navigable map to store the events
	 * @param id Id of the task
	 */
	public Task (ConcurrentSkipListMap<String, Contact> map, String id) {
		this.id=id;
		this.map=map;
	}

	/**
	 * Main method of the task. Generates 1000 contact objects and
	 * store them in the navigable map
	 */
	@Override
	public void run() {
		for (int i=0; i<1000; i++) {
			Contact contact=new Contact(id, String.valueOf(i+1000));
			map.put(id+contact.getPhone(), contact);
		}		
	}
	
	
}

//=*=*=*=*
//./Chapter_6/ch6_recipe05/src/com/packtpub/java7/concurrency/chapter6/recipe06/util/Contact.java
package com.packtpub.java7.concurrency.chapter6.recipe06.util;

/**
 * This class implements a Contact to store in the navigable map
 *
 */
public class Contact {

	/**
	 * Name of the contact
	 */
	private String name;
	
	/**
	 * Phone number of the contact
	 */
	private String phone;
	
	/**
	 * Constructor of the class
	 * @param name Name of the contact
	 * @param phone Phone number of the contact
	 */
	public Contact(String name, String phone) {
		this.name=name;
		this.phone=phone;
	}

	/**
	 * Method that returns the name of the contact
	 * @return The name of the contact
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method that returns the phone number of the contact
	 * @return
	 */
	public String getPhone() {
		return phone;
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe05/src/com/packtpub/java7/concurrency/chapter6/recipe06/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe06.core;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.packtpub.java7.concurrency.chapter6.recipe06.task.Task;
import com.packtpub.java7.concurrency.chapter6.recipe06.util.Contact;

/**
 * Main class of the example. It executes twenty-five tasks that
 * store contacts in the navigable map and then shows part of the content
 * of that navigable map
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Create the navigable map
		 */
		ConcurrentSkipListMap<String, Contact> map;
		map=new ConcurrentSkipListMap<>();
		
		/*
		 * Create an array to store the 25 threads that execute
		 * the tasks
		 */
		Thread threads[]=new Thread[25];
		int counter=0;
		
		/*
		 * Execute the 25 tasks
		 */
		for (char i='A'; i<'Z'; i++) {
			Task task=new Task(map, String.valueOf(i));
			threads[counter]=new Thread(task);
			threads[counter].start();
			counter++;
		}
		
		/*
		 * Wait for the finalization of the threads
		 */
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Write the size of the map
		 */
		System.out.printf("Main: Size of the map: %d\n",map.size());
		
		/*
		 * Write the first element of the map
		 */
		Map.Entry<String, Contact> element;
		Contact contact;
		
		element=map.firstEntry();
		contact=element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n",contact.getName(),contact.getPhone());
		
		/*
		 * Write the last element of the map
		 */
		element=map.lastEntry();
		contact=element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n",contact.getName(),contact.getPhone());

		/*
		 * Write a subset of the map 
		 */
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String, Contact> submap=map.subMap("A1996", "B1002");
		do {
			element=submap.pollFirstEntry();
			if (element!=null) {
				contact=element.getValue();
				System.out.printf("%s: %s\n",contact.getName(),contact.getPhone());
			}
		} while (element!=null);
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe07/src/com/packtpub/java7/concurrency/chapter6/recipe08/task/Account.java
package com.packtpub.java7.concurrency.chapter6.recipe08.task;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This class simulate a bank account 
 *
 */
public class Account {

	/**
	 * Balance of the bank account
	 */
	private AtomicLong balance;
	
	public Account(){
		balance=new AtomicLong();
	}

	/**
	 * Returns the balance of the account
	 * @return the balance of the account
	 */
	public long getBalance() {
		return balance.get();
	}

	/**
	 * Establish the balance of the account
	 * @param balance the new balance of the account
	 */
	public void setBalance(long balance) {
		this.balance.set(balance);
	}
	
	/**
	 * Add an import to the balance of the account
	 * @param amount import to add to the balance
	 */
	public void addAmount(long amount) {
		this.balance.getAndAdd(amount);
	}
	
	/**
	 * Subtract an import to the balance of the account
	 * @param amount import to subtract to the balance
	 */
	public void subtractAmount(long amount) {
		this.balance.getAndAdd(-amount);
	}
	
}

//=*=*=*=*
//./Chapter_6/ch6_recipe07/src/com/packtpub/java7/concurrency/chapter6/recipe08/task/Company.java
package com.packtpub.java7.concurrency.chapter6.recipe08.task;

/**
 * This class simulates a company that pays a salary an
 * insert money into an account 
 *
 */
public class Company implements Runnable {

	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account the account affected by the operations
	 */
	public Company(Account account) {
		this.account=account;
	}
	
	/**
	 * Core method of the Runnable
	 */
	@Override
	public void run() {
		for (int i=0; i<10; i++){
			account.addAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe07/src/com/packtpub/java7/concurrency/chapter6/recipe08/task/Bank.java
package com.packtpub.java7.concurrency.chapter6.recipe08.task;

/**
 * This class simulates a bank or a cash dispenser that takes money
 * from an account
 *
 */
public class Bank implements Runnable {

	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account The account affected by the operations
	 */
	public Bank(Account account) {
		this.account=account;
	}
	
	
	/**
	 * Core method of the Runnable
	 */
	@Override
	public void run() {
		for (int i=0; i<10; i++){
			account.subtractAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe07/src/com/packtpub/java7/concurrency/chapter6/recipe08/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe08.core;

import com.packtpub.java7.concurrency.chapter6.recipe08.task.Account;
import com.packtpub.java7.concurrency.chapter6.recipe08.task.Bank;
import com.packtpub.java7.concurrency.chapter6.recipe08.task.Company;


/**
 * Main class of the example. It creates an account, a company and a bank
 * to work with the account. The final balance should be equal to the initial, but....
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a new account ...
		Account	account=new Account();
		// an initialize its balance to 1000
		account.setBalance(1000);
		
		// Creates a new Company and a Thread to run its task
		Company	company=new Company(account);
		Thread companyThread=new Thread(company);
		// Creates a new Bank and a Thread to run its task
		Bank bank=new Bank(account);
		Thread bankThread=new Thread(bank);
		
		// Prints the initial balance
		System.out.printf("Account : Initial Balance: %d\n",account.getBalance());
		
		// Starts the Threads
		companyThread.start();
		bankThread.start();

		try {
			// Wait for the finalization of the Threads
			companyThread.join();
			bankThread.join();
			// Print the final balance
			System.out.printf("Account : Final Balance: %d\n",account.getBalance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//=*=*=*=*
//./Chapter_6/ch6_recipe01/src/com/packtpub/java7/concurrency/chapter6/recipe01/task/AddTask.java
package com.packtpub.java7.concurrency.chapter6.recipe01.task;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Task that add 10000 elements to a ConcurrentListDeque
 *
 */
public class AddTask implements Runnable {

	/**
	 * List to add the elements
	 */
	private ConcurrentLinkedDeque<String> list;
	
	/**
	 * Constructor of the class
	 * @param list List to add the elements
	 */
	public AddTask(ConcurrentLinkedDeque<String> list) {
		this.list=list;
	}
	
	/**
	 * Main method of the class. Add 10000 elements to the list
	 * using the add() method that adds the element at the end of
	 * the list
	 */
	@Override
	public void run() {
		String name=Thread.currentThread().getName();
		for (int i=0; i<10000; i++){
			list.add(name+": Element "+i);
		}
	}

}

//=*=*=*=*
//./Chapter_6/ch6_recipe01/src/com/packtpub/java7/concurrency/chapter6/recipe01/task/PollTask.java
package com.packtpub.java7.concurrency.chapter6.recipe01.task;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Task that delete 10000 elements from a ConcurrentListDeque
 *
 */
public class PollTask implements Runnable {

	/**
	 * List to delete the elements
	 */
	private ConcurrentLinkedDeque<String> list;
	
	/**
	 * Constructor of the class
	 * @param list List to delete the elements
	 */
	public PollTask(ConcurrentLinkedDeque<String> list) {
		this.list=list;
	}
	
	/**
	 * Main method of the task. Deletes 10000 elements from the
	 * list using the pollFirst() that deletes the first element of the
	 * list and pollLast() that deletes the last element of the list
	 */
	@Override
	public void run() {
		for (int i=0; i<5000; i++) {
			list.pollFirst();
			list.pollLast();
		}
	}
	
	

}

//=*=*=*=*
//./Chapter_6/ch6_recipe01/src/com/packtpub/java7/concurrency/chapter6/recipe01/core/Main.java
package com.packtpub.java7.concurrency.chapter6.recipe01.core;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.packtpub.java7.concurrency.chapter6.recipe01.task.AddTask;
import com.packtpub.java7.concurrency.chapter6.recipe01.task.PollTask;

/**
 * Main class of the example. First, execute 100 AddTask objects to
 * add 1000000 elements to the list and the execute 100 PollTask objects
 * to delete all those elements.
 *
 */
public class Main {

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		// Create a ConcurrentLinkedDeque to work with it in the example
		ConcurrentLinkedDeque<String> list=new ConcurrentLinkedDeque<>();
		// Create an Array of 100 threads
		Thread threads[]=new Thread[100];

		// Create 100 AddTask objects and execute them as threads
		for (int i=0; i<threads.length; i++){
			AddTask task=new AddTask(list);
			threads[i]=new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d AddTask threads have been launched\n",threads.length);
		
		// Wait for the finalization of the threads
		for (int i=0; i<threads.length; i++) {
				threads[i].join();
		}
		
		// Write to the console the size of the list
		System.out.printf("Main: Size of the List: %d\n",list.size());
		
		// Create 100 PollTask objects and execute them as threads
		for (int i=0; i<threads.length; i++){
			PollTask task=new PollTask(list);
			threads[i]=new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d PollTask threads have been launched\n",threads.length);
		
		// Wait for the finalization of the threads
		for (int i=0; i<threads.length; i++) {
			threads[i].join();
		}
		
		// Write to the console the size of the list
		System.out.printf("Main: Size of the List: %d\n",list.size());
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe2/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/TicketOffice2.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a ticket office. It sell or return tickets
 * for the two cinemas
 *
 */
public class TicketOffice2 implements Runnable {

	/**
	 * The cinema 
	 */
	private Cinema cinema;
	
	/**
	 * Constructor of the class
	 * @param cinema the cinema
	 */
	public TicketOffice2(Cinema cinema){
		this.cinema=cinema;
	}
	
	/**
	 * Core method of this ticket office. Simulates selling and returning tickets
	 */
	@Override
	public void run() {
		cinema.sellTickets2(2);
		cinema.sellTickets2(4);
		cinema.sellTickets1(2);
		cinema.sellTickets1(1);
		cinema.returnTickets2(2);
		cinema.sellTickets1(3);
		cinema.sellTickets2(2);
		cinema.sellTickets1(2);
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe2/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/TicketOffice1.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a ticket office. It sell or return tickets
 * for the two cinemas
 *
 */
public class TicketOffice1 implements Runnable {

	/**
	 * The cinema 
	 */
	private Cinema cinema;
	
	/**
	 * Constructor of the class
	 * @param cinema the cinema
	 */
	public TicketOffice1 (Cinema cinema) {
		this.cinema=cinema;
	}
	
	/**
	 * Core method of this ticket office. Simulates selling and returning tickets
	 */
	@Override
	public void run() {
		cinema.sellTickets1(3);
		cinema.sellTickets1(2);
		cinema.sellTickets2(2);
		cinema.returnTickets1(3);
		cinema.sellTickets1(5);
		cinema.sellTickets2(2);
		cinema.sellTickets2(2);
		cinema.sellTickets2(2);
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe2/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Cinema.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

public class Cinema {
	
	/**
	 * This two variables store the vacancies in two cinemas
	 */
	private long vacanciesCinema1;
	private long vacanciesCinema2;

	/**
	 * Two objects for the synchronization. ControlCinema1 synchronizes the
	 * access to the vacancesCinema1 attribute and controlCinema2 synchronizes
	 * the access to the vacanciesCinema2 attribute.
	 */
	private final Object controlCinema1, controlCinema2;
	
	/**
	 * Constructor of the class. Initializes the objects
	 */
	public Cinema(){
		controlCinema1=new Object();
		controlCinema2=new Object();
		vacanciesCinema1=20;
		vacanciesCinema2=20;
	}
	
	/**
	 * This method implements the operation of sell tickets for the cinema 1
	 * @param number number of tickets sold
	 * @return true if the tickets are sold, false if there is no vacancies
	 */
	public boolean sellTickets1 (int number) {
		synchronized (controlCinema1) {
			if (number<vacanciesCinema1) {
				vacanciesCinema1-=number;
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * This method implements the operation of sell tickets for the cinema 2
	 * @param number number of tickets sold
	 * @return true if the tickets are sold, false if there is no vacancies
	 */
	public boolean sellTickets2 (int number){
		synchronized (controlCinema2) {
			if (number<vacanciesCinema2) {
				vacanciesCinema2-=number;
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * This method implements the operation of return tickets for the cinema 1
	 * @param number number of the tickets returned
	 * @return true
	 */
	public boolean returnTickets1 (int number) {
		synchronized (controlCinema1) {
			vacanciesCinema1+=number;
			return true;
		}
	}

	/**
	 * This method implements the operation of return tickets for the cinema 1
	 * @param number number of the tickets returned
	 * @return true
	 */
	public boolean returnTickets2 (int number) {
		synchronized (controlCinema2) {
			vacanciesCinema2+=number;
			return true;
		}
	}

	/**
	 * Return the vacancies in the cinema 1
	 * @return the vacancies in the cinema 1
	 */
	public long getVacanciesCinema1() {
		return vacanciesCinema1;
	}

	/**
	 * Return the vacancies in the cinema 2
	 * @return the vacancies in the cinema 2
	 */
	public long getVacanciesCinema2() {
		return vacanciesCinema2;
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe2/src/com/packtpub/java7/concurrency/chapter2/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.Cinema;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.TicketOffice1;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.TicketOffice2;

/**
 * Core class of the example. Creates a cinema and two threads for
 * the ticket office. Run the threads to analyze the results obtained
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a Cinema
		Cinema cinema=new Cinema();
		
		// Creates a TicketOffice1 and a Thread to run it
		TicketOffice1 ticketOffice1=new TicketOffice1(cinema);
		Thread thread1=new Thread(ticketOffice1,"TicketOffice1");

		// Creates a TicketOffice2 and a Thread to run it
		TicketOffice2 ticketOffice2=new TicketOffice2(cinema);
		Thread thread2=new Thread(ticketOffice2,"TicketOffice2");
		
		// Starts the threads
		thread1.start();
		thread2.start();
		
		try {
			// Waits for the finalization of the threads
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Print the vacancies in the cinemas
		System.out.printf("Room 1 Vacancies: %d\n",cinema.getVacanciesCinema1());
		System.out.printf("Room 2 Vacancies: %d\n",cinema.getVacanciesCinema2());
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe3/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/EventStorage.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements an Event storage. Producers will storage
 * events in it and Consumers will process them. An event will
 * be a java.util.Date object
 *
 */
public class EventStorage {
   
	/**
	 * Maximum size of the storage
	 */
	private int maxSize;
	/**
	 * Storage of events
	 */
	private List<Date> storage;
	
	/**
	 * Constructor of the class. Initializes the attributes.
	 */
	public EventStorage(){
		maxSize=10;
		storage=new LinkedList<>();
	}
	
	/**
	 * This method creates and storage an event.
	 */
	public synchronized void set(){
			while (storage.size()==maxSize){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			storage.add(new Date());
			System.out.printf("Set: %d",storage.size());
			notify();
	}
	
	/**
	 * This method delete the first event of the storage.
	 */
	public synchronized void get(){
			while (storage.size()==0){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("Get: %d: %s",storage.size(),((LinkedList<?>)storage).poll());
			notify();
	}
	
}

//=*=*=*=*
//./Chapter_2/ch2_recipe3/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Consumer.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class implements a consumer of events.
 *
 */
public class Consumer implements Runnable {

	/**
	 * Store to work with
	 */
	private EventStorage storage;
	
	/**
	 * Constructor of the class. Initialize the storage
	 * @param storage The store to work with
	 */
	public Consumer(EventStorage storage){
		this.storage=storage;
	}
	
	/**
	 * Core method for the consumer. Consume 100 events
	 */
	@Override
	public void run() {
		for (int i=0; i<100; i++){
			storage.get();
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe3/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Producer.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class implements a producer of events.
 *
 */
public class Producer implements Runnable {

	/**
	 * Store to work with
	 */
	private EventStorage storage;
	
	/**
	 * Constructor of the class. Initialize the storage.
	 * @param storage The store to work with
	 */
	public Producer(EventStorage storage){
		this.storage=storage;
	}
	
	/**
	 * Core method of the producer. Generates 100 events.
	 */
	@Override
	public void run() {
		for (int i=0; i<100; i++){
			storage.set();
		}
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe3/src/com/packtpub/java7/concurrency/chapter2/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.Consumer;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.EventStorage;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Producer;

/**
 * Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	 */
	public static void main(String[] args) {
		
		// Creates an event storage
		EventStorage storage=new EventStorage();
		
		// Creates a Producer and a Thread to run it
		Producer producer=new Producer(storage);
		Thread thread1=new Thread(producer);

		// Creates a Consumer and a Thread to run it
		Consumer consumer=new Consumer(storage);
		Thread thread2=new Thread(consumer);
		
		// Starts the thread
		thread2.start();
		thread1.start();
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe7/src/com/packtpub/java7/concurrency/chapter2/recipe6/task/Buffer.java
package com.packtpub.java7.concurrency.chapter2.recipe6.task;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements a buffer to stores the simulate file lines between the
 * producer and the consumers
 * 
 */
public class Buffer {

	/**
	 * The buffer
	 */
	private LinkedList<String> buffer;

	/**
	 * Size of the buffer
	 */
	private int maxSize;

	/**
	 * Lock to control the access to the buffer
	 */
	private ReentrantLock lock;

	/**
	 * Conditions to control that the buffer has lines and has empty space
	 */
	private Condition lines;
	private Condition space;

	/**
	 * Attribute to control where are pending lines in the buffer
	 */
	private boolean pendingLines;

	/**
	 * Constructor of the class. Initialize all the objects
	 * 
	 * @param maxSize
	 *            The size of the buffer
	 */
	public Buffer(int maxSize) {
		this.maxSize = maxSize;
		buffer = new LinkedList<>();
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}

	/**
	 * Insert a line in the buffer
	 * 
	 * @param line
	 *            line to insert in the buffer
	 */
	public void insert(String line) {
		lock.lock();
		try {
			while (buffer.size() == maxSize) {
				space.await();
			}
			buffer.offer(line);
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread()
					.getName(), buffer.size());
			lines.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns a line from the buffer
	 * 
	 * @return a line from the buffer
	 */
	public String get() {
		String line=null;
		lock.lock();		
		try {
			while ((buffer.size() == 0) &&(hasPendingLines())) {
				lines.await();
			}
			
			if (hasPendingLines()) {
				line = buffer.poll();
				System.out.printf("%s: Line Readed: %d\n",Thread.currentThread().getName(),buffer.size());
				space.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return line;
	}

	/**
	 * Establish the value of the variable
	 * 
	 * @param pendingLines
	 */
	public void setPendingLines(boolean pendingLines) {
		this.pendingLines = pendingLines;
	}

	/**
	 * Returns the value of the variable
	 * 
	 * @return the value of the variable
	 */
	public boolean hasPendingLines() {
		return pendingLines || buffer.size() > 0;
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe7/src/com/packtpub/java7/concurrency/chapter2/recipe6/task/Consumer.java
package com.packtpub.java7.concurrency.chapter2.recipe6.task;

import java.util.Random;

/**
 * This class reads line from the buffer and process it
 *
 */
public class Consumer implements Runnable {

	/**
	 * The buffer
	 */
	private Buffer buffer;
	
	/**
	 * Constructor of the class. Initialize the buffer
	 * @param buffer
	 */
	public Consumer (Buffer buffer) {
		this.buffer=buffer;
	}
	
	/**
	 * Core method of the consumer. While there are pending lines in the
	 * buffer, try to read one.
	 */
	@Override
	public void run() {
		while (buffer.hasPendingLines()) {
			String line=buffer.get();
			processLine(line);
		}
	}

	/**
	 * Method that simulates the processing of a line. Waits 10 milliseconds
	 * @param line
	 */
	private void processLine(String line) {
		try {
			Random random=new Random();
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe7/src/com/packtpub/java7/concurrency/chapter2/recipe6/task/Producer.java
package com.packtpub.java7.concurrency.chapter2.recipe6.task;

import com.packtpub.java7.concurrency.chapter2.recipe6.utils.FileMock;

/**
 * This class gets lines from the simulate file and stores them in the
 * buffer, if there is space in it.
 *
 */
public class Producer implements Runnable {

	/**
	 * Simulated File
	 */
	private FileMock mock;
	
	/**
	 * Buffer
	 */
	private Buffer buffer;
	
	/**
	 * Constructor of the class. Initialize the objects
	 * @param mock Simulated file
	 * @param buffer Buffer
	 */
	public Producer (FileMock mock, Buffer buffer){
		this.mock=mock;
		this.buffer=buffer;	
	}
	
	/**
	 * Core method of the producer. While are pending lines in the
	 * simulated file, reads one and try to store it in the buffer.
	 */
	@Override
	public void run() {
		buffer.setPendingLines(true);
		while (mock.hasMoreLines()){
			String line=mock.getLine();
			buffer.insert(line);
		}
		buffer.setPendingLines(false);
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe7/src/com/packtpub/java7/concurrency/chapter2/recipe6/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe6.core;

import com.packtpub.java7.concurrency.chapter2.recipe6.task.Buffer;
import com.packtpub.java7.concurrency.chapter2.recipe6.task.Consumer;
import com.packtpub.java7.concurrency.chapter2.recipe6.task.Producer;
import com.packtpub.java7.concurrency.chapter2.recipe6.utils.FileMock;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Creates a simulated file with 100 lines
		 */
		FileMock mock=new FileMock(101, 10);
		
		/**
		 * Creates a buffer with a maximum of 20 lines
		 */
		Buffer buffer=new Buffer(20);
		
		/**
		 * Creates a producer and a thread to run it
		 */
		Producer producer=new Producer(mock, buffer);
		Thread threadProducer=new Thread(producer,"Producer");
		
		/**
		 * Creates three consumers and threads to run them
		 */
		Consumer consumers[]=new Consumer[3];
		Thread threadConsumers[]=new Thread[3];
		
		for (int i=0; i<3; i++){
			consumers[i]=new Consumer(buffer);
			threadConsumers[i]=new Thread(consumers[i],"Consumer "+i);
		}
		
		/**
		 * Strats the producer and the consumers
		 */
		threadProducer.start();
		for (int i=0; i<3; i++){
			threadConsumers[i].start();
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe7/src/com/packtpub/java7/concurrency/chapter2/recipe6/utils/FileMock.java
package com.packtpub.java7.concurrency.chapter2.recipe6.utils;

/**
 * This class simulates a text file. It creates a defined number
 * of random lines to process them sequentially.
 *
 */
public class FileMock {
	
	/**
	 * Content of the simulate file
	 */
	private String content[];
	/**
	 * Number of the line we are processing
	 */
	private int index;
	
	/**
	 * Constructor of the class. Generate the random data of the file
	 * @param size: Number of lines in the simulate file
	 * @param length: Length of the lines
	 */
	public FileMock(int size, int length){
		content=new String[size];
		for (int i=0; i<size; i++){
			StringBuilder buffer=new StringBuilder(length);
			for (int j=0; j<length; j++){
				int indice=(int)Math.random()*255;
				buffer.append((char)indice);
			}
			content[i]=buffer.toString();
		}
		index=0;
	}
	
	/**
	 * Returns true if the file has more lines to process or false if not
	 * @return true if the file has more lines to process or false if not
	 */
	public boolean hasMoreLines(){
		return index<content.length;
	}
	
	/**
	 * Returns the next line of the simulate file or null if there aren't more lines
	 * @return
	 */
	public String getLine(){
		if (this.hasMoreLines()) {
			System.out.println("Mock: "+(content.length-index));
			return content[index++];
		} 
		return null;
	}
	
}

//=*=*=*=*
//./Chapter_2/ch2_recipe4/src/com/packtpub/java7/concurrency/chapter2/recipe3/task/Job.java
package com.packtpub.java7.concurrency.chapter2.recipe3.task;

/**
 * This class simulates a job that send a document to print.
 *
 */
public class Job implements Runnable {

	/**
	 * Queue to print the documents
	 */
	private PrintQueue printQueue;
	
	/**
	 * Constructor of the class. Initializes the queue
	 * @param printQueue
	 */
	public Job(PrintQueue printQueue){
		this.printQueue=printQueue;
	}
	
	/**
	 * Core method of the Job. Sends the document to the print queue and waits
	 *  for its finalization
	 */
	@Override
	public void run() {
		System.out.printf("%s: Going to print a document\n",Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());		
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe4/src/com/packtpub/java7/concurrency/chapter2/recipe3/task/PrintQueue.java
package com.packtpub.java7.concurrency.chapter2.recipe3.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class simulates a print queue
 *
 */
public class PrintQueue {

	/**
	 * Lock to control the access to the queue.
	 */
	private final Lock queueLock=new ReentrantLock();
	
	/**
	 * Method that prints a document
	 * @param document document to print
	 */
	public void printJob(Object document){
		queueLock.lock();
		
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe4/src/com/packtpub/java7/concurrency/chapter2/recipe3/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe3.core;

import com.packtpub.java7.concurrency.chapter2.recipe3.task.Job;
import com.packtpub.java7.concurrency.chapter2.recipe3.task.PrintQueue;

/**
 * Main class of the example.
 *
 */
public class Main {

	/**
	 * Main method of the class. Run ten jobs in parallel that
	 * send documents to the print queue at the same time.
	 */
	public static void main (String args[]){
		
		// Creates the print queue
		PrintQueue printQueue=new PrintQueue();
		
		// Creates ten Threads
		Thread thread[]=new Thread[10];
		for (int i=0; i<10; i++){
			thread[i]=new Thread(new Job(printQueue),"Thread "+i);
		}
		
		// Starts the Threads
		for (int i=0; i<10; i++){
			thread[i].start();
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe5/src/com/packtpub/java7/concurrency/chapter2/recipe4/task/Reader.java
package com.packtpub.java7.concurrency.chapter2.recipe4.task;

/**
 * This class implements a reader that consults the prices
 *
 */
public class Reader implements Runnable {

	/**
	 * Class that stores the prices
	 */
	private PricesInfo pricesInfo;
	
	/**
	 * Constructor of the class
	 * @param pricesInfo object that stores the prices
	 */
	public Reader (PricesInfo pricesInfo){
		this.pricesInfo=pricesInfo;
	}
	
	/**
	 * Core method of the reader. Consults the two prices and prints them
	 * to the console
	 */
	@Override
	public void run() {
		for (int i=0; i<10; i++){
			System.out.printf("%s: Price 1: %f\n",Thread.currentThread().getName(),pricesInfo.getPrice1());
			System.out.printf("%s: Price 2: %f\n",Thread.currentThread().getName(),pricesInfo.getPrice2());
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe5/src/com/packtpub/java7/concurrency/chapter2/recipe4/task/PricesInfo.java
package com.packtpub.java7.concurrency.chapter2.recipe4.task;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class simulates the store of two prices. We will
 * have a writer that stores the prices and readers that 
 * consult this prices
 *
 */
public class PricesInfo {
	
	/**
	 * The two prices
	 */
	private double price1;
	private double price2;
	
	/**
	 * Lock to control the access to the prices
	 */
	private ReadWriteLock lock;
	
	/**
	 * Constructor of the class. Initializes the prices and the Lock
	 */
	public PricesInfo(){
		price1=1.0;
		price2=2.0;
		lock=new ReentrantReadWriteLock();
	}

	/**
	 * Returns the first price
	 * @return the first price
	 */
	public double getPrice1() {
		lock.readLock().lock();
		double value=price1;
		lock.readLock().unlock();
		return value;
	}

	/**
	 * Returns the second price
	 * @return the second price
	 */
	public double getPrice2() {
		lock.readLock().lock();
		double value=price2;
		lock.readLock().unlock();
		return value;
	}

	/**
	 * Establish the prices
	 * @param price1 The price of the first product
	 * @param price2 The price of the second product
	 */
	public void setPrices(double price1, double price2) {
		lock.writeLock().lock();
		this.price1=price1;
		this.price2=price2;
		lock.writeLock().unlock();
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe5/src/com/packtpub/java7/concurrency/chapter2/recipe4/task/Writer.java
package com.packtpub.java7.concurrency.chapter2.recipe4.task;

/**
 * This class implements a writer that establish the prices
 *
 */
public class Writer implements Runnable {

	/**
	 * Class that stores the prices
	 */
	private PricesInfo pricesInfo;
	
	/**
	 * Constructor of the class
	 * @param pricesInfo object that stores the prices
	 */
	public Writer(PricesInfo pricesInfo){
		this.pricesInfo=pricesInfo;
	}
	
	/**
	 * Core method of the writer. Establish the prices
	 */
	@Override
	public void run() {
		for (int i=0; i<3; i++) {
			System.out.printf("Writer: Attempt to modify the prices.\n");
			pricesInfo.setPrices(Math.random()*10, Math.random()*8);
			System.out.printf("Writer: Prices have been modified.\n");
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe5/src/com/packtpub/java7/concurrency/chapter2/recipe4/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe4.core;

import com.packtpub.java7.concurrency.chapter2.recipe4.task.PricesInfo;
import com.packtpub.java7.concurrency.chapter2.recipe4.task.Reader;
import com.packtpub.java7.concurrency.chapter2.recipe4.task.Writer;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main class of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Creates an object to store the prices
		PricesInfo pricesInfo=new PricesInfo();
		
		Reader readers[]=new Reader[5];
		Thread threadsReader[]=new Thread[5];
		
		// Creates five readers and threads to run them
		for (int i=0; i<5; i++){
			readers[i]=new Reader(pricesInfo);
			threadsReader[i]=new Thread(readers[i]);
		}
		
		// Creates a writer and a thread to run it
		Writer writer=new Writer(pricesInfo);
		Thread threadWriter=new Thread(writer);
		
		// Starts the threads
		for (int i=0; i<5; i++){
			threadsReader[i].start();
		}
		threadWriter.start();
		
	}

}

//=*=*=*=*
//./Chapter_2/chx_recipe1/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Sensor2.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a sensor in the building
 */
public class Sensor2 implements Runnable {

	/**
	 * Object with the statistics of the building
	 */
	private BuildStats stats;
	
	/**
	 * Constructor of the class
	 * @param stats object with the statistics of the building
	 */
	public Sensor2(BuildStats stats){
		this.stats=stats;
	}
	
	/**
	 * Core method of the Runnable. Simulates inputs and outputs in the building
	 */
	@Override
	public void run() {
		stats.comeIn();
		stats.comeIn();
		stats.goOut();
		stats.goOut();
		stats.goOut();
	}

}

//=*=*=*=*
//./Chapter_2/chx_recipe1/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/Sensor1.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a sensor in the building
 */
public class Sensor1 implements Runnable {

	/**
	 * Object with the statistics of the building
	 */
	private BuildStats stats;
	
	/**
	 * Constructor of the class
	 * @param stats object with the statistics of the building
	 */
	public Sensor1(BuildStats stats){
		this.stats=stats;
	}
	
	/**
	 * Core method of the Runnable. Simulates inputs and outputs in the building
	 */
	@Override
	public void run() {
		stats.comeIn();
		stats.comeIn();
		stats.comeIn();
		stats.goOut();
		stats.comeIn();
	}

}

//=*=*=*=*
//./Chapter_2/chx_recipe1/src/com/packtpub/java7/concurrency/chapter2/recipe2/task/BuildStats.java
package com.packtpub.java7.concurrency.chapter2.recipe2.task;

import java.util.concurrent.TimeUnit;

/**
 * 
 * This class simulates a control class that stores the statistics of
 * access to a building, controlling the number of people inside the building
 *
 */
public class BuildStats {

	/**
	 * Number of people inside the building
	 */
	private long numPeople;
	
	/**
	 * Method that simulates when people come in into the building
	 */
	public /*synchronized*/ void comeIn(){
		System.out.printf("%s: A person enters.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople++;
		}
		generateCard();
	}
	
	/**
	 * Method that simulates when people leave the building
	 */
	public /*synchronized*/ void goOut(){
		System.out.printf("%s: A person leaves.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople--;
		}
		generateReport();
	}
	
	/**
	 * Method that simulates the generation of a card when people come in into the building
	 */
	private void generateCard(){
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method that simulates the generation of a report when people leaves the building
	 */
	private void generateReport(){
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that print the number of people inside the building
	 */
	public void printStats(){
		System.out.printf("%d persons in the building.\n",numPeople);
	}
	
}

//=*=*=*=*
//./Chapter_2/chx_recipe1/src/com/packtpub/java7/concurrency/chapter2/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import java.util.Date;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.BuildStats;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor1;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor2;

/**
 * Main class of the example. Creates an object with the statistics of the
 * building and executes two threads that simulates two sensors in the building
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a new object for the statistics
		BuildStats stats=new BuildStats();

		// Create a Sensor1 object and a Thread to run it
		Sensor1 sensor1=new Sensor1(stats);
		Thread thread1=new Thread(sensor1,"Sensor 1");

		// Create a Sensor 2 object and a Thread to run it
		Sensor2 sensor2=new Sensor2(stats);
		Thread thread2=new Thread(sensor2,"Sensor 2");
		
		// Get the actual time
		Date date1=new Date();
		
		//Starts the threads
		thread1.start();
		thread2.start();
		
		try {
			// Wait for the finalization of the threads
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Get the actual time and print the execution time
		Date date2=new Date();
		stats.printStats();
		System.out.println("Execution Time: "+((date2.getTime()-date1.getTime())/1000));

	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_problem/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Account.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulate a bank account 
 *
 */
public class Account {

	/**
	 * Balance of the bank account
	 */
	private double balance;

	/**
	 * Returns the balance of the account
	 * @return the balance of the account
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Establish the balance of the account
	 * @param balance the new balance of the account
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	/**
	 * Add an import to the balance of the account
	 * @param amount import to add to the balance
	 */
	public void addAmount(double amount) {
		double tmp=balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp+=amount;
		balance=tmp;
	}
	
	/**
	 * Subtract an import to the balance of the account
	 * @param amount import to subtract to the balance
	 */
	public void subtractAmount(double amount) {
		double tmp=balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp-=amount;
		balance=tmp;
	}
	
}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_problem/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Company.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulates a company that pays a salary an
 * insert money into an account 
 *
 */
public class Company implements Runnable {

	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account the account affected by the operations
	 */
	public Company(Account account) {
		this.account=account;
	}
	
	/**
	 * Core method of the Runnable
	 */
	public void run() {
		for (int i=0; i<100; i++){
			account.addAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_problem/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Bank.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulates a bank or a cash dispenser that takes money
 * from an account
 *
 */
public class Bank implements Runnable {

	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account The account affected by the operations
	 */
	public Bank(Account account) {
		this.account=account;
	}
	
	
	/**
	 * Core method of the Runnable
	 */
	public void run() {
		for (int i=0; i<100; i++){
			account.subtractAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_problem/src/com/packtpub/java7/concurrency/chapter2/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe1.core;

import com.packtpub.java7.concurrency.chapter2.recipe1.task.Account;
import com.packtpub.java7.concurrency.chapter2.recipe1.task.Bank;
import com.packtpub.java7.concurrency.chapter2.recipe1.task.Company;


/**
 * Main class of the example. It creates an account, a company and a bank
 * to work with the account. The final balance should be equal to the initial, but....
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a new account ...
		Account	account=new Account();
		// an initialize its balance to 1000
		account.setBalance(1000);
		
		// Creates a new Company and a Thread to run its task
		Company	company=new Company(account);
		Thread companyThread=new Thread(company);
		// Creates a new Bank and a Thread to run its task
		Bank bank=new Bank(account);
		Thread bankThread=new Thread(bank);
		
		// Prints the initial balance
		System.out.printf("Account : Initial Balance: %f\n",account.getBalance());
		
		// Starts the Threads
		companyThread.start();
		bankThread.start();

		try {
			// Wait for the finalization of the Threads
			companyThread.join();
			bankThread.join();
			// Print the final balance
			System.out.printf("Account : Final Balance: %f\n",account.getBalance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe6/src/com/packtpub/java7/concurrency/chapter2/recipe5/task/Job.java
package com.packtpub.java7.concurrency.chapter2.recipe5.task;

/**
 * This class simulates a job that send a document to print
 *
 */
public class Job implements Runnable {

	/**
	 * The queue to send the documents
	 */
	private PrintQueue printQueue;
	
	/**
	 * Constructor of the class. Initializes the print queue
	 * @param printQueue the print queue to send the documents
	 */
	public Job(PrintQueue printQueue){
		this.printQueue=printQueue;
	}
	
	/**
	 * Core method of the Job. Sends the document to the queue
	 */
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job\n",Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());		
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe6/src/com/packtpub/java7/concurrency/chapter2/recipe5/task/PrintQueue.java
package com.packtpub.java7.concurrency.chapter2.recipe5.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class simulates a print queue. 
 *
 */
public class PrintQueue {

	/**
	 * Creates a lock to control the access to the queue.
	 * With the boolean attribute, we control the fairness of
	 * the Lock
	 */
	private final Lock queueLock=new ReentrantLock(false);
	
	/**
	 * Method that prints the Job. The printing is divided in two phase two
	 * show how the fairness attribute affects the election of the thread who
	 * has the control of the lock
	 * @param document The document to print
	 */
	public void printJob(Object document){
		queueLock.lock();
		
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
		
		
		queueLock.lock();
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
	}
}

//=*=*=*=*
//./Chapter_2/ch2_recipe6/src/com/packtpub/java7/concurrency/chapter2/recipe5/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe5.core;

import com.packtpub.java7.concurrency.chapter2.recipe5.task.Job;
import com.packtpub.java7.concurrency.chapter2.recipe5.task.PrintQueue;

/**
 * Main class of the example
 *
 */
public class Main {
	
	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main (String args[]){
		// Creates the print queue
		PrintQueue printQueue=new PrintQueue();
		
		// Cretes ten jobs and the Threads to run them
		Thread thread[]=new Thread[10];
		for (int i=0; i<10; i++){
			thread[i]=new Thread(new Job(printQueue),"Thread "+i);
		}
		
		// Launch a thread ever 0.1 seconds
		for (int i=0; i<10; i++){
			thread[i].start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_solution/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Account.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulates a bank account 
 *
 */
public class Account {

	/**
	 * Balance of the bank account
	 */
	private double balance;

	/**
	 * Returns the balance of the account
	 * @return the balance of the account
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Establish the balance of the account
	 * @param balance the new balance of the account
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	/**
	 * Add an import to the balance of the account
	 * @param amount the import to add to the balance of the account
	 */
	public synchronized void addAmount(double amount) {
		double tmp=balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp+=amount;
		balance=tmp;
	}
	
	/**
	 * Subtract an import to the balance of the account
	 * @param amount the import to subtract to the balance of the account 
	 */
	public synchronized void subtractAmount(double amount) {
		double tmp=balance;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp-=amount;
		balance=tmp;
	}
	
}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_solution/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Company.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulates a company that pays a salary an
 * insert money into an account 
 *
 */
public class Company implements Runnable {
	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account the account affected by the operations
	 */
	public Company(Account account) {
		this.account=account;
	}
	
	/**
	 * Core method of the Runnable
	 */
	public void run() {
		for (int i=0; i<100; i++){
			account.addAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_solution/src/com/packtpub/java7/concurrency/chapter2/recipe1/task/Bank.java
package com.packtpub.java7.concurrency.chapter2.recipe1.task;

/**
 * This class simulates a bank or a cash dispenser that takes money
 * from an account
 * 
 */
public class Bank implements Runnable {

	/**
	 * The account affected by the operations
	 */
	private Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account The account affected by the operations
	 */
	public Bank(Account account) {
		this.account=account;
	}
	
	/**
	 * Core method of the Runnable
	 */
	public void run() {
		for (int i=0; i<100; i++){
			account.subtractAmount(1000);
		}
	}

}

//=*=*=*=*
//./Chapter_2/ch2_recipe1_solution/src/com/packtpub/java7/concurrency/chapter2/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter2.recipe1.core;

import com.packtpub.java7.concurrency.chapter2.recipe1.task.Account;
import com.packtpub.java7.concurrency.chapter2.recipe1.task.Bank;
import com.packtpub.java7.concurrency.chapter2.recipe1.task.Company;

/**
 * Main class of the example. It creates an account, a company and a bank
 * to work with the account. The final balance is equal to the initial.
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a new account ...
		Account	account=new Account();
		// an initialize its balance to 1000
		account.setBalance(1000);
		
		// Creates a new Company and a Thread to run its task
		Company	company=new Company(account);
		Thread companyThread=new Thread(company);
		// Creates a new Bank and a Thread to run its task
		Bank bank=new Bank(account);
		Thread bankThread=new Thread(bank);
		
		// Prints the initial balance
		System.out.printf("Account : Initial Balance: %f\n",account.getBalance());
		
		// Starts the Threads
		companyThread.start();
		bankThread.start();

		try {
			// Wait for the finalization of the Threads
			companyThread.join();
			bankThread.join();
			// Print the final balance
			System.out.printf("Account : Final Balance: %f\n",account.getBalance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe09/src/com/packtpub/java7/concurrency/chapter7/recipe09/task/Event.java
package com.packtpub.java7.concurrency.chapter7.recipe09.task;

/**
 * This class stores the attributes of an event. Its thread
 * and is priority. Implements the comparable interface to
 * help the priority queue to decide which event has more priority 
 *
 */
public class Event implements Comparable<Event> {
	
	/**
	 * Number of the thread that generates the event
	 */
	private String thread;
	/**
	 * Priority of the thread
	 */
	private int priority;
	
	/**
	 * Constructor of the thread. It initializes its attributes
	 * @param thread Number of the thread that generates the event
	 * @param priority Priority of the event
	 */
	public Event(String thread, int priority){
		this.thread=thread;
		this.priority=priority;
	}
	
	/**
	 * Method that returns the number of the thread that generates the
	 * event
	 * @return The number of the thread that generates the event
	 */
	public String getThread() {
		return thread;
	}
	
	/**
	 * Method that returns the priority of the event
	 * @return The priority of the event
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Method that compares two events and decide which has more priority
	 */
	@Override
	public int compareTo(Event e) {
		if (this.priority>e.getPriority()) {
			return -1;
		} else if (this.priority<e.getPriority()) {
			return 1; 
		} else {
			return 0;
		}
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe09/src/com/packtpub/java7/concurrency/chapter7/recipe09/task/MyPriorityTransferQueue.java
package com.packtpub.java7.concurrency.chapter7.recipe09.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements a priority based transfer queue. It extends the
 * PriorityBlockingQueue class and implements the TransferQueue interface
 *
 * @param <E> Class of the elements to be stored in the queue
 */
public class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E> implements
		TransferQueue<E> {

	/**
	 * Serial Version of the class
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number of consumers waiting
	 */
	private AtomicInteger counter;
	
	/**
	 * Blocking queue to store the transfered elements
	 */
	private LinkedBlockingQueue<E> transfered;
	
	/**
	 * Lock to control the acces to the operations
	 */
	private ReentrantLock lock;
	
	/**
	 * Constructor of the class
	 */
	public MyPriorityTransferQueue() {
		counter=new AtomicInteger(0);
		lock=new ReentrantLock();
		transfered=new LinkedBlockingQueue<>();
	}
	
	/**
	 * This method tries to transfer an element to a consumer. If there is
	 * a consumer waiting, we puts the element in the queue and return the
	 * true value. Else, return the false value.
	 */
	@Override
	public boolean tryTransfer(E e) {
		lock.lock();
		boolean value;
		if (counter.get()==0) {
			value=false;
		} else {
			put(e);
			value=true;
		}
		lock.unlock();
		return value;
	}

	/**
	 * Transfer an element to the consumer. If there is a consumer waiting,
	 * puts the element on the queue and return the true value. Else, puts the
	 * value in the transfered queue and returns the false value. In this case, the
	 * thread than makes the call will be blocked until a consumer takes the transfered
	 * elements
	 */
	@Override
	public void transfer(E e) throws InterruptedException {
		lock.lock();
		if (counter.get()!=0) {
			put(e);
			lock.unlock();
		} else {
			transfered.add(e);
			lock.unlock();
			synchronized (e) {
				e.wait();
			}
		}
	}
	
	/**
	 * This method tries to transfer an element to a consumer waiting a maximum period
	 * of time. If there is a consumer waiting, puts the element in the queue. Else,
	 * puts the element in the queue of transfered elements and wait the specified period of time
	 * until that time pass or the thread is interrupted.
	 */
	@Override
	public boolean tryTransfer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {
		lock.lock();
		if (counter.get()!=0) {
			put(e);
			lock.unlock();
			return true;
		} else {
			transfered.add(e);
			long newTimeout=TimeUnit.MILLISECONDS.convert(timeout, unit);
			lock.unlock();
			e.wait(newTimeout);
			lock.lock();
			if (transfered.contains(e)) {
				transfered.remove(e);
				lock.unlock();
				return false;
			} else {
				lock.unlock();
				return true;
			}
		}
	}


	/**
	 * Method that returns if the queue has waiting consumers
	 */
	@Override
	public boolean hasWaitingConsumer() {
		return (counter.get()!=0);
	}

	/**
	 * Method that returns the number of waiting consumers
	 */
	@Override
	public int getWaitingConsumerCount() {
		return counter.get();
	}

	/**
	 * Method that returns the first element of the queue or is blocked if the queue
	 * is empty. If there is transfered elements, takes the first transfered element and
	 * wake up the thread that is waiting for the transfer of that element. Else, takes the
	 * first element of the queue or is blocked until there is one element in the queue.
	 */
	@Override
	public E take() throws InterruptedException {
		lock.lock();
		counter.incrementAndGet();
		E value=transfered.poll();
		if (value==null) {
			lock.unlock();
			value=super.take();
			lock.lock();
		} else {
			synchronized (value) {
				value.notify();
			}
		}
		counter.decrementAndGet();
		lock.unlock();
		return value;
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe09/src/com/packtpub/java7/concurrency/chapter7/recipe09/task/Consumer.java
package com.packtpub.java7.concurrency.chapter7.recipe09.task;

/**
 *	This class implements the Consumer of the events. There is only
 * one consumer in the example that consumes 1002 events 
 *
 */
public class Consumer implements Runnable {

	/**
	 * Buffer from which the consumer takes the events
	 */
	private MyPriorityTransferQueue<Event> buffer;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param buffer Buffer from which the consumer takes the events
	 */
	public Consumer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer=buffer;
	}
	
	/**
	 * Main method of the consumer. It takes 1002 events from the buffer
	 */
	@Override
	public void run() {
		for (int i=0; i<1002; i++) {
			try {
				Event value=buffer.take();
				System.out.printf("Consumer: %s: %d\n",value.getThread(),value.getPriority());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe09/src/com/packtpub/java7/concurrency/chapter7/recipe09/task/Producer.java
package com.packtpub.java7.concurrency.chapter7.recipe09.task;

/**
 * This class implements the producers of data. It store 100
 * events in the queue with incremental priority
 *
 */
public class Producer implements Runnable {
	
	/**
	 * Buffer used to store the events
	 */
	private MyPriorityTransferQueue<Event> buffer;
	
	/**
	 * Constructor of the class. It initializes its parameters
	 * @param buffer Buffer to store the events
	 */
	public Producer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer=buffer;
	}
	
	/**
	 * Main method of the producer. Store 100 events in the buffer with
	 * incremental priority
	 */
	@Override
	public void run() {
		for (int i=0; i<100; i++) {
			Event event=new Event(Thread.currentThread().getName(),i);
			buffer.put(event);
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe09/src/com/packtpub/java7/concurrency/chapter7/recipe09/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe09.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe09.task.Consumer;
import com.packtpub.java7.concurrency.chapter7.recipe09.task.Event;
import com.packtpub.java7.concurrency.chapter7.recipe09.task.MyPriorityTransferQueue;
import com.packtpub.java7.concurrency.chapter7.recipe09.task.Producer;

/**
 * Main class of the example.
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		/*
		 * Create a Prioriy Transfer Queue
		 */
		MyPriorityTransferQueue<Event> buffer=new MyPriorityTransferQueue<>();
		
		/*
		 * Create a Producer object
		 */
		Producer producer=new Producer(buffer);
		
		/*
		 * Launch 10 producers
		 */
		Thread producerThreads[]=new Thread[10];
		for (int i=0; i<producerThreads.length; i++) {
			producerThreads[i]=new Thread(producer);
			producerThreads[i].start();
		}

		/*
		 * Create and launch the consumer
		 */
		Consumer consumer=new Consumer(buffer);
		Thread consumerThread=new Thread(consumer);
		consumerThread.start();

		/*
		 * Write in the console the actual consumer count
		 */
		System.out.printf("Main: Buffer: Consumer count: %d\n",buffer.getWaitingConsumerCount());

		/*
		 * Transfer an event to the consumer
		 */
		Event myEvent=new Event("Core Event",0);
		buffer.transfer(myEvent);
		System.out.printf("Main: My Event has ben transfered.\n");
		
		/*
		 * Wait for the finalization of the producers
		 */
		for (int i=0; i<producerThreads.length; i++) {
			producerThreads[i].join();
		}
		
		/*
		 * Sleep the thread for one second
		 */
		TimeUnit.SECONDS.sleep(1);
		
		/*
		 * Write the actual consumer count
		 */
		System.out.printf("Main: Buffer: Consumer count: %d\n",buffer.getWaitingConsumerCount());
		
		/*
		 * Transfer another event
		 */
		myEvent=new Event("Core Event 2",0);
		buffer.transfer(myEvent);
		
		/*
		 * Wait for the finalization of the consumer
		 */
		consumerThread.join();
		
		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe02/src/com/packtpub/java7/concurrency/chapter7/recipe02/task/MyPriorityTask.java
package com.packtpub.java7.concurrency.chapter7.recipe02.task;

import java.util.concurrent.TimeUnit;

/**
 * This is the base class to implement a priority-based executor. It implements the base for the priority tasks.
 * They are based on the Runnable interface and implement the Comparable interface. 
 * If a task has a higher value of its priority attribute, it will be stored before in the priority queue and
 * it will be executed before
 *
 */
public class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {

	/**
	 * This attribute stores the priority of the task
	 */
	private int priority;
	
	/**
	 * This attribute stores the name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the task. It initialize its attributes
	 * @param name Name of the task
	 * @param priority Priority of the task
	 */
	public MyPriorityTask(String name, int priority) {
		this.name=name;
		this.priority=priority;
	}
	
	/**
	 * Method that returns the priority of the task
	 * @return the priority of the task
	 */
	public int getPriority(){
		return priority;
	}
	
	/**
	 * Method that compares the priorities of two tasks. The task with higher priority value will
	 * be stored before in the list and it will be executed before
	 */
	@Override
	public int compareTo(MyPriorityTask o) {
		if (this.getPriority() < o.getPriority()) {
			return 1;
		}
		
		if (this.getPriority() > o.getPriority()) {
			return -1;
		}
		
		return 0;
	}

	/**
	 * Main method of the task. It only writes a message to the console. It will be overridden by the real tasks
	 */
	@Override
	public void run() {
		System.out.printf("MyPriorityTask: %s Priority : %d\n",name,priority);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe02/src/com/packtpub/java7/concurrency/chapter7/recipe02/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe02.core;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe02.task.MyPriorityTask;

/**
 * Main method of the class. It creates an Executor with a PriorityQueue as working queue and then
 * sends various tasks with different priorities to check that they are executed in the correct order
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Create an executor with a PriorityBlockingQueue as the structure to store the tasks
		 */
		ThreadPoolExecutor executor=new ThreadPoolExecutor(2,2,1,TimeUnit.SECONDS,new PriorityBlockingQueue<Runnable>());

		/*
		 * Send four task to the executor
		 */
		for (int i=0; i<4; i++){
			MyPriorityTask task=new MyPriorityTask("Task "+i,i);
			executor.execute(task);
		}
		
		/*
		 * sleep the thread during one second
		 */
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Send four tasks to the executor
		 */
		for (int i=4; i<8; i++) {
			MyPriorityTask task=new MyPriorityTask("Task "+i,i);
			executor.execute(task);			
		}
		
		/*
		 * Shutdown the executor
		 */
		executor.shutdown();
		
		/*
		 * Wait for the finalization of the executor
		 */
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Write a message to the console indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe01/src/com/packtpub/java7/concurrency/chapter7/recipe01/task/SleepTwoSecondsTask.java
package com.packtpub.java7.concurrency.chapter7.recipe01.task;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Task implemented to test the customized executor
 *
 */
public class SleepTwoSecondsTask implements Callable<String> {

	/**
	 * Main method of the tasks. It only sleeps the current thread for two seconds
	 */
	public String call() throws Exception {
		TimeUnit.SECONDS.sleep(2);
		return new Date().toString();
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe01/src/com/packtpub/java7/concurrency/chapter7/recipe01/executor/MyExecutor.java
package com.packtpub.java7.concurrency.chapter7.recipe01.executor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class extends the ThreadPoolExecutor class to implement a customized executor.
 * It overrides the shutdown(), shutdownNow(), beforeExecute() and afterExecute() to
 * show statistics about the tasks executed by the Executor
 * 
 */
public class MyExecutor extends ThreadPoolExecutor {

	/**
	 * A HashMap to store the start date of the tasks executed by the executor. When 
	 * a task finish, it calculates the difference between the start date and the end date
	 * to show the duration of the task
	 */
	private ConcurrentHashMap<String, Date> startTimes;
	
	/**
	 * Constructor of the executor. Call the parent constructor and initializes the HashMap
	 * @param corePoolSize Number of threads to keep in the pool
	 * @param maximumPoolSize Maximum number of threads in the pool
	 * @param keepAliveTime Maximum time that threads can be idle
	 * @param unit Unit of time of the keepAliveTime parameter
	 * @param workQueue Queue where the submited tasks will be stored
	 */
	public MyExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		startTimes=new ConcurrentHashMap<>();
	}

	/**
	 * This method is called to finish the execution of the Executor. We write statistics
	 * about the tasks executed in it
	 */
	@Override
	public void shutdown() {
		System.out.printf("MyExecutor: Going to shutdown.\n");
		System.out.printf("MyExecutor: Executed tasks: %d\n",getCompletedTaskCount());
		System.out.printf("MyExecutor: Running tasks: %d\n",getActiveCount());
		System.out.printf("MyExecutor: Pending tasks: %d\n",getQueue().size());
		super.shutdown();
	}

	/**
	 * This method is called to finish the execution of the Executor immediately. We write statistics
	 * about the tasks executed in it
	 */
	@Override
	public List<Runnable> shutdownNow() {
		System.out.printf("MyExecutor: Going to immediately shutdown.\n");
		System.out.printf("MyExecutor: Executed tasks: %d\n",getCompletedTaskCount());
		System.out.printf("MyExecutor: Running tasks: %d\n",getActiveCount());
		System.out.printf("MyExecutor: Pending tasks: %d\n",getQueue().size());
		return super.shutdownNow();
	}

	/**
	 * This method is executed before the execution of a task. We store the start date in the HashMap
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.out.printf("MyExecutor: A task is beginning: %s : %s\n",t.getName(),r.hashCode());
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}

	/**
	 * This method is executed after the execution of a task. We calculate the execution time of the task
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		Future<?> result=(Future<?>)r;
		try {
			System.out.printf("*********************************\n");
			System.out.printf("MyExecutor: A task is finishing.\n");
			System.out.printf("MyExecutor: Result: %s\n",result.get());
			Date startDate=startTimes.remove(String.valueOf(r.hashCode()));
			Date finishDate=new Date();
			long diff=finishDate.getTime()-startDate.getTime();
			System.out.printf("MyExecutor: Duration: %d\n",diff);
			System.out.printf("*********************************\n");
		} catch (InterruptedException  | ExecutionException e) {
			e.printStackTrace();
		}
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe01/src/com/packtpub/java7/concurrency/chapter7/recipe01/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe01.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe01.executor.MyExecutor;
import com.packtpub.java7.concurrency.chapter7.recipe01.task.SleepTwoSecondsTask;

/**
 * Main clas of the example. It creates a custom executor and executes 10 tasks in it
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		 * Creation of the custom executor
		 */
		MyExecutor myExecutor=new MyExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
		
		/*
		 * Create a list to store the objects to control the execution of the tasks 
		 */
		List<Future<String>> results=new ArrayList<>();
		
		/*
		 * Create and submit to the executor 10 tasks 
		 */
		for (int i=0; i<10; i++) {
			SleepTwoSecondsTask task=new SleepTwoSecondsTask();
			Future<String> result=myExecutor.submit(task);
			results.add(result);
		}
		
		/*
		 * Get the result of the execution of the first five tasks 
		 */
		for (int i=0; i<5; i++){
			try {
				String result=results.get(i).get();
				System.out.printf("Main: Result for Task %d : %s\n",i,result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Call the shutdown method 
		 */
		myExecutor.shutdown();
		
		/*
		 * Get the results of the execution of the last five tasks
		 */
		for (int i=5; i<10; i++){
			try {
				String result=results.get(i).get();
				System.out.printf("Main: Result for Task %d : %s\n",i,result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Wait for the finalization of the Executor
		 */
		try {
			myExecutor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe08/src/com/packtpub/java7/concurrency/chapter7/recipe08/task/MyLock.java
package com.packtpub.java7.concurrency.chapter7.recipe08.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * This class implements a basic Lock. It uses a myAbstractQueueSyncrhonized object
 * as the element from which implement the methods of the lock. 
 *
 */
public class MyLock implements Lock{

	/**
	 * Synchronizer to implement the operations of the locks
	 */
	private AbstractQueuedSynchronizer sync;
	
	/**
	 * Constructor of the class. It initializes its attribute
	 */
	public MyLock() {
		sync=new MyAbstractQueuedSynchronizer();
	}

	/**
	 * Method that try to acquire the lock. If it can't, the thread
	 * will be blocked until the thread that has it release the lock
	 */
	@Override
	public void lock() {
		sync.acquire(1);
	}

	/**
	 * Method that try to acquire the lock. If it can't, the thread will
	 * be blocked until the thread that has it release the lock. The difference
	 * with the lock() method is that in this case, the blocked threads can
	 * be interrupted
	 */
	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	/**
	 * Method that try to acquire the lock. If it can, the method returns the true
	 * value. It it can't, the method return the false value
	 */
	@Override
	public boolean tryLock() {
		try {
			return sync.tryAcquireNanos(1, 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method that try to acquire the lock. If it can, the method returns the true value.
	 * If it can't, wait the time specified as parameter and if the lock hasn't been
	 * released, it returns the false value. It the lock is released in that period of time,
	 * the thread acquires the lock and the method returns the true value
	 */
	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireNanos(1, TimeUnit.NANOSECONDS.convert(time, unit));
	}

	/**
	 * Method that release the lock
	 */
	@Override
	public void unlock() {
		sync.release(1);
	}

	/**
	 * Method that creates a new condition for the lock
	 */
	@Override
	public Condition newCondition() {
		return sync.new ConditionObject();
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe08/src/com/packtpub/java7/concurrency/chapter7/recipe08/task/Task.java
package com.packtpub.java7.concurrency.chapter7.recipe08.task;

import java.util.concurrent.TimeUnit;

/**
 * This class implements a Task that uses the Lock
 *
 */
public class Task implements Runnable {
	
	/**
	 * Lock used by the task
	 */
	private MyLock lock;
	
	/**
	 * Name of the task
	 */
	private String name;
	
	/**
	 * Constructor of the class
	 * @param name Name of the task
	 * @param lock Lock used by the task
	 */
	public Task(String name, MyLock lock){
		this.lock=lock;
		this.name=name;
	}

	/**
	 * Main method of the task. It gets the lock, sleep the thread for two seconds
	 * and then release the lock.
	 */
	@Override
	public void run() {
		lock.lock();
		System.out.printf("Task: %s: Take the lock\n",name);
		try {
			TimeUnit.SECONDS.sleep(2);
			System.out.printf("Task: %s: Free the lock\n",name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe08/src/com/packtpub/java7/concurrency/chapter7/recipe08/task/MyAbstractQueuedSynchronizer.java
package com.packtpub.java7.concurrency.chapter7.recipe08.task;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * This class extends the AbstractQueueSynchronizer class to implement
 * the basis of a Lock. Internally, it uses an AtomicInteger variable
 * to store the state of the lock. It also stores the current thread that
 * has the lock. The tryAcquire()  method and tryRelease() method
 * are the starting point for the Lock implementation
 *
 */
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {

	/**
	 * Serial version UID of the class
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Attribute that stores the state of the lock. 0 if it's free, 1 if it's busy
	 */
	private AtomicInteger state;
	
	/**
	 * Constructor of the class
	 */
	public MyAbstractQueuedSynchronizer() {
		state=new AtomicInteger(0);
	}
	
	/**
	 * This method try to acquire the control of the lock
	 * @return true if the thread acquires the lock, false otherwise
	 */
	@Override
	protected boolean tryAcquire(int arg) {
		return state.compareAndSet(0, 1);
	}

	/**
	 * This method try to free the control of the lock
	 * @return true if the thread releases the lock, false otherwise
	 */
	@Override
	protected boolean tryRelease(int arg) {
		return state.compareAndSet(1, 0);
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe08/src/com/packtpub/java7/concurrency/chapter7/recipe08/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe08.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe08.task.MyLock;
import com.packtpub.java7.concurrency.chapter7.recipe08.task.Task;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		 * Create a new MyLock object 
		 */
		MyLock lock=new MyLock();
		
		/*
		 * Create and run ten task objects
		 */
		for (int i=0; i<10; i++){
			Task task=new Task("Task-"+i,lock);
			Thread thread=new Thread(task);
			thread.start();
		}
		
		/*
		 * The main thread also tries to get the lock
		 */
		boolean value;
		do {
			try {
				value=lock.tryLock(1,TimeUnit.SECONDS);
				if (!value) {
					System.out.printf("Main: Trying to get the Lock\n");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				value=false;
			}
		} while (!value);
		
		/*
		 * The main thread release the lock
		 */
		System.out.printf("Main: Got the lock\n");
		lock.unlock();
		
		/*
		 * Write a message in the console indicating the end of the program
		 */
		System.out.printf("Main: End of the program\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe07/src/com/packtpub/java7/concurrency/chapter7/reciper07/task/MyWorkerTask.java
package com.packtpub.java7.concurrency.chapter7.reciper07.task;
import java.util.Date;
import java.util.concurrent.ForkJoinTask;

/**
 * This class extends the ForkJoinTask class to implement your own version of a task running 
 * in a ForkJoinPool of the Frok/Join framework. It's equivalent to the RecursiveAction and
 * Recursive classes. As the RecursiveAction class, it doesn't return any result
 *
 */
public abstract class MyWorkerTask extends ForkJoinTask<Void> {

	/**
	 * Serial Version UID of the class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the task 
	 */
	private String name;
	
	/**
	 * Constructor of the class. Initializes its attributes 
	 * @param name Name of the task
	 */
	public MyWorkerTask(String name) {
		this.name=name;
	}

	/**
	 * Method that returns the result of the task. In this case, as 
	 * the task doesn't return a result, it returns a null value
	 */
	@Override
	public Void getRawResult() {
		return null;
	}

	/**
	 * Method that establish the result of the task. In this case, as
	 * the task doesn't return a result, this method is empty
	 */
	@Override
	protected void setRawResult(Void value) {
		
	}

	/**
	 * Main method of the task. Is called by the Fork/Join pool. It calls
	 * the compute() method that is an abstract method that have to be
	 * implemented by the tasks that extend this class, calculating its execution
	 * time and writing it in the console
	 */
	@Override
	protected boolean exec() {
		Date startDate=new Date();
		compute();
		Date finishDate=new Date();
		long diff=finishDate.getTime()-startDate.getTime();
		System.out.printf("MyWorkerTask: %s : %d Milliseconds to complete.\n",name,diff);
		return true;
	}

	/**
	 * Method that returns the name of the console
	 * @return The name of the task
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Main method of the child tasks. It has to be overridden in the child classes 
	 * and implement on it its main logic
	 */
	protected abstract void compute();
}

//=*=*=*=*
//./Chapter_7/ch7_recipe07/src/com/packtpub/java7/concurrency/chapter7/reciper07/task/Task.java
package com.packtpub.java7.concurrency.chapter7.reciper07.task;

/**
 * Task that extends the MyWorkerTask class to be executed
 * in a Fork/Join framework
 *
 */
public class Task extends MyWorkerTask {

	/**
	 * Serival Version UID of the task
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Array of integers. This task will increment all the elements of the array
	 */
	private int array[];
	/**
	 * First element of the array that this task is going to increment
	 */
	private int start;

	/**
	 * Last element of the array that this task is going to increment
	 */
	private int end;
	
	/**
	 * Constructor of the class. It initializes its attributes
	 * @param name Name of the task
	 * @param array Array of elements that is going to be incremented
	 * @param start First element of the array to be incremented by this task
	 * @param end Last element of the array to be incremented by this task
	 */
	public Task(String name, int array[], int start, int end){
		super(name);
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	/**
	 * Main method of the task. If the task has to increment less that 100
	 * elements, it increments them directly. Else, it divides the
	 * operation in two subtasks
	 */
	@Override
	protected void compute() {
		if (end-start>100){
			int mid=(end+start)/2;
			Task task1=new Task(this.getName()+"1",array,start,mid);
			Task task2=new Task(this.getName()+"2",array,mid,end);
			invokeAll(task1,task2);
		} else {
			for (int i=start; i<end; i++) {
				array[i]++;
			}			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe07/src/com/packtpub/java7/concurrency/chapter7/reciper07/core/Main.java
package com.packtpub.java7.concurrency.chapter7.reciper07.core;

import java.util.concurrent.ForkJoinPool;

import com.packtpub.java7.concurrency.chapter7.reciper07.task.Task;

/**
 * Main class of the example. It creates a ForkJoinPool and a 
 * Task and executes the task in the pool
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		/*
		 * Create an array of 10000 elements
		 */
		int array[]=new int[10000];
		
		/*
		 * ForkJoinPool to execute the task 
		 */
		ForkJoinPool pool=new ForkJoinPool();
		
		/*
		 * Task to increment the elements of the array
		 */
		Task task=new Task("Task",array,0,array.length);
		
		/*
		 * Send the task to the pool
		 */
		pool.invoke(task);
		
		/*
		 * Shutdown the pool
		 */
		pool.shutdown();
		
		/*
		 * Write a message in the console
		 */
		System.out.printf("Main: End of the program.\n");
		
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe04/src/com/packtpub/java7/concurrency/chapter7/recipe04/task/MyTask.java
package com.packtpub.java7.concurrency.chapter7.recipe04.task;

import java.util.concurrent.TimeUnit;

/**
 * Task to check the MyThread and MyThreadFactory classes. It sleeps
 * the thread for two seconds
 *
 */
public class MyTask implements Runnable {

	/**
	 * Main method of the task. It sleeps the thread for two seconds
	 */
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe04/src/com/packtpub/java7/concurrency/chapter7/recipe04/task/MyThread.java
package com.packtpub.java7.concurrency.chapter7.recipe04.task;

import java.util.Date;

/**
 * This class implement your own Thread. It stores the creation date, the
 * start date and the finish date of the thread. It provides a mehtod that
 * calculates the execution time of the thread. Overrides the toString() method
 * to return information about the creationDate and the execution time of the thread
 */
public class MyThread extends Thread {
	
	/**
	 * Creation date of the thread
	 */
	private Date creationDate;
	
	/**
	 * Start date of the thread
	 */
	private Date startDate;
	
	/**
	 * Finish date of the thread
	 */
	private Date finishDate;
	
	/**
	 * Constructor of the class. It establishes the value of the creation date attribute
	 * @param target Runnable object that this thread is going to execute
	 * @param name Name of the thread
	 */
	public MyThread(Runnable target, String name ){
		super(target,name);
		setCreationDate();
	}

	/**
	 * Main method of the thread. Stores the start and finish date of the thread and calls
	 * the run() method of its parent class
	 */
	@Override
	public void run() {
		setStartDate();
		super.run();
		setFinishDate();
		System.out.printf("Thread: %s\n",toString());
	}
	
	/**
	 * Method that establish the creation date of the thread
	 */
	public void setCreationDate() {
		creationDate=new Date();
	}
	
	/**
	 * Method that establish the start date of the thread
	 */
	public void setStartDate() {
		startDate=new Date();
	}
	
	/**
	 * Method that establish the finish date of the thread
	 */
	public void setFinishDate() {
		finishDate=new Date();
	}
	
	/**
	 * Method that calculates the execution time of the thread as the difference
	 * between the finish date and the start date.
	 * @return
	 */
	public long getExecutionTime() {
		long ret;
		ret=finishDate.getTime()-startDate.getTime();
		return ret;
	}
	
	/**
	 * Method that generates a String with information about the creation date and the
	 * execution time of the thread
	 */
	public String toString(){
		StringBuffer buffer=new StringBuffer();
		buffer.append(getName());
		buffer.append(": ");
		buffer.append(" Creation Date: ");
		buffer.append(creationDate);
		buffer.append(" : Running time: ");
		buffer.append(getExecutionTime());
		buffer.append(" Milliseconds.");
		return buffer.toString();
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe04/src/com/packtpub/java7/concurrency/chapter7/recipe04/task/MyThreadFactory.java
package com.packtpub.java7.concurrency.chapter7.recipe04.task;

import java.util.concurrent.ThreadFactory;

/**
 * Factory to create our kind of threads. Implement the
 * ThreadFactory interface.  
 *
 */
public class MyThreadFactory implements ThreadFactory {

	/**
	 * Attribute to store the number of threads created by the Factory
	 */
	private int counter;
	
	/**
	 * Prefix to use in the name of the threads created by the Factory
	 */
	private String prefix;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param prefix Prefix to use in the name of the threads
	 */
	public MyThreadFactory (String prefix) {
		this.prefix=prefix;
		counter=1;
	}
	
	/**
	 * Method that create a new MyThread object to execute the Runnable
	 * object that receives as parameter 
	 */
	@Override
	public Thread newThread(Runnable r) {
		MyThread myThread=new MyThread(r,prefix+"-"+counter);
		counter++;
		return myThread;
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe04/src/com/packtpub/java7/concurrency/chapter7/recipe04/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe04.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe04.task.MyThreadFactory;
import com.packtpub.java7.concurrency.chapter7.recipe04.task.MyTask;

/**
 * Main class of the example. Creates a Factory, an Executor using
 * that factory and submits a task to the executor
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		/*
		 * Create a new MyThreadFactory object
		 */
		MyThreadFactory threadFactory=new MyThreadFactory("MyThreadFactory");
		
		/*
		 * Create a new ThreadPoolExecutor and configure it for use the 
		 * MyThreadFactoryObject created before as the factory to create the threads
		 */
		ExecutorService executor=Executors.newCachedThreadPool(threadFactory);
		
		/*
		 * Create a new Task object
		 */
		MyTask task=new MyTask();
		
		/*
		 * Submit the task 
		 */
		executor.submit(task);
		
		/*
		 * Shutdown the executor
		 */
		executor.shutdown();
		
		/*
		 * Wait for the finalization of the executor
		 */
		executor.awaitTermination(1, TimeUnit.DAYS);
		
		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
		
		

	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe06/src/com/packtpub/java7/concurrency/chapter7/recipe06/task/MyWorkerThread.java
package com.packtpub.java7.concurrency.chapter7.recipe06.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * This class implements a custom thread for the Fork/Join framework. It extends the
 * ForkJoinWorkerThread that is the default implementation of the threads that executes
 * the tasks in the Fork/Join Framework. This custom thread counts the number of tasks
 * executed in it
 *
 */
public class MyWorkerThread extends ForkJoinWorkerThread {

	/**
	 * ThreadLocal attribute to store the number of tasks executed by each thread
	 */
	private static ThreadLocal<Integer> taskCounter=new ThreadLocal<>();

	/**
	 * Constructor of the class. It calls the constructor of its parent class using the
	 * super keyword
	 * @param pool ForkJoinPool where the thread will be executed
	 */
	protected MyWorkerThread(ForkJoinPool pool) {
		super(pool);
	}

	/**
	 * This method is called when a worker thread of the Fork/Join framework begins its execution.
	 * It initializes its task counter
	 */
	@Override
	protected void onStart() {
		super.onStart();
		System.out.printf("MyWorkerThread %d: Initializing task counter.\n",getId());
		taskCounter.set(0);
	}

	/**
	 * This method is called when a worker thread of the Fork/Join framework ends its execution.
	 * It writes in the console the value of the taskCounter attribute.
	 */
	@Override
	protected void onTermination(Throwable exception) {
		System.out.printf("MyWorkerThread %d: %d\n",getId(),taskCounter.get());
		super.onTermination(exception);
	}
	
	/**
	 * This method is called for each task to increment the task counter of the worker thread
	 */
	public void addTask(){
		int counter=taskCounter.get().intValue();
		counter++;
		taskCounter.set(counter);
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe06/src/com/packtpub/java7/concurrency/chapter7/recipe06/task/MyRecursiveTask.java
package com.packtpub.java7.concurrency.chapter7.recipe06.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Task that will be executed in the Fork/Join framework. It calculates 
 * the sum of all array elements
 *
 */
public class MyRecursiveTask extends RecursiveTask<Integer> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Array to be summed
	 */
	private int array[];
	
	/**
	 * Start and end positions of the part of the array to be summed by this task
	 */
	private int start, end;
	
	/**
	 * Constructor of the class. It initializes the  attributes of the task
	 * @param array Array to be summed
	 * @param start Start position of the block of the array to be summed by this task
	 * @param end End position of the block of the array to be summed by this task
	 */
	public MyRecursiveTask(int array[],int start, int end) {
		this.array=array;
		this.start=start;
		this.end=end;
	}

	/**
	 * Main method of the task. If the task has less than 100 elements to sum, it calculates
	 * the sum of these elements directly. Else, it creates two subtask to process the two
	 * halves of the block.
	 * 
	 * It also calls the addTask() method of the thread that is executing the task to
	 * updates its internal counter of tasks
	 */
	@Override
	protected Integer compute() {
		Integer ret;
		MyWorkerThread thread=(MyWorkerThread)Thread.currentThread();
		thread.addTask();
		if (end-start>100) {
			int mid=(start+end)/2;
			MyRecursiveTask task1=new MyRecursiveTask(array,start,mid);
			MyRecursiveTask task2=new MyRecursiveTask(array,mid,end);
			invokeAll(task1,task2);
			ret=addResults(task1,task2);
		} else {
			int add=0;
			for (int i=start; i<end; i++) {
				add+=array[i];
			}
			ret=new Integer(add);
		}
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * Method that adds the results of the two subtasks create by this task
	 * @param task1 First task
	 * @param task2 Second task
	 * @return The sum of the results of the two tasks
	 */
	private Integer addResults(MyRecursiveTask task1, MyRecursiveTask task2) {
		int value;
		try {
			value = task1.get().intValue()+task2.get().intValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
			value=0;
		} catch (ExecutionException e) {
			e.printStackTrace();
			value=0;
		}
		return new Integer(value);
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe06/src/com/packtpub/java7/concurrency/chapter7/recipe06/task/MyWorkerThreadFactory.java
package com.packtpub.java7.concurrency.chapter7.recipe06.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

/**
 * Factory to be used by the Fork/Join framework to create the worker threads. Implements
 * the ForkJoinWorkerThreadFactory interface
 *
 */
public class MyWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

	/**
	 * Method that creates a worker thread for the Fork/Join framework
	 * @param pool ForkJoinPool where the thread will be executed
	 * @return a MyWorkerThread thread
	 */
	@Override
	public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
		return new MyWorkerThread(pool);
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe06/src/com/packtpub/java7/concurrency/chapter7/recipe06/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe06.core;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe06.task.MyRecursiveTask;
import com.packtpub.java7.concurrency.chapter7.recipe06.task.MyWorkerThreadFactory;

/**
 * Main class of the example. It creates an array of 100000 elements, initializes all
 * the elements to the 1 value, creates a new ForkJoinPool with the new 
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * Create a new MyWorkerThreadFactory
		 */
		MyWorkerThreadFactory factory=new MyWorkerThreadFactory();
		
		/*
		 * Create new ForkJoinPool, passing as parameter the factory created before
		 */
		ForkJoinPool pool=new ForkJoinPool(4, factory, null, false);
		
		/*
		 * Create and initializes the elements of the array
		 */
		int array[]=new int[100000];
		
		for (int i=0; i<array.length; i++){
			array[i]=1;
		}
		
		/*
		 * Create a new Task to sum the elements of the array
		 */
		MyRecursiveTask task=new MyRecursiveTask(array,0,array.length);
		
		/*
		 * Send the task to the ForkJoinPool 
		 */
		pool.execute(task);
		
		
		/*
		 * Wait for the finalization of the task
		 */
		task.join();
		
		/*
		 * Shutdown the pool
		 */
		pool.shutdown();
		
		/*
		 * Wait for the finalization of the pool
		 */
		pool.awaitTermination(1, TimeUnit.DAYS);
		
		/*
		 * Write the result of the task
		 */
		System.out.printf("Main: Result: %d\n",task.get());
		
		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe05/src/com/packtpub/java7/concurrency/chapter7/recipe05/task/MyScheduledTask.java
package com.packtpub.java7.concurrency.chapter7.recipe05.task;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * This class implements an scheduled task to be execute in a scheduled thread pool executor. It's
 * a parameterized class where V is the type of data that will be returned by the task. 
 * 
 * An scheduled thread pool executor can execute two kinds of tasks:
 * 		Delayed Tasks: This kind of tasks are executed once after a period of time.
 * 		Periodic Tasks: This kind of tasks are executed from time to time
 * @param <V> Type of data that will be returned by the task
 * 
 */
public class MyScheduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {

	/**
	 * Attribute to store the task that will be used to create a MyScheduledTask
	 */
	private RunnableScheduledFuture<V> task;
	
	/**
	 * ScheduledThreadPoolExecutor that is going to execute the task
	 */
	private ScheduledThreadPoolExecutor executor;
	
	/**
	 * Period of time between two executions of the task
	 */
	private long period;
	
	/**
	 * Date when will begin the next execution of the task
	 */
	private long startDate;
	
	/**
	 * Constructor of the class. It initializes the attributes of the class
	 * @param runnable Runnable submitted to be executed by the task
	 * @param result Result that will be returned by the task
	 * @param task Task that will execute the Runnable object
	 * @param executor Executor that is going to execute the task
	 */
	public MyScheduledTask(Runnable runnable, V result, RunnableScheduledFuture<V> task, ScheduledThreadPoolExecutor executor) {
		super(runnable, result);
		this.task=task;
		this.executor=executor;
	}

	/**
	 * Method that returns the reminder for the next execution of the task. If is 
	 * a delayed task, returns the delay of the original task. Else, return the difference
	 * between the startDate attribute and the actual date.
	 * @param unit TimeUnit to return the result
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		if (!isPeriodic()) {
			return task.getDelay(unit);
		} else {
			if (startDate==0){
				return task.getDelay(unit);
			} else {
				Date now=new Date();
				long delay=startDate-now.getTime();
				return unit.convert(delay, TimeUnit.MILLISECONDS);
			}
		}
	}

	/**
	 * Method to compare two tasks. It calls the compareTo() method of the original task
	 */
	@Override
	public int compareTo(Delayed o) {
		return task.compareTo(o);
	}

	/**
	 * Method that returns if the task is periodic or not. It calls the isPeriodic() method
	 * of the original task
	 */
	@Override
	public boolean isPeriodic() {
		return task.isPeriodic();
	}

	
	/**
	 * Method that executes the task. If it's a periodic task, it updates the 
	 * start date of the task and store in the queue of the executor the task to
	 * be executed again
	 */
	@Override
	public void run() {
		if (isPeriodic() && (!executor.isShutdown())) {
			Date now=new Date();
			startDate=now.getTime()+period;
			executor.getQueue().add(this);
		}
		System.out.printf("Pre-MyScheduledTask: %s\n",new Date());
		System.out.printf("MyScheduledTask: Is Periodic: %s\n",isPeriodic());
		super.runAndReset();
		System.out.printf("Post-MyScheduledTask: %s\n",new Date());
	}

	/**
	 * Method that establish the period of the task for periodic tasks
	 * @param period
	 */
	public void setPeriod(long period) {
		this.period=period;
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe05/src/com/packtpub/java7/concurrency/chapter7/recipe05/task/Task.java
package com.packtpub.java7.concurrency.chapter7.recipe05.task;

import java.util.concurrent.TimeUnit;

/**
 * Runnable object to check the MyScheduledTask and MyScheduledThreadPoolExecutor classes.
 *
 */
public class Task implements Runnable {

	/**
	 * Main method of the task. Writes a message, sleeps the current thread for two seconds and
	 * writes another message
	 */
	@Override
	public void run() {
		System.out.printf("Task: Begin.\n");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Task: End.\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe05/src/com/packtpub/java7/concurrency/chapter7/recipe05/task/MyScheduledThreadPoolExecutor.java
package com.packtpub.java7.concurrency.chapter7.recipe05.task;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Our implementation of an ScheduledThreadPoolExecutor two executes MyScheduledTasks tasks. It extends
 * the ScheduledThreadPoolExecutor class
 *
 */
public class MyScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	/**
	 * Constructor of the class. Calls the constructor of its parent class using the super keyword
	 * @param corePoolSize Number of threads to keep in the pool
	 */
	public MyScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
	}


	/**
	 * Method that converts a RunnableScheduledFuture task in a MyScheduledTask task
	 */
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable,
			RunnableScheduledFuture<V> task) {
		MyScheduledTask<V> myTask=new MyScheduledTask<V>(runnable, null, task,this);	
		return myTask;
	}


	/**
	 * Method that schedule in the executor a periodic tasks. It calls the method of its parent class using
	 * the super keyword and stores the period of the task.
	 */
	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
			long initialDelay, long period, TimeUnit unit) {
		ScheduledFuture<?> task= super.scheduleAtFixedRate(command, initialDelay, period, unit);
		MyScheduledTask<?> myTask=(MyScheduledTask<?>)task;
		myTask.setPeriod(TimeUnit.MILLISECONDS.convert(period,unit));
		return task;
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe05/src/com/packtpub/java7/concurrency/chapter7/recipe05/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe05.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter7.recipe05.task.MyScheduledThreadPoolExecutor;
import com.packtpub.java7.concurrency.chapter7.recipe05.task.Task;

/**
 * Main class of the example. Creates a MyScheduledThreadPoolExecutor and
 * executes a delayed task and a periodic task in it.
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		/*
		 * Create a MyScheduledThreadPool object
		 */
		MyScheduledThreadPoolExecutor executor=new MyScheduledThreadPoolExecutor(2);
		
		/*
		 * Create a task object  
		 */
		Task task=new Task();
		
		/*
		 * Write the start date of the execution
		 */
		System.out.printf("Main: %s\n",new Date());
		
		/*
		 * Send to the executor a delayed task. It will be executed after 1 second of delay
		 */
		executor.schedule(task, 1, TimeUnit.SECONDS);
		
		/*
		 * Sleeps the thread three seconds 
		 */
		TimeUnit.SECONDS.sleep(3);
		
		/*
		 * Create another task
		 */
		task=new Task();
		
		/*
		 * Write the actual date again
		 */
		System.out.printf("Main: %s\n",new Date());
		
		/*
		 * Send to the executor a delayed task. It will begin its execution after 1 second of dealy
		 * and then it will be executed every three seconds
		 */
		executor.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);
		
		/*
		 * Sleep the thread during ten seconds
		 */
		TimeUnit.SECONDS.sleep(10);

		/*
		 * Shutdown the executor
		 */
		executor.shutdown();
		
		/*
		 * Wait for the finalization of the executor
		 */
		executor.awaitTermination(1, TimeUnit.DAYS);
		
		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe10/src/com/packtpub/java7/concurrency/chapter7/recipe10/task/Sensor2.java
package com.packtpub.java7.concurrency.chapter7.recipe10.task;

/**
 * Class that simulates a sensor in the doors of the parking
 *
 */
public class Sensor2 implements Runnable {

	/**
	 * Counter of cars in the parking
	 */
	private ParkingCounter counter;
	
	/**
	 * Constructor of the class. It initializes its attributes
	 * @param counter Counter of cars in the parking
	 */
	public Sensor2(ParkingCounter counter) {
		this.counter=counter;
	}
	
	/**
	 * Main method of the sensor. Simulates the traffic in the door of the parking
	 */
	@Override
	public void run() {
		counter.carIn();
		counter.carOut();
		counter.carOut();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe10/src/com/packtpub/java7/concurrency/chapter7/recipe10/task/Sensor1.java
package com.packtpub.java7.concurrency.chapter7.recipe10.task;

/**
 * Class that simulates a sensor in the doors of the parking
 *
 */
public class Sensor1 implements Runnable {

	/**
	 * Counter of cars in the parking
	 */
	private ParkingCounter counter;
	
	/**
	 * Constructor of the class. It initializes its attributes
	 * @param counter Counter of cars in the parking
	 */
	public Sensor1(ParkingCounter counter) {
		this.counter=counter;
	}
	

	/**
	 * Main method of the sensor. Simulates the traffic in the door of the parking
	 */
	@Override
	public void run() {
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carOut();
		counter.carOut();
		counter.carOut();
		counter.carIn();
		counter.carIn();
		counter.carIn();
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe10/src/com/packtpub/java7/concurrency/chapter7/recipe10/task/ParkingCounter.java
package com.packtpub.java7.concurrency.chapter7.recipe10.task;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements an atomic object extending the
 * AtomicInteger class and  providing two additional operations
 *  
 */
public class ParkingCounter extends AtomicInteger {

	/**
	 * Serial Version UID of the class 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Max number accepted by this counter
	 */
	private int maxNumber;
	
	/**
	 * Constructor of the class
	 * @param maxNumber Max number accepter by this counter
	 */
	public ParkingCounter(int maxNumber){
		set(0);
		this.maxNumber=maxNumber;
	}
	
	/**
	 * Method that increments the internal counter if it has
	 * a value less than the maximum. Is implemented to be and
	 * atomic operation
	 * @return True if the car can enter in the parking, false if not.
	 */
	public boolean carIn() {
		for (;;) {
			int value=get();
			if (value==maxNumber) {
				System.out.printf("ParkingCounter: The parking is full.\n");
				return false;
			} else {
				int newValue=value+1;
				boolean changed=compareAndSet(value,newValue);
				if (changed) {
					System.out.printf("ParkingCounter: A car has entered.\n");
					return true;
				}
			}
		}
	}

	/**
	 * Method that decrements the internal counter if it has
	 * a value bigger than 0. Is implemented to be and
	 * atomic operation
	 * @return True if the car leave the parking, false if there are 0 cars 
	 * in the parking
	 */
	public boolean carOut() {
		for (;;) {
			int value=get();
			if (value==0) {
				System.out.printf("ParkingCounter: The parking is empty.\n");
				return false;
			} else {
				int newValue=value-1;
				boolean changed=compareAndSet(value,newValue);
				if (changed) {
					System.out.printf("ParkingCounter: A car has gone out.\n");
					return true;
				}
			}
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe10/src/com/packtpub/java7/concurrency/chapter7/recipe10/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe10.core;

import com.packtpub.java7.concurrency.chapter7.recipe10.task.ParkingCounter;
import com.packtpub.java7.concurrency.chapter7.recipe10.task.Sensor1;
import com.packtpub.java7.concurrency.chapter7.recipe10.task.Sensor2;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		/*
		 * Create a ParkingCounter object
		 */
		ParkingCounter counter=new ParkingCounter(5);
		
		/*
		 * Create and launch two sensors
		 */
		Sensor1 sensor1=new Sensor1(counter);
		Sensor2 sensor2=new Sensor2(counter);
		
		Thread thread1=new Thread(sensor1);
		Thread thread2=new Thread(sensor2);
		
		thread1.start();
		thread2.start();
		
		/*
		 * Wait for the finalization of the threads
		 */
		thread1.join();
		thread2.join();
		
		/*
		 * Write in the console the number of cars in the parking
		 */
		System.out.printf("Main: Number of cars: %d\n",counter.get());
		
		/*
		 * Writ a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe03/src/com/packtpub/java7/concurrency/chapter7/recipe03/task/MyTask.java
package com.packtpub.java7.concurrency.chapter7.recipe03.task;

import java.util.concurrent.TimeUnit;


/**
 * Task to be executed in the MyThread threads
 *
 */
public class MyTask implements Runnable {

	/**
	 * Main method of the Thread. Sleeps the thread during two seconds
	 */
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe03/src/com/packtpub/java7/concurrency/chapter7/recipe03/task/MyThread.java
package com.packtpub.java7.concurrency.chapter7.recipe03.task;

import java.util.Date;

/**
 * This class extends the Thread class calculating its execution time
 *
 */
public class MyThread extends Thread {
	
	/**
	 * Creation date of the Thread
	 */
	private Date creationDate;
	
	/**
	 * Start date of the Thread
	 */
	private Date startDate;
	
	/**
	 * Finish date of the Thread
	 */
	private Date finishDate;
	
	/**
	 * Constructor of the class. Use the constructor of the Thread class and storeas the creation date of the Thread
	 * @param target Task to execute
	 * @param name Name of the thread
	 */
	public MyThread(Runnable target, String name ){
		super(target,name);
		setCreationDate();
	}

	/**
	 * Main method of the thread. Stores the start date and the finish date and calls the run() method of the parent class
	 */
	@Override
	public void run() {
		setStartDate();
		super.run();
		setFinishDate();
	}
	
	/**
	 * Method that establish the value of the creation date
	 */
	public void setCreationDate() {
		creationDate=new Date();
	}
	
	/**
	 * Method that establish the value of the start date
	 */
	public void setStartDate() {
		startDate=new Date();
	}
	
	/**
	 * Method that establish the value of the finish date
	 */
	public void setFinishDate() {
		finishDate=new Date();
	}
	
	/**
	 * Method that calculates the execution time of the thread
	 * @return The execution time of the thread
	 */
	public long getExecutionTime() {
		return finishDate.getTime()-startDate.getTime();
	}
	
	/**
	 * Method that writes information about the thread
	 */
	@Override
	public String toString(){
		StringBuilder buffer=new StringBuilder();
		buffer.append(getName());
		buffer.append(": ");
		buffer.append(" Creation Date: ");
		buffer.append(creationDate);
		buffer.append(" : Running time: ");
		buffer.append(getExecutionTime());
		buffer.append(" Milliseconds.");
		return buffer.toString();
	}
}

//=*=*=*=*
//./Chapter_7/ch7_recipe03/src/com/packtpub/java7/concurrency/chapter7/recipe03/task/MyThreadFactory.java
package com.packtpub.java7.concurrency.chapter7.recipe03.task;

import java.util.concurrent.ThreadFactory;

/**
 * Factory to create MyThread objects
 *
 */
public class MyThreadFactory implements ThreadFactory {

	/**
	 * Attribute to store the number of threads created in this factory
	 */
	private int counter;
	
	/**
	 * String to create the name of the threads created with this factory
	 */
	private String prefix;
	
	/**
	 * Constructor of the class. Initialize its parameters
	 * @param prefix First part of the name of the threads created with this factory
	 */
	public MyThreadFactory (String prefix) {
		this.prefix=prefix;
		counter=1;
	}
	
	/**
	 * Method that creates a new MyThread thread
	 */
	@Override
	public Thread newThread(Runnable r) {
		MyThread myThread=new MyThread(r,prefix+"-"+counter);
		counter++;
		return myThread;
	}

}

//=*=*=*=*
//./Chapter_7/ch7_recipe03/src/com/packtpub/java7/concurrency/chapter7/recipe03/core/Main.java
package com.packtpub.java7.concurrency.chapter7.recipe03.core;

import com.packtpub.java7.concurrency.chapter7.recipe03.task.MyThreadFactory;
import com.packtpub.java7.concurrency.chapter7.recipe03.task.MyTask;

/**
 * Main class of the example. Creates a factory, a MyThread object to execute a Task object
 * and executes the Thread
 * 	
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Create a Factory
		 */
		MyThreadFactory myFactory=new MyThreadFactory("MyThreadFactory");
	
		/*
		 * Crate a Task
		 */
		MyTask task=new MyTask();
		
		/*
		 * Create a Thread using the Factory to execute the Task
		 */
		Thread thread=myFactory.newThread(task);
		
		/*
		 * Start the Thread
		 */
		thread.start();
		
		/*
		 * Wait for the finalization of the Thread
		 */
		thread.join();
		
		/*
		 * Write the thread info to the console
		 */
		System.out.printf("Main: Thread information.\n");
		System.out.printf("%s\n",thread);
		System.out.printf("Main: End of the example.\n");

	}

}

//=*=*=*=*
//./Appendix/app_recipe02/src/com/packtpub/java7/concurrency/chapter9/recipe02/GoodLocks.java
package com.packtpub.java7.concurrency.chapter9.recipe02;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class GoodLocks {
	private Lock lock1, lock2;
	
	public GoodLocks(Lock lock1, Lock lock2) {
		this.lock1=lock1;
		this.lock2=lock2;
	}
	
	public void operation1(){
		lock1.lock();
		lock2.lock();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock2.unlock();
			lock1.unlock();
		}
		
	}

	public void operation2(){
		lock1.lock();
		lock2.lock();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock2.unlock();
			lock1.unlock();
		}
	}
}

//=*=*=*=*
//./Appendix/app_recipe02/src/com/packtpub/java7/concurrency/chapter9/recipe02/BadLocks.java
package com.packtpub.java7.concurrency.chapter9.recipe02;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class BadLocks {

	private Lock lock1, lock2;
	
	public BadLocks(Lock lock1, Lock lock2) {
		this.lock1=lock1;
		this.lock2=lock2;
	}
	
	public void operation1(){
		lock1.lock();
		lock2.lock();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock2.unlock();
			lock1.unlock();
		}
		
	}

	public void operation2(){
		lock2.lock();
		lock1.lock();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock1.unlock();
			lock2.unlock();
		}
		
	}

}

//=*=*=*=*
//./Appendix/app_recipe08/src/com/packtpub/java7/concurrency/chapter9/recipe09/task/Task.java
package com.packtpub.java7.concurrency.chapter9.recipe09.task;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	private int array[];
	
	public Task(int array[]) {
		this.array=array;
	}
	
	@Override
	public void run() {
		for (int i=0; i<array.length; i++ ){
			array[i]++;
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Appendix/app_recipe08/src/com/packtpub/java7/concurrency/chapter9/recipe09/task/TaskFJ.java
package com.packtpub.java7.concurrency.chapter9.recipe09.task;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class TaskFJ extends RecursiveAction {


	private static final long serialVersionUID = 1L;
	private int array[];
	private int start, end;
	
	public TaskFJ(int array[], int start, int end) {
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	@Override
	protected void compute() {
		if (end-start>1000) {
			int mid=(start+end)/2;
			TaskFJ task1=new TaskFJ(array,start,mid);
			TaskFJ task2=new TaskFJ(array,mid,end);
			task1.fork();
			task2.fork();
			task1.join();
			task2.join();
		} else {
			for (int i=start; i<end; i++) {
				array[i]++;
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

//=*=*=*=*
//./Appendix/app_recipe08/src/com/packtpub/java7/concurrency/chapter9/recipe09/core/Main.java
package com.packtpub.java7.concurrency.chapter9.recipe09.core;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter9.recipe09.task.Task;
import com.packtpub.java7.concurrency.chapter9.recipe09.task.TaskFJ;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int array[]=new int[100000];
		Task task=new Task(array);
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		Date start,end;
		start=new Date();
		executor.execute(task);
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end=new Date();
		System.out.printf("Main: Executor: %d\n",(end.getTime()-start.getTime()));
		
		TaskFJ taskFJ=new TaskFJ(array,1,100000);
		ForkJoinPool pool=new ForkJoinPool();
		start=new Date();
		pool.execute(taskFJ);
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end=new Date();
		System.out.printf("Core: Fork/Join: %d\n",(end.getTime()-start.getTime()));
		
	}

}

//=*=*=*=*
//./Appendix/app_recipe01/src/com/packtpub/java7/concurrency/chapter9/recipe01/PersonMutable.java
package com.packtpub.java7.concurrency.chapter9.recipe01;

public class PersonMutable {
	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}

//=*=*=*=*
//./Appendix/app_recipe01/src/com/packtpub/java7/concurrency/chapter9/recipe01/PersonImmutable.java
package com.packtpub.java7.concurrency.chapter9.recipe01;

public final class PersonImmutable {
	
	final private String firstName;
	final private String lastName;
	
	public PersonImmutable (String firstName, String lastName, String address) {
		this.firstName=firstName;
		this.lastName=lastName;
	}

	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}
}

//=*=*=*=*
//./Appendix/app_recipe03/src/com/packtpub/java7/concurrency/chapter9/recipe03/task/TaskAtomic.java
package com.packtpub.java7.concurrency.chapter9.recipe03.task;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskAtomic implements Runnable {

	private AtomicInteger number;
	
	public TaskAtomic () {
		this.number=new AtomicInteger();
	}
	
	@Override
	public void run() {
		for (int i=0; i<1000000; i++) {
			number.set(i);
		}
	}

}

//=*=*=*=*
//./Appendix/app_recipe03/src/com/packtpub/java7/concurrency/chapter9/recipe03/task/TaskLock.java
package com.packtpub.java7.concurrency.chapter9.recipe03.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskLock implements Runnable {

	private Lock lock;
	private int number;
	
	public TaskLock() {
		this.lock=new ReentrantLock();
	}

	@Override
	public void run() {
		for (int i=0; i<1000000; i++) {
			lock.lock();
			number=i;
			lock.unlock();
		}
		
	}
	
	
}

//=*=*=*=*
//./Appendix/app_recipe03/src/com/packtpub/java7/concurrency/chapter9/recipe03/core/Main.java
package com.packtpub.java7.concurrency.chapter9.recipe03.core;

import java.util.Date;

import com.packtpub.java7.concurrency.chapter9.recipe03.task.TaskAtomic;
import com.packtpub.java7.concurrency.chapter9.recipe03.task.TaskLock;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TaskAtomic atomicTask=new TaskAtomic();

		TaskLock lockTask=new TaskLock();
		
		int numberThreads=50;
		Thread threads[]=new Thread[numberThreads];
		Date begin,end;
		
		begin=new Date();
		for (int i=0; i<numberThreads; i++) {
			threads[i]=new Thread(lockTask);
			threads[i].start();
		}

		for (int i=0; i<numberThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end=new Date();
		
		System.out.printf("Main: Lock results: %d\n",(end.getTime()-begin.getTime()));

		begin=new Date();
		for (int i=0; i<numberThreads; i++) {
			threads[i]=new Thread(atomicTask);
			threads[i].start();
		}

		for (int i=0; i<numberThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end=new Date();
		
		System.out.printf("Main: Atomic results: %d\n",(end.getTime()-begin.getTime()));	
	}

}

//=*=*=*=*
//./Appendix/app_recipe09/src/com/packtpub/java7/concurrency/chapter9/recipe10/task/Task.java
package com.packtpub.java7.concurrency.chapter9.recipe10.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Task implements Runnable {
	
	private Lock lock;
	
	public Task (Lock lock) {
		this.lock=lock;
	}
	
	@Override
	public void run() {
		System.out.printf("%s: Starting\n",Thread.currentThread().getName());
		lock.lock();
		
		criticalSection();
		
		System.out.printf("%s: Press a key to continue: \n",Thread.currentThread().getName());
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		try {
			String line=in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lock.unlock();
	}

	private void criticalSection() {
		Random random=new Random();
		int wait=random.nextInt(10);
		System.out.printf("%s: Wait for %d seconds\n",Thread.currentThread().getName(),wait);
		try {
			TimeUnit.SECONDS.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}

//=*=*=*=*
//./Appendix/app_recipe09/src/com/packtpub/java7/concurrency/chapter9/recipe10/core/Main.java
package com.packtpub.java7.concurrency.chapter9.recipe10.core;

import java.util.concurrent.locks.ReentrantLock;

import com.packtpub.java7.concurrency.chapter9.recipe10.task.Task;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReentrantLock lock=new ReentrantLock();
		for (int i=0; i<10; i++) {
			Task task=new Task(lock);
			Thread thread=new Thread(task);
			thread.start();
		}
	}

}

//=*=*=*=*
//./Appendix/app_recipe07/src/com/packtpub/java7/concurrncy/chapter9/recipe10/task/Task.java
package com.packtpub.java7.concurrncy.chapter9.recipe10.task;

import com.packtpub.java7.concurrncy.chapter9.recipe10.util.DBConnectionOK;

public class Task implements Runnable {

	@Override
	public void run() {

		System.out.printf("%s: Getting the connection...\n",Thread.currentThread().getName());
		DBConnectionOK connection=DBConnectionOK.getConnection();
		System.out.printf("%s: End\n",Thread.currentThread().getName());
	}

}

//=*=*=*=*
//./Appendix/app_recipe07/src/com/packtpub/java7/concurrncy/chapter9/recipe10/util/DBConnection.java
package com.packtpub.java7.concurrncy.chapter9.recipe10.util;

public class DBConnection {

	private static DBConnection connection;
	
	private DBConnection() {
		
	}
	
	public static DBConnection getConnection(){
		if (connection==null) {
			connection=new DBConnection();
		}
		return connection;
	}
}

//=*=*=*=*
//./Appendix/app_recipe07/src/com/packtpub/java7/concurrncy/chapter9/recipe10/util/DBConnectionOK.java
package com.packtpub.java7.concurrncy.chapter9.recipe10.util;

public class DBConnectionOK {

	private DBConnectionOK() {
		System.out.printf("%s: Connection created.\n",Thread.currentThread().getName());
	}

    private static class LazyDBConnection {
        private static final DBConnectionOK INSTANCE = new DBConnectionOK();
    }
    
    public static DBConnectionOK getConnection() {
        return LazyDBConnection.INSTANCE;
    }	
	
}

//=*=*=*=*
//./Appendix/app_recipe07/src/com/packtpub/java7/concurrncy/chapter9/recipe10/core/Main.java
package com.packtpub.java7.concurrncy.chapter9.recipe10.core;

import com.packtpub.java7.concurrncy.chapter9.recipe10.task.Task;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i=0; i<20; i++){
			Task task=new Task();
			Thread thread=new Thread(task);
			thread.start();
		}

	}

}

//=*=*=*=*
//./Appendix/app_recipe05/src/com/packtpub/java7/concurrency/chapter9/recipe06/task/Task.java
package com.packtpub.java7.concurrency.chapter9.recipe06.task;

public class Task implements Runnable {

	@Override
	public void run() {
		int r;
		for (int i=0; i<1000000; i++) {
			r=0;
			r++;
			r++;
			r*=r;
		}
	}

}

//=*=*=*=*
//./Appendix/app_recipe05/src/com/packtpub/java7/concurrency/chapter9/recipe06/core/Main.java
package com.packtpub.java7.concurrency.chapter9.recipe06.core;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter9.recipe06.task.Task;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread threads[]=new Thread[1000];
		Date start,end;
		
		start=new Date();
		for (int i=0; i<threads.length; i++) {
			Task task=new Task();
			threads[i]=new Thread(task);
			threads[i].start();
		}
		
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end=new Date();
		System.out.printf("Main: Threads: %d\n",(end.getTime()-start.getTime()));
		
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		start=new Date();
		for (int i=0; i<threads.length; i++) {
			Task task=new Task();
			executor.execute(task);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end=new Date();
		System.out.printf("Main: Executor: %d\n",(end.getTime()-start.getTime()));

	}

}

//=*=*=*=*
//./Appendix/app_recipe04/src/com/packtpub/java7/concurrency/chapter9/recipe05/task/Task2.java
package com.packtpub.java7.concurrency.chapter9.recipe05.task;

import java.util.concurrent.locks.Lock;

import com.packtpub.java7.concurrency.chapter9.recipe05.utils.Operations;

public class Task2 implements Runnable {

	private Lock lock;
	
	public Task2 (Lock lock) {
		this.lock=lock;
	}
	
	@Override
	public void run() {
		lock.lock();
		Operations.readData();
		lock.unlock();
		Operations.processData();
		lock.lock();
		Operations.writeData();
		lock.unlock();
	}
}

//=*=*=*=*
//./Appendix/app_recipe04/src/com/packtpub/java7/concurrency/chapter9/recipe05/task/Task1.java
package com.packtpub.java7.concurrency.chapter9.recipe05.task;

import java.util.concurrent.locks.Lock;

import com.packtpub.java7.concurrency.chapter9.recipe05.utils.Operations;

public class Task1 implements Runnable {

	private Lock lock;
	
	public Task1 (Lock lock) {
		this.lock=lock;
	}
	
	@Override
	public void run() {
		lock.lock();
		Operations.readData();
		Operations.processData();
		Operations.writeData();
		lock.unlock();
	}

}

//=*=*=*=*
//./Appendix/app_recipe04/src/com/packtpub/java7/concurrency/chapter9/recipe05/core/Main.java
package com.packtpub.java7.concurrency.chapter9.recipe05.core;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.packtpub.java7.concurrency.chapter9.recipe05.task.Task1;
import com.packtpub.java7.concurrency.chapter9.recipe05.task.Task2;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Lock lock=new ReentrantLock();
		Task1 task1=new Task1(lock);
		Task2 task2=new Task2(lock);
		Thread threads[]=new Thread[10];
		
		Date begin, end;
		
		begin=new Date();
		for (int i=0; i<threads.length; i++) {
			threads[i]=new Thread(task1);
			threads[i].start();
		}
		
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end=new Date();
		System.out.printf("Main: First Approach: %d\n",(end.getTime()-begin.getTime()));
		
		begin=new Date();
		for (int i=0; i<threads.length; i++) {
			threads[i]=new Thread(task2);
			threads[i].start();
		}
		
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end=new Date();
		System.out.printf("Main: Second Approach: %d\n",(end.getTime()-begin.getTime()));
		

	}

}

//=*=*=*=*
//./Appendix/app_recipe04/src/com/packtpub/java7/concurrency/chapter9/recipe05/utils/Operations.java
package com.packtpub.java7.concurrency.chapter9.recipe05.utils;

import java.util.concurrent.TimeUnit;

public class Operations {
	
	public static void readData(){
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeData(){
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void processData(){
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe07/src/com/packtpub/java7/concurrency/chapter1/recipe7/task/CleanerTask.java
package com.packtpub.java7.concurrency.chapter1.recipe7.task;

import java.util.Date;
import java.util.Deque;

import com.packtpub.java7.concurrency.chapter1.recipe7.event.Event;

/**
 * Class that review the Event data structure and delete
 * the events older than ten seconds
 *
 */
public class CleanerTask extends Thread {

	/**
	 * Data structure that stores events
	 */
	private Deque<Event> deque;

	/**
	 * Constructor of the class
	 * @param deque data structure that stores events
	 */
	public CleanerTask(Deque<Event> deque) {
		this.deque = deque;
		// Establish that this is a Daemon Thread
		setDaemon(true);
	}


	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		while (true) {
			Date date = new Date();
			clean(date);
		}
	}

	/**
	 * Method that review the Events data structure and delete
	 * the events older than ten seconds
	 * @param date
	 */
	private void clean(Date date) {
		long difference;
		boolean delete;
		
		if (deque.size()==0) {
			return;
		}
		
		delete=false;
		do {
			Event e = deque.getLast();
			difference = date.getTime() - e.getDate().getTime();
			if (difference > 10000) {
				System.out.printf("Cleaner: %s\n",e.getEvent());
				deque.removeLast();
				delete=true;
			}	
		} while (difference > 10000);
		if (delete){
			System.out.printf("Cleaner: Size of the queue: %d\n",deque.size());
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe07/src/com/packtpub/java7/concurrency/chapter1/recipe7/task/WriterTask.java
package com.packtpub.java7.concurrency.chapter1.recipe7.task;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe7.event.Event;

/**
 * Runnable class that generates and event every second
 *
 */
public class WriterTask implements Runnable {

	/**
	 * Data structure to stores the events
	 */
	Deque<Event> deque;
	
	/**
	 * Constructor of the class
	 * @param deque data structure that stores the event
	 */
	public WriterTask (Deque<Event> deque){
		this.deque=deque;
	}
	
	/**
	 * Main class of the Runnable
	 */
	@Override
	public void run() {
		
		// Writes 100 events
		for (int i=1; i<100; i++) {
			// Creates and initializes the Event objects 
			Event event=new Event();
			event.setDate(new Date());
			event.setEvent(String.format("The thread %s has generated an event",Thread.currentThread().getId()));
			
			// Add to the data structure
			deque.addFirst(event);
			try {
				// Sleeps during one second
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe07/src/com/packtpub/java7/concurrency/chapter1/recipe7/event/Event.java
package com.packtpub.java7.concurrency.chapter1.recipe7.event;

import java.util.Date;

/**
 * Class that stores event's information 
 *
 */
public class Event {

	/**
	 * Date of the event
	 */
	private Date date;
	
	/**
	 * Message of the event
	 */
	private String event;
	
	/**
	 * Reads the Date of the event
	 * @return the Date of the event
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Writes the Date of the event
	 * @param date the date of the event
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Reads the message of the event
	 * @return the message of the event
	 */
	public String getEvent() {
		return event;
	}
	
	/**
	 * Writes the message of the event
	 * @param event the message of the event
	 */
	public void setEvent(String event) {
		this.event = event;
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe07/src/com/packtpub/java7/concurrency/chapter1/recipe7/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe7.core;

import java.util.ArrayDeque;
import java.util.Deque;

import com.packtpub.java7.concurrency.chapter1.recipe7.event.Event;
import com.packtpub.java7.concurrency.chapter1.recipe7.task.CleanerTask;
import com.packtpub.java7.concurrency.chapter1.recipe7.task.WriterTask;

/**
 * Main class of the example. Creates three WriterTaks and a CleanerTask 
 *
 */
public class Main {

	/**
	 * Main method of the example. Creates three WriterTasks and a CleanerTask
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Creates the Event data structure
		Deque<Event> deque=new ArrayDeque<Event>();
		
		// Creates the three WriterTask and starts them
		WriterTask writer=new WriterTask(deque);
		for (int i=0; i<3; i++){
			Thread thread=new Thread(writer);
			thread.start();
		}
		
		// Creates a cleaner task and starts them
		CleanerTask cleaner=new CleanerTask(deque);
		cleaner.start();

	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe02/src/com/packtpub/java7/concurrency/chapter1/recipe2/task/Calculator.java
package com.packtpub.java7.concurrency.chapter1.recipe2.task;

/**
 * This class prints the multiplication table of a number
 *
 */
public class Calculator implements Runnable {

	/**
	 *  The number
	 */
	private int number;
	
	/**
	 *  Constructor of the class
	 * @param number : The number
	 */
	public Calculator(int number) {
		this.number=number;
	}
	
	/**
	 *  Method that do the calculations
	 */
	@Override
	public void run() {
		for (int i=1; i<=10; i++){
			System.out.printf("%s: %d * %d = %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe02/src/com/packtpub/java7/concurrency/chapter1/recipe2/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe2.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

import com.packtpub.java7.concurrency.chapter1.recipe2.task.Calculator;

/**
 *  Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Thread priority infomation 
		System.out.printf("Minimum Priority: %s\n",Thread.MIN_PRIORITY);
		System.out.printf("Normal Priority: %s\n",Thread.NORM_PRIORITY);
		System.out.printf("Maximun Priority: %s\n",Thread.MAX_PRIORITY);
		
		Thread threads[];
		Thread.State status[];
		
		// Launch 10 threads to do the operation, 5 with the max
		// priority, 5 with the min
		threads=new Thread[10];
		status=new Thread.State[10];
		for (int i=0; i<10; i++){
			threads[i]=new Thread(new Calculator(i));
			if ((i%2)==0){
				threads[i].setPriority(Thread.MAX_PRIORITY);
			} else {
				threads[i].setPriority(Thread.MIN_PRIORITY);
			}
			threads[i].setName("Thread "+i);
		}
		
		
		// Wait for the finalization of the threads. Meanwhile, 
		// write the status of those threads in a file
		try (FileWriter file = new FileWriter(".\\data\\log.txt");PrintWriter pw = new PrintWriter(file);){
			
			for (int i=0; i<10; i++){
				pw.println("Main : Status of Thread "+i+" : "+threads[i].getState());
				status[i]=threads[i].getState();
			}

			for (int i=0; i<10; i++){
				threads[i].start();
			}
			
			boolean finish=false;
			while (!finish) {
				for (int i=0; i<10; i++){
					if (threads[i].getState()!=status[i]) {
						writeThreadInfo(pw, threads[i],status[i]);
						status[i]=threads[i].getState();
					}
				}
				
				finish=true;
				for (int i=0; i<10; i++){
					finish=finish &&(threads[i].getState()==State.TERMINATED);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  This method writes the state of a thread in a file
	 * @param pw : PrintWriter to write the data
	 * @param thread : Thread whose information will be written
	 * @param state : Old state of the thread
	 */
	private static void writeThreadInfo(PrintWriter pw, Thread thread, State state) {
		pw.printf("Main : Id %d - %s\n",thread.getId(),thread.getName());
		pw.printf("Main : Priority: %d\n",thread.getPriority());
		pw.printf("Main : Old State: %s\n",state);
		pw.printf("Main : New State: %s\n",thread.getState());
		pw.printf("Main : ************************************\n");
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe12/src/com/packtpub/java7/concurrency/chapter1/recipe12/task/Task.java
package com.packtpub.java7.concurrency.chapter1.recipe12.task;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe12/src/com/packtpub/java7/concurrency/chapter1/recipe12/factory/MyThreadFactory.java
package com.packtpub.java7.concurrency.chapter1.recipe12.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Class that implements the ThreadFactory interface to
 * create a basic thread factory
 *
 */
public class MyThreadFactory implements ThreadFactory {

	// Attributes to save the necessary data to the factory
	private int counter;
	private String name;
	private List<String> stats;
	
	/**
	 * Constructor of the class
	 * @param name Base name of the Thread objects created by this Factory
	 */
	public MyThreadFactory(String name){
		counter=0;
		this.name=name;
		stats=new ArrayList<String>();
	}
	
	/**
	 * Method that creates a new Thread object using a Runnable object
	 * @param r: Runnable object to create the new Thread
	 */
	@Override
	public Thread newThread(Runnable r) {
		// Create the new Thread object
		Thread t=new Thread(r,name+"-Thread_"+counter);
		counter++;
		// Actualize the statistics of the factory
		stats.add(String.format("Created thread %d with name %s on %s\n",t.getId(),t.getName(),new Date()));
		return t;
	}
	
	/**
	 * Method that returns the statistics of the ThreadFactory 
	 * @return The statistics of the ThreadFactory
	 */
	public String getStats(){
		StringBuffer buffer=new StringBuffer();
		Iterator<String> it=stats.iterator();
		
		while (it.hasNext()) {
			buffer.append(it.next());
		}
		
		return buffer.toString();
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe12/src/com/packtpub/java7/concurrency/chapter1/recipe12/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe12.core;

import com.packtpub.java7.concurrency.chapter1.recipe12.factory.MyThreadFactory;
import com.packtpub.java7.concurrency.chapter1.recipe12.task.Task;

/**
 * Main class of the example. Creates a Thread factory and creates ten 
 * Thread objects using that Factory 
 *
 */
public class Main {

	/**
	 * Main method of the example. Creates a Thread factory and creates 
	 * ten Thread objects using that Factory
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the factory
		MyThreadFactory factory=new MyThreadFactory("MyThreadFactory");
		// Creates a task
		Task task=new Task();
		Thread thread;
		
		// Creates and starts ten Thread objects
		System.out.printf("Starting the Threads\n");
		for (int i=0; i<10; i++){
			thread=factory.newThread(task);
			thread.start();
		}
		// Prints the statistics of the ThreadFactory to the console
		System.out.printf("Factory stats:\n");
		System.out.printf("%s\n",factory.getStats());
		
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe04/src/com/packtpub/java7/concurrency/chapter1/recipe4/task/FileSearch.java
package com.packtpub.java7.concurrency.chapter1.recipe4.task;

import java.io.File;

/**
 * This class search for files with a name in a directory
 */
public class FileSearch implements Runnable {

	/**
	 * Initial path for the search
	 */
	private String initPath;
	/**
	 * Name of the file we are searching for
	 */
	private String fileName;

	/**
	 * Constructor of the class
	 * 
	 * @param initPath
	 *            : Initial path for the search
	 * @param fileName
	 *            : Name of the file we are searching for
	 */
	public FileSearch(String initPath, String fileName) {
		this.initPath = initPath;
		this.fileName = fileName;
	}

	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		File file = new File(initPath);
		if (file.isDirectory()) {
			try {
				directoryProcess(file);
			} catch (InterruptedException e) {
				System.out.printf("%s: The search has been interrupted",Thread.currentThread().getName());
				cleanResources();
			}
		}
	}

	/**
	 * Method for cleaning the resources. In this case, is empty
	 */
	private void cleanResources() {

	}

	/**
	 * Method that process a directory
	 * 
	 * @param file
	 *            : Directory to process
	 * @throws InterruptedException
	 *             : If the thread is interrupted
	 */
	private void directoryProcess(File file) throws InterruptedException {

		// Get the content of the directory
		File list[] = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					// If is a directory, process it
					directoryProcess(list[i]);
				} else {
					// If is a file, process it
					fileProcess(list[i]);
				}
			}
		}
		// Check the interruption
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
	}

	/**
	 * Method that process a File
	 * 
	 * @param file
	 *            : File to process
	 * @throws InterruptedException
	 *             : If the thread is interrupted
	 */
	private void fileProcess(File file) throws InterruptedException {
		// Check the name
		if (file.getName().equals(fileName)) {
			System.out.printf("%s : %s\n",Thread.currentThread().getName() ,file.getAbsolutePath());
		}

		// Check the interruption
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe04/src/com/packtpub/java7/concurrency/chapter1/recipe4/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe4.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe4.task.FileSearch;

/**
 *  Main class of the example. Search for the autoexect.bat file
 *  on the Windows root folder and its subfolders during ten seconds
 *  and then, interrupts the Thread
 */
public class Main {

	/**
	 * Main method of the core. Search for the autoexect.bat file
	 * on the Windows root folder and its subfolders during ten seconds
	 * and then, interrupts the Thread
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the Runnable object and the Thread to run it
		FileSearch searcher=new FileSearch("C:\\","autoexec.bat");
		Thread thread=new Thread(searcher);
		
		// Starts the Thread
		thread.start();
		
		// Wait for ten seconds
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Interrupts the thread
		thread.interrupt();
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe09/src/com/packtpub/java7/concurrency/chapter1/recipe7/task/SafeTask.java
package com.packtpub.java7.concurrency.chapter1.recipe7.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class that shows the usage of ThreadLocal variables to share
 * data between Thread objects
 *
 */
public class SafeTask implements Runnable {

	/**
	 * ThreadLocal shared between the Thread objects
	 */
	private static ThreadLocal<Date> startDate= new ThreadLocal<Date>() {
		protected Date initialValue(){
			return new Date();
		}
	};
	

	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		// Writes the start date
		System.out.printf("Starting Thread: %s : %s\n",Thread.currentThread().getId(),startDate.get());
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Writes the start date
		System.out.printf("Thread Finished: %s : %s\n",Thread.currentThread().getId(),startDate.get());
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe09/src/com/packtpub/java7/concurrency/chapter1/recipe7/task/UnsafeTask.java
package com.packtpub.java7.concurrency.chapter1.recipe7.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class that shows the problem generate when some Thread objects
 * share a data structure
 *
 */
public class UnsafeTask implements Runnable{

	/**
	 * Date shared by all threads
	 */
	private Date startDate;
	
	/**
	 * Main method of the class. Saves the start date and writes
	 * it to the console when it starts and when it ends
	 */
	@Override
	public void run() {
		startDate=new Date();
		System.out.printf("Starting Thread: %s : %s\n",Thread.currentThread().getId(),startDate);
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Thread Finished: %s : %s\n",Thread.currentThread().getId(),startDate);
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe09/src/com/packtpub/java7/concurrency/chapter1/recipe7/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe7.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe7.task.UnsafeTask;

/**
 * Main class of the UnsafeTask. Creates a Runnable task and
 * three Thread objects that run it.
 *
 */
public class Main {

	/**
	 * Main method of the UnsafeTaks. Creates a Runnable task and
	 * three Thread objects that run it.
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the unsafe task
		UnsafeTask task=new UnsafeTask();
		
		// Throw three Thread objects
		for (int i=0; i<3; i++){
			Thread thread=new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe09/src/com/packtpub/java7/concurrency/chapter1/recipe7/core/SafeMain.java
package com.packtpub.java7.concurrency.chapter1.recipe7.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe7.task.SafeTask;

/**
 * Main class of the example.
 *
 */
public class SafeMain {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a task
		SafeTask task=new SafeTask();
		
		// Creates and start three Thread objects for that Task
		for (int i=0; i<3; i++){
			Thread thread=new Thread(task);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread.start();
		}

	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe08/src/com/packtpub/java7/concurrency/chapter1/recipe8/handler/ExceptionHandler.java
package com.packtpub.java7.concurrency.chapter1.recipe8.handler;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Class that process the uncaught exceptions throwed in a Thread
 *
 */
public class ExceptionHandler implements UncaughtExceptionHandler {


	/**
	 * Main method of the class. It process the uncaught excpetions throwed
	 * in a Thread
	 * @param t The Thead than throws the Exception
	 * @param e The Exception throwed
	 */
	@Override	
	public void uncaughtException(Thread t, Throwable e) {
		System.out.printf("An exception has been captured\n");
		System.out.printf("Thread: %s\n",t.getId());
		System.out.printf("Exception: %s: %s\n",e.getClass().getName(),e.getMessage());
		System.out.printf("Stack Trace: \n");
		e.printStackTrace(System.out);
		System.out.printf("Thread status: %s\n",t.getState());
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe08/src/com/packtpub/java7/concurrency/chapter1/recipe8/task/Task.java
package com.packtpub.java7.concurrency.chapter1.recipe8.task;

/**
 * Runnable class than throws and Exception
 *
 */
public class Task implements Runnable {


	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		// The next instruction always throws and exception
		int numero=Integer.parseInt("TTT");
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe08/src/com/packtpub/java7/concurrency/chapter1/recipe8/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe8.core;

import com.packtpub.java7.concurrency.chapter1.recipe8.handler.ExceptionHandler;
import com.packtpub.java7.concurrency.chapter1.recipe8.task.Task;

/**
 * Main class of the example. Initialize a Thread to process the uncaught
 * exceptions and starts a Task object that always throws an exception 
 *
 */
public class Main {

	/**
	 * Main method of the example. Initialize a Thread to process the 
	 * uncaught exceptions and starts a Task object that always throws an
	 * exception 
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the Task
		Task task=new Task();
		// Creates the Thread
		Thread thread=new Thread(task);
		// Sets de uncaugh exceptio handler
		thread.setUncaughtExceptionHandler(new ExceptionHandler());
		// Starts the Thread
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Thread has finished\n");

	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe06/src/com/packtpub/java7/concurrency/chapter1/recipe6/task/DataSourcesLoader.java
package com.packtpub.java7.concurrency.chapter1.recipe6.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class that simulates an initialization operation. It sleeps during four seconds
 *
 */
public class DataSourcesLoader implements Runnable {


	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		
		// Writes a messsage
		System.out.printf("Begining data sources loading: %s\n",new Date());
		// Sleeps four seconds
		try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Writes a message
		System.out.printf("Data sources loading has finished: %s\n",new Date());
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe06/src/com/packtpub/java7/concurrency/chapter1/recipe6/task/NetworkConnectionsLoader.java
package com.packtpub.java7.concurrency.chapter1.recipe6.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class that simulates an initialization operation. It sleeps during six seconds 
 *
 */
public class NetworkConnectionsLoader implements Runnable {


	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		// Writes a message
		System.out.printf("Begining network connections loading: %s\n",new Date());
		// Sleep six seconds
		try {
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Writes a message
		System.out.printf("Network connections loading has finished: %s\n",new Date());
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe06/src/com/packtpub/java7/concurrency/chapter1/recipe6/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe6.core;

import java.util.Date;

import com.packtpub.java7.concurrency.chapter1.recipe6.task.DataSourcesLoader;
import com.packtpub.java7.concurrency.chapter1.recipe6.task.NetworkConnectionsLoader;

/**
 * Main class of the Example. Create and start two initialization tasks
 * and wait for their finish
 *
 */
public class Main {

	/**
	 * Main method of the class. Create and star two initialization tasks
	 * and wait for their finish
	 * @param args
	 */
	public static void main(String[] args) {

		// Creates and starts a DataSourceLoader runnable object
		DataSourcesLoader dsLoader = new DataSourcesLoader();
		Thread thread1 = new Thread(dsLoader,"DataSourceThread");
		thread1.start();

		// Creates and starts a NetworkConnectionsLoader runnable object
		NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
		Thread thread2 = new Thread(ncLoader,"NetworkConnectionLoader");
		thread2.start();

		// Wait for the finalization of the two threads
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Waits a message
		System.out.printf("Main: Configuration has been loaded: %s\n",new Date());
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe01/src/com/packtpub/java7/concurrency/chapter1/recipe1/task/Calculator.java
package com.packtpub.java7.concurrency.chapter1.recipe1.task;

/**
 *  This class prints the multiplication table of a number
 */
public class Calculator implements Runnable {

	/**
	 *  The number
	 */
	private int number;
	
	/**
	 *  Constructor of the class
	 * @param number : The number
	 */
	public Calculator(int number) {
		this.number=number;
	}
	
	/**
	 *  Method that do the calculations
	 */
	@Override
	public void run() {
		for (int i=1; i<=10; i++){
			System.out.printf("%s: %d * %d = %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe01/src/com/packtpub/java7/concurrency/chapter1/recipe1/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe1.core;

import com.packtpub.java7.concurrency.chapter1.recipe1.task.Calculator;

/**
 *  Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		//Launch 10 threads that make the operation with a different number
		for (int i=1; i<=10; i++){
			Calculator calculator=new Calculator(i);
			Thread thread=new Thread(calculator);
			thread.start();
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe10/src/com/packtpub/java7/concurrency/chapter1/recipe10/task/Result.java
package com.packtpub.java7.concurrency.chapter1.recipe10.task;

/**
 * Class that stores the result of the search
 *
 */
public class Result {
	
	/**
	 * Name of the Thread that finish
	 */
	private String name;
	
	/**
	 * Read the name of the Thread
	 * @return The name of the Thread
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Write the name of the Thread
	 * @param name The name of the Thread
	 */
	public void setName(String name) {
		this.name = name;
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe10/src/com/packtpub/java7/concurrency/chapter1/recipe10/task/SearchTask.java
package com.packtpub.java7.concurrency.chapter1.recipe10.task;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class that simulates a search operation
 *
 */
public class SearchTask implements Runnable {

	/**
	 * Store the name of the Thread if this Thread finish and is not interrupted
	 */
	private Result result;
	
	/**
	 * Constructor of the class
	 * @param result Parameter to initialize the object that stores the results
	 */
	public SearchTask(Result result) {
		this.result=result;
	}

	@Override
	public void run() {
		String name=Thread.currentThread().getName();
		System.out.printf("Thread %s: Start\n",name);
		try {
			doTask();
			result.setName(name);
		} catch (InterruptedException e) {
			System.out.printf("Thread %s: Interrupted\n",name);
			return;
		}
		System.out.printf("Thread %s: End\n",name);
	}
	
	/**
	 * Method that simulates the search operation
	 * @throws InterruptedException Throws this exception if the Thread is interrupted
	 */
	private void doTask() throws InterruptedException {
		Random random=new Random((new Date()).getTime());
		int value=(int)(random.nextDouble()*100);
		System.out.printf("Thread %s: %d\n",Thread.currentThread().getName(),value);
		TimeUnit.SECONDS.sleep(value);
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe10/src/com/packtpub/java7/concurrency/chapter1/recipe10/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe10.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe10.task.Result;
import com.packtpub.java7.concurrency.chapter1.recipe10.task.SearchTask;

public class Main {

	/**
	 * Main class of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a ThreadGroup
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result=new Result();

		// Create a SeachTask and 10 Thread objects with this Runnable
		SearchTask searchTask=new SearchTask(result);
		for (int i=0; i<5; i++) {
			Thread thread=new Thread(threadGroup, searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Write information about the ThreadGroup to the console
		System.out.printf("Number of Threads: %d\n",threadGroup.activeCount());
		System.out.printf("Information about the Thread Group\n");
		threadGroup.list();

		// Write information about the status of the Thread objects to the console
		Thread[] threads=new Thread[threadGroup.activeCount()];
		threadGroup.enumerate(threads);
		for (int i=0; i<threadGroup.activeCount(); i++) {
			System.out.printf("Thread %s: %s\n",threads[i].getName(),threads[i].getState());
		}

		// Wait for the finalization of the Threadds
		waitFinish(threadGroup);
		
		// Interrupt all the Thread objects assigned to the ThreadGroup
		threadGroup.interrupt();
	}

	/**
	 * Method that waits for the finalization of one of the ten Thread objects
	 * assigned to the ThreadGroup
	 * @param threadGroup
	 */
	private static void waitFinish(ThreadGroup threadGroup) {
		while (threadGroup.activeCount()>9) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe03/src/com/packtpub/java7/concurrency/chapter1/recipe3/task/PrimeGenerator.java
package com.packtpub.java7.concurrency.chapter1.recipe3.task;

/**
 *  This class generates prime numbers until is interrumped
 */
public class PrimeGenerator extends Thread{

	/**
	 *  Central method of the class
	 */
	@Override
	public void run() {
		long number=1L;
		
		// This bucle never ends... until is interrupted
		while (true) {
			if (isPrime(number)) {
				System.out.printf("Number %d is Prime\n",number);
			}
			
			// When is interrupted, write a message and ends
			if (isInterrupted()) {
				System.out.printf("The Prime Generator has been Interrupted\n");
				return;
			}
			number++;
		}
	}

	/**
	 *  Method that calculate if a number is prime or not
	 * @param number : The number
	 * @return A boolean value. True if the number is prime, false if not.
	 */
	private boolean isPrime(long number) {
		if (number <=2) {
			return true;
		}
		for (long i=2; i<number; i++){
			if ((number % i)==0) {
				return false;
			}
		}
		return true;
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe03/src/com/packtpub/java7/concurrency/chapter1/recipe3/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe3.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe3.task.PrimeGenerator;

/**
 *  Main class of the sample. Launch the PrimeGenerator, waits 
 *  five seconds and interrupts the Thread
 */
public class Main {

	/**
	 * Main method of the sample. Launch the PrimeGenerator, waits
	 * five seconds and interrupts the Thread
	 * @param args
	 */
	public static void main(String[] args) {

		// Launch the prime numbers generator
		Thread task=new PrimeGenerator();
		task.start();
		
		// Wait 5 seconds
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Interrupt the prime number generator
		task.interrupt();
	}

}

//=*=*=*=*
//./Chapter_1/ch1_recipe05/src/com/packtpub/java7/concurrency/chapter1/recipe5/task/FileClock.java
package com.packtpub.java7.concurrency.chapter1.recipe5.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class that writes the actual date to a file every second
 * 
 */
public class FileClock implements Runnable {

	/**
	 * Main method of the class
	 */
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.printf("%s\n", new Date());
			try {
				// Sleep during one second
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.out.printf("The FileClock has been interrupted");
			}
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe05/src/com/packtpub/java7/concurrency/chapter1/recipe5/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe5.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe5.task.FileClock;

/**
 * Main class of the Example. Creates a FileClock runnable object
 * and a Thread to run it. Starts the Thread, waits five seconds
 * and interrupts it. 
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a FileClock runnable object and a Thread
		// to run it
		FileClock clock=new FileClock();
		Thread thread=new Thread(clock);
		
		// Starts the Thread
		thread.start();
		try {
			// Waits five seconds
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
		// Interrupts the Thread
		thread.interrupt();
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe11/src/com/packtpub/java7/concurrency/chapter1/recipe11/task/Task.java
package com.packtpub.java7.concurrency.chapter1.recipe11.task;

import java.util.Random;

/**
 * Class that implements the concurrent task
 *
 */
public class Task implements Runnable {

	@Override
	public void run() {
		int result;
		// Create a random number generator
		Random random=new Random(Thread.currentThread().getId());
		while (true) {
			// Generate a random number a calculate 1000 divide by that random number
			result=1000/((int)(random.nextDouble()*1000));
			System.out.printf("%s : %f\n",Thread.currentThread().getId(),result);
			// Check if the Thread has been interrupted
			if (Thread.currentThread().isInterrupted()) {
				System.out.printf("%d : Interrupted\n",Thread.currentThread().getId());
				return;
			}
		}
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe11/src/com/packtpub/java7/concurrency/chapter1/recipe11/group/MyThreadGroup.java
package com.packtpub.java7.concurrency.chapter1.recipe11.group;

/**
 * Class that extends the ThreadGroup class to implement
 * a uncaught exceptions method 
 *
 */
public class MyThreadGroup extends ThreadGroup {

	/**
	 * Constructor of the class. Calls the parent class constructor
	 * @param name
	 */
	public MyThreadGroup(String name) {
		super(name);
	}


	/**
	 * Method for process the uncaught exceptions
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// Prints the name of the Thread
		System.out.printf("The thread %s has thrown an Exception\n",t.getId());
		// Print the stack trace of the exception
		e.printStackTrace(System.out);
		// Interrupt the rest of the threads of the thread group
		System.out.printf("Terminating the rest of the Threads\n");
		interrupt();
	}
}

//=*=*=*=*
//./Chapter_1/ch1_recipe11/src/com/packtpub/java7/concurrency/chapter1/recipe11/core/Main.java
package com.packtpub.java7.concurrency.chapter1.recipe11.core;

import com.packtpub.java7.concurrency.chapter1.recipe11.group.MyThreadGroup;
import com.packtpub.java7.concurrency.chapter1.recipe11.task.Task;

/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example. Creates a group of threads of
	 * MyThreadGroup class and two threads inside this group
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a MyThreadGroup object
		MyThreadGroup threadGroup=new MyThreadGroup("MyThreadGroup");
		// Create a Taks object
		Task task=new Task();
		// Create and start two Thread objects for this Task
		for (int i=0; i<2; i++){
			Thread t=new Thread(threadGroup,task);
			t.start();
		}
	}

}

//=*=*=*=*
