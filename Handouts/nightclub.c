// "Nightclub" -- multiple threads may be in the critical region at once, but
// only up to a limit.
//
// Inspired by Allen Downey's Little Book of Semaphores

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

sem_t bouncer;

int n_threads = 20;

void * thread(void *arg) {
    int id = *(int *) arg;
    free(arg);


    printf("%d is in the club. Partying for a random amount of time.\n", id);
    sleep(rand() % 20);
    printf("\t%d is tired of partying and leaving the club.\n", id);



    return NULL;
}
                                                                             
int main(int argc, char *argv[]) {                    
    // Initialize semaphore
    

    // Declare and start threads
   	pthread_t threads[n_threads];
   	int i;
   	for (i = 0; i < n_threads; i++) {
   	    int *arg = malloc(sizeof(int));
   	    *arg = i;
   	    pthread_create(&threads[i], NULL, thread, arg);
   	}

    // Wait for child to finish
    for (i = 0; i < n_threads; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Parent is done waiting and will now exit.\n");
    return 0;
}