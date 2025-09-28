#!/bin/bash

echo "🚀 Building Oriola Denim for Railway..."

# Clean and package
mvn clean package -DskipTests

# Check if JAR was created
if [ -f "target/oriola-denim-0.0.1-SNAPSHOT.jar" ]; then
    echo "✅ JAR created successfully!"
    echo "📦 JAR size: $(du -h target/oriola-denim-0.0.1-SNAPSHOT.jar | cut -f1)"
else
    echo "❌ JAR creation failed!"
    exit 1
fi

echo "🎉 Build completed for Railway deployment!"
