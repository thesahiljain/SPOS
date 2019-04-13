import java.util.*;

public class Bankers {
	
	// Declare all necessary variables
	static Scanner input = new Scanner(System.in);
	static int need[][];
	static int available[];
	static int max[][];
	static int allocation[][];
	static int request[];
	static int resources, processes, processRequest;
	
	//Method to accept the user input
	static void accept() {
		System.out.print("Enter number of resources : ");
		resources = input.nextInt();
		System.out.print("Enter number of processes : ");
		processes = input.nextInt();
		
		need = new int[processes][resources];
		available = new int[resources];
		max = new int[processes][resources];
		allocation = new int[processes][resources];
		request = new int[resources];
		
		System.out.println("Enter allocation matrix");
		for(int i = 0; i < processes; i++)
			for(int j = 0; j < resources; j++)
				allocation[i][j] = input.nextInt();
		System.out.println("Enter max matrix");
		for(int i = 0; i < processes; i++)
			for(int j = 0; j < resources; j++)
				max[i][j] = input.nextInt();
		System.out.println("Enter available matrix");
		for(int j = 0; j < resources; j++)
			available[j] = input.nextInt();
	}
	
	// Method to calculate and display need matrix
	static void calculateNeed() {
		System.out.println("Need Matrix");
		for(int i = 0; i < processes; i++) {
			for(int j = 0; j < resources; j++) {
				need[i][j] =  max[i][j] - allocation[i][j];
				System.out.print(need[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	// Check resource availability
	static boolean isResourceAvailable(int process) {
		for(int i = 0; i < resources; i++)
			if(available[i] < need[process][i])
				return false;
		return true;
	}
	
	// Applying safety algorithm
	static void safety() {
		boolean isExecuted[] = new boolean[processes];
		for(int j = 0; j < processes;) {
			boolean allocated = false;
			// Try to allocate a process, if not then deadlock
			for(int i = 0; i < processes; i++) {
				if(!isExecuted[i] && isResourceAvailable(i)) {
					for(int resource = 0; resource < resources; resource++)
						available[resource] = available[resource] - need[i][resource] + max[i][resource];
					System.out.println("Allocated process : " + i);
					allocated = true;
					isExecuted[i] = true;
					j++;
				}
			}
			if(!allocated) {
				System.out.println("All processes CANNOT allocated safely!");
				return;
			}
		}
		System.out.println("All processes CAN be allocated safely!");
	}
	
	// Check is request is less than needed
	static boolean requestNeed() {
		for(int i = 0; i < resources; i++)
			if(request[i] > need[processRequest][i]) {
				System.out.println("Request is greater than need!");
				return false;
			}
		return true;
	}
	
	// Check is resources are available
	static boolean requestAvailable() {
		for(int i = 0; i < resources; i++)
			if(request[i] > available[i]) {
				System.out.println("Request is greater than available!");
				return false;
			}
		return true;
	}
	
	// Additional resource request
	static void resourceRequest() {
		
		System.out.print("Enter the process which wants to request more resources : ");
		processRequest = input.nextInt();
		System.out.println("Enter request resource amount");
		for(int i = 0; i < resources; i++) request[i] = input.nextInt();
		
		if(requestNeed() && requestAvailable()) {
			for(int i = 0; i < resources; i++) {
				available[i] = available[i]-request[i];
				need[processRequest][i] = need[processRequest][i]-request[i];
				allocation[processRequest][i] = allocation[processRequest][i]+request[i];
			}
			System.out.println("Request fulfilled!");
		}else
			System.out.println("Unable to fulfill Request.");
	}
	
	public static void main(String[] args) {
		accept();
		calculateNeed();
		resourceRequest();
		safety();
	}
}
