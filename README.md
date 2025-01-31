# **Taptiles**

🧩 **Taptiles** is a 2D tile-matching puzzle game where players match identical tiles to remove them from the board and achieve the highest score.

## 📌 **Features**
- ✅ User registration and login
- 🏆 High score saving
- ⭐ Game rating system
- 💬 Commenting on the game
- 🔄 Responsive design for both PC and mobile devices

## 🎮 **How to Play**
1. Click on two identical free tiles to remove them.
2. Tiles can only be removed if there is a clear line between them.
3. Clear the entire board to complete the level.
4. Earn the highest score based on time.
5. Log in to save your scores, ratings, and comments.

## 📷 **Screenshots**

### **Game**
![Alt text](/design/game.png?raw=true "Game")

### **Login screen**
![Alt text](/design/login.png?raw=true "Login screen")

### **Top scores and comments**
![Alt text](/design/topscores_comments.png?raw=true "Top scores and comments")

### **Rating and add comment**
![Alt text](/design/rating_addcomment.png?raw=true "Rating and add comments")

## 🚀 **Installation and Setup**


1. Clone the repository. 
2. Configure the database:
   - Create a PostgreSQL database
   - Edit `application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/taptiles_db
     spring.datasource.username=your_user
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```  
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```  
4. Open a web browser and navigate to `http://localhost:8080` to play the game.

## 🛠 **Technologies Used**
- **Backend:** Java, Spring Boot, JPA, JDBC, PostgreSQL
- **Frontend:** HTML, JavaScript, CSS, Thymeleaf
- **Build:** Maven
- **API:** REST API
