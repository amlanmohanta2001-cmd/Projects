#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
    int secret_nr, guess, attempts = 0;
    srand(time(0)); 
    secret_nr = rand() % 100 + 1; 

    printf("--- Welcome to the Linux Terminal Challenge ---\n");
    printf("I'm thinking of a number between 1 and 100.\n");

    do {
        printf("Enter your guess: ");
        if (scanf("%d", &guess) != 1) {
            printf("Invalid input! Please enter a number.\n");
            while(getchar() != '\n'); // Clear buffer
            continue;
        }
        attempts++;

        if (guess > secret_nr) {
            printf("Too high! Try again.\n");
        } else if (guess < secret_nr) {
            printf("Too low! Try again.\n");
        } else {
            printf("\nCorrect! You found it in %d attempts.\n", attempts);
        }
    } while (guess != secret_nr);

    return 0;
}