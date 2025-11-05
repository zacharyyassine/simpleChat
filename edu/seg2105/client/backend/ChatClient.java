package edu.seg2105.client.backend;

import ocsf.client.*;
import java.io.*;
import edu.seg2105.client.common.*;

/**
 * Chat Client class with added console command support.
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable. It allows display to UI.
   */
  private ChatIF clientUI;

  /**
   * Login ID for this client user.
   */
  private String loginID;

  //Constructors ****************************************************

  public ChatClient(String host, int port, ChatIF clientUI) throws IOException 
  {
    super(host, port);
    this.clientUI = clientUI;
    openConnection();  // Attempt immediate connection

    // Automatically send login to server once connected
    if (loginID != null) {
      sendToServer("#login " + loginID);
    }
  }

  //Instance methods ************************************************

  public void setLoginID(String id)
  {
    this.loginID = id;
  }

  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  public void handleMessageFromClientUI(String message)
  {
    try 
    {
      sendToServer(message);
    } 
    catch (IOException e) 
    {
      clientUI.display("Could not send message to server.");
      quit();
    }
  }

  /** LOGOFF: Disconnect from server but do NOT exit program. */
  public void logoff()
  {
    try 
    {
      closeConnection();
      clientUI.display("Connection closed.");
    } 
    catch (IOException e) 
    {
      clientUI.display("Error closing connection.");
    }
  }

  /** LOGIN: Reconnect to server AND resend login ID. */
  public void login()
  {
    try 
    {
      openConnection();
      clientUI.display("Connected to server.");
      if (loginID != null) {
        sendToServer("#login " + loginID);
      }
    } 
    catch (IOException e) 
    {
      clientUI.display("Could not connect to server.");
    }
  }

  /** Change host ONLY if NOT connected. */
  public void sethost(String host)
  {
    if (!isConnected())
      setHost(host);
    else
      clientUI.display("Error: Must be logged off before changing host.");
  }

  /** Change port ONLY if NOT connected. */
  public void setport(int port)
  {
    if (!isConnected())
      setPort(port);
    else
      clientUI.display("Error: Must be logged off before changing port.");
  }

  /** Display current host. */
  public String getHostInfo()
  {
    return getHost();
  }

  /** Display current port. */
  public int getPortInfo()
  {
    return getPort();
  }

  /** Quit client entirely. */
  public void quit()
  {
    try { closeConnection(); } catch(IOException e) {}
    System.exit(0);
  }
}
