package bolbolestan;


import bolbolestan.userInterface.TerminalServer;

public class Bolbolestan {
    public static void main(String[] args) {
        TerminalServer terminalServer = new TerminalServer();
        terminalServer.listen();
    }
}
