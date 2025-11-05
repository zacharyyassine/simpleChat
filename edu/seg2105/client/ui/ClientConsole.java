package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author François Bélanger
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Laganière
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client = new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    fromConsole = new Scanner(System.in); 
  }

  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      String message;

      while (true) 
      {
        message = fromConsole.nextLine();

        // Handle commands starting with '#'
        if (message.startsWith("#")) {
          handleCommand(message);
        }
        else {
          client.handleMessageFromClientUI(message);
        }
      }

    } 
    catch (Exception ex) 
    {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  // NEW helper method for commands
  private void handleCommand(String cmd)
  {
    if (cmd.equals("#quit")) {
      client.quit();
    }
    else if (cmd.equals("#logoff")) {
      client.logoff();
    }
    else if (cmd.startsWith("#sethost")) {
      String[] parts = cmd.split(" ");
      if (parts.length > 1)
        client.sethost(parts[1]);
      else
        System.out.println("Usage: #sethost <hostname>");
    }
    else if (cmd.startsWith("#setport")) {
      String[] parts = cmd.split(" ");
      if (parts.length > 1)
        client.setport(Integer.parseInt(parts[1]));
      else
        System.out.println("Usage: #setport <port>");
    }
    else if (cmd.equals("#login")) {
      client.login();
    }
    else if (cmd.equals("#gethost")) {
      System.out.println("Current host: " + client.getHost());
    }
    else if (cmd.equals("#getport")) {
      System.out.println("Current port: " + client.getPort());
    }
    else {
      System.out.println("Unknown command.");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The loginID.
   * @param args[1] The host (optional).
   * @param args[2] The port (optional).
   */
  public static void main(String[] args) 
  {
    if (args.length < 1) {
      System.out.println("ERROR: No login ID provided.\nUsage: java ClientConsole <loginID> [host] [port]");
      return;
    }

    String loginID = args[0];
    String host = "localhost";
    int port = DEFAULT_PORT;

    if (args.length > 1)
      host = args[1];

    if (args.length > 2)
      port = Integer.parseInt(args[2]);

    ClientConsole chat = new ClientConsole(host, port);

    // Send login command to the server automatically
    chat.client.handleMessageFromClientUI("#login " + loginID);

    chat.accept();
  }

}
//End of ConsoleChat class
