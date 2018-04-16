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
    
    // a should not pass this point until b reaches the corresponding in its
    // own code
    sem_post(&a_arrived);
     sem_wait(&b_arrived);


    printf("a is proceeeding forward.\n");
    return NULL;
}


void * b(void *arg) {
    printf("This is thread b.\n");
    sleep(6);
    printf("b has arrived at the rendezvous point.\n");

    // b should not pass this point until a reaches the corresponding point
    // in its code
    sem_post(&b_arrived);
    sem_wait(&a_arrived);

    printf("b is proceeeding forward.\n");
    return NULL;
}
                                                                             
int main(int argc, char *argv[]) {                    
    // Initialize semaphores
    sem_init(&a_arrived, 0, 0);
    sem_init(&b_arrived, 0, 0);

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