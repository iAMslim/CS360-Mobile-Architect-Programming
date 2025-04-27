# CS360-Mobile-Architect-Programming

1. Summary of App Requirements and Goals
The Weight-Tracking App helps users track their weight over time while receiving motivational alerts when goal milestones are reached. The app addresses user needs for simple data entry, visual feedback, and real-time goal notifications through SMS.

2. Screens and Features Supporting User Needs
Key screens and features include:

Login Screen: User registration and login system.

Weight Tracking Screen: Allows users to add, edit, and delete weight entries with a goal weight reminder via SMS.

Dynamic Weight List: Uses RecyclerView for displaying logged weights.

The UI focused on ease of use with clear dialogs for data entry and simple navigation elements like menus and floating action buttons.

3. Coding Approach and Strategies
The app was developed using Java and SQLite. I used a structured approach with SQLiteOpenHelper for data management, and RecyclerView for dynamic UI. While not fully MVVM, I applied basic separation of UI and data logic. These strategies help keep code manageable and can be expanded for future apps.

4. Testing Process
I tested functionality manually on the Android Emulator, ensuring users could register, login, and log weights correctly. I also validated that SMS notifications worked when goal weights were achieved. This process was critical in confirming real-time features like permission handling and database integrity.

5. Innovation and Challenges
One of the biggest challenges was integrating SMS alerts for goal weight achievement. This required handling permissions dynamically and using Android’s SmsManager API. I also had to ensure data integrity between user authentication and weight tracking.

6. Highlighted Success
A standout feature was the goal weight SMS notification, which combined user data, real-time alerts, and system-level permissions. This demonstrated my ability to blend backend logic with user interaction effectively.
