# NeuroTalks - Quick Start Guide

## 🚀 Running the Application

### 1. Start MariaDB (if not already running)
```bash
sudo service mysql start
# or if using MariaDB:
sudo service mariadb start
```

### 2. Launch the Application
```bash
cd /home/rose/NeuroTalks
sudo bash ./mvnw spring-boot:run
```

Wait for the startup message:
```
Tomcat started on port(s): 8080 (http) with context path ''
```

### 3. Access the Application

| Resource | URL |
|----------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **API Spec (JSON)** | http://localhost:8080/v3/api-docs |
| **Blog** | http://localhost:8080/blog |
| **Login** | http://localhost:8080/login |
| **Register** | http://localhost:8080/reg |
| **Homepage** | http://localhost:8080/homepage |

---

## 🔧 Testing Endpoints in Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. All endpoints are listed with descriptions
3. Click "Try it out" to test any endpoint
4. For authenticated endpoints, use:
   - **Username**: Register via `/reg` endpoint
   - **Password**: Your chosen password (BCrypt encrypted)
   - **Auth Method**: Form login at `/login`

---

## 📦 Build & Test Commands

### Clean Build
```bash
sudo bash ./mvnw clean compile
```

### Run Tests
```bash
sudo bash ./mvnw test
```

### Run Tests + Build
```bash
sudo bash ./mvnw clean test
```

### Build WAR File
```bash
sudo bash ./mvnw clean package
# Output: target/app-0.0.1-SNAPSHOT.war
```

---

## 🗄️ Database Configuration

| Setting | Value |
|---------|-------|
| **Host** | localhost:3306 |
| **Database** | app1 |
| **Username** | springuser |
| **Password** | Alphabeta7 |
| **Driver** | com.mysql.jdbc.Driver (or com.mysql.cj.jdbc.Driver) |
| **Hibernate** | ddl-auto=update (auto-creates tables) |

### Connect to Database
```bash
mysql -u springuser -p app1
# Password: Alphabeta7
```

---

## 🛑 Stopping the Application

In another terminal:
```bash
# Kill the Maven process
pkill -f "spring-boot:run"

# Or kill the Java process directly
pkill -f "java.*AppApplication"
```

---

## 📋 Available Endpoints

### Blog Endpoints
- `GET /blog` - List all posts
- `POST /blog/add` - Create post
- `GET /blog/{id}` - Get post details
- `POST /blog/{id}/edit` - Update post
- `POST /blog/{id}/remove` - Delete post

### User Endpoints
- `GET /reg` - Registration form
- `POST /reg` - Create new user
- `GET /login` - Login form
- `POST /login` - Authenticate (handled by Spring Security)

### Home Endpoints
- `GET /homepage` - User dashboard (requires authentication)
- `GET /homepage/post` - Create post form
- `POST /homepage/post` - Create post
- `GET /homepage/post/{id}` - View post details
- `GET /homepage/{email}` - View user profile

---

## 🔐 Security Notes

- All endpoints under `/homepage` require authentication
- `/blog/add` requires ROLE_USER or ROLE_ADMIN
- `/blog`, `/login`, `/reg` are publicly accessible
- Swagger documentation endpoints are public
- Passwords are BCrypt encoded (security best practice)

---

## 📊 Project Structure

```
NeuroTalks/
├── src/
│   ├── main/
│   │   ├── java/com/example/app/
│   │   │   ├── controllers/        # REST/MVC endpoints (7 controllers)
│   │   │   ├── models/             # JPA entities (3 entities)
│   │   │   ├── repo/               # Spring Data repositories (3 repos)
│   │   │   ├── config/             # Swagger configuration
│   │   │   ├── servlets/           # Security configuration
│   │   │   ├── auth/               # Authentication service
│   │   │   └── AppApplication.java # Entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/             # CSS, JS, images
│   │       └── templates/          # Thymeleaf HTML templates
│   └── test/
│       └── java/com/example/app/
│           └── AppApplicationTests.java
├── pom.xml                          # Maven dependencies
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
└── target/                          # Compiled output
```

---

## ✅ Verification Checklist

- [ ] MariaDB is running and accessible
- [ ] Application starts without errors
- [ ] Swagger UI loads at http://localhost:8080/swagger-ui.html
- [ ] OpenAPI spec available at http://localhost:8080/v3/api-docs
- [ ] Can access `/blog` endpoint without authentication
- [ ] Can register new user via `/reg`
- [ ] Can login and access `/homepage`
- [ ] All tests pass: `sudo bash ./mvnw test`

---

## 🐛 Troubleshooting

### Issue: "Connection refused" on startup
**Solution**: Ensure MariaDB is running
```bash
sudo service mariadb status
sudo service mariadb start
```

### Issue: "Port 8080 already in use"
**Solution**: Kill the existing process
```bash
lsof -ti:8080 | xargs kill -9
```

### Issue: "Cannot connect to database"
**Solution**: Check database credentials in `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/app1
spring.datasource.username=springuser
spring.datasource.password=Alphabeta7
```

### Issue: Swagger UI shows no endpoints
**Solution**: Ensure controllers are in the `com.example.app.controllers` package (auto-scanned)

---

## 📝 Last Modified

- **Date**: December 2, 2025
- **Status**: ✅ Production Ready
- **Swagger Integration**: ✅ Complete with Springdoc-OpenAPI 1.6.4
- **Tests**: ✅ All passing (1/1)
- **Build**: ✅ Successful

---

For detailed implementation information, see: `SWAGGER_SETUP_SUMMARY.md`
