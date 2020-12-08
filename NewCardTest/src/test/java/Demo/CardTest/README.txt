README file:

(1) Install Eclipse IDE on the local computer.  Instructions can be found at the following URL:
https://www.eclipse.org/downloads/

(2) Install Maven on Eclipse on the local computer.  Instructions can be found at the following URL:
https://www.toolsqa.com/java/maven/how-to-install-maven-eclipse-ide/

(3) Within Eclipse, click on "File", and select "Import". Select "Git", and select 
"Projects from Git (with smart import)".  Double-click to open "Clone URI".

(4) On the next screen ("Source Git Repository"), specify the following in the URI field:
https://github.com/ccmcfadden/CardTest.git.  Specify "github.com" in the "Host" field
and "/ccmcfadden/CardTest.git" in the "Repository Path" field.  Click "Next".

(5) In "Branch Selection", select "master" branch. Click "Next".  
Click "Next" on the next screen ("Local Destination").

(6) On the next screen ("Import Projects from File System or Archive"), select 
"CardTest/NewCardTest", and deselect "CardTest".  Click "Finish".

(7) Build the project by clicking on Project, and selecting "Build Project"

(8) Run the project by right-clicking on "CardTestSuite.java" (under "Demo.CardTest" package, 
which is under "src/test/java" folder) and selecting "Run As".  Select "JUnit Test".  

(6) Observe the output on the Console screen.