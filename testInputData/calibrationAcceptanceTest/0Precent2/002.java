import java.io.*;

class Dictionary{

public static void main(String args[]){
	try{
		
        File file = new File("words");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		URLHack uh = new URLHack();
		String line="";
		while((line = bf.readLine()) != null){
			if(line.length() <=3) {
                uh.crackIt(line);
		    }
        }

		catch(IOException ioe){
			System.out.println("Error: "+ioe);
		}
	}
	try{
		
        File file = new File("words");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		URLHack uh = new URLHack();
		String line="";
		while((line = bf.readLine()) != null){
			if(line.length() <=3) {
                uh.crackIt(line);
		    }
        }

		catch(IOException ioe){
			System.out.println("Error: "+ioe);
		}
	}
	try{
		
        File file = new File("words");
		FileReader fr = new FileReader(file);
		BufferedReader bf = new BufferedReader(fr);
		URLHack uh = new URLHack();
		String line="";
		while((line = bf.readLine()) != null){
			if(line.length() <=3) {
                uh.crackIt(line);
		    }
        }

		catch(IOException ioe){
			System.out.println("Error: "+ioe);
		}
	}
}