import java.util.*;

public class Scheduling {
	
	static Scanner input = new Scanner(System.in);
	static ArrayList<Process> processes;
	static ArrayList<String> nameChart;
	static ArrayList<Integer> timeChart; 
	
	public static void main(String[] args) {
		while(true) {
			System.out.println("1. FCFS");
			System.out.println("2. SJF");
			System.out.println("3. Priority Scheduling");
			System.out.println("4. Round Robin");
			System.out.print("Enter choice : ");
			int choice = input.nextInt();
			if(choice < 1 || choice > 4)
				break;
			input();
			Collections.sort(processes);
			switch(choice) {
			case 1:
				fcfs();
				break;
			case 2:
				sjf();
				break;
			case 3:
				priorityScheduling();
				break;
			case 4:
				roundRobin();
				break;
			}
			display();
		}
	}
	
	static void input() {
		processes = new ArrayList<>();
		System.out.print("Enter number of processes : ");
		int total = input.nextInt();
		System.out.println("Enter name, arrival time and burst time for each process");
		for(int t = 0; t < total; t++)
			processes.add(new Process(input.next(), input.nextInt(), input.nextInt()));
		System.out.println();
		
		nameChart = new ArrayList<>();
		timeChart = new ArrayList<>();
	}
	
	static void display() {
		ganttChart();
		System.out.println("Name\tAT\tBT\tTAT\tWT\tFT");
		for(Process process : processes)
			System.out.println(process.name + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.turnAroundTime + "\t" + process.waitTime + "\t" + process.finishTime + "\t" + process.dummyBurstTime);
		System.out.println();
		
		float averageWait = 0;
		float averageTurnAround = 0;
		for(Process process : processes) {
			averageWait += process.waitTime;
			averageTurnAround += process.turnAroundTime;
		}
		averageWait /= processes.size();
		averageTurnAround /= processes.size();
		System.out.println(String.format("Average Wait Time : %.2f", averageWait));
		System.out.println(String.format("Average Turn-Around Time : %.2f", averageTurnAround));
		System.out.println();
	}
	
	static void ganttChart() {
		System.out.println("Gantt Chart");
		for(String name : nameChart)
			System.out.print("\t" + name + "\t");
		System.out.println();
		System.out.print("0");
		for(int time : timeChart)
			System.out.print("\t\t" + time);
		System.out.println();
	}
	
	// FCFS with Gantt Chart
	static void fcfs() {
		int currentTime = 0;
		for(Process process : processes) {
			if(currentTime < process.arrivalTime)
				currentTime = process.arrivalTime;
			currentTime += process.burstTime;
			process.turnAroundTime = currentTime-process.arrivalTime;
			process.waitTime = process.turnAroundTime-process.burstTime;
			process.finishTime = currentTime;
			nameChart.add(process.name);
			timeChart.add(currentTime);
		}
		System.out.println();
	}
	// Shortest Job First, along with gantt chart
	static void sjf() {
		int currentTime = 0;
		System.out.println("Gantt Chart");
		System.out.print("0");
		while(true) {
			// Check if all jobs are scheduled or not
			boolean jobDone = true;
			for(Process process : processes)
				if(process.dummyBurstTime > 0) {
					jobDone = false;
					break;
				}
			if(jobDone) break;
			// Make a list of all arrived processes
			ArrayList<Process> arrivedProcesses = new ArrayList<>();
			for(Process process : processes)
				if(process.arrivalTime <= currentTime && process.dummyBurstTime > 0)
					arrivedProcesses.add(process);
			if(arrivedProcesses.isEmpty()) {
				currentTime++;
				continue;
			}
			// Find job with minimum dummy burst
			Process current = arrivedProcesses.get(0);
			for(Process process : arrivedProcesses)
				if(process.dummyBurstTime < current.dummyBurstTime)
					current = process;
			// Apply scheduling
			current.dummyBurstTime--;
			currentTime++;
			nameChart.add(current.name);
			timeChart.add(currentTime);
			// If the process is completed, do the math
			if(current.dummyBurstTime == 0) {
				current.finishTime = currentTime;
				current.turnAroundTime = currentTime-current.arrivalTime;
				current.waitTime = current.turnAroundTime-current.burstTime;
			}
		}
		System.out.println();
	}
	
	static void priorityScheduling() {
		int schedulingLeft = processes.size();
		for(Process process : processes) {
			System.out.print("Enter priority for process " + process.name + " : ");
			process.priority = input.nextInt();
		}
		boolean isAdded[] = new boolean[schedulingLeft];
		int currentTime = 0;
		PriorityQueue<Process> waiting = new PriorityQueue<>(schedulingLeft, new Comparator<Process>() {
			public int compare(Process p1, Process p2) {
				return p2.priority-p1.priority;
			}
			
		});
		while(schedulingLeft > 0) {
			// Add the arrived processes to priority queue not yet added
			for(int i = 0; i < processes.size(); i++) {
				if(!isAdded[i] && processes.get(i).arrivalTime <= currentTime) {
					isAdded[i] = true;
					waiting.add(processes.get(i));
				}
			}
			
			if(!waiting.isEmpty()) {
				Process current = waiting.poll();	
				currentTime = currentTime + current.burstTime;
				current.finishTime = currentTime;
				current.turnAroundTime = currentTime-current.arrivalTime;
				current.waitTime = current.turnAroundTime-current.burstTime;
				schedulingLeft--;
				
				nameChart.add(current.name);
				timeChart.add(currentTime);
			}else
				currentTime++;
		}
	}
	
	static void roundRobin() {
		System.out.print("Enter quantum time : ");
		int quantum = input.nextInt();
		Queue<Process> queue = new LinkedList<>();
		int schedulingLeft = processes.size();
		int currentTime = 0;
		Process toBeAdded = null;
		
		while(schedulingLeft > 0) {
			// Add newly arrived processes to queue
			for(Process process : processes)
				if(process.arrivalTime <= currentTime && process.dummyBurstTime > 0 && !queue.contains(process) && !process.equals(toBeAdded))
					queue.add(process);
			if(toBeAdded != null) {
				queue.add(toBeAdded);
				toBeAdded = null;
			}
			if(queue.isEmpty()) continue;
			
			Process process = queue.poll();
			if(process.dummyBurstTime > quantum) {
				process.dummyBurstTime -= quantum;
				currentTime = currentTime += quantum;
				nameChart.add(process.name);
				timeChart.add(currentTime);
				//queue.add(process);
				toBeAdded = process;
			}else {
				if(process.dummyBurstTime == quantum)
					currentTime = currentTime+quantum;
				else
					currentTime = currentTime+(quantum-process.dummyBurstTime);
				process.dummyBurstTime = 0;
				process.turnAroundTime = currentTime-process.arrivalTime;
				process.waitTime = process.turnAroundTime-process.burstTime;
				process.finishTime = currentTime;
				nameChart.add(process.name);
				timeChart.add(currentTime);
				schedulingLeft--;
			}
		}
	}
}

class Process implements Comparable<Process>{
	
	public String name = "";
	public int arrivalTime = 0;
	public int burstTime = 0, dummyBurstTime = 0;
	public int turnAroundTime = 0;
	public int waitTime = 0;
	public int finishTime = 0;
	public int priority = 0;
	
	public Process(String name, int arrivalTime, int burstTime) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.dummyBurstTime = burstTime;
	}
	
	public int compareTo(Process process) {
		return this.arrivalTime-process.arrivalTime;
	}
	
}
