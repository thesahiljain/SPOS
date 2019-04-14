#include <pthread.h>
#include <stdio.h>
#include <sys/types.h>

pthread_t thread1,thread2;

int add1(int a[3])
{
        a[2]=a[1]+a[0];
        printf("Result of thread 1 : %d \n",a[2]);
}

int add2(int b[3])
{
        b[2]=b[1]+b[0];
        printf("Result of thread 2 : %d \n",b[2]);
}

main()
{
        int array[4], a[3], b[3];
        pthread_t thread1, thread2;
        printf("Enter 4 numbers : ");
	
	int i;
        for(i=0;i<4;i++)
                scanf("%d", &array[i]);
      
        a[0]=array[0];
        a[1]=array[1];
        b[0]=array[2];
        b[1]=array[3];
        
        pthread_create(&thread1 ,NULL, (void*)add1, a);
        pthread_create(&thread2 ,NULL, (void*)add2, b);
	
        pthread_join(thread1,NULL);
        pthread_join(thread2,NULL);

        int answer = a[2] + b[2];
        printf("Final Result : %d\n", answer);
     
}
/* OUTPUT
student@B4L0106:~/Desktop$ gcc systhread.c -lpthread
student@B4L0106:~/Desktop$ ./a.out
Enter 4 numbers : 1 2 3 4
Result of thread 1 : 3 
Result of thread 2 : 7 
Final Result : 10
*/
