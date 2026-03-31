#include <iostream>
#include <cstdlib>
#include <ctime>

using namespace std;

int main() {
    srand(time(0));  // Seed for random number generation
    
    int secretNumber;
    int guess;
    int attempts;
    char playAgain;
    
    cout << "=====================================" << endl;
    cout << "  Welcome to the Number Guessing Game!" << endl;
    cout << "=====================================" << endl;
    
    do {
        // Generate a random number between 1 and 100
        secretNumber = (rand() % 100) + 1;
        attempts = 0;
        
        cout << "\nI'm thinking of a number between 1 and 100." << endl;
        cout << "Can you guess what it is?" << endl;
        
        // Game loop
        while (true) {
            cout << "\nEnter your guess: ";
            cin >> guess;
            
            // Input validation
            if (cin.fail()) {
                cin.clear();
                cin.ignore(10000, '\n');
                cout << "Invalid input! Please enter a valid number." << endl;
                continue;
            }
            
            if (guess < 1 || guess > 100) {
                cout << "Please enter a number between 1 and 100!" << endl;
                continue;
            }
            
            attempts++;
            
            if (guess == secretNumber) {
                cout << "\n🎉 Congratulations! You guessed the number!" << endl;
                cout << "The secret number was: " << secretNumber << endl;
                cout << "You took " << attempts << " attempt(s)." << endl;
                break;
            } else if (guess < secretNumber) {
                cout << "Too low! Try a higher number." << endl;
            } else {
                cout << "Too high! Try a lower number." << endl;
            }
        }
        
        // Ask if player wants to play again
        cout << "\nDo you want to play again? (y/n): ";
        cin >> playAgain;
        
    } while (playAgain == 'y' || playAgain == 'Y');
    
    cout << "\nThanks for playing! Goodbye!" << endl;
    
    return 0;
}
