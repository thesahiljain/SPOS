#include <stdio.h>
#include <sys/types.h>

int main()
{
	pid_t num_pid = fork();
	
	if(num_pid == 0)
	        printf("This is the child process : %d\n", getpid());
	if(num_pid > 0)
	        printf("This is the parent process : %d\n", getpid());
}
/*
student@B4L0106:~/Desktop$ ./a.out
This is the parent process : 3632
This is the child process : 3633
*/
