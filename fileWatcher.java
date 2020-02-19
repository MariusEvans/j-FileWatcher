package sendemail;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * This class watches a directory for changes and writes those changes to a log with a date. The user will be alerted of changes via the system tray.
 * @author Marius Evans
 * @version 2
 */
public class fileWatcher {
    
    /**
     * This method will watch the directory for changes, add the changes to the log with a date and notify the user of changes via the system tray.
     * @param dirChangesLog the directory for the .txt file of changes. Example dir: "C:/Users/user_name/Documents/Everyday";
     * @param dirToWatch the directory to watch. Example dir: "C:/Users/user_name/Documents/Everyday/changesLog.txt";
     */
    public void watch(String dirChangesLog, String dirToWatch){
        System.out.println("Watching...");
        File dir = new File(dirToWatch);
        final Path myDir = dir.toPath();
        
        File changesLog = new File(dirChangesLog);
        final Path changesLogPath = changesLog.toPath();
        
        try {
           WatchService watcher = myDir.getFileSystem().newWatchService();
           myDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

           WatchKey watckKey = watcher.take();
           List<WatchEvent<?>> events = watckKey.pollEvents();
           for (WatchEvent event : events) {
                
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modified: " + event.context().toString());
                    String changed = event.context().toString();
                    
                    //create log of changes
                    try
                    {
                        String timeStamp = new SimpleDateFormat("dd.MM.yyyy h:mm").format(Calendar.getInstance().getTime());
                        System.out.println(timeStamp);
                        List<String> lines = Files.readAllLines(changesLogPath, StandardCharsets.UTF_8);
                        lines.add(changed+" modified at "+timeStamp);
                        Files.write(changesLogPath, lines);
                        
                        //send windows notification of changes
                        if (SystemTray.isSupported()) 
                        {
                          SystemTray tray = SystemTray.getSystemTray();

                          //If the icon is a file
                          Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                          TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                          trayIcon.setImageAutoSize(true);
                          trayIcon.setToolTip("File has been modified");
                          tray.add(trayIcon);

                          trayIcon.displayMessage("File Watcher", changed+" has been modified.", MessageType.INFO);
                          //wait 10 seconds to aviod duplicate entries
                          try
                          {
                            System.out.println("Waiting 10s...");
                            Thread.sleep(10000);
                          }
                          catch(InterruptedException ex)
                          {
                            Thread.currentThread().interrupt();
                          } 
                        } 
                        else 
                        {
                          System.err.println("System tray not supported!");
                          JOptionPane.showMessageDialog(null, "System Tray not supported");
                        }
                    }
                    catch(Exception exc)
                    {
                        System.out.println(exc);
                        exc.printStackTrace();
                    }
                }
            }
           
        }
        catch (Exception e) 
        {
            System.out.println("Error: " + e.toString());
        }
    }
}
