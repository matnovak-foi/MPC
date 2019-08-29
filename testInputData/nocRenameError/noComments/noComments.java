package org.foi.common.filesystem.file;
/*
	Komentari bla bla bla
	bla bla bla
*/
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import org.foi.mpc.MPCexcpetions;
import javax.swing.plaf.synth.SynthOptionPaneUI;

/*
	Komentari bla bla bla
	bla bla bla
*/
public class TextFileUtility {
	/*
	Komentari bla bla bla
	bla bla bla
*/
    public static class FileDoesNotExistException extends RuntimeException {}
	/*
	Komentari bla bla bla
	bla bla bla
*/
    public static class FileAlreadyExistException extends RuntimeException {} //fsafdsd
    public static class FileCreateException extends RuntimeException {//dsfasdf
        public FileCreateException(String message) {//adfsdf
            super(message);
        }
    }
	
	Charset charset;
	
	/*
	Komentari bla bla bla
	bla bla bla
*/
    public TextFileUtility(Charset charset) {
        this.charset = charset;
    }
	
/*
	Komentari bla bla bla
	bla bla bla
*/
    public File createFileWithText(File dir, String fileName, String text) throws IOException {
        if(dir == null || fileName == null)
            throw new MPCexcpetions.IsNullException();
		
		/*
	Komentari bla bla bla
	bla bla bla
*/
        if(fileName=="")//Gsdfgdfg
            throw new FileCreateException("File name can not be Empty string!");
		
		/*
	Komentari bla bla bla
	bla bla bla
*/
        File file = new File(dir.getPath()+File.separator+fileName);
        createFileWithText(file, text);//agfdsfgd
		//fsdfasdfas
        return file;
    }

	/*
	Komentari bla bla bla
	bla bla bla
*/
    public void createFileWithText(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
	/*
	Komentari bla bla bla
	bla bla bla
*/	
        createFileIfNotExists(file);
        writeTextToFile(file, text, StandardOpenOption.WRITE);
    }
    
	/*
	Komentari bla bla bla
	bla bla bla
*/
    public String readFileContentToString(File file) throws IOException {
        if(file == null)
            throw new MPCexcpetions.IsNullException();
		/*
	Komentari bla bla bla
	bla bla bla
*/
        BufferedReader reader = createBufferedReader(file);
		/*
	Komentari bla bla bla
	bla bla bla
*/
        return readFileContent(reader);
    }
	
    /*
	Komentari bla bla bla
	bla bla bla
*/
    public void appendTextToFile(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
		
		/*
	Komentari bla bla bla
	bla bla bla
*/
        else if(!file.exists())
            throw new FileDoesNotExistException();
        
        writeTextToFile(file, text, StandardOpenOption.APPEND);
    }

	/*
	Komentari bla bla bla
	bla bla bla
*/
    private void createFileIfNotExists(File file) {
		/*
	Komentari bla bla bla
	bla bla bla
*/
        if (!file.exists()) {
            createEmptyFile(file);
        } else {
            throw new FileAlreadyExistException();
        }
    }

	/*
	Komentari bla bla bla
	bla bla bla
*/
    private void createEmptyFile(File file) {
        try {
            Files.createFile(file.toPath(), new FileAttribute[]{});
        } catch (IOException ex) {
            throw new FileCreateException(ex.getMessage());
        }
    }

	/*
	Komentari bla bla bla
	bla bla bla
*/
    private void writeTextToFile(File file, String text, OpenOption... options) throws IOException {
        BufferedWriter bf = null;
		/*
	Komentari bla bla bla
	bla bla bla
*/
        try {
            bf = Files.newBufferedWriter(file.toPath(), charset, options);
            bf.write(text);
			/*
	Komentari bla bla bla
	bla bla bla
*/
        } finally {
            closeCreatedBufferedWriter(bf);
        }
    }

	/*
	Komentari bla bla bla
	bla bla bla
*/
    private void closeCreatedBufferedWriter(BufferedWriter bf) throws IOException {
        if (bf != null) {
            bf.close();
        }
    }
}