#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

sem_t a_arrived;
sem_t b_arrived;

void * a(void *arg) {
    printf("This is thread a.\n");
    sleep(3);
    printf("a has arrived at the rendezvous point.\n");



    printf("a is proceeeding forward.\n");
    return NULL;
}


void * b(void *arg) {
    printf("This is thread b.\n");
    sleep(6);
    printf("b has arrived at the rendezvous point.\n");



    printf("b is proceeeding forward.\n");
    return NULL;
}
                                                                             
int main(int argc, char *argv[]) {                    
    // Initialize semaphores


    // Declare and start threads
   	pthread_t a_thread;
    pthread_t b_thread;
    pthread_create(&a_thread, NULL, a, NULL);
    pthread_create(&b_thread, NULL, b, NULL);

    // Wait for child to finish
    pthread_join(a_thread, NULL); 
    pthread_join(b_thread, NULL); 

    printf("Parent is done waiting and will now exit.\n");
    return 0;
}