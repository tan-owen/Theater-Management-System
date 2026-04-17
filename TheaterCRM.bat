@echo off
:: Step 1: Compile files from 'src' into the 'bin' folder
javac -d bin Main.java

:: Step 2: Run the compiled class from the 'bin' folder
java -cp bin Main

:: Step 3: Keep the window open to see output
pause
