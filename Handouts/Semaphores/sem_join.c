#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

sem_t done;

void * child(void *arg) {
    printf("Child is sleeping...\n");
    sleep(10);
    printf("Child is now finished.\n");



    return NULL;
}

                                                                             
int main(int argc, char *argv[]) {                    


    // Declare and start threads
   	pthread_t t;
    pthread_create(&t, NULL, child, NULL);
    
    // Wait for child to finish
    printf("Parent is waiting for child to finish.\n");



    printf("Parent is done waiting and will now exit.\n");
    return 0;
}
