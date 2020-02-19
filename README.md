# j-FileWatcher
A simple program which monitors a single file for changes in a directory using WatchService.<br/>
This program will watch for changes in a directory and write the changes to a specified .txt changes log. It will also notify the user via the system tray of any changes (if the system tray is supported).

## How to use
To use simply use the method watch(String, String) passing in the directory of the changesLog and the directory to watch for changes. </br>
E.g. FileWatcher fw = new FileWatcher();<br/>
String changesLog = "C:/Users/user_name/Documents/Everyday";<br/>
String directoryToWatch = "C:/Users/user_name/Documents/Everyday/changesLog.txt";</br>
fw.watch(changesLog, directoryToWatch);
