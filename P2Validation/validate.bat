@echo off
setlocal enableextensions

:: Script to validate assignment submission for Project 2 for SWEN30006 SEM 2 2021

:: Unzip submission
set zipfile=Pasur.zip
echo|set /p=Looking for %zipfile%...
if not exist %zipfile% (
    echo ERROR: Specified zip file can not be found.
	pause
	exit /b
)
echo found!

set output=output.txt
set results=test_results.txt
set logfile=pasur.log

if exist %output% del /q %output%

:: Delete twice below because rmdir is flakey if dir has files in it
if exist tmp_unzipped rmdir /s /q tmp_unzipped
if exist build rmdir /s /q build
if exist dist rmdir /s /q dist
if exist %output% del %output%
if exist %results% del %results%
if exist %logfile% del %logfile%

:: Unzip submission
set zip_path="C:\Program Files\7-Zip\7z"

set ant=apache-ant-1.10.8\bin\ant

echo|set /p=Unzipping archive...
%zip_path% x "%zipfile%" -otmp_unzipped -y > nul
if not exist tmp_unzipped (
	echo ERROR: Failed to unzip %zipfile%
	pause
	exit /b
)
echo done!

:: Validate supporting files
echo|set /p=Looking for DesignAnalysis.pdf...
if not exist tmp_unzipped\Pasur\DesignAnalysis.pdf (
	echo.
	echo ERROR: DesignAnalysis.pdf not found. Please check the submission guide and make sure you are following the requirements.
	pause
	exit /b
)
echo found!

:: Copy the modem over
echo|set /p=Copying JGameGrid...
copy /Y JGameGrid.jar tmp_unzipped\Pasur\JGameGrid.jar 

echo.
echo|set /p=Building project...
:: Change this, if necessary, to point to your jdk
:: The variable Path in your system environment variables should include:
::     %JAVA_HOME%\bin
set "JAVA_HOME=C:\Program Files\Java\jdk-11"
call %ant% >> "%output%" && (echo success!) || (echo Build failed. Checkout %output% for the compiler log. & pause & exit /b)


echo.
echo|set /p=Running project...
call java -jar tmp_unzipped\dist\lib\Project1.jar >> %results% && (echo success!) || (echo failed...check %output% and %results% for details.)

echo|set /p=Checking results...
fc expected.txt %logfile% > nul
if errorlevel 1 (
    echo FAILED
	echo results failed >> %output%
) else (
    echo success!
	echo results successful! >> %output%
)




:: Cleaning up
echo.
echo|set /p=Cleaning up...
if exist tmp_unzipped rmdir /s /q tmp_unzipped
if exist tmp_unzipped rmdir /s /q tmp_unzipped
echo done.
:: Cleaning done

echo.
pause

exit /b
