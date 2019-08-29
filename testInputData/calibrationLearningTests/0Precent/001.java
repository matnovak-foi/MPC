import java.io.*;

class WatchDog {

	public int main(String args[]){
		Calls c = new Calls();
		
		c.retrieveFile("students/");
        c.retrieveFile("images/newcsitlogo.jpg");
        c.retrieveFile("images/rmitcsit.jpg");
        c.retrieveFile("images/helix.jpg");
		
		String checksum = c.getChecksum("index.html");
		String imgchecksum1 = c.getChecksum("newcsitlogo.jpg");
        String imgchecksum3 = c.getChecksum("rmitcsit.jpg");
        String imgchecksum5 = c.getChecksum("helix.jpg");

        System.out.println("Checksum of original file " +checksum);
        System.out.println("Checksum of image 1 " +imgchecksum1);
        System.out.println("Checksum of image 2" +imgchecksum3);
        System.out.println("Checksum of image 3" +imgchecksum5);
       }
    }
}