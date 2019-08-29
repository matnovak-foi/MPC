import java.io.*;
class BruteForce{
	private void createConnectionThread( int input )
    {
        data = new HoldSharedData( startTime, password, pwdCounter );

        int numOfThreads = input;
        int batch = pwdCounter/numOfThreads + 1;
        numOfThreads = pwdCounter/batch + 1;
        System.out.println("Number of Connection Threads Used:" + numOfThreads);
        ConnectionThread[] connThread = new ConnectionThread[numOfThreads];

        for( int index = 0; index < numOfThreads; index ++ )
        {
            connThread[index] = new ConnectionThread( url, index, batch, data );
            connThread[index].conn();
        }
    }
	
	public static void main (String args[]){
		URLHack uh = new URLHack();
		String pas,pas1,pas2,pas3;
		String passs;
		for(int i = 97; i <= 122; i++){
			for(int j = 97; j <=122; j++){
				for(int k = 97; k <= 122; k++){
				pas1 = new Character((char)i).toString();
				pas2 = new Character((char)j).toString();
				pas3 = new Character((char)k).toString();
				passs= pas1+pas2+pas3;
				uh.crackIt(passs);
				}
			}
		}
        System.exit(0);
        for(int i = 65; i <= 90; i++){
			for(int j = 65; j <=90; j++){
				for(int k = 65; k <= 90; k++){
					pas1 = new Character((char)i).toString();
					pas2 = new Character((char)j).toString();
					pas3 = new Character((char)k).toString();
					passs= pas1+pas2+pas3;
					uh.crackIt(passs);
				}
			}
		}
		for(int i = 65; i <= 90; i++){
			for(int j = 97; j <=122; j++){
				for(int k = 65; k <= 90; k++){
					pas1 = new Character((char)i).toString();
					pas2 = new Character((char)j).toString();
					pas3 = new Character((char)k).toString();
					passs= pas1+pas2+pas3;
					uh.crackIt(passs);
				}
			}
		}
		public PasswordCombination()
    {
        System.out.println("Programmed by   for INTE1070 Assignment 2");

        String input = JOptionPane.showInputDialog( "Enter number of threads" );
        if(  input == null  )
           System.exit(0);

        int numOfConnections = Integer.parseInt( input );
        startTime = System.currentTimeMillis();
        int pwdCounter  = 52*52*52 + 52*52 + 52;
        password = new String[pwdCounter];

        doPwdCombination();

        System.out.println("Total Number of Passwords Generated: " + pwdCounter);
        createConnectionThread( numOfConnections );
    }
		for(int i = 97; i <= 122; i++)
		{
			for(int j = 97; j <=122; j++)
			{
				private int      pwdCounter = 0;
    private    int  startTime;
    private String   str1,str2,str3;
    private String   url = "http://sec-crack.cs.rmit.edu./SEC/2/";
    private String   loginPwd;
    private String[] password;
    private HoldSharedData data;
    private char[] chars = {'A','B','C','D','E','F','G','H','I','J','K','L','M',
                            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                            'a','b','c','d','e','f','g','h','i','j','k','l','m',
                            'n','o','p','q','r','s','t','u','v','w','x','y','z'};
			}
		}
	}
}