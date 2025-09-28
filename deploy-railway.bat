@echo off
echo 🚀 Deploying Oriola Denim to Railway...

REM Build the project
echo 📦 Building JAR...
call mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo ❌ Build failed!
    pause
    exit /b 1
)

echo ✅ Build successful!

REM Check if Railway CLI is installed
railway --version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Railway CLI not found. Please install it first.
    echo 📥 Download from: https://docs.railway.app/develop/cli
    pause
    exit /b 1
)

echo 🚀 Deploying to Railway...
railway up

echo 🎉 Deployment completed!
pause
