#include <stdio.h>
#include <sys/types.h>

int main(int args, char *argv[])
{
        char *ps_args[]={"ps"};
        pid_t pid = fork();
        
        if(pid==0){
	        printf("This is a child process :  %d\n", getpid());
	        execvp(ps_args[0], ps_args, getpid());
	}
	else if(pid>0){
	        wait();
	        printf("This is a parent process : %d\n", getpid());
	}else
		printf("FORK Failed!\n");
}
/*
student@B4L0106:~/Desktop$ ./a.out
This is a child process :  3366
  PID TTY          TIME CMD
 3260 pts/0    00:00:00 bash
 3365 pts/0    00:00:00 a.out
 3366 pts/0    00:00:00 ps
This is a parent process : 3365
*/
